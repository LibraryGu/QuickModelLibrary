package com.icrane.quickmode.utils.image;

import android.content.Context;
import android.graphics.Bitmap;

import com.icrane.quickmode.app.QModel;
import com.icrane.quickmode.app.Releasable;
import com.icrane.quickmode.utils.common.CommonUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * 这个类内部集成了ImageLoader，是一个ImageLoader的工具类，可以使用这个工具为ImageView加载图片。
 */
public final class ImageLoaderUtils implements Releasable {

    public static final int DEFAULT_MEMORY_CACHE_SIZE = 1 * 1024 * 1024;
    public static final int DEFAULT_DISK_CACHE_SIZE = 20 * 1024 * 1024;
    public static final int DEFAULT_DISK_FILE_COUNT = 200;

    private static ImageLoaderUtils imageLoaderUtils = null;

    private String cacheDir;

    private static ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;
    private ImageLoaderConfiguration imageLoaderConfiguration;

    protected ImageLoaderUtils(String cacheDir) {
        this.cacheDir = cacheDir;
        imageLoader = ImageLoader.getInstance();
        defaultConfig(QModel.getInstance().getTopActivity(), this.cacheDir);
    }

    /**
     * 初始化ImageLoaderUtils,获取默认工具实例，图片缓存存储在手机data文件夹中
     *
     * @return 返回默认的工具
     */
    public static ImageLoaderUtils getDefaultImageLoaderUtils() {
        return getInstance("data");
    }

    /**
     * 初始化ImageLoaderUtils
     *
     * @param cacheDir 图片缓存存储的文件夹
     * @return 实例对象
     */
    public static ImageLoaderUtils getInstance(String cacheDir) {
        if (CommonUtils.isEmpty(imageLoaderUtils)) {
            imageLoaderUtils = new ImageLoaderUtils(cacheDir);
        }
        return imageLoaderUtils;
    }

    /**
     * ImageLoader的配置
     *
     * @param config 配置对象
     */
    public void config(ImageLoaderConfiguration config) {
        this.imageLoaderConfiguration = config;
        imageLoader.init(imageLoaderConfiguration);
    }

    /**
     * ImageLoader的默认配置
     *
     * @param context  上下文对象
     * @param cacheDir 缓存存储路径
     */
    public void defaultConfig(Context context, String cacheDir) {

        //图片保存系统路径
        File cacheDirectory = StorageUtils.getOwnCacheDirectory(context, cacheDir);
        displayImageOptions = new DisplayImageOptions.Builder()
                .considerExifParams(true) //启用EXIF和JPEG图像格式
                .cacheInMemory(true) //启用内存缓存
                .cacheOnDisk(true) //启用外存缓存
                .imageScaleType(ImageScaleType.EXACTLY) //图片缩放类型
                .bitmapConfig(Bitmap.Config.ARGB_8888) //bitmap配置
                .build();
        config(new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(DEFAULT_MEMORY_CACHE_SIZE))
                .memoryCacheSize(DEFAULT_MEMORY_CACHE_SIZE)
                .diskCache(new UnlimitedDiscCache(cacheDirectory)) // 默认
                .diskCacheSize(DEFAULT_DISK_CACHE_SIZE)
                .diskCacheFileCount(DEFAULT_DISK_FILE_COUNT)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // 默认
                .imageDownloader(new BaseImageDownloader(context)) // 默认
                .imageDecoder(new BaseImageDecoder(true)) // 默认
                .defaultDisplayImageOptions(displayImageOptions) // 默认
                .writeDebugLogs()
                .build());
    }

    /**
     * 获取DisplayImageOptions显示选项对象
     *
     * @return 图片显示配置对象
     */
    public DisplayImageOptions getOptions() {
        return displayImageOptions;
    }

    /**
     * 获取ImageLoaderConfigurationI配置对象
     *
     * @return ImageLoaderConfigurationI配置对象
     */
    public ImageLoaderConfiguration getConfiguration() {
        return imageLoaderConfiguration;
    }

    /**
     * 获取ImageLoader对象
     *
     * @return ImageLoader对象
     */
    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    /**
     * 设置缓存文件夹
     *
     * @param cacheDir 缓存文件夹
     */
    public void setCacheDir(String cacheDir) {
        this.cacheDir = cacheDir;
    }

    /**
     * 获取缓存文件夹
     *
     * @return 缓存文件夹
     */
    public String getCacheDir() {
        return cacheDir;
    }

    /**
     * ImageLoader生命周期，resume()
     */
    public static void resume() {
        if (imageLoader != null)
            imageLoader.resume();
    }

    /**
     * ImageLoader生命周期，用于控制内部线程
     */
    public static void pause() {
        if (imageLoader != null)
            imageLoader.pause();
    }

    /**
     * ImageLoader生命周期，
     */
    public static void stop() {
        if (imageLoader != null)
            imageLoader.stop();
    }

    /**
     * ImageLoader生命周期，
     */
    public static void destroy() {
        if (imageLoader != null)
            imageLoader.destroy();
    }

    @Override
    public void release() {
        if (!CommonUtils.isEmpty(imageLoaderUtils)) {
            this.destroy();
            imageLoaderUtils = null;
        }
    }
}
