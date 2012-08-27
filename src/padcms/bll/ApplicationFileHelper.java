package padcms.bll;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Thread.State;
import java.util.Stack;

import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;

public class ApplicationFileHelper {
	// public static Stack<ApplicationResource> stackDownloadFirstQuery = new
	// Stack<ApplicationResource>();
	// public static Stack<ApplicationResource> stackDownloadSecondQuery = new
	// Stack<ApplicationResource>();
	// public static Thread threadDownloadFirstQuery = null;
	// public static Thread threadDownloadSecondQuery = null;

	public static String tempFolderPath;
	public static String externalBasePath;

	public static File getFileTempFolder(String fileName) {
		return new File(getFileTempFolder().getAbsolutePath() + "/" + fileName);
	}

	public static File getFileTempFolder() {
		return getFolderByPathAndCreate(getApplicationToRootFolder()
				.getAbsolutePath() + "/temp");
	}

	// public static File getPathToDBFolder(String fileName) {
	// return new File(getApplicationToRootFolder().getAbsolutePath() + "/"
	// + fileName);
	// }

	public static File getFileApplicationDB(String dbName) {
		return new File(getFolderByPathAndCreate(getApplicationToRootFolder()
				.getAbsolutePath()) + "/" + dbName);
	}

	// public static File getPathToRevisionDBFolder() {
	// return new File(getApplicationToRootFolder().getAbsolutePath());
	// }

	public static File getFileInstallRevisionFolder(int revisionID) {

		return getFolderByPathAndCreate(getApplicationToRootFolder()
				.getAbsolutePath() + "/" + revisionID);
	}

	//
	// public static File getPathToIssueFolder(String name) {
	// return new File(getFolderByPathAndCreate(getApplicationToRootFolder()
	// .getAbsolutePath() + "/issue")
	// + "/" + name);
	// }

	// public static File getPathToRevisionDatabaseFolder(String name) {
	// return new File(getPathToIssueFolder(name) + "/sqlite.db");
	// }

	public static File getFileDBFolder() {
		return getFolderByPathAndCreate(getApplicationToRootFolder()
				.getAbsolutePath());
	}

	public static File getFileRevisionResourcesFolder(int revisionID) {
		return getFolderByPathAndCreate(getFileInstallRevisionFolder(revisionID)
				.getAbsolutePath() + "/res");
	}

	public static File getApplicationToRootFolder() {
		return getFolderByPathAndCreate(externalBasePath + "/"
				+ ApplicationManager.applicationBaseFolder + "/"
				+ ApplicationManager.getAppicationID());
	}

	// public static File getDownloadingFile(String fileName) {
	// // File file = new File(root.getAbsolutePath() + File.separator
	// // + FOLDER_NAME + File.separator + fileName);
	// return new File(getDownloadFilePath(fileName));
	// }

	// public static String getDownloadFilePath(String fileName) {
	// return getPathToTempFolder() + File.separator + fileName;
	// }

	public static File getFolderByPathAndCreate(String folderPath) {
		File fileFolder = new File(folderPath);
		if (!fileFolder.exists()) {
			fileFolder.mkdirs();
		}
		return fileFolder;
	}

	// public static void renameFolderForExtractingToDeleteVersion(
	// String folderPath, String postfix) {
	// File folder = new File(folderPath);
	// File folderDel = new File(folderPath + postfix);
	// folder.renameTo(folderDel);
	// }
	// public static void runDeletingThreadFor(final File file) {
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// deleteFiles(file);
	// }
	// }).start();
	// }

	// public static void runDeletingOfExtractedFolderFor(String folderPath) {
	// final File path = new File(folderPath);
	//
	// new Thread(new Runnable() {
	// public void run() {
	// deleteFiles(path);
	// }
	// }).start();
	// }

	public static boolean isSuchFileOnCard(String folderPath) {
		return (new File(folderPath)).exists();
	}

