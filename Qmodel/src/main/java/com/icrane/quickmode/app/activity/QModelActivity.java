package com.icrane.quickmode.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.icrane.quickmode.app.QModel;
import com.icrane.quickmode.app.fragment.utils.FragmentGenerator;
import com.icrane.quickmode.app.fragment.utils.FragmentHandler;
import com.icrane.quickmode.utils.image.ImageLoaderUtils;
import com.icrane.quickmode.widget.actionbar.ActionBar;
import com.icrane.quickmode.widget.actionbar.ActionBarLayout;

@SuppressWarnings("ALL")
public class QModelActivity extends FragmentActivity {

    public static Handler mainHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            handleCallback.handleMessage(msg);
        }
    };

    private ViewGroup basicContentView;
    // 自定义的ActionBar
    private ActionBarLayout mQModelActionBar;
    // 这是最基础的handler对象，用于在主线程中更新数据等；
    private static HandleCallback handleCallback;
    // 系统Application
    private QModel mQModel;

    /**
     * 实现Toast显示类
     *
     * @author Administrator
     */
    public static class ToastAsist {

        /**
         * 显示提示信息
         *
         * @param context  上下文
         * @param text     字符串序列
         * @param duration 提示在屏幕停留的时间
         */
        public static void showToast(Context context, CharSequence text,
                                     int duration) {
            Toast.makeText(context, text, duration).show();
        }

        /**
         * 显示提示信息
         *
         * @param context  上下文
         * @param resId    字符串资源id
         * @param duration 提示在屏幕停留的时间
         */
        public static void showToast(Context context, int resId, int duration) {
            showToast(context, context.getString(resId), duration);
        }

        /**
         * 显示提示信息，提示在屏幕停留的时间较长
         *
         * @param context 上下文
         * @param resId   字符串资源id
         */
        public static void showLongToast(Context context, int resId) {
            showLongToast(context, context.getString(resId));
        }

        /**
         * 显示提示信息，提示在屏幕停留的时间较长
         *
         * @param context 上下文
         * @param text    字符串序列
         */
        public static void showLongToast(Context context, CharSequence text) {
            showToast(context, text, Toast.LENGTH_LONG);
        }

        /**
         * 显示提示信息，提示在屏幕停留的时间较短
         *
         * @param context 上下文
         * @param resId   字符串资源id
         */
        public static void showShortToast(Context context, int resId) {
            showShortToast(context, context.getString(resId));
        }

        /**
         * 显示提示信息，提示在屏幕停留的时间较短
         *
         * @param context 上下文
         * @param text    字符串序列
         */
        public static void showShortToast(Context context, CharSequence text) {
            showToast(context, text, Toast.LENGTH_SHORT);
        }

        /**
         * 由一个Handler来管理提示
         *
         * @param context  上下文
         * @param resId    字符串资源id
         * @param duration 提示在屏幕停留的时间
         */
        public static void showHandleToast(Context context, final int resId,
                                           final int duration) {
            showHandleToast(context, context.getString(resId), duration);
        }

        /**
         * 由一个Handler来管理提示
         *
         * @param context  上下文
         * @param text     字符串序列
         * @param duration 提示在屏幕停留的时间
         */
        public static void showHandleToast(final Context context,
                                           final CharSequence text, final int duration) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    showToast(context, text, duration);
                }
            });
        }
    }

    /**
     * handle接口
     *
     * @author Administrator
     */
    public interface HandleCallback {
        public void handleMessage(Message msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //此处获取实例,需要调用QModel.init(Context context)方法，init方法只需要调用一次即可；
        mQModel = QModel.getInstance();
        //将当前Activity压入栈中
        mQModel.attemptPushActivityToStack(this);

    }

    @Override
    protected void onStop() {
        ImageLoaderUtils.stop();
        super.onStop();
    }

    @Override
    protected void onResume() {
        ImageLoaderUtils.resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        ImageLoaderUtils.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 获取handle的回调
     *
     * @return handle的回调接口
     */
    public static HandleCallback getHandleCallback() {
        return handleCallback;
    }

    /**
     * 设置handle的回调
     *
     * @param handleCallback handle的回调接口
     */
    public static void setHandleCallback(HandleCallback handleCallback) {
        QModelActivity.handleCallback = handleCallback;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 这里为Fragment执行onActivityResult()方法
        Fragment currentFragment = FragmentHandler.getInstance(FragmentGenerator
                .getInstance(getSupportFragmentManager()))
                .getCurrentFragment();
        if (currentFragment != null) {
            currentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

// ---------------------- Custom Method ----------------------//

    @Override
    public void setContentView(int layoutResID) {
        this.mQModelActionBar = new ActionBarLayout(this);
        this.basicContentView = (ViewGroup) getLayoutInflater().inflate(
                layoutResID, this.mQModelActionBar, false);
        this.mQModelActionBar.addView(this.basicContentView);
        super.setContentView(this.mQModelActionBar);
    }

    /**
     * 获取ActionBar实例
     *
     * @return ActionBar实例
     */
    protected ActionBar getQModelActionBar() {
        return this.mQModelActionBar;
    }

    /**
     * 获取布局视图
     *
     * @return 布局视图
     */
    public ViewGroup getContentView() {
        return basicContentView;
    }

    /**
     * 除去标题栏，注意这个方法需要在onCreate()方法前使用。
     */
    public void windowNoTitle() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * 使屏幕全屏，注意这个方法需要在onCreate()方法前使用。
     */
    public void windowFullScreen() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 获取系统Application
     *
     * @return 系统Application
     */
    public QModel getQModel() {
        return mQModel;
    }

    /**
     * 获取基类中的handler对象
     *
     * @return
     */
    public Handler getMainHandler() {
        return mainHandler;
    }

    /**
     * 启动一个mainThread以外的线程
     *
     * @param r Runnable接口
     */
    public void runOnThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.start();
    }

    /**
     * 执行异步任务
     *
     * @param task     异步任务类
     * @param params   参数
     * @param <Params> 参数类型
     */
    public <Params> void runOnAsyncTask(AsyncTask task, Params... params) {
        task.execute(params);
    }

}
