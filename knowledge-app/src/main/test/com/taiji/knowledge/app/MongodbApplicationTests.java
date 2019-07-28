package com.taiji.knowledge.app;

import com.taiji.knowledge.core.service.CaseCountService;
import com.taiji.knowledge.core.service.KnowledgeService;
import com.taiji.knowledge.core.service.job.StatisticsJob;
import com.taiji.knowledge.core.vo.CaseCountQueryVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MongodbApplicationTests {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private StatisticsJob statisticsJob;
    @Autowired
    private KnowledgeService knowledgeService;
    @Autowired
    private CaseCountService caseCountService;

    @Test
    public void test() throws Exception {
//        statisticsJob.generateStatisticsCollection();
//        knowledgeService.countLawData("9181");
//        knowledgeService.countAyDimension();
//        DimensionCountQueryVo q=new DimensionCountQueryVo();
//        q.setAyCode("9181");
//        q.setType(5);
//        knowledgeService.getDimensionData(q);
        CaseCountQueryVo queryVo = new CaseCountQueryVo();
        queryVo.setFjm("J30");
//        queryVo.setStandardStart("0300");
//        queryVo.setStandardEnd("0400");
//        queryVo.setCaseStateStart("60");
//        queryVo.setCaseStateEnd("80");
        caseCountService.countAyRank(queryVo);
//        caseCountService.countCaseNum(queryVo);
//        DimensionCountQueryVo q=new DimensionCountQueryVo();
//        q.setAyCode("9722");
//        knowledgeService.countTotalDimension(q);
//        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 创建统计集合
     *
     * @throws Exception
     */
    @Test
    public void generateStatisticsCollection() throws Exception {
        statisticsJob.generateStatisticsCollection();
    }
}
