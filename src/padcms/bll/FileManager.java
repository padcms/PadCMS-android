package padcms.bll;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;

public class FileManager {
	
	
	public static void copyFilesFromAssetsToFileSystem(Context mContext,
			String sourceFolder, String destFolder) {
		AssetManager aManager = mContext.getAssets();

		File fileDir = new File(destFolder);

		if (!fileDir.exists()) {
			fileDir.mkdirs();
			try {

				for (String fileName : aManager.list(sourceFolder)) {
					if (aManager.list(sourceFolder + "/" + fileName) != null
							&& aManager.list(sourceFolder + "/" + fileName).length > 0) {
						copyFromDirectoryToDir(aManager, sourceFolder + "/"
								+ fileName, fileDir.getAbsolutePath() + "/"
								+ fileName);
					} else {
						InputStream inputFile = aManager.open(sourceFolder
								+ "/" + fileName);
						FileOutputStream outputFile = new FileOutputStream(
								new File(fileDir.getAbsolutePath() + "/"
										+ fileName));
						saveFile(inputFile, outputFile);
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void copyFromDirectoryToDir(AssetManager aManager,
			String outputPath, String inputFolder) {
		File Dir = new File(inputFolder);
		Dir.mkdirs();
		try {
			for (String fileName : aManager.list(outputPath)) {
				if (aManager.list(outputPath + "/" + fileName) != null
						&& aManager.list(outputPath + "/" + fileName).length > 0) {
					copyFromDirectoryToDir(aManager, outputPath + "/"
							+ fileName, Dir.getAbsolutePath() + "/" + fileName);
				} else {
					InputStream inputFile = aManager.open(outputPath + "/"
							+ fileName);
					FileOutputStream outputFile = new FileOutputStream(
							new File(Dir.getAbsolutePath() + "/" + fileName));
					saveFile(inputFile, outputFile);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveFile(InputStream inputFile,
			FileOutputStream outputFile) {

		if (inputFile != null) {
			byte[] buffer = new byte[5 * 1024];
			int bufferLength = 0;
			try {
				while ((bufferLength = inputFile.read(buffer)) > 0) {
					outputFile.write(buffer, 0, bufferLength);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					outputFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

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

}
