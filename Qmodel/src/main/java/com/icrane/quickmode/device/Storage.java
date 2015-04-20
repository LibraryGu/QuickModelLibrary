package com.icrane.quickmode.device;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.icrane.quickmode.app.Releasable;
import com.icrane.quickmode.utils.Charset;
import com.icrane.quickmode.utils.common.CommonUtils;
import com.icrane.quickmode.utils.common.SDKSupport;
import com.icrane.quickmode.utils.common.Streams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/*
 * 文件操作类
 * 需要权限:
 * 	<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/> or
 *	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/> or
 *	<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/> or
 *	<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/> or
 *	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/> or
 *  <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
 */
@SuppressWarnings("ALL")
public final class Storage implements Releasable {

    // 写入失败
    public static final int WRITE_FAILED = -1;
    // 写入成功
    public static final int WRITE_SUCCESS = 0;

    public static final int DEFAULT_CAPACITY = 1024;

    private static Storage mDeviceStorage = null;

    private String path;
    private StatFs mStatFs;

    private Storage() {
        this(DirectoryType.SDCARD_DIRECTORY.getDirectory());
    }

    private Storage(String path) {
        this.path = path;
    }

    /**
     * 获取DeviceStorageManager实例对象,获取的时唯一的对象；
     *
     * @return
     */
    public static Storage getDefaultStorage() {
        if (CommonUtils.isEmpty(mDeviceStorage)) {
            mDeviceStorage = new Storage();
        }
        return mDeviceStorage;
    }

    /**
     * 获取DeviceStorageManager的一个新实例
     *
     * @param path 传入存储路径
     * @return
     */
    public static Storage getStorage(String path) {
        return new Storage(path);
    }

    /**
     * 读取文件内容
     *
     * @param filePath    文件路径
     * @param charsetName 内容编码
     * @return 返回文件内容
     * @throws java.io.FileNotFoundException
     * @throws java.io.UnsupportedEncodingException
     */
    public String readToFile(String filePath, Charset charsetName)
            throws FileNotFoundException, UnsupportedEncodingException {
        return Streams.read(Streams.obtainBufferedReader(Streams.obtainFileInputStream(filePath), charsetName));
    }

    /**
     * 读取文件
     *
     * @param is 输入流
     * @return 返回文件内容
     * @throws java.io.UnsupportedEncodingException
     */
    public String readToFile(InputStream is)
            throws UnsupportedEncodingException {
        return Streams.read(Streams.obtainBufferedReader(is, Charset.UTF_8));
    }

    /**
     * 读取asset文件夹中的文件
     *
     * @param context    上下文
     * @param fileName   文件名
     * @param accessMode 允许读取的模式
     * @return
     * @throws java.io.IOException
     */
    public InputStream readToAsset(Context context, String fileName,
                                   int accessMode) throws IOException {
        AssetManager assetManager = context.getAssets();
        return assetManager.open(fileName, accessMode);
    }

    /**
     * 读取Asset文件夹中的图片
     *
     * @param context  上下文
     * @param fileName 文件名称
     * @return 返回一个Bitmap位图
     */
    public Bitmap readToAssetBitmap(Context context, String fileName) {
        Bitmap bitmap = null;
        try {
            InputStream is = readToAsset(context, fileName,
                    AssetManager.ACCESS_RANDOM);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 读取Asset文件夹
     *
     * @param context     上下文
     * @param fileName    文件名称
     * @param accessMode  允许读取方式
     * @param charsetName 字符编码
     * @return 返回读取的内容
     * @throws java.io.UnsupportedEncodingException
     * @throws java.io.IOException
     */
    public String readToAssetFile(Context context, String fileName,
                                  int accessMode, Charset charsetName)
            throws UnsupportedEncodingException, IOException {
        return Streams.read(Streams.obtainBufferedReader(readToAsset(context, fileName, accessMode), charsetName));
    }

    /**
     * 读取Asset文件夹中的图片
     *
     * @param context  上下文
     * @param fileName 文件名称
     * @return 返回一个Drawable可变图片
     */
    public Drawable readToAssetDrawable(Context context, String fileName) {
        return new BitmapDrawable(context.getResources(), readToAssetBitmap(context, fileName));
    }

    /**
     * 写入文件
     *
     * @param filePath 写入文件的路径
     * @param content  写入文件的内容
     * @throws java.io.IOException
     */
    public boolean writeToFile(String filePath, String content)
            throws IOException {
        return Streams.writer(Streams.obtainBufferedWriter(Streams
                .obtainFileWriter(filePath)), content);
    }

    /**
     * 写入文件
     *
     * @param filePath 文件路径
     * @param fileName 文件名
     * @param is       输入流
     * @param size     每次读取的缓冲大小
     * @throws java.io.IOException
     */
    public void writeToFile(String filePath, String fileName, InputStream is,
                            int size, OnWriteListener owl) throws IOException {
        int count = 0;
        int progress = 0;
        byte[] buff = new byte[size];
        FileOutputStream fos = Streams.obtainFileOutputStream(filePath,
                fileName);
        while ((count = is.read(buff)) != -1) {
            progress += count;
            owl.onWrite(fos, 0, count);
            owl.onProgressChanged(progress);
        }
        fos.flush();
        is.close();
        fos.close();
    }

    /**
     * 创建文件
     *
     * @param path 文件路径
     * @return true表示已经创建成功，false则表示异常或创建失败
     */
    public static boolean createFiles(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return false;
    }

    /**
     * 删除所有文件
     *
     * @param filePath 文件路径
     * @return 删除所有文件
     */
    public boolean removeFiles(String filePath) {
        File file = new File(filePath);
        if (!file.isFile()) {
            File[] files = file.listFiles();
            if (files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
                return true;
            }
        } else {
            return file.delete();
        }
        return false;
    }

    /**
     * 获取文件数量
     *
     * @param dirPath 文件夹路径
     * @return 文件数量
     */
    public int getFileCount(String dirPath) {
        File file = new File(dirPath);
        if (file.isDirectory()) {
            return file.listFiles().length;
        }
        return 0;
    }

    /**
     * 获取指定路径目录剩余容量
     *
     * @param dirPath     文件目录
     * @param maxCapacity 最大容量
     * @return 获取指定路径目录剩余容量
     */
    public long getRemainCapacity(String dirPath, long maxCapacity) {
        return maxCapacity - getFolderCapacity(dirPath);
    }

    /**
     * 获取文件夹容量
     *
     * @param dirPath 文件目录
     * @return 获取文件夹容量
     */
    public long getFolderCapacity(String dirPath) {
        long folderSize = 0L;
        File dirFile = new File(dirPath);
        if (dirFile.isDirectory()) {
            File[] files = dirFile.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    folderSize += file.length();
                } else if (file.isDirectory()) {
                    folderSize += getFolderCapacity(file.getPath());
                }
            }
        }
        return folderSize;
    }

