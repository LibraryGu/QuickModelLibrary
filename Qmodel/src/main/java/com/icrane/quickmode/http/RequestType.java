package com.icrane.quickmode.http;

/**
 * 请求类型
 *
 * @author gujiwen
 */
public enum RequestType {
    /**
     * 直接请求网络
     */
    REQUEST_MODE_NETWORK,
    /**
     * 直接请求缓存
     */
    REQUEST_MODE_CACHE,
    /**
     * 检查缓存，如果缓存不存在则请求网络
     */
    REQUEST_MODE_CHECKING_CACHE
}
