package com.getweixinmessage.network;

import com.getweixinmessage.FLog;
import com.getweixinmessage.GetMessage;

public class SendTable {

	public SendTable() {
		// TODO Auto-generated constructor stub
	}

	public static void send(final String table, final int uid, final String data, final long tag){
		Thread thread = new Thread(){
			@Override
			public void run() {
				FLog.v("fq","--------> send:"+table+" tag="+tag);
				String desUrl = "http://192.168.3.30:81/WeiXin/ReceiveMsgData.ashx";
				String param = "table="+table+"&uid="+uid+"&data="+data;
				String res = DataHttp.sendHttpPost(desUrl, param);
				FLog.v("fq","========>recieve:"+table+" res="+res);
				
				if(GetMessage.TABLE_NAME_MESSAGE.equalsIgnoreCase(table) && "ok".equalsIgnoreCase(res)){
					GetMessage.setLine(String.valueOf(uid), GetMessage.PRE_MESSAGE, tag);
				}
			}
		};
		
		thread.start();
	}
}
