package padcms.bll;

import padcms.application11.R;
import padcms.kiosk.RevisionStateManager;
import padcms.net.NetHepler;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.Display;

public class ApplicationManager {
	public static String databaseName;
	public static String dataBasePath;
	public static String applicationBaseFolder;
	private static int applicationStatus;
	// private static int clientID;
	private static float density;
	private static int appicationID;
	public static String elementResolution;
	public static String elementResolutionHorisontal;

	public static final int APPLICATION_STATUS_EXIT = -1;
	public static final int APPLICATION_STATUS_RUN = 1;
	public static final int APPLICATION_STATUS_SUSPENDED = 0;

	public static void initApplication(Context context) {
		ApplicationFileHelper.externalBasePath = Environment
				.getExternalStorageDirectory().getAbsolutePath();
		NetHepler.serviceHost = context.getString(R.string.service_host);
		// clientID = Integer.parseInt(context.getString(R.string.clientID));
		appicationID = Integer.parseInt(context
				.getString(R.string.appicationID));
		databaseName = context.getString(R.string.dbName);
		applicationBaseFolder = context.getString(R.string.rootFolder);
		dataBasePath = getDataBasePath(context);
		applicationStatus = APPLICATION_STATUS_RUN;

		Display display = ((Activity) context).getWindowManager()
				.getDefaultDisplay();
		int baseWidth = display.getWidth();
		if (display.getWidth() > display.getHeight()) {
			baseWidth = display.getHeight();
		}
		RevisionStateManager.getInstance(context);
		ImageLoader.getInstance(context);
		density = context.getResources().getDisplayMetrics().density;
	
		if (baseWidth <= 600) {
			elementResolution = "600-1024";
			elementResolutionHorisontal = "1024-768";
		} else if (baseWidth > 600 && baseWidth <= 768) {
			elementResolution = "768-1024";
			elementResolutionHorisontal = "1024-768";
		} else if (baseWidth > 768 && baseWidth <= 800) {
			elementResolution = "800-1280";
			elementResolutionHorisontal = "1280-800";
		} else if (baseWidth > 800) {
			elementResolution = "1536-2048";
			elementResolutionHorisontal = "2048-1536";
		}

	}

	private static String getDataBasePath(Context context) {

		return ApplicationFileHelper.getFileApplicationDB(databaseName)
				.getAbsolutePath();

	}

	public static void setApplicationStatus(int status) {
		applicationStatus = status;
	}

	public static void exitApplication() {
		setApplicationStatus(APPLICATION_STATUS_EXIT);
	}

	public static boolean isApplicationNeedExit() {
		return applicationStatus == APPLICATION_STATUS_EXIT;
	}

	public static float getDensity() {
		return density;
	}

	public static int getAppicationID() {
		return appicationID;
	}

	public static void setAppicationID(int appicationID) {
		ApplicationManager.appicationID = appicationID;
	}

	public static boolean isEnoughMemory(long dwldSize) {
		double sdAvailSize = ApplicationFileHelper
				.getAvailableExternalMemorySize();
		Log.d("tag", "available memory = " + sdAvailSize);
		return Math.abs(2 * dwldSize) < sdAvailSize;
	}
}
