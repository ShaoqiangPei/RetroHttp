//package com.httplibrary.httpApp;
//
///**
// * ############ 此类为模板代码,勿删 ############
// *
// * Title:具体的某个通讯使用的Retrofitor
// * description:
// *
// * autor:pei
// * created on 2019/12/17
// */
//public class ApiRetrofitor extends BaseRetrofitor {
//
//    private static final String RELEASE_URL="";//正式
//    private static final String TEST_URL="";//测试
//
//    private ApiRetrofitor() {}
//
//    private static class Holder {
//        private static ApiRetrofitor instance = new ApiRetrofitor();
//    }
//
//    public static ApiRetrofitor getInstance() {
//        return Holder.instance;
//    }
//
//    @Override
//    public String getReleaseUrl() {
//        return RELEASE_URL;
//    }
//
//    @Override
//    public String getTestUrl() {
//        return TEST_URL;
//    }
//
//}