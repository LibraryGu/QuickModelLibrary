package com.icrane.quickmode.utils.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.icrane.quickmode.app.QModel;

/**
 * 这个类当被构造函数构造一个实例时，就会获取当前系统版本号和版本名，可以通过getInstance()方法构造一个Version
 * 对象，调用obtainVersionCode()和obtainVersionName()分别获取版本号和版本名。
 */
public final class Version {

    private static Version version = null;
    private int versionCode = 0;

    private String versionName = "";
    private Context context;

    protected Version() {

        this.context = QModel.getTopActivity();
        PackageManager pm = context.getPackageManager();
        String packageName = context.getPackageName();

        try {

            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            versionCode = packageInfo.versionCode;
            versionName = packageInfo.versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取实例
     *
     * @return 实例对象
     */
    public static Version getInstance() {

        if (version == null) {
            version = new Version();
        }

        return version;
    }

    /**
     * 获取int类型的版本号
     *
     * @return int类型的版本号
     */
    public int obtainVersionCode() {
        return versionCode;
    }

    /**
     * 获取版本名
     *
     * @return 版本名
     */
    public String obtainVersionName() {
        return versionName;
    }
}
