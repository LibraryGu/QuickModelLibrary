package com.icrane.quickmode.model;


import java.io.Serializable;

/**
 * 广告类
 * 
 * @author Administrator
 * 
 */
public class Ad implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String imageAdd;
	public String linkAdd;

	public Ad() {
	}

	public Ad(String imageAdd ,String linkAdd) {
		setImageAdd(imageAdd);
		setLinkAdd(linkAdd);
	}

	public String getImageAdd() {
		return imageAdd;
	}

	public void setImageAdd(String imageAdd) {
		this.imageAdd = imageAdd;
	}

	public String getLinkAdd() {
		return linkAdd;
	}

	public void setLinkAdd(String linkAdd) {
		this.linkAdd = linkAdd;
	}

	@Override
	public String toString() {
		return "Ad [imageAdd=" + imageAdd + ", linkAdd=" + linkAdd + "]";
	}

}
