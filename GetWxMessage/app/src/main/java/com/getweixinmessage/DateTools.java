package com.getweixinmessage;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class DateTools {

	public DateTools() {
		// TODO Auto-generated constructor stub
	}

	public static String timeStamp2Date(long time,String format) {  
        if(time <= 0){  
            return "";  
        }  
        if(format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";  
        SimpleDateFormat sdf = new SimpleDateFormat(format);  
        return sdf.format(new Date(time));  
    }  
    /** 
     * ���ڸ�ʽ�ַ���ת����ʱ��� 
     * @param date �ַ������� 
     * @param format �磺yyyy-MM-dd HH:mm:ss 
     * @return 
     */  
    public static String date2TimeStamp(String date_str,String format){  
        try {  
            SimpleDateFormat sdf = new SimpleDateFormat(format);  
            return String.valueOf(sdf.parse(date_str).getTime()/1000);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return "";  
    }  
}
