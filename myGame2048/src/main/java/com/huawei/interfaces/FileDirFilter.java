

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
