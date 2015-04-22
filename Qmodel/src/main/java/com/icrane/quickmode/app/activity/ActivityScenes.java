package com.icrane.quickmode.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.icrane.quickmode.app.QModel;
import com.icrane.quickmode.app.effect.SlideEffect;
import com.icrane.quickmode.utils.common.LogUtils;

/**
 * Created by gujiwen on 15/4/9.
 */
public final class ActivityScenes {

    private Intent intent = null;
    private Activity activity;

    public ActivityScenes(Intent intent) {
        this.activity = QModel.getTopActivity();
        LogUtils.d("Top Activity:" + this.activity);
        this.intent = intent;
    }

    public ActivityScenes() {
        this(new Intent());
    }

    /**
     * 切换Activity
     *
     * @param direction 设置改变效果
     * @param isFinish  是否销毁
     */
    public void sceneTo(SlideEffect.SlideDirection direction, boolean isFinish) {
        activity.startActivity(intent);
        transformsAndFinish(direction, isFinish);
    }

    /**
     * 直接finish当前的Activity
     *
     * @param direction 设置改变效果
     */
    public void sceneToFinish(SlideEffect.SlideDirection direction) {
        transformsAndFinish(direction, true);
    }

    /**
     * 切换Activity且有返回结果
     *
     * @param requestCode 请求码
     * @param direction   设置改变效果
     * @param isFinish    是否销毁
     */
    public void sceneToForResult(int requestCode, SlideEffect.SlideDirection direction, boolean isFinish) {
        activity.startActivityForResult(intent, requestCode);
        this.transformsAndFinish(direction, isFinish);
    }

    /**
     * 销毁Activity是否
     *
     * @param isFinish 是否销毁
     */
    protected void finish(boolean isFinish) {
        if (isFinish) {
            activity.finish();
        }
    }

    /**
     * 切换效果
     *
     * @param direction 设置改变效果
     */
    protected void transforms(SlideEffect.SlideDirection direction) {
        SlideEffect effect = direction.obtain();
        activity.overridePendingTransition(effect.enterAnim, effect.exitAnim);
    }

    /**
     * 切换效果且设置是否销毁
     *
     * @param direction 设置改变效果
     * @param isFinish  是否销毁
     */
    protected void transformsAndFinish(SlideEffect.SlideDirection direction, boolean isFinish) {
        finish(isFinish);
        transforms(direction);
    }

    /**
     * 获取Intent对象
     *
     * @return Intent对象
     */
    public Intent getIntent() {
        return intent;
    }

    /**
     * 设置Intent对象
     *
     * @param intent Intent对象
     */
    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    /**
     * 获取当前Activity对象
     *
     * @return activity对象
     */
    public Activity getActivity() {
        return activity;
    }

    /**
     * 设置当前使用的Activity对象
     *
     * @param context 上下文
     */
    public void setActivity(Context context) {
        this.activity = (Activity) context;
    }
}
