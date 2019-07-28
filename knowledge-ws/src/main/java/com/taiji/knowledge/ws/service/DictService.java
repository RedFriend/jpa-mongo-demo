package com.taiji.knowledge.ws.service;


import com.taiji.knowledge.ws.vo.WsDict;

import java.util.List;

public interface DictService {

    List<WsDict> findByType(String type);

    WsDict getByType(String type);
}
