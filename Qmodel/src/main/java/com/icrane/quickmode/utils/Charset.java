package com.icrane.quickmode.utils;

/**
 * 网络编码枚举类
 * 
 * @author Administrator
 * 
 */
public enum Charset {

	UTF_8(0), GB_2312(1), GBK(2), BIG_5(3), ISO_8859_1(4), GB_18030(5);

	int mFlag;

	private Charset(int flag) {
		this.mFlag = flag;
	}

	public String obtain() {

		String charsetStr = null;

		switch (mFlag) {
		case 0:
			charsetStr = "UTF-8";
			break;
		case 1:
			charsetStr = "GB2312";
			break;
		case 2:
			charsetStr = "GBK";
			break;
		case 3:
			charsetStr = "BIG-5";
			break;
		case 4:
			charsetStr = "ISO-8859-1";
			break;
		case 5:
			charsetStr = "GB-18030";
			break;
		default:
			charsetStr = "ISO-8859-1";
			break;
		}
		return charsetStr;
	}
}
