package com.clubscaddy.AsyTask;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.clubscaddy.Bean.NewsBean;
import com.clubscaddy.custumview.FileDir;
import com.clubscaddy.fragment.NewsFeedActivity.CustomerDownloadImageListener;
import com.clubscaddy.fragment.NewsFeedActivity.CustomerShoppingTimeAttachment;
import com.clubscaddy.fragment.NewsFeedActivity.PlayAudioListener;
import com.clubscaddy.fragment.NewsFeedActivity.ShopAdptDownloadImageListener;
import com.clubscaddy.fragment.NewsFeedActivity.ShopkeeperShoppingTimeAttachment;
import com.clubscaddy.fragment.NewsFeedActivity.VideoPlayerListener;

public class DownloadImageAsyTask extends AsyncTask<Void, Void, Void> {

	String Tag = getClass().getName();
	Context mContext;
	String murl;
	String Filename;
	ProgressBar popup_progressbar;
	ShopkeeperShoppingTimeAttachment shoppingtimeattachment;
	CustomerShoppingTimeAttachment customerShoppingTimeAttachment;
	ShopAdptDownloadImageListener shopAdptdownloadImageListener;
	CustomerDownloadImageListener custAdptdownloadImageListener;
	PlayAudioListener playAudioListener;
	VideoPlayerListener videoPlayListener;
	String abousaltPath = null;
	NewsBean newsBean;
	File mfile;
	int beanIndex;

	public DownloadImageAsyTask(Context mContext, String murl, String Filename,
			ShopkeeperShoppingTimeAttachment shoppingtimeattachment,
			ProgressBar shop_chat_progressbar) {
		this.mContext = mContext;
		this.murl = murl;
		this.Filename = Filename;
		this.shoppingtimeattachment = shoppingtimeattachment;
		popup_progressbar = shop_chat_progressbar;
	}

	public DownloadImageAsyTask(Context mContext, String murl, String Filename,
			CustomerShoppingTimeAttachment customerShoppingTimeAttachment,
			ProgressBar cust_chat_progressbar) {
		this.mContext = mContext;
		this.murl = murl;
		this.Filename = Filename;
		this.customerShoppingTimeAttachment = customerShoppingTimeAttachment;
		popup_progressbar = cust_chat_progressbar;
	}

	public DownloadImageAsyTask(Context mContext, String murl, String Filename,
			ShopAdptDownloadImageListener shopAdptdownloadImageListener,
			ProgressBar popup_progressbar) {
		this.mContext = mContext;
		this.murl = murl;
		this.Filename = Filename;
		this.shopAdptdownloadImageListener = shopAdptdownloadImageListener;
		this.popup_progressbar = popup_progressbar;
	}

	public DownloadImageAsyTask(Context mContext, String murl, String Filename,
			CustomerDownloadImageListener custAdptdownloadImageListener,
			ProgressBar popup_progressbar) {
		this.mContext = mContext;
		this.murl = murl;
		this.Filename = Filename;
		this.custAdptdownloadImageListener = custAdptdownloadImageListener;
		this.popup_progressbar = popup_progressbar;
	}

	
	public DownloadImageAsyTask(Context mContext, NewsBean newsBean, String Filename,
			VideoPlayerListener videoPlayListener, ProgressBar popup_progressbar,int index) {
		this.mContext = mContext;
		this.murl = newsBean.getVedioUrl();
		this.Filename = Filename;
		this.videoPlayListener = videoPlayListener;
		this.popup_progressbar = popup_progressbar;
		this.newsBean = newsBean;
		this.beanIndex = index;
	}

	public DownloadImageAsyTask(Context mContext, String murl, String Filename,
			PlayAudioListener playAudioListener, ProgressBar popup_progressbar) {
		this.mContext = mContext;
		this.murl = murl;
		this.Filename = Filename;
		this.playAudioListener = playAudioListener;
		this.popup_progressbar = popup_progressbar;
	}

	@Override
	protected Void doInBackground(Void... params) {
		DownloadImage(murl, Filename);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);

