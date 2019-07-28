package com.taiji.knowledge.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.taiji.common.util.CommonUtil;
import com.taiji.common.util.ResultVo;
import com.taiji.knowledge.app.util.DateUtil;
import com.taiji.knowledge.core.service.CaseCountService;
import com.taiji.knowledge.core.service.KnowledgeService;
import com.taiji.knowledge.core.vo.*;
import com.taiji.knowledge.ws.service.WsTemplateService;
import com.taiji.knowledge.ws.vo.WsTemplateVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by yunsama on 2019/5/21.
 */
@Controller
@RequestMapping("/knowledge")
public class KnowledgeBaseController {

    Logger logger = LoggerFactory.getLogger(KnowledgeBaseController.class);

    @Autowired
    CaseCountService caseCountService;

    @Autowired
    KnowledgeService knowledgeService;

    @Autowired
    WsTemplateService wsTemplateService;

    @RequestMapping("caseDimensionPage")
    public String caseDimensionPage(Model model, HttpServletRequest request) throws Exception {
        int civilNum = 0;
        int criminalNum = 0;
        int administrationNum = 0;
        List<CountVo> list = null;
        List<ArgueVo> argueVos = null;
        List<LawVo> lawVos = null;
        String caseReaso = "";
        String ayCode = "";
        CaseCountQueryVo caseCountQueryVo = new CaseCountQueryVo();
        caseCountQueryVo.setLarqStart(DateUtils.parseDate("2010-01-01","yyyy-MM-dd"));
        caseCountQueryVo.setLarqEnd(new Date());
        caseCountQueryVo.setStandardStart("0300");
        caseCountQueryVo.setStandardEnd("0400");
        try {
            list = caseCountService.countAyRank(caseCountQueryVo);  //案由
            civilNum = caseCountService.countCaseNum(caseCountQueryVo); //民事
            caseCountQueryVo.setStandardStart("0100");
            caseCountQueryVo.setStandardEnd("0300");
            criminalNum = caseCountService.countCaseNum(caseCountQueryVo); //刑事
            caseCountQueryVo.setStandardStart("0400");
            caseCountQueryVo.setStandardEnd("0500");
            administrationNum = caseCountService.countCaseNum(caseCountQueryVo);//行政
            if (CommonUtil.isNotNull(list)) {
                caseReaso = list.get(1).getName();
                ayCode = list.get(1).getValue();
            }
        } catch (Exception e) {
            logger.error("统计各类型案件异常：", e);
        }
        int sum = civilNum + criminalNum + administrationNum;
        model.addAttribute("civilNum", civilNum);
        model.addAttribute("criminalNum", criminalNum);
        model.addAttribute("administrationNum", administrationNum);
        model.addAttribute("sum", sum);
        model.addAttribute("countList", JSONObject.toJSONString(list));
        model.addAttribute("ayCode", ayCode);
        model.addAttribute("caseReaso", caseReaso);
        return "knowledgeBase/caseDimension";
    }

