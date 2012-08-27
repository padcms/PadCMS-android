package padcms.magazine.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import padcms.application11.R;
import padcms.bll.ApplicationManager;
import padcms.bll.DialogHelper;
import padcms.magazine.factory.IssueViewFactory;
import padcms.magazine.factory.ResourceFactory;
import padcms.magazine.page.State;
import padcms.magazine.page.elementview.BaseElementView;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

public class ResourceController {
	//
	// public static interface onDrowBitmapListner {
	//
	// void onDrow(Bitmap drowBitmap);
	//
	// void onInvalidateView();
	// }
	//
	// public static interface onDrowPartedBitmapListner {
	//
	// void onDrow(List<Bitmap> drowBitmap);
	//
	// }

	public static interface onUpdateProgress {

		void showProgress();

		void startProgress(int maxValue);

		void setProgress(int valueProgress);

		void hideProgress();

	}

	protected boolean isParted;
	protected State state;
	protected boolean isProcessing;
	protected Context mContext;
	protected String pathToResourse = "";
	protected String urlToResource = "";
	protected BaseElementView baseElemetView;
	// protected onDrowBitmapListner mOnDrowBitmapListner;
	// protected onDrowPartedBitmapListner mOnDrowPartedBitmapListner;
	protected onUpdateProgress mUpdateProgress;

	public ResourceController(Context mContext) {
		this.mContext = mContext;
	}
	

	// public onDrowBitmapListner getOnDrowBitmapListner() {
	// return mOnDrowBitmapListner;
	// }
	//
	// public void setOnDrowBitmapListner(onDrowBitmapListner
	// mDrowBitmapListner) {
	// this.mOnDrowBitmapListner = mDrowBitmapListner;
	// }
	//
	// public onDrowPartedBitmapListner getOnDrowPartedBitmapListner() {
	// return mOnDrowPartedBitmapListner;
	// }
	//
	// public void setOnDrowPartedBitmapListner(
	// onDrowPartedBitmapListner mDrowPartedBitmapListner) {
	// this.mOnDrowPartedBitmapListner = mDrowPartedBitmapListner;
	// }

	public Context getmContext() {
		return mContext;
	}


	public onUpdateProgress getOnUpdateProgress() {
		return mUpdateProgress;
	}

	public BaseElementView getBaseElemetView() {
		return baseElemetView;
	}

	public void setBaseElemetView(BaseElementView baseElemetView) {
		this.baseElemetView = baseElemetView;
	}

	public void setOnUpdateProgress(onUpdateProgress mUpdateProgress) {
		this.mUpdateProgress = mUpdateProgress;
	}

	public boolean isExistResourceLocal() {
		if (new File(pathToResourse).exists())
			return true;
		else {
			if (mUpdateProgress != null) {
				mUpdateProgress.showProgress();
			}
		}
		return false;
	}

	public boolean getIsProcessing() {
		return isProcessing;
	}

	public void setIsProcessing(boolean value) {
		isProcessing = value;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getPathToResourse() {
		return pathToResourse;
	}

	// public void setPathToResourse(String pathToImageResourse) {
	// this.pathToResourse = pathToImageResourse;
	// }

	public String getUrlToResource() {
		return urlToResource;
	}

	public void setUrlToResource(String urlToImageResource) {
		this.urlToResource = urlToImageResource;
		if (urlToResource.length() > 0)
			this.pathToResourse = new File(IssueViewFactory
					.getIssueViewFactoryIstance(mContext)
					.getPathToResourceFolder(), String.valueOf(urlToResource
					.hashCode())).getAbsolutePath();
	}

	public void installResource() {
		if (urlToResource.length() > 0) {
			if (mUpdateProgress != null) {
				mUpdateProgress.showProgress();
			}
			File file = new File(pathToResourse);

			URL url = null;
			try {
				url = new URL(urlToResource);
			} catch (MalformedURLException e2) {
				e2.printStackTrace();
				return;
			}

			HttpURLConnection connection = null;
			try {

				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(3000);
			} catch (IOException e2) {
				e2.printStackTrace();
				return;
			}

			try {
				connection.connect();
			} catch (IOException e1) {
				e1.printStackTrace();
				return;
			}

			int contentLength = connection.getContentLength();

			if (ApplicationManager.isEnoughMemory(contentLength)) {
				int readBytesNumberAtOnes = 0;
				if (mUpdateProgress != null) {
					mUpdateProgress.startProgress(contentLength);
				}

				InputStream inputStream = null;
				OutputStream outputStream = null;

				try {
					inputStream = connection.getInputStream();
					outputStream = new FileOutputStream(file);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
					return;
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}

				byte[] buf = new byte[1024];
				int currentTotalNumberOfReadBytes = 0;

				try {
					while ((readBytesNumberAtOnes = inputStream.read(buf)) != -1) {
						currentTotalNumberOfReadBytes += readBytesNumberAtOnes;

						outputStream.write(buf, 0, readBytesNumberAtOnes);
						outputStream.flush();
						if (mUpdateProgress != null) {
							mUpdateProgress
									.setProgress(currentTotalNumberOfReadBytes);
						}
						if (contentLength == currentTotalNumberOfReadBytes) {
							break;
						}
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				try {
					inputStream.close();
					outputStream.close();
				} catch (IOException e) {

					e.printStackTrace();

				}
				if (mUpdateProgress != null) {
					mUpdateProgress.hideProgress();
				}

				showResource();
			} else {
				Log.e("download url:", "url" + urlToResource);
				if (ResourceFactory.isEnoughMemory) {
					ResourceFactory.isEnoughMemory = false;
					DialogHelper.makeAsynckMessageShow(mContext,
							R.string.error_no_space);

				}
			}
		}
	}

	public void showResource() {
	}

	public void releaseInctiveResources() {

	}
	// public void downloadResource();
	//
	// public void showResource();
	//
	// public void setState(State state);
	//
	// public State getState();
	//
	// public boolean isExistResourceLocal();
	//
	// public boolean getIsProcessing();
	//
	// public void setIsProcessing(boolean value);
	//
	// public abstract void updateProgress(Bundle progressPramsBundel);
	//
	// public void releaseInctiveResources();
	// public void updateResourceState();
}
