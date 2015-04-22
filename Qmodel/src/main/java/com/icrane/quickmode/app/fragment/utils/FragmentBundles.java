package com.icrane.quickmode.app.fragment.utils;

import android.os.Bundle;

import com.icrane.quickmode.utils.common.CommonUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class FragmentBundles extends HashMap<String, Bundle> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static FragmentBundles mFragmentBundle = null;
	// 公平锁
	private final Lock lock = new ReentrantLock();

	private FragmentBundles() {
	}

	/**
	 * 获取默认缓存对象
	 * 
	 * @return 默认缓存对象
	 */
	public static final FragmentBundles getDefault() {
		return getInstance();
	}

	/**
	 * 获取缓存对象
	 * 
	 * @return 缓存对象
	 */
	public static final FragmentBundles getInstance() {
		if (CommonUtils.isEmpty(mFragmentBundle)) {
			mFragmentBundle = new FragmentBundles();
		}
		return mFragmentBundle;
	}

	@Override
	public boolean containsKey(Object key) {
		try {
			lock.lock();
			return super.containsKey(key);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Bundle get(Object key) {
		try {
			lock.lock();
			return super.get(key);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Bundle put(String key, Bundle value) {
		try {
			lock.lock();
			return super.put(key, value);
		} finally {
			lock.unlock();
		}
	}

	public int size() {
		try {
			lock.lock();
			return super.size();
		} finally {
			lock.unlock();
		}
	}

	public void clear() {
		try {
			lock.lock();
			super.clear();
		} finally {
			lock.unlock();
		}
	}

	public Collection<Entry<String ,Bundle>> getAll() {
		try {
			lock.lock();
			return new ArrayList<Entry<String ,Bundle>>(super.entrySet());
		} finally {
			lock.unlock();
		}
	}

}
