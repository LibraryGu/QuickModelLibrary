package com.icrane.quickmode.app.activity.impl;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.icrane.quickmode.R;
import com.icrane.quickmode.app.QModelHelper;
import com.icrane.quickmode.app.effect.SlideEffect;

/**
 * Created by gujiwen on 15/4/20.
 */
@SuppressWarnings("ALL")
public class QModelSwitchFragmentActivity extends QModelGesturesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model_fragment_content_layout);
    }


    /**
     * 切换Fragment界面
     *
     * @param cls       Fragment类
     * @param bundle    需要传入的额外数据
     * @param direction 动画效果
     */
    protected void switchFragment(Class<? extends Fragment> cls, Bundle bundle,
                                  SlideEffect.SlideDirection direction) {
        QModelHelper.getInstance().getFragmentHandler().switchFragment(R.id.fragment_content, cls,
                bundle, direction);
    }
}
