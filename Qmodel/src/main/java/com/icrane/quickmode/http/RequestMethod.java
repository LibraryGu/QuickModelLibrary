package com.icrane.quickmode.http;

/**
 * 请求方式
 *
 * @author gujiwen
 */
public enum RequestMethod {

    GET("GET"), POST("POST");

    private String methodName;

    private RequestMethod(String method) {
        this.methodName = method;
    }

    /**
     * 获取字符串表示代表
     *
     * @return 字符串表示代表
     */
    public String obtain() {
        return methodName;
    }
}
