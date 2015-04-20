package com.icrane.quickmode.http.exec.data.packet.impl;

import com.icrane.quickmode.cache.CacheType;
import com.icrane.quickmode.http.RequestMethod;
import com.icrane.quickmode.http.RequestMode;
import com.icrane.quickmode.http.exec.data.HttpRequestForm;
import com.icrane.quickmode.http.exec.data.packet.AbRequestPacket;
import com.icrane.quickmode.utils.Charset;

import org.apache.http.Header;

public class HttpRequestPacket extends AbRequestPacket {

    /**
     * 是否使用内存缓存
     */
    private boolean useMemoryCache = false;
    /**
     * 是否使用硬件缓存
     */
    private boolean useHardwareCache = false;
    /**
     * 请求地址
     */
    private String uri = "";
    /**
     * 完整Uri
     */
    private String completeUri = "";
    /**
     * 请求头
     */
    private Header[] headers = null;
    /**
     * 缓存类型
     */
    private CacheType cacheType = CacheType.LRU;

    /**
     * 请求表单对象
     */
    private HttpRequestForm httpRequestForm = HttpRequestForm
            .create(RequestMethod.GET);
    /**
     * 字符编码
     */
    private Charset charset = Charset.UTF_8;
    /**
     * 请求类型
     */
    private RequestMode requestType = RequestMode.REQUEST_MODE_CHECKING_CACHE;

    protected HttpRequestPacket() {
    }

    /**
     * 创建实例
     *
     * @return 返回RequestPacket
     */
    public static AbRequestPacket create() {
        return new HttpRequestPacket();
    }

    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public AbRequestPacket setUri(String uri) {
        this.uri = uri;
        return this;
    }

    @Override
    public String getCompleteUri() {
        this.completeUri = uri
                + (httpRequestForm != null ? httpRequestForm.convertToString()
                : "");
        return completeUri;
    }

    @Override
    public AbRequestPacket setCompleteUri(String completeUri) {
        this.completeUri = completeUri;
        return this;
    }

    @Override
    public Header[] getHeaders() {
        return headers;
    }

    @Override
    public AbRequestPacket setHeaders(Header... headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public HttpRequestForm getHttpRequestForm() {
        return httpRequestForm;
    }

    @Override
    public AbRequestPacket setHttpRequestForm(HttpRequestForm httpRequestForm) {
        this.httpRequestForm = httpRequestForm;
        return this;
    }

    @Override
    public AbRequestPacket setUseMemoryCache(boolean useMemoryCache) {
        this.useMemoryCache = useMemoryCache;
        return this;
    }

    @Override
    public boolean isUseMemoryCache() {
        return useMemoryCache;
    }

    @Override
    public AbRequestPacket setUseHardWareCache(boolean useHardwareCache) {
        this.useHardwareCache = useHardwareCache;
        return this;
    }

    @Override
    public boolean isUseHardWareCache() {
        return useHardwareCache;
    }

    @Override
    public CacheType getCacheType() {
        return cacheType;
    }

    @Override
    public AbRequestPacket setCacheType(CacheType cacheType) {
        this.cacheType = cacheType;
        return this;
    }

    @Override
    public Charset getCharset() {
        return charset;
    }

    @Override
    public AbRequestPacket setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    @Override
    public RequestMode getRequestType() {
        return requestType;
    }

    @Override
    public AbRequestPacket setRequestType(RequestMode requestType) {
        this.requestType = requestType;
        return this;
    }

}
