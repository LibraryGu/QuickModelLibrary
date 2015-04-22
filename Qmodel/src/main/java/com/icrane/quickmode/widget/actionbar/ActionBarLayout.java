package com.icrane.quickmode.widget.actionbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.icrane.quickmode.R;
import com.icrane.quickmode.app.QModelHelper;
import com.icrane.quickmode.app.activity.ActivityScenes;
import com.icrane.quickmode.app.effect.SlideEffect;
import com.icrane.quickmode.utils.common.CommonUtils;
import com.icrane.quickmode.widget.ViewLayoutParams;

/**
 * Created by gujiwen on 15/4/16.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ActionBarLayout extends ViewGroup implements ActionBar, View.OnClickListener {

    private static final int ACTIONBAR_DEFAULT_HEIGHT = 48;
    private static final String ACTIONBAR_MENU_BACK_TAG = "back_menu";
    private static final String ACTIONBAR_MENU_TITLE_TAG = "title_menu";

    private SparseArray<View> mLeftAreaArray = new SparseArray<View>();
    private SparseArray<View> mCenterAreaArray = new SparseArray<View>();
    private SparseArray<View> mRightAreaArray = new SparseArray<View>();

    private int layoutID;
    private View mActionBarContentView;

    private LayoutInflater mInflater;
    private View mActionBarRoot;

    private ActionBarMenu backMenu;
    private ActionBarMenu titleMenu;

    private FrameLayout mActionBarContainer;
    private LinearLayout mActionBar;

    private LinearLayout mLeftArea;
    private LinearLayout mCenterArea;
    private LinearLayout mRightArea;

    private boolean isShowing = true;
    private boolean isDisplayHomeAsUpEnabled = true;
    private boolean isDisplayHomeAsTitleEnable = true;

    private Class<?> mHomeAsBackParent = null;
    private SlideEffect.SlideDirection mHomeAsBackParentSlidDirection = SlideEffect.SlideDirection.LEFT_TO_RIGHT;

    public enum Module {
        LEFT,
        CENTER,
        RIGHT
    }

    public ActionBarLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public ActionBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ActionBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int childWidth = 0;
        int childHeight = 0;

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childWidth = childView.getMeasuredWidth();
            childHeight += childView.getMeasuredHeight();
        }

        int measureWidth = (widthMode == MeasureSpec.EXACTLY) ? sizeWidth : childWidth;
        int measureHeight = (heightMode == MeasureSpec.EXACTLY) ? sizeHeight : childHeight;

        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childCount = getChildCount();
        int mHeight = 0;

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int measureChildHeight = childView.getMeasuredHeight();
            childView.layout(l, mHeight, r, mHeight + measureChildHeight);
            mHeight += measureChildHeight;
        }

    }

    /**
     * 初始化视图
     *
     * @param context
     * @param attrs
     */
    private void initView(Context context, AttributeSet attrs) {

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mActionBarRoot = mInflater.inflate(R.layout.actionbar_layout, this, false);
        mActionBar = (LinearLayout) mActionBarRoot.findViewById(R.id.actionBar);
        mActionBarContainer = (FrameLayout) mActionBarRoot.findViewById(R.id.actionBarContainer);

        this.addView(mActionBarRoot);
        this.generateArea(mActionBarRoot);
        this.generateDefaultMenu();

    }

    /**
     * 生成默认按钮
     */
    private void generateDefaultMenu() {

        backMenu = new ActionBarMenu(getContext());
        titleMenu = new ActionBarMenu(getContext());

        backMenu.setBackgroundResource(R.drawable.selecor_action_back);
        backMenu.setTag(ACTIONBAR_MENU_BACK_TAG);
        backMenu.setOnClickListener(this);
        backMenu.setGravity(Gravity.CENTER);

        titleMenu.setTag(ACTIONBAR_MENU_TITLE_TAG);
        titleMenu.menuText(R.string.app_name, 20, Color.WHITE, Typeface.DEFAULT_BOLD);
        titleMenu.menuIcon(R.mipmap.ic_launcher, Menu.Position.DRAWABLE_LEFT);
        titleMenu.setGravity(Gravity.CENTER);

        this.addMenu(backMenu, Module.LEFT);
        this.addMenu(titleMenu, Module.CENTER);
    }

    /**
     * 生成可添加区域
     *
     * @param rootView 根视图
     */
    protected void generateArea(View rootView) {
        this.mLeftArea = (LinearLayout) rootView.findViewById(R.id.leftArea);
        this.mCenterArea = (LinearLayout) rootView.findViewById(R.id.centerArea);
        this.mRightArea = (LinearLayout) rootView.findViewById(R.id.rightArea);
    }

    /**
     * 添加进入布局
     *
     * @param viewGroup   对应ViewGroup
     * @param sparseArray 存储容器
     * @param view        需要存储的视图
     */
    protected void addChildForArea(ViewGroup viewGroup, SparseArray<View> sparseArray, View view) {
        int key = view.getId();
        if (key > 0) {
            sparseArray.put(key, view);
            return;
        }
        key = viewGroup.getChildCount();
        sparseArray.put(key, view);
    }

    @Override
    public void setDisplayHomeAsUpEnabled(boolean isEnable) {
        this.isDisplayHomeAsUpEnabled = isEnable;
        if (this.isDisplayHomeAsUpEnabled) {
            this.backMenu.setVisibility(View.VISIBLE);
        } else {
            this.backMenu.setVisibility(View.GONE);
        }
    }

    @Override
    public void setDisplayHomeAsTitleEnable(boolean isEnable) {
        this.isDisplayHomeAsTitleEnable = isEnable;
        if (this.isDisplayHomeAsTitleEnable) {
            this.titleMenu.setVisibility(View.VISIBLE);
        } else {
            this.titleMenu.setVisibility(View.GONE);
        }
    }

    @Override
    public void setDisplayHomeAsTitle(int resId) {
        this.setDisplayHomeAsTitle(getResources().getString(resId));
    }

    @Override
    public void setDisplayHomeAsTitle(String text) {
        this.titleMenu.setText(text);
    }

    @Override
    public void setDisplayHomeAsTitleBackground(int resId) {
        this.setDisplayHomeAsTitleBackground(CommonUtils.obtainDrawable(getContext(), resId));
    }

    @Override
    public void setDisplayHomeAsTitleBackground(Drawable drawable) {
        this.titleMenu.setBackground(drawable);
    }

    @Override
    public void setDisplayHomeAsTitleIcon(int resId, Menu.Position position) {
        this.setDisplayHomeAsTitleIcon(CommonUtils.obtainDrawable(getContext(), resId), position);
    }

    @Override
    public void setDisplayHomeAsTitleIcon(Drawable drawable, Menu.Position position) {
        this.titleMenu.menuIcon(drawable, position);
    }

    @Override
    public void setHomeAsUpNavigationParent(Class<?> parent, SlideEffect.SlideDirection direction) {
        this.mHomeAsBackParent = parent;
        this.mHomeAsBackParentSlidDirection = direction;
    }

    @Override
    public void setShowActionBar(boolean isShow) {
        this.isShowing = isShow;
        if (this.isShowing) {
            mActionBar.setVisibility(View.VISIBLE);
        } else {
            mActionBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void setHomeAsUpNavigationParent(Class<?> parent) {
        setHomeAsUpNavigationParent(parent, this.mHomeAsBackParentSlidDirection);
    }

    @Override
    public void setDisplayHomeAsUpBackground(int resId) {
        this.backMenu.setBackgroundResource(resId);
    }

    @Override
    public void setDisplayHomeAsUpBackground(Drawable drawable) {
        this.backMenu.setBackground(drawable);
    }

    @Override
    public void setActionBarBackground(int resId) {
        mActionBar.setBackgroundResource(resId);
    }

    @Override
    public void setActionBarBackground(Drawable drawable) {
        mActionBar.setBackground(drawable);
    }

    @Override
    public void setActionBarBackgroundColor(int color) {
        mActionBar.setBackgroundColor(color);
    }

    @Override
    public void addMenu(ActionBarMenu menu, LayoutParams params, Module module) {
        this.addCustomView(menu, params, module);
    }

    @Override
    public void addMenu(ActionBarMenu menu, Module module) {
        this.addMenu(menu, ViewLayoutParams.FULL_WRAP.obtain(LinearLayout.LayoutParams.class), module);
    }

    @Override
    public void addCustomView(View view, LayoutParams params, Module module) {
        if (view != null) {
            switch (module) {
                case LEFT:
                    mLeftArea.addView(view, params);
                    addChildForArea(mLeftArea, mLeftAreaArray, view);
                    break;
                case CENTER:
                    mCenterArea.addView(view, params);
                    addChildForArea(mCenterArea, mCenterAreaArray, view);
                    break;
                case RIGHT:
                    mRightArea.addView(view, params);
                    addChildForArea(mRightArea, mRightAreaArray, view);
                    break;
            }
        }
    }

    @Override
    public void addCustomView(View view, Module module) {
        this.addCustomView(view, ViewLayoutParams.FULL_WRAP.obtain(LinearLayout.LayoutParams.class), module);
    }

    @Override
    public View findViewById(int id, Module module) {
        switch (module) {
            case LEFT:
                return mLeftAreaArray.get(id);
            case CENTER:
                return mCenterAreaArray.get(id);
            case RIGHT:
                return mRightAreaArray.get(id);
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals(ACTIONBAR_MENU_BACK_TAG)) {
            if (mHomeAsBackParent != null) {
                ActivityScenes scenes = QModelHelper.getInstance().getActivityScenes(mHomeAsBackParent);
                scenes.getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                scenes.sceneTo(this.mHomeAsBackParentSlidDirection, true);
            }
        }
    }

    @Override
    public LayoutInflater getLayoutInflater() {
        return mInflater;
    }

    @Override
    public View getActionBarRoot() {
        return mActionBarRoot;
    }

    @Override
    public ActionBarMenu getBackMenu() {
        return backMenu;
    }

    @Override
    public void setBackMenu(ActionBarMenu backMenu) {
        this.backMenu = backMenu;
    }

    @Override
    public ActionBarMenu getTitleMenu() {
        return titleMenu;
    }

    @Override
    public void setTitleMenu(ActionBarMenu titleMenu) {
        this.titleMenu = titleMenu;
    }

    @Override
    public void setActionBarContentView(int layoutID) {
        this.layoutID = layoutID;
        this.mActionBarContentView = this.getLayoutInflater().inflate(this.layoutID, mActionBarContainer, false);
        this.setActionBarContentView(this.mActionBarContentView);
    }

    @Override
    public void setActionBarContentView(View layoutView) {
        this.mActionBarContentView = layoutView;
        this.mActionBarContainer.addView(this.mActionBarContentView);
    }

    @Override
    public View getActionBarContentView() {
        return mActionBarContentView;
    }

    @Override
    public ViewGroup getActionBar() {
        return mActionBar;
    }

    @Override
    public LinearLayout getLeftArea() {
        return mLeftArea;
    }

    @Override
    public LinearLayout getCenterArea() {
        return mCenterArea;
    }

    @Override
    public LinearLayout getRightArea() {
        return mRightArea;
    }

    @Override
    public boolean isDisplayHomeAsUpEnabled() {
        return isDisplayHomeAsUpEnabled;
    }

    @Override
    public boolean isDisplayHomeAsTitleEnable() {
        return isDisplayHomeAsTitleEnable;
    }

    @Override
    public Class<?> getHomeAsBackParent() {
        return mHomeAsBackParent;
    }

    @Override
    public void setHomeAsBackParent(Class<?> homeAsBackParent) {
        this.mHomeAsBackParent = homeAsBackParent;
    }

    @Override
    public SlideEffect.SlideDirection getHomeAsBackParentSlidDirection() {
        return mHomeAsBackParentSlidDirection;
    }

    @Override
    public void setHomeAsBackParentSlidDirection(SlideEffect.SlideDirection direction) {
        this.mHomeAsBackParentSlidDirection = direction;
    }

    @Override
    public SparseArray<View> getLeftAreaArray() {
        return mLeftAreaArray;
    }

    @Override
    public SparseArray<View> getCenterAreaArray() {
        return mCenterAreaArray;
    }

    @Override
    public SparseArray<View> getRightAreaArray() {
        return mRightAreaArray;
    }

    @Override
    public boolean isShowing() {
        return isShowing;
    }

}
