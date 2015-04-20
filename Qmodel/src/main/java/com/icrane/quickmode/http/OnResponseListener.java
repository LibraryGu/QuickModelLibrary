package com.icrane.quickmode.http;


import com.icrane.quickmode.http.exec.client.AbClientExecutor;
import com.icrane.quickmode.http.exec.data.packet.AbResponsePacket;

@SuppressWarnings("ALL")
public interface OnResponseListener<T> {

    /**
     * 处理响应数据
     *
     * @param executor 进行网络请求的执行对象
     * @param response 进行网络请求得到的响应对象
     * @return 返回数据包
     */
    public AbResponsePacket handleResponse(AbClientExecutor executor, T response);

    /**
     * 请求成功,接收数据包
     *
     * @param response 响应包
     */
    public Object onReceive(AbResponsePacket response);

    /**
     * 作为更新操作的地方
     *
     * @param obj 返回数据
     */
    public void onUpdate(Object obj);

    /**
     * 请求失败,接收数据包
     *
     * @param response 响应包
     */
    public void onError(AbResponsePacket response);
}
