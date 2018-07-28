package com.clubscaddy.imageutility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

public class ImageSave extends ContentProvider
{
	 public static final Uri CONTENT_URI = Uri.parse("content://com.example.stylist/");

	 private static final HashMap<String, String> MIME_TYPES = new HashMap<String, String>();
	 File mFile;
	public static final String image ="test.jpg";
	 static {

	  MIME_TYPES.put(".jpg", "image/jpeg");

	  MIME_TYPES.put(".jpeg", "image/jpeg");

	 }

	 @Override
	 public boolean onCreate() {
		  Log.e("ContentProvider","Create .....");	
	  try {

	   mFile = new File(getContext().getFilesDir(),image);

	   if(!mFile.exists()) {

	    mFile.createNewFile();

	   }

	   getContext().getContentResolver().notifyChange(CONTENT_URI, null);

	   return (true);

	  } catch (Exception e) {

	   e.printStackTrace();

	   return false;

	  }

	 }

	 @Override
	 public String getType(Uri uri) {

	  String path = uri.toString();

	  for (String extension : MIME_TYPES.keySet()) {

	   if (path.endsWith(extension)) {

	    return (MIME_TYPES.get(extension));

	   }

	  }

	  return (null);

	 }
	 
	 
	 @Override
	 public ParcelFileDescriptor openFile(Uri uri, String mode)	 throws FileNotFoundException 
	 {
	  
	  if (!mFile.exists())
	  {
		  try {
			mFile.createNewFile();
			Log.e("Imgfile","Create File .....");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }  
		getContext().getContentResolver().notifyChange(CONTENT_URI, null);	
		
	  }	
	  Log.e("Openfile","Content .....");	 
	  
	  return (ParcelFileDescriptor.open(mFile,ParcelFileDescriptor.MODE_READ_WRITE));	  

	 }
	 
	 
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
