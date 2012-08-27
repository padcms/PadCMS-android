package padcms.magazine.menu.elementView;

import padcms.magazine.controls.imagecontroller.ImageViewResources;
import padcms.magazine.factory.ResourceFactory;
import padcms.magazine.factory.ResourceResolutionHelper;
import padcms.magazine.page.BasePageView;
import padcms.magazine.page.State;
import padcms.magazine.page.elementview.BaseElementView;
import padcms.magazine.resource.ImageViewController;
import padcms.magazine.resource.MenuImageViewController;
import padcms.magazine.resource.ResourceController;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class VerticalSummaryMenuElementView extends BaseElementView {
	private ImageView menuImageView;

	// protected ResourceController resourceController;
	// private State state = State.RELEASE;
	// private BasePageView parentPageView;
	// Handler handler = new Handler();
	private int menuHeight;

	public VerticalSummaryMenuElementView(BasePageView parentPageView,
			String pathToSource) {
		super(parentPageView, null);

		resourceController = new MenuImageViewController(
				parentPageView.getContext(), pathToSource, "192-256");
		resourceController.setBaseElemetView(this);
		ResourceResolutionHelper resourceResolution = ((MenuImageViewController) resourceController)
				.getResourceResolution();
		if (resourceResolution != null) {

			menuHeight = resourceResolution.height;
		}

		// resourceController.setOnDrowBitmapListner(this);
	}

	// @Override
	// public void onDrow(final Bitmap drowBitmap) {
	// if (menuImageView != null) {
	//
	// handler.post(new Runnable() {
	// @Override
	// public void run() {
	//
	// if (drowBitmap != null && !drowBitmap.isRecycled()) {
	// ResourceResolutionHelper bitmapResolution = ResourceResolutionHelper
	// .getBitmapResolutionVerticalMenuItem(
	// menuImageView.getContext(),
	// drowBitmap.getWidth(),
	// drowBitmap.getHeight());
	//
	// menuImageView.getLayoutParams().width = bitmapResolution.width;
	// menuImageView.getLayoutParams().height = bitmapResolution.height;
	//
	// menuImageView.setImageBitmap(drowBitmap);
	//
	// resourceController.releaseInctiveResources();
	// } else {
	// menuImageView.setImageBitmap(null);
	// resourceController.releaseInctiveResources();
	// }
	//
	// }
	// });
	//
	// }
	//
	// }
	//
	// @Override
	// public void onInvalidateView() {
	// menuImageView.invalidate();
	// }

	public View getView(Context context) {
		if (menuImageView == null) {
			Gallery.LayoutParams bgImageViewParams = new Gallery.LayoutParams(
					-2, -2);

			// ResourceResolutionHelper bitmapResolution =
			// ResourceResolutionHelper
			// .getBitmapResolutionVerticalMenuItem(context, -1, -1);
			//
			// bgImageViewParams.width = bitmapResolution.width;
			// bgImageViewParams.height = bitmapResolution.height;
			menuImageView = new ImageViewResources(context);
			menuImageView.setScaleType(ScaleType.FIT_XY);
			menuImageView.setBackgroundColor(Color.TRANSPARENT);
			menuImageView.setLayoutParams(bgImageViewParams);
			// ((ImageViewController) resourceController)
			// .setTargetImagetView(menuImageView);
		}
		return menuImageView;
	}

	public void setState(State state) {

		if (state == State.ACTIVE && (menuHeight == -2 || menuHeight == 0)) {

			ResourceResolutionHelper resourceResolution = ((MenuImageViewController) resourceController)
					.getResourceResolution();
			if (resourceResolution != null) {

				menuHeight = resourceResolution.height;
			}
		}

		resourceController.setState(state);
		if (getState() != state) {
			super.setState(state);
			if (state != State.RELEASE) {
				ResourceFactory.processResourceController(resourceController);
			}
		}

	}

	@Override
	public ResourceResolutionHelper getResolutionForController(Bitmap bitmap) {

		ResourceResolutionHelper bitmapResolutionVerticalMenuItem = ResourceResolutionHelper
				.getBitmapResolutionVerticalMenuItem(
						menuImageView.getContext(), bitmap.getWidth(),
						bitmap.getHeight());
		// menuHeight = bitmapResolutionVerticalMenuItem.height;
		return bitmapResolutionVerticalMenuItem;
	}

	@Override
	public View getShowingView() {
		return menuImageView;
	}

	public int getMenuHeight() {
		return menuHeight;
	}

}
