package com.icrane.quickmode.cache;

public interface OnLruCacheListener {
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void onLeastRecentlyUsed(Object key, Object value);
}
