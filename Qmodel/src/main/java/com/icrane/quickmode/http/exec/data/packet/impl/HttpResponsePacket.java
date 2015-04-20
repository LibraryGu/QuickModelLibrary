package com.icrane.quickmode.http.exec.data.packet.impl;

import com.icrane.quickmode.cache.CacheType;
import com.icrane.quickmode.http.HttpDataType;
import com.icrane.quickmode.http.HttpError;
import com.icrane.quickmode.http.RequestMode;
import com.icrane.quickmode.http.exec.data.HttpRequestForm;
import com.icrane.quickmode.http.exec.data.packet.AbResponsePacket;
import com.icrane.quickmode.utils.Charset;

import org.apache.http.Header;

import java.util.List;
import java.util.Map;

public class HttpResponsePacket extends AbResponsePacket {

    /**
     * 是否使用缓存
     */
    private boolean useMemoryCache = false;
    /**
     * 是否使用硬件缓存
     */
    private boolean useHardwareCache = false;
    /**
     * 响应码
     */
    private int statusCode = 0;
    /**
     * 接收的内容长度
     */
    private long contentLength = 0L;
    /**
     * 接收的内容类型
     */
    private String contentType = "text/html";
    /**
     * 请求地址
     */
    private String uri = "";
    /**
     * 完整Uri
     */
    private String completeUri = "";
    /**
     * 接收的内容
     */
    private Object content;
    /**
     * 缓存内容
     */
    private Object cacheContent;
    /**
     * 请求头
     */
    private Map<String, List<String>> urlConnHeaders;

    private Header[] clientHeaders;
    /**
     * 数据类型
     */
    private HttpDataType dataType;
    /**
     * 请求表单对象
     */
    private HttpRequestForm httpRequestForm;
    /**
     * 字符编码
     */
    private Charset charset = Charset.UTF_8;
    /**
     * 请求类型
     */
    private RequestMode requestType = RequestMode.REQUEST_MODE_NETWORK;
    /**
     * 异常信息对象
     */
    private HttpError httpError = HttpError.ERROR_NONE;
    /**
     * 缓存类型
     */
    private CacheType cacheType = CacheType.LRU;

    protected HttpResponsePacket() {
    }

    public static HttpResponsePacket create() {
        return new HttpResponsePacket();
    }

    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public AbResponsePacket setUri(String uri) {
        this.uri = uri;
        return this;
    }

    @Override
    public String getCompleteUri() {
        return completeUri;
    }

    @Override
    public AbResponsePacket setCompleteUri(String completeUri) {
        this.completeUri = completeUri;
        return this;
    }

    @Override
    public Map<String, List<String>> getURLConnHeaders() {
        return urlConnHeaders;
    }

    @Override
    public AbResponsePacket setURLConnHeaders(Map<String, List<String>> headers) {
        this.urlConnHeaders = headers;
        return this;
    }

    @Override
    public Header[] getClientHeaders() {
        return clientHeaders;
    }

    @Override
    public AbResponsePacket setClientHeaders(Header[] headers) {
        this.clientHeaders = headers;
        return this;
    }

    @Override
    public HttpRequestForm getHttpRequestForm() {
        return httpRequestForm;
    }

    @Override
    public AbResponsePacket setHttpRequestForm(HttpRequestForm httpRequestForm) {
        this.httpRequestForm = httpRequestForm;
        return this;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public AbResponsePacket setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    @Override
    public long getContentLength() {
        return contentLength;
    }

    @Override
    public AbResponsePacket setContentLength(long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public AbResponsePacket setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    @Override
    public Object getContent() {
        return content;
    }

    @Override
    public AbResponsePacket setContent(Object content) {
        this.content = content;
        return this;
    }

    @Override
    public Object getCacheContent() {
        return cacheContent;
    }

    @Override
    public AbResponsePacket setCacheContent(Object cacheContent) {
        this.cacheContent = cacheContent;
        return this;
    }

    @Override
    public Charset getCharset() {
        return charset;
    }

    @Override
    public AbResponsePacket setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    @Override
    public AbResponsePacket setUseMemoryCache(boolean useMemoryCache) {
        this.useMemoryCache = useMemoryCache;
        return this;
    }

    @Override
    public boolean isUseMemoryCache() {
        return useMemoryCache;
    }

    @Override
    public RequestMode getRequestType() {
        return requestType;
    }

    @Override
    public AbResponsePacket setRequestType(RequestMode requestType) {
        this.requestType = requestType;
        return this;
    }

    @Override
    public HttpError getHttpErrorMessage() {
        return httpError;
    }

    @Override
    public AbResponsePacket setHttpErrorMessage(HttpError httpError) {
        this.httpError = httpError;
        return this;
    }

    @Override
    public HttpDataType getDataType() {
        return dataType;
    }

    @Override
    public AbResponsePacket setDataType(HttpDataType dataType) {
        this.dataType = dataType;
        return this;
    }

    @Override
    public AbResponsePacket setUseHardWareCache(boolean useHardwareCache) {
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
    public AbResponsePacket setCacheType(CacheType cacheType) {
        this.cacheType = cacheType;
        return this;
    }

}
