package com.huawei.image;

/**
 * �� �� ��: DavikChen 
 * �� �ڣ� 2012-12-11 ����5:41:53 
 * �� �� �ˣ� 
 * �� �ڣ� 
 * �� ����ͼƬ���ز��������� 
 * �� �� �ţ� 1.0
 */
public class SmartImageConfig {
	public boolean isAutoRotate = false; //  �Ƿ���ת
	public boolean isRound = false; // �Ƿ�Բ��
	public boolean isDownloadImage = true; // �Ƿ�����ͼƬ
	
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
