package padcms.bll;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

public class ApplicationResource {
	public int id;
	public String urlToResource;
	public View view;
	public String pathToFile;

//	public void startDownloadItem() {
//		String fileName = "" + (urlToResource).hashCode();
//		Log.i("startDownloadItem", "" + id + "-" + urlToResource);
//		File file = ApplicationFileHelper.getFileTempFolder(fileName);
//		if (!file.exists()) {
//
//			InputStream in = null;
//			try {
//				in = new URL(urlToResource).openStream();
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			ApplicationFileHelper.saveFile(in, file);
//		} else
//			pathToFile = file.getAbsolutePath();
//	}

	public static Bitmap getApplicationRecorceBitmap(String url) {
		String fileName = "" + (url).hashCode();
		return BitmapFactory.decodeFile(ApplicationFileHelper
				.getFileTempFolder(fileName).getAbsolutePath());
	}

	public static Bitmap getApplicationRecorceBitmap(String url, Options opt) {
		String fileName = "" + (url).hashCode();
		return BitmapFactory.decodeFile(ApplicationFileHelper
				.getFileTempFolder(fileName).getAbsolutePath(), opt);
	}

	public static Drawable getApplicationRecorceDrawable(String url) {
		String fileName = "" + (url).hashCode();
		return Drawable.createFromPath(ApplicationFileHelper
				.getFileTempFolder(fileName).getAbsolutePath());
	}

	public static File getApplicationRecorceFile(String url) {
		String fileName = "" + (url).hashCode();
		return ApplicationFileHelper.getFileTempFolder(fileName);
	}
}
