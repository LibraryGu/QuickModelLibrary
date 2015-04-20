package com.icrane.quickmode.utils.common;

import android.util.Log;

@SuppressWarnings("ALL")
public final class LogUtils {

    // 全局变量，可以通过设置此变量，是否要打印log
    public static final boolean DEBUG = false;
    // 线程运行栈位置,通过此变量设置设置正确的打印log信息
    private static int stackTracePosition = 7;

    public static void i(String msg) {
        sl(LogType.INFO, msg);
    }

    public static void d(String msg) {
        sl(LogType.DEBUG, msg);
    }

    public static void e(String msg) {
        sl(LogType.ERROR, msg);
    }

    public static void v(String msg) {
        sl(LogType.VERBOSE, msg);
    }

    public static void w(String msg) {
        sl(LogType.WARN, msg);
    }

    public static void sl(LogType type, String msg) {
        if (DEBUG) {
            debug();
            return ;
        }
        String currentTag = getCurrentTag();
        switch (type) {
            case INFO:
                Log.i(currentTag, msg);
                break;
            case DEBUG:
                Log.d(currentTag, msg);
                break;
            case ERROR:
                Log.e(currentTag, msg);
                break;
            case VERBOSE:
                Log.v(currentTag, msg);
                break;
            case WARN:
                Log.w(currentTag, msg);
                break;
        }
    }

    public enum LogType {
        INFO, DEBUG, ERROR, VERBOSE, WARN
    }

    /**
     * 获取当前的标签
     *
     * @return
     */
    public static String getCurrentTag() {
        String className = getClassName();
        String currentTag = className.substring(className.lastIndexOf(".") + 1).replaceFirst("\\$\\d*", "");
        return currentTag;
    }

    /**
     * 获取调用行数的
     *
     * @return 调用行数的
     */
    public static int getLineNumber() {
        return getRealStackTraceElement().getLineNumber();
    }

    /**
     * 获取当前的类名
     *
     * @return 当前的类名
     */
    public static String getClassName() {
        return getRealStackTraceElement().getClassName();
    }

    /**
     * 获取当前的方法名
     *
     * @return 当前的方法名
     */
    public static String getMethodName() {
        return getRealStackTraceElement().getMethodName();
    }

    /**
     * 获取当前文件名
     *
     * @return 当前文件名
     */
    public static String getFileName() {
        return getRealStackTraceElement().getFileName();
    }

    /**
     * 获取真实的线程运行栈
     *
     * @return 真实的线程运行栈
     */
    private static StackTraceElement getRealStackTraceElement() {
        // debug();
        return Thread.currentThread().getStackTrace()[stackTracePosition];
    }

    /**
     * 获取线程运行栈位置
     *
     * @return 线程运行栈位置
     */
    public static int getStackTracePosition() {
        return stackTracePosition;
    }

    /**
     * 设置线程运行栈位置
     *
     * @return 线程运行栈位置
     */
    public static void setStackTracePosition(int position) {
        stackTracePosition = position;
    }

    /**
     * 测试用打印方法
     */
    public static void debug() {
        StackTraceElement[] stackTraceElements = Thread.currentThread()
                .getStackTrace();
        for (int i = 0; i < stackTraceElements.length; i++) {
            System.out.println("--------第" + i + "个--------");
            System.out.println("ClassName:"
                    + stackTraceElements[i].getClassName());
            System.out.println("MethodName:"
                    + stackTraceElements[i].getMethodName());
            System.out.println("toString:" + stackTraceElements[i].toString());
        }
    }
}
