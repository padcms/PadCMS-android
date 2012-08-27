package padcms.kiosk.view.listener;

import java.io.File;

import padcms.application11.R;
import padcms.bll.ApplicationFileHelper;
import padcms.bll.DialogHelper;
import padcms.dao.application.bean.Revision;
import padcms.dao.factory.DataStorageFactory;
import padcms.kiosk.RevisionStateManager;
import padcms.kiosk.RevisionStateManager.RevisionInstallState;
import padcms.kiosk.view.BasicViewController;
import padcms.magazine.IssueActivity;
import padcms.magazine.service.RevisionServiceController;
import padcms.net.NetHepler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class InstallButtonController implements OnClickListener {
	private Context context;
	private BasicViewController baseViewController;

	/**
	 * Allocates new <code>FirstButtonListener</code> object.
	 * 
	 * @param coverControlsComponent
	 * @param context
	 */
	public InstallButtonController(Context context,
			BasicViewController baseViewController) {
		// this.parentViewController = coverControlsComponent;
		this.baseViewController = baseViewController;
		this.context = context;
	}

	/**
	 * Makes a decision what task initialize or not, what changes in GUI in
	 * accordance with onClick on upper(firstButton) button under|left to cover
	 * of issue.
	 */
	public void onClick(View v) {

		if (!v.isClickable())
			return;

		Revision revision = (Revision) v.getTag();

		RevisionInstallState state = RevisionStateManager.getInstance(
				v.getContext()).getState(revision.getId().intValue());
		Log.i("FIRST BUTTON PRESSED", "FIRST BUTTON PRESSED:" + state);
		switch (state) {

		case NOT_INTALLED:
			if (!this.baseViewController.isInstallerALive()) {
				initializeInstall(revision);
			} else {
				showConcurrentDownloadingMessage();
			}
			break;

		case INSTALLING:

			baseViewController.interruptInstall(revision);
			break;

		case INSTALLED:
			handleOpenning(v.getContext(), revision);
			break;

		}
	}

	private void handleOpenning(final Context mContext, final Revision revision) {
		Log.i("FIRST BUTTON LISTENER", "OPEN_BUTTON PRESSED");
		String absolutePathRevisionDB = ApplicationFileHelper
				.getFileInstallRevisionFolder(revision.getId().intValue())
				.getAbsolutePath()
				+ "/sqlite.db";
		if (new File(absolutePathRevisionDB).exists()) {
			baseViewController.setVisibleStateOpening(revision);
			DataStorageFactory.getInstance(mContext).initRevisionPages(
					revision, new Handler() {
						@Override
						public void handleMessage(Message msg) {
							super.handleMessage(msg);
							if (msg.what == 1) {
								RevisionServiceController.getInstanse(mContext).doBindService(revision);
								Intent intent = new Intent(mContext,
										IssueActivity.class);
								intent.putExtra("revisionID", revision.getId());

								mContext.startActivity(intent);

								((Activity) mContext)
										.overridePendingTransition(
												R.anim.zoom_enter,
												R.anim.hold_slide);
							}
						}
					});
		} else {
			baseViewController.setRevisionState(revision,
					RevisionStateManager.RevisionInstallState.NOT_INTALLED);
			baseViewController.setVisibleOnState(
					RevisionStateManager.RevisionInstallState.NOT_INTALLED,
					revision);

		}

	}

	private void showConcurrentDownloadingMessage() {
		DialogHelper.ShowMessageWindow(context, context
				.getString(R.string.warning_title), context
				.getString(R.string.concurrent_downloading_imposible_msg));
	}

	private void showNoConnectionMessage() {
		DialogHelper.ShowMessageWindow(context,
				context.getString(R.string.warning_title),
				context.getString(R.string.download_impossible_no_connection));
	}

	private void initializeInstall(Revision revision) {
		if (NetHepler.isOnline(context)) {
			baseViewController.startInstall(revision);
		} else {
			showNoConnectionMessage();
		}
	}
}
