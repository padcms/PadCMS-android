package padcms.magazine.menu.elementView;

import padcms.magazine.controls.imagecontroller.ImageViewResources;
import padcms.magazine.factory.ResourceFactory;
import padcms.magazine.factory.ResourceResolutionHelper;
import padcms.magazine.page.HorisontalPageView;
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
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * background
 * 
 * Background is a PDF, JPEG, PNG or an HTML/CSS/Images. It takes whole IPad
 * screen.
 * 
 * resource (string) relative path to the resource
 */
public class HorisontalMenuElementView extends BaseElementView {
	private ImageView menuImageView;

	// protected ResourceController resourceController;
	// private State state = State.RELEASE;
	// private HorisontalPageView parentPageView;
	// Handler handler = new Handler();

	public HorisontalMenuElementView(HorisontalPageView parentPageView,
			String pathToSource) {
		super(parentPageView, null);
		// this.parentPageView = parentPageView;
		resourceController = new MenuImageViewController(
				parentPageView.getContext(), pathToSource, "204-153");
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

	//
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
	// .getBitmapResolutionHorisontalMenuItem(
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
	//
	// @Override
	// public void onInvalidateView() {
	// menuImageView.invalidate();
	// }

	// public HorisontalPageView getParentPageView() {
	// return parentPageView;
	// }
	//
	// public void setParentPageView(HorisontalPageView parentPageView) {
	// this.parentPageView = parentPageView;
	// }

	public View getView(Context context) {
		if (menuImageView == null) {
			Gallery.LayoutParams bgImageViewParams = new Gallery.LayoutParams(
					-2, -2);

			ResourceResolutionHelper bitmapResolution = ResourceResolutionHelper
					.getBitmapResolutionHorisontalMenuItem(context, 204, 153);

			bgImageViewParams.width = bitmapResolution.width;
			bgImageViewParams.height = bitmapResolution.height;

			menuImageView = new ImageViewResources(context);
			menuImageView.setScaleType(ScaleType.FIT_XY);
			menuImageView.setBackgroundColor(Color.WHITE);
			menuImageView.setLayoutParams(bgImageViewParams);

			// ((ImageViewController) resourceController)
			// .setTargetImagetView(menuImageView);
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
				menuImageView.getContext(), bitmap.getWidth(),
				bitmap.getHeight());

	}

	@Override
	public View getShowingView() {

		return menuImageView;
	}

}
