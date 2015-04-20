package com.icrane.quickmode.http.exec.client;

import com.icrane.quickmode.http.OnRequestListener;
import com.icrane.quickmode.http.OnResponseListener;
import com.icrane.quickmode.http.RequestMethod;
import com.icrane.quickmode.http.exec.data.HttpRequestForm;
import com.icrane.quickmode.http.exec.data.packet.AbRequestPacket;
import com.icrane.quickmode.http.exec.data.packet.AbResponsePacket;
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
public class HttpClientExecutor extends AbClientExecutor<HttpResponse> {

    // 客户端对象
    private HttpClient mHttpClient = null;
    //请求数据包
    private AbRequestPacket mRequestPacket;
    // 连接池对象
    private ClientConnectionManager mClientConnectionManager;

    public HttpClientExecutor(AbRequestPacket request, OnResponseListener onResponseListener) {
        super(request, onResponseListener);
        mRequestPacket = getRequestPacket();
    }

    @Override
    protected AbResponsePacket requestNetwork() throws ClientProtocolException,
            IOException {

        // 声明HttpResponse对象
        HttpResponse mHttpResponse = null;

        if (CommonUtils.isEmpty(mRequestPacket))
            throw new NullPointerException("request packet is null!");

        // 获取请求连接地址
        String uri = mRequestPacket.getUri();
        // 获取请求表单
        HttpRequestForm form = mRequestPacket.getHttpRequestForm();
        // 获取请求Client
        mHttpResponse = execHttpClient(form, uri);
        // 获取请求接口
        OnRequestListener mRequestListener = this.getOnRequestListener();

        // 提供一个OnRequestListener接口，并调用doResponse()方法对请求进行操作，最后得到请求数据包，并返回
        if (CommonUtils.isEmpty(mRequestListener))
            throw new NullPointerException("request listener is null!");

        return mRequestListener.doResponse(this, mHttpResponse);

    }

    /**
     * Client执行操作获取HttpResponse
     *
     * @param form 请求表单
     * @param uri  请求地址
     * @throws org.apache.http.client.ClientProtocolException
     * @throws java.io.IOException
     */
    protected HttpResponse execHttpClient(HttpRequestForm form, String uri)
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
        HttpUtils.addHeaders(requestBase, mRequestPacket.getHeaders());
        generateDefaultHttpClient();
        // 执行操作并获取HttpResponse
        mHttpResponse = mHttpClient.execute(requestBase);

        return mHttpResponse;
    }

    /**
     * 获取HttpClient对象
     *
     * @return HttpClient对象
     */
    protected HttpClient generateDefaultHttpClient() {

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

            mClientConnectionManager = new ThreadSafeClientConnManager(
                    httpParams, mSchemeRegistry);

            // 实例化一个默认HttpClient对象
            mHttpClient = new DefaultHttpClient(mClientConnectionManager,
                    httpParams);
        }
        return mHttpClient;

    }

    /**
     * 获取HttpClient对象
     *
     * @return HttpClient对象
     */
    public HttpClient getHttpClient() {
        return mHttpClient;
    }

    /**
     * 设置HttpClient对象
     *
     * @param mHttpClient HttpClient对象
     */
    public void setHttpClient(HttpClient mHttpClient) {
        this.mHttpClient = mHttpClient;
    }

    /**
     * 获取连接池对象
     *
     * @return 连接池对象
     */
    public ClientConnectionManager getClientConnectionManager() {
        return mClientConnectionManager;
    }

    /**
     * 设置连接池对象
     *
     * @param clientConnectionManager 连接池对象
     */
    public void setClientConnectionManager(ClientConnectionManager clientConnectionManager) {
        this.mClientConnectionManager = clientConnectionManager;
    }

    @Override
    public void closeConnections() {

        HttpClient mHttpClient = getHttpClient();
        if (mHttpClient == null) throw new NullPointerException("HttpClient is null!");
        // 关闭池中空闲连接
        mHttpClient.getConnectionManager().closeIdleConnections(0, TimeUnit.SECONDS);
        // 关闭池中所有过期连接
        mHttpClient.getConnectionManager().closeExpiredConnections();
        release();

    }

    @Override
    public void release() {

        HttpClient mHttpClient = getHttpClient();
        if (mHttpClient != null) return;
        // 关闭连接池
        mHttpClient.getConnectionManager().shutdown();
        mHttpClient = null;
        System.gc();

    }

}
