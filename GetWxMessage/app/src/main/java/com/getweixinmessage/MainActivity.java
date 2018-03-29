package com.getweixinmessage;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.getweixinmessage.Utils.PermissionUtil;
import com.getweixinmessage.Utils.ServiceUtils;

public class MainActivity extends Activity {

	Activity mMainAct = null;
	private TextView mShowText;
	private ScrollView mScrollView;
	private StringBuilder shwoStr = new StringBuilder();
	public static Handler gHandler;

	private RelativeLayout mRl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mRl = (RelativeLayout)findViewById(R.id.rl);

		mShowText = (TextView) findViewById(R.id.show_text);
		mScrollView = (ScrollView) findViewById(R.id.my_scroll);
		mMainAct = this;
		gHandler = mHandlr;

		final Intent intent = new Intent();
		intent.setAction("com.getweixinmessage.service.BootServeice");
		eintent = new Intent(ServiceUtils.createExplicitFromImplicitIntent(this,intent));
		bindService(eintent,conn, Service.BIND_AUTO_CREATE);
		if (Build.VERSION.SDK_INT < 23) {
			startService(eintent);
		}else {
			requestPermiss();
		}
	}

	Intent eintent;
	ServiceConnection conn=new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Toast.makeText(getApplication(),"后台服务连接成功",Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Toast.makeText(getApplication(),"后台服务断开连接",Toast.LENGTH_SHORT).show();
		}

	};
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(conn);
	}

	Handler mHandlr = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			shwoStr.append((String)msg.obj+"\r\n-----	--------	-------	-----	----"+"\r\n");
			mShowText.setText(shwoStr.toString());
			mScrollView.fullScroll(ScrollView.FOCUS_DOWN); 
		}
	};


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	private static final int REQUEST_CODE = 0x01;
	private static String[] PERMISSIONS_STORAGE = {
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.READ_PHONE_STATE
	};

	private void requestPermiss() {
		PermissionUtil.checkPermission(MainActivity.this, mRl, PERMISSIONS_STORAGE,REQUEST_CODE, new PermissionUtil.permissionInterface() {
			@Override
			public void success() {
				startService(eintent);
			}
		});
	}
}
