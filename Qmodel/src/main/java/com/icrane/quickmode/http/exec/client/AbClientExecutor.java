package com.icrane.quickmode.http.exec.client;


import com.icrane.quickmode.app.Releasable;
import com.icrane.quickmode.cache.BasicLruCache;
import com.icrane.quickmode.cache.CacheType;
import com.icrane.quickmode.http.HttpDataType;
import com.icrane.quickmode.http.HttpError;
import com.icrane.quickmode.http.OnRequestListener;
import com.icrane.quickmode.http.OnResponseListener;
import com.icrane.quickmode.http.RequestType;
import com.icrane.quickmode.http.exec.data.packet.AbRequestPacket;
import com.icrane.quickmode.http.exec.data.packet.AbResponsePacket;
import com.icrane.quickmode.http.exec.data.packet.impl.HttpRequestPacket;
import com.icrane.quickmode.http.exec.data.packet.impl.HttpResponsePacket;
import com.icrane.quickmode.utils.common.CommonUtils;
import com.icrane.quickmode.utils.common.HttpUtils;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.util.Observable;

import javax.net.ssl.HttpsURLConnection;

public abstract class AbClientExecutor<T> extends Observable implements Releasable, Runnable {

    // 支持HTTPS安全通信对象
    private HttpsURLConnection mHttpsURLConnection = null;
    // Lru缓存
    private BasicLruCache mLruCache;
    // 请求数据包
    private AbRequestPacket mRequestPacket;
    // 响应数据包
    private AbResponsePacket mResponsePacket;
    // 读取接口
    private OnResponseListener mOnResponseListener;
    // 网络请求执行者
    private HttpExecutorManager mHttpExecutorManager;

    // 请求监听器
    private OnRequestListener<T> mOnRequestListener = new OnRequestListener<T>() {
        @Override
        public AbResponsePacket doResponse(AbClientExecutor executor, T response) {
            return executor.getOnResponseListener().handleResponse(executor, response);
        }

    };

    /**
     * 构造方法
     *
     * @param request            请求包
     * @param onResponseListener 请求管理者
     */
    public AbClientExecutor(AbRequestPacket request, OnResponseListener onResponseListener) {

        this.mRequestPacket = (CommonUtils.isEmpty(request)) ? HttpRequestPacket.create() : request;
        this.mOnResponseListener = onResponseListener;
        CacheType cacheType = this.mRequestPacket.getCacheType();
        if (cacheType != null) {
            this.mLruCache = HttpUtils.switchCache(cacheType);
        } else {
            this.mLruCache = HttpUtils.switchCache(CacheType.LRU);
        }

    }

    /**
     * 绑定HttpExecutorManager
     *
     * @param manager http执行管理者
     */
    public void bindExecutorManager(HttpExecutorManager manager) {
        this.mHttpExecutorManager = manager;
        this.addObserver(this.mHttpExecutorManager);
    }

    /**
     * 执行网络操作
     */
    public void execute() {
        try {
            RequestType requestType = mRequestPacket.getRequestType();
            // 判断请求类型，进行请求数据
            switch (requestType) {

                case REQUEST_MODE_NETWORK:
                    mResponsePacket = requestNetwork();
                    break;
                case REQUEST_MODE_CACHE:
                    mResponsePacket = requestCache(mRequestPacket.getCompleteUri());
                    break;
                case REQUEST_MODE_CHECKING_CACHE:
                    mResponsePacket = requestCache(mRequestPacket.getCompleteUri());
                    if (CommonUtils.isEmpty(mResponsePacket.getCacheContent()))
                        mResponsePacket = requestNetwork();
                    break;

            }
            this.notifyPreUpdate(mResponsePacket);

        } catch (ClientProtocolException e) {

            this.getHttpExecutorManager().commitErrorMessage(HttpExecutorManager.ERROR, HttpError.ERROR_EXCEPTION,
                    mResponsePacket, e);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通知更新
     *
     * @param data 数据对象
     */
    protected void notifyPreUpdate(Object data) {
        this.setChanged();
        this.notifyObservers(data);
    }

    /**
     * 执行请求操作
     *
     * @return 返回响应包对象
     * @throws org.apache.http.client.ClientProtocolException {@link org.apache.http.client.ClientProtocolException}
     * @throws java.io.IOException                            {@link java.io.IOException}
     */
    protected abstract AbResponsePacket requestNetwork() throws ClientProtocolException, IOException;

    /**
     * 请求缓存
     *
     * @param completeUri 缓存对应的uri
     * @return 返回响应包对象
     */
    protected AbResponsePacket requestCache(String completeUri) {
        // 声明一个缓存字符串对象
        Object cacheContent = null;
        // 声明并实例化一个HttpDataPacket对象
        AbResponsePacket responsePacket = HttpResponsePacket.create();

        // 判断HttpRequestPacket对象是否为空
        if (CommonUtils.isEmpty(mRequestPacket))
            throw new NullPointerException("request packet is null!");
        // 获取缓存
        cacheContent = (!CommonUtils.isEmpty(mLruCache)) ? mLruCache.get(completeUri) : "";

        // 设置HttpResponsePacket对象
        responsePacket.setUri(mRequestPacket.getUri())
                .setDataType(HttpDataType.CACHE_CONTENT)
                .setUseMemoryCache(mRequestPacket.isUseMemoryCache())
                .setUseHardWareCache(mRequestPacket.isUseHardWareCache())
                .setRequestType(mRequestPacket.getRequestType())
                .setHttpRequestForm(mRequestPacket.getHttpRequestForm())
                .setCharset(mRequestPacket.getCharset())
                .setCompleteUri(mRequestPacket.getCompleteUri())
                .setCacheType(mRequestPacket.getCacheType())
                .setCacheContent(cacheContent);

        return responsePacket;
    }

    /**
     * 设置请求包对象
     *
     * @param requestPacket 请求包
     */
    public void setRequestPacket(AbRequestPacket requestPacket) {
        this.mRequestPacket = requestPacket;
    }

    /**
     * 获取请求包对象
     *
     * @return 返回请求包对象
     */
    public AbRequestPacket getRequestPacket() {
        return mRequestPacket;
    }

    /**
     * 获取响应处理类
     *
     * @return 返回响应监听器对象
     */
    public OnResponseListener getOnResponseListener() {
        return mOnResponseListener;
    }

    /**
     * 设置响应处理类
     *
     * @param onResponseListener 响应监听器对象
     */
    public void setOnResponseListener(OnResponseListener onResponseListener) {
        this.mOnResponseListener = onResponseListener;
    }

    /**
     * 获取请求监听器
     *
     * @return 返回请求监听器
     */
    public OnRequestListener getOnRequestListener() {
        return mOnRequestListener;
    }

    /**
     * 设置请求监听器
     *
     * @param onRequestListener 请求监听器
     */
    public void setOnRequestListener(OnRequestListener onRequestListener) {
        this.mOnRequestListener = onRequestListener;
    }

    /**
     * 获取网络任务管理器
     *
     * @return 网络任务管理器
     */
    public HttpExecutorManager getHttpExecutorManager() {
        return mHttpExecutorManager;
    }

    /**
     * 获取Lru缓存
     *
     * @return Lru缓存
     */
    public BasicLruCache getLruCache() {
        return mLruCache;
    }

    /**
     * 设置Lru缓存
     *
     * @param lruCache Lru缓存
     */
    public void setLruCache(BasicLruCache lruCache) {
        this.mLruCache = lruCache;
    }

    @Override
    public void run() {
        this.execute();
    }

    /**
     * 关闭连接，这里是关闭空闲和过期的连接
     */
    public abstract void closeConnections();

}