    /**
     * 将文件夹内文件放入Map中
     *
     * @param dirPath 文件夹路径
     * @return
     */
    public Map<String, File> directoryToFileMap(String dirPath) {
        File file = new File(dirPath);
        Map<String, File> fileMap = new HashMap<String, File>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    fileMap.put(files[i].getPath(), files[i]);
                }
            }
        }
        return fileMap;
    }

    /**
     * 获取相应类型的存储目录路径
     *
     * @return 获取相应类型的存储目录路径
     */
    public String getDirectory() {
        return path;
    }

    /**
     * 改变文件目录路径
     *
     * @param path 改变文件目录路径
     */
    public void setDirectory(String path) {
        this.path = path;
    }

    /**
     * 存储设备是否可用
     *
     * @return 存储设备是否可用
     */
    public boolean isStorageAvailable() {
        return getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 文件是否存在
     *
     * @param filePath 文件路径
     * @return 文件是否存在
     */
    public boolean isExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 返回当前存储路径文件状态
     *
     * @param filePath 文件路径
     * @return 当前存储路径文件状态
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public String getStorageState(String filePath) {
        File storageFile = new File(path);
        return SDKSupport.isSupportTargetApiVersion(Build.VERSION_CODES.KITKAT_WATCH) ? Environment
                .getExternalStorageState(storageFile) : Environment.getStorageState(storageFile);
    }

    /**
     * 返回外部存储设备状态
     *
     * @return 返回外部存储设备状态
     */
    public String getExternalStorageState() {
        return Environment.getExternalStorageState();
    }

    /**
     * 数据块对象
     *
     * @return 数据块对象
     */
    public StatFs getStatFs() {
        mStatFs = new StatFs(path);
        return mStatFs;
    }

    /**
     * 获取单个数据块大小
     *
     * @return 获取单个数据块大小
     */
    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    public long getBlockSize() {
        return SDKSupport.isSupportTargetApiVersion(Build.VERSION_CODES.KITKAT_WATCH) ? (int) getStatFs()
                .getBlockSizeLong() : (int) getStatFs().getBlockSize();
    }

    /**
     * 获取所有数据块
     *
     * @return 获取所有数据块
     */
    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    public long getBlockCount() {
        return SDKSupport.isSupportTargetApiVersion(Build.VERSION_CODES.KITKAT_WATCH) ? (int) getStatFs()
                .getBlockCountLong() : (int) getStatFs().getBlockCount();
    }

    /**
     * 获取空闲数据块数量
     *
     * @return 获取空闲数据块数量
     */
    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    public long getAvailableBlocks() {
        return SDKSupport.isSupportTargetApiVersion(Build.VERSION_CODES.KITKAT_WATCH) ? (int) getStatFs()
                .getAvailableBlocksLong() : (int) getStatFs()
                .getAvailableBlocks();
    }

    /**
     * 获取指定目录总容量，单位是MB
     *
     * @return 获取指定目录总容量，单位是MB
     */
    public long getTotalCapacity() {
        BigDecimal blockSize = new BigDecimal(getBlockSize());
        BigDecimal blockCount = new BigDecimal(getBlockCount());
        return blockSize.multiply(blockCount).longValue();
    }

    /**
     * 获取指定目录可用空间，单位为MB
     *
     * @return 获取指定目录可用空间，单位为MB
     */
    public long getRemainSpace() {
        BigDecimal blockSize = new BigDecimal(getBlockSize());
        BigDecimal availableBlocks = new BigDecimal(getBlockCount());
        return blockSize.multiply(availableBlocks).longValue();
    }

    @Override
    public void release() {
        if (mDeviceStorage != null) {
            mDeviceStorage = null;
        }
    }

    /**
     * 存储目录类型
     *
     * @author Administrator
     */
    public enum DirectoryType {

        SDCARD_DIRECTORY(Environment.getExternalStorageDirectory()
                + File.separator), DOWNLOAD_CACHE_DIRECTORY(Environment
                .getDownloadCacheDirectory() + File.separator), DATA_DIRECTORY(
                Environment.getDataDirectory() + File.separator), ROOT_DIRECTORY(
                Environment.getRootDirectory() + File.separator),;

        private String directory;

        private DirectoryType(String dir) {
            setDirectory(dir);
        }

        private void setDirectory(String dir) {
            directory = dir;
        }

        public String getDirectory() {
            return directory;
        }

    }

    public interface OnWriteListener {
        public void onWrite(OutputStream os, int offset, int length);

        public void onProgressChanged(int progress);
    }
}
