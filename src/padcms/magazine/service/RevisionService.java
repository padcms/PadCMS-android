package padcms.magazine.service;

import padcms.application11.R;
import padcms.dao.factory.DataStorageFactory;
import padcms.kiosk.KioskActivity;
import padcms.kiosk.RevisionStateManager;
import padcms.kiosk.RevisionStateManager.RevisionInstallState;
import padcms.magazine.IssueActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class RevisionService extends Service {
	private static final String TAG = "RevisionService";
	private NotificationManager mNM;
	// private final IBinder mBinder = new LocalBinder();
	private Notification notificationProgress;
	public int maxProgressValue = 0;

	private int revisionID = -1;
	private String revisionTitle;

	static final int MSG_SET_MAXVALUE = 1;
	static final int MSG_SET_VALUE = 2;
	static final int MSG_REMOVE_NOTIF = 3;
	final Messenger mMessenger = new Messenger(new IncomingHandler());

	public class LocalBinder extends Binder {
		RevisionService getService() {
			return RevisionService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		revisionID = intent.getExtras().getInt("revisionID");
		revisionTitle = intent.getExtras().getString("revisionTitle");

		Log.e(TAG, "onBind " + revisionID);

		showNotification();
		return mMessenger.getBinder();
	}

	@Override
	public void onCreate() {
		Log.e(TAG, "onCreate " + revisionID);
		// Toast.makeText(this, "Service Started.", Toast.LENGTH_LONG).show();
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

	}

	@Override
	public void onDestroy() {
		// Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
		Log.e(TAG, "onDestroy " + revisionID);
		maxProgressValue = 0;
		removeNotification();

	}

	@Override
	public void onStart(Intent intent, int startid) {
		Log.e(TAG, "onStart " + revisionID);
		Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();

	}

	private void showNotification() {
		CharSequence text = "notify";
		notificationProgress = new Notification(R.drawable.icon, text,
				System.currentTimeMillis());
		// notificationProgress.flags = Notification.FLAG_NO_CLEAR;

		Intent intent = new Intent(this, IssueActivity.class);
		intent.putExtra("revisionID", revisionID);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, 0);

		notificationProgress.setLatestEventInfo(this, "Processing revision:"
				+ revisionID, text, contentIntent);

		notificationProgress.contentView = new RemoteViews(
				getApplicationContext().getPackageName(),
				R.layout.progress_download_service);

		notificationProgress.contentView.setTextViewText(R.id.revisionName,
				revisionTitle);

		mNM.notify(revisionID, notificationProgress);

	}

	private void removeNotification() {
		mNM.cancel(revisionID);
		Log.e("removeNotification removeNotification", "" + revisionID);
	}

	private void setMaxProgress(int maxProgress) {
		Log.e("setMaxProgress", "" + maxProgress);
		maxProgressValue = maxProgress;
		notificationProgress.contentView.setViewVisibility(
				R.id.ProgressBarDownload, View.VISIBLE);
		notificationProgress.contentView.setProgressBar(
				R.id.ProgressBarDownload, maxProgress, 0, false);
		mNM.notify(revisionID, notificationProgress);
		// notificationProgress.setLatestEventInfo(this, "Downloading revision:"
		// + reviciosID, "text", null);
	}

	private void setProgress(int progressValue) {
		Log.e("setProgress " + progressValue, "" + maxProgressValue);
		if (maxProgressValue > 0) {

			Log.e("setProgress reviciosID", "" + revisionID);
			if (progressValue == maxProgressValue ) {
				notificationProgress.contentView.setViewVisibility(
						R.id.ProgressBarDownload, View.INVISIBLE);
				notificationProgress.contentView.setTextViewText(
						R.id.revisionName, "Download complite");
				RevisionStateManager.getInstance(this).setState(revisionID,
						RevisionInstallState.DOWNLOADED);
				startNewDownload();
			} else {
				notificationProgress.contentView.setProgressBar(
						R.id.ProgressBarDownload, maxProgressValue,
						progressValue, false);

			}
			mNM.notify(revisionID, notificationProgress);
		}
	}

	private void startNewDownload() {
		KioskActivity.checkInstalledMagazin(this, DataStorageFactory
				.getInstance(this).getApplication());

	}

	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SET_MAXVALUE:
				setMaxProgress(msg.arg2);
				break;

			case MSG_SET_VALUE:

				setProgress(msg.arg1);

				break;
			case MSG_REMOVE_NOTIF:
				removeNotification();
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

}