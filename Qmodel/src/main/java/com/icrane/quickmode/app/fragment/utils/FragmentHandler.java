package com.icrane.quickmode.app.fragment.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.icrane.quickmode.app.Releasable;
import com.icrane.quickmode.app.effect.SlideEffect;
import com.icrane.quickmode.app.fragment.IFragmentSwitcher;
import com.icrane.quickmode.utils.common.CommonUtils;

public final class FragmentHandler implements IFragmentSwitcher, Releasable {

    private String currentFragmentTag;
    private Fragment currentFragment;
    private FragmentBundles fragmentBundles;
    private FragmentGenerator fragmentGenerator;

    private static FragmentHandler fragmentHandler = null;

    private FragmentHandler(FragmentGenerator fragmentGenerator) {
        this.fragmentBundles = FragmentBundles.getDefault();
        this.fragmentGenerator = fragmentGenerator;
    }

    /**
     * 获取一个fragment处理对象
     *
     * @param fragmentGenerator fragment生成器
     * @return fragment处理对象
     */
    public static FragmentHandler getInstance(FragmentGenerator fragmentGenerator) {
        if (CommonUtils.isEmpty(fragmentHandler)) {
            fragmentHandler = new FragmentHandler(fragmentGenerator);
        }
        return fragmentHandler;
    }

    /**
     * 获取当前fragment标签
     *
     * @return fragment的标签
     */
    public String getCurrentFragmentTag() {
        return currentFragmentTag;
    }

    /**
     * 设置当前fragment的标签
     *
     * @param tag fragment的标签
     */
    public void setCurrentFragmentTag(String tag) {
        this.currentFragmentTag = tag;
    }

    /**
     * 获取当前fragment对象
     *
     * @return 当前fragment对象
     */
    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    /**
     * 设置当前fragment对象
     *
     * @param currentFragment 当前fragment对象
     */
    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    /**
     * 获取fragment的Bundle数据缓存
     *
     * @return FragmentBundles对象
     */
    public FragmentBundles getFragmentBundles() {
        return fragmentBundles;
    }

    /**
     * 保存Fragment返回数据
     *
     * @param key    对应的键
     * @param bundle 存储的Bundle对象
     */
    protected void saveBundleData(String key, Bundle bundle) {
        if (bundle != null) {
            fragmentBundles.put(key, bundle);
        }
    }

    /**
     * 在显示fragment时添加滑动动画
     *
     * @param ft        FragmentTransaction对象
     * @param direction 动画方向
     */
    protected void fragmentUseSlideEffect(FragmentTransaction ft,
                                          SlideEffect.SlideDirection direction) {
        if (direction != null) {
            SlideEffect bombAnim = direction.obtain();
            ft.setCustomAnimations(bombAnim.enterAnim, bombAnim.exitAnim);
        }
    }

    /**
     * 是否脱离管理当前fragment
     *
     * @param ft FragmentTransaction对象
     */
    protected void detachCurrentFragment(FragmentTransaction ft) {
        // 获取fragment的标签，判断是否为空，如果不为空则detach这个fragment
        if (!CommonUtils.isEmpty(this.getCurrentFragmentTag())) {
            // 如果当前fragment不为空，先将当前的fragment脱离出去
            Fragment fragment = fragmentGenerator.getFragmentManager()
                    .findFragmentByTag(this.getCurrentFragmentTag());
            ft.detach(fragment);
        }
    }

    @Override
    public <T> void switchFragment(int container, Class<? extends Fragment> cls, Bundle bundle, SlideEffect.SlideDirection direction) {
        FragmentTransaction fragmentTransaction = fragmentGenerator
                .getFragmentTransaction();
        this.detachCurrentFragment(fragmentTransaction);
        // 获取fragment的类名 ，作为要切换的fragment的标签名
        String fragmentTag = cls.getName();
        Fragment fragment = fragmentGenerator.fragmentGeneration(container, fragmentTag);
        // 保存数据
        this.saveBundleData(fragmentTag, bundle);
        // 使用弹出动画
        this.fragmentUseSlideEffect(fragmentTransaction, direction);
        // 提交数据
        fragmentTransaction.commit();
        // 保存当前fragment标签
        this.setCurrentFragmentTag(fragmentTag);
        // 保存当前fragment对象
        this.setCurrentFragment(fragment);
    }

    /**
     * 释放资源
     */
    @Override
    public void release() {
        if (fragmentHandler != null) {
            currentFragmentTag = null;
            currentFragment = null;
            fragmentBundles = null;
            fragmentGenerator = null;
            fragmentHandler = null;
            System.gc();
        }
    }

}
