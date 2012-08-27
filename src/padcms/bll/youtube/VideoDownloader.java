package padcms.bll.youtube;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import padcms.magazine.VideoActivity;
import android.util.Log;

public class VideoDownloader extends Thread implements Runnable {
	private long contentLength = -1;
	private long downloadingProgress = 0;
	private String embedUrlStr;
	private String pathToFile;
	private File tempFile;
	private VideoActivity videoActivity;

	public VideoDownloader(String embedUrlStr, String pathToFile,
			File tempFile, VideoActivity va) {
		super("DOWNLOADING_STREAM");
		this.pathToFile = pathToFile;
		this.embedUrlStr = embedUrlStr;
		this.tempFile = tempFile;
		this.videoActivity = va;
	}

	@Override
	public void run() {
		try {
			this.startDownloadItem();
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), "", e);
		}
	}

	/**
	 * 3rd
	 */
	private void startDownloadItem() {
		if (!tempFile.exists()) {
			Log.d("INSIDE startDownloadItem", "!!!!!!");

			BufferedInputStream bis = null;
			try {
				bis = new BufferedInputStream(
						new URL(this.embedUrlStr).openStream());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			saveInputStreamToFile(this.pathToFile, bis);
		}
	}

	/**
	 * 4th
	 */
	private void saveInputStreamToFile(String filePath, BufferedInputStream bin) {
		BufferedOutputStream bos = null;
		try {
			Log.i("INSIDE saveInputStreamToFile",
					"INSIDE saveInputStreamToFile");
			this.tempFile.createNewFile();
			bos = new BufferedOutputStream(
					new FileOutputStream(filePath, false));
			byte[] buffer = new byte[1024];
			int byteRead = 0;

			URL url = new URL(this.embedUrlStr);
			URLConnection conection = url.openConnection();
			conection.connect();
			conection.setConnectTimeout(3000);

			contentLength = conection.getContentLength();
			int count = 0;
			while ((byteRead = bin.read(buffer)) != -1
					&& !Thread.currentThread().isInterrupted()) {
				bos.write(buffer, 0, byteRead);
				bos.flush();
				count++;
				downloadingProgress += byteRead;

				if (contentLength != -1)
					updateSeekBar();
				else if (count == 400) {
					videoActivity.playVideoInUi();
				}

//				if (Thread.currentThread().isInterrupted()) {
//					closeStreams(bin, bos);
//					new File(filePath).delete();
//					return;
//				}
				// if (count == 400) {
				// Log.d("invokes playVideoInUi()"," 200: invokes playVideoInUi()");
				// this.videoActivity.playVideoInUi();
				// }
			}
			if (Thread.currentThread().isInterrupted()) {
				closeStreams(bin, bos);
				new File(filePath).delete();
				return;
			}
			Log.i("DOWNLOADING ENDS", "DOWNLOADING ENDS");
			downloadingProgress = 100L;

			if (count < 401) {
				Log.d("invokes playVideoInUi()", "201: invokes playVideoInUi()");
				this.videoActivity.playVideoInUi();
			}
		} catch (Exception e) {
			Log.e("catch download", "" + e.toString());
		} finally {
			closeStreams(bin, bos);
		}
	}

	private void closeStreams(BufferedInputStream bis, BufferedOutputStream bos) {
		if (bos != null) {
			try {
				bos.flush();
				bos.close();
			} catch (IOException e) {
				Log.e("in downloading thread: outputStream closing error", "",
						e);
			}
		}
		if (bis != null)
			try {
				bis.close();
			} catch (IOException e) {
				Log.e("in downloading thread: inputStream closing error", "", e);
			}
	}

	public synchronized long getDownloadingProgress() {
		return this.downloadingProgress;
	}

	public long getContentLength() {
		return this.contentLength;
	}

	public void updateSeekBar() {

		float persentDownload = (float) downloadingProgress
				/ (float) contentLength;

		videoActivity.setDownloadProgress((int) (persentDownload * 1000));
	}
}
