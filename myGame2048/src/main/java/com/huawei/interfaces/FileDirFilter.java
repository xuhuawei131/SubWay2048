/**
		* FileDirFilter.java V1.0 2014��6��14�� ����8:43:48
		*
		* Copyright JIAYUAN Co. ,Ltd. All rights reserved.
		*
		* Modification history(By WAH-WAY):
		*
		* Description:
		*/

		package com.huawei.interfaces;

import java.io.File;
import java.io.FilenameFilter;

public class FileDirFilter implements FilenameFilter {
	@Override
	public boolean accept(File dir, String filename) {
		if(dir!=null){
			if(dir.isDirectory()){
				return true;
			}
		}
		return false;

	}

}
