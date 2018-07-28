package com.clubscaddy;

import java.io.File;
import java.security.Permission;
import java.util.ArrayList;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.clubscaddy.Interface.MyPermissionGrrantedListner;
import com.clubscaddy.utility.CropSquareBitmaTransformation;

import com.clubscaddy.R;
import com.clubscaddy.utility.ImageThumbTranslation;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.UserPermision;

public class CustomPhotoGalleryActivity extends AppCompatActivity implements OnClickListener
{

	private GridView grdImages;
	private Button btnSelect;
	private Button cancel;
	private ImageAdapter imageAdapter;
	private String[] arrPath;
	private String[] arrPathUri;
	private boolean[] thumbnailsselection;
	private int ids[];
	private int count;
	public  Activity mContext;
	int total_previous_item  = 0;
	private ArrayList<String>permissionList ;
	
	
	/**
	 * Overrides methods
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.custom_gallery);
		mContext = this;
		grdImages = (GridView) findViewById(R.id.grdImages);
		btnSelect = (Button) findViewById(R.id.btnSelect);
		cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(this);
		btnSelect.setOnClickListener(this);
		permissionList = new ArrayList<>();
		permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
		permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

		try
		{
			total_previous_item = Integer.parseInt(getIntent().getStringExtra("item"))	;
		}
		catch(Exception e)
		{

		}


	checkPermissionGranted();

	}
	public void checkPermissionGranted()
	{
		if (UserPermision.isAllPermissionIsGranted(mContext , permissionList))
		{
			getAndSetLocalStorageImageOnList();
		}
		else
		{
			UserPermision.updatePermission(mContext, permissionList,
					new MyPermissionGrrantedListner() {
						@Override
						public void isAllPermissionGranted(boolean isPermissionGranted, int code)
						{

							checkPermissionGranted();

						}
					}, 0);
		}

	}

	public void getAndSetLocalStorageImageOnList()
	{
		try {
			final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
			final String orderBy = MediaStore.Images.Media._ID;
			@SuppressWarnings("deprecation")
			Cursor imagecursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
			int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);
			this.count = imagecursor.getCount();
			this.arrPath = new String[this.count];
			this.arrPathUri = new String[this.count];

			ids = new int[count];
			this.thumbnailsselection = new boolean[this.count];
			for (int i = 0; i < this.count; i++) {
				imagecursor.moveToPosition(i);
				ids[i] = imagecursor.getInt(image_column_index);
				int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
				arrPath[i] = imagecursor.getString(dataColumnIndex);
				File file = new File(imagecursor.getString(dataColumnIndex));
				Uri s = Uri.fromFile(file);
				// arrPath[i] = s.toString();
				arrPathUri[i] = s.toString();
			}
			// imagecursor.close();
			imageAdapter = new ImageAdapter();
			grdImages.setAdapter(imageAdapter);

		} catch (Exception e)
		{
			Toast.makeText(getApplicationContext() , e.getMessage() , Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	public void onBackPressed() {
		try {

			setResult(Activity.RESULT_CANCELED);
			super.onBackPressed();
		} catch (Exception e) {
		//	new SendErrorAsync(mContext, e);
		}
	}

	/**
	 * Class method
	 */

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.btnSelect)
		{
			final int len = thumbnailsselection.length;
			int cnt = 0;
			String selectImages = "";
			String selectImagesUri = "";
			for (int i = 0; i < len; i++) {
				if (thumbnailsselection[i]) {
					cnt++;
					selectImages = selectImages + arrPath[i] + "|";
					selectImagesUri = selectImagesUri + arrPathUri[i] + "|";
					Log.e("path", arrPath[i]);
				}
			}
			if (cnt == 0) {
				Toast.makeText(getApplicationContext(), "Please select at least one image", Toast.LENGTH_LONG).show();
			} else {

				Log.d("SelectedImages", selectImages);
				Intent i = new Intent();
				i.putExtra("data", selectImages);
				i.putExtra("uri", selectImagesUri);
				setResult(Activity.RESULT_OK, i);
				finish();
			}
		}
		if (v.getId() == R.id.cancel)
		{
			finish();
		}

	}


	public class ImageAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public ImageAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return count;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.custom_gallery_item, null);
				holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);
				holder.chkImage = (CheckBox) convertView.findViewById(R.id.chkImage);
				holder.prog = (ProgressBar) convertView.findViewById(R.id.prog);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.chkImage.setId(position);
			holder.imgThumb.setId(position);

			holder.chkImage.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					int id = cb.getId();
					if (thumbnailsselection[id]) 
					{
						cb.setChecked(false);
						thumbnailsselection[id] = false;
					} else {
						int counter = total_previous_item;
						for (int i = 0; i < thumbnailsselection.length - 1; i++) {
							if (thumbnailsselection[i])
								counter++;
						}
						if (counter >= 10) {
							cb.setChecked(false);
						//	Utill.showDialg(msg, mContext);
							showDialg("You can't upload more then 10 images",  CustomPhotoGalleryActivity.this);
							//Toast.makeText(CustomPhotoGalleryActivity.this, "No more report can be select", Toast.LENGTH_SHORT).show();
							return;
						}
						cb.setChecked(true);
						thumbnailsselection[id] = true;
					}
				}
			});
			holder.imgThumb.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					int id = holder.chkImage.getId();
					if (thumbnailsselection[id]) {
						holder.chkImage.setChecked(false);
						thumbnailsselection[id] = false;
					} else {
						int counter = total_previous_item;
						for (int i = 0; i < thumbnailsselection.length - 1; i++) {
							if (thumbnailsselection[i])
								counter++;
						}
						if (counter >= 10) {
							showDialg("You can't upload more then 10 images", CustomPhotoGalleryActivity.this);
							return;
						}
						holder.chkImage.setChecked(true);
						thumbnailsselection[id] = true;
					}
				}
			});
			holder.imgThumb.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					return false;
				}
			});
			try {

				/*
				 * opt = UniversalImageLoaderHelper.setImageOptions();
				 * image_load = ImageLoader.getInstance();
				 * image_load.init(ImageLoaderConfiguration
				 * .createDefault(mContext));
				 * image_load.displayImage(arrPathUri[position],
				 * holder.imgThumb, opt, new CustomImageLoader(null, mContext));
				 */
				//Uri uri = Uri.fromFile(new File(arrPath[position]));


				//ImageTransformation

				int scaleWidth = (int) mContext.getResources().getDimension(R.dimen.edit_profile_size);


				Glide.with(CustomPhotoGalleryActivity.this)
						.load(new File(arrPath[position])).transform(new ImageThumbTranslation(getApplicationContext(),scaleWidth)).placeholder(R.drawable.default_img).into(holder.imgThumb);


			} catch (Exception e) {
			//		new SendErrorAsync(mContext, e).execute();
			
			}
			holder.chkImage.setChecked(thumbnailsselection[position]);
			holder.id = position;
			return convertView;
		}
	}

	/**
	 * Inner class
	 * 
	 * @author tasol
	 */
	class ViewHolder {
		ImageView imgThumb;
		CheckBox chkImage;
		ProgressBar prog;
		int id;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	String filePath ;
	Bitmap photo;


	private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
		if (maxHeight > 0 && maxWidth > 0) {
			int width = image.getWidth();
			int height = image.getHeight();
			float ratioBitmap = (float) width / (float) height;
			float ratioMax = (float) maxWidth / (float) maxHeight;

			int finalWidth = maxWidth;
			int finalHeight = maxHeight;
			if (ratioMax > 1) {
				finalWidth = (int) ((float)maxHeight * ratioBitmap);
			} else {
				finalHeight = (int) ((float)maxWidth / ratioBitmap);
			}
			image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
			return image;
		} else {
			return image; 
		}
	}

	private Bitmap imageRoated(File file){
		Bitmap bitmap = null;
		int rotate;
		try {
			ExifInterface exif = new ExifInterface(file.getAbsolutePath());
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {

			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				bitmap = rotateImage(file,rotate);
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				bitmap = rotateImage(file,rotate);
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				bitmap = rotateImage(file,rotate);
				break;
			case 1:
				rotate = 0;
				bitmap = rotateImage(file,rotate); 
				break;

			case 2:
				rotate = 0; 
				bitmap = rotateImage(file,rotate);
				break;
			case 4:
				rotate = 180;
				bitmap = rotateImage(file,rotate);
				break;

			case 0:
				rotate = 0;
				bitmap = rotateImage(file,rotate);
				break;
			}
		} catch (Exception e) {
			Log.e("Exception", ""+e.toString());
		}

		return bitmap;
	}