	//
	// public static void runThreadDownloadFirstQuery(Handler callbackHandler) {
	// if (threadDownloadFirstQuery == null
	// || threadDownloadFirstQuery.getState() == State.TERMINATED) {
	// threadDownloadFirstQuery = new Thread(
	// new RunnableDownloadFirstQuery(callbackHandler),
	// "threadDownloadFirstQuery");
	// threadDownloadFirstQuery.start();
	// }
	// }
	//
	// public static void runThreadDownloadSecondQuery(Activity mActivity) {
	// if (threadDownloadSecondQuery == null
	// || threadDownloadSecondQuery.getState() == State.TERMINATED) {
	// threadDownloadSecondQuery = new Thread(
	// new RunnableDownloadSecondQuery(),
	// "threadDownloadSecondQuery");
	// threadDownloadSecondQuery.start();
	// }
	// }

	// public static class RunnableDownloadFirstQuery implements Runnable {
	// Handler callbackHandler;
	//
	// public RunnableDownloadFirstQuery(Handler callbackHandler) {
	// this.callbackHandler = callbackHandler;
	// }
	//
	// @Override
	// public synchronized void run() {
	// Log.v("Run thread", "start");
	// while (true) {
	// if (stackDownloadFirstQuery.size() > 0)//
	// &&ApplicationController.ApplicationActive
	// {
	// ApplicationResource res = stackDownloadFirstQuery.get(0);
	// stackDownloadFirstQuery.removeElement(res);
	// if (res.urlToResource.length() > 0
	// || !res.urlToResource.equals("null"))
	// res.startDownloadItem();
	// } else {
	// Message msg = new Message();
	// // /msg.what = UpdateManager.START_APPLICATION;
	// if (callbackHandler != null)
	// callbackHandler.sendMessage(msg);
	// return;
	// }
	// }
	// }
	// }
	//
	// public static class RunnableDownloadSecondQuery implements Runnable {
	// @Override
	// public synchronized void run() {
	// Log.v("Run thread", "start");
	// while (true) {
	// if (stackDownloadSecondQuery.size() > 0)//
	// &&ApplicationController.ApplicationActive
	// {
	// ApplicationResource res = stackDownloadSecondQuery.get(0);
	// stackDownloadSecondQuery.removeElement(res);
	// res.startDownloadItem();
	// } else {
	// return;
	// }
	// }
	// }
	// }

	public static void saveFile(InputStream inputStream, File outputFile) {

		if (inputStream != null) {
			FileOutputStream outputFileStream = null;
			byte[] buffer = new byte[5 * 1024];
			int bufferLength = 0;
			try {
				outputFileStream = new FileOutputStream(outputFile);
				while ((bufferLength = inputStream.read(buffer)) > 0) {
					outputFileStream.write(buffer, 0, bufferLength);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (outputFileStream != null)
						outputFileStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void deleteFileAsynk(File file) {

		final File fileDelete = new File(file.getAbsolutePath() + "_del");
		file.renameTo(fileDelete);
		new Thread() {
			@Override
			public void run() {
				deleteFiles(fileDelete);
			}
		}.start();
	}

	public static void deleteFiles(File dirOfFiles) {
		if (dirOfFiles.exists()) {
			if (dirOfFiles.isDirectory()) {
				for (File file : dirOfFiles.listFiles()) {
					if (file.isDirectory()) {
						if (file.list().length > 0) {
							deleteFiles(file);
						}
						file.delete();

					} else {
						file.delete();
					}
				}
				dirOfFiles.delete();
			} else {
				dirOfFiles.delete();
			}
		}
	}

	public static boolean externalMemoryAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	public static String getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return formatSize(availableBlocks * blockSize);
	}

	public static String getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return formatSize(totalBlocks * blockSize);
	}

	public static long getAvailableExternalMemorySize() {
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return availableBlocks * blockSize;
		} else {
			return 0;
		}
	}

	public static long getTotalExternalMemorySize() {
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();
			return totalBlocks * blockSize;
		} else {
			return 0;
		}
	}

	public static String formatSize(long size) {
		String suffix = null;

		if (size >= 1024) {
			suffix = "KB";
			size /= 1024;
			if (size >= 1024) {
				suffix = "MB";
				size /= 1024;
			}
		}

		StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

		int commaOffset = resultBuffer.length() - 3;
		while (commaOffset > 0) {
			resultBuffer.insert(commaOffset, ',');
			commaOffset -= 3;
		}

		if (suffix != null)
			resultBuffer.append(suffix);
		return resultBuffer.toString();
	}

}
