package com.icrane.quickmode.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * 这个缓存类简单的实现了LRU算法,当此类调用put或putAll方法添加数据时,
 * 会调用removeEldestEntry()方法让缓存数量保持与容量大小相同, 并且会移除最旧的缓存对象。
 *
 * @param <K> 键类型
 * @param <V> 对应值类型
 * @author gujiwen
 */
public abstract class BasicLruCache<K, V> extends LinkedHashMap<K, V> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // 默认最大容量
    private static final int DEFAULT_MAX_CAPACITY = 20;
    // 默认负荷系数
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    // 是否已经执行清除
    private boolean isClear;
    // 最大容量
    private int maxCapacity;
    // LRU监听器
    private OnLruCacheListener mOnLruCachelistener;

    public BasicLruCache(int capacity) {
        super(capacity <= 0 ? DEFAULT_MAX_CAPACITY : capacity,
                DEFAULT_LOAD_FACTOR, true);
        this.maxCapacity = capacity <= 0 ? DEFAULT_MAX_CAPACITY : capacity;
    }

    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(key);
    }

    @Override
    public V get(Object key) {
        return super.get(key);
    }

    @Override
    public V put(K key, V value) {
        return super.put(key, value);
    }

    public int size() {
        return super.size();
    }

    public void clear() {
        if (isClearable()) {
            super.clear();
        }
    }

    public Collection<Entry<K, V>> getAll() {
        return new ArrayList<Entry<K, V>>(super.entrySet());
    }

    public boolean isClearable() {
        return isClear;
    }

    public void setClearable(boolean isClear) {
        this.isClear = isClear;
    }

    public int maxCapacity() {
        return maxCapacity;
    }

    public OnLruCacheListener getOnLruCacheListener() {
        return mOnLruCachelistener;
    }

    public void setOnLruCacheListener(OnLruCacheListener listener) {
        this.mOnLruCachelistener = listener;
    }

}
