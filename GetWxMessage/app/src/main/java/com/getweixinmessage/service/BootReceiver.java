package com.getweixinmessage.service;

import com.getweixinmessage.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	public BootReceiver() {
		
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v("fq","onReceive action="+intent.getAction());
//		mHtread.start();
		
		String actionBoot = "android.intent.action.BOOT_COMPLETED";
		if(actionBoot.equals(intent.getAction())){
			Intent i = new Intent(context, MainActivity.class);
//			context.startActivity(i);
			context.startService(new Intent("com.getweixinmessage.service.BootServeice"));
		}
		
	}
	
	
	Thread mHtread = new Thread(){
		public void run() {
			while(true){
				Log.v("fq","onReceive time "+System.currentTimeMillis());
				Thread.currentThread();
				try {
					sleep(1000*10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		};
	};
}
