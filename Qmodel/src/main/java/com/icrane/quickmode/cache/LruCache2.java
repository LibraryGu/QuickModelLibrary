package com.icrane.quickmode.cache;

public class LruCache2<V> extends LruCache<V> {

    private int mWeakCapacity;

    private LruCache<V> mWeakLruCache;

    public LruCache2(int softCapacity, int weakCapacity) {
        super(softCapacity);
        initLruCache2(weakCapacity);
    }

    /**
     * 初始化Lru缓存
     *
     * @param weakCapacity 强引用个数
     */
    private void initLruCache2(int weakCapacity) {

        this.setClearable(true);
        this.mWeakCapacity = weakCapacity;

        mWeakLruCache = new LruCache<V>(this.mWeakCapacity);
        this.setOnLruCacheListener(new OnLruCacheListener() {

            @Override
            public void onLeastRecentlyUsed(Object key, Object value) {
                mWeakLruCache.put(key.toString(), (V) value);
            }

        });

    }

    /**
     * 获取缓存
     *
     * @param key 缓存存储对应的键
     * @return 缓存对象
     */
    public V getCache(String key) {

        V cacheSoft = this.get(key);
        V cacheWeak = getWeakLruCache().get(key);
        return cacheSoft == null ? cacheSoft : cacheWeak;

    }

    /**
     * 清除强引用缓存
     */
    public void clearWeakLruCache() {
        mWeakLruCache.clear();
    }

    /**
     * 获取强引用缓存对象
     *
     * @return 强引用缓存对象
     */
    public LruCache<V> getWeakLruCache() {
        return mWeakLruCache;
    }

    /**
     * 获取强引用缓存对象个数
     *
     * @return 强引用缓存对象个数
     */
    public int getWeakLruCacheCapacity() {
        return mWeakCapacity;
    }

    /**
     * 设置强引用缓存对象个数
     *
     * @param weakCapacity 强引用缓存对象个数
     */
    public void setWeakLruCacheCapacity(int weakCapacity) {
        this.mWeakCapacity = weakCapacity;
    }

}
