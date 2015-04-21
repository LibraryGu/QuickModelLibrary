package com.icrane.quickmode.http.exec.data.packet;

import com.icrane.quickmode.cache.CacheType;
import com.icrane.quickmode.http.HttpDataType;
import com.icrane.quickmode.http.HttpError;
import com.icrane.quickmode.http.RequestType;
import com.icrane.quickmode.http.exec.data.HttpRequestForm;
import com.icrane.quickmode.utils.Charset;

import org.apache.http.Header;

import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public abstract class AbResponsePacket {

    /**
     * 获取请求地址
     *
     * @return 请求地址
     */
    public abstract String getUri();

    /**
     * 设置请求地址
     *
     * @param uri 请求地址
     */
    public abstract AbResponsePacket setUri(String uri);

    /**
     * 获取完整地址
     *
     * @return 完整地址
     */
    public abstract String getCompleteUri();

    /**
     * 设置完整地址
     *
     * @param completeUri 完整地址
     */
    public abstract AbResponsePacket setCompleteUri(String completeUri);

    /**
     * 获取所有请求头
     *
     * @return 所有请求头，是一个数组
     */
    public abstract Map<String, List<String>> getURLConnHeaders();

    /**
     * 设置请求头
     *
     * @param headers 请求头
     */
    public abstract AbResponsePacket setURLConnHeaders(Map<String,List<String>> headers);

    /**
     * 获取所有请求头
     *
     * @return 所有请求头，是一个数组
     */
    public abstract Header[] getClientHeaders();

    /**
     * 设置请求头
     *
     * @param headers 请求头
     */
    public abstract AbResponsePacket setClientHeaders(Header[] headers);

    /**
     * 获取请求表单
     *
     * @return 请求表单
     */
    public abstract HttpRequestForm getHttpRequestForm();

    /**
     * 设置请求表单
     *
     * @param httpRequestForm 请求表单
     */
    public abstract AbResponsePacket setHttpRequestForm(
            HttpRequestForm httpRequestForm);

    /**
     * 获取状态码
     *
     * @return 状态码
     */
    public abstract int getStatusCode();

    /**
     * 设置状态码
     *
     * @param statusCode 状态码
     */
    public abstract AbResponsePacket setStatusCode(int statusCode);

    /**
     * 获取内容长度
     *
     * @return 内容长度
     */
    public abstract long getContentLength();

    /**
     * 设置内容长度
     *
     * @param contentLength 内容长度
     */
    public abstract AbResponsePacket setContentLength(long contentLength);

    /**
     * 获取内容类型
     *
     * @return 内容类型
     */
    public abstract String getContentType();

    /**
     * 设置内容类型
     *
     * @param contentType 内容类型
     */
    public abstract AbResponsePacket setContentType(String contentType);

    /**
     * 获取内容
     *
     * @return 内容
     */
    public abstract Object getContent();

    /**
     * 设置内容
     *
     * @param content 内容
     */
    public abstract AbResponsePacket setContent(Object content);

    /**
     * 获取缓存内容
     *
     * @return 缓存内容
     */
    public abstract Object getCacheContent();

    /**
     * 设置缓存内容
     *
     * @param cacheContent 缓存内容
     */
    public abstract AbResponsePacket setCacheContent(Object cacheContent);

    /**
     * 获取字符编码
     *
     * @return 字符编码
     */
    public abstract Charset getCharset();

    /**
     * 设置字符编码
     *
     * @param charset 字符编码
     */
    public abstract AbResponsePacket setCharset(Charset charset);

    /**
     * 设置使用内存缓存
     *
     * @param useMemoryCache 如果为true表示使用内存缓存，false则表示不使用
     */
    public abstract AbResponsePacket setUseMemoryCache(boolean useMemoryCache);

    /**
     * 是否使用内存缓存
     *
     * @return 如果为true表示使用内存缓存，false则表示不使用
     */
    public abstract boolean isUseMemoryCache();

    /**
     * 获取请求方式
     *
     * @return 请求方式
     */
    public abstract RequestType getRequestType();

    /**
     * 设置请求类型
     *
     * @param requestType 请求类型
     */
    public abstract AbResponsePacket setRequestType(RequestType requestType);

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    public abstract HttpError getHttpErrorMessage();

    /**
     * 设置错误信息
     *
     * @param httpError 错误信息
     */
    public abstract AbResponsePacket setHttpErrorMessage(HttpError httpError);

    /**
     * 获取数据类型
     *
     * @return 数据类型
     */
    public abstract HttpDataType getDataType();

    /**
     * 设置数据类型
     *
     * @param dataType 数据类型
     */
    public abstract AbResponsePacket setDataType(HttpDataType dataType);

    /**
     * 设置使用硬件缓存
     *
     * @param useHardwareCache 如果为true表示使用硬件缓存，false则表示不使用
     */
    public abstract AbResponsePacket setUseHardWareCache(boolean useHardwareCache);

    /**
     * 是否使用硬件缓存
     *
     * @return 如果为true表示使用硬件缓存，false则表示不使用
     */
    public abstract boolean isUseHardWareCache();

    /**
     * 获取缓存类型
     *
     * @return 缓存类型
     */
    public abstract CacheType getCacheType();

    /**
     * 设置字符编码
     *
     * @param cacheType 缓存类型
     */
    public abstract AbResponsePacket setCacheType(CacheType cacheType);
}
