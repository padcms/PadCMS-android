package padcms.magazine.help;

import padcms.bll.ApplicationManager;
import padcms.dao.issue.bean.Element;
import padcms.magazine.controls.imagecontroller.ImageViewResources;
import padcms.magazine.factory.ResourceFactory;
import padcms.magazine.factory.ResourceResolutionHelper;
import padcms.magazine.page.BasePageView;
import padcms.magazine.page.State;
import padcms.magazine.page.elementview.BaseElementView;
import padcms.magazine.resource.ImageViewController;
import padcms.net.NetHepler;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

/**
 * scrolling_pane
 * 
 * Scrolling pan is a PDF, JPEG, PNG or an HTML/CSS/Images element that can have
 * any height and scrolls freely vertically
 * 
 * resource (string) relative path to the resource
 * 
 * top (int) set in pixels, it defines where this element should be shown at the
 * beginning. It also defines the lowest position of the scrolling pan; to
 * avoid, it disappears from the screen.
 */
public class HelpElementViewHorisontal extends BaseElementView {

	private ImageView scrpllingPanelView;

	public HelpElementViewHorisontal(Context mContext, String pathToHelpPage) {
		super(null, null);
		resourcePathStr = pathToHelpPage;

		resourceController = new ImageViewController(mContext, resourcePathStr,
				ApplicationManager.elementResolutionHorisontal);
		resourceController.setOnUpdateProgress(this);
		resourceController.setBaseElemetView(this);
	}

	// @Override
	// public void onDrow(final Bitmap drowBitmap) {
	// if (scrpllingPanelView != null) {
	// runInUI(new Runnable() {
	// @Override
	// public void run() {
	// if (drowBitmap != null && !drowBitmap.isRecycled()) {
	// scrpllingPanelView.setImageBitmap(drowBitmap);
	//
	// ResourceResolutionHelper bitmapResolution = ResourceResolutionHelper
	// .getBitmapResolutionHelpPageHorisontal(
	// scrpllingPanelView.getContext(),
	// drowBitmap.getWidth(),
	// drowBitmap.getHeight());
	//
	// scrpllingPanelView.getLayoutParams().width = bitmapResolution.width;
	// scrpllingPanelView.getLayoutParams().height = bitmapResolution.height;
	// scrpllingPanelView.setBackgroundColor(Color.WHITE);
	// resourceController.releaseInctiveResources();
	// } else {
	// scrpllingPanelView.setImageBitmap(null);
	// scrpllingPanelView
	// .setBackgroundColor(Color.TRANSPARENT);
	// resourceController.releaseInctiveResources();
	// }
	// }
	// });
	// }
	// }
	// @Override
	// public void onInvalidateView() {
	// scrpllingPanelView.invalidate();
	// }
	// public HelpElementViewHorisontal(BasePageView parentPageView,
	// Element element) {
	// super(parentPageView, element);
	//
	// }

	@Override
	public View getView(Context context) {

		FrameLayout.LayoutParams scrollElementLayoutParams = new FrameLayout.LayoutParams(
				-2, -2);
		RelativeLayout.LayoutParams rlView = new RelativeLayout.LayoutParams(
				-2, -2);
		rlView.addRule(RelativeLayout.CENTER_IN_PARENT);

		RelativeLayout scrillingContainer = new RelativeLayout(context);

		scrpllingPanelView = new ImageViewResources(context);
		scrpllingPanelView.setLayoutParams(scrollElementLayoutParams);
		scrpllingPanelView.setScaleType(ScaleType.FIT_XY);
		scrpllingPanelView.setBackgroundColor(Color.WHITE);

		scrillingContainer.addView(scrpllingPanelView);

		scrillingContainer.setLayoutParams(rlView);

		// .setTargetImagetView(scrpllingPanelView);
		return scrillingContainer;
	}

	@Override
	public View getShowingView() {

		return scrpllingPanelView;
	}

	@Override
	public void setState(State state) {

		super.setState(state);

		resourceController.setState(state);

		if (state != State.RELEASE)
			ResourceFactory.processResourceController(resourceController);

	}

	@Override
	public ResourceResolutionHelper getResolutionForController(Bitmap bitmap) {

		return ResourceResolutionHelper.getBitmapResolutionHelpPageHorisontal(
				scrpllingPanelView.getContext(), bitmap.getWidth(),
				bitmap.getHeight());
	}
}
