package com.icrane.quickmode.widget.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.icrane.quickmode.R;

public class RoundImageView extends BasicImageView {

	private static final float DEFAULT_ROUND = 0.1f;
	private static final int DEFAULT_BASE = 100;
	private float round;

	public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		final TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.RoundImageView);
		round = a.getFraction(R.styleable.RoundImageView_round,
				DEFAULT_BASE, 0, DEFAULT_ROUND);
		a.recycle();
	}

	public RoundImageView(Context context) {
		super(context);
	}

	public void setRound(float round) {
		this.round = round;
	}

	public float getRound() {
		return this.round;
	}

	@Override
	public void drawTo(Canvas canvas, RectF rectF, Rect rect, Paint paint) {
		canvas.drawRoundRect(rectF, round, round, paint);
	}

}
