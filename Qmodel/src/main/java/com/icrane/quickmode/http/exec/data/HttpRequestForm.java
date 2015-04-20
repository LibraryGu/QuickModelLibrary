package com.icrane.quickmode.http.exec.data;

import com.icrane.quickmode.http.RequestMethod;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("ALL")
public final class HttpRequestForm {

    // 请求方式
    private RequestMethod requestMethod = RequestMethod.GET;


    private Map<String, String> parameters = Collections.synchronizedMap(new HashMap<String, String>());
    private List<BasicNameValuePair> valuePairs = Collections.synchronizedList(new ArrayList<BasicNameValuePair>());

    /**
     * 构造方法
     *
     * @param method 请求方式
     */
    protected HttpRequestForm(RequestMethod method) {
        this.requestMethod = method;
    }

    public static HttpRequestForm create(RequestMethod method) {
        return new HttpRequestForm(method);
    }

    /**
     * 添加参数
     *
     * @param parameterKey   参数键
     * @param parameterValue 参数值
     */
    public HttpRequestForm put(String parameterKey, String parameterValue) {

        switch (requestMethod) {
            case GET:
                putParamsForGet(parameterKey, parameterValue);
                break;
            case POST:
                putParameterForPost(parameterKey, parameterValue);
                break;
            default:
                putParamsForGet(parameterKey, parameterValue);
                break;
        }
        return this;

    }

    /**
     * Get方式，添加参数
     *
     * @param parameterKey   参数键
     * @param parameterValue 参数值
     */
    protected void putParamsForGet(String parameterKey, String parameterValue) {
        parameters.put(parameterKey, parameterValue);
    }

    /**
     * Post方式，添加参数
     *
     * @param parameterKey   参数键
     * @param parameterValue 参数值
     */
    protected void putParameterForPost(String parameterKey, String parameterValue) {
        valuePairs.add(new BasicNameValuePair(parameterKey, parameterValue));
    }

    /**
     * 获取参数表单
     */
    public <T> T getParametersForm() {
        switch (requestMethod) {
            case GET:
                return (T) convertToString();
            case POST:
                return (T) getParametersFormOfPost();
            default:
                return (T) convertToString();
        }
    }

    /**
     * 返回get方式的参数表单
     */
    protected Map<String, String> getParametersFormOfGet() {
        return parameters;
    }

    /**
     * 返回post方式的参数表单
     */
    protected List<BasicNameValuePair> getParametersFormOfPost() {
        return valuePairs;
    }

    /**
     * 转换Get表单为字符串表示
     *
     * @return Get表单为字符串形式
     */
    protected String convertGetFormToString() {

        StringBuffer buff = new StringBuffer();
        Set<String> parameterKeys = parameters.keySet();

        if (!parameterKeys.isEmpty()) {

            buff.append("?");

            for (String parameterKey : parameterKeys) {
                buff.append(parameterKey + "=" + parameters.get(parameterKey) + "&");
            }

            buff.replace(buff.length() - 1, buff.length(), "");

        }
        return buff.toString();

    }

    /**
     * 转换Post表单为字符串表示
     *
     * @return Post表单为字符串形式
     */
    protected String convertPostFormToString() {

        StringBuffer buff = new StringBuffer();

        if (!valuePairs.isEmpty()) {

            buff.append("?");

            for (BasicNameValuePair nameValuePair : valuePairs) {
                buff.append(nameValuePair.getName() + "=" + nameValuePair.getValue() + "&");
            }

            buff.replace(buff.length() - 1, buff.length(), "");

        }
        return buff.toString();

    }

    /**
     * 转换成String字符串
     */
    public String convertToString() {

        switch (requestMethod) {
            case GET:
                return convertGetFormToString();
            case POST:
                return convertPostFormToString();
            default:
                return convertGetFormToString();
        }

    }

    /**
     * 获取请求方式
     *
     * @return 请求方式
     */
    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    /**
     * 设置请求方式
     *
     * @param requestMethod 请求方式
     */
    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

}
