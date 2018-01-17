package com.huawei.image;

/**
 * 创 建 人: DavikChen 
 * 日 期： 2012-12-11 下午5:41:53 
 * 修 改 人： 
 * 日 期： 
 * 描 述：图片下载参数配置类 
 * 版 本 号： 1.0
 */
public class SmartImageConfig {
	public boolean isAutoRotate = false; //  是否旋转
	public boolean isRound = false; // 是否圆角
	public boolean isDownloadImage = true; // 是否下载图片
	
	public SmartImageConfig() {
		super();
	}

	public SmartImageConfig(boolean isAutoRotate, boolean isRound, boolean isDownloadImage) {
		super();
		this.isAutoRotate = isAutoRotate;
		this.isRound = isRound;
		this.isDownloadImage = isDownloadImage;
	}



}
