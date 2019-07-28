package com.taiji.knowledge.core.service.job;

import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.model.Aggregates;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 处理mongoDB中SP_AJXX到较小的映射集合SP_STA,用来统计数据
 *
 * @author penghongyou
 */
@Component
public class StatisticsJob {

    public ThreadPoolExecutor executor = new ThreadPoolExecutor(96, 96, 1800, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(5000));
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 生成SP_AJXX的数据统计的集合
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 0 23 ? * *")
    public void generateStatisticsCollection() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1);
        Date startDate = DateUtils.parseDate("2010-01-01", "yyyy-MM-dd");
        Date now = new Date();
        cal.setTime(startDate);
        cal.get(Calendar.YEAR);

        //按立案时间查询数据,LARQ字段已建立索引
        String collectionName = "SP_STATISTICS";
        mongoTemplate.dropCollection(collectionName);
        int i = 0;
        while (startDate.before(now)) {
            String sd = DateFormatUtils.format(startDate, "yyyy-MM-dd");
            startDate = DateUtils.addDays(startDate, 1);
            String ed = DateFormatUtils.format(startDate, "yyyy-MM-dd");
            List<Bson> aggregations = Arrays.asList(Aggregates.match(new BasicDBObject("LARQ", new BasicDBObject("$gte", sd).append("$lt", ed))),
                    Aggregates.project(new BasicDBObject("AJID", 1)
                            .append("LAAY", 1)
                            .append("LARQ", 1)
                            .append("JARQ", 1)
                            .append("CBFY", 1)
                            .append("AJLB", 1)
                            .append("SPCX", 1)
                            .append("AJZT", 1)
                            .append("MODI_DATE", 1)
                            .append("SP_FLWS.LAW_INFO.FL", 1)
                            .append("SP_FLWS.LAW_INFO.TM", 1)
                            .append("SP_FLWS.LAW_INFO.SSFL_ID", 1)
                            .append("SP_FLWS.LAW_INFO.SSTM_ID", 1)
                            .append("SP_FLWS.WSNR_DIM.CASE_DIMENSION_LABEL_NAME", 1)
                            .append("SP_FLWS.WSNR_DIM.DISPUTE_FOCUS_LABEL_NAME", 1)
                    ));
            executor.execute(() -> saveStatisticsData(runMongoClientAggregate(aggregations, "SP_AJXX"), collectionName)
            );
            i++;
        }
        System.out.println("启动获取数据线程数" + i);
    }

    /**
     * 发现返回的集合只有101条的限制,目前还未找到修改限制的方式
     * 暂时不适用
     *
     * @throws Exception
     */
    @Deprecated
    public void generateStatisticsCollection2() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1);
        Date startDate = DateUtils.parseDate("2010-01-01", "yyyy-MM-dd");
        Date now = new Date();
        cal.setTime(startDate);
        cal.get(Calendar.YEAR);

        List<Map<String, Map<String, Integer>>> list = new ArrayList<>();
        //按立案时间查询数据,LARQ字段已建立索引
        String sql = "db.getCollection('SP_AJXX').aggregate([{$match:{'LARQ':{$gte:'%s',$lt:'%s'}}},{$project:{_id:0,AJID: 1,LAAY: 1,LARQ: 1,JARQ:1,CBFY:1,AJLB:1,SPCX:1,AJZT:1,'MODI_DATE': 1,'SP_FLWS.LAW_INFO.FL': 1,'SP_FLWS.LAW_INFO.TM': 1,'SP_FLWS.LAW_INFO.SSFL_ID': 1,'SP_FLWS.LAW_INFO.SSTM_ID': 1,'SP_FLWS.WSNR_DIM.CASE_DIMENSION_LABEL_NAME': 1,'SP_FLWS.WSNR_DIM.DISPUTE_FOCUS_LABEL_NAME': 1}}])";
        String collectionName = "SP_STATISTICS_" + DateFormatUtils.format(now, "yyyy-MM-dd");
        while (startDate.before(now)) {
            String sd = DateFormatUtils.format(startDate, "yyyy-MM-dd");
            startDate = DateUtils.addDays(startDate, 5);
            String ed = DateFormatUtils.format(startDate, "yyyy-MM-dd");
            String mql = String.format(sql, sd, ed);
            System.out.println(mql);
            executor.execute(() -> saveStatisticsData(runCommand(mql), collectionName)
            );
        }
        //TODO
        Thread.sleep(Integer.MAX_VALUE);
    }

    private void saveStatisticsData(List<Document> list, String collectionName) {
        System.out.println(Thread.currentThread().getName() + "  " + list.size());
        if (!list.isEmpty()) {
            synchronized (this) {
                if (!mongoTemplate.collectionExists(collectionName)) {
                    mongoTemplate.createCollection(collectionName);
                }
            }
            mongoTemplate.getDb().getCollection(collectionName).insertMany(list);
        }
    }


    private List<Document> runCommand(String mql) {
        BasicDBObject bson = new BasicDBObject();
        bson.put("$eval", mql);
        bson.put("nolock", true);
        Document object = mongoTemplate.getDb().runCommand(bson);
        Document retval = (Document) object.get("retval");
        return (List<Document>) retval.get("_batch");
    }

    private List<Document> runSpringDataAggregate(Aggregation aggregation, String collection) {
        List<Document> list = new LinkedList<>();
        AggregationResults<Document> ar = mongoTemplate.aggregate(aggregation, collection, Document.class);
        for (Document document : ar.getMappedResults()) {
            list.add(document);
        }
        return list;
    }

    private List<Document> runMongoClientAggregate(List<Bson> aggregation, String collection) {
        List<Document> list = new LinkedList<>();
        AggregateIterable<Document> retval = mongoTemplate.getCollection(collection).aggregate(aggregation);
        for (Document document : retval) {
            list.add(document);
        }
        return list;
    }
}
