package com.taiji.knowledge.core.service;

import com.taiji.knowledge.core.vo.CaseCountQueryVo;
import com.taiji.knowledge.core.vo.CountVo;

import java.util.List;

/**
 * Created by yunsama on 2019/6/13.
 */
public interface CaseCountService {

    /***
     * 统计各类型案件 新收 结案 旧存
     * @param queryVo
     * @return
     */
    public int countCaseNum(CaseCountQueryVo queryVo) throws Exception;

    /***
     * 统计全院前12类案由
     * @param queryVo 传立案开始时间  结束时间  法院代码或分级码
     * @return
     */
    public List<CountVo> countAyRank(CaseCountQueryVo queryVo) throws Exception;
}
