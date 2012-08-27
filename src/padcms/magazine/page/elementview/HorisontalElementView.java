package padcms.magazine.page.elementview;

import padcms.magazine.controls.imagecontroller.ImageViewTouch;
import padcms.magazine.factory.ResourceFactory;
import padcms.magazine.factory.ResourceResolutionHelper;
import padcms.magazine.page.HorisontalPageView;
import padcms.magazine.page.State;
import padcms.magazine.resource.ImageViewZoomController;
import padcms.magazine.resource.ResourceController;
import padcms.magazine.resource.ResourceController.onUpdateProgress;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * background
 * 
 * Background is a PDF, JPEG, PNG or an HTML/CSS/Images. It takes whole IPad
 * screen.
 * 
 * resource (string) relative path to the resource
 */
public class HorisontalElementView extends BaseElementView  {

	private ImageViewTouch scaledImageView;
	// private RelativeLayout backgroundContainer;
	// private HorisontalPageView parentPageView;
	// private ImageView bgImageView;
	// protected ResourceController resourceController;
	protected ResourceController resourceControllerThnbnail;
//	private ProgressDownloadElementView progressDownloadElementView;

	// private State state = State.RELEASE;
	// Handler handler = new Handler();

	public HorisontalElementView(HorisontalPageView parentPageView,
			String pathToSource) {
		super(parentPageView, null);
		// this.parentPageView = parentPageView;

		resourceController = new ImageViewZoomController(
				parentPageView.getContext(), pathToSource, "1024-768");
		resourceController.setOnUpdateProgress(this);
		resourceController.setBaseElemetView(this);

		// resourceController.setOnDrowBitmapListner(new onDrowBitmapListner() {
		// @Override
		// public void onInvalidateView() {
		// scaledImageView.invalidate();
		// }
		//
		// @Override
		// public void onDrow(final Bitmap drowBitmap) {
		// if (scaledImageView != null) {
		// handler.post(new Runnable() {
		// @Override
		// public void run() {
		//
		// if (drowBitmap != null && !drowBitmap.isRecycled()) {
		// // backgroundView.setImageBitmap(bitmap);
		// ResourceResolutionHelper bitmapResolution = ResourceResolutionHelper
		// .getBitmapResolutionHorisontal(
		// scaledImageView.getContext(),
		// drowBitmap.getWidth(),
		// drowBitmap.getHeight());
		//
		// scaledImageView.getLayoutParams().width = bitmapResolution.width;
		// scaledImageView.getLayoutParams().height = bitmapResolution.height;
		// if (scaledImageView.isBitmapSet())
		// scaledImageView.setImageBitmapReset(
		// drowBitmap, false);
		// else
		// scaledImageView.setImageBitmapReset(
		// drowBitmap, true);
		//
		// resourceController.releaseInctiveResources();
		// scaledImageView.setBackgroundColor(Color.WHITE);
		// } else {
		// scaledImageView.setImageBitmap(null);
		// scaledImageView
		// .setBackgroundColor(Color.TRANSPARENT);
		// resourceController.releaseInctiveResources();
		// }
		//
		// }
		// });
		//
		// }
		// }
		// });

		// 2048-1536
		resourceControllerThnbnail = new ImageViewZoomController(
				parentPageView.getContext(), pathToSource, "204-153");

		resourceControllerThnbnail.setOnUpdateProgress(this);
		resourceControllerThnbnail.setBaseElemetView(this);
		// resourceControllerThnbnail
		// .setOnDrowBitmapListner(new onDrowBitmapListner() {
		// @Override
		// public void onInvalidateView() {
		// scaledImageView.invalidate();
		// }
		//
		// @Override
		// public void onDrow(final Bitmap drowBitmap) {
		// if (scaledImageView != null) {
		//
		// handler.post(new Runnable() {
		// @Override
		// public void run() {
		//
		// if (drowBitmap != null
		// && !drowBitmap.isRecycled()) {
		//
		// // if (scaledImageView.isBitmapSet())
		// // scaledImageView.setImageBitmapReset(bitmap,
		// // false);
		// // else
		// scaledImageView.setImageBitmapReset(
		// drowBitmap, true);
		//
		// ResourceResolutionHelper bitmapResolution = ResourceResolutionHelper
		// .getBitmapResolutionHorisontal(
		// scaledImageView
		// .getContext(),
		// drowBitmap.getWidth(),
		// drowBitmap.getHeight());
		//
		// scaledImageView.getLayoutParams().width = bitmapResolution.width;
		// scaledImageView.getLayoutParams().height = bitmapResolution.height;
		// scaledImageView
		// .setBackgroundColor(Color.WHITE);
		// resourceControllerThnbnail
		// .releaseInctiveResources();
		// } else {
		// scaledImageView.setImageBitmap(null);
		// scaledImageView
		// .setBackgroundColor(Color.TRANSPARENT);
		// resourceControllerThnbnail
		// .releaseInctiveResources();
		// }
		//
		// }
		// });
		//
		// }
		//
		// }
		// });

	}
//
//	public ProgressDownloadElementView getProgressDownloadElementView() {
//		return progressDownloadElementView;
//	}
//
//	public void setProgressDownloadElementView(
//			ProgressDownloadElementView progressDownloadElementView) {
//		this.progressDownloadElementView = progressDownloadElementView;
//	}

