

package com.huawei.interfaces;

import java.io.File;
import java.util.Comparator;

public class Filecomparator implements Comparator<File> {

	@Override
	public int compare(File lhs, File rhs) {
		return lhs.getName().compareTo(rhs.getName());
	}

}
