package com.taiji.knowledge.app.util;

import com.taiji.common.util.CommonUtil;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yunsama on 2019/5/23.
 */
public class DateUtil {

    // 获得当天0点时间
    public static Date getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();


    }
    // 获得昨天0点时间
    public static Date getYesterdaymorning() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getTimesmorning().getTime()-3600*24*1000);
        return cal.getTime();
    }
    // 获得当天近7天时间
    public static Date getWeekFromNow() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis( getTimesmorning().getTime()-3600*24*1000*7);
        return cal.getTime();
    }

    // 获得当天24点时间
    public static Date getTimesnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // 获得本周开始日期
    public static Date getWeeknigStartDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return CommonUtil.setHMS(cal.getTime(), 00, 00, 00);
    }

    //
    public static Date getWeeknigEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getWeeknigStartDate());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        return CommonUtil.setHMS(cal.getTime(), 23, 59, 59);
    }

    // 获得本月开始日期
    public static Date getMonthStartDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH,1);
        return CommonUtil.setHMS(cal.getTime(), 00, 00, 00);
    }

    // 获得本月结束日期
    public static Date getMonthEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return CommonUtil.setHMS(cal.getTime(), 23, 59, 59);
    }
    // 获得某月结束日期
    public static Date getMonthEndDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return CommonUtil.setHMS(cal.getTime(), 23, 59, 59);
    }

    public static Date getLastMonthStartMorning() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getMonthStartDate());
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    /**
     * 当前季度的开始日期
     * 
     * @return
     */
    public static Date getCurrentQuarterStartDate() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 0);
            }else if (currentMonth >= 4 && currentMonth <= 6){
                c.set(Calendar.MONTH, 3);
            }else if (currentMonth >= 7 && currentMonth <= 9){
                c.set(Calendar.MONTH, 4);
            }else if (currentMonth >= 10 && currentMonth <= 12){
                c.set(Calendar.MONTH, 9);
            }
            c.set(Calendar.DATE, 1);
            now = CommonUtil.setHMS(c.getTime(), 00, 00, 00);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 当前季度的结束日期
     * 
     * @return
     * */
    public  static Date getCurrentQuarterEndDate() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            now = CommonUtil.setHMS(c.getTime(), 23, 59, 59);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }


    /**
     * 获取今年是哪一年
     * @return
     */
     public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
     }

    /**
     * 本年的开始日期
     * @return
     */
    public static Date getCurrentYearStartDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        return CommonUtil.setHMS(cal.getTime(), 00, 00, 00);
    }

    /**
     * 本年的结束日期
     * @return
     */
    public static Date getCurrentYearEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 31);
        return CommonUtil.setHMS(cal.getTime(), 23, 59, 59);
    }

    /**
     * 上年开始点时间
     * @return
     */
    public static Date getLastYearStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getCurrentYearStartDate());
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }

    /**
     * 获取一段时间内的每个月第一天
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public static List<Date> getMonthFisrtDay(Date startDate,Date endDate) throws Exception{
        List<Date> dateList=new ArrayList<>();

        Date d1 = new SimpleDateFormat("yyyy-MM").parse(CommonUtil.formatDate("yyyy-MM",startDate));//定义起始日期

        Date d2 = new SimpleDateFormat("yyyy-MM").parse(CommonUtil.formatDate("yyyy-MM",endDate));//定义结束日期

        Calendar dd = Calendar.getInstance();//定义日期实例

        dd.setTime(d1);//设置日期起始时间

        dateList.add(dd.getTime());
        while(dd.getTime().before(d2)){//判断是否到结束日期

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

            String str = sdf.format(dd.getTime());

            System.out.println(str);//输出日期结果
            dd.add(Calendar.MONTH, 1);//进行当前日期月份加1
            dateList.add(dd.getTime());
        }
        return dateList;
    }
}
