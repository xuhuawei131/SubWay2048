/**
		* Filecomparator.java V1.0 2014年6月14日 下午10:23:00
		*
		* Copyright JIAYUAN Co. ,Ltd. All rights reserved.
		*
		* Modification history(By WAH-WAY):
		*
		* Description:
		*/

package com.huawei.interfaces;

import java.io.File;
import java.util.Comparator;

public class Filecomparator implements Comparator<File> {

	@Override
	public int compare(File lhs, File rhs) {
		return lhs.getName().compareTo(rhs.getName());
	}

}
