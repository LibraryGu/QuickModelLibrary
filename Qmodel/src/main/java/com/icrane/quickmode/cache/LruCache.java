package com.icrane.quickmode.cache;

import com.icrane.quickmode.utils.common.CommonUtils;

public class LruCache<V> extends BasicLruCache<String, V> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LruCache(int capacity) {
		super(capacity);
	}

	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry<String, V> eldest) {

		OnLruCacheListener onLruCacheListener = this.getOnLruCacheListener();
		this.setClearable(true);

		if (!CommonUtils.isEmpty(onLruCacheListener)) {
			onLruCacheListener.onLeastRecentlyUsed(eldest.getKey() ,eldest.getValue());
		}
		return size() > maxCapacity();
	}

}
