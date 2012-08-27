package padcms.magazine.page.elementview;

import padcms.dao.issue.bean.Element;
import padcms.magazine.controls.imagecontroller.ImageViewGroup;
import padcms.magazine.factory.ResourceFactory;
import padcms.magazine.page.BasePageView;
import padcms.magazine.page.State;
import padcms.magazine.resource.PartedImageViewController;
import android.content.Context;
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
public class BackgroundElementView extends BaseElementView {

	private ImageViewGroup backgroundView;

	// private RelativeLayout backgroundContainer;

	public BackgroundElementView(BasePageView parentPageView, Element element) {
		super(parentPageView, element);

		resourceController = new PartedImageViewController(
				parentPageView.getContext(), resourcePathStr);
		// resourceController.setOnDrowBitmapListner(this);
		if (resourcePathStr != null && resourcePathStr.length() > 0)
			resourceController.setOnUpdateProgress(this);
		resourceController.setBaseElemetView(this);

	}

	public BackgroundElementView(BasePageView parentPageView,
			String resourcePathStr) {
		super(parentPageView, null);
		this.resourcePathStr = resourcePathStr;
		resourceController = new PartedImageViewController(
				parentPageView.getContext(), resourcePathStr);
		// resourceController.setOnDrowBitmapListner(this);
		if (resourcePathStr != null && resourcePathStr.length() > 0)
			resourceController.setOnUpdateProgress(this);
		
		resourceController.setBaseElemetView(this);

	}

	// @Override
	// public void onDrow(final Bitmap drowBitmap) {
	// if (backgroundView != null) {
	//
	// runInUI(new Runnable() {
	// @Override
	// public void run() {
	// if (drowBitmap != null && !drowBitmap.isRecycled()) {
	// backgroundView.setImageBitmap(drowBitmap);
	// backgroundView
	// .setBackgroundColor(getElementBackgroundColor());
	//
	// ResourceResolutionHelper bitmapResolution = ResourceResolutionHelper
	// .getBitmapResolution(
	// backgroundView.getContext(),
	// drowBitmap.getWidth(),
	// drowBitmap.getHeight());
	//
	// backgroundView.getLayoutParams().width = bitmapResolution.width;
	// backgroundView.getLayoutParams().height = bitmapResolution.height;
	//
	// resourceController.releaseInctiveResources();
	// } else {
	// backgroundView.setImageBitmap(null);
	// backgroundView.setBackgroundColor(Color.TRANSPARENT);
	// resourceController.releaseInctiveResources();
	// }
	//
	// }
	// });
	//
	// }
	// }
	// @Override
	// public void onInvalidateView() {
	// backgroundView.invalidate();
	// }

	@Override
	public void setElementBackgroundColor(int elementBackgroundColor) {
		super.setElementBackgroundColor(elementBackgroundColor);
		if (backgroundView != null) {
			backgroundView.setBackgroundColor(elementBackgroundColor);
		}
	}

	@Override
	public View getView(Context context) {
		if (backgroundView == null) {

			// backgroundContainer = new RelativeLayout(context);
			backgroundView = new ImageViewGroup(context);

			RelativeLayout.LayoutParams backLayoutParams = new RelativeLayout.LayoutParams(
					-1, -2);
			// backLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			backgroundView.setLayoutParams(backLayoutParams);
			// backgroundView.setScaleType(ScaleType.FIT_XY);

			backgroundView.setBackgroundColor(getElementBackgroundColor());

			// backgroundContainer.addView(backgroundView);

			// RelativeLayout.LayoutParams backgroundContainerLayoutParams = new
			// RelativeLayout.LayoutParams(
			// -1, -2);
			// backgroundContainerLayoutParams
			// .addRule(RelativeLayout.CENTER_IN_PARENT);
			// backgroundContainer
			// .setLayoutParams(backgroundContainerLayoutParams);

			initViewWithActiveZone(backgroundView);
			// ((PartedImageViewController) resourceController)
			// .setImageViewGroup(backgroundView);

		}
		return backgroundView;
	}

	@Override
	public void setState(State state) {

		resourceController.setState(state);
		if (getState() != state) {
			super.setState(state);
			if (state != State.RELEASE)
				ResourceFactory.processResourceController(resourceController);
		}
	}

	public void destroyElementView() {
		if (backgroundView != null)
			backgroundView.removeAllViews();
		backgroundView = null;

	}

	@Override
	public View getShowingView() {

		return backgroundView;
	}

}
