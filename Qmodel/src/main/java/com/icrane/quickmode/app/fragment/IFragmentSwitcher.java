package com.icrane.quickmode.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.icrane.quickmode.app.effect.SlideEffect;

@SuppressWarnings("ALL")
public interface IFragmentSwitcher {

    /**
     * 跳转至指定fragment界面，此方法需要注意:因为这个提交事务是在不丢失数据的情况下进行。
     * 所以需要注意旧版本和新版本的提交,以下给出相应的方法：
     * 原因是因为Honeycomb版本对Activity的生命周期做了重大的修改。
     * 在Honeycomb之前，Activity在被暂停之前是不可被杀的，这意味着onSaveInstanceState()是在onPause()之前调用的。
     * 自从Honeycomb版本，Activity只能在停止以后是可被杀的，意味着onSaveInstanceState()会在onStop()之前调用，而不是在onPause()之前调用。
     * 总结如下：
     * <div>
     * <table border="1">
     * <tr>
     * <td></td>
     * <td>Honeycomb之前</td>
     * <td>Homeycomb之后</td>
     * </tr>
     * <tr>
     * <td>Activity能在onPause()之前被干掉</td>
     * <td>NO</td>
     * <td>NO</td>
     * </tr>
     * <tr>
     * <td>Activity能在onStop()之前被干掉</td>
     * <td>YES</td>
     * <td>NO</td>
     * </tr>
     * <tr>
     * <td>onSaveInstanceState(Bundle)通常在哪个方法之前调用</td>
     * <td>onPause()</td>
     * <td>onStop()</td>
     * </tr>
     * </table>
     * </div>
     * <p/>
     * 因为Activity生命周期的改变，support库有时需要根据平台版本的不同而改变它的行为。
     * 例如，设备运行在Honeycomb或以上的系统，每次在onSaveInstanceState()后调用commit()方法，系统都会抛出异常去警告开发者发生了状态丢失。
     * 然而，每次都抛出一个异常是过于严格的在设备运行Honeycomb以前的系统，它们的onSaveInstanceState()在Activity生命周期中被更早的调并且更容易导致状态丢失。
     * Android团队不得不做出了妥协：在了在老系统上拥有更好的交互操作，在onPause()和onStop()之间，老设备只能承受状态丢失。
     * Support库在不同版本上不同的行为
     * 总结如下：
     * <div>
     * <table border="1">
     * <tr>
     * <td></td>
     * <td>Honeycomb之前</td>
     * <td>Homeycomb之后</td>
     * </tr>
     * <tr>
     * <td>commit()在onPause()之前</td>
     * <td>OK</td>
     * <td>OK</td>
     * </tr>
     * <tr>
     * <td>commit()在onPause()和onStop()之间</td>
     * <td>STATE_LOSS</td>
     * <td>OK</td>
     * </tr>
     * <tr>
     * <td>commit()在onStop()之后</td>
     * <td>EXCEPTION</td>
     * <td>EXCEPTION</td>
     * </tr>
     * </table>
     * </div>
     *
     * @param container 所在容器
     * @param cls       对应的class
     * @param bundle    要传输的数据
     * @param direction 弹出动画
     */
    public <T> void switchFragment(int container, Class<? extends Fragment> cls, Bundle bundle,
                                   SlideEffect.SlideDirection direction);
}
