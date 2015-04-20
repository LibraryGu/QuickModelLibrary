package com.icrane.quickmode.http.exec.client;


import com.icrane.quickmode.app.Releasable;
import com.icrane.quickmode.http.exec.data.packet.AbResponsePacket;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.util.Observable;

@SuppressWarnings("ALL")
public abstract class AbClientExecutor extends Observable implements Releasable {
	/**
	 * 执行网络操作
	 * 
	 * @return
	 */
	public abstract void execute();

	/**
	 * 绑定HttpExecutorManager
	 * 
	 * @param manager
	 *            http执行管理者
	 */
	public abstract void bindExecutorManager(HttpExecutorManager manager);

	/**
	 * 执行请求操作
	 * 
	 * @return
	 * @throws org.apache.http.client.ClientProtocolException
	 * @throws java.io.IOException
	 */
	protected abstract AbResponsePacket requestNetwork()
			throws ClientProtocolException, IOException;

	/**
	 * 请求缓存
	 * 
	 * @param completeUri
	 *            缓存对应的uri
	 * @return
	 */
	protected abstract AbResponsePacket requestCache(String completeUri);
	
	/**
	 * 关闭连接，这里是关闭空闲和过期的连接
	 */
	public abstract void closeConnections();

}
