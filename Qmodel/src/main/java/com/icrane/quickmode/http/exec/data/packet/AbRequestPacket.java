package com.icrane.quickmode.http.exec.data.packet;

import com.icrane.quickmode.cache.CacheType;
import com.icrane.quickmode.http.RequestType;
import com.icrane.quickmode.http.exec.data.HttpRequestForm;
import com.icrane.quickmode.utils.Charset;

import org.apache.http.Header;

@SuppressWarnings("ALL")
public abstract class AbRequestPacket {

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
    public abstract AbRequestPacket setUri(String uri);

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
    public abstract AbRequestPacket setCompleteUri(String completeUri);

    /**
     * 获取所有请求头
     *
     * @return 所有请求头，是一个数组
     */
    public abstract Header[] getHeaders();

    /**
     * 设置请求头
     *
     * @param headers 请求头
     */
    public abstract AbRequestPacket setHeaders(Header... headers);

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
    public abstract AbRequestPacket setHttpRequestForm(
            HttpRequestForm httpRequestForm);

    /**
     * 设置使用内存缓存
     *
     * @param useMemoryCache 如果为true表示使用内存缓存，false则表示不使用
     */
    public abstract AbRequestPacket setUseMemoryCache(boolean useMemoryCache);

    /**
     * 是否使用内存缓存
     *
     * @return 如果为true表示使用内存缓存，false则表示不使用
     */
    public abstract boolean isUseMemoryCache();

    /**
     * 设置使用硬件缓存
     *
     * @param useHardwareCache 如果为true表示使用硬件缓存，false则表示不使用
     */
    public abstract AbRequestPacket setUseHardWareCache(boolean useHardwareCache);

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
    public abstract AbRequestPacket setCacheType(CacheType cacheType);

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
    public abstract AbRequestPacket setCharset(Charset charset);

    /**
     * 获取请求类型
     */
    public abstract RequestType getRequestType();

    /**
     * 设置请求类型
     *
     * @param requestType 请求类型
     */
    public abstract AbRequestPacket setRequestType(RequestType requestType);

}
