package com.taiji.knowledge.core.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.BsonField;
import com.taiji.knowledge.core.service.KnowledgeService;
import com.taiji.knowledge.core.vo.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by yunsama on 2019/6/13.
 */
@Service
public class KnowledgeServiceImpl implements KnowledgeService {

    /**
     * 字典集合-争议焦点
     */
    private static List<Document> disputeFocus = new ArrayList<>();
    /**
     * 字典集合-法律
     */
    private static List<Document> lawIndex = new ArrayList<>();
    /**
     * 字典集合-法条
     */
    private static List<Document> lawItem = new ArrayList<>();
    /**
     * 字典集合-款项
     */
    private static List<Document> lawSubitem = new ArrayList<>();
    /**
     * 字典集合-案由
     */
    private static List<Document> ayCollection = new ArrayList<>();
    /**
     * 线程池
     * 用于遍历SP_AJXX数据生成统计集合
     */
    public ThreadPoolExecutor executor = new ThreadPoolExecutor(32, 32, 1800, TimeUnit.HOURS,
            new LinkedBlockingQueue<>(50000));

    @Autowired
    private MongoTemplate mongoTemplate;

    @Cacheable(value = "countArgueTop5", key = "#ayCode")
    @Override
    public List<ArgueVo> countArgueTop5(String ayCode) {
        List<ArgueVo> list = new ArrayList<>();
//        String mql = "db.getCollection('SP_SAT3').aggregate([{$match:{'LAAY':'%s','SP_FLWS.WSNR_DIM.DISPUTE_FOCUS_LABEL_NAME':{$exists:true,$ne:''}}},{$unwind:'$SP_FLWS'},{$match:{'SP_FLWS.WSNR_DIM.DISPUTE_FOCUS_LABEL_NAME':{$exists:true,$ne:''}}},{$project:{_id:0,'LAAY':1,'FOCUS_LABEL_NAME':{$split:['$SP_FLWS.WSNR_DIM.DISPUTE_FOCUS_LABEL_NAME',';']}}},{$unwind:'$FOCUS_LABEL_NAME'},{$group:{_id:{LAAY:'$LAAY','FOCUS_LABEL_NAME':'$FOCUS_LABEL_NAME'},count:{$sum:1}}},{$project:{_id:0,'LAAY':'$_id.LAAY','FOCUS_LABEL_NAME':'$_id.FOCUS_LABEL_NAME','NUM':'$count'}},{ $sort : { LAAY:1,NUM : -1} }])";
        BasicDBObject match = new BasicDBObject("SP_FLWS.WSNR_DIM.DISPUTE_FOCUS_LABEL_NAME", new BasicDBObject("$exists", true).append("$ne", ""));
        if (!StringUtils.isEmpty(ayCode)) {
            match.append("LAAY", ayCode);
        }
        List<Bson> aggregations = Arrays.asList(
                Aggregates.match(match),
                Aggregates.unwind("$SP_FLWS"),
                Aggregates.project(new BasicDBObject("LAAY", 1).append("FOCUS_LABEL_NAME", new BasicDBObject("$split", Arrays.asList("$SP_FLWS.WSNR_DIM.DISPUTE_FOCUS_LABEL_NAME", ";")))),
                Aggregates.unwind("$FOCUS_LABEL_NAME"),
                Aggregates.group(new BasicDBObject("LAAY", "$LAAY").append("FOCUS_LABEL_NAME", "$FOCUS_LABEL_NAME"), new BsonField("count", new BasicDBObject("$sum", 1))),
                Aggregates.project(new BasicDBObject("_id", 0).append("LAAY", "$_id.LAAY").append("FOCUS_LABEL_NAME", "$_id.FOCUS_LABEL_NAME").append("NUM", "$count")),
                Aggregates.sort(new BasicDBObject("NUM", -1)),
                Aggregates.limit(5)
        );
        List<Document> retval = runMongoClientAggregate(aggregations, "SP_STATISTICS");
        for (Document document : retval) {
            ArgueVo av = new ArgueVo();
            av.setFrequency(document.getInteger("NUM"));
            av.setArgueContent(document.getString("FOCUS_LABEL_NAME"));
            av.setId(ayCode + "-" + document.getString("FOCUS_LABEL_NAME"));
            list.add(av);
        }
        return list;
    }
    @Cacheable(value = "countLawData", key = "#ayCode")
    @Override
    public List<LawVo> countLawData(String ayCode) {

        List<LawVo> list = new ArrayList<>();
//        String mql = "db.getCollection('SP_SAT3').aggregate([{$match:{'LAAY':'%s','SP_FLWS.LAW_INFO.SSFL_ID':{$exists:true,$ne:''},'SP_FLWS.LAW_INFO.SSTM_ID':{$exists:true,$ne:''}}},{$unwind:'$SP_FLWS'},{$match:{'SP_FLWS.LAW_INFO.SSFL_ID':{$exists:true,$ne:''},'SP_FLWS.LAW_INFO.SSTM_ID':{$exists:true,$ne:''}}},{$unwind:'$SP_FLWS.LAW_INFO'},{$group:{_id:{LAAY:'$LAAY','SSFL_ID':'$SP_FLWS.LAW_INFO.SSFL_ID','SSTM_ID':'$SP_FLWS.LAW_INFO.SSTM_ID'},count:{$sum:1}}},{$project:{_id:0,'LAAY':'$_id.LAAY','SSFL_ID':'$_id.SSFL_ID','SSTM_ID':'$_id.SSTM_ID','NUM':'$count'}},{$sort:{ NUM : -1}}])";
        BasicDBObject match = new BasicDBObject("SP_FLWS.LAW_INFO.SSFL_ID", new BasicDBObject("$exists", true).append("$ne", "")).append("SP_FLWS.LAW_INFO.SSTM_ID", new BasicDBObject("$exists", true).append("$ne", ""));
        if (!StringUtils.isEmpty(ayCode)) {
            match.append("LAAY", ayCode);
        }
        List<Bson> aggregations = Arrays.asList(
                Aggregates.match(match),
                Aggregates.unwind("$SP_FLWS"),
                Aggregates.unwind("$SP_FLWS.LAW_INFO"),
                Aggregates.group(new BasicDBObject("LAAY", "$LAAY").append("SSFL_ID", "$SP_FLWS.LAW_INFO.SSFL_ID").append("SSTM_ID", "$SP_FLWS.LAW_INFO.SSTM_ID"), new BsonField("count", new BasicDBObject("$sum", 1))),
                Aggregates.project(new BasicDBObject("_id", 0).append("LAAY", "$_id.LAAY").append("SSFL_ID", "$_id.SSFL_ID").append("SSTM_ID", "$_id.SSTM_ID").append("NUM", "$count")),
                Aggregates.sort(new BasicDBObject("NUM", -1)),
                Aggregates.limit(5)
        );
        List<Document> retval = runMongoClientAggregate(aggregations, "SP_STATISTICS");
        for (Document document : retval) {
            LawVo lv = new LawVo();
            lv.setFrequency(document.getInteger("NUM"));
            lv.setLaw(concatLawItem(document.getString("SSFL_ID"), document.getString("SSTM_ID")));
            lv.setId(document.getString("SSTM_ID"));
            list.add(lv);
        }
        return list;
    }

