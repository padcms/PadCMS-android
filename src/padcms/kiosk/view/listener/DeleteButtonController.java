package padcms.kiosk.view.listener;

import java.io.File;

import padcms.application11.R;
import padcms.bll.ApplicationFileHelper;
import padcms.dao.application.bean.Revision;
import padcms.kiosk.RevisionStateManager;
import padcms.kiosk.RevisionStateManager.RevisionInstallState;
import padcms.kiosk.view.BasicViewController;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;

public class DeleteButtonController implements OnClickListener {
	private BasicViewController parentView;

	public DeleteButtonController(BasicViewController parentView) {
		this.parentView = parentView;
	}

	public void onClick(View v) {

		final Revision revision = (Revision) v.getTag();
		final Context mContext = v.getContext();
		AlertDialog.Builder alertDialogConfirmDelete = new AlertDialog.Builder(
				v.getContext());
		alertDialogConfirmDelete.setMessage(mContext
				.getString(R.string.delete_question)
				+ " '"
				+ revision.getParenIssue().getTitle() + "' ?");
		alertDialogConfirmDelete
				.setNegativeButton(R.string.cancel_button, null);
		alertDialogConfirmDelete.setPositiveButton(R.string.button_delete,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						RevisionStateManager.getInstance(mContext).setState(
								revision.getId().intValue(),
								RevisionInstallState.NOT_INTALLED);

						File installFolder = ApplicationFileHelper
								.getFileInstallRevisionFolder(revision.getId()
										.intValue());
						// File delInstallFolder = new File(installFolder
						// .getAbsolutePath() + "_del");
						// installFolder.renameTo(delInstallFolder);
						// // this.parentView.setProcessingIssue(issue);
						ApplicationFileHelper.deleteFileAsynk(installFolder);

						parentView.setVisibleOnState(
								RevisionInstallState.NOT_INTALLED, revision);

					}
				});
		alertDialogConfirmDelete.show();

	}
}