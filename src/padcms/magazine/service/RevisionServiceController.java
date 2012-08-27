package padcms.magazine.service;

import padcms.dao.application.bean.Revision;
import padcms.magazine.factory.IssueViewFactory;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class RevisionServiceController {
	// private RevisionService mBoundService;
	private Context mContext;
	private boolean mIsBound;
	public Messenger mService = null;
	public int revisionID;
	public static RevisionServiceController instanse;
	private int maxProgressValue = 0;

	public static RevisionServiceController getInstanse(Context mContext) {
		if (instanse == null) {
			instanse = new RevisionServiceController(mContext);
		}
		return instanse;
	}

	public RevisionServiceController(Context mContext) {
		this.mContext = mContext;
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {

			// mBoundService = ((RevisionService.LocalBinder) service)
			// .getService();
		//	Toast.makeText(mContext, "onServiceConnected.", Toast.LENGTH_LONG)
		//			.show();
			mService = new Messenger(service);
			mIsBound = true;
			

		}

		public void onServiceDisconnected(ComponentName className) {

		//	Toast.makeText(mContext, "onServiceDisconnected.",
		//			Toast.LENGTH_LONG).show();
			removeAllNotification();
				
			mService = null;

		}
	};

	public void doBindService(Revision revision) {
		// Log.e("reviciosID doBindService",""+mIsBound);
		if (this.revisionID != revision.getId().intValue() || !mIsBound) {
			doUnbindService();
			this.revisionID = revision.getId().intValue();
			IssueViewFactory.getIssueViewFactoryIstance(mContext)
			 .initPageViewCollection(mContext);
		
			Intent intent = new Intent(mContext, RevisionService.class);
			intent.putExtra("revisionID", revisionID);
			intent.putExtra("revisionTitle", revision.getParenIssue()
					.getTitle()); 

			mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

			mIsBound = true;
		}
	}

	public void doUnbindService() {
		if (mService != null && mIsBound) {
			maxProgressValue = 0;
			Log.e("doUnbindService doUnbindService", "doUnbindService doUnbindService");
			IssueViewFactory.getIssueViewFactoryIstance(mContext)
					.refuseDownloadBG();
		
		//	removeNotification();
			if (mService != null)
			mContext.unbindService(mConnection);
			removeAllNotification();
			
			mService = null;
			mIsBound = false;
		}
	}

	public void setMaxProgress(int revisionID, int maxProgress) {
		if (this.revisionID == revisionID && mService != null) {
			maxProgressValue = maxProgress;
			Message msg = Message.obtain(null,
					RevisionService.MSG_SET_MAXVALUE, 0, maxProgress);
			try {
			
					mService.send(msg);
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}
	}

	public void setProgress(int revisionID, int progressValue) {
		if (this.revisionID == revisionID && progressValue > 0) {
			Message msg = Message.obtain(null, RevisionService.MSG_SET_VALUE,
					progressValue, 0);
			try {
				if (mService != null && mIsBound)
					mService.send(msg);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public int getMaxProgress() {
		return maxProgressValue;
	}

	public void removeNotification() {
		Message msg = Message.obtain(null, RevisionService.MSG_REMOVE_NOTIF, 0,
				0);
		try {
			if (mService != null)
				mService.send(msg);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void removeAllNotification() {
		NotificationManager	mNM = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
		mNM.cancelAll();
	}
}