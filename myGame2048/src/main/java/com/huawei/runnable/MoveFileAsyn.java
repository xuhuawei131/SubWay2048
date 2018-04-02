

		package com.huawei.runnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.huawei.interfaces.FileDirFilter;

import android.os.AsyncTask;

		public class MoveFileAsyn extends AsyncTask<String, Integer, Integer> {
			private String oldF;
			private String newF;
			public MoveFileAsyn(String oldFile,String newFile){
				this.oldF=oldFile;
				this.newF=newFile;
			}
			
			@Override
			protected Integer doInBackground(String... params) {
				File oldFile=new File(oldF);
				File []Files=oldFile.listFiles(new FileDirFilter());
				for(int i=0;i<Files.length;i++){
					try {
						FileInputStream	fi = new FileInputStream(Files[i]);
						FileOutputStream fo = new FileOutputStream(newF+"/"+Files[i].getName());
					        byte data[] = new byte[fi.available()];
					        fi.read(data);
					        fo.write(data);
					        fi.close();
						    fo.close();
						    publishProgress(i/Files.length*100);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
							
					} catch (IOException e) {
						e.printStackTrace();
							
					}
			       
				}
				return null;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
					
			}

			@Override
			protected void onPostExecute(Integer result) {
				super.onPostExecute(result);
					
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
					
			}

}