    /**
     * 统计（民事，刑事。行政）案件数
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("findCaseCountByDate")
    public Map<String, Integer> findCaseCountByDate(HttpServletRequest request) {
        Map<String, Integer> map = new HashMap<>();
        String overall = request.getParameter("overall");
        if (CommonUtil.isNotNull(overall)) {
            CaseCountQueryVo vo = getCaseTypeAndDate(overall, null);
            int civilNum = 0;
            int criminalNum = 0;
            int administrationNum = 0;
            CaseCountQueryVo caseCountQueryVo = new CaseCountQueryVo();
            caseCountQueryVo.setLarqStart(vo.getLarqStart());  //开始日期
            caseCountQueryVo.setLarqEnd(vo.getLarqEnd());    //结束日期
            caseCountQueryVo.setStandardStart("0300");
            caseCountQueryVo.setStandardEnd("0400");
            try {
                civilNum = caseCountService.countCaseNum(caseCountQueryVo);
                map.put("civil", civilNum);
                caseCountQueryVo.setStandardStart("0100");
                caseCountQueryVo.setStandardEnd("0300");
                criminalNum = caseCountService.countCaseNum(caseCountQueryVo);
                map.put("criminal", criminalNum);
                caseCountQueryVo.setStandardStart("0400");
                caseCountQueryVo.setStandardEnd("0500");
                administrationNum = caseCountService.countCaseNum(caseCountQueryVo);
                map.put("administration", administrationNum);
            } catch (Exception e) {
                logger.error("统计各类型案件异常：", e);
            }
            int sum = civilNum + criminalNum + administrationNum;
            map.put("sum", sum);
        }
        return map;
    }

    /**
     * 根据案件类型统计案由数
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("findCaseCaseReasonInfoByCaseType")
    public ResultVo findCaseCaseReasonInfoByCaseType(HttpServletRequest request) {
        ResultVo resultVo = new ResultVo();
        String caseType = request.getParameter("caseType");
        if (CommonUtil.isNotNull(caseType)) {
            CaseCountQueryVo vo = getCaseTypeAndDate(null, caseType);
            CaseCountQueryVo caseCountQueryVo = new CaseCountQueryVo();
            caseCountQueryVo.setStandardStart(vo.getStandardStart());
            caseCountQueryVo.setStandardEnd(vo.getStandardEnd());
            try {
                List<CountVo> list = caseCountService.countAyRank(caseCountQueryVo);
                resultVo.setSuccess(true);
                resultVo.setSubject(list);
            } catch (Exception e) {
                logger.error("统计各类型案由异常：", e);
            }
        }
        return resultVo;
    }

    /**
     * 根据案由获取前五个争议焦点及法条
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("ggetArgueAndReasonAndLawByAy")
    public ArgueAndLawVo ggetArgueAndReasonAndLawByAy(HttpServletRequest request) {
        String aydm = request.getParameter("aydm");
        ArgueAndLawVo argueAndLawVo = new ArgueAndLawVo();
        try {
            List<ArgueVo> argueVos = knowledgeService.countArgueTop5(aydm);
            List<LawVo> lawVos = knowledgeService.countLawData(aydm);
            argueAndLawVo.setArgueVos(argueVos);
            argueAndLawVo.setLawVos(lawVos);
            argueAndLawVo.setAyCode(aydm);
        } catch (Exception e) {
            logger.error("根据案由查前五个争议焦点/法条异常：", e);
        }
        return argueAndLawVo;
    }


    @RequestMapping("retrievalPage")
    public String retrievalPage(Model model, HttpServletRequest request) {
        DimensionCountQueryVo vo = new DimensionCountQueryVo();
        vo.setStartDate(DateUtil.getCurrentQuarterStartDate());//开始日期
        vo.setEndDate(DateUtil.getCurrentQuarterEndDate());//结束日期
        vo.setType(1);
        try {
            List<CountVo> countlist = knowledgeService.countTotalDimension(vo);   //默认查询本季度的数据
            model.addAttribute("countlist", JSONObject.toJSONString(countlist));
            List<AyDimensionVo> ayDimensionVos = knowledgeService.countAyDimension();
            model.addAttribute("ayDimensionVos", JSONObject.toJSONString(ayDimensionVos));
            List<DimensionVo> dimensionVos = null;
            List<DimensionVo> dimensionVosTwo = null;
            List<DimensionVo> dimensionVosThree = null;
            String titleInfo = "";
            if (CommonUtil.isNotNull(ayDimensionVos)) {
                DimensionCountQueryVo vos = new DimensionCountQueryVo();
                vos.setAyCode(ayDimensionVos.get(0).getAyCode());
                vos.setType(3);
                dimensionVos = knowledgeService.getDimensionData(vos);  //查询该案由的一级维度数据
                if (CommonUtil.isNotNull(dimensionVos)) {
                    vos.setDimensionId(dimensionVos.get(0).getId());
                    vos.setType(4);
                    dimensionVosTwo = knowledgeService.getDimensionData(vos); //根据一级维度id查询二级维度信息
                    if (CommonUtil.isNotNull(dimensionVosTwo)) {
                        vos.setDimensionId(dimensionVosTwo.get(0).getId());
                        vos.setType(5);
                        dimensionVosThree = knowledgeService.getDimensionData(vos); //根据二级维度id查询三级维度信息
                    }
                }
            }
            if (!dimensionVos.isEmpty()) {
                titleInfo = dimensionVos.get(0).getDimension();
                if (!dimensionVosTwo.isEmpty()) {
                    titleInfo += ">" + dimensionVosTwo.get(0).getDimension();
                    if (!dimensionVosThree.isEmpty()) {
                        titleInfo += ">" + dimensionVosThree.get(0).getDimension();
                    }
                }
            }
            model.addAttribute("dimensionVos", dimensionVos);
            model.addAttribute("dimensionVosTwo", dimensionVosTwo);
            model.addAttribute("dimensionVosThree", dimensionVosThree);
            model.addAttribute("titleInfo", titleInfo);
        } catch (Exception e) {
            logger.error("统计各维度总数/统计案由各维度数top 7 异常：", e);
        }
        return "knowledgeBase/retrieval";
    }

    /**
     * 根据时间及类型获取维度统计
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("getDimensionDataByTypeAndDate")
    public ResultVo getDimensionDataByTypeAndDate(HttpServletRequest request) {
        ResultVo resultVo = new ResultVo();
        String overall = request.getParameter("overall");
        Integer type = CommonUtil.isNotNull(request.getParameter("type")) ? Integer.parseInt(request.getParameter("type")) : null;
        CaseCountQueryVo vo = getCaseTypeAndDate(overall, null);
        DimensionCountQueryVo dimensionCountVo = new DimensionCountQueryVo();
        dimensionCountVo.setStartDate(vo.getLarqStart());
        dimensionCountVo.setEndDate(vo.getLarqEnd());
        dimensionCountVo.setType(type);
        try {
            List<CountVo> list = knowledgeService.countTotalDimension(dimensionCountVo);
            resultVo.setSuccess(true);
            resultVo.setSubject(list);
        } catch (Exception e) {
            logger.error("根据时间及类型按维度统计案件异常：", e);
        }
        return resultVo;
    }

    /**
     * 案由维度统计
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("getAyDimensionCount")
    public ResultVo getAyDimensionCount(HttpServletRequest request) {
        ResultVo resultVo = new ResultVo();
        try {
            List<AyDimensionVo> list = knowledgeService.countAyDimension();
            resultVo.setSuccess(true);
            resultVo.setSubject(list);
        } catch (Exception e) {
            logger.error("按维度统计案由异常：", e);
        }
        return resultVo;
    }

    /**
     * 查询维度明细
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("getDimensionData")
    public ResultVo getDimensionData(String caseReason, String dimensionId, String level) {
        ResultVo resultVo = new ResultVo();
        DimensionCountQueryVo vo = new DimensionCountQueryVo();
        vo.setDimensionId(dimensionId);
        if (level.equals("1")) {
            vo.setType(3);
        } else if (level.equals("2")) {
            vo.setType(4);
        } else if (level.equals("3")) {
            vo.setType(5);
        }

        if(!StringUtils.isEmpty(caseReason)&&!"null".equals(caseReason)){
            vo.setAyCode(caseReason);
        }
        try {
            List<DimensionVo> list = knowledgeService.getDimensionData(vo);
            resultVo.setSuccess(true);
            resultVo.setSubject(list);
        } catch (Exception e) {
            logger.error("查询维度明细异常：", e);
        }
        return resultVo;
    }

    /**
     * 根据案件类型统计案件数
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("getCaseCountData")
    public ResultVo getCaseCountData() {
        ResultVo resultVo = new ResultVo();
        try {
            List<CountVo> list = wsTemplateService.countWsNum();
            resultVo.setSuccess(true);
            resultVo.setSubject(list);
        } catch (Exception e) {
            logger.error("统计各类型案件异常：", e);
        }
        return resultVo;
    }

    @ResponseBody
    @RequestMapping("getTemplateData")
    public List<WsTemplateVo> getTemplateData(HttpServletRequest request) {
        List<WsTemplateVo> list = null;
        Integer type = CommonUtil.isNotNull(request.getParameter("type")) ? Integer.parseInt(request.getParameter("type")) : null;
        try {
            list = wsTemplateService.getWsTemplate(type);
            if (CommonUtil.isNotNull(list)) {
                Collections.sort(list, new Comparator<WsTemplateVo>() {
                    @Override
                    public int compare(WsTemplateVo b1, WsTemplateVo b2) {
                        return b2.getNum() - (b1.getNum());
                    }
                });
            }
        } catch (Exception e) {
            logger.error("根据案件类型插叙文书模板数据异常：", e);
        }
        return list;
    }

    public String getDistrictByFydm(String fydm) {
        String district = "";
        if ("2577".equals(fydm) || "2579".equals(fydm)) {  // 2577 深圳中院   2579  福田法院
            return "福田区";
        }
        if ("2578".equals(fydm)) {// 2578  罗湖法院
            return "罗湖区";
        }
        if ("2579".equals(fydm)) {// 2578  福田法院
            return "福田区";
        }
        if ("2580".equals(fydm)) {
            return "南山区";
        }
        if ("2581".equals(fydm)) {
            return "宝安区";
        }
        if ("2582".equals(fydm)) {
            return "龙岗区";
        }
        if ("2583".equals(fydm)) {
            return "盐田区";
        }
        if ("2714".equals(fydm)) {
            return "前海合作区";
        }
        if ("5006".equals(fydm)) {
            return "龙华区";
        }
        if ("5007".equals(fydm)) {
            return "坪山区";
        }
        return district;
    }


    public CaseCountQueryVo getCaseTypeAndDate(String overall, String caseType) {
        CaseCountQueryVo vo = new CaseCountQueryVo();
        if (CommonUtil.isNotNull(overall)) {
            Date startDate = null;
            Date ensDate = null;
            if ("1".equals(overall)) {  //本周
                startDate = DateUtil.getWeeknigStartDate();
                ensDate = DateUtil.getWeeknigEndDate();
            } else if ("2".equals(overall)) { //本月
                startDate = DateUtil.getMonthStartDate();
                ensDate = DateUtil.getMonthEndDate();
            } else if ("3".equals(overall)) { //本季度
                startDate = DateUtil.getCurrentQuarterStartDate();
                ensDate = DateUtil.getCurrentQuarterEndDate();
            } else if ("4".equals(overall)) { //今年
                startDate = DateUtil.getCurrentYearStartDate();
                ensDate = DateUtil.getCurrentYearEndDate();
            }
            vo.setLarqStart(startDate);
            vo.setLarqEnd(ensDate);
        }
        if (CommonUtil.isNotNull(caseType)) {
            String standardStart = "";
            String standardEnd = "";
            if (caseType.contains("civil")) {  //民事
                standardStart = "0300";
                standardEnd = "0400";
            } else if (caseType.contains("criminal")) { // 刑事
                standardStart = "0100";
                standardEnd = "0300";
            } else if (caseType.contains("administration")) {  //行政
                standardStart = "0400";
                standardEnd = "0500";
            }
            vo.setStandardStart(standardStart);
            vo.setStandardEnd(standardEnd);
        }
        return vo;
    }
}