    @Cacheable(value = "countTotalDimension", key = "#dimensionCountQueryVo.toString()")
    @Override
    public List<CountVo> countTotalDimension(DimensionCountQueryVo dimensionCountQueryVo) {
        List<CountVo> list = new ArrayList<>();
//        String mql = "db.getCollection('SP_SAT3').aggregate([{$match:{'LAAY':'%s','SP_FLWS.WSNR_DIM.DISPUTE_FOCUS_LABEL_NAME':{$exists:true,$ne:''}}},{$unwind:'$SP_FLWS'},{$match:{'SP_FLWS.WSNR_DIM.DISPUTE_FOCUS_LABEL_NAME':{$exists:true,$ne:''}}},{$project:{_id:0,'LAAY':1,'FOCUS_LABEL_NAME':{$split:['$SP_FLWS.WSNR_DIM.DISPUTE_FOCUS_LABEL_NAME',';']}}},{$unwind:'$FOCUS_LABEL_NAME'},{$group:{_id:{LAAY:'$LAAY','FOCUS_LABEL_NAME':'$FOCUS_LABEL_NAME'},count:{$sum:1}}},{$project:{_id:0,'LAAY':'$_id.LAAY','FOCUS_LABEL_NAME':'$_id.FOCUS_LABEL_NAME','NUM':'$count'}},{ $sort : { LAAY:1,NUM : -1} }])";
//        List<Document> retval = runCommand(String.format(mql, dimensionCountQueryVo.getAyCode()));

        List<Bson> aggregations = Arrays.asList(
                Aggregates.match(new BasicDBObject("SP_FLWS.WSNR_DIM.DISPUTE_FOCUS_LABEL_NAME", new BasicDBObject("$exists", true).append("$ne", ""))),
                Aggregates.unwind("$SP_FLWS"),
                Aggregates.project(new BasicDBObject("LAAY", 1).append("FOCUS_LABEL_NAME", new BasicDBObject("$split", Arrays.asList("$SP_FLWS.WSNR_DIM.DISPUTE_FOCUS_LABEL_NAME", ";")))),
                Aggregates.unwind("$FOCUS_LABEL_NAME"),
                Aggregates.group(new BasicDBObject("LAAY", "$LAAY").append("FOCUS_LABEL_NAME", "$FOCUS_LABEL_NAME"), new BsonField("count", new BasicDBObject("$sum", 1))),
                Aggregates.project(new BasicDBObject("_id", 0).append("LAAY", "$_id.LAAY").append("FOCUS_LABEL_NAME", "$_id.FOCUS_LABEL_NAME").append("NUM", "$count")),
                Aggregates.sort(new BasicDBObject("NUM", -1))
        );
        List<Document> retval = runMongoClientAggregate(aggregations, "SP_STATISTICS");

        //统计争议各维度焦点
        int first = 0;
        int second = 0;
        int third = 0;
        for (Document document : retval) {
            Document fd = getDisputeFocus(document.getString("LAAY"), document.getString("FOCUS_LABEL_NAME"));
            String level = fd.getString("D_LEVEL");
            Integer num = document.getInteger("NUM");
            if ("1".equals(level)) {
                first = first + num;
            } else if ("2".equals(level)) {
                second = second + num;
            } else if ("3".equals(level)) {
                third = third + num;
            }
        }
        CountVo cv = new CountVo();
        cv.setName("一级维度");
        cv.setNum(first);
        list.add(cv);
        CountVo cv2 = new CountVo();
        cv2.setName("二级维度");
        cv2.setNum(second);
        list.add(cv2);
        CountVo cv3 = new CountVo();
        cv3.setName("三级维度");
        cv3.setNum(third);
        list.add(cv3);
        return list;
    }

