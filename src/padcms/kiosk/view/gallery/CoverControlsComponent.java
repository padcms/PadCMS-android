package padcms.kiosk.view.gallery;

import java.io.File;

import padcms.application11.R;
import padcms.bll.ApplicationFileHelper;
import padcms.dao.application.bean.Revision;
import padcms.dao.factory.DataStorageFactory;
import padcms.kiosk.RevisionStateManager;
import padcms.kiosk.RevisionStateManager.RevisionInstallState;
import padcms.kiosk.view.BasicViewController;
import padcms.kiosk.view.action.InstallerController;
import padcms.kiosk.view.adapter.GalleryAdapter;
import padcms.kiosk.view.listener.DeleteButtonController;
import padcms.kiosk.view.listener.InstallButtonController;
import padcms.magazine.service.RevisionServiceController;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CoverControlsComponent extends RelativeLayout implements
		BasicViewController, InstallerController.OnStateInstallChangeListener,
		InstallerController.OnProgressInstallChangeListener {

	private InstallerController installThread;
	private TextView titleTxt;
	private TextView stateTxt;
	private ImageButton installButton;
	private ImageButton deleteButton;
	private ProgressBar mProgress;
	private TextView textViewPercents;
	private RelativeLayout progressbarConteiner;

	private Revision currentRevision;
	private Revision processingRevision;

	private InstallButtonController installButtonLisner;

	private Gallery gallary;

	/**
	 * Allocates new <code>CoverControlsComponent</code> object.
	 * 
	 * @param context
	 * @param attrs
	 */
	public CoverControlsComponent(Context context, AttributeSet attrs) {
		super(context, attrs);

		((Activity) context).getLayoutInflater().inflate(
				R.layout.kiosk_buttons, this, true);

		initLayoutControls(context);
		installButtonLisner = new InstallButtonController(context, this);
		this.installButton.setOnClickListener(installButtonLisner);
		this.deleteButton.setOnClickListener(new DeleteButtonController(this));
	}

	public void setGallaryView(Gallery gallary) {
		this.gallary = gallary;
	}

	public Gallery getGallaryView() {
		return gallary;
	}

	public InstallButtonController getOpenButtonLisner() {
		return installButtonLisner;
	}

	public ImageButton getFirstButton() {
		return installButton;
	}

	public void setFirstButton(ImageButton firstButton) {
		this.installButton = firstButton;
	}

	public void setCurrentRevision(Revision revision) {

		currentRevision = revision;

		setTitle(currentRevision.getParenIssue().getTitle());
		installButton.setTag(currentRevision);
		deleteButton.setTag(currentRevision);

		if (currentRevision != null) {

			setVisibleOnState(RevisionStateManager.getInstance(getContext())
					.getState(currentRevision.getId().intValue()),
					currentRevision);

		} else {

			installButton.setVisibility(View.GONE);

		}

	}

	public Revision getCurrenRevision() {
		return currentRevision;
	}

	@Override
	public void setVisibleOnState(Revision revision) {
		setVisibleOnState(getRevisionState(revision), revision);
	}

	@Override
	public void setVisibleOnState(RevisionInstallState state, Revision revision) {
		installButton.setVisibility(View.VISIBLE);
		switch (state) {

		case NOT_INTALLED:
			setVisibleStateReadyToInstall(revision);
			break;

		case INSTALLING:
			setVisibleStateCancelInstall(revision);
			break;

		case INSTALLED:
			setVisibleStateReadyToOpen(revision);
			break;
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

	/**
	 * Sets text on firstButton "Install", secondButton invisible, sets
	 * progressBar(and reset it) and stateTxt invisible.
	 */

	private void setVisibleStateReadyToInstall(Revision revision) {
		if (revision == currentRevision) {
			((Activity) getContext()).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					installButton
							.setBackgroundResource(R.drawable.open_cover_button_selector);

					deleteButton.setVisibility(View.GONE);
					setVisibilityProgressBar(false);

					resetProgress();

					setStateTitleVisible(false);
					installButton.setClickable(true);

					deleteButton.setClickable(false);

				}
			});

		}
	}

	/**
	 * Sets text on firstButton "Cancel", secondButton invisible, restores
	 * progress on progressBar and sets stateTxt visible and text on stateTxt
	 * "Dowloading...".
	 */

	private void setVisibleStateCancelInstall(Revision revision) {
		if (revision == currentRevision) {
			((Activity) getContext()).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// setState(R.string.title_download);
					installButton
							.setBackgroundResource(R.drawable.download_cover_button_selector);

					deleteButton.setVisibility(View.GONE);
					setVisibilityProgressBar(true);

					setStateTitleVisible(true);
					installButton.setClickable(true);
					deleteButton.setClickable(false);
				}
			});

		}
	}

	private void setVisibleStateReadyToOpen(Revision revision) {
		if (revision == currentRevision) {
			((Activity) getContext()).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					installButton
							.setBackgroundResource(R.drawable.open_cover_button_selector);
					deleteButton.setVisibility(View.VISIBLE);
					deleteButton
							.setBackgroundResource(R.drawable.delete_cover_button_selector);

					setVisibilityProgressBar(false);
					setStateTitleVisible(false);

					installButton.setClickable(true);
					deleteButton.setClickable(true);

				}
			});

		}
	}

	@Override
	public void setVisibleStateOpening(Revision revision) {
		if (revision.equals(currentRevision)) {
			gallary.setOnTouchListener(frozenTouchLIstner);

			installButton.setClickable(false);
			deleteButton.setClickable(false);
			((GalleryAdapter) gallary.getAdapter()).setZoomForCover(revision);

		}

	}

	/**
	 * @return issue which is processed.
	 */
	@Override
	public Revision getProcessingRevision() {
		return this.processingRevision;
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
			setState(R.string.title_download);
		}
	}

	/**
	 * Interrupts task threads and prepare GUI.
	 */
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

	/**
	 * Sets progressBar visible, sets progress to zero, sets progress in
	 * percents text to zero (0%).
	 */
	@Override
	public void resetProgress() {
		mProgress.setVisibility(View.VISIBLE);
		mProgress.setProgress(0);
		StringBuilder sb = new StringBuilder();
		sb.append("0%");
		textViewPercents.setText(sb.subSequence(0, sb.length()));
	}

	/**
	 * Sets progressBar max.
	 * 
	 * @param max
	 *            is value of progressBar maximum.
	 */
	@Override
	public void setProgressBarMax(int max) {
		mProgress.setMax(max);
	}

	/**
	 * Updates progressBar and text describing it in percents.
	 */
	@Override
	public void updateProgressWith(long current) {

		if (current > 0) {
			mProgress.setProgress((int) current);

			double partOfLoad = (double) current / mProgress.getMax();
			double partOfLoadInPersantDouble = partOfLoad * 100;
			Long partOfLoadInPersants = (long) partOfLoadInPersantDouble;
			StringBuilder sb = new StringBuilder(
					partOfLoadInPersants.toString());
			sb.append("%");
			textViewPercents.setText(sb.subSequence(0, sb.length()));
		}
	}

	/**
	 * Sets text and show Toast with error message.
	 */
	@Override
	public void setErrMsg(int resId) {
		String message = getContext().getString(resId);
		Toast.makeText(getContext(), message, 2000).show();
	}

	/**
	 * Sets progressBar visible and when sets it invisible sets progress to
	 * zero.
	 * 
	 * @param isVisible
	 */
	public void setVisibilityProgressBar(boolean isVisible) {
		if (isVisible) {
			progressbarConteiner.setVisibility(View.VISIBLE);
		} else {
			progressbarConteiner.setVisibility(View.INVISIBLE);
			mProgress.setProgress(0);
		}
	}

	public void setFirstButtonName(int resId) {
		this.installButton.setTag(getContext().getString(resId));
	}

	public void setTitle(String title) {
		titleTxt.setText(title);
	}

	public void setState(int resId) {
		stateTxt.setText(resId);
	}

	public void setStateTitleVisible(boolean flag) {
		if (flag)
			stateTxt.setVisibility(View.VISIBLE);
		else
			stateTxt.setVisibility(View.INVISIBLE);
	}

	private void initLayoutControls(Context context) {
		installButton = (ImageButton) findViewById(R.id.buttonFirst);
		deleteButton = (ImageButton) findViewById(R.id.buttonSecond);
		titleTxt = (TextView) findViewById(R.id.TextViewTitle);
		stateTxt = (TextView) findViewById(R.id.TextViewState);
		mProgress = (ProgressBar) findViewById(R.id.progressbar_id);
		textViewPercents = (TextView) findViewById(R.id.TextViewPercents);
		progressbarConteiner = (RelativeLayout) findViewById(R.id.progressbarConteiner);
	}

	@Override
	public void refresh() {
		if (gallary != null)
			gallary.setOnTouchListener(enabaledTouchLIstner);
		if (currentRevision != null)
			this.setCurrentRevision(currentRevision);
	}

	private OnTouchListener frozenTouchLIstner = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			return true;
		}
	};
	private OnTouchListener enabaledTouchLIstner = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			v.onTouchEvent(event);
			return false;
		}
	};

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
	public void onStateChenged(RevisionInstallState state, Revision revision) {
		setRevisionState(revision, state);
		if (currentRevision == revision)
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
		if (currentRevision == revision)
			((Activity) getContext()).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					updateProgressWith(progress);
				}
			});

	}

	@Override
	public void onProgressInstallChenged(Revision revision, final int progress) {
		if (currentRevision == revision)
			((Activity) getContext()).runOnUiThread(new Runnable() {
				@Override
				public void run() {

					updateProgressWith(progress);
				}
			});

	}

	@Override
	public void onProgressInstallSetMax(Revision revision, final int progressMax) {
		if (currentRevision == revision)
			((Activity) getContext()).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					setState(R.string.title_install);
					setProgressBarMax(progressMax);
				}
			});

	}

	@Override
	public void onProgressDownloadSetMax(Revision revision,
			final int progressMax) {
		if (currentRevision == revision)
			((Activity) getContext()).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					setState(R.string.title_download);
					setProgressBarMax(progressMax);
				}
			});
	}

	@Override
	public void onProgressInstallFinished(final Revision revision) {
		((Activity) getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
//				DataStorageFactory.getInstance(getContext()).initRevisionPages(
//						revision, new Handler() {
//							@Override
//							public void handleMessage(Message msg) {
//								super.handleMessage(msg);
//								if (msg.what == 1) {
//
//									RevisionServiceController service = new RevisionServiceController(
//											getContext());
//									service.doBindService(revision.getId()
//											.intValue());
//								}
//							}
//						});
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
