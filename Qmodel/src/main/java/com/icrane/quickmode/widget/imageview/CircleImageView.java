package com.icrane.quickmode.widget.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.icrane.quickmode.R;

public class CircleImageView extends BasicImageView {

	private static final int DEFAULT_BASE = 1;

	private float radius;
	private float radiusPercent;

	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		final TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CircleImageView);
		radiusPercent = a.getFraction(
				R.styleable.CircleImageView_radius, DEFAULT_BASE, 0,
				0.5f);
		a.recycle();
	}

	public CircleImageView(Context context) {
		super(context);
	}

	@Override
	public void drawTo(Canvas canvas, RectF rectF, Rect rect, Paint paint) {
		radius = rectF.width() * radiusPercent;
		canvas.drawCircle(rectF.centerX(), rectF.centerY(), radius, paint);
	}

	/**
	 * 获取圆形图片的半径
	 * 
	 * @return
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * 设置圆形图片的半径
	 * 
	 * @param radius
	 */
	public void setRadius(float radius) {
		this.radius = radius;
	}

}
