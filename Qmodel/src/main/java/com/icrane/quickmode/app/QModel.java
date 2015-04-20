package com.icrane.quickmode.app;

import android.app.Activity;
import android.content.Context;

import com.icrane.quickmode.app.activity.ActivityStack;
import com.icrane.quickmode.app.activity.QModelActivity;
import com.icrane.quickmode.utils.common.LogUtils;

/**
 * Created by gujiwen on 15/4/15.
 */
@SuppressWarnings("ALL")
public final class QModel implements Releasable, IContextConvert<QModelActivity> {

    // Application
    private static QModel mQModel;
    // activity栈管理对象，内部封装了对activity在栈中的管理；
    private static ActivityStack mActivityStack;
    // 栈顶的Activity对象
    private static Activity activityContext;
    // 当前应用程序上下文对象
    private static Context applicationContext;
    // 判断是否已初始化
    private static boolean isInitialized = false;

    /**
     * 获取实例对象
     *
     * @return 实例对象
     */
    public static QModel getInstance() {
        if (!isInitialized())
            throw new ExceptionInInitializerError("It is not initialized!");
        if (mQModel == null) {
            mQModel = new QModel();
        }
        return mQModel;
    }

    /**
     * * 这个方法必须先调用，如果继承的是原生系统的Activity，必须在onCreate中使用AMPApplication的此方法管理
     * QModel qmApplication = QModel.getInstance();
     * QModel.init(activity);
     *
     * @param context 对应Activity
     */
    public static void init(Context context) {
        if (!isInitialized) {
            if (context == null)
                throw new ExceptionInInitializerError("Init failed:Context is null!");
            applicationContext = context;
            mActivityStack = ActivityStack.getInstance();
            isInitialized = true;
            LogUtils.d("QModel init:" + isInitialized);
        }
    }

    /**
     * 判断是否进行了初始化
     *
     * @return boolean类型，如果返回true表示已经初始化，反之则返回false
     */
    public static boolean isInitialized() {
        return isInitialized;
    }

    /**
     * 获取当前应用程序上下文
     *
     * @return 当前应用程序上下文
     */
    public static Context getApplicationContext() {
        return applicationContext;
    }

    /**
     * 将当前Activity压如栈中，如果继承的是原生系统的Activity，必须在onCreate()中使用QModel管理Activity
     * QModel mQModel = QModel.getInstance();
     * QModel.attemptPushActivityToStack(activity);
     *
     * @param activity 需要压入栈中的Activity对象
     */
    public void attemptPushActivityToStack(Activity activity) {
        mActivityStack.push(activity);
    }

    /**
     * 将当前Activity弹出栈中，如果继承的是原生系统的Activity，必须在onDestroy中使用QModel管理Activity
     * QModel mQModel = QModel.getInstance();
     * QModel.attemptPopActivityToStack(activity);
     *
     * @param activity 需要弹出栈中的Activity对象
     */
    public void attemptPopActivityToStack(Activity activity) {
        mActivityStack.pop(activity);
    }

    /**
     * 退出进程
     */
    public void exitApplication() {
        LogUtils.d("QModel exiting...");
        release();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        System.gc();
    }

    @Override
    public void release() {
        if (applicationContext != null) {
            mActivityStack.release();
            QModelHelper.getInstance().release();
            applicationContext = null;
        }
    }

    /**
     * 获取栈顶的Activity
     *
     * @return 栈顶的Activity
     */
    public static Activity getTopActivity() {
        activityContext = mActivityStack.getStackTop();
        return activityContext;
    }

    @Override
    public QModelActivity getActivityContext() {
        if (activityContext == null)
            throw new NullPointerException("obtain Activity Context is faild,because 'activityContext' is null!");
        if (!(activityContext instanceof QModelActivity))
            throw new ClassCastException(activityContext.getClass() + " cannot be cast to " + QModelActivity.class);
        return (QModelActivity) activityContext;
    }
}
