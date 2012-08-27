package padcms.magazine.resource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import padcms.bll.ApplicationManager;
import padcms.magazine.controls.imagecontroller.ImageViewGroup;
import padcms.magazine.factory.IssueViewFactory;
import padcms.magazine.page.State;
import padcms.net.NetHepler;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration.Status;
import android.util.Log;

public class PartedImageViewController extends ResourceController {
	public static final int BUF_SIZE = 1024 * 8;

	// private ImageViewGroup imageViewGroup;

	public PartedImageViewController(Context mContext) {
		super(mContext);

	}

	public PartedImageViewController(Context mContext, String uriToResourse) {
		super(mContext);
		if (uriToResourse != null && uriToResourse.length() > 0) {
			setUrlToResource(NetHepler
					.getUrlToResourceByDemetion(uriToResourse));

		}
	}

	public PartedImageViewController(Context mContext, String uriToResourse,
			String demention) {
		super(mContext);
		if (uriToResourse != null && uriToResourse.length() > 0) {
			setUrlToResource(NetHepler.getUrlToResourceByDemetion(
					uriToResourse, demention));

		}
	}

	@Override
	public void setUrlToResource(String urlToImageResource) {

		if (urlToImageResource.length() > 0) {
			urlToResource = urlToImageResource.replace(".png", ".zip").replace(".jpg", ".zip");
			this.pathToResourse = new File(IssueViewFactory
					.getIssueViewFactoryIstance(mContext)
					.getPathToResourceFolder(), String.valueOf(urlToResource
					.hashCode())).getAbsolutePath();
		}
	}

	// public void setImageViewGroup(ImageViewGroup imageViewGroup) {
	// this.imageViewGroup = imageViewGroup;
	// }

	@Override
	public void installResource() {
		if (!downloadFile(urlToResource, new File(pathToResourse + ".zip"))) {
			return;
		}

		if (!extractZip(new File(pathToResourse + ".zip"), new File(
				pathToResourse))) {
			return;
		}

		showResource();

	}

