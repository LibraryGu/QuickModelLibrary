package com.icrane.quickmode.http.exec.client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;

import com.icrane.quickmode.app.QModel;
import com.icrane.quickmode.app.Releasable;
import com.icrane.quickmode.http.HttpError;
import com.icrane.quickmode.http.OnResponseListener;
import com.icrane.quickmode.http.exec.data.packet.AbResponsePacket;
import com.icrane.quickmode.http.exec.data.packet.impl.HttpResponsePacket;
import com.icrane.quickmode.utils.common.CommonUtils;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class HttpExecutorManager implements Observer, Releasable {

    /**
     * 错误标识码
     */
    public static final int ERROR = -1;
    /**
     * 更新标示符
     */
    public static final int UPDATE = 0;
    /**
     * 提交延时
     */
    public static final int COMMIT_DURATION = 55;
    /**
     * 关闭线程池并且等待任务完成的超时时间
     */
    public static final int SHUTDOWN_AWAIT_TERMINATION_TIMEOUT = 60;
    /**
     * 线程池
     */
    private ExecutorService mExecutorService;
    /**
     * 请求客户端
     */
    private AbClientExecutor mClientExecutor;
    /**
     * 连接管理者
     */
    private ConnectivityManager mConnectivityManager;
    /**
     * 请求执行者
     */
    private static HttpExecutorManager mHttpExecutorManager = null;

    /**
     * 设置Handler的回调
     */
    @SuppressLint("HandlerLeak")
    final Handler execHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            OnResponseListener asyncBasicResponse = mClientExecutor
                    .getOnResponseListener();
            if (!CommonUtils.isEmpty(asyncBasicResponse)) {
                switch (msg.what) {
                    case UPDATE:
                        asyncBasicResponse.onUpdate(msg.obj);
                        break;
                    case ERROR:
                        asyncBasicResponse.onError((AbResponsePacket) msg.obj);
                        break;
                }
            }
        }

    };

    @Override
    public synchronized void update(Observable observable, Object data) {
        Object dataObject = mClientExecutor.getOnResponseListener().onReceive((AbResponsePacket) data);
        commit(UPDATE, dataObject, COMMIT_DURATION);
    }

    /**
     * 构造方法
     */
    private HttpExecutorManager() {
        // 生成线程池
        mExecutorService = Executors.newCachedThreadPool();
        // 获取连接管理器
        mConnectivityManager = (ConnectivityManager) QModel.getTopActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 初始化HttpExecutorManager
     *
     * @return 网络管理者
     */
    public static HttpExecutorManager getInstance() {
        if (CommonUtils.isEmpty(mHttpExecutorManager)) {
            mHttpExecutorManager = new HttpExecutorManager();
        }
        return mHttpExecutorManager;
    }

    /**
     * 提交网络任务
     *
     * @param executor 进行网络请求的执行对象
     */
    public void execute(AbClientExecutor executor) {
        // 初始化
        this.mClientExecutor = executor;
        this.mClientExecutor.bindExecutorManager(this);
        mExecutorService.execute(this.mClientExecutor);
    }

    /**
     * 提交数据
     *
     * @param what     通知类型
     * @param obj      传输参数
     * @param duration 间隔时间
     */
    protected synchronized void commit(int what, Object obj, long duration) {
        Message msg = execHandler.obtainMessage();
        msg.what = what;
        msg.obj = obj;
        execHandler.sendMessageDelayed(msg, duration);
    }

    /**
     * 提交异常数据
     *
     * @param what           发送数据的类型
     * @param errorStatus    错误状态
     * @param responsePacket HttpRespDataPacket对象
     * @param obj            对应与错误状态，而传入不同类型数据
     */
    public synchronized void commitErrorMessage(int what,
                                                HttpError errorStatus, AbResponsePacket responsePacket, Object obj) {

        HttpError httpError = errorStatus;
        switch (httpError) {
            case ERROR_NONE:
                break;
            case ERROR_STR:
                httpError.setErrorMessage((String) obj);
                break;
            case ERROR_EXCEPTION:
                httpError.setException((Exception) obj);
                break;
        }
        responsePacket.setHttpErrorMessage(httpError);
        commit(what, responsePacket, COMMIT_DURATION);

    }

    /**
     * 网络是否有效
     *
     * @return 网络是否有效
     */
    public boolean isNetworkAvailable() {

        // 获取上下文
        Context context = QModel.getTopActivity();
        if (!CommonUtils.isEmpty(context)) {
            // 判断连接管理者是否为空
            if (CommonUtils.isEmpty(mConnectivityManager)) {
                return false;
            }
            // 获取网络信息对象
            NetworkInfo networkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            // 判断网络信息对象是否为空，不为空则判断获取的网络信息对象是否有效
            if (!CommonUtils.isEmpty(networkInfo)) {
                return networkInfo.isAvailable();
            }
        }
        return false;

    }

    /**
     * 关闭线程池并等待的任务完成
     *
     * @param timeout 超时时间
     * @return true表示关闭，false会尽可能的关闭
     */
    public boolean shutdownAwaitTermination(long timeout) {

        try {
            // 首先将线程池置于关闭状态，拒绝传入任务
            mExecutorService.shutdown();
            // 请求关闭、发生超时或者当前线程中断，无论哪一个首先发生之后，都将导致阻塞，直到所有任务完成执行
            if (!mExecutorService.awaitTermination(timeout, TimeUnit.SECONDS)) {
                // 如果有必要则会试图停止所有正在执行的活动任务，暂停处理正在等待的任务
                mExecutorService.shutdownNow();
                // 再次请求关闭、发生超时或者当前线程中断
                if (!mExecutorService.awaitTermination(timeout,
                        TimeUnit.SECONDS)) {
                    // 如果不成功则选择告诉用户，线程没有终止
                    this.commitErrorMessage(ERROR, HttpError.ERROR_STR,
                            HttpResponsePacket.create(),
                            "ThreadPool did not terminate");
                    return false;
                }
            }
            return true;

        } catch (InterruptedException e) {
            // 试图停止所有正在执行的活动任务，暂停处理正在等待的任务
            mExecutorService.shutdownNow();
            // 中断当前线程
            Thread.currentThread().interrupt();
            // 告知中断信息
            this.commitErrorMessage(ERROR, HttpError.ERROR_EXCEPTION,
                    HttpResponsePacket.create(), e);
        }
        return false;
    }

    /**
     * 获取客户端网络执行者
     *
     * @return 客户端网络执行者
     */
    public AbClientExecutor getClientExecutor() {
        return mClientExecutor;
    }

    /**
     * 设置客户端网络执行者
     *
     * @param executor 客户端网络执行者
     */
    public void setClientExecutor(AbClientExecutor executor) {
        this.mClientExecutor = executor;
    }

    @Override
    public void release() {

        if (!CommonUtils.isEmpty(mHttpExecutorManager)) {
            if (shutdownAwaitTermination(SHUTDOWN_AWAIT_TERMINATION_TIMEOUT)) {
                mExecutorService = null;
                if (!CommonUtils.isEmpty(mClientExecutor)) {
                    mClientExecutor.release();
                }
            }
            mConnectivityManager = null;
            mHttpExecutorManager = null;
            System.gc();

        }

    }

}
