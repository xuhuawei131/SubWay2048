/**
		* C_Utils.java V1.0 2014��6��10�� ����11:01:18
		*
		* Copyright JIAYUAN Co. ,Ltd. All rights reserved.
		*
		* Modification history(By WAH-WAY):
		*
		* Description:
		*/

		package com.huawei.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;

import com.huawei.constants.C_Constants;

public class C_Utils {
	
	
	
	/**
	* ����������
	* ��ͼƬ����д���ļ�
	* @author ��ά(xuhuawei)
	* <p>�������� ��2014-4-29 ����4:25:23</p>
	* @param data
	* <p>�޸���ʷ ��(�޸��ˣ��޸�ʱ�䣬�޸�ԭ��/����)</p>
	*/
	public static void writeData2File(byte[] data) {
		File picPath = new File(C_Constants.diskCachePath + System.nanoTime()
				+ ".pnga");
		BufferedOutputStream bufferedOutputStream;
		try {
			bufferedOutputStream = new BufferedOutputStream(
					new FileOutputStream(picPath));
			bufferedOutputStream.write(data);
			bufferedOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
			* ����������
			*
			* @author ��ά(WAH-WAY)
			* <p>�������� ��2014��6��10�� ����6:21:54</p>
			*
			* @param bitmap
			*
			* <p>�޸���ʷ ��(�޸��ˣ��޸�ʱ�䣬�޸�ԭ��/����)</p>
	 */
	public static void saveBitmap(Bitmap bitmap) {
		File f = new File(C_Constants.diskCachePath +"/"+System.nanoTime()
				+ ".pnga");
		try {
			FileOutputStream out = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}
