package com.icrane.quickmode.app.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;

import com.icrane.quickmode.app.Releasable;
import com.icrane.quickmode.utils.common.CommonUtils;
import com.icrane.quickmode.utils.common.LogUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressWarnings("ALL")
public final class ActivityStack implements Releasable {

    private int size = 0;
    private boolean contains = false;
    private boolean isEmpty = true;

    private Activity currentActivity = null;
    private static ActivityStack activityStack = null;

    private List<Activity> activities = null;
    private ReentrantLock reentrantLock = null;
    private StackLock mStackLock = null;


    private ActivityStack() {
        activities = Collections.synchronizedList(new LinkedList<Activity>());
        reentrantLock = new ReentrantLock();
        mStackLock = new StackLock();
    }

    /**
     * 获取ActivityStack实例
     *
     * @return 获取ActivityStack实例
     */
    public static ActivityStack getInstance() {
        if (CommonUtils.isEmpty(activityStack))
            activityStack = new ActivityStack();
        return activityStack;
    }

    /**
     * 将Activity压入栈中
     *
     * @param ac 将Activity压入栈中
     */
    public void push(final Activity ac) {
        LogUtils.d("pushActivity:" + ac.getClass());
        mStackLock.callback(reentrantLock, new StackLock.LockCallback() {
            @Override
            public void lockOperation() {
                activities.add(ac);
                currentActivity = ac;
            }
        });
    }

    /**
     * 弹出activity
     */
    public void pop(final Activity ac) {
        mStackLock.callback(reentrantLock, new StackLock.LockCallback() {
            @Override
            public void lockOperation() {
                Activity activity = getStackTop();
                LogUtils.d("popActivity:" + activity.getClass());
                activities.remove(ac);
                activity.finish();
            }
        });
    }

    /**
     * 移除所有Activity
     */
    public boolean popAll() {
        mStackLock.callback(reentrantLock, new StackLock.LockCallback() {
            @Override
            public void lockOperation() {
                for (Activity ac : activities) {
                    pop(ac);
                }
                activities.clear();
            }
        });
        return this.isEmpty();
    }

    /**
     * 栈中是否存在Activity
     *
     * @return 栈中是否存在Activity
     */
    public boolean isEmpty() {
        mStackLock.callback(reentrantLock, new StackLock.LockCallback() {
            @Override
            public void lockOperation() {
                isEmpty = activities.isEmpty();
            }
        });
        return isEmpty;
    }

    /**
     * 判断传入的Activity后面是否有对象
     *
     * @param ac Activity
     * @return 判断传入的Activity后面是否有对象，如果为true表示有，false则没有；
     */
    public boolean contains(final Activity ac) {
        mStackLock.callback(reentrantLock, new StackLock.LockCallback() {
            @Override
            public void lockOperation() {
                contains = activities.contains(ac);
            }
        });
        return contains;
    }

    /**
     * 当前栈Activity个数
     *
     * @return 当前栈Activity个数
     */
    public int size() {
        mStackLock.callback(reentrantLock, new StackLock.LockCallback() {
            @Override
            public void lockOperation() {
                size = activities.size();
            }
        });
        return size;
    }

    /**
     * 获取当前栈中顶部Activity
     *
     * @return 获取当前栈中顶部Activity
     */
    public Activity getStackTop() {
        return currentActivity;
    }

    /**
     * 栈底是否有元素
     *
     * @return 栈底是否有元素
     */
    public boolean hasStackBottom() {
        if (!isEmpty) {
            int bottom = size - 1;
            if (bottom > 0) {
                Activity ac = activities.get(bottom);
                if (ac != null) return true;
            }
        }
        return false;
    }

    /**
     * 获取栈中所有Activity的列表
     *
     * @return 获取栈中所有Activity的列表
     */
    public List<Activity> getActivityList() {
        return activities;
    }

    @Override
    public void release() {
        if (!CommonUtils.isEmpty(activityStack)) {
            if (!this.isEmpty()) {
                popAll();
            }
            activityStack = null;
        }
    }

    /**
     * StackLock
     */
    public static class StackLock {

        public void callback(Lock lock, LockCallback callback) {
            try {
                lock.lock();
                callback.lockOperation();
            } finally {
                lock.unlock();
            }
        }

        public interface LockCallback {
            public void lockOperation();
        }
    }

}
