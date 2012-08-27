package padcms.kiosk.view.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import padcms.application11.R;
import padcms.bll.ApplicationFileHelper;
import padcms.bll.ApplicationManager;
import padcms.dao.application.bean.Revision;
import padcms.kiosk.RevisionStateManager.RevisionInstallState;
import padcms.net.NetHepler;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.widget.Toast;

/**
 * <code>Downloader</code> is used to download file from the Internet. It
 * extends <code>Thread</code> and runs in self thread.
 */
public class InstallerController extends Thread implements Runnable {
	// private static final String CONTENT_LENGTH_HEADER = "Content-Length";
	// private final String tag = this.getClass().getName();
	// private final String fileNameForSave;
	// private final String urlFrom;
	// private ProgressHandler handler;
	// private long currentTotalNumberOfReadBytes = 0;
	// private int contentLength = 0;
	public static final int BUF_SIZE = 1024;
	private OnStateInstallChangeListener onStateInstallChangeListener;
	private OnProgressInstallChangeListener onProgressInstallChangeListener;

	private Context mContext;
	private File folderDB;
	private File fileZipDB;

	private String urlToDB;
	private String urlToVideo;
	private File fileToVideo;
	private Revision revision;

	private int contentLength;
	private int currentProgres;

	public InstallerController(Context mContext, Revision revision) {
		super("DOWNLOADER_THREAD");
		this.mContext = mContext;
		this.revision = revision;
		folderDB = ApplicationFileHelper.getFileInstallRevisionFolder(revision
				.getId().intValue());
		fileZipDB = new File(folderDB, revision.getId().toString() + ".zip");
		urlToDB = NetHepler.getUrlToDownloadRevision(revision.getId());
		if (revision.getVideo() != null && revision.getVideo().length() > 0) {
			urlToVideo = NetHepler.getAbsoluteUrlToVideo(revision.getVideo());
			fileToVideo = new File(
					ApplicationFileHelper.getFileRevisionResourcesFolder(revision
							.getId().intValue()), String.valueOf(urlToVideo
							.hashCode()));

		}

	}

	public static interface OnStateInstallChangeListener {

		void onStateChenged(RevisionInstallState state, Revision revision);

		void onErrorInstall(Revision revision);

		void onInstallIterapted(Revision revision);

	}

	public static interface OnProgressInstallChangeListener {

		void onProgressDownloadChenged(Revision revision, int progress);

		void onProgressInstallChenged(Revision revision, int progress);

		void onProgressInstallSetMax(Revision revision, int progressMax);

		void onProgressDownloadSetMax(Revision revision, int progressMax);

		void onProgressInstallFinished(Revision revision);

		void onProgressDownloadFinished(Revision revision);

	}

	public void run() {
		super.run();
		if (onStateInstallChangeListener != null) {
			onStateInstallChangeListener.onStateChenged(
					RevisionInstallState.INSTALLING, revision);
		}

		if (!downloadFile(urlToDB, fileZipDB)) {
			if (onStateInstallChangeListener != null) {
				onStateInstallChangeListener.onInstallIterapted(revision);
				onStateInstallChangeListener.onErrorInstall(revision);
			}
			return;
		}

		if (!extractZip(fileZipDB, folderDB)) {
			if (onStateInstallChangeListener != null) {
				onStateInstallChangeListener.onInstallIterapted(revision);
				onStateInstallChangeListener.onErrorInstall(revision);
			}
			return;
		}

		if (urlToVideo != null && urlToVideo.length() > 0) {

			downloadFile(urlToVideo, fileToVideo);

		}

		if (!Thread.currentThread().isInterrupted()
				&& onStateInstallChangeListener != null) {
			Log.i("tag", "DOWNLOAD finish");
			onStateInstallChangeListener.onStateChenged(
					RevisionInstallState.INSTALLED, revision);
		} else {
			Log.i("tag", "DOWNLOAD interupted");
		}
	}