	private boolean downloadFile(String fromUrl, File toFile) {
		if (mUpdateProgress != null) {
			mUpdateProgress.showProgress();
		}
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		boolean returnValue = true;

		try {
			int readBytesNumberAtOnes = 0;
			URL url = new URL(fromUrl);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(3000);
			connection.connect();

			int contentLength = connection.getContentLength();
			int currentProgres = 0;

			if (ApplicationManager.isEnoughMemory(contentLength)) {
				if (mUpdateProgress != null) {
					mUpdateProgress.startProgress(contentLength);
				}

				bis = new BufferedInputStream(connection.getInputStream());
				bos = new BufferedOutputStream(new FileOutputStream(toFile));
				byte[] buf = new byte[BUF_SIZE];

				int oneFrom100part = contentLength / 100;
				int counter = 1;
				while ((readBytesNumberAtOnes = bis.read(buf)) != -1) {
					// Log.i("tag", "DOWNLOAD STREAM ");
					if (Thread.currentThread().isInterrupted()) {
						Log.i("tag", "DOWNLOAD STREAM INTERRUPTED");

						currentProgres = 0;
						// toFile.delete();
						// Log.d("tag",
						// "Downloading file was deleted."
						// + toFile.length());
						returnValue = false;
						break;
					}
					currentProgres += readBytesNumberAtOnes;
					bos.write(buf, 0, readBytesNumberAtOnes);
					bos.flush();

					if (currentProgres > oneFrom100part * counter
							|| currentProgres == contentLength) {

						counter++;
						if (mUpdateProgress != null) {
							mUpdateProgress.setProgress(currentProgres);
						}
					}

					if (contentLength == currentProgres) {
						Log.i("tag", "Download finished");

						currentProgres = 0;

						break;
					}

				}
				if (mUpdateProgress != null) {
					mUpdateProgress.hideProgress();
				}
				// closeStreams(bis, bos);
				// this.handler
				// .sendMessageOfEventOnly(ProgressHandler.MSG_DOWNLOAD_ENDS);

			} else {

				returnValue = false;
			}
		} catch (IOException e) {

			// if (onStateInstallChangeListener != null) {
			// onStateInstallChangeListener.onInstallIterapted(revision);
			// onStateInstallChangeListener.onErrorInstall(revision);
			// }

			returnValue = false;
		} finally {
			try {
				closeStreams(bis, bos);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return returnValue;
	}

	private void closeStreams(BufferedInputStream bis,
			BufferedOutputStream outStream) throws IOException {
		if (bis != null)
			bis.close();

		if (outStream != null)
			outStream.close();
	}

	private boolean extractZip(File fromFileZip, File toFolder) {
		if (mUpdateProgress != null) {
			mUpdateProgress.showProgress();
		}
		File tempFolder = new File(toFolder.getAbsolutePath() + "_temp");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		Log.i("!!!!Extracting", "from file=\"" + fromFileZip.getAbsolutePath()
				+ "\"");
		int currentProgres = 0;
		boolean returnadValue = true;
		try {

			ZipFile zipFile = new ZipFile(fromFileZip);
			int contentLength = zipFile.size();

			Enumeration<? extends ZipEntry> e = zipFile.entries();
			if (mUpdateProgress != null) {
				mUpdateProgress.startProgress(contentLength);
			}
			while (e.hasMoreElements()) {

				ZipEntry entry = (ZipEntry) e.nextElement();
				File destinationFilePath = new File(tempFolder, entry.getName());

				destinationFilePath.getParentFile().mkdirs();
				if (entry.isDirectory()) {
					continue;
				} else {
					Log.d("tag", "Extracting " + destinationFilePath);

					bis = new BufferedInputStream(zipFile.getInputStream(entry));
					int b;
					byte buffer[] = new byte[BUF_SIZE];
					FileOutputStream fos = new FileOutputStream(
							destinationFilePath);

					bos = new BufferedOutputStream(fos, BUF_SIZE);

					while ((b = bis.read(buffer, 0, BUF_SIZE)) != -1) {
						if (Thread.currentThread().isInterrupted()) {
							Log.d("EXTRACTOR", "UNEXSPACTLY INTERRUPTED");
							// if (onStateInstallChangeListener != null) {
							// onStateInstallChangeListener
							// .onInstallIterapted(revision);
							// }
							returnadValue = false;
							break;
						}
						bos.write(buffer, 0, b);
					}
					bos.flush();
					if (!returnadValue) {
						currentProgres = 0;
						break;
					}
				}

				currentProgres++;
				if (mUpdateProgress != null) {
					mUpdateProgress.setProgress(currentProgres);
				}
				if (contentLength == currentProgres) {
					Log.d("EXTRACTOR", "ALL WAS EXTRACTED");
					break;
				}
			}

			if (contentLength != currentProgres) {
				//
				// Log.e("ERROR OF EXTRACTING",
				// "ERROR OF EXTRACTING TOTAL_ENTRIES_NUMBER="
				// + contentLength
				// + ", but currentNumberOfReadEntries="
				// + currentProgres);

				// if (onStateInstallChangeListener != null) {
				// onStateInstallChangeListener.onInstallIterapted(revision);
				// onStateInstallChangeListener.onErrorInstall(revision);
				// }
				// alertMessage(R.string.extracting_error);
				fromFileZip.delete();
				tempFolder.delete();
				returnadValue = false;
			} else {
				returnadValue = true;
				fromFileZip.delete();
				tempFolder.renameTo(toFolder);

			}

			closeStreams(bis, bos);
			if (mUpdateProgress != null) {
				mUpdateProgress.hideProgress();
			}
		} catch (Exception ex) {
			Thread.currentThread().interrupt();
			Log.e("Extractor url", urlToResource);
			Log.e("Extractor", "IOExeption during extraction", ex);
			// if (onStateInstallChangeListener != null) {
			// onStateInstallChangeListener.onInstallIterapted(revision);
			// onStateInstallChangeListener.onErrorInstall(revision);
			// }

			returnadValue = false;
		} finally {

			try {
				closeStreams(bis, bos);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returnadValue;
	}

	@Override
	public void setState(State state) {

		super.setState(state);
		if (isExistResourceLocal()) {

			switch (state) {
			case DOWNLOAD:
				if (getBaseElemetView().getShowingView() != null) {
					((ImageViewGroup) getBaseElemetView().getShowingView())
							.relese();
				}
				break;
			case ACTIVE:
				break;
			case DISACTIVE:

				break;
			case RELEASE:
				if (getBaseElemetView().getShowingView() != null) {
					((ImageViewGroup) getBaseElemetView().getShowingView())
							.relese();
				}
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void showResource() {
		Log.i("showResource:"+state, "url:" + urlToResource);
		super.showResource();
		if (isExistResourceLocal()) {

			switch (state) {
			case ACTIVE:
				onDrow(false);
				break;
			case DISACTIVE:
				onDrow(true);
				break;
			case RELEASE:
				if (getBaseElemetView().getShowingView() != null) {
					((ImageViewGroup) getBaseElemetView().getShowingView())
							.relese();
				}
				break;
			default:
				break;
			}
		}

	}

	public void onDrow(final boolean scale) {

		if (getBaseElemetView().getShowingView() != null) {
			if (mUpdateProgress != null) {
				mUpdateProgress.hideProgress();
			}
			((Activity) mContext).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ImageViewGroup imageViewGroup = ((ImageViewGroup) getBaseElemetView()
							.getShowingView());

					imageViewGroup.setImagePath(pathToResourse);
					if (scale) {
						imageViewGroup.drowScale();
					} else {
						imageViewGroup.drowOriginal();
					}

				}
			});
		}
	}

}
