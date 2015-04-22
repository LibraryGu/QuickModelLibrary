package com.icrane.quickmode.utils.common;

import android.graphics.PointF;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public final class AlgorithmUtils {

    /**
     * md5加密方法
     *
     * @param data 要加密的字符串
     * @return md5加密后的字符串对象
     */
    public static String encodeMd5(String data) {

        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(data.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {

            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return md5StrBuff.toString();
    }

    /**
     * base64加密
     *
     * @param str     要加密的字符串
     * @param charset 字符编码
     * @param flags   加密方式
     * @return 返回加密后的字符串
     * @throws java.io.UnsupportedEncodingException 不支持编码异常
     */
    public static String encodeBase64(String str, String charset, int flags)
            throws UnsupportedEncodingException {
        return Base64.encodeToString(str.getBytes(charset), flags);
    }

    /**
     * base64解密
     *
     * @param encodeStr 加密过的字符串
     * @param charset   字符编码
     * @param flags     解密方式
     * @return 返回加密后的字符串
     * @throws java.io.UnsupportedEncodingException 不支持编码异常
     */
    public static String decodeBase64(String encodeStr, String charset,
                                      int flags) throws UnsupportedEncodingException {
        return new String(Base64.decode(encodeStr, flags), charset);
    }

    /**
     * 两点间距离
     *
     * @param p1 第一点
     * @param p2 第二点
     * @return 两点间距离
     */
    public float measureSpacing(PointF p1, PointF p2) {
        float x = p1.x - p2.x;
        float y = p1.y - p2.y;
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 取两点的中心点
     *
     * @param p1 第一点
     * @param p2 第二点
     * @return 取两点的中心点
     */
    public PointF measureCenter(PointF p1, PointF p2) {
        PointF middle = new PointF();
        float x = p1.x + p2.x;
        float y = p1.y + p2.y;
        middle.set(x / 2, y / 2);
        return middle;
    }

    /**
     * 将点转换为角度
     *
     * @param p1 第一点
     * @param p2 第二点
     * @return 点转换的角度
     */
    public float convertsAngleOfPoint(PointF p1, PointF p2) {
        double deltaX = (p1.x - p2.x);
        double deltaY = (p1.y - p2.y);
        double radians = Math.atan2(deltaX, deltaY);
        return (float) Math.toDegrees(radians);
    }

}
