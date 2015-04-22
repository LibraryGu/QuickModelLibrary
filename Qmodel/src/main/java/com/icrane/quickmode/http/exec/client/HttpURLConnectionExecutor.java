package com.icrane.quickmode.http.exec.client;

import com.icrane.quickmode.http.OnRequestListener;
import com.icrane.quickmode.http.OnResponseListener;
import com.icrane.quickmode.http.RequestMethod;
import com.icrane.quickmode.http.exec.data.HttpRequestForm;
import com.icrane.quickmode.http.exec.data.packet.AbRequestPacket;
import com.icrane.quickmode.http.exec.data.packet.AbResponsePacket;
import com.icrane.quickmode.utils.common.CommonUtils;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by gujiwen on 15/4/20.
 */
public class HttpURLConnectionExecutor extends AbClientExecutor<HttpURLConnection> {

    //请求数据包
    private AbRequestPacket mRequestPacket;
    // 支持HTTPS安全通信对象
    private HttpURLConnection mHttpURLConnection;

    public HttpURLConnectionExecutor(AbRequestPacket request, OnResponseListener onResponseListener) {
        super(request, onResponseListener);
        mRequestPacket = getRequestPacket();

    }

    @Override
    protected AbResponsePacket requestNetwork() throws ClientProtocolException, IOException {

        // 声明HttpResponse对象
        HttpURLConnection mHttpURLConnection = null;

        if (CommonUtils.isEmpty(mRequestPacket))
            throw new NullPointerException("request packet is null!");

        // 获取请求连接地址
        String uri = mRequestPacket.getUri();
        // 获取请求表单
        HttpRequestForm form = mRequestPacket.getHttpRequestForm();
        // 获取请求Client
        mHttpURLConnection = execHttpURLConnection(form, uri);
        // 获取请求接口
        OnRequestListener mRequestListener = this.getOnRequestListener();

        // 提供一个OnRequestListener接口，并调用doResponse()方法对请求进行操作，最后得到请求数据包，并返回
        if (CommonUtils.isEmpty(mRequestListener))
            throw new NullPointerException("request listener is null!");

        return mRequestListener.doResponse(this, mHttpURLConnection);
    }

    /**
     * 获取默认HttpsURLConnection对象
     *
     * @param mURL URL对象
     * @return 默认HttpsURLConnection对象
     */
    protected HttpURLConnection generateDefaultHttpURLConnection(URL mURL) {
        try {
            if (mHttpURLConnection == null) {
                mHttpURLConnection = (HttpURLConnection) mURL.openConnection();
                mHttpURLConnection.setRequestProperty("Accept-Encoding", "identity");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mHttpURLConnection;
    }

    /**
     * 执行操作execHttpURLConnection
     *
     * @param form 请求表单
     * @param uri  地址
     * @return HttpURLConnection
     * @throws java.net.ProtocolException     Http协议异常
     * @throws java.net.MalformedURLException MalformedURLException异常
     */
    protected HttpURLConnection execHttpURLConnection(HttpRequestForm form, String uri) throws ProtocolException, MalformedURLException {

        // 判断请求地址是否为null
        if (uri.equals("") || uri == null) throw new NullPointerException("uri is null!");
        // 判断请求表单是否为null
        if (CommonUtils.isEmpty(form)) throw new NullPointerException("form is null!");

        RequestMethod method = form.getRequestMethod();
        reset(uri, method);

        return mHttpURLConnection;
    }

    /**
     * 重设置
     *
     * @param uri    地址
     * @param method 请求方法
     * @throws java.net.ProtocolException     Http协议异常
     * @throws java.net.MalformedURLException MalformedURLException异常
     */
    protected void reset(String uri, RequestMethod method) throws MalformedURLException, ProtocolException {
        generateDefaultHttpURLConnection(new URL(uri));
        if (method == RequestMethod.POST) mHttpURLConnection.setDoOutput(true);
        mHttpURLConnection.setRequestMethod(method.obtain());
    }

    /**
     * 获取HttpsURLConnection
     *
     * @return HttpsURLConnection对象
     */
    public HttpURLConnection getHttpsURLConnection() {
        return mHttpURLConnection;
    }

    /**
     * 设置HttpsURLConnection
     *
     * @param mHttpsURLConnection HttpsURLConnection对象
     */
    public void setmHttpsURLConnection(HttpsURLConnection mHttpsURLConnection) {
        this.mHttpURLConnection = mHttpsURLConnection;
    }

    @Override
    public void closeConnections() {

        if (CommonUtils.isEmpty(mHttpURLConnection))
            throw new NullPointerException("HttpURLConnection is null");
        mHttpURLConnection.disconnect();
        release();

    }

    @Override
    public void release() {

        if (CommonUtils.isEmpty(mHttpURLConnection)) return;
        mHttpURLConnection = null;
        System.gc();

    }

}
