package com.huawei.image;


public class SmartImageConfig {
	public boolean isAutoRotate = false;
	public boolean isRound = false;
	public boolean isDownloadImage = true;
	
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
