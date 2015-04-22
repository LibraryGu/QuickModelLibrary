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
public final class CommonUtils {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd hh:mm";
    public static final String CHINESE_DATE_FORMAT = "yyyy年MM月dd日 hh时mm分";

    public static final String MATCHES_HTTP_URL = "(http://|https://).*?";
    public static final String MATCHES_MAIL = "[a-zA-Z0-9_]{1,12}+@[a-zA-Z0-9]+(\\.[a-zA-Z]+){1,3}";
    public static final String MATCHES_CHINESE = "[\\u4e00-\\u9fa5]+";

    /**
     * 判断一個对象是否为空
     *
     * @param obj 指定对象
     * @return 是否为空
     */
    public static boolean isEmpty(Object obj) {
        return obj == null;
    }

    /**
     * 判断集合是否为空
     *
     * @param collection 集合类
     * @return 集合是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.size() == 0 || collection
                .isEmpty());
    }

    /**
     * 判断字符串对象是否为空
     *
     * @param str 字符串对象
     * @return 字符串对象是否为空
     */
    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    /**
     * 判断是否为url链接
     *
     * @param input 字符串对象
     * @return 是否为url链接
     */
    public static boolean isHttpUrl(String input) {
        return matching(MATCHES_HTTP_URL, input);
    }

    /**
     * 正则匹配
     *
     * @param regex 正则表达式
     * @param input 输入需要匹配的内容
     * @return true表示匹配成功, false表示失败
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
     * @return 正则匹配，并获取匹配的结果集
     */
    public static List<String> matchingToList(String regex, String input, int group) {

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
     * @param str 字符串对象
     * @return true表示存在, false表示不存在
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
     * @param str 字符串对象
     * @return true表示存在, false表示不存在
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
     * @param str 字符串对象
     * @return true表示存在, false表示不存在
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
     * @param values 列表对象
     * @return 转换为字符串
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
     * @param cls    对应类型对象
     * @param source 资源名称
     * @return 获取资源id，如果获取失败将会返回-1
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
     * @param source 资源id的字符串名称
     * @return 图片资源id
     */
    public static int getDrawableResourcesId(String source) {
        return getResourcesId(R.drawable.class, source);
    }

    /**
     * 获取字符串资源id
     *
     * @param source 资源id的字符串名称
     * @return 字符串资源id
     */
    public static int getStringsResourcesId(String source) {
        return getResourcesId(R.string.class, source);
    }

    /**
     * 获取测量资源id
     *
     * @param source 资源id的字符串名称
     * @return 测量资源id
     */
    public static int getDimensResourcesId(String source) {
        return getResourcesId(R.dimen.class, source);
    }

    /**
     * 获取布局资源id
     *
     * @param source 资源id的字符串名称
     * @return 布局资源id
     */
    public static int getLayoutResourcesId(String source) {
        return getResourcesId(R.layout.class, source);
    }

    /**
     * 获取风格资源id
     *
     * @param source 资源id的字符串名称
     * @return 风格资源id
     */
    public static int getStylesResourcesId(String source) {
        return getResourcesId(R.style.class, source);
    }

    /**
     * 获取动画资源id
     *
     * @param source 资源id的字符串名称
     * @return 动画资源id
     */
    public static int getAnimResourcesId(String source) {
        return getResourcesId(R.anim.class, source);
    }

    /**
     * 获取随机图片Id
     *
     * @param pattern     前缀
     * @param randomRange 随机数字范围
     * @return 随机图片Id
     */
    public static int getRandomDrawableId(String pattern, int randomRange) {
        int num = (int) ((Math.random() * randomRange) + 1);
        return getDrawableResourcesId(pattern + num);
    }

    /**
     * 获取一个drawable图片
     *
     * @param context 描述上下文对象
     * @param resId   资源id
     * @return Drawable图片
     */
    public static Drawable obtainDrawable(Context context, int resId) {
        if (resId <= 0) return null;
        return context.getResources().getDrawable(resId);
    }

    /**
     * 获取一个drawable图片
     *
     * @param context 描述上下文对象
     * @param bitmap  Bitmap位图
     * @return Drawable图片
     */
    public static Drawable obtainDrawable(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    /**
     * 获取一个drawable图片
     *
     * @param context 描述上下文对象
     * @param is      输入流
     * @return Drawable图片
     */
    public static Drawable obtainDrawable(Context context, InputStream is) {
        return new BitmapDrawable(context.getResources(), is);
    }

    /**
     * 获取一个drawable图片
     *
     * @param context 描述上下文对象
     * @param resId   资源id
     * @param density 密度
     * @return Drawable图片
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
     * @param formatStr    格式
     * @param milliseconds 毫秒
     * @return 字符串表示
     */
    public static String dateFormat(String formatStr, long milliseconds) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date = new Date(milliseconds);
        return format.format(date);
    }

    /**
     * 匹配中文
     *
     * @param str 字符串对象
     * @return true表示匹配成功，false表示失败
     */
    public static boolean matchesChinese(String str) {
        return str.matches(MATCHES_CHINESE);
    }

    /**
     * 匹配邮箱地址
     *
     * @param mail email地址
     * @return true表示匹配成功，反之返回false
     */
    public static boolean matchesMail(String mail) {
        return mail.matches(MATCHES_MAIL);
    }

    /**
     * 强制隐藏软键盘
     *
     * @param context 描述上下文对象
     * @param view    视图对象
     * @return true表示隐藏成功，反之返回false
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
     * @return 可变的UUID
     */
    public static UUID getVariableUUID() {
        return UUID.randomUUID();
    }

    /**
     * 获取唯一的UUID
     *
     * @param context 描述上下文对象
     * @return 唯一的UUID
     */
    public static UUID getOnlyUUID(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = manager.getDeviceId();
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        UUID uuid = new UUID(androidId.hashCode(), ((long) deviceId.hashCode() << 32));
        return uuid;
    }
}
