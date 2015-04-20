package com.icrane.quickmode.http.exec.client;

import com.icrane.quickmode.cache.BasicLruCache;
import com.icrane.quickmode.cache.CacheType;
import com.icrane.quickmode.http.HttpDataType;
import com.icrane.quickmode.http.HttpError;
import com.icrane.quickmode.http.OnRequestListener;
import com.icrane.quickmode.http.RequestMethod;
import com.icrane.quickmode.http.RequestMode;
import com.icrane.quickmode.http.exec.data.HttpRequestForm;
import com.icrane.quickmode.http.exec.data.packet.AbRequestPacket;
import com.icrane.quickmode.http.exec.data.packet.AbResponsePacket;
import com.icrane.quickmode.http.exec.data.packet.impl.HttpResponsePacket;
import com.icrane.quickmode.http.handler.AsyncBasicResponse;
import com.icrane.quickmode.utils.Charset;
import com.icrane.quickmode.utils.common.CommonUtils;
import com.icrane.quickmode.utils.common.HttpUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ALL")
public class HttpClientExecutor extends BasicClientExecutor {

    // 客户端引用
    private static HttpClient mHttpClient = null;
    // Lru缓存
    private BasicLruCache lruCache;
    // 请求数据包
    private AbRequestPacket requestPacket;
    // 响应数据包
    private AbResponsePacket responsePacket;
    // 读取接口
    private AsyncBasicResponse mAsyncBasicResponse;
    // 连接池对象
    private ClientConnectionManager clientConnectionManager;
    // 网络请求执行者
    private HttpExecutorManager httpExecutorManager;
    // 请求接口
    private OnRequestListener onRequestListener = new OnRequestListener() {

        @Override
        public AbResponsePacket doResponse(HttpClientExecutor executor,
                                           HttpResponse response) {
            return executor.getAsyncBasicResponse().handleResponse(executor,
                    response);
        }

    };

    /**
     * 构造方法
     *
     * @param request
     * @param responseHandler
     */
    public HttpClientExecutor(AbRequestPacket request, AsyncBasicResponse responseHandler) {

        this.requestPacket = request;
        this.mAsyncBasicResponse = responseHandler;
        CacheType cacheType = this.requestPacket.getCacheType();
        if (cacheType != null) {
            this.lruCache = HttpUtils.switchCache(cacheType);
        } else {
            this.lruCache = HttpUtils.switchCache(CacheType.LRU);
        }

    }

    @Override
    public void bindExecutorManager(HttpExecutorManager manager) {

        this.httpExecutorManager = manager;
        this.addObserver(this.httpExecutorManager);

    }

