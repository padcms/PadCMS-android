package padcms.magazine.page.elementview;

import padcms.dao.issue.bean.Element;
import padcms.magazine.controls.ContentScrollingView;
import padcms.magazine.controls.imagecontroller.ImageViewGroup;
import padcms.magazine.controls.imagecontroller.ImageViewResources;
import padcms.magazine.factory.ElementViewFactory;
import padcms.magazine.factory.ResourceFactory;
import padcms.magazine.page.BasePageView;
import padcms.magazine.page.State;
import padcms.magazine.resource.ImageViewController;
import padcms.magazine.resource.PartedImageViewController;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
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
public class ScrollingPaneElementView extends BaseElementView {
	private int top;

	private ImageViewGroup scrpllingPanelView;

	// private ContentScrollingView contentScrolling;
	// private LinearLayout containerPartedImages;

	public ScrollingPaneElementView(BasePageView parentPageView, Element element) {
		super(parentPageView, element);

		top = ElementViewFactory.getElementDataIntValue(ElementViewFactory
				.getElementDataCurrentType(elementDataCollection, "top"));

		resourceController = new PartedImageViewController(
				parentPageView.getContext(), resourcePathStr);
		resourceController.setOnUpdateProgress(this);
		resourceController.setBaseElemetView(this);
		// resourceController.setOnDrowBitmapListner(this);
		// resourceController
		// .setOnDrowPartedBitmapListner(new onDrowPartedBitmapListner() {
		//
		// @Override
		// public void onDrow(final List<Bitmap> drowBitmapList) {
		// if (containerPartedImages != null) {
		// runInUI(new Runnable() {
		// @Override
		// public void run() {
		// containerPartedImages.removeAllViews();
		// for (Bitmap drowBitmap : drowBitmapList) {
		// ImageViewResources scrpllingPanelView = new ImageViewResources(
		// containerPartedImages
		// .getContext());
		// scrpllingPanelView
		// .setScaleType(ScaleType.FIT_XY);
		//
		// FrameLayout.LayoutParams lpFrame = new FrameLayout.LayoutParams(
		// -2, -2);
		// scrpllingPanelView
		// .setLayoutParams(lpFrame);
		//
		// if (drowBitmap != null
		// && !drowBitmap.isRecycled()) {
		// scrpllingPanelView
		// .setImageBitmap(drowBitmap);
		//
		// ResourceResolutionHelper bitmapResolution =
		// ResourceResolutionHelper.getBitmapResolution(
		// scrpllingPanelView
		// .getContext(),
		// drowBitmap.getWidth(),
		// drowBitmap.getHeight());
		//
		// scrpllingPanelView
		// .getLayoutParams().width = bitmapResolution.width;
		// scrpllingPanelView
		// .getLayoutParams().height = bitmapResolution.height;
		//
		// }
		// containerPartedImages
		// .addView(scrpllingPanelView);
		// }
		// ScrollingPaneElementView.this.scrpllingPanelView.setImageBitmap(null);
		//
		// resourceController
		// .releaseInctiveResources();
		// }
		// });
		//
		// }
		// }
		// });
	}

	// @Override
	// public void onInvalidateView() {
	// runInUI(new Runnable() {
	//
	// @Override
	// public void run() {
	// scrpllingPanelView.invalidate();
	// }
	// });
	// }
	//
	// @Override
	// public void onDrow(final Bitmap drowBitmap) {
	// if (scrpllingPanelView != null) {
	// runInUI(new Runnable() {
	// @Override
	// public void run() {
	//
	// if (drowBitmap != null && !drowBitmap.isRecycled()) {
	// scrpllingPanelView.setImageBitmap(drowBitmap);
	// containerPartedImages.removeAllViews();
	// ResourceResolutionHelper bitmapResolution = ResourceResolutionHelper
	// .getBitmapResolution(
	// scrpllingPanelView.getContext(),
	// drowBitmap.getWidth(),
	// drowBitmap.getHeight());
	//
	// scrpllingPanelView.getLayoutParams().width = bitmapResolution.width;
	// scrpllingPanelView.getLayoutParams().height = bitmapResolution.height;
	//
	// resourceController.releaseInctiveResources();
	// } else {
	// scrpllingPanelView.setImageBitmap(null);
	// scrpllingPanelView
	// .setBackgroundColor(Color.TRANSPARENT);
	// resourceController.releaseInctiveResources();
	// }
	// }
	// });
	//
	// }
	// }

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	ActiveZoneElementDataView activeZoneElementView;

