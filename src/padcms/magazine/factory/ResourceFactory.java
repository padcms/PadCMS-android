package padcms.magazine.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import padcms.magazine.menu.elementView.VerticalStripeMenuElementView;
import padcms.magazine.menu.elementView.VerticalSummaryMenuElementView;
import padcms.magazine.page.State;
import padcms.magazine.resource.MenuImageViewController;
import padcms.magazine.resource.ResourceController;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ResourceFactory {
	public static ArrayList<ResourceController> downloadCollection = new ArrayList<ResourceController>();
	public static ArrayList<ResourceController> downloadMenuCollection = new ArrayList<ResourceController>();
	public static ArrayList<ResourceController> downloadBGCollection = new ArrayList<ResourceController>();
	public static ArrayList<ResourceController> prepereElementCollection = new ArrayList<ResourceController>();
	public static ArrayList<ResourceController> prepereMenuElementCollection = new ArrayList<ResourceController>();
	public static Thread threadPrepareResource;
	public static Thread threadPrepareMenuResource;
	public static Thread threadDownloadResource;
	public static Thread threadDownloadMenuResource;
	public static boolean isEnoughMemory = true;

	public static void processResourceController(
			ResourceController resourceController) {
		if (!resourceController.getIsProcessing()) {

			if (resourceController.isExistResourceLocal()) {

				if (resourceController.getOnUpdateProgress() != null) {
					resourceController.getOnUpdateProgress().hideProgress();
				}

				if (resourceController.getState() != State.DOWNLOAD)
					if (resourceController.getState() == State.EXTRA_ACTIVE) {

						resourceController.showResource();

					} else {
						if (resourceController instanceof MenuImageViewController) {

							insertInPrepareController(resourceController,
									prepereMenuElementCollection);

						} else {

							insertInPrepareController(resourceController,
									prepereElementCollection);

						}
					}
			} else {
				if (isEnoughMemory)
					if (resourceController instanceof MenuImageViewController) {

						insertInDownloadCollection(resourceController,
								downloadMenuCollection, downloadMenuCollection);
					} else {

						insertInDownloadCollection(resourceController,
								downloadCollection, downloadBGCollection);

					}

			}
		} else if (resourceController.getState() == State.RELEASE) {
			if (resourceController.isExistResourceLocal()) {
				prepereElementCollection.remove(resourceController);
			} else {
				downloadCollection.remove(resourceController);
			}
		}
	}

	private static void insertInDownloadCollection(
			ResourceController resourceController,
			ArrayList<ResourceController> downloadElementCollection,
			ArrayList<ResourceController> downloadBgElementCollection) {

		if (resourceController.getState() != State.RELEASE) {
			if (resourceController.getState() == State.DOWNLOAD) {
				synchronized (downloadBgElementCollection) {
					if (downloadBgElementCollection
							.contains(resourceController)) {

						downloadBgElementCollection.remove(resourceController);
						int insurtposition = 0;

						if (downloadBgElementCollection.size() > 0) {
							insurtposition = downloadBgElementCollection.size() - 1;

						}

						downloadBgElementCollection.add(insurtposition,
								resourceController);
					} else {

						downloadBgElementCollection.add(resourceController);

					}
				}
				runDownloadResource(resourceController);
			} else {
				synchronized (downloadElementCollection) {
					if (!downloadElementCollection.contains(resourceController)) {
						if (resourceController.getOnUpdateProgress() != null) {
							resourceController.getOnUpdateProgress()
									.showProgress();
						}

						int insurtposition = 0;
						// if (resourceController.getState() == State.ACTIVE
						// && downloadElementCollection.size() > 0) {
						// insurtposition = downloadElementCollection.size() -
						// 1;
						//
						// }
						Log.i("downloadElementCollection:", "pos:"
								+ insurtposition);
						downloadElementCollection.add(resourceController);

						runDownloadResource(resourceController);
					} else if (resourceController.getState() == State.ACTIVE) {
						downloadElementCollection.remove(resourceController);
						// int insurtposition = 0;
						// if (resourceController.getState() == State.ACTIVE
						// && downloadElementCollection.size() > 0) {
						// insurtposition = downloadElementCollection.size() -
						// 1;
						//
						// }
						downloadElementCollection.add(resourceController);
					}
				}

			}
		}

	}

	private static void insertInPrepareController(
			ResourceController resourceController,
			ArrayList<ResourceController> prepereElementCollection) {

		synchronized (prepereElementCollection) {

			// if (resourceController.getmUpdateProgress() != null) {
			// resourceController.getmUpdateProgress().hideProgress();
			// }
			if (!prepereElementCollection.contains(resourceController)) {

				prepereElementCollection.add(0, resourceController);

				runPrepareResource(resourceController);

			} else if (resourceController.getState() == State.ACTIVE) {
				prepereElementCollection.remove(resourceController);
				if (prepereElementCollection.size() > 0)
					prepereElementCollection.add(
							prepereElementCollection.size() - 1,
							resourceController);
				else
					prepereElementCollection.add(0, resourceController);
			}
		}
	}

	public static void refuseDownloadBG() {
		downloadBGCollection.clear();
	}

	public static void destroy() {
		downloadCollection.clear();
		downloadMenuCollection.clear();
		// downloadBGCollection.clear();
		prepereElementCollection.clear();
		prepereMenuElementCollection.clear();

		// if (threadDownloadResource != null)
		// threadDownloadResource.interrupt();
		if (threadDownloadMenuResource != null)
			threadDownloadMenuResource.interrupt();

		threadDownloadResource = null;
		threadDownloadMenuResource = null;

		if (threadPrepareResource != null)
			threadPrepareResource.interrupt();
		if (threadPrepareMenuResource != null)
			threadPrepareMenuResource.interrupt();
		threadPrepareMenuResource = null;
		threadPrepareResource = null;
		isEnoughMemory = true;
	}

	public static void runDownloadResource(ResourceController resourceController) {
		if (resourceController instanceof MenuImageViewController) {

			if (threadDownloadMenuResource == null
					|| threadDownloadMenuResource.getState() == Thread.State.TERMINATED) {
				threadDownloadMenuResource = new Thread(
						runnableMenuDownloadResource,
						"thread_DownloadMenuResource");
				threadDownloadMenuResource.start();
			}

		} else {

			if (threadDownloadResource == null
					|| threadDownloadResource.getState() == Thread.State.TERMINATED) {

				threadDownloadResource = new Thread(runnableDownloadResource,
						"thread_DownloadResource");
				threadDownloadResource.start();
			}
		}
	}

	public static void runPrepareResource(ResourceController resourceController) {
		if (resourceController instanceof MenuImageViewController) {
			if (threadPrepareMenuResource == null
					|| threadPrepareMenuResource.getState() == Thread.State.TERMINATED) {
				threadPrepareMenuResource = new Thread(
						runnablePrepareMenuResource,
						"thread_Prepare_MenuResource");
				threadPrepareMenuResource.start();

			}
		} else {
			if (threadPrepareResource == null
					|| threadPrepareResource.getState() == Thread.State.TERMINATED) {
				threadPrepareResource = new Thread(runnablePrepareResource,
						"thread_PrepareResource");
				threadPrepareResource.start();

			}
		}
	}

	public static Runnable runnableDownloadResource = new Runnable() {
		@Override
		public synchronized void run() {

			while (true) {
				if (downloadCollection.size() > 0) {
					ResourceController resourceController = downloadCollection
							.get(0);
					resourceController.setIsProcessing(true);

					resourceController.installResource();

					resourceController.setIsProcessing(false);
					downloadCollection.remove(resourceController);
				} else if (downloadBGCollection.size() > 0) {
					ResourceController resourceController = downloadBGCollection
							.get(downloadBGCollection.size() - 1);
					resourceController.setIsProcessing(true);
					if (!resourceController.isExistResourceLocal())
						resourceController.installResource();

					resourceController.setIsProcessing(false);
					downloadBGCollection.remove(resourceController);
				} else
					return;

			}

		}
	};

	public static boolean isAllMenuSummeryDownloaded() {
		for (ResourceController resourceController : downloadMenuCollection) {
			if (resourceController.getBaseElemetView() instanceof VerticalSummaryMenuElementView) {
				return false;
			}
		}
		return true;

	}

	public static boolean isAllMenuStripeDownloaded() {

		for (ResourceController resourceController : downloadMenuCollection) {
			if (resourceController.getBaseElemetView() instanceof VerticalStripeMenuElementView) {
				return false;
			}
		}

		return true;

	}

	public static Runnable runnableMenuDownloadResource = new Runnable() {
		@Override
		public synchronized void run() {

			while (true) {
				if (downloadMenuCollection.size() > 0) {

					ResourceController resourceController = downloadMenuCollection
							.get(downloadMenuCollection.size() - 1);
					resourceController.setIsProcessing(true);

					resourceController.installResource();

					resourceController.setIsProcessing(false);
					downloadMenuCollection.remove(resourceController);

				} else
					return;

			}

		}
	};

	public static Runnable runnablePrepareResource = new Runnable() {
		@Override
		public synchronized void run() {
			while (prepereElementCollection.size() > 0) {

				ResourceController resourceController = prepereElementCollection
						.get(prepereElementCollection.size() - 1);
				try {
					resourceController.setIsProcessing(true);

					resourceController.showResource();

					resourceController.setIsProcessing(false);
				} catch (Exception e) {
					e.getStackTrace();
					Log.i("Exception runnablePrepareResource ",
							"error:" + e.getMessage());
				}
				prepereElementCollection.remove(resourceController);

			}

		}
	};

	public static Runnable runnablePrepareMenuResource = new Runnable() {
		@Override
		public synchronized void run() {
			while (prepereMenuElementCollection.size() > 0) {
				ResourceController resourceController = prepereMenuElementCollection
						.get(prepereMenuElementCollection.size() - 1);
				resourceController.setIsProcessing(true);

				resourceController.showResource();

				resourceController.setIsProcessing(false);

				prepereMenuElementCollection.remove(resourceController);
			}

		}
	};

	// public static Bitmap getBitmapFromUrl(String urlToImage, String
	// saveFilePath) {
	//
	// File outputFile = new File(saveFilePath);
	// try {
	// Bitmap bitmap = null;
	// InputStream is = new URL(urlToImage).openStream();
	// OutputStream os = new FileOutputStream(outputFile);
	// // Utils.CopyStream(is, os);
	// final int buffer_size = 1024;
	// try {
	// byte[] bytes = new byte[buffer_size];
	// // int length=is.
	// for (;;) {
	// int count = is.read(bytes, 0, buffer_size);
	// if (count == -1)
	// break;
	// os.write(bytes, 0, count);
	// }
	// } catch (Exception ex) {
	// }
	// os.close();
	// bitmap = decodeBitmap(outputFile);
	// return bitmap;
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// return null;
	// }
	// }
	public static Bitmap decodeBitmap(String bitmapFilePath) throws Exception,
			OutOfMemoryError {
		// Log.i("$$$$$$$$$$$$$$$$ decodeBitmap $$$$$$$$$$$$$$",
		// bitmapFilePath);
		File bitmapFile = new File(bitmapFilePath);
		if (bitmapFile.exists()) {
			return decodeBitmap(bitmapFile);
		}
		return null;
	}

	public static Bitmap decodeBitmap(File bitmapFile) throws Exception,
			OutOfMemoryError {

		BitmapFactory.Options o2 = new BitmapFactory.Options();

		o2.inPreferredConfig = Config.ARGB_4444;
		return BitmapFactory.decodeStream(new FileInputStream(bitmapFile),
				null, o2);

	}

	public static Bitmap decodeBitmapWithoutCatch(String bitmapFilePath) {
		File bitmapFile = new File(bitmapFilePath);
		if (bitmapFile.exists()) {
			return decodeBitmapWithoutCatch(bitmapFile);
		}
		return null;
	}

	public static Bitmap decodeBitmapWithoutCatch(File bitmapFile) {

		BitmapFactory.Options o2 = new BitmapFactory.Options();
		try {
			return BitmapFactory.decodeStream(new FileInputStream(bitmapFile),
					null, o2);
		} catch (OutOfMemoryError e) {
			Log.e("Error decodeFile:", "" + e.getMessage());
			System.gc();
			return decodeScaledBitmap(bitmapFile, 2);
		} catch (Exception e) {
			Log.e("Error decodeFile:", "" + e.getMessage());
		}
		return null;

	}

	public static Bitmap decodeScaledBitmap(File bitmapFile, int sampleSize) {

		try {
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = sampleSize;
			o2.inPreferredConfig = Config.ARGB_4444;
			return BitmapFactory.decodeStream(new FileInputStream(bitmapFile),
					null, o2);

		} catch (OutOfMemoryError e) {
			if (sampleSize < 8)
				return decodeScaledBitmap(bitmapFile, ++sampleSize);
		} catch (FileNotFoundException e) {

		}
		return null;
	}

	public static Bitmap decodeScaledBitmap(Bitmap bitmap) {

		return decodeScaledBitmap(bitmap, 4);
	}

	public static Bitmap decodeScaledBitmap(Bitmap bitmap, int inSize) {
		if (bitmap != null && !bitmap.isRecycled()) {
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = inSize;
			o2.inPreferredConfig = Config.ARGB_4444;
			try {

				return Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()
						/ inSize, bitmap.getHeight() / inSize, true);
			} catch (OutOfMemoryError e) {
				if (inSize < 10) {
					return decodeScaledBitmap(bitmap, ++inSize);
				}
			}
		}
		return null;
	}

	public static Bitmap decodeScaledBitmap(String bitmapFilePath) {
		// Log.i("$$$$$$$$$$$$$$$$ decodeScaledBitmap $$$$$$$$$$$$$$",
		// bitmapFilePath);
		File bitmapFile = new File(bitmapFilePath);
		if (bitmapFile.exists()) {
			return decodeScaledBitmap(bitmapFile);
		}
		return null;
	}

	public static Bitmap decodeScaledBitmap(File bitmapFile) {
		try {

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = 4;
			o2.inPreferredConfig = Config.ARGB_4444;
			return BitmapFactory.decodeStream(new FileInputStream(bitmapFile),
					null, o2);

		} catch (OutOfMemoryError e) {
			return decodeScaledBitmap(bitmapFile, 6);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

}
