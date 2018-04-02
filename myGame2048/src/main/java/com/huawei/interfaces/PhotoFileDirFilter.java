

package com.huawei.interfaces;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Comparator;

public class PhotoFileDirFilter implements FilenameFilter, Comparator<File> {

    @Override
    public boolean accept(File dir, String filename) {
        if (!dir.isDirectory()) {
            return true;
        }
        filename = filename.toLowerCase();
        if (filename.indexOf(".") == -1) {
            return false;
        }
        if (filename.endsWith(".jpg") || filename.endsWith(".png")
                || filename.endsWith(".pnga")) {
            return true;
        }
        return false;
    }

    @Override
    public int compare(File lhs, File rhs) {
        return lhs.getName().compareToIgnoreCase(rhs.getName());

    }

}
