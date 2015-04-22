package com.icrane.quickmode.app.effect;

import com.icrane.quickmode.R;

/**
 * 动画效果
 * 
 * @author Administrator
 */
public class SlideEffect {

	public int enterAnim;
	public int exitAnim;

	/**
	 * 构造方法
	 */
	public SlideEffect() {
	}

	/**
	 * 构造方法
	 * 
	 * @param enterAnim
	 *            开始动画
	 * @param exitAnim
	 *            结束动画
	 */
	public SlideEffect(int enterAnim, int exitAnim) {
		this.enterAnim = enterAnim;
		this.exitAnim = exitAnim;
	}

	/**
	 * 直接获取一个动画对象
	 * 
	 * @param direction
	 *            枚举
	 * @return 动画对象
	 */
	public static SlideEffect obtainBombAnimation(SlideDirection direction) {
		return direction.obtain();
	}
	
	/**
	 * activity动画弹出方向枚举
	 * 
	 * @author Administrator
	 * 
	 */
	public enum SlideDirection {
		/**
		 * 从右至左
		 */
		RIGHT_TO_LEFT(R.anim.slide_in_from_right, R.anim.slide_out_to_left),
		/**
		 * 从左至右
		 */
		LEFT_TO_RIGHT(R.anim.slide_in_from_left, R.anim.slide_out_to_right),
		/**
		 * 从下至上
		 */
		BOTTOM_TO_TOP(R.anim.slide_in_from_bottom, R.anim.slide_out_to_top),
		/**
		 * 从上至下
		 */
		TOP_TO_BOTTOM(R.anim.slide_in_from_top, R.anim.slide_out_to_bottom);

		private SlideEffect slideEffect;

		private SlideDirection(int enterAnim, int exitAnim) {
			slideEffect = new SlideEffect(enterAnim, exitAnim);
		}

		/**
		 * 获取动画
		 * 
		 * @return 返回一个组合动画
		 */
		public SlideEffect obtain() {
			return slideEffect;
		}
	}

}
