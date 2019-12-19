//package com.httplibrary.httpApp;
//
///**
// * ############ 此类为模板代码,勿删 ############
// *
// * Description: app版本切换及Log调试参数控制类
// *
// * Author:pei
// * Date: 2019/3/21
// */
//public class AppConfig {
//
//    /**是否为测试版本**/
//    private boolean isTest=true;
//
//    /**是否打开调试模式(整个app非通讯模块Log)**/
//    private boolean isDebug=true;
//
//    /**是否开启通讯模块Log打印(调试时方便查看通讯传参和返回结果)**/
//    private boolean isHttpLog=true;
//
//
//    private AppConfig(){}
//
//    private static class Holder {
//        private static AppConfig instance = new AppConfig();
//    }
//
//    public static AppConfig getInstance(){
//        return Holder.instance;
//    }
//
//    public boolean isTest(){
//        return isTest;
//    }
//
//    public boolean isDebug(){
//        return isDebug;
//    }
//
//    public boolean isHttpLog() {
//        return isHttpLog;
//    }
//
//}