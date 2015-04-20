package com.icrane.quickmode.utils.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.icrane.quickmode.app.QModel;

@SuppressWarnings("ALL")
public final class Version {


    private static Version version = null;
    private int versionCode = 0;

    private String versionName = "";
    private Context context;

    private Version() {

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

    public static Version getInstance() {

        if (version == null) {
            version = new Version();
        }
        
        return version;
    }

    public int obtainVersionCode() {
        return versionCode;
    }

    public String obtainVersionName() {
        return versionName;
    }
}
