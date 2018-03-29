package com.getweixinmessage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class RootTools {

	public RootTools() {
		// TODO Auto-generated constructor stub
	}

	public static boolean is_root() {
		boolean res = false;
		try {
			if ((!new File("/system/bin/su").exists())
					&& (!new File("/system/xbin/su").exists())) {
				res = false;
			} else {
				res = true;
			};
		} catch (Exception e) {

		}
		return res;
	}
	
	public static boolean get_root(){
		boolean res = true;
	    if (!is_root()){
	    	try{
	            Runtime.getRuntime().exec("su");
	            res = true;
	        }
	        catch (Exception e){
	        	res = false;
	        }
	    }
	    return res;
	}
	
	
	/**
	 * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
	 * 
	 * @return 应用程序是/否获取Root权限
	 */
	public static boolean upgradeRootPermission(String pkgCodePath) {
	    Process process = null;
	    DataOutputStream os = null;
	    try {
	        String cmd="chmod 777 " + pkgCodePath;
	        process = Runtime.getRuntime().exec("su"); //切换到root帐号
	        os = new DataOutputStream(process.getOutputStream());
	        os.writeBytes(cmd + "\n");
	        os.writeBytes("exit\n");
	        os.flush();
	        process.waitFor();
	    } catch (Exception e) {
	        return false;
	    } finally {
	        try {
	            if (os != null) {
	                os.close();
	            }
	            process.destroy();
	        } catch (Exception e) {
	        }
	    }
	    return true;
	}
	
	
	public static boolean do_exec(String cmd) { 
        try {  
            Process p = Runtime.getRuntime().exec(cmd);  
            /*BufferedReader in = new BufferedReader(  
                                new InputStreamReader(p.getInputStream()));  
            String line = null;  
            while ((line = in.readLine()) != null) {  
                s += line + "/n";                 
            }  */
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
            return false;
        }  
        return true;       
    }
	
	public static boolean runSu(String cmd) {
	    Process process = null;
	    DataOutputStream os = null;
	    try {
	        process = Runtime.getRuntime().exec("su"); //切换到root帐号
	        os = new DataOutputStream(process.getOutputStream());
	        os.writeBytes(cmd + "\n");
	        os.writeBytes("exit\n");
	        os.flush();
	        process.waitFor();
	    } catch (Exception e) {
	        return false;
	    } finally {
	        try {
	            if (os != null) {
	                os.close();
	            }
	            process.destroy();
	        } catch (Exception e) {
	        }
	    }
	    return true;
	}
}
