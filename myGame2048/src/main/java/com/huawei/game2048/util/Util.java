

package com.huawei.game2048.util;

import com.huawei.game2048.constants.Constants;


public class Util {

    public static int px2dip(int px) {
        return (int) (px / Constants.density + 0.5f * (px >= 0 ? 1 : -1));
    }


    public static int dip2px(int dip) {
        return (int) (dip * Constants.density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    public static void moveFile2Dir(String dir, String des) {

    }


}
