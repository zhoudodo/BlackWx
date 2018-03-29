package com.getweixinmessage;

import java.util.ArrayList;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.getweixinmessage.model.TableMessage;
import com.getweixinmessage.model.TableMessageItem;
import com.getweixinmessage.model.TableRcontact;
import com.getweixinmessage.model.TableRcontactItem;
import com.getweixinmessage.model.TableUserInfo;
import com.getweixinmessage.model.TableUserInfoItem;
import com.getweixinmessage.network.SendTable;

public class Tools {

	public Tools() {
		// TODO Auto-generated constructor stub
	}

	public static void setColumn(Cursor c, TableMessageItem item){
		item.setMsgId(c.getInt(c.getColumnIndex("msgId")));
		item.setMsgSvrId(c.getLong(c.getColumnIndex("msgSvrId")));
		item.setType(c.getInt(c.getColumnIndex("type")));
		item.setStatus(c.getInt(c.getColumnIndex("status")));
		item.setIsSend(c.getInt(c.getColumnIndex("isSend")));
		item.setIsShowTimer(c.getLong(c.getColumnIndex("isShowTimer")));
		item.setCreateTime(c.getLong(c.getColumnIndex("createTime")));
		item.setTalker(c.getString(c.getColumnIndex("talker")));
		item.setContent(c.getString(c.getColumnIndex("content")));
		item.setImgPath(c.getString(c.getColumnIndex("imgPath")));
		item.setReserved(c.getString(c.getColumnIndex("reserved")));
		item.setTransContent(c.getString(c.getColumnIndex("transContent")));
		item.setTransBrandWording(c.getString(c.getColumnIndex("transBrandWording")));
		item.setTalkerId(c.getLong(c.getColumnIndex("talkerId")));
		item.setBizClientMsgId(c.getString(c.getColumnIndex("bizClientMsgId")));
		item.setBizChatId(c.getLong(c.getColumnIndex("bizChatId")));
		item.setBizChatUserId(c.getString(c.getColumnIndex("bizChatUserId")));
		item.setMsgSeq(c.getLong(c.getColumnIndex("msgSeq")));
		item.setFlag(c.getInt(c.getColumnIndex("flag")));
		
		/*item.setCreateTime(c.getLong(c.getColumnIndex("createTime")));
		item.setTalker(c.getString(c.getColumnIndex("talker")));
		item.setContent(c.getString(c.getColumnIndex("content")));*/
	}
	
	public static void setColumnUserinfo(Cursor c, TableUserInfoItem item){
		item.setId(c.getLong(c.getColumnIndex("id")));
		item.setType(c.getInt(c.getColumnIndex("type")));
		item.setValue(c.getString(c.getColumnIndex("value")));
	}
	
	public static void setColumnRcontact(Cursor c, TableRcontactItem item){
		item.setUsername(c.getString (c.getColumnIndex("username")));
		item.setAlias(c.getString(c.getColumnIndex("alias")));
		item.setConRemark(c.getString(c.getColumnIndex("conRemark")));
		item.setDomainList(c.getString(c.getColumnIndex("domainList")));
		item.setNickname(c.getString(c.getColumnIndex("nickname")));
		item.setPyInitial(c.getString(c.getColumnIndex("pyInitial")));
		item.setQuanPin(c.getString(c.getColumnIndex("quanPin")));
		item.setShowHead(c.getLong(c.getColumnIndex("showHead")));
		item.setType(c.getLong(c.getColumnIndex("type")));
		item.setWeiboFlag(c.getLong(c.getColumnIndex("weiboFlag")));
		item.setWeiboNickname(c.getString(c.getColumnIndex("weiboNickname")));
		item.setConRemarkPYFull(c.getString(c.getColumnIndex("conRemarkPYFull")));
		item.setConRemarkPYShort(c.getString(c.getColumnIndex("conRemarkPYShort")));
		item.setVerifyFlag(c.getLong(c.getColumnIndex("verifyFlag")));
		item.setEncryptUsername(c.getString(c.getColumnIndex("encryptUsername")));
		item.setChatroomFlag(c.getLong(c.getColumnIndex("chatroomFlag")));
		item.setDeleteFlag(c.getLong(c.getColumnIndex("deleteFlag")));
		item.setContactLabelIds(c.getString(c.getColumnIndex("contactLabelIds")));
	}
	
	
	public static void getQuery(int uin, SQLiteDatabase db,String table, final String PRE){
		FLog.i("fq", "getQuery "+table);
		Cursor c = db.query(table, null, null, null, null, null,
				null);
		if(c==null || c.getCount()<1){
			FLog.e("fq", "start query "+table+" error");
			return ;
		}
		
		/*for(int i=0; i<c.getColumnCount();i++){
			FLog.v("fq","private String "+c.getColumnName(i)+";");
		}*/
		
		int moveCount = 0;
		long lastId = 0 ;
		boolean bMoveCursor = false;
//		int lastMsgId
		FLog.i("fq", "start query "+table);
		String data="";
		if(c.getCount()>0){
			c.moveToFirst();
			bMoveCursor = true;
		}
		if(GetMessage.PRE_USERINFO.equalsIgnoreCase(PRE)){
			TableUserInfo tableuserinfo = new TableUserInfo();
			ArrayList<TableUserInfoItem> listUserinfo = new ArrayList<TableUserInfoItem>();
			while (c.moveToNext()) {
				moveCount++;
				TableUserInfoItem item = new TableUserInfoItem();
				Tools.setColumnUserinfo(c, item);
				listUserinfo.add(item);
			}
			tableuserinfo.setList(listUserinfo);
			data = JSON.toJSONString(tableuserinfo);
		}
		
		if(GetMessage.PRE_MESSAGE.equalsIgnoreCase(PRE)){
			TableMessage tablemsg = new TableMessage();
			ArrayList<TableMessageItem> listmsg = new ArrayList<TableMessageItem>();
			long preLastMsgId = GetMessage.getLine(String.valueOf(uin), PRE);
			FLog.v("fq","lastMsgId="+preLastMsgId);
			while (c.moveToNext()) {
				lastId = c.getLong(c.getColumnIndex("msgId"));
				if(preLastMsgId!=0 && lastId<=preLastMsgId){
					continue;
				}
				moveCount++;
				TableMessageItem item = new TableMessageItem();
				Tools.setColumn(c, item);
				listmsg.add(item);
			}
			tablemsg.setList(listmsg);
			if(moveCount>0){
				data = JSON.toJSONString(tablemsg);
			}
		}
		
		if(GetMessage.PRE_RCONTACT.equalsIgnoreCase(PRE)){
			TableRcontact tablecontact = new TableRcontact();
			ArrayList<TableRcontactItem> listcontact = new ArrayList<TableRcontactItem>();
			while (c.moveToNext()) {
				moveCount++;
				TableRcontactItem item = new TableRcontactItem();
				Tools.setColumnRcontact(c, item);
				listcontact.add(item);
			}
			tablecontact.setList(listcontact);
			data = JSON.toJSONString(tablecontact);
		}
		FLog.i("fq", "end query "+table+" moveCount="+moveCount);
		if(!TextUtils.isEmpty(data)){
			SendTable.send(table, uin, data, lastId);
		}
		c.close();
//		GetMessage.setLine(PRE, offsetLine+moveCount);
	}
}