    @Cacheable(value = "countAyDimension")
    @Override
    public List<AyDimensionVo> countAyDimension() {

        List<CountVo> list = new ArrayList<>();
//        String mql = "db.getCollection('SP_SAT3').aggregate([{$match:{'SP_FLWS.WSNR_DIM.DISPUTE_FOCUS_LABEL_NAME':{$exists:true,$ne:''}}},{$unwind:'$SP_FLWS'},{$match:{'SP_FLWS.WSNR_DIM.DISPUTE_FOCUS_LABEL_NAME':{$exists:true,$ne:''}}},{$project:{_id:0,'LAAY':1,'FOCUS_LABEL_NAME':{$split:['$SP_FLWS.WSNR_DIM.DISPUTE_FOCUS_LABEL_NAME',';']}}},{$unwind:'$FOCUS_LABEL_NAME'},{$group:{_id:{LAAY:'$LAAY','FOCUS_LABEL_NAME':'$FOCUS_LABEL_NAME'},count:{$sum:1}}},{$project:{_id:0,'LAAY':'$_id.LAAY','FOCUS_LABEL_NAME':'$_id.FOCUS_LABEL_NAME','NUM':'$count'}},{ $sort : { LAAY:1,NUM : -1} }])";
//        List<Document> retval = runCommand(mql);

        List<Bson> aggregations = Arrays.asList(
                Aggregates.match(new BasicDBObject("SP_FLWS.WSNR_DIM.DISPUTE_FOCUS_LABEL_NAME", new BasicDBObject("$exists", true).append("$ne", ""))),
                Aggregates.unwind("$SP_FLWS"),
                Aggregates.project(new BasicDBObject("LAAY", 1).append("FOCUS_LABEL_NAME", new BasicDBObject("$split", Arrays.asList("$SP_FLWS.WSNR_DIM.DISPUTE_FOCUS_LABEL_NAME", ";")))),
                Aggregates.unwind("$FOCUS_LABEL_NAME"),
                Aggregates.group(new BasicDBObject("LAAY", "$LAAY").append("FOCUS_LABEL_NAME", "$FOCUS_LABEL_NAME"), new BsonField("count", new BasicDBObject("$sum", 1))),
                Aggregates.project(new BasicDBObject("_id", 0).append("LAAY", "$_id.LAAY").append("FOCUS_LABEL_NAME", "$_id.FOCUS_LABEL_NAME").append("NUM", "$count")),
                Aggregates.sort(new BasicDBObject("NUM", -1))
        );
        List<Document> retval = runMongoClientAggregate(aggregations, "SP_STATISTICS");
        //统计争议各维度焦点
        return getAyDimensionFocus(retval);
    }

