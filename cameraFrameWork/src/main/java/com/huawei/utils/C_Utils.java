

		package com.huawei.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;

import com.huawei.constants.C_Constants;

public class C_Utils {
	
	
	

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
