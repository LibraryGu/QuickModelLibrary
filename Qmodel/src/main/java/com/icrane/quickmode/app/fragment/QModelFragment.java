package com.icrane.quickmode.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icrane.quickmode.app.QModelHelper;
import com.icrane.quickmode.app.fragment.utils.FragmentBundles;
import com.icrane.quickmode.app.fragment.utils.FragmentHandler;

@SuppressWarnings("ALL")
public abstract class QModelFragment extends Fragment {

    static final int NO_ID = -1;

    private int mID = NO_ID;

    public QModelFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mID = getFragmentLayoutResource();
        if (this.mID == NO_ID) throw new IllegalArgumentException("id is not available!");
        View resourceView = inflateContentView(inflater, container);
        this.onCreateFragmentView(resourceView, savedInstanceState);
        return resourceView;
    }

    /**
     * 填充布局文件
     *
     * @param inflater  布局填充器
     * @param container 父容器
     * @return 填充后生成的View
     */
    protected View inflateContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(mID, container, false);
    }

    /**
     * 获取Fragment的xml布局资源
     *
     * @return xml布局资源
     */
    protected abstract int getFragmentLayoutResource();

    /**
     * 在Fragment被创建时调用
     *
     * @param resourceView       根据xml布局资源生成的View对象
     * @param savedInstanceState 保存状态
     */
    public abstract void onCreateFragmentView(View resourceView, Bundle savedInstanceState);

    /**
     * 获取FragmentHandler对象
     *
     * @return 返回FragmentHandler对象
     */
    public FragmentHandler getFragmentHandler() {
        return QModelHelper.getInstance().getFragmentHandler();
    }

    /**
     * 获取FragmentBundles对象
     *
     * @return 返回FragmentBundles对象
     */
    public FragmentBundles getFragmentBundles() {
        return this.getFragmentHandler().getFragmentBundles();
    }

    /**
     * 获取传送过来的Bundle对象
     *
     * @return 返回Bundle对象
     */
    public Bundle getArgumentBundle() {
        return getFragmentBundles().get(this.getClass().getName());
    }

}
