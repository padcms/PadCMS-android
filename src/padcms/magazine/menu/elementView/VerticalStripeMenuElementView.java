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
import padcms.net.NetHepler;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class VerticalStripeMenuElementView extends BaseElementView {
	private ImageView menuImageView;

	// protected ResourceController resourceController;
	// private State state = State.RELEASE;
	// private BasePageView parentPageView;
	// Handler handler = new Handler();

	public VerticalStripeMenuElementView(BasePageView parentPageView,
			String pathToSource) {
		super(parentPageView, null);

		resourceController = new MenuImageViewController(
				parentPageView.getContext(), pathToSource, "192-256");
		resourceController.setBaseElemetView(this);
		// resourceController.setOnDrowBitmapListner(this);
		// {// 1024-768 2048-1536
		// @Override
		// public void drowElement(final Bitmap bitmap) {
		//
		//
		//
		// }
		//
		// @Override
		// public void updateProgress(Bundle progressPramsBundel) {
		// }
		//
		// };
	}

	// @Override
	// public void onDrow(final Bitmap drowBitmap) {
	//
	// if (menuImageView != null) {
	//
	// handler.post(new Runnable() {
	// @Override
	// public void run() {
	//
	// if (drowBitmap != null && !drowBitmap.isRecycled()) {
	//
	// menuImageView.setImageBitmap(drowBitmap);
	//
	// resourceController.releaseInctiveResources();
	// } else {
	// menuImageView.setImageBitmap(null);
	// // menuImageView
	// // .setBackgroundColor(Color.TRANSPARENT);
	// resourceController.releaseInctiveResources();
	// }
	//
	// }
	// });
	//
	// }
	//
	// }
	// @Override
	// public void onInvalidateView() {
	// menuImageView.invalidate();
	// }

	// public BasePageView getParentPageView() {
	// return parentPageView;
	// }
	//
	// public void setParentPageView(BasePageView parentPageView) {
	// this.parentPageView = parentPageView;
	// }

	public View getView(Context context) {
		if (menuImageView == null) {
			ViewGroup.LayoutParams bgImageViewParams = new ViewGroup.LayoutParams(
					-1, -1);
			menuImageView = new ImageViewResources(context);
			menuImageView.setScaleType(ScaleType.FIT_XY);
			menuImageView.setBackgroundColor(Color.TRANSPARENT);
			menuImageView.setLayoutParams(bgImageViewParams);

			// ((ImageViewController) resourceController)
			// // .setTargetImagetView(menuImageView);
		}
		return menuImageView;
	}

	public void setState(State state) {

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
		return ResourceResolutionHelper.getBitmapResolutionHorisontalMenuItem(
				getParentPageView().getContext(), bitmap.getWidth(),
				bitmap.getHeight());

	}

	@Override
	public View getShowingView() {
		return menuImageView;
	}
}
