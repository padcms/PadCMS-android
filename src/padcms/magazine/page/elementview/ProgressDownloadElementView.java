package padcms.magazine.page.elementview;

import padcms.application11.R;
import padcms.bll.ApplicationSkinFactory;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class ProgressDownloadElementView extends RelativeLayout {
	public static final int SET_PROGRESS_MAX_LENGTH = 1;
	public static final int SET_PROGRESS = 2;
	public static final int HIDE_PROGRESS = 3;
	public static final int HIDE_PROGRESS_FAST = 5;
	public static final int SHOW_PROGRESS = 4;
	private boolean isShowing;
	private View elementViewContent;

	private ProgressBar progressBar;
	// private TextView textViewTitle;
	// private TextView textViewTitleState;
	private int maxValueProgress;

	public ProgressDownloadElementView(Context context) {
		super(context);
		elementViewContent = LayoutInflater.from(context).inflate(
				R.layout.progress_download_layout, this, true);
		// textViewTitleState = (TextView) elementViewContent
		// .findViewById(R.id.textViewTitleState);
		// textViewTitle = (TextView) elementViewContent
		// .findViewById(R.id.textViewTitle);
		progressBar = (ProgressBar) elementViewContent
				.findViewById(R.id.ProgressBarDownload);
		// elementViewContent .setVisibility(INVISIBLE);
	}

	public void setTitle(String title) {
		// textViewTitle.setText(title);
	}

	public void showProgress() {

		showView();
	}

	public void hideProgressAnimate() {
		progressBar.setIndeterminate(true);
		if (isShowing) {
			ApplicationSkinFactory.animateHide(this);
			isShowing = false;
		}
	}

	public void hideFastView() {
		isShowing = false;
		progressBar.setIndeterminate(true);
		ApplicationSkinFactory.animateHideFast(this);

	}

	public void setProgressValue(int valueProgress) {
		// progressBar.setMax(maxValueProgress);
		progressBar.setIndeterminate(false);
		progressBar.setProgress(valueProgress);
	}

	public void setMaxValue(int maxValueProgress) {
		progressBar.setIndeterminate(false);
		progressBar.setMax(maxValueProgress);
		progressBar.setProgress(0);
	}

	public void showView() {
		if (!isShowing) {
			bringToFront();
			ApplicationSkinFactory.animateShow(this);
			isShowing = true;
		}
	}

	// private Bundle bundelUpdateProgress;
	//
	// // public void setProgressParamsByBundel() {
	// int message = bundelUpdateProgress.getInt("message");
	// switch (message) {
	// case SET_PROGRESS_MAX_LENGTH:
	// // textViewTitleState.setText(progressBar.getContext().getString(
	// // R.string.title_download));
	// setMaxValue();
	//
	// break;
	// case SET_PROGRESS:
	// // textViewTitleState.setText(progressBar.getContext().getString(
	// // R.string.title_download));
	// setProgressValue();
	//
	// break;
	// case HIDE_PROGRESS:
	// // textViewTitleState.setText(progressBar.getContext().getString(
	// // R.string.title_waiting));
	// hideProgressAnimate();
	//
	// break;
	// case HIDE_PROGRESS_FAST:
	// hideProgressAnimate();
	//
	// break;
	// case SHOW_PROGRESS:
	// // textViewTitleState.setText(progressBar.getContext().getString(
	// // R.string.title_waiting));
	// showProgress();
	//
	// break;
	//
	// default:
	// break;
	// }
	//
	// }

	// public void setProgressParamsByBundel(final Bundle bundel) {
	// bundelUpdateProgress = bundel;
	// int message = bundelUpdateProgress.getInt("message");
	// if (message == SET_PROGRESS_MAX_LENGTH) {
	// maxValueProgress = bundel.getInt("value");
	// }
	//
	// Message msg = new Message();
	// msg.obj = this;
	// handlerUpdateProgress.sendMessage(msg);
	//
	// }

	// public static Handler handlerUpdateProgress = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// ProgressDownloadElementView elementviewProgress =
	// (ProgressDownloadElementView) msg.obj;
	// elementviewProgress.setProgressParamsByBundel();
	// };
	// };

}
