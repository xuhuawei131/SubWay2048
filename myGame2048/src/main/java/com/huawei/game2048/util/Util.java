/**
		* Util.java V1.0 2014年6月12日 上午11:11:06
		*
		* Copyright JIAYUAN Co. ,Ltd. All rights reserved.
		*
		* Modification history(By WAH-WAY):
		*
		* Description:
		*/

		package com.huawei.game2048.util;

import com.huawei.game2048.constants.Constants;


		public class Util {
			/**
			 * 将px转化为dip
			 * @param px 带转化的px值
			 * @return dip值
			 */
			public static int px2dip(int px) {
				return (int) (px / Constants.density + 0.5f * (px >= 0 ? 1 : -1));
			}

			/**
			 * 将dip转化为px
			 * @param px 带转化的dip值
			 * @return px值
			 */
			public static int dip2px(int dip) {
				return (int) (dip * Constants.density + 0.5f * (dip >= 0 ? 1 : -1));
			}
			
			public static void moveFile2Dir(String dir,String des){
				
			}
			
			
}