	private boolean downloadFile(String fromUrl, File toFile) {

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		boolean returnValue = true;
		// File file = null;

		try {
			int readBytesNumberAtOnes = 0;
			URL url = new URL(fromUrl);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(3000);
			connection.connect();

			checkResponseCode(connection);

			contentLength = connection.getContentLength();
			currentProgres = 0;
			checkContentLengthIncorrect(contentLength);

			if (ApplicationManager.isEnoughMemory(contentLength)) {
				if (onStateInstallChangeListener != null) {
					onStateInstallChangeListener.onStateChenged(
							RevisionInstallState.INSTALLING, revision);
				}

				if (onProgressInstallChangeListener != null) {
					onProgressInstallChangeListener.onProgressDownloadSetMax(
							revision, contentLength);

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

						if (onStateInstallChangeListener != null) {
							onStateInstallChangeListener
									.onInstallIterapted(revision);
						}
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
						if (onProgressInstallChangeListener != null) {
							onProgressInstallChangeListener
									.onProgressDownloadChenged(revision,
											currentProgres);
						}

						counter++;
					}
					if (contentLength == currentProgres) {
						Log.i("tag", "Download finished");
						if (onProgressInstallChangeListener != null) {
							onProgressInstallChangeListener
									.onProgressDownloadFinished(revision);

						}

						currentProgres = 0;

						break;
					}
				}
				// closeStreams(bis, bos);
				// this.handler
				// .sendMessageOfEventOnly(ProgressHandler.MSG_DOWNLOAD_ENDS);

			} else {

				// if (onStateInstallChangeListener != null) {
				// onStateInstallChangeListener.onInstallIterapted(revision);
				// onStateInstallChangeListener.onErrorInstall(revision);
				// }

				alertMessage(R.string.download_impossible_no_space);
				returnValue = false;
			}
		} catch (IOException e) {

			// if (onStateInstallChangeListener != null) {
			// onStateInstallChangeListener.onInstallIterapted(revision);
			// onStateInstallChangeListener.onErrorInstall(revision);
			// }
			alertMessage(R.string.download_impossible_connection_error);
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

	private boolean extractZip(File fromFileZip, File toFolder) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		Log.i("!!!!Extracting", "from file=\"" + fromFileZip.getAbsolutePath()
				+ "\"");
		currentProgres = 0;
		boolean returnadValue = true;
		try {

			ZipFile zipFile = new ZipFile(fromFileZip);
			contentLength = zipFile.size();

			if (onProgressInstallChangeListener != null) {
				onProgressInstallChangeListener.onProgressInstallSetMax(
						revision, contentLength);
			}

			Enumeration<? extends ZipEntry> e = zipFile.entries();

			while (e.hasMoreElements()) {
				if (Thread.currentThread().isInterrupted()) {
					if (onStateInstallChangeListener != null)
						onStateInstallChangeListener
								.onInstallIterapted(revision);
					currentProgres = 0;
					returnadValue = false;
					break;
				}

				ZipEntry entry = (ZipEntry) e.nextElement();
				File destinationFilePath = new File(toFolder, entry.getName());

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
					if (!returnadValue) {
						currentProgres = 0;
						break;
					}
				}

				currentProgres++;
				if (onProgressInstallChangeListener != null) {
					onProgressInstallChangeListener.onProgressInstallChenged(
							revision, currentProgres);
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
				alertMessage(R.string.extracting_error);

				returnadValue = false;
			} else if (onProgressInstallChangeListener != null) {
				returnadValue = true;
				fileZipDB.delete();
				onProgressInstallChangeListener
						.onProgressInstallFinished(revision);

			}

			closeStreams(bis, bos);

		} catch (Exception ex) {
			Thread.currentThread().interrupt();
			Log.e("Extractor", "IOExeption during extraction", ex);
			// if (onStateInstallChangeListener != null) {
			// onStateInstallChangeListener.onInstallIterapted(revision);
			// onStateInstallChangeListener.onErrorInstall(revision);
			// }
			alertMessage(R.string.extracting_error);
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

	public void clean() {
		if (urlToDB.length() > 0) {
			ApplicationFileHelper.deleteFiles(fileZipDB);
		}
	}

	public void alertMessage(int messageID) {
		alertMessage(mContext.getString(messageID));
	}

	public void alertMessage(final String message) {
		((Activity) mContext).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void checkContentLengthIncorrect(long contentLength)
			throws IOException {
		if (contentLength < 1) {
			Log.e("tag", "Incorrect HTTP RESPONSE. CONTENT-LENGTH < 1");
			// alertMessage(R.string.download_impossible_http_response_error);
			throw new IOException();
		}
	}

	private void checkResponseCode(HttpURLConnection connection)
			throws IOException {
		if (connection.getResponseCode() / 100 != 2) {

			// alertMessage(R.string.download_impossible_http_response_error);
			throw new IOException();

		}
	}

	private void closeStreams(BufferedInputStream bis,
			BufferedOutputStream outStream) throws IOException {
		if (bis != null)
			bis.close();

		if (outStream != null)
			outStream.close();
	}

	public void setOnStateInstallChangeListener(
			OnStateInstallChangeListener onStateInstallChangeListener) {
		this.onStateInstallChangeListener = onStateInstallChangeListener;
	}

	public void setOnProgressInstallChangeListener(
			OnProgressInstallChangeListener onProgressInstallChangeListener) {
		this.onProgressInstallChangeListener = onProgressInstallChangeListener;
	}

	public int getContentLength() {
		return contentLength;
	}

	public int getCurrentProgres() {
		return currentProgres;
	}

}