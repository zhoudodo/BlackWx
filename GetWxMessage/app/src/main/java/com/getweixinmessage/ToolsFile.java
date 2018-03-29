package com.getweixinmessage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;
import android.util.Log;

public class ToolsFile {

	public ToolsFile() {
		// TODO Auto-generated constructor stub
	}

	public static String findFirstFile(String dir, String filename){
		String res = null;
		File fileDir = new File(dir);
		if(fileDir!=null && fileDir.isDirectory() && fileDir.list()!=null){
			for(File f : fileDir.listFiles()){
				if(f.isFile() && f.getName().equalsIgnoreCase(filename)){
					Log.v("fq","findFile get = "+f.getAbsolutePath());
					return f.getAbsolutePath();
				}
				if(f.isDirectory()){
					res=findFirstFile(f.toString(), filename);
					if(!TextUtils.isEmpty(res)){
						Log.v("fq","findFile dir "+fileDir.toString()+"-->"+f.toString());
						return res;
					}
				}
			}
		}
		return res;
	}
	
/*	public static List<String> findAllFile(String dir, String filename, List<String> resList){
		if(resList == null){
			resList = new ArrayList<String>();
		}
		File fileDir = new File(dir);
		if(fileDir!=null && fileDir.isDirectory() && fileDir.list()!=null){
			for(File f : fileDir.listFiles()){
				if(f.isFile() && f.getName().equalsIgnoreCase(filename)){
//					Log.v("fq","findFile get = "+f.getAbsolutePath());
					resList.add(f.getAbsolutePath());
				}
				if(f.isDirectory()){
					findAllFile(f.toString(), filename, resList);
				}
			}
		}
		return resList;
	}*/
	
	public static List<String> findAllFile(File dir, String filename, List<String> resList){
		if(resList == null){
			resList = new ArrayList<String>();
		}
		File fileDir = dir;
		if(fileDir!=null && fileDir.isDirectory() && fileDir.list()!=null){
			for(File f : fileDir.listFiles()){
				if(f.isFile() && f.getName().equalsIgnoreCase(filename)){
//					Log.v("fq","findFile get = "+f.getAbsolutePath());
					resList.add(f.getAbsolutePath());
				}
				if(f.isDirectory()){
					findAllFile(f, filename, resList);
				}
			}
		}
		return resList;
	}
}
