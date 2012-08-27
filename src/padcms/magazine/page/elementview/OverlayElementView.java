package padcms.magazine.page.elementview;

import padcms.dao.issue.bean.Element;
import padcms.magazine.controls.imagecontroller.ImageViewResources;
import padcms.magazine.factory.ResourceFactory;
import padcms.magazine.page.BasePageView;
import padcms.magazine.page.State;
import padcms.magazine.resource.ImageViewController;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

/**
 * overlay
 * 
 * Overlay content is displayed on the semi-transparent layer on top of the
 * background content when 'legend' button is clicked.
 * 
 * resource (string) relative path to the resource
 */
public class OverlayElementView extends BaseElementView {
	private ImageView overlayView;
	private RelativeLayout overlayContainer;

	public OverlayElementView(BasePageView parentPageView, Element element) {
		super(parentPageView, element);
		resourceController = new ImageViewController(
				parentPageView.getContext(), resourcePathStr);
		resourceController.setOnUpdateProgress(this);
		resourceController.setBaseElemetView(this);
		// resourceController.setOnDrowBitmapListner(this);
	}

	// @Override
	// public void onInvalidateView() {
	// overlayView.invalidate();
	// }
	// @Override
	// public void onDrow(final Bitmap drowBitmap) {
	// if (overlayView != null) {
	//
	// runInUI(new Runnable() {
	// @Override
	// public void run() {
	// overlayView.setTag(drowBitmap);
	// if (drowBitmap != null && !drowBitmap.isRecycled()) {
	// overlayView.setImageBitmap(drowBitmap);
	//
	// ResourceResolutionHelper bitmapResolution = ResourceResolutionHelper
	// .getBitmapResolution(overlayView.getContext(),
	// drowBitmap.getWidth(),
	// drowBitmap.getHeight());
	//
	// overlayView.getLayoutParams().width = bitmapResolution.width;
	// overlayView.getLayoutParams().height = bitmapResolution.height;
	//
	// resourceController.releaseInctiveResources();
	// } else {
	// overlayView.setImageBitmap(null);
	//
	// resourceController.releaseInctiveResources();
	// }
	//
	// }
	// });
	//
	// }
	// }

	@Override
	public void setElementBackgroundColor(int elementBackgroundColor) {
		super.setElementBackgroundColor(elementBackgroundColor);
		if (overlayView != null) {
			overlayView.setBackgroundColor(elementBackgroundColor);
		}
	}

	@Override
	public View getView(Context context) {
		if (overlayContainer == null) {

			overlayContainer = new RelativeLayout(context);
			overlayView = new ImageViewResources(context);

			RelativeLayout.LayoutParams backLayoutParams = new RelativeLayout.LayoutParams(
					-1, -2);
			backLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

			overlayView.setLayoutParams(backLayoutParams);
			overlayView.setScaleType(ScaleType.FIT_XY);

			overlayView.setBackgroundColor(getElementBackgroundColor());

			overlayContainer.addView(overlayView);

			RelativeLayout.LayoutParams backgroundContainerLayoutParams = new RelativeLayout.LayoutParams(
					-1, -2);
			backgroundContainerLayoutParams
					.addRule(RelativeLayout.CENTER_IN_PARENT);
			overlayContainer.setLayoutParams(backgroundContainerLayoutParams);

			initViewWithActiveZone(overlayContainer);
			// ((ImageViewController) resourceController)
			// .setTargetImagetView(overlayView);

		}
		return overlayContainer;
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
		if (overlayContainer != null)
			overlayContainer.removeAllViews();
		overlayContainer = null;

	}

	@Override
	public View getShowingView() {
		return overlayView;
	}

}
