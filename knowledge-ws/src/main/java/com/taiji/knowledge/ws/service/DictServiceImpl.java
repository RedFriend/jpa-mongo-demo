package com.taiji.knowledge.ws.service;

import com.taiji.knowledge.ws.mapper.SqlMapper;
import com.taiji.knowledge.ws.vo.WsDict;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictServiceImpl implements DictService {
    @Autowired
    private SqlSession sqlSession;

    @Override
    public List<WsDict> findByType(String type) {
        String sql = "SELECT * FROM `t_ws_dict` where type=#{type}";
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        return sqlMapper.selectList(sql, type, WsDict.class);
    }

    @Override
    public WsDict getByType(String type) {
        String sql = "SELECT * FROM `t_ws_dict` where type=#{type}";
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        return sqlMapper.selectOne(sql, type, WsDict.class);
    }
}
