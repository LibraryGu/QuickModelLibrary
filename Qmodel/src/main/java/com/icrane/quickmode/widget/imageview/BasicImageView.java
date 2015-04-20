package com.icrane.quickmode.widget.imageview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.icrane.quickmode.R;
import com.icrane.quickmode.utils.common.CommonUtils;
import com.icrane.quickmode.utils.image.ImageLoaderUtils;

public abstract class BasicImageView extends ImageView {

    private Bitmap realBitmap;
    private PorterDuffXfermode mPorterDuffXfermode = new PorterDuffXfermode(
            Mode.SRC_IN);

    public BasicImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BasicImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.BasicImageView);
        String url = a.getString(R.styleable.BasicImageView_fromURL);
        if (url != null)
            this.setFromURLImage(url);
        a.recycle();
    }

    public BasicImageView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.drawRealImage(canvas);
    }

    /**
     * 绘制新层到当前位图中
     *
     * @param bitmap
     * @return
     */
    protected Bitmap drawToBitmap(Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        Rect rect = new Rect(0, 0, width, height);
        RectF rectF = new RectF(rect);

        paint.setColor(color);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);

        drawTo(canvas, rectF, rect, paint);
        paint.setXfermode(mPorterDuffXfermode);
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return newBitmap;
    }

    /**
     * 设置来自于URL的图片
     *
     * @param url
     */
    public void setFromURLImage(String url) {
        ImageLoaderUtils.getDefaultImageLoaderUtils().getImageLoader().displayImage(url, this);
    }

    /**
     * 绘制真实图片
     *
     * @param canvas
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void drawRealImage(Canvas canvas) {
        Drawable bitmapDrawable = getDrawable();
        if (bitmapDrawable == null) {
            bitmapDrawable = getBackground();
            drawRealImage(canvas, bitmapDrawable);
            this.setBackground(CommonUtils.obtainDrawable(getContext(),
                    realBitmap));
            return;
        }
        drawRealImage(canvas, bitmapDrawable);
    }

    /**
     * 绘制真实图片
     *
     * @param canvas
     * @param bitmapDrawable
     */
    protected void drawRealImage(Canvas canvas, Drawable bitmapDrawable) {
        // 获取设置的真实位图
        realBitmap = drawToBitmap(((BitmapDrawable) bitmapDrawable).getBitmap());
        final Rect realBitmapRect = new Rect(0, 0, realBitmap.getWidth(),
                realBitmap.getHeight());
        final RectF realBitmapRectF = new RectF(realBitmapRect);
        canvas.drawBitmap(realBitmap, realBitmapRect, realBitmapRectF,
                new Paint());
    }

    /**
     * 获取位图对象
     *
     * @return
     */
    public Bitmap getBitmap() {
        return realBitmap;
    }

    /**
     * 设置位图对象
     *
     * @param realBitmap
     */
    public void setBitmap(Bitmap realBitmap) {
        this.realBitmap = realBitmap;
    }

    /**
     * 获取PorterDuffXfermode
     *
     * @return
     */
    protected PorterDuffXfermode getPorterDuffXfermode() {
        return mPorterDuffXfermode;
    }

    /**
     * 设置PorterDuffXfermode
     *
     * @param xfermode
     */
    protected void setPorterDuffXfermode(PorterDuffXfermode xfermode) {
        this.mPorterDuffXfermode = xfermode;
    }

    /**
     * 绘制
     *
     * @param canvas
     * @param rectF
     * @param paint
     */
    public abstract void drawTo(Canvas canvas, RectF rectF, Rect rect,
                                Paint paint);

}
