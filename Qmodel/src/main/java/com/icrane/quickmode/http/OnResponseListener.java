package com.icrane.quickmode.http;


import com.icrane.quickmode.http.exec.data.packet.AbResponsePacket;

public interface OnResponseListener {
    /**
     * 请求成功,接收数据包
     *
     * @param response
     */
    public Object onReceive(AbResponsePacket response);

    /**
     * 作为更新操作的地方
     *
     * @param obj
     */
    public void onUpdate(Object obj);

    /**
     * 请求失败,接收数据包
     *
     * @param response
     */
    public void onError(AbResponsePacket response);
}