    @Cacheable(value = "getDimensionData", key = "#dimension.toString()")
    @Override
    public List<DimensionVo> getDimensionData(DimensionCountQueryVo dimension) throws Exception {

        List<DimensionVo> list = new ArrayList<>();
//        String mql = "db.getCollection('TB_DICT_FOCUS_AY').aggregate([{$match:{'AYDM':'%s'}}])";

//        List<Bson> aggregations = Arrays.asList(
//                Aggregates.match(new BasicDBObject("AYDM", dimension.getAyCode()))
//        );
//        List<Document> retval = runMongoClientAggregate(aggregations, "TB_DICT_FOCUS_AY");
        List<Document> retval = findDisputeFocus(dimension.getAyCode());
        for (Document document : retval) {
            if (dimension.getType() != null && dimension.getType() == 1) {
                DimensionVo dv = new DimensionVo();
                dv.setId(document.getString("DID"));
                dv.setDimension(document.getString("D_NAME"));
                list.add(dv);
            } else if (dimension.getType() != null && dimension.getType() == 2) {
                DimensionVo dv = new DimensionVo();
                dv.setId(document.getString("DID"));
                dv.setDimension(document.getString("D_NAME"));
                list.add(dv);

            } else if (dimension.getType() != null && dimension.getType() == 3) {
                if ("1".equals(document.getString("D_LEVEL"))) {
                    DimensionVo dv = new DimensionVo();
                    dv.setId(document.getString("DID"));
                    dv.setDimension(document.getString("D_NAME"));
                    list.add(dv);
                }
            } else if (dimension.getType() != null && dimension.getType() == 4) {
                if ("2".equals(document.getString("D_LEVEL")) && dimension.getDimensionId().equals(document.getString("PID"))) {
                    DimensionVo dv = new DimensionVo();
                    dv.setId(document.getString("DID"));
                    dv.setDimension(document.getString("D_NAME"));
                    list.add(dv);
                }
            } else if (dimension.getType() != null && dimension.getType() == 5) {
                if ("3".equals(document.getString("D_LEVEL")) && dimension.getDimensionId().equals(document.getString("PID"))) {
                    DimensionVo dv = new DimensionVo();
                    dv.setId(document.getString("DID"));
                    dv.setDimension(document.getString("D_NAME"));
                    list.add(dv);
                }
            } else {
                if ("1".equals(document.getString("D_LEVEL"))) {
                    DimensionVo dv = new DimensionVo();
                    dv.setId(document.getString("DID"));
                    dv.setDimension(document.getString("D_NAME"));
                    list.add(dv);
                }
            }
        }
        return list;
    }

    private List<AyDimensionVo> getAyDimensionFocus(List<Document> docs) {
        List<AyDimensionVo> list = new ArrayList<>();
        //根据ayCode分组
        Map<String, List<Document>> groupMap = new HashMap<>();
        for (Document doc : docs) {
            String ay = doc.getString("LAAY");
            if (groupMap.containsKey(ay)) {
                groupMap.get(ay).add(doc);
            } else {
                groupMap.put(ay, new ArrayList<>(Collections.singletonList(doc)));
            }
        }
        //统计争议各维度焦点
        for (String ay : groupMap.keySet()) {
            List<Document> focus = groupMap.get(ay);
            int first = 0;
            int second = 0;
            int third = 0;
            for (Document doc : focus) {
                Document fd = getDisputeFocus(ay, doc.getString("FOCUS_LABEL_NAME"));
                String level = fd.getString("D_LEVEL");
                Integer num = doc.getInteger("NUM");
                if ("1".equals(level)) {
                    first = first + num;
                } else if ("2".equals(level)) {
                    second = second + num;
                } else if ("3".equals(level)) {
                    third = third + num;
                }
            }
            AyDimensionVo ayDimensionVo = new AyDimensionVo();
            ayDimensionVo.setAyCode(ay);
            ayDimensionVo.setAy(getAyName(ay));
            ayDimensionVo.setNum1(first);
            ayDimensionVo.setNum2(second);
            ayDimensionVo.setNum3(third);
            list.add(ayDimensionVo);
        }
        return list;
    }