    @Override
    public void execute() {

        try {

            RequestMode requestType = this.requestPacket.getRequestType();

            // 判断请求类型，进行请求数据
            switch (requestType) {

                case REQUEST_MODE_NETWORK:
                    responsePacket = requestNetwork();
                    break;
                case REQUEST_MODE_CACHE:
                    responsePacket = requestCache(requestPacket.getCompleteUri());
                    break;
                case REQUEST_MODE_CHECKING_CACHE:
                    responsePacket = requestCache(requestPacket.getCompleteUri());
                    if (CommonUtils.isEmpty(responsePacket.getCacheContent()))
                        responsePacket = requestNetwork();
                    break;

            }

            this.notifyPreUpdate(responsePacket);

        } catch (ClientProtocolException e) {

            this.httpExecutorManager.commitErrorMessage(HttpExecutorManager.ERROR, HttpError.ERROR_EXCEPTION,
                    responsePacket, e);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected AbResponsePacket requestNetwork() throws ClientProtocolException,
            IOException {

        // 声明HttpResponse对象
        HttpResponse mHttpResponse = null;

        if (CommonUtils.isEmpty(requestPacket))
            throw new NullPointerException("request packet is null!");

        // 获取请求连接地址
        String uri = requestPacket.getUri();
        // 获取请求表单
        HttpRequestForm form = requestPacket.getHttpRequestForm();
        mHttpResponse = execClient(form, uri);

        // 获取请求接口
        OnRequestListener mRequestListener = this.getOnRequestListener();

        // 提供一个OnRequestListener接口，并调用doResponse()方法对请求进行操作，最后得到请求数据包，并返回
        if (CommonUtils.isEmpty(mRequestListener))
            throw new NullPointerException("request listener is null!");

        return mRequestListener.doResponse(this, mHttpResponse);

    }

    @Override
    protected AbResponsePacket requestCache(String completeUri) {

        // 声明一个缓存字符串对象
        Object cacheContent = null;
        // 声明并实例化一个HttpDataPacket对象
        AbResponsePacket responsePacket = HttpResponsePacket.create();

        // 判断HttpRequestPacket对象是否为空
        if (CommonUtils.isEmpty(requestPacket)) throw new NullPointerException("request is null!");

        // 获取缓存
        cacheContent = (!CommonUtils.isEmpty(lruCache)) ? lruCache.get(completeUri) : "";

        // 设置HttpResponsePacket对象
        responsePacket.setUri(requestPacket.getUri())
                .setDataType(HttpDataType.CACHE_CONTENT)
                .setUseMemoryCache(requestPacket.isUseMemoryCache())
                .setUseHardWareCache(requestPacket.isUseHardWareCache())
                .setRequestType(requestPacket.getRequestType())
                .setHttpRequestForm(requestPacket.getHttpRequestForm())
                .setCharset(requestPacket.getCharset())
                .setCompleteUri(requestPacket.getCompleteUri())
                .setCacheType(requestPacket.getCacheType())
                .setCacheContent(cacheContent);


        return responsePacket;

    }

    @Override
    public AbRequestPacket getRequestPacket() {
        return requestPacket;
    }

    @Override
    public void setRequestPacket(AbRequestPacket requestPacket) {
        this.requestPacket = requestPacket;
    }

    @Override
    public OnRequestListener getOnRequestListener() {
        return onRequestListener;
    }

    @Override
    public void setOnRequestListener(OnRequestListener onRequestListener) {
        this.onRequestListener = onRequestListener;
    }

    @Override
    public AsyncBasicResponse getAsyncBasicResponse() {
        return mAsyncBasicResponse;
    }

    @Override
    public void setAsyncBasicResponse(AsyncBasicResponse asyncBasicResponse) {
        this.mAsyncBasicResponse = asyncBasicResponse;
    }

    @Override
    public HttpExecutorManager getHttpExecutorManager() {
        return httpExecutorManager;
    }

    @Override
    public BasicLruCache getLruCache() {
        return lruCache;
    }

    @Override
    public void setLruCache(BasicLruCache lruCache) {
        this.lruCache = lruCache;
    }

    @Override
    public void closeConnections() {

        if (mHttpClient == null) throw new NullPointerException("HttpClientExecuter is null!");

        // 关闭池中空闲连接
        mHttpClient.getConnectionManager().closeIdleConnections(0, TimeUnit.SECONDS);
        // 关闭池中所有过期连接
        mHttpClient.getConnectionManager().closeExpiredConnections();

    }

    /**
     * 获取HttpClient对象
     *
     * @return
     */
    protected HttpClient obtainDefaultHttpClient() {

        if (mHttpClient == null) {

            final HttpParams httpParams = new BasicHttpParams();

            HttpUtils.setHttpProtocolParams(HttpUtils.DEFAULT_USER_AGENT, Charset.UTF_8, Charset.UTF_8, true,
                    HttpUtils.DEFAULT_HTTP_VERSION, httpParams);

            HttpUtils.setHttpConnectionParams(
                    HttpUtils.DEFAULT_CONNECTION_TIMEOUT,
                    HttpUtils.DEFAULT_LINGER,
                    HttpUtils.DEFAULT_SOCKET_BUFFER_SIZE,
                    HttpUtils.DEFAULT_SO_TIMEOUT, true, true, httpParams);

            HttpUtils.setConnManagerParams(new ConnPerRouteBean(HttpUtils.DEFAULT_HOST_CONNECTIONS),
                    HttpUtils.DEFAULT_MAX_TOTAL_CONNECTIONS,
                    HttpUtils.DEFAULT_SOCKET_TIMEOUT, httpParams);

            HttpUtils.setHttpClientParams(HttpUtils.AUTHENTICATING, "", true,
                    httpParams);

            Scheme httpScheme = HttpUtils.createScheme(HttpUtils.SCHEME_HTTP, PlainSocketFactory.getSocketFactory(),
                    HttpUtils.SCHEME_HTTP_PORT);
            Scheme httpsScheme = HttpUtils.createScheme(HttpUtils.SCHEME_HTTPS, SSLSocketFactory.getSocketFactory(),
                    HttpUtils.SCHEME_HTTPS_PORT);

            SchemeRegistry mSchemeRegistry = HttpUtils.createSchemeRegistry(httpScheme, httpsScheme);

            clientConnectionManager = new ThreadSafeClientConnManager(
                    httpParams, mSchemeRegistry);

            // 实例化一个默认HttpClient对象
            mHttpClient = new DefaultHttpClient(clientConnectionManager,
                    httpParams);

        }
        return mHttpClient;

    }

    /**
     * Client执行操作获取HttpResponse
     *
     * @param form 请求表单
     * @param uri  请求地址
     * @throws org.apache.http.client.ClientProtocolException
     * @throws java.io.IOException
     */
    protected HttpResponse execClient(HttpRequestForm form, String uri)
            throws ClientProtocolException, IOException {

        // 声明HttpRequestBase对象
        HttpRequestBase requestBase = null;
        // 声明HttpResponse对象
        HttpResponse mHttpResponse = null;

        // 判断请求地址是否为null
        if (uri.equals("") || uri == null) throw new NullPointerException("uri is null!");

        // 判断请求表单是否为null
        if (CommonUtils.isEmpty(form)) throw new NullPointerException("form is null!");

        RequestMethod method = form.getRequestMethod();
        requestBase = HttpUtils.switchRequestBase(uri, method, form);

        // 添加请求头
        HttpUtils.addHeaders(requestBase, requestPacket.getHeaders());
        // 获取默认HttpClient对象
        mHttpClient = obtainDefaultHttpClient();
        // 执行操作并获取HttpResponse
        mHttpResponse = mHttpClient.execute(requestBase);

        return mHttpResponse;
    }

    /**
     * 通知更新
     *
     * @param data
     */
    public void notifyPreUpdate(Object data) {
        this.setChanged();
        this.notifyObservers(data);
    }

    /**
     * 获取HttpClient对象
     *
     * @return
     */
    public HttpClient getHttpClient() {
        return mHttpClient;
    }

    /**
     * 设置HttpClient对象
     *
     * @param httpClient
     * @return
     */
    public HttpClient setHttpClient(HttpClient httpClient) {
        return mHttpClient = httpClient;
    }

    /**
     * 获取线程连接池
     *
     * @return 线程连接池
     */
    public ClientConnectionManager getClientConnectionManager() {
        return clientConnectionManager;
    }

    /**
     * 设置连接池
     *
     * @param clientConnectionManager
     */
    public void setClientConnectionManager(
            ClientConnectionManager clientConnectionManager) {
        this.clientConnectionManager = clientConnectionManager;
    }

    @Override
    public void release() {

        if (mHttpClient != null) return;

        closeConnections();
        // 关闭连接池
        mHttpClient.getConnectionManager().shutdown();
        mHttpClient = null;
        System.gc();

    }

}
