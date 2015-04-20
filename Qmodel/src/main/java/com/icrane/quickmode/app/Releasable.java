package com.icrane.quickmode.app;

/**
 * Created by gujiwen on 15/4/8.
 */
@SuppressWarnings("ALL")
public interface Releasable {
    /**
     * 释放资源，这个方法在onDestroy()方法中调用
     */
    public void release();
}