    private List<Document> findDisputeFocus(String ayCode) {
        if (disputeFocus.isEmpty()) {
            disputeFocus = findDictionaryCollection("TB_DICT_FOCUS_AY");
        }
        if (!StringUtils.isEmpty(ayCode)) {

            return disputeFocus.stream().filter(d -> d.getString("AYDM").equals(ayCode)).collect(Collectors.toList());
        } else {
            return disputeFocus;
        }
    }

    private String getAyName(String ayCode) {
        if (ayCollection.isEmpty()) {
            ayCollection = findDictionaryCollection("TB_DICT_AY");
        }
        Document focusDocument = ayCollection.stream().filter(d -> d.getString("AY").equals(ayCode)).findFirst().orElse(null);
        return focusDocument == null ? "" : focusDocument.getString("NAME");
    }

    private Document getDisputeFocus(String ayCode, String disputeFocusName) {
        if (disputeFocus.isEmpty()) {
            disputeFocus = findDictionaryCollection("TB_DICT_FOCUS_AY");
        }
        Document focusDocument = disputeFocus.stream().filter(d -> d.getString("AYDM").equals(ayCode) && d.getString("D_NAME").equals(disputeFocusName)).findFirst().orElse(null);
        return focusDocument == null ? new Document() : focusDocument;
    }

    private List<Document> runCommand(String mql) {
        BasicDBObject bson = new BasicDBObject();
        bson.put("$eval", mql);
        Document object = mongoTemplate.getDb().runCommand(bson);
        Document retval = (Document) object.get("retval");
        return (List<Document>) retval.get("_batch");
    }

    private List<Document> runMongoClientAggregate(List<Bson> aggregation, String collection) {
        List<Document> list = new LinkedList<>();
        AggregateIterable<Document> retval = mongoTemplate.getCollection(collection).aggregate(aggregation);
        for (Document document : retval) {
            list.add(document);
        }
        return list;
    }

    /**
     * 显示名称 《中华人民共和国民事诉讼法》第一百四十四条
     *
     * @param ssflId
     * @param sstmId
     * @return
     */
    private String concatLawItem(String ssflId, String sstmId) {
        if (lawIndex.isEmpty()) {
            lawIndex = findDictionaryCollection("TB_LAW_INDEX_NATION");
            lawItem = findDictionaryCollection("TB_LAW_ITEM_NATION");
        }
        FindIterable<Document> indexIt = mongoTemplate.getCollection("TB_LAW_INDEX_NATION").find(new BasicDBObject("OBJ_ID", ssflId));
        Document index = null;
        for (Document document : indexIt) {
            index = document;
            break;
        }
        FindIterable<Document> itemIt = mongoTemplate.getCollection("TB_LAW_ITEM_NATION").find(new BasicDBObject("OBJ_ID", sstmId));
        Document item = null;
        for (Document document : itemIt) {
            item = document;
            break;
        }
//        Document index = lawIndex.stream().filter(d -> d.getString("OBJ_ID").equals(ssflId)).findFirst().orElse(null);
//        Document item = lawItem.stream().filter(d -> d.getString("OBJ_ID").equals(sstmId)).findFirst().orElse(null);
        return String.valueOf(index == null ? "" : index.getString("BT")) + " " + String.valueOf(item == null ? "" : item.getString("TM"));
    }

    /**
     * 获取字典表数据
     *
     * @param collectionName
     * @return
     */
    private List<Document> findDictionaryCollection2(String collectionName) {
        String mql = "db.getCollection('%s').find()";
        List<Document> retval = runCommand(String.format(mql, collectionName));
        return retval;
    }

    /**
     * 获取字典表数据
     *
     * @param collectionName
     * @return
     */
    private List<Document> findDictionaryCollection(String collectionName) {
        List<Document> retval = new ArrayList<>();
        FindIterable<Document> list = mongoTemplate.getCollection(collectionName).find();
        for (Document document : list) {
            retval.add(document);
        }
        return retval;
    }
}
