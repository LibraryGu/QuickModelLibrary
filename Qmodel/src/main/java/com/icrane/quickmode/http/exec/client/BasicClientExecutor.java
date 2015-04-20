package com.icrane.quickmode.http.exec.client;

import com.icrane.quickmode.cache.BasicLruCache;
import com.icrane.quickmode.http.OnRequestListener;
import com.icrane.quickmode.http.exec.data.packet.AbRequestPacket;
import com.icrane.quickmode.http.handler.AsyncBasicResponse;

@SuppressWarnings("ALL")
public abstract class BasicClientExecutor extends AbClientExecutor implements
        Runnable {

    @Override
    public void run() {
        this.execute();
    }

    /**
     * 获取请求包对象
     *
     * @return
     */
    public abstract AbRequestPacket getRequestPacket();

    /**
     * 设置请求包对象
     *
     * @param requestPacket
     */
    public abstract void setRequestPacket(AbRequestPacket requestPacket);

    /**
     * 获取请求监听器
     *
     * @return
     */
    public abstract OnRequestListener getOnRequestListener();

    /**
     * 设置请求监听器
     *
     * @param onRequestListener
     */
    public abstract void setOnRequestListener(
            OnRequestListener onRequestListener);

    /**
     * 获取响应处理类
     *
     * @return
     */
    public abstract AsyncBasicResponse getAsyncBasicResponse();

    /**
     * 设置响应处理类
     *
     * @param asyncBasicResponse
     */
    public abstract void setAsyncBasicResponse(AsyncBasicResponse asyncBasicResponse);

    /**
     * 获取网络任务管理器
     *
     * @return 网络任务管理器
     */
    public abstract HttpExecutorManager getHttpExecutorManager();

    /**
     * 获取Lru缓存
     *
     * @return Lru缓存
     */
    public abstract BasicLruCache getLruCache();

    /**
     * 设置Lru缓存
     *
     * @param lruCache Lru缓存
     */
    public abstract void setLruCache(BasicLruCache lruCache);

}
