package com.getweixinmessage;

import android.util.Log;

public class FLog {

	public FLog() {
		// TODO Auto-generated constructor stub
	}

	public static boolean SHOW = true;
	public static String  TAG  = "fq";
	public static String  TAG_T  = "temp";
	
	public static void v(String str){
		if(!SHOW) return;
		Log.v(TAG,str);
		if(MainActivity.gHandler != null){
			MainActivity.gHandler.obtainMessage(0, str).sendToTarget();
		}
	}
	
	public static void t(String str){
		if(!SHOW) return;
		Log.v(TAG_T,str);
	}
	
	public static void e(String str){
		if(!SHOW) return;
		Log.e(TAG,str);
		if(MainActivity.gHandler != null){
			MainActivity.gHandler.obtainMessage(0, str).sendToTarget();
		}
	}
	
	
	public static void v(String t, String str){
		if(!SHOW) return;
		Log.v(t,str);
		if(MainActivity.gHandler != null){
			MainActivity.gHandler.obtainMessage(0, str).sendToTarget();
		}
	}
	
	public static void e(String t, String str){
		if(!SHOW) return;
		Log.e(t,str);
		if(MainActivity.gHandler != null){
			MainActivity.gHandler.obtainMessage(0, str).sendToTarget();
		}
	}
	
	public static void i(String t, String str){
		if(!SHOW) return;
		Log.i(t,str);
		if(MainActivity.gHandler != null){
			MainActivity.gHandler.obtainMessage(0, str).sendToTarget();
		}
	}
	
	
	
	
	public static void showClassName(){
		String clazzName2 = new Throwable().getStackTrace()[1].getClassName(); 
		FLog.v("showClassName="+clazzName2);
	}
	
	public static String getLineInfo()
    {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getMethodName() + ": Line " + ste.getLineNumber();
    }
}
