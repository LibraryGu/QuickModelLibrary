package com.icrane.quickmode.cache;

public interface OnLruCacheListener {
    /**
     * @param key   键
     * @param value 值
     */
    public void onLeastRecentlyUsed(Object key, Object value);
}
