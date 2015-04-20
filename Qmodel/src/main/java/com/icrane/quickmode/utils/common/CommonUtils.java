package com.icrane.quickmode.utils.common;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.icrane.quickmode.R;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint({"SimpleDateFormat", "NewApi"})
@SuppressWarnings("ALL")
public final class CommonUtils {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd hh:mm";
    public static final String CHINESE_DATE_FORMAT = "yyyy年MM月dd日 hh时mm分";

    public static final String MATCHES_HTTP_URL = "(http://|https://).*?";
    public static final String MATCHES_MAIL = "[a-zA-Z0-9_]{1,12}+@[a-zA-Z0-9]+(\\.[a-zA-Z]+){1,3}";
    public static final String MATCHES_CHINESE = "[\\u4e00-\\u9fa5]+";

    /**
     * 判斷一個對象是否為空
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        return obj == null;
    }

    /**
     * 判断集合是否为空
     *
     * @param collection 集合类
     * @return
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.size() == 0 || collection
                .isEmpty());
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    /**
     * 判断是否为url链接
     *
     * @param input
     * @return
     */
    public static boolean isHttpUrl(String input) {
        return matching(MATCHES_HTTP_URL, input);
    }

    /**
     * 正则匹配
     *
     * @param regex 正则表达式
     * @param input 输入需要匹配的内容
     * @return
     */
    public static boolean matching(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    /**
     * 正则匹配，并获取匹配的结果集
     *
     * @param regex 正则表达式
     * @param input 输入需要匹配的内容
     * @param group 获取匹配结果的组
     * @return
     */
    public static List<String> matchingToList(String regex, String input,
                                              int group) {

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        List<String> groups = new ArrayList<String>();
        while (matcher.find()) {
            groups.add(matcher.group(group));
        }
        return groups;
    }

    /**
     * 是否存在特殊字符
     *
     * @param str
     * @return
     */
    public static boolean isExistSpecialSymbols(String str) {
        for (Character c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                if (!Character.isLetter(c)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否存在数字
     *
     * @param str
     * @return
     */
    public static boolean isExistNumber(String str) {
        for (Character c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否存在字母
     *
     * @param str
     * @return
     */
    public static boolean isExistLetter(String str) {
        for (Character c : str.toCharArray()) {
            if (Character.isLetter(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 转换为字符串
     *
     * @param values
     * @return
     */
    public static String convertListToURLParamsString(List<?> values) {
        StringBuffer buff = new StringBuffer();
        for (Object object : values) {
            buff.append(object.toString() + "&");
        }
        buff.replace(buff.length() - 1, buff.length(), "");
        return buff.toString();
    }

    /**
     * 获取资源id，如果获取失败将会返回-1
     *
     * @param cls
     * @param source
     * @return
     */
    public static int getResourcesId(Class<?> cls, String source) {
        try {
            Field field = cls.getField(source);
            int id = Integer.parseInt(field.get(null).toString());
            return id;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取图片资源id
     *
     * @param source
     * @return
     */
    public static int getDrawableResourcesId(String source) {
        return getResourcesId(R.drawable.class, source);
    }

    /**
     * 获取字符串资源id
     *
     * @param source
     * @return
     */
    public static int getStringsResourcesId(String source) {
        return getResourcesId(R.string.class, source);
    }

    /**
     * 获取测量资源id
     *
     * @param source
     * @return
     */
    public static int getDimensResourcesId(String source) {
        return getResourcesId(R.dimen.class, source);
    }

    /**
     * 获取布局资源id
     *
     * @param source
     * @return
     */
    public static int getLayoutResourcesId(String source) {
        return getResourcesId(R.layout.class, source);
    }

    /**
     * 获取风格资源id
     *
     * @param source
     * @return
     */
    public static int getStylesResourcesId(String source) {
        return getResourcesId(R.style.class, source);
    }

    /**
     * 获取动画资源id
     *
     * @param source
     * @return
     */
    public static int getAnimResourcesId(String source) {
        return getResourcesId(R.anim.class, source);
    }

    /**
     * 获取随机图片Id
     *
     * @return
     */
    public static int getRandomDrawableId(String pattern, int randomRange) {
        int num = (int) ((Math.random() * randomRange) + 1);
        return getDrawableResourcesId(pattern + num);
    }

    /**
     * 获取一个drawable图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Drawable obtainDrawable(Context context, int resId) {
        if (resId <= 0) return null;
        return context.getResources().getDrawable(resId);
    }

    /**
     * 获取一个drawable图片
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static Drawable obtainDrawable(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    /**
     * 获取一个drawable图片
     *
     * @param context 上下文
     * @param is
     * @return
     */
    public static Drawable obtainDrawable(Context context, InputStream is) {
        return new BitmapDrawable(context.getResources(), is);
    }

    /**
     * 获取一个drawable图片
     *
     * @param context
     * @param resId
     * @param density
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    public static Drawable obtainDensityDrawable(Context context, int resId,
                                                 int density) {
        if (resId <= 0) return null;
        return context.getResources().getDrawableForDensity(resId, density);
    }

    /**
     * 转化时间
     *
     * @param pattern
     * @param milliseconds
     * @return
     */
    public static String dateFormat(String pattern, long milliseconds) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = new Date(milliseconds);
        return format.format(date);
    }

    /**
     * 匹配中文
     *
     * @param str
     * @return
     */
    public static boolean matchesChinese(String str) {
        return str.matches(MATCHES_CHINESE);
    }

    /**
     * 匹配邮箱地址
     *
     * @param mail
     * @return
     */
    public static boolean matchesMail(String mail) {
        return mail.matches(MATCHES_MAIL);
    }

    /**
     * 强制隐藏软键盘
     *
     * @param context
     * @param view
     * @return
     */
    public static boolean hideSoftKeyboard(Context context, View view) {
        InputMethodManager mInputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        return mInputMethodManager.hideSoftInputFromWindow(
                view.getWindowToken(), 0);
    }

    /**
     * 获取可变的UUID
     *
     * @return
     */
    public static UUID getVariableUUID() {
        return UUID.randomUUID();
    }

    /**
     * 获取唯一的UUID
     *
     * @param context
     * @return
     */
    public static UUID getOnlyUUID(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = manager.getDeviceId();
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        UUID uuid = new UUID(androidId.hashCode(), ((long) deviceId.hashCode() << 32));
        return uuid;
    }
}
