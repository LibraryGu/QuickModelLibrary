package com.icrane.quickmode.http;

import com.icrane.quickmode.http.exec.client.HttpClientExecutor;
import com.icrane.quickmode.http.exec.data.packet.AbResponsePacket;

import org.apache.http.HttpResponse;

public interface OnRequestListener {
	/**
	 * 处理响应数据
	 * 
	 * @param executor
	 *            进行网络请求的执行对象
	 * @param response
	 *            进行网络请求得到的响应对象
	 * @return 返回数据包
	 */
	public AbResponsePacket doResponse(HttpClientExecutor executor,
                                     HttpResponse response);
}
