package com.clubscaddy.custumview;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;

import com.clubscaddy.R;

public class SDCardMemory {
	
	public static File createSDCardDir(Context mcContext){
		File folder = new File(Environment.getExternalStorageDirectory() + "/Clubscaddy");
		boolean success = true;
		if (!folder.exists()) {
			success = folder.mkdir();
		}
		return folder;
	}
	
	public static File createImageSubDir(Context mContext,File dirPath){
		File imageDir = new File(dirPath, "Image");
		boolean success = true;
		if (!imageDir.exists()){
			success =imageDir.mkdirs();
		}
		return imageDir;
	}
	private static String JPEG_FILE_SUFFIX = ".jpg";



}
