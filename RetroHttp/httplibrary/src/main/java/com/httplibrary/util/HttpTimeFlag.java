package com.httplibrary.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Title:统计通讯耗时工具
 * description:
 * autor:pei
 * created on 2019/12/19
 */
public class HttpTimeFlag {

    private static final String RETROHTTP_TAG="retrohttp_tag";
    private static final int MIN_COUNT=0;
    private static final int MAX_COUNT=1000;

    private int mCount=MIN_COUNT;

    private Map<String,Long> map=new HashMap<>();

    private HttpTimeFlag(){}

    private static class Holder {
        private static HttpTimeFlag instance = new HttpTimeFlag();
    }

    public static HttpTimeFlag getInstance() {
        return Holder.instance;
    }

    /**设置String类型起始时间戳**/
    public String startFlag() {
        String flag=createTimeFlag();
        long time = System.currentTimeMillis();
        map.put(flag, time);
        RetroLog.w("====== RetroHttp设置通讯起始时间戳flag=" + flag +" ======");
        return flag;
    }

    /**
     * 获取 flag节点下时间段，返回"-1"表示没有设置时间起点，无法计算用时。
     *
     * @param flag String 类型
     * @return
     */
    public String stopFlagByString(String flag){
        long tempTime=stopFlag(flag);
        String tempStr = "-1";
        if (tempTime == -1) {
            RetroLog.w( "===== RetroHttp通讯TimeFlag=" + flag + "  用时: -1(未设置开始时间戳或时间戳tag生成失败)");
        } else {
            tempStr = formatTime(tempTime);
            RetroLog.w( "====== RetroHttp通讯TimeFlag=" + flag + "  用时: " + tempStr + " ======");
        }
        return tempStr;
    }

    /**
     * 生成自增tag,范围在 retrohttp_tag1-retrohttp_tag1000之间
     *
     * @return retrohttp_tag+数字
     */
    private String createTimeFlag(){
        mCount++;
        if(mCount>HttpTimeFlag.MAX_COUNT){
            mCount=HttpTimeFlag.MIN_COUNT+1;
            //清理所有tag
            clearFlag();
        }
        String flag=HttpTimeFlag.RETROHTTP_TAG+mCount;
        return flag;
    }

    /**清空所有时间戳**/
    private void clearFlag(){
        map.clear();
    }

    /**
     * 获取 flag节点下时间段，返回"-1"表示没有设置时间起点，无法计算用时。
     *
     * @param flag  String类型
     * @return
     */
    private long stopFlag(String flag) {
        if (StringUtil.isEmpty(flag)) {
            return -1;
        }
        long tempTime = map.get(flag);
        if (tempTime > 0) {
            long time = System.currentTimeMillis();
            map.remove(flag);
            return time - tempTime;
        }
        return -1;
    }

    /**毫秒转化时分秒毫秒**/
    private String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "天");
        }
        if (hour > 0) {
            sb.append(hour + "小时");
        }
        if (minute > 0) {
            sb.append(minute + "分");
        }
        if (second > 0) {
            sb.append(second + "秒");
        }
        if (milliSecond > 0) {
            sb.append(milliSecond + "毫秒");
        }
        return sb.toString();
    }

}
