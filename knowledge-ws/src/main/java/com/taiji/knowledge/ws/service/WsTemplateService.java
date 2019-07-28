package com.taiji.knowledge.ws.service;


import com.taiji.knowledge.core.vo.CountVo;
import com.taiji.knowledge.ws.vo.WsTemplateVo;

import java.util.List;

/**
 * Created by yunsama on 2019/5/24.
 */
public interface WsTemplateService {

    /***
     * 统计民事、刑事、行政文书模板数
     * @return
     */
    public List<CountVo> countWsNum() throws Exception;


    /***
     * 文书
     * @param type
     * @return
     */
    public List<WsTemplateVo> getWsTemplate(int type) throws Exception;
}
