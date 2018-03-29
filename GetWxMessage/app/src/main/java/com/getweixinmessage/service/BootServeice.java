package com.getweixinmessage.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.getweixinmessage.GetMessage;

public class BootServeice extends Service {

	public BootServeice() {
		// TODO Auto-generated constructor stub
	}
	
	long mThreadTime = 0;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.d("fq", "BootServeice onBind"); 
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.d("fq", "BootServeice onBind"); 
		super.onCreate();
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("fq", "BootServeice onStartCommand"); 
		if((System.currentTimeMillis()-mThreadTime) > 1000*60*10){
			Log.d("fq", "onStartCommand MessageThread start");
			new MessageThread().start();
		}
		return 0;
	}

	class MessageThread extends Thread{
		@Override
		public void run() {
//			Log.v("fq","time = "+DateTools.timeStamp2Date(Long.valueOf("1463038472296"), "yyyy-MM-dd HH:mm:ss"));
			
			long time =0;
			while(true){
				mThreadTime =  time = SystemClock.uptimeMillis();
				GetMessage.getMessage();
				long sleeptime = SystemClock.uptimeMillis() - time + 3*1000;
				if(sleeptime>0){
					Thread.currentThread();
					try {
						sleep(sleeptime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
	};
		
}
