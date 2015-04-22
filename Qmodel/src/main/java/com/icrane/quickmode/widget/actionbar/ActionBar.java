package com.icrane.quickmode.widget.actionbar;

import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icrane.quickmode.app.effect.SlideEffect;

/**
 * Created by gujiwen on 15/4/17.
 */
public interface ActionBar {

    /**
     * 设置是否打开返回按钮
     *
     * @param isEnable true表示打开,false表示关闭
     */
    public void setDisplayHomeAsUpEnabled(boolean isEnable);

    /**
     * 设置返回的界面
     *
     * @param parent    返回的界面
     * @param direction 动画效果
     */
    public void setHomeAsUpNavigationParent(Class<?> parent, SlideEffect.SlideDirection direction);

    /**
     * 设置显示ActionBar
     *
     * @param isShow true表示显示，false表示不显示
     */
    public void setShowActionBar(boolean isShow);

    /**
     * 设置返回的界面,使用默认动画效果
     *
     * @param parent 返回的界面
     */
    public void setHomeAsUpNavigationParent(Class<?> parent);

    /**
     * 设置返回键背景
     *
     * @param resId 背景资源
     */
    public void setDisplayHomeAsUpBackground(int resId);

    /**
     * 设置返回键背景
     *
     * @param drawable 背景图片
     */
    public void setDisplayHomeAsUpBackground(Drawable drawable);

    /**
     * 设置是否打开标题
     *
     * @param isEnable true表示打开，false表示关闭
     */
    public void setDisplayHomeAsTitleEnable(boolean isEnable);

    /**
     * 设置标题
     *
     * @param resId 资源id
     */
    public void setDisplayHomeAsTitle(int resId);

    /**
     * 设置标题
     *
     * @param text 字符串
     */
    public void setDisplayHomeAsTitle(String text);

    /**
     * 设置标题背景
     *
     * @param resId 资源id
     */
    public void setDisplayHomeAsTitleBackground(int resId);

    /**
     * 设置标题背景
     *
     * @param drawable 背景图片
     */
    public void setDisplayHomeAsTitleBackground(Drawable drawable);

    /**
     * 设置标题小图标
     *
     * @param resId    资源id
     * @param position 放置的位置
     */
    public void setDisplayHomeAsTitleIcon(int resId, Menu.Position position);

    /**
     * 设置标题的小图标
     *
     * @param drawable 小图标图片
     * @param position 放置的位置
     */
    public void setDisplayHomeAsTitleIcon(Drawable drawable, Menu.Position position);

    /**
     * 设置ActionBar的背景
     *
     * @param resId 资源id
     */
    public void setActionBarBackground(int resId);

    /**
     * 设置ActionBar的背景
     *
     * @param drawable 背景图片
     */
    public void setActionBarBackground(Drawable drawable);

    /**
     * 设置ActionBar的背景颜色
     *
     * @param color 背景颜色
     */
    public void setActionBarBackgroundColor(int color);

    /**
     * 添加菜单
     *
     * @param menu   ActionBarMenu菜单对象
     * @param params 布局大小属性
     * @param module 放置的模块内
     */
    public void addMenu(ActionBarMenu menu, ViewGroup.LayoutParams params, ActionBarLayout.Module module);

    /**
     * 添加菜单
     *
     * @param menu   ActionBarMenu菜单对象
     * @param module 放置的模块内
     */
    public void addMenu(ActionBarMenu menu, ActionBarLayout.Module module);

    /**
     * 添加自定义View
     *
     * @param view   视图对象
     * @param params 布局大小属性
     * @param module 放置的模块
     */
    public void addCustomView(View view, ViewGroup.LayoutParams params, ActionBarLayout.Module module);

    /**
     * 添加自定义View
     *
     * @param view   视图对象
     * @param module 放置的模块
     */
    public void addCustomView(View view, ActionBarLayout.Module module);

    /**
     * 寻找视图
     *
     * @param id     对应id
     * @param module 对应模块
     * @return 返回视图对象
     */
    public View findViewById(int id, ActionBarLayout.Module module);

    /**
     * 获取LayoutInflater对象
     *
     * @return LayoutInflater对象
     */
    public LayoutInflater getLayoutInflater();

    /**
     * 获取根视图
     *
     * @return 根视图
     */
    public View getActionBarRoot();

    /**
     * 获取返回键视图
     *
     * @return 返回键视图
     */
    public ActionBarMenu getBackMenu();

    /**
     * 设置返回键视图
     *
     * @param backMenu 返回键视图
     */
    public void setBackMenu(ActionBarMenu backMenu);

    /**
     * 获取标题视图
     *
     * @return 标题视图
     */
    public ActionBarMenu getTitleMenu();

    /**
     * 设置标题视图
     *
     * @param titleMenu 标题视图
     */
    public void setTitleMenu(ActionBarMenu titleMenu);

    /**
     * 设置内容视图
     *
     * @param layoutID 资源id
     */
    public void setActionBarContentView(int layoutID);

    /**
     * 设置内容视图
     *
     * @param layoutView 资源id
     */
    public void setActionBarContentView(View layoutView);

    /**
     * 获取ActionBar的内容视图
     *
     * @return ActionBar的内容视图
     */
    public View getActionBarContentView();

    /**
     * 获取ActionBar对象
     *
     * @return ActionBar对象
     */
    public ViewGroup getActionBar();

    /**
     * 获取左边区域视图
     *
     * @return 左边区域视图
     */
    public ViewGroup getLeftArea();

    /**
     * 获取居中区域视图
     *
     * @return 居中区域视图
     */
    public ViewGroup getCenterArea();

    /**
     * 获取右边区域视图
     *
     * @return 右边区域视图
     */
    public ViewGroup getRightArea();

    /**
     * 是否打开了返回键显示
     *
     * @return true表示打开，false表示关闭
     */
    public boolean isDisplayHomeAsUpEnabled();

    /**
     * 是否打开了标题显示
     *
     * @return true表示打开，false表示关闭
     */
    public boolean isDisplayHomeAsTitleEnable();

    /**
     * 获取返回键按下后返回的界面
     *
     * @return 返回键按下后返回的界面的Class
     */
    public Class<?> getHomeAsBackParent();

    /**
     * 设置返回键按下后返回的界面
     *
     * @param homeAsBackParent 返回的界面
     */
    public void setHomeAsBackParent(Class<?> homeAsBackParent);

    /**
     * 获取返回键的动画效果
     *
     * @return 返回键的动画效果
     */
    public SlideEffect.SlideDirection getHomeAsBackParentSlidDirection();

    /**
     * 设置返回键的动画效果
     *
     * @param direction 返回键的动画效果
     */
    public void setHomeAsBackParentSlidDirection(SlideEffect.SlideDirection direction);

    /**
     * 获取居右边区域的子视图数组
     *
     * @return 获取居右边区域的子视图数组
     */
    public SparseArray<View> getLeftAreaArray();

    /**
     * 获取居中边区域的子视图数组
     *
     * @return 获取居中区域的子视图数组
     */
    public SparseArray<View> getCenterAreaArray();

    /**
     * 获取左边区域的子视图数组
     *
     * @return 获取左边区域的子视图数组
     */
    public SparseArray<View> getRightAreaArray();

    /**
     * 获取当前ActionBar显示状态
     *
     * @return true表示已经显示, false表示当前没有显示
     */
    public boolean isShowing();
}
