/**
		* Util.java V1.0 2014��6��12�� ����11:11:06
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
			 * ��pxת��Ϊdip
			 * @param px ��ת����pxֵ
			 * @return dipֵ
			 */
			public static int px2dip(int px) {
				return (int) (px / Constants.density + 0.5f * (px >= 0 ? 1 : -1));
			}

			/**
			 * ��dipת��Ϊpx
			 * @param px ��ת����dipֵ
			 * @return pxֵ
			 */
			public static int dip2px(int dip) {
				return (int) (dip * Constants.density + 0.5f * (dip >= 0 ? 1 : -1));
			}
			
			public static void moveFile2Dir(String dir,String des){
				
			}
			
			
}
