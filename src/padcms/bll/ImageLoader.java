package padcms.bll;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;

import padcms.net.Utils;
import android.content.Context;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class ImageLoader {

	// the simplest in-memory cache implementation. This should be replaced with
	// something like SoftReference or BitmapOptions.inPurgeable(since 1.6)
	private HashMap<String, Bitmap> cache = new HashMap<String, Bitmap>();
	private static ImageLoader _instance;
	private File cacheDir;

	public synchronized static ImageLoader getInstance(Context context) {
		if (_instance == null)
			_instance = new ImageLoader(context);
		return _instance;
	}

	public ImageLoader(Context context) {
		// Make the background thead low priority. This way it will not affect
		// the UI performance
		photoLoaderThread = new PhotosLoader();
		photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);
		photoLoaderThread.setName("PhotosLoader");
		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = ApplicationFileHelper.getFileTempFolder();
		else
			cacheDir = context.getCacheDir();

		if (!cacheDir.exists()) {
			cacheDir.mkdir();
		}

	}

	public File getCacheDir() {
		return cacheDir;
	}

	public void setCacheDir(File cacheDir) {
		this.cacheDir = cacheDir;
	}

	public void drawImage(String url, Handler callbackToDrow, int width) {
		Bitmap cachedBitmap = null;
		if (cache.containsKey(url))
			cachedBitmap = cache.get(url);

		if (cachedBitmap != null && !cachedBitmap.isRecycled()) {
			Message msg = new Message();
			msg.obj = cachedBitmap;
			callbackToDrow.sendMessage(msg);
			// view.setBackgroundDrawable(new BitmapDrawable(
			// getReflectedBitmap(cachedBitmap)));

		} else {
			cache.remove(url);
			queuePhoto(url, callbackToDrow, width);
			// imageView.setImageDrawable(createDrawableShape());
		}
	}

	// public void drawImage(String url, Handler callbackToDrow,
	// ImageView imageView, int width) {
	// Bitmap cachedBitmap = null;
	// if (cache.containsKey(url))
	// cachedBitmap = cache.get(url);
	// if (cachedBitmap != null && !cachedBitmap.isRecycled()) {
	// Message msg = new Message();
	// Object[] objArray = new Object[2];
	// objArray[0] = cachedBitmap;
	// objArray[1] = imageView;
	// msg.obj = objArray;
	//
	// callbackToDrow.sendMessage(msg);
	// // view.setBackgroundDrawable(new BitmapDrawable(
	// // getReflectedBitmap(cachedBitmap)));
	//
	// } else {
	// cache.remove(url);
	// queuePhoto(url, callbackToDrow, imageView, width);
	// // imageView.setImageDrawable(createDrawableShape());
	// }
	// }

	public Bitmap getReflectedBitmap(Bitmap originalImage) {
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		// This will not scale but will flip on the Y axis
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		// Create a Bitmap with the flip matix applied to it.
		// We only want the bottom half of the image
		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
				height / 2, width, height / 2, matrix, false);

		// Create a new bitmap with same width but taller to fit reflection
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		// Create a new Canvas with the bitmap that's big enough for
		// the image plus gap plus reflection
		Canvas canvas = new Canvas(bitmapWithReflection);
		// Draw in the original image
		canvas.drawBitmap(originalImage, 0, 0, null);
		// Draw in the gap
		Paint deafaultPaint = new Paint();
		canvas.drawRect(0, height, width, height, deafaultPaint);
		// Draw in the reflection
		canvas.drawBitmap(reflectionImage, 0, height, null);

		// Create a shader that is a linear gradient that covers the
		// reflection
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0,
				originalImage.getHeight(), 0, bitmapWithReflection.getHeight(),
				0x70ffffff, 0x00ffffff, TileMode.CLAMP);
		// Set the paint to use this shader (linear gradient)
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight(),
				paint);
		originalImage.recycle();
		return bitmapWithReflection;
	}

	private void queuePhoto(String url, Handler callbackToDrow, int width) {
		// This ImageView may be used for other images before. So there may be
		// some old tasks in the queue. We need to discard them.
		photosQueue.Clean(url);
		PhotoToLoad p = new PhotoToLoad(url, callbackToDrow, width);

		synchronized (photosQueue.photosToLoad) {
			photosQueue.photosToLoad.push(p);
			// photosQueue.photosToLoad.notifyAll();
		}

		// start thread if it's not started yet
		if (photoLoaderThread.getState() == Thread.State.NEW)
			photoLoaderThread.start();
	}

	// private void queuePhoto(String url, Handler callbackToDrow,
	// ImageView imageView, int width) {
	// // This ImageView may be used for other images before. So there may be
	// // some old tasks in the queue. We need to discard them.
	// photosQueue.Clean(url);
	// PhotoToLoad p = null;
	// p = new PhotoToLoad(url, callbackToDrow, imageView, width);
	//
	// synchronized (photosQueue.photosToLoad) {
	// photosQueue.photosToLoad.push(p);
	// photosQueue.photosToLoad.notifyAll();
	// }
	//
	// // start thread if it's not started yet
	// if (photoLoaderThread.getState() == Thread.State.NEW)
	// photoLoaderThread.start();
	// }

	private Bitmap getBitmap(PhotoToLoad photoToLoad) {
		// I identify images by hashcode. Not a perfect solution, good for the
		// demo.
		String filename = String.valueOf(photoToLoad.url.hashCode());
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		File f = new File(cacheDir, filename);

		// from SD cache
		if (f.exists()) {
			Bitmap b = decodeFile(f, photoToLoad.width);
			if (b != null)
				return b;
		} else {
			try {
				Bitmap bitmap = null;
				InputStream is = new URL(photoToLoad.url).openStream();
				OutputStream os = new FileOutputStream(f);
				Utils.CopyStream(is, os);
				os.close();
				bitmap = decodeFile(f, photoToLoad.width);
				return bitmap;
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}
		return null;
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f, int size) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.

			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			if (size > 0) {
				while (true) {
					if (width_tmp / 2 < size || height_tmp / 2 < size)
						break;
					width_tmp /= 2;
					height_tmp /= 2;
					scale *= 2;
				}
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (OutOfMemoryError e) {
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public Handler callbackDraw;
		public int width;

		// public final ImageView imageView;

		public PhotoToLoad(String url, Handler callbackDraw, int width) {
			this.url = url;
			this.callbackDraw = callbackDraw;
			this.width = width;
			// imageView = null;
		}

		// public PhotoToLoad(String url, Handler callbackDraw,
		// ImageView imageView, int width) {
		// this.url = url;
		// // this.imageView = imageView;
		// this.callbackDraw = callbackDraw;
		// this.width = width;
		// }
	}

	PhotosQueue photosQueue = new PhotosQueue();

	public void stopThread() {
		photoLoaderThread.interrupt();
	}

	public void CleanPhotoQ() {
		cache = new HashMap<String, Bitmap>();
		if (photoLoaderThread.getState() == Thread.State.TERMINATED) {
			photoLoaderThread = new PhotosLoader();
			photoLoaderThread.start();
		}
		photosQueue.removeAll();
	}

	// stores list of photos to download
	class PhotosQueue {
		private Stack<PhotoToLoad> photosToLoad = new Stack<PhotoToLoad>();

		// removes all instances of this ImageView
		public void Clean(String url) {
			for (int j = 0; j < photosToLoad.size();) {
				if (photosToLoad.size() < j
						&& photosToLoad.get(j).url.equals(url))
					photosToLoad.remove(j);
				else
					++j;
			}
		}

		public void removeAll() {
			photosToLoad.removeAllElements();
		}
	}

	class PhotosLoader extends Thread {
		public void run() {
			try {
				while (true) {
					// thread waits until there are any images to load in the
					// queue
					if (photosQueue.photosToLoad.size() == 0)
						synchronized (photosQueue.photosToLoad) {
							photosQueue.photosToLoad.wait();
						}
					if (photosQueue.photosToLoad.size() != 0) {
						PhotoToLoad photoToLoad;
						synchronized (photosQueue.photosToLoad) {
							photoToLoad = photosQueue.photosToLoad.pop();
						}
						final Bitmap bmp = getBitmap(photoToLoad);

						cache.put(photoToLoad.url, bmp);
						// Object tag = photoToLoad.imageView.getTag();
						if (bmp != null) {
							if (photoToLoad.callbackDraw != null) {
								// bmp = getReflectedBitmap(bmp);
								// // if (photoToLoad.imageView != null) {
								// Message msg = new Message();
								// Object[] objArray = new Object[2];
								// objArray[0] = bmp;
								// // objArray[1] = photoToLoad.imageView;
								// msg.obj = objArray;
								// photoToLoad.callbackDraw.sendMessage(msg);
								//
								// } else {
								Message msg = new Message();
								msg.obj = bmp;
								photoToLoad.callbackDraw.sendMessage(msg);
								// }
							}
						}
					}
					if (Thread.interrupted())
						break;
				}
			} catch (InterruptedException e) {
				// allow thread to exit
			}
		}
	}

	PhotosLoader photoLoaderThread = new PhotosLoader();

	// Used to display bitmap in the UI thread
	// class BitmapDisplayer implements Runnable {
	// Bitmap bitmap;
	// View imageView;
	//
	// public BitmapDisplayer(Bitmap b, View i) {
	// bitmap = b;
	// imageView = i;
	// }
	//
	// public void run() {
	// if (bitmap != null)
	// imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
	// // else
	// // imageView.setImageDrawable(createDrawableShape());
	// }
	// }

	public void clearCache() {
		// clear memory cache
		cache.clear();

		// clear SD cache
		File[] files = cacheDir.listFiles();
		for (File f : files)
			f.delete();
	}

	public void destroy() {

		CleanPhotoQ();
		cache.clear();

		_instance = null;
	}
}
