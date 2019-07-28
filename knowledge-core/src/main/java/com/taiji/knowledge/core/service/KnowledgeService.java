package com.taiji.knowledge.core.service;


import com.taiji.knowledge.core.vo.*;

import java.util.List;

/**
 * Created by yunsama on 2019/5/23.
 */
public interface KnowledgeService {

    /**
     * 根据案由代码统计争议焦点top5
     * @param ayCode 案由代码
     * @return
     */
    public List<ArgueVo> countArgueTop5(String ayCode) throws Exception;


    /**
     * 统计争议点查询法条
     * @param ayCode   案由代码
     * @return
     * @throws Exception
     */
    public List<LawVo> countLawData(String ayCode) throws Exception;

    /**
     * 统计各维度总数
     * @param record
     * @return
     * @throws Exception
     */
    public List<CountVo> countTotalDimension(DimensionCountQueryVo record) throws Exception;

    /**
     * 统计案由各维度数top 7
     * @return
     * @throws Exception
     */
    public List<AyDimensionVo> countAyDimension() throws Exception;

    /**
     * 查询维度明细
     * @param record
     * @return
     * @throws Exception
     */
    public List<DimensionVo> getDimensionData(DimensionCountQueryVo record) throws Exception;

}