		if (shoppingtimeattachment != null) {
			if (popup_progressbar != null) {
				popup_progressbar.setVisibility(View.GONE);
			}
			if (abousaltPath != null) {
				shoppingtimeattachment.onSucess(abousaltPath);
			}
		}

		if (customerShoppingTimeAttachment != null) {
			if (popup_progressbar != null) {
				popup_progressbar.setVisibility(View.GONE);
			}

			if (abousaltPath != null) {
				customerShoppingTimeAttachment.onSucess(abousaltPath);
			}
		}

		if (shopAdptdownloadImageListener != null) {
			if (popup_progressbar != null) {
				popup_progressbar.setVisibility(View.GONE);
			}
			if (abousaltPath != null) {
				shopAdptdownloadImageListener.onSucess();
			}
		}

		if (custAdptdownloadImageListener != null) {
			if (popup_progressbar != null) {
				popup_progressbar.setVisibility(View.GONE);
			}
			if (abousaltPath != null) {
				custAdptdownloadImageListener.onSucess();
			}
		}

		if (videoPlayListener != null) {
			if (popup_progressbar != null) {
				popup_progressbar.setVisibility(View.GONE);
			}
			if (abousaltPath != null) {
				videoPlayListener.onSucess(abousaltPath,newsBean);
			}
		}

		if (playAudioListener != null) {
			if (abousaltPath != null) {
				playAudioListener.onSucess(abousaltPath);
			}

			if (popup_progressbar != null) {
				popup_progressbar.setVisibility(View.GONE);
			}
		}
	}

	private void DownloadImage(String mUrl, String mFilename) {
		InputStream in = null;
		try {
			mfile = createMemory(mFilename);
			if (mfile.exists()) {
				abousaltPath = mfile.getAbsolutePath();
			} else {
				try {
					in = OpenHttpConnection(mUrl);
					byte[] buffer = new byte[1024];
					int bufferLength = 0;
					FileOutputStream fileOutput = new FileOutputStream(mfile);
					while ((bufferLength = in.read(buffer)) > 0) {
						fileOutput.write(buffer, 0, bufferLength);
					}
					fileOutput.close();
					in.close();

					abousaltPath = mfile.getAbsolutePath();

				} catch (IOException e) {
					Log.e(Tag + "IOException", e.toString());
				} catch (NullPointerException e) {
					Log.e(Tag + "Nullpointer Exception", e.toString());
				} catch (ArrayIndexOutOfBoundsException e) {

				} catch (Exception e) {
					Log.e(Tag + "Exception", e.toString());
				}
			}
		} catch (NullPointerException e) {
			Log.e(Tag, e.toString());
		} catch (Exception e) {
			Log.e(Tag, e.toString());
		}

	}

	private InputStream OpenHttpConnection(String urlString) throws IOException {
		InputStream in = null;
		int response = -1;

		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();

		if (!(conn instanceof HttpURLConnection))
			throw new IOException("Not an HTTP connection");
		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
			}
		} catch (Exception ex) {
			throw new IOException("Error connecting");
		}
		return in;
	}

	private File createMemory(String mFilename) {
		String[] extensionOfFile = mFilename.split("\\.");
		String extensionType = extensionOfFile[1];

		File dirPath = FileDir.createSDCardDir(mContext);
		if (extensionType.equalsIgnoreCase("mp3")) {
			File audioDir = FileDir.createAudioSubDir(mContext, dirPath);
			mfile = new File(audioDir, Filename);
		}

		if (extensionType.equalsIgnoreCase("jpg")
				|| extensionType.equalsIgnoreCase("png")
				|| extensionType.equalsIgnoreCase("jpeg")) {
			File imageDir = FileDir.createImageSubDir(mContext, dirPath);
			mfile = new File(imageDir, mFilename);
		}

		if (extensionType.equalsIgnoreCase("3gp")
				|| extensionType.equalsIgnoreCase("mov")
				|| extensionType.equalsIgnoreCase("avi")
				|| extensionType.equalsIgnoreCase("mp4")) {
			File videoDir = FileDir.createVidoeSubDir(mContext, dirPath);
			mfile = new File(videoDir, mFilename);
		}
		return mfile;
	}
}