	@Override
	public View getView(Context context) {

		if (scrpllingPanelView == null) {
			activeZoneElementView = getParentPageView()
					.getPageActiveZoneViewScrolling();
			// contentScrolling = new ContentScrollingView(context,
			// getParentPageView().getColor());

			// RelativeLayout.LayoutParams layoutParamsScrollingView = new
			// RelativeLayout.LayoutParams(
			// android.view.ViewGroup.LayoutParams.FILL_PARENT,
			// android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

			// layoutParamsScrollingView
			// .addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			// layoutParamsScrollingView
			// .addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

			RelativeLayout.LayoutParams scrollElementLayoutParams = new RelativeLayout.LayoutParams(
					-2, -2);

			// RelativeLayout.LayoutParams rlView = new
			// RelativeLayout.LayoutParams(
			// -2, -2);
			// rlView.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

			// FrameLayout scrillingContainer = new FrameLayout(context);
			scrpllingPanelView = new ImageViewGroup(context);
			// containerPartedImages = new LinearLayout(context);
			// containerPartedImages.setOrientation(LinearLayout.VERTICAL);
			if (activeZoneElementView != null) {
				scrollElementLayoutParams.width = -1;
				// activeZoneElementView.getWidthActiveZone();
				// layoutParamsScrollingView.height = -1;
				//
				scrollElementLayoutParams.height = // -1;
				activeZoneElementView.getHeightActiveZone();

				scrollElementLayoutParams.topMargin = activeZoneElementView
						.getTopActiveZone();
				scrollElementLayoutParams.leftMargin = activeZoneElementView
						.getLeftActiveZone();

				// scrollElementLayoutParams.width = activeZoneElementView
				// .getWidthActiveZone()
				// + activeZoneElementView.getLeftActiveZone();
				// scrillingContainer.scrollTo(
				// activeZoneElementView.getLeftActiveZone(),
				// activeZoneElementView.getTopActiveZone());
				// scrillingContainer
				// .setPadding(-activeZoneElementView.getLeftActiveZone(),
				// -activeZoneElementView.getTopActiveZone(), 0, 0);
				// rlView.topMargin = -activeZoneElementView.getTopActiveZone();
				//
				// contentScrolling.setBackgroundColor(Color.GREEN);
				// contentScrolling.getBackground().setAlpha(150);

			} else
				scrollElementLayoutParams
						.addRule(RelativeLayout.CENTER_IN_PARENT);

			// contentScrolling.setLayoutParams(layoutParamsScrollingView);

			scrpllingPanelView.setLayoutParams(scrollElementLayoutParams);
			// scrpllingPanelView.setScaleType(ScaleType.FIT_XY);

			// scrillingContainer.addView(scrpllingPanelView);
			// scrillingContainer.addView(containerPartedImages,
			// new FrameLayout.LayoutParams(-1, -1));
			// contentScrolling.addView(scrillingContainer, rlView);

			initViewWithActiveZone(scrpllingPanelView);
			// relativeLayoutContentSmoothConteiner.addView(contentScrolling);
			// ((PartedImageViewController) resourceController)
			// .setImageViewGroup(scrpllingPanelView);

		}
		return scrpllingPanelView;
	}

	// public View getView1(Context context) {
	//
	// if (contentScrolling == null) {
	// activeZoneElementView = getParentPageView()
	// .getPageActiveZoneViewScrolling();
	// contentScrolling = new ContentScrollingView(context,
	// getParentPageView().getColor());
	//
	// RelativeLayout.LayoutParams layoutParamsScrollingView = new
	// RelativeLayout.LayoutParams(
	// android.view.ViewGroup.LayoutParams.FILL_PARENT,
	// android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	//
	// layoutParamsScrollingView
	// .addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	// layoutParamsScrollingView
	// .addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	//
	// FrameLayout.LayoutParams scrollElementLayoutParams = new
	// FrameLayout.LayoutParams(
	// -2, -2);
	//
	// RelativeLayout.LayoutParams rlView = new RelativeLayout.LayoutParams(
	// -2, -2);
	// rlView.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	//
	// FrameLayout scrillingContainer = new FrameLayout(context);
	// scrpllingPanelView = new ImageViewGroup(context);
	// //containerPartedImages = new LinearLayout(context);
	// // containerPartedImages.setOrientation(LinearLayout.VERTICAL);
	// if (activeZoneElementView != null) {
	// layoutParamsScrollingView.width = -1;
	// // activeZoneElementView
	// // .getWidthActiveZone();
	// // layoutParamsScrollingView.height = -1;
	// //
	// layoutParamsScrollingView.height = -1;
	// // activeZoneElementView
	// // .getHeightActiveZone();
	//
	// layoutParamsScrollingView.topMargin = activeZoneElementView
	// .getTopActiveZone();
	// layoutParamsScrollingView.leftMargin = activeZoneElementView
	// .getLeftActiveZone();
	//
	// // scrollElementLayoutParams.width = activeZoneElementView
	// // .getWidthActiveZone()
	// // + activeZoneElementView.getLeftActiveZone();
	// scrillingContainer.scrollTo(
	// activeZoneElementView.getLeftActiveZone(),
	// activeZoneElementView.getTopActiveZone());
	// // scrillingContainer
	// // .setPadding(-activeZoneElementView.getLeftActiveZone(),
	// // -activeZoneElementView.getTopActiveZone(), 0, 0);
	// // rlView.topMargin = -activeZoneElementView.getTopActiveZone();
	// //
	// // contentScrolling.setBackgroundColor(Color.GREEN);
	// // contentScrolling.getBackground().setAlpha(150);
	//
	// } else
	// layoutParamsScrollingView
	// .addRule(RelativeLayout.CENTER_IN_PARENT);
	//
	// contentScrolling.setLayoutParams(layoutParamsScrollingView);
	//
	// scrpllingPanelView.setLayoutParams(scrollElementLayoutParams);
	// scrpllingPanelView.setScaleType(ScaleType.FIT_XY);
	//
	// scrillingContainer.addView(scrpllingPanelView);
	// scrillingContainer.addView(containerPartedImages,
	// new FrameLayout.LayoutParams(-1, -1));
	// contentScrolling.addView(scrillingContainer, rlView);
	//
	// initViewWithActiveZone(scrillingContainer);
	// // relativeLayoutContentSmoothConteiner.addView(contentScrolling);
	// ((ImageViewController) resourceController)
	// .setTargetImagetView(scrpllingPanelView);
	//
	// }
	// return contentScrolling;
	// }

	@Override
	public void setState(State state) {
		// if (state != getState()) {
		resourceController.setState(state);
		if (getState() != state) {
			super.setState(state);
			if (state != State.RELEASE)
				ResourceFactory.processResourceController(resourceController);
		}

	}

	public void destroyElementView() {
		if (scrpllingPanelView != null)
			scrpllingPanelView.removeAllViews();
		scrpllingPanelView = null;

	}

	@Override
	public View getShowingView() {
		return scrpllingPanelView;
	}
}
