package com.clubscaddy.custumview;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class FileDir {
	
	public static File createSDCardDir(Context mcContext){
		File folder = new File(Environment.getExternalStorageDirectory() + "/Tennis");
		boolean success = true;
		if (!folder.exists()) {
			success = folder.mkdir();
		}
		return folder;
	}
	
	public static File createAudioSubDir(Context mContext,File dirPath){
		File audioDir = new File(dirPath, "Audio");
		boolean success = true;
		if (!audioDir.exists()){
			success =audioDir.mkdirs();
		}
		return audioDir;
	}
	
	
	public static File createImageSubDir(Context mContext,File dirPath){
		File audioDir = new File(dirPath, "Image");
		boolean success = true;
		if (!audioDir.exists()){
			success =audioDir.mkdirs();
		}
		return audioDir;
	}
	
	public static File createVidoeSubDir(Context mContext,File dirPath){
		File audioDir = new File(dirPath, "Video");
		boolean success = true;
		if (!audioDir.exists()){
			success =audioDir.mkdirs();
		}
		return audioDir;
	}
	
	
}
