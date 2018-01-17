/**
		* C_Utils.java V1.0 2014年6月10日 上午11:01:18
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
	* 功能描述：
	* 把图片数据写入文件
	* @author 许华维(xuhuawei)
	* <p>创建日期 ：2014-4-29 下午4:25:23</p>
	* @param data
	* <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
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
			* 功能描述：
			*
			* @author 许华维(WAH-WAY)
			* <p>创建日期 ：2014年6月10日 下午6:21:54</p>
			*
			* @param bitmap
			*
			* <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
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
