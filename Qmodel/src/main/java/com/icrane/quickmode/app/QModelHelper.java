package com.icrane.quickmode.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;

import com.icrane.quickmode.app.activity.ActivityScenes;
import com.icrane.quickmode.app.fragment.utils.FragmentGenerator;
import com.icrane.quickmode.app.fragment.utils.FragmentHandler;
import com.icrane.quickmode.device.Bluetooth;
import com.icrane.quickmode.device.Dimens;
import com.icrane.quickmode.device.Storage;
import com.icrane.quickmode.http.exec.client.HttpExecutorManager;
import com.icrane.quickmode.utils.common.CommonUtils;
import com.icrane.quickmode.utils.image.ImageLoaderUtils;

/**
 * Created by gujiwen on 15/4/15.
 */
public final class QModelHelper implements Releasable {

    private static QModelHelper mQModelHelper = null;

    // 这个类可以执行跳转功能的
    private ActivityScenes mActivityScenes;
    // 蓝牙对象，用于管理蓝牙功能
    private Bluetooth mBluetooth;
    // 设备像素转化对象，用于手机设备像素转化，适配机型等；
    private Dimens mDeviceDimens;
    // 设备文件操作对象，用于操作手机设备中的文件，注意添加操作文件权限；
    private Storage mDeviceStorage;
    // Fragment管理对象，封装了对Fragment的切换，状态的保存，动画等；
    private FragmentHandler mFragmentHandler;
    // Fragment生成器
    private FragmentGenerator mFragmentGenerator;
    // 网络管理对象，可以使用此类访问网络或检测网络等，注意添加访问权限；
    private HttpExecutorManager mHttpExecutorManager;
    // 图片加载工具类
    private ImageLoaderUtils mImageLoaderUtils;

    private QModelHelper() {
        init();
    }

    /**
     * 获取实例
     *
     * @return 获取实例
     */
    public static final QModelHelper getInstance() {
        if (CommonUtils.isEmpty(mQModelHelper)) {
            mQModelHelper = new QModelHelper();
        }
        return mQModelHelper;
    }

    /**
     * 初始化
     */
    protected void init() {
        Activity activity = QModel.getTopActivity();
        if (activity instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) activity;
            this.mFragmentGenerator = FragmentGenerator.getInstance(fragmentActivity.getSupportFragmentManager());
            this.mFragmentHandler = FragmentHandler.getInstance(mFragmentGenerator);
        }
        this.mBluetooth = Bluetooth.getInstance();
        this.mDeviceDimens = Dimens.getInstance();
        this.mDeviceStorage = Storage.getDefaultStorage();
        this.mHttpExecutorManager = HttpExecutorManager.getInstance();
        this.mImageLoaderUtils = ImageLoaderUtils.getDefaultImageLoaderUtils();

    }

    /**
     * 获取Activity切换器
     *
     * @return Activity切换器
     */
    public ActivityScenes getActivityScenes() {
        return mActivityScenes;
    }

    /**
     * 获取Activity切换器
     *
     * @param intent 传入一个Intent对象
     * @return Activity切换器
     */
    public ActivityScenes getActivityScenes(Intent intent) {
        mActivityScenes = new ActivityScenes();
        mActivityScenes.setIntent(intent);
        return mActivityScenes;
    }

    /**
     * 获取Activity切换器
     *
     * @param action intent的动作
     * @return Activity切换器
     */
    public ActivityScenes getActivityScenes(String action) {
        return getActivityScenes(action, null, null);
    }

    /**
     * 获取Activity切换器
     *
     * @param action intent的动作
     * @param uri    统一资源标识符
     * @return Activity切换器
     */
    public ActivityScenes getActivityScenes(String action, Uri uri) {
        return getActivityScenes(action, uri, null);
    }

    /**
     * 获取Activity切换器
     *
     * @param cls 要切换到的Activity
     * @return Activity切换器
     */
    public ActivityScenes getActivityScenes(Class<?> cls) {
        return getActivityScenes(null, null, cls);
    }

    /**
     * 获取Activity切换器
     *
     * @param action intent的动作
     * @param uri    统一资源标识符
     * @param cls    要切换到的Activity
     * @return Activity切换器
     */
    public ActivityScenes getActivityScenes(String action, Uri uri, Class<?> cls) {
        mActivityScenes = new ActivityScenes();
        Intent sceneIntent = mActivityScenes.getIntent();
        if (!CommonUtils.isEmpty(action))
            sceneIntent.setAction(action);
        if (!CommonUtils.isEmpty(uri))
            sceneIntent.setData(uri);
        if (!CommonUtils.isEmpty(cls))
            sceneIntent.setClass(QModel.getTopActivity(), cls);
        return mActivityScenes;
    }

    /**
     * 获取fragment生成器
     *
     * @return fragment生成器
     */
    public FragmentGenerator getFragmentGenerator() {
        return mFragmentGenerator;
    }

    /**
     * 获取fragment管理对象
     *
     * @return fragment管理对象
     */
    public FragmentHandler getFragmentHandler() {
        return mFragmentHandler;
    }

    /**
     * 获取DeviceDimens对象
     *
     * @return DeviceDimens对象
     */
    public Dimens getDeviceDimens() {
        return mDeviceDimens;
    }

    /**
     * 获取DeviceFileOperations对象
     *
     * @return DeviceFileOperations对象
     */
    public Storage getDeviceStorage() {
        return mDeviceStorage;
    }

    /**
     * 获取Bluetooth对象
     *
     * @return Bluetooth对象
     */
    public Bluetooth getBluetooth() {
        return mBluetooth;
    }

    /**
     * 获取NetworkManager对象
     *
     * @return NetworkManager对象
     */
    public HttpExecutorManager getHttpExecutorManager() {
        return mHttpExecutorManager;
    }

    /**
     * 获取ImageLoaderUtils对象
     *
     * @return ImageLoaderUtils对象
     */
    public ImageLoaderUtils getImageLoaderUtils() {
        return mImageLoaderUtils;
    }

    @Override
    public void release() {
        if (mQModelHelper != null) {
            mBluetooth.release();
            mDeviceDimens.release();
            mDeviceStorage.release();
            mFragmentHandler.release();
            mImageLoaderUtils.release();
            mFragmentGenerator.release();
            mHttpExecutorManager.release();
            mQModelHelper = null;
        }
    }
}
