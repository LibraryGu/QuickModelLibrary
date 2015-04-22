package com.icrane.quickmode.app.fragment.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.icrane.quickmode.app.QModel;
import com.icrane.quickmode.app.Releasable;
import com.icrane.quickmode.app.fragment.QModelFragment;
import com.icrane.quickmode.utils.common.CommonUtils;

public final class FragmentGenerator implements Releasable {

    private static FragmentGenerator fragmentGenerator = null;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private FragmentGenerator(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.fragmentTransaction = fragmentManager.beginTransaction();
    }

    public static FragmentGenerator getInstance(FragmentManager fragmentManager) {
        if (CommonUtils.isEmpty(fragmentGenerator)) {
            fragmentGenerator = new FragmentGenerator(fragmentManager);
        }
        return fragmentGenerator;
    }

    /**
     * 生成fragment
     *
     * @param container    fragment被添加的容器
     * @param fragmentName fragment的标签名
     */
    @SuppressWarnings("unchecked")
    public Fragment fragmentGeneration(int container, String fragmentName) {
        QModelFragment fragment = (QModelFragment) fragmentManager
                .findFragmentByTag(fragmentName);
        if (fragment == null) {
            fragment = (QModelFragment) Fragment.instantiate(QModel
                    .getTopActivity(), fragmentName);
            fragmentTransaction.attach(fragment);
        } else {
            fragmentTransaction.add(container, fragment, fragmentName);
        }
        return fragment;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public FragmentTransaction getFragmentTransaction() {
        return fragmentTransaction;
    }

    public void setFragmentTransaction(FragmentTransaction fragmentTransaction) {
        this.fragmentTransaction = fragmentTransaction;
    }

    /**
     * 释放资源
     */
    @Override
    public void release() {
        if(fragmentGenerator != null) {
            fragmentGenerator = null;
        }
    }

}
