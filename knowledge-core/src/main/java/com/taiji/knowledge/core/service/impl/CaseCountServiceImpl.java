package com.taiji.knowledge.core.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.BsonField;
import com.taiji.knowledge.core.service.CaseCountService;
import com.taiji.knowledge.core.vo.CaseCountQueryVo;
import com.taiji.knowledge.core.vo.CountVo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yunsama on 2019/6/13.
 */
@Service
public class CaseCountServiceImpl implements CaseCountService {
    /**
     * 字典集合-案由
     */
    private static List<Document> ayCollection = new ArrayList<>();
    @Autowired
    private MongoTemplate mongoTemplate;


    @Cacheable(value = "countCaseNum", key = "#queryVo.toString()")
    @Override
    public int countCaseNum(CaseCountQueryVo queryVo) throws Exception {
        List<Bson> aggregations = new ArrayList<>();
        BasicDBObject match = new BasicDBObject("CBFY", new BasicDBObject("$exists", true));
        if (!StringUtils.isEmpty(queryVo.getFjm())) {
            match.append("CBFY", queryVo.getFjm());
        }
        if (!StringUtils.isEmpty(queryVo.getLarqStart())) {
            match.append("LARQ",
                    new BasicDBObject("$gte", DateFormatUtils.format(queryVo.getLarqStart(), "yyyy-MM-dd"))
                            .append("$lt", DateFormatUtils.format(queryVo.getLarqEnd(), "yyyy-MM-dd")));
        }
        if (!StringUtils.isEmpty(queryVo.getJarqStart())) {
            match.append("JARQ",
                    new BasicDBObject("$gte", queryVo.getJarqStart())
                            .append("$lt", queryVo.getJarqEnd()));
        }

        if (!StringUtils.isEmpty(queryVo.getCaseStateStart())) {
            match.append("AJZT",
                    new BasicDBObject("$gte", queryVo.getCaseStateStart())
                            .append("$lt", queryVo.getCaseStateEnd()));
        }
        if ("0300".equals(queryVo.getStandardStart()) && "0400".equals(queryVo.getStandardEnd())) {
            //民事
            match.append("AJLB", "2");
        } else if ("0100".equals(queryVo.getStandardStart()) && "0300".equals(queryVo.getStandardEnd())) {
            //刑事
            match.append("AJLB", "1");
        } else if ("0400".equals(queryVo.getStandardStart()) && "0500".equals(queryVo.getStandardEnd())) {
            //行政
            match.append("AJLB", "6");
        }
        aggregations.add(Aggregates.match(match));
        aggregations.add(Aggregates.count());
        List<Document> retval = runMongoClientAggregate(aggregations, "SP_STATISTICS");

        return retval.isEmpty() ? 0 : retval.get(0).getInteger("count");
    }

    @Cacheable(value = "countAyRank", key = "#queryVo.toString()")
    @Override
    public List<CountVo> countAyRank(CaseCountQueryVo queryVo) throws Exception {
        List<CountVo> list = new ArrayList<>();
        List<Bson> aggregations = new ArrayList<>();
        BasicDBObject match = new BasicDBObject("CBFY", new BasicDBObject("$exists", true));
        if (!StringUtils.isEmpty(queryVo.getFjm())) {
            match.append("CBFY", queryVo.getFjm());
        }
        if (!StringUtils.isEmpty(queryVo.getLarqStart())) {
            match.append("LARQ",
                    new BasicDBObject("$gte", DateFormatUtils.format(queryVo.getLarqStart(), "yyyy-MM-dd"))
                            .append("$lt", DateFormatUtils.format(queryVo.getLarqEnd(), "yyyy-MM-dd")));
        }
        if (!StringUtils.isEmpty(queryVo.getJarqStart())) {
            match.append("JARQ",
                    new BasicDBObject("$gte", queryVo.getJarqStart())
                            .append("$lt", queryVo.getJarqEnd()));
        }

        if (!StringUtils.isEmpty(queryVo.getCaseStateStart())) {
            match.append("AJZT",
                    new BasicDBObject("$gte", queryVo.getCaseStateStart())
                            .append("$lt", queryVo.getCaseStateEnd()));
        }
        if ("0300".equals(queryVo.getStandardStart()) && "0400".equals(queryVo.getStandardEnd())) {
            //民事
            match.append("AJLB", "2");
        } else if ("0100".equals(queryVo.getStandardStart()) && "0300".equals(queryVo.getStandardEnd())) {
            //刑事
            match.append("AJLB", "1");
        } else if ("0400".equals(queryVo.getStandardStart()) && "0500".equals(queryVo.getStandardEnd())) {
            //行政
            match.append("AJLB", "6");
        }
        aggregations.add(Aggregates.match(match));
        aggregations.add(Aggregates.group(new BasicDBObject("LAAY", "$LAAY"), new BsonField("count", new BasicDBObject("$sum", 1))));
        aggregations.add(Aggregates.project(new BasicDBObject("_id", 0).append("LAAY", "$_id.LAAY").append("count", "$count")));
        aggregations.add(Aggregates.sort(new BasicDBObject("count", -1)));
        aggregations.add(Aggregates.limit(5));
        List<Document> retval = runMongoClientAggregate(aggregations, "SP_STATISTICS");
        for (Document document : retval) {
            CountVo countVo = new CountVo();
            countVo.setName(getAyName(document.get("LAAY")));
            countVo.setValue(String.valueOf(document.get("LAAY")));
            countVo.setNum(document.getInteger("count"));
            list.add(countVo);
        }
        return list;
    }

    private String getAyName(Object ayCode) {
        if (ayCollection.isEmpty()) {
            ayCollection = findDictionaryCollection("TB_DICT_AY");
        }
        Document focusDocument = ayCollection.stream().filter(d -> d.getString("AY").equals(String.valueOf(ayCode))).findFirst().orElse(null);
        return focusDocument == null ? "" : focusDocument.getString("NAME");
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
     * 获取字典表数据
     *
     * @param collectionName
     * @return
     */
    private List<Document> findDictionaryCollection(String collectionName) {
        List<Document> retval = new ArrayList<>();
        FindIterable<Document> list = mongoTemplate.getCollection(collectionName).find().batchSize(Integer.MAX_VALUE);
        for (Document document : list) {
            retval.add(document);
        }
        return retval;
    }
}