	// public HorisontalPageView getParentPageView() {
	// return parentPageView;
	// }

	public View getView(Context context) {
		if (scaledImageView == null) {
			// RelativeLayout.LayoutParams bgImageViewParams = new
			// RelativeLayout.LayoutParams(
			// -2, -2);
			// bgImageViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);

			// bgImageView = new ImageViewResources(context);
			// bgImageView.setScaleType(ScaleType.FIT_XY);
			// bgImageView.setLayoutParams(bgImageViewParams);
			// bgImageView.setBackgroundColor(Color.WHITE);

			RelativeLayout.LayoutParams scaledImageViewParams = new RelativeLayout.LayoutParams(
					-1, -1);
			scaledImageViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);

			scaledImageView = new ImageViewTouch(context, null);// new
			scaledImageView.setLayoutParams(scaledImageViewParams);

			// ((ImageViewZoomController) resourceController)
			// .setTargetImagetView(scaledImageView);
			// ((ImageViewZoomController) resourceControllerThnbnail)
			// .setTargetImagetView(scaledImageView);
			// backgroundContainer = new RelativeLayout(context);
			// backgroundContainer.addView(bgImageView);

			// backgroundContainer.addView(scaledImageView);
			//
			// RelativeLayout.LayoutParams backgroundContainerLayoutParams = new
			// RelativeLayout.LayoutParams(
			// -2, -2);
			// backgroundContainerLayoutParams
			// .addRule(RelativeLayout.CENTER_IN_PARENT);
			// // backgroundContainer.setBackgroundColor(Color.WHITE);
			// backgroundContainer
			// .setLayoutParams(backgroundContainerLayoutParams);

		}
		return scaledImageView;
	}

	public void setState(State state) {

		// if ((this.state == State.STOP || this.state == State.ACTIVE)
		// && state == State.STOP)
		// scaledImageView.setImageState(state);
		// else {

		if (state == State.ACTIVE) {
			// scaledImageView.setImageState(state);
			resourceController.setState(state);
			ResourceFactory.processResourceController(resourceController);
		} else if (state == State.DISACTIVE) {
			resourceController.setState(State.RELEASE);
			resourceControllerThnbnail.setState(State.ACTIVE);
			ResourceFactory
					.processResourceController(resourceControllerThnbnail);
		} else if (state == State.RELEASE) {
			// scaledImageView.setImageState(state);
			resourceController.setState(State.RELEASE);
			resourceControllerThnbnail.setState(State.RELEASE);
		} else if (state == State.DOWNLOAD) {
			resourceController.setState(State.DOWNLOAD);
			resourceControllerThnbnail.setState(State.DOWNLOAD);
			ResourceFactory
					.processResourceController(resourceControllerThnbnail);
			ResourceFactory.processResourceController(resourceController);
		}
		// }
		super.setState(state);
		// resourceController.setState(state);
		// if (state != State.RELEASE)
		// ResourceFactory.processResourceController(resourceController);

	}

	// @Override
	// public void showProgress() {
	// if (getProgressDownloadElementView() != null) {
	// runInUI(new Runnable() {
	// public void run() {
	// getProgressDownloadElementView().showProgress();
	// }
	// });
	// }
	// }
	//
	// @Override
	// public void startProgress(final int maxValue) {
	// if (getProgressDownloadElementView() != null) {
	// runInUI(new Runnable() {
	// public void run() {
	// getProgressDownloadElementView().setMaxValue(maxValue);
	// }
	// });
	// }
	// }
	//
	// @Override
	// public void setProgress(final int valueProgress) {
	// if (getProgressDownloadElementView() != null) {
	// runInUI(new Runnable() {
	// public void run() {
	// getProgressDownloadElementView().setProgressValue(
	// valueProgress);
	// }
	// });
	// }
	// }
	//
	// @Override
	// public void hideProgress() {
	//
	// if (getProgressDownloadElementView() != null) {
	// runInUI(new Runnable() {
	// public void run() {
	// getProgressDownloadElementView().hideFastView();
	// }
	// });
	//
	// }
	//
	// }

	// public void runInUI(Runnable runnable) {
	// if (parentPageView != null)
	// ((Activity) parentPageView.getContext()).runOnUiThread(runnable);
	// }
	@Override
	public ResourceResolutionHelper getResolutionForController(Bitmap bitmap) {

		return ResourceResolutionHelper.getBitmapResolutionHorisontal(
				getParentPageView().getContext(), bitmap.getWidth(),
				bitmap.getHeight());
	}

	@Override
	public View getShowingView() {

		return scaledImageView;
	}
}
