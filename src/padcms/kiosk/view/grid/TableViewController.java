package padcms.kiosk.view.grid;

import java.io.File;

import padcms.application11.R;
import padcms.bll.ApplicationFileHelper;
import padcms.dao.application.bean.Revision;
import padcms.kiosk.RevisionStateManager;
import padcms.kiosk.RevisionStateManager.RevisionInstallState;
import padcms.kiosk.view.BasicViewController;
import padcms.kiosk.view.action.InstallerController;
import padcms.kiosk.view.adapter.TableViewAdapter;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Object of <code>GridViewController</code> provides the interaction elements
 * of the interface with the threads of user tasks. Elements of View are
 * arranged in the form of a GridView.
 */

public class TableViewController extends TableLayout implements
		BasicViewController, InstallerController.OnStateInstallChangeListener,
		InstallerController.OnProgressInstallChangeListener {

	private InstallerController installThread;
	private Revision processingRevision;
	private View processingView;
	private TableViewAdapter tableViewAdapter;
	private int columnsCount = 1;

	/**
	 * Allocates new <code>GridViewController</code> object.
	 * 
	 * @param context
	 */
	public TableViewController(Context context) {
		super(context);
	}

	/**
	 * Allocates new <code>GridViewController</code> object.
	 * 
	 * @param context
	 */
	public TableViewController(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @return issue which is processed.
	 */
	@Override
	public Revision getProcessingRevision() {
		if (this.processingRevision != null) {
			return (Revision) this.processingRevision;
		} else {
			return null;
		}
	}

	@Override
	public void startInstall(Revision revision) {
		processingRevision = revision;
		installThread = new InstallerController(getContext(), revision);
		installThread.setOnProgressInstallChangeListener(this);
		installThread.setOnStateInstallChangeListener(this);
		installThread.start();
	}

	@Override
	public void interruptInstall(Revision revision) {
		if (isInstallerALive()) {
			this.installThread.interrupt();
			this.installThread = null;
		}

	}

	/**
	 * @return <code>true</code> if downloadThread is not <code>null</code> and
	 *         alive, <code>false</code> in another case.
	 */
	@Override
	public boolean isInstallerALive() {
		if (installThread != null && installThread.isAlive()
				&& !this.installThread.isInterrupted()) {
			return true;
		}
		return false;
	}

	public InstallerController getInstallThread() {
		return installThread;
	}

	public void setInstallThread(InstallerController installThread) {
		this.installThread = installThread;
		if (installThread != null) {
			this.installThread.setOnProgressInstallChangeListener(this);
			this.installThread.setOnStateInstallChangeListener(this);
			resetProgress();
			setProgressBarMax(installThread.getContentLength());

		}
	}

	@Override
	public void setVisibleOnState(Revision revision) {
		RevisionInstallState state = RevisionStateManager.getInstance(
				getContext()).getState(revision.getId().intValue());
		setVisibleOnState(state, revision);
	}

	@Override
	public void setVisibleOnState(RevisionInstallState state, Revision revision) {

		View revisionView = tableViewAdapter.findViewInGridByRevision(revision);
		setVisibleStateForView(state, revision, revisionView);
	}

	public void setVisibleStateForView(final RevisionInstallState state,
			final Revision revision, final View revisionView) {
		((Activity) getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {

				switch (state) {
				case NOT_INTALLED:
					setVisibleStateReadyToInstall(revision, revisionView);
					break;

				case INSTALLING:
					setVisibleStateCancelInstall(revision, revisionView);
					break;

				case INSTALLED:
					setVisibleStateReadyToOpen(revision, revisionView);
					break;
				}
			}
		});

	}

	public void setVisibleStateReadyToInstall(Revision revision,
			View revisionView) {
		if (revisionView != null) {

			TextView stateTxt = (TextView) revisionView
					.findViewById(R.id.textViewStateInGrid);
			Button firstButton = (Button) revisionView
					.findViewById(R.id.buttonFirstInGrid);
			Button downloadButton = (Button) revisionView
					.findViewById(R.id.buttonDownload);
			Button cancelButton = (Button) revisionView
					.findViewById(R.id.buttonCancel);
			Button instalButton = (Button) revisionView
					.findViewById(R.id.buttonInstall);
			Button openButton = (Button) revisionView
					.findViewById(R.id.buttonOpen);
			Button secondButton = (Button) revisionView
					.findViewById(R.id.buttonSecondInGrid);
			ProgressBar mProgress = (ProgressBar) revisionView
					.findViewById(R.id.progressbar_id_in_grid);
			// TextView textViewPercents = (TextView)
			// view.findViewById(R.id.textViewPercentsInGrid);

			firstButton.setVisibility(View.GONE);
			downloadButton.setVisibility(View.GONE);
			cancelButton.setVisibility(View.GONE);
			instalButton.setVisibility(View.VISIBLE);
			openButton.setVisibility(View.GONE);
			secondButton.setVisibility(View.INVISIBLE);
			mProgress.setVisibility(View.INVISIBLE);// ======================
			// resetProgress(mProgress, textViewPercents);
			stateTxt.setVisibility(View.INVISIBLE);
			mProgress.setProgress(0);
		}

	}

	public void setProcessingStateText(int textID) {
		if (processingView != null) {
			TextView stateTxt = (TextView) processingView
					.findViewById(R.id.textViewStateInGrid);
			stateTxt.setText(R.string.title_download);
			stateTxt.setVisibility(View.VISIBLE);
		}
	}

	public void setVisibleStateCancelInstall(Revision revision,
			View revisionView) {

		if (revisionView != null) {
			processingView = revisionView;
			processingRevision = revision;
			TextView stateTxt = (TextView) revisionView
					.findViewById(R.id.textViewStateInGrid);
			Button firstButton = (Button) revisionView
					.findViewById(R.id.buttonFirstInGrid);
			Button downloadButton = (Button) revisionView
					.findViewById(R.id.buttonDownload);
			Button cancelButton = (Button) revisionView
					.findViewById(R.id.buttonCancel);
			Button instalButton = (Button) revisionView
					.findViewById(R.id.buttonInstall);
			Button openButton = (Button) revisionView
					.findViewById(R.id.buttonOpen);
			Button secondButton = (Button) revisionView
					.findViewById(R.id.buttonSecondInGrid);
			ProgressBar mProgress = (ProgressBar) revisionView
					.findViewById(R.id.progressbar_id_in_grid);
			// TextView textViewPercents = (TextView)
			// view.findViewById(R.id.textViewPercentsInGrid);

			// stateTxt.setText(R.string.title_download);
			stateTxt.setVisibility(View.VISIBLE);

			firstButton.setVisibility(View.GONE);
			downloadButton.setVisibility(View.GONE);
			cancelButton.setVisibility(View.VISIBLE);
			instalButton.setVisibility(View.GONE);
			openButton.setVisibility(View.GONE);
			secondButton.setVisibility(View.INVISIBLE);
			mProgress.setVisibility(View.VISIBLE);
			// textViewPercents.setVisibility(View.VISIBLE);
			// textViewPercents.setText(R.string.zero_percents);

		}
	}

	public void setVisibleStateReadyToOpen(Revision revision, View revisionView) {
		if (revisionView != null) {

			TextView stateTxt = (TextView) revisionView
					.findViewById(R.id.textViewStateInGrid);
			Button firstButton = (Button) revisionView
					.findViewById(R.id.buttonFirstInGrid);
			Button downloadButton = (Button) revisionView
					.findViewById(R.id.buttonDownload);
			Button cancelButton = (Button) revisionView
					.findViewById(R.id.buttonCancel);
			Button instalButton = (Button) revisionView
					.findViewById(R.id.buttonInstall);
			Button openButton = (Button) revisionView
					.findViewById(R.id.buttonOpen);
			Button secondButton = (Button) revisionView
					.findViewById(R.id.buttonSecondInGrid);
			ProgressBar mProgress = (ProgressBar) revisionView
					.findViewById(R.id.progressbar_id_in_grid);
			// TextView textViewPercents = (TextView)
			// view.findViewById(R.id.textViewPercentsInGrid);

			firstButton.setVisibility(View.GONE);
			downloadButton.setVisibility(View.GONE);
			cancelButton.setVisibility(View.GONE);
			instalButton.setVisibility(View.GONE);
			openButton.setVisibility(View.VISIBLE);
			secondButton.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.INVISIBLE);// =================
			resetProgress();
			// textViewPercents.setVisibility(View.INVISIBLE);
			stateTxt.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * Sets text on firstButton "Download" and background for it, sets
	 * secondButton, stateTxt and probressBar invisible, reset progressBar.
	 */
	public void setVisibleStateReadyToInstalling(Revision revision,
			View revisionView) {

		if (revisionView != null) {
			TextView stateTxt = (TextView) revisionView
					.findViewById(R.id.textViewStateInGrid);
			Button firstButton = (Button) revisionView
					.findViewById(R.id.buttonFirstInGrid);
			Button downloadButton = (Button) revisionView
					.findViewById(R.id.buttonDownload);
			Button cancelButton = (Button) revisionView
					.findViewById(R.id.buttonCancel);
			Button instalButton = (Button) revisionView
					.findViewById(R.id.buttonInstall);
			Button openButton = (Button) revisionView
					.findViewById(R.id.buttonOpen);
			Button secondButton = (Button) revisionView
					.findViewById(R.id.buttonSecondInGrid);

			ProgressBar mProgress = (ProgressBar) revisionView
					.findViewById(R.id.progressbar_id_in_grid);
			/*
			 * TextView textViewPercents = (TextView)
			 * view.findViewById(R.id.textViewPercentsInGrid);
			 */

			firstButton.setVisibility(View.GONE);
			downloadButton.setVisibility(View.VISIBLE);
			cancelButton.setVisibility(View.GONE);
			instalButton.setVisibility(View.GONE);
			openButton.setVisibility(View.GONE);
			secondButton.setVisibility(View.INVISIBLE);
			mProgress.setVisibility(View.INVISIBLE);// ================
			resetProgress();
			// textViewPercents.setVisibility(View.INVISIBLE);
			stateTxt.setVisibility(View.INVISIBLE);
		}

	}

	/**
	 * Sets progressBar visible, sets progress to zero
	 */
	@Override
	public void resetProgress() {
		if (this.processingView != null) {
			ProgressBar pb = (ProgressBar) processingView
					.findViewById(R.id.progressbar_id_in_grid);
			pb.setProgress(0);
		}
	}

	/**
	 * Sets progressBar max.
	 * 
	 * @param max
	 *            is value of progressBar maximum.
	 */
	@Override
	public void setProgressBarMax(int max) {

		if (processingView != null) {
			ProgressBar pb = (ProgressBar) processingView
					.findViewById(R.id.progressbar_id_in_grid);
			// TextView percents = (TextView)
			// view.findViewById(R.id.textViewPercentsInGrid);
			pb.setMax(max);
			// percents.setText(R.string.zero_percents);
		}

	}

	/**
	 * Updates progressBar and text describing it in percents.
	 */
	@Override
	public void updateProgressWith(long current) {

		if (processingView != null) {
			ProgressBar mProgress = (ProgressBar) processingView
					.findViewById(R.id.progressbar_id_in_grid);

			// long increm = current - mProgress.getProgress();
			if (current > 0) {
				// if (current > Integer.MAX_VALUE) {
				// while (current > Integer.MAX_VALUE) {
				// mProgress.incrementProgressBy(Integer.MAX_VALUE);
				// current -= Integer.MAX_VALUE;
				// }
				// }
				mProgress.setProgress((int) current);
				// mProgress.incrementProgressBy(diff)rogressBy((int) increm);
				processingView.invalidate();
				mProgress.invalidate();

			} else {
				Log.i("UPDATE PROGRESS", "CURRENT=" + current);
			}
		}
	}

	/**
	 * Set text and show Toast with error message.
	 */
	@Override
	public void setErrMsg(int resId) {
		String message = getContext().getString(resId);
		Toast.makeText(getContext(), message, 2000).show();
	}

	public void setProcessingRevision(Revision processingRevision) {
		this.processingRevision = processingRevision;
	}

	/**
	 * If processingIssue is't null updates visible state of processingView.
	 */

	@Override
	public void refresh() {
		Log.d("In GridviewController.refresh()",
				"In GridviewController.refresh()");
		if (this.processingRevision != null) {
			setVisibleOnState(processingRevision);
		}
	}

	public TableViewAdapter getTableViewAdapter() {
		return tableViewAdapter;
	}

	public void setTableViewAdapter(TableViewAdapter tableViewAdapter) {
		removeAllViews();
		this.tableViewAdapter = tableViewAdapter;
		tableViewAdapter.setTableViewContent(this, columnsCount);

	}

	public void updateDataAdapter() {
		if (tableViewAdapter != null) {
			removeAllViews();
			tableViewAdapter.setTableViewContent(this, columnsCount);
		}
	}

	public int getCoulumsCount() {
		return columnsCount;
	}

	public void setCoulumsCount(int columnsCount) {
		this.columnsCount = columnsCount;
	}

	@Override
	public RevisionInstallState getRevisionState(Revision revision) {

		return RevisionStateManager.getInstance(getContext()).getState(
				revision.getId().intValue());
	}

	@Override
	public void setRevisionState(Revision revision,
			RevisionInstallState revisionState) {
		RevisionStateManager.getInstance(getContext()).setState(
				revision.getId().intValue(), revisionState);

	}

	@Override
	public void setVisibleStateOpening(Revision revision) {

	}

	@Override
	public void onStateChenged(RevisionInstallState state, Revision revision) {
		RevisionStateManager.getInstance(getContext()).setState(
				revision.getId().intValue(), state);

		setVisibleOnState(state, revision);
	}

	@Override
	public void onErrorInstall(Revision revision) {
		// RevisionStateManager.getInstance(getContext()).setState(
		// revision.getId().intValue(), RevisionInstallState.NOT_INTALLED);
		// setVisibleOnState(RevisionInstallState.NOT_INTALLED, revision);
	}

	@Override
	public void onProgressDownloadChenged(Revision revision, final int progress) {
		((Activity) getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				updateProgressWith(progress);
			}
		});

	}

	@Override
	public void onProgressInstallChenged(Revision revision, final int progress) {
		((Activity) getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {

				updateProgressWith(progress);
			}
		});

	}

	@Override
	public void onProgressInstallSetMax(Revision revision, final int progressMax) {

		((Activity) getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				setProcessingStateText(R.string.title_install);
				setProgressBarMax(progressMax);
			}
		});

	}

	@Override
	public void onProgressDownloadSetMax(Revision revision,
			final int progressMax) {
		((Activity) getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {

				setProcessingStateText(R.string.title_download);
				setProgressBarMax(progressMax);
			}
		});
	}

	@Override
	public void onProgressInstallFinished(Revision revision) {
		((Activity) getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getContext(), "Install finished", 200).show();
			}
		});
	}

	@Override
	public void onProgressDownloadFinished(Revision revision) {
		((Activity) getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getContext(), "Download finished", 200).show();

			}
		});
	}

	@Override
	public void onInstallIterapted(final Revision revision) {
		((Activity) getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				interruptInstall(revision);
				RevisionStateManager.getInstance(getContext()).setState(
						revision.getId().intValue(),
						RevisionInstallState.NOT_INTALLED);
				File installFolder = ApplicationFileHelper
						.getFileInstallRevisionFolder(revision.getId()
								.intValue());
				ApplicationFileHelper.deleteFileAsynk(installFolder);

				setVisibleOnState(RevisionInstallState.NOT_INTALLED, revision);

				processingRevision = null;
			}
		});

	}

}