private Bitmap rotateImage(File file, int rotate) {
		
		try {
			//FileInputStream fis = new FileInputStream(file);
			 photo = BitmapFactory.decodeFile(file.getAbsolutePath());
			Matrix matrix = new Matrix();
			matrix.postRotate(rotate); 
		photo = Bitmap.createBitmap(photo , 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);
			//ByteArrayOutputStream out = new ByteArrayOutputStream();
			//photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
			//bm = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

		} catch (OutOfMemoryError e) {
			Log.e("OutOfMemoryError", ""+e.toString());
		}

		return photo;
	}
	
	
	
	
	
	
public  void showDialg(String msg , Activity mContext)
{
	
	final	AlertDialog alertDialog = new AlertDialog.Builder(
            mContext ).create();

//Setting Dialog Title
alertDialog.setTitle(SessionManager.getClubName(getApplicationContext()));

//Setting Dialog Message
alertDialog.setMessage(msg);

//Setting Icon to Dialog


//Setting OK Button
alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
    public void onClick(DialogInterface dialog, int which) {
    // Write your code here to execute after dialog closed
    	alertDialog.dismiss();
    }
});

//Showing Alert Message
alertDialog.show();

}
	
	// ProgressBar prog = (ProgressBar) vi.findViewById(R.id.prog);
}
