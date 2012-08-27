package padcms.magazine.resource;

import padcms.magazine.factory.ResourceResolutionHelper;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.widget.ImageView;

public class MenuImageViewController extends ImageViewController {

	public MenuImageViewController(Context mContext,
			String imageResoureRelativePath) {
		super(mContext, imageResoureRelativePath);

	}

	public MenuImageViewController(Context mContext,
			String imageResoureRelativePath, String dimentionString) {
		super(mContext, imageResoureRelativePath, dimentionString);

	}

	@Override
	protected void onDrow(final Bitmap bitmap) {
		if (getBaseElemetView().getShowingView() != null) {

			((Activity) mContext).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ImageView targetImagetView = (ImageView) getBaseElemetView()
							.getShowingView();
					
					if (bitmap != null && !bitmap.isRecycled()) {

						if (targetImagetView.getLayoutParams().width == -2
								|| targetImagetView.getLayoutParams().height == -2) {
							ResourceResolutionHelper bitmapResolution = getBaseElemetView()
									.getResolutionForController(bitmap);
							//
							targetImagetView.getLayoutParams().width = bitmapResolution.width;
							targetImagetView.getLayoutParams().height = bitmapResolution.height;
						}
						targetImagetView.setImageBitmap(bitmap);

						releaseInctiveResources();

					} else {
						targetImagetView.setImageBitmap(null);

						releaseInctiveResources();
					}
				}
			});

		}

	}

	public ResourceResolutionHelper getResourceResolution() {
		ResourceResolutionHelper resolutionHelper = null;
		if (isExistResourceLocal()) {
			Options opt = new Options();
			opt.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(pathToResourse, opt);

			resolutionHelper = ResourceResolutionHelper.getBitmapResolutionVerticalMenuItem(
					mContext, opt.outWidth, opt.outHeight);
		}
		return resolutionHelper;
	}
	//
	// private Bitmap bitmapOriginal;
	// private Bitmap bitmapScaled;
	// private boolean isBitmapOriginalExist;
	// private boolean isBitmapScaledExist;
	// private int sizeScale = -1;
	//
	// public void drowElement() {
	// if (isBitmapOriginalExist) {
	// if (mDrowBitmapListner != null) {
	// mDrowBitmapListner.onDrow(bitmapOriginal);
	// }
	//
	// } else if (isBitmapScaledExist) {
	// if (mDrowBitmapListner != null) {
	// mDrowBitmapListner.onDrow(bitmapScaled);
	// }
	// } else if (mDrowBitmapListner != null) {
	// mDrowBitmapListner.onDrow(null);
	// }
	// }
	// public abstract void drowElement(Bitmap bitmap);
	//
	// public MenuElementResourceController(Context mContext,
	// String imageResourseRelativePath) {
	// this.mContext = mContext;
	// if (imageResourseRelativePath != null
	// && imageResourseRelativePath.length() > 0) {
	// this.urlToImageResource = NetHepler
	// .getUrlToResourceByDemetion(imageResourseRelativePath);
	//
	// this.pathToImageResourse = new File(IssueViewFactory
	// .getIssueViewFactoryIstance(mContext)
	// .getPathToResourceFolder(),
	// String.valueOf(urlToImageResource.hashCode()))
	// .getAbsolutePath();
	// }
	//
	// }
	//
	// public MenuElementResourceController(Context mContext,
	// String imageResourseRelativePath, String resolution) {
	// this.mContext = mContext;
	// if (imageResourseRelativePath != null
	// && imageResourseRelativePath.length() > 0) {
	// this.urlToImageResource = NetHepler.getUrlToResourceByDemetion(
	// imageResourseRelativePath, resolution);
	//
	// this.pathToImageResourse = new File(IssueViewFactory
	// .getIssueViewFactoryIstance(mContext)
	// .getPathToResourceFolder(),
	// String.valueOf(urlToImageResource.hashCode()))
	// .getAbsolutePath();
	//
	// }
	// }
	//
	// public void initResourceController(String imageResourseRelativePath) {
	// if (imageResourseRelativePath != null
	// && imageResourseRelativePath.length() > 0) {
	//
	// this.urlToImageResource = NetHepler
	// .getUrlToResourceByDemetion(imageResourseRelativePath);
	//
	// this.pathToImageResourse = new File(IssueViewFactory
	// .getIssueViewFactoryIstance(mContext)
	// .getPathToResourceFolder(),
	// String.valueOf(urlToImageResource.hashCode()))
	// .getAbsolutePath();
	//
	// } else {
	//
	// this.urlToImageResource = this.pathToImageResourse = "";
	//
	// }
	// isBitmapOriginalExist = false;
	// isBitmapScaledExist = false;
	// // releaseInctiveResources();
	//
	// }
	//
	// public int getSizeScale() {
	// return sizeScale;
	// }
	//
	// public void setSizeScale(int sizeScale) {
	// this.sizeScale = sizeScale;
	// }
	//
	// @Override
	// public void downloadResource() {
	//
	// // bitmapOriginal = ResourceFactory.getBitmapFromUrl(urlToImageResource,
	// // pathToImageResourse);
	// // if (bitmapOriginal != null) {
	// // isBitmapOriginalExist = true;
	// // showResource();
	// // }
	//
	// if (urlToImageResource.trim().length() > 0) {
	// Log.e("download url1:" + this.getClass().getName(), "url"
	// + urlToImageResource);
	// Bundle bundel = new Bundle();
	// bundel.putInt("message", ProgressDownloadElementView.SHOW_PROGRESS);
	// updateProgress(bundel);
	// File file = new File(pathToImageResourse);
	// InputStream is;
	// int readBytesNumberAtOnes = 0;
	//
	// URL url = null;
	// try {
	// url = new URL(urlToImageResource);
	// } catch (MalformedURLException e2) {
	// e2.printStackTrace();
	// return;
	// }
	//
	// HttpURLConnection connection = null;
	// try {
	// connection = (HttpURLConnection) url.openConnection();
	// connection.setConnectTimeout(3000);
	// } catch (IOException e2) {
	// e2.printStackTrace();
	// return;
	// }
	//
	// try {
	// connection.connect();
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// return;
	// }
	//
	// int contentLength = connection.getContentLength();
	// if (ApplicationManager.isEnoughMemory(contentLength)) {
	// bundel = new Bundle();
	// bundel.putInt("message",
	// ProgressDownloadElementView.SET_PROGRESS_MAX_LENGTH);
	// bundel.putInt("value", contentLength);
	//
	// updateProgress(bundel);
	//
	// InputStream inputStream = null;
	// OutputStream outputStream = null;
	//
	// try {
	// inputStream = connection.getInputStream();
	// outputStream = new FileOutputStream(file);
	// } catch (FileNotFoundException e1) {
	// e1.printStackTrace();
	// return;
	// } catch (IOException e) {
	// e.printStackTrace();
	// return;
	// }
	//
	// byte[] buf = new byte[1024];
	// int currentTotalNumberOfReadBytes = 0;
	//
	// try {
	// while ((readBytesNumberAtOnes = inputStream.read(buf)) != -1) {
	// currentTotalNumberOfReadBytes += readBytesNumberAtOnes;
	//
	// outputStream.write(buf, 0, readBytesNumberAtOnes);
	//
	// bundel = new Bundle();
	// bundel.putInt("message",
	// ProgressDownloadElementView.SET_PROGRESS);
	//
	// bundel.putInt("value", currentTotalNumberOfReadBytes);
	// updateProgress(bundel);
	//
	// if (contentLength == currentTotalNumberOfReadBytes) {
	// break;
	// }
	// }
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	//
	// try {
	// inputStream.close();
	// outputStream.close();
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	//
	// }
	// bundel = new Bundle();
	// bundel.putInt("message",
	// ProgressDownloadElementView.HIDE_PROGRESS);
	// updateProgress(bundel);
	//
	// showResource();
	// } else {
	// Log.e("download url2:" + this.getClass().getName(), "url"
	// + urlToImageResource);
	//
	// if (ResourceFactory.isEnoughMemory) {
	// ResourceFactory.isEnoughMemory = false;
	//
	// makeAsynckMessageShow(R.string.error_no_space);
	//
	// }
	// }
	// }
	//
	// }
	//
	// private void makeAsynckMessageShow(final String message) {
	// ((Activity) mContext).runOnUiThread(new Runnable() {
	//
	// @Override
	// public void run() {
	// DialogHelper.ShowErrorWindow(mContext, message);
	// // Toast.makeText(
	// // mContext,
	// // mContext.getString(R.string.download_impossible_no_space),
	// // 1000).show();
	// }
	// });
	// }
	//
	// private void makeAsynckMessageShow(int message) {
	// makeAsynckMessageShow(mContext.getString(message));
	// }
	//
	// @Override
	// public State getState() {
	// return state;
	// }
	//
	// public void setState(State state) {
	//
	// if (this.state != state) {
	// this.state = state;
	// switch (state) {
	// case ACTIVE:
	// // if (isBitmapOriginalExist) {
	// // drowElement(bitmapOriginal, false);
	// // isBitmapScaledExist = false;
	// // }else
	// // if(!isBitmapScaledExist)
	// // drowElement(null, true);
	// // //releaseInctiveResources();
	// break;
	// case DISACTIVE:
	// if (isBitmapScaledExist) {
	// isBitmapOriginalExist = false;
	// makeHandleDrow();
	//
	// } else if (isBitmapOriginalExist) {
	// bitmapScaled = ResourceFactory
	// .decodeScaledBitmap(bitmapOriginal);
	//
	// isBitmapScaledExist = true;
	// isBitmapOriginalExist = false;
	// // bitmapScaled = ResourceFactory
	// // .decodeScaledBitmap(bitmapOriginal);
	// //
	// // isBitmapScaledExist = true;
	// // isBitmapOriginalExist = false;
	//
	// makeHandleDrow();
	//
	// } else {
	// makeHandleDrow();
	// }
	// break;
	// case RELEASE:
	// isBitmapOriginalExist = false;
	// isBitmapScaledExist = false;
	// releaseInctiveResources();
	// makeHandleDrow();
	// break;
	// default:
	// break;
	// }
	// }
	//
	// }
	//
	// @Override
	// public void showResource() {
	// switch (state) {
	// case ACTIVE:
	// isBitmapScaledExist = false;
	// if (isBitmapOriginalExist) {
	// // drowElement(bitmapOriginal, false);
	// makeHandleDrow();
	// } else {
	//
	// try {
	// bitmapOriginal = ResourceFactory
	// .decodeBitmap(pathToImageResourse);
	// } catch (Exception e) {
	//
	// IssueViewFactory.getIssueViewFactoryIstance(mContext)
	// .getVerticalPageSwitcherController()
	// .releaseInactive();
	//
	// makeAsynckMessageShow("ERROR: " + e.getMessage());
	//
	// }
	// if (bitmapOriginal != null) {
	// isBitmapOriginalExist = true;
	//
	// }
	// makeHandleDrow();
	// // else if (state == State.ACTIVE) {
	// // isProcessing = false;
	// // ResourceFactory.processResourceController(this);
	// // }
	// // drowElement(bitmapOriginal, false);
	// }
	// // releaseInctiveResources();
	// // releaseBitmap(bitmapScaled);
	//
	// break;
	// case DISACTIVE:
	//
	// if (isBitmapScaledExist) {
	// isBitmapOriginalExist = false;
	// makeHandleDrow();
	// // drowElement(bitmapScaled, true);
	//
	// } else if (isBitmapOriginalExist) {
	// bitmapScaled = ResourceFactory
	// .decodeScaledBitmap(bitmapOriginal);
	//
	// isBitmapScaledExist = true;
	// isBitmapOriginalExist = false;
	// makeHandleDrow();
	// // drowElement(bitmapScaled, true);
	// } else {
	//
	// bitmapScaled = ResourceFactory
	// .decodeScaledBitmap(pathToImageResourse);
	// isBitmapScaledExist = true;
	// isBitmapOriginalExist = false;
	// makeHandleDrow();
	// // drowElement(bitmapScaled, true);
	//
	// }
	// // releaseInctiveResources();
	// // releaseBitmap(bitmapOriginal);
	//
	// break;
	//
	// case RELEASE:
	// // releaseBitmap(bitmapScaled);
	// // releaseBitmap(bitmapOriginal);
	// isBitmapOriginalExist = false;
	// isBitmapScaledExist = false;
	// releaseInctiveResources();
	// makeHandleDrow();
	// // drowElement(null, true);
	// break;
	//
	// default:
	// break;
	// }
	// }
	//
	// public void releaseResources() {
	// isBitmapOriginalExist = false;
	// isBitmapScaledExist = false;
	// releaseInctiveResources();
	// }
	//
	// @Override
	// public void updateResourceState() {
	// if (bitmapOriginal != null && !bitmapOriginal.isRecycled())
	// isBitmapOriginalExist = true;
	// else
	// isBitmapOriginalExist = false;
	//
	// if (bitmapScaled != null && !bitmapScaled.isRecycled())
	// isBitmapScaledExist = true;
	// else
	// isBitmapScaledExist = false;
	// }
	//
	// @Override
	// public void releaseInctiveResources() {
	// if (!isBitmapOriginalExist) {
	// releaseBitmap(bitmapOriginal);
	// }
	// if (!isBitmapScaledExist) {
	// releaseBitmap(bitmapScaled);
	// }
	//
	// }
	//
	// private void releaseBitmap(Bitmap bitmap) {
	// if (bitmap != null && !bitmap.isRecycled()) {
	// bitmap.recycle();
	// bitmap = null;
	// }
	// }
	//
	// public void makeHandleDrow() {
	// // setBitmapsStates();
	// drowElement();
	// }
	//
	// public void setBitmapsStates() {
	// if (isBitmapOriginalExist)
	// if (bitmapOriginal != null && !bitmapOriginal.isRecycled()) {
	// isBitmapOriginalExist = true;
	// } else
	// isBitmapOriginalExist = false;
	//
	// if (isBitmapScaledExist)
	// if (bitmapScaled != null && !bitmapScaled.isRecycled()) {
	// isBitmapScaledExist = true;
	// } else
	// isBitmapScaledExist = false;
	//
	// }
	//
	// public static Handler handlerUpdateView = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// MenuElementResourceController resourceController =
	// (MenuElementResourceController) msg.obj;
	// resourceController.drowElement();
	//
	// };
	// };

}
