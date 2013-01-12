package com.jonathanysp.weddingapp;

import java.io.File;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class tools {
	
	public static final int SUCCESS = 1;
	public static final int LOGIN_ERROR = 2;
	public static final int TOKEN_ERROR = 3;
	public static final int UPLOAD_ERROR = 4;
	public static final String SERVER = new String("http://192.168.1.125/");
	//public static final String SERVER = new String("http://ec2-67-202-1-120.compute-1.amazonaws.com/");
	
	public static Uri getOutputMediaFileUri(){
		return Uri.fromFile(getOutputMediafile());
	}

	public static File getOutputMediafile(){
		
		File mediaDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Wedding");
		
		if(!mediaDir.exists()){
			if(!mediaDir.mkdirs()){
				Log.d("Wedding App", "failed to create: " + mediaDir.getAbsolutePath());
			}
		}
		
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
	    File mediaFile;
	    mediaFile = new File(mediaDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
	    
	    return mediaFile;
	}
	
	public static String getToken(Context c){
		SharedPreferences prefs = c.getSharedPreferences("login", 0);
		return prefs.getString("token", null);
	}
	
	public static String getImageUrl(String token, String name, String quality){
		return SERVER + "website/getPhoto.php?mode=mobile&token=" + token + "&name=" + name + "&quality=" + quality;
	}
}
