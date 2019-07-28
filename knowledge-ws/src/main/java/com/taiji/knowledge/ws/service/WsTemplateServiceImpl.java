package com.taiji.knowledge.ws.service;

import com.taiji.knowledge.core.vo.CountVo;
import com.taiji.knowledge.ws.mapper.SqlMapper;
import com.taiji.knowledge.ws.service.DictService;
import com.taiji.knowledge.ws.service.WsTemplateService;
import com.taiji.knowledge.ws.vo.WsTemplateVo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yunsama on 2019/6/13.
 */
@Service
public class WsTemplateServiceImpl implements WsTemplateService {
    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private DictService dictService;

    @Override
    public List<CountVo> countWsNum() {
        String sql = dictService.getByType("文书统计-按模板类型").getRemark();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        return sqlMapper.selectList(sql, CountVo.class);
    }

    @Override
    public List<WsTemplateVo> getWsTemplate(int i) {
        String caseType = "03";
        if (i == 1) {
            //民事
            caseType = "03";
        } else if (i == 2) {
            //刑事
            caseType = "02";
        } else if (i == 3) {
            //行政
            caseType = "04";
        }

        String sql = dictService.getByType("文书统计-按模模板使用情况").getRemark();
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        return sqlMapper.selectList(sql, caseType, WsTemplateVo.class);
    }
}
