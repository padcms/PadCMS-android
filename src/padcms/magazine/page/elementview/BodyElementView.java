package padcms.magazine.page.elementview;

import padcms.dao.issue.bean.Element;
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
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

/**
 * body
 * 
 * The body content is the main element of an article template. It may contain a
 * PDF, JPEG, PNG or an HTML/CSS/Images archive. PDF or HTML may contain active
 * zones or links. Active zones may launch the video associated with the
 * article.
 * 
 * resource (string) relative path to the resource
 * 
 * top (int) says where the body content should be positioned from the top of
 * the screen
 * 
 * hasPhotoGalleryLink (boolean) If TRUE means that body template have a link to
 * launch photo gallery, and there is no need to show the default one. If FALSE,
 * the default one will be shown.
 * 
 * showTopLayer (boolean) do we have to show scroller
 */
public class BodyElementView extends BaseElementView {

	// top (int) says where the body content should be positioned from the top
	// of the screen
	private int top;
	// hasPhotoGalleryLink (boolean) If TRUE means that body template have a
	// link to launch photo gallery, and there is no need to show the default
	// one. If FALSE, the default one will be shown.
	private boolean hasPhotoGalleryLink;
	// showTopLayer (boolean) do we have to show scroller
	private boolean needToShowTopLayer;

	private ImageViewGroup bodyView;

	// private ContentScrollingView contentScrollingBody;

	public BodyElementView(BasePageView parentPageView, Element element) {
		super(parentPageView, element);

		top = ElementViewFactory.getElementDataIntValue(ElementViewFactory
				.getElementDataCurrentType(elementDataCollection, "top"));
		hasPhotoGalleryLink = ElementViewFactory
				.getElementDataBooleanValue(ElementViewFactory
						.getElementDataCurrentType(elementDataCollection,
								"hasPhotoGalleryLink"));
		needToShowTopLayer = ElementViewFactory
				.getElementDataBooleanValue(ElementViewFactory
						.getElementDataCurrentType(elementDataCollection,
								"needToShowTopLayer"));
		resourceController = new PartedImageViewController(
				parentPageView.getContext(), resourcePathStr);

		resourceController.setOnUpdateProgress(this);
		resourceController.setBaseElemetView(this);
		// String urlToResource = NetHepler
		// .getUrlToResourceByDemetion(resourcePathStr);
		//
		// String pathToResourceLocal = ApplicationFileHelper
		// .getPathToIssueFolder(IssueViewFactory.issueFolderName)
		// .getAbsolutePath()
		// + "/" + String.valueOf(urlToResource.hashCode());

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
		//
		// containerPartedImages
		// .addView(scrpllingPanelView);
		// }
		// // BodyElementView.this.bodyView
		// // .setImageBitmap(null);
		// // resourceController
		// // .releaseInctiveResources();
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
	// bodyView.invalidate();
	// }
	// });
	// }

	// @Override
	// public void onDrow(final Bitmap drowBitmap) {
	//
	// if (bodyView != null) {
	//
	// runInUI(new Runnable() {
	// @Override
	// public void run() {
	//
	// if (drowBitmap != null && !drowBitmap.isRecycled()) {
	// containerPartedImages.removeAllViews();
	//
	// bodyView.setImageBitmap(drowBitmap);
	// bodyView.setBackgroundColor(getElementBackgroundColor());
	//
	// ResourceResolutionHelper bitmapResolution = ResourceResolutionHelper
	// .getBitmapResolution(bodyView.getContext(),
	// drowBitmap.getWidth(),
	// drowBitmap.getHeight());
	//
	// bodyView.getLayoutParams().width = bitmapResolution.width;
	// bodyView.getLayoutParams().height = bitmapResolution.height;
	// resourceController.releaseInctiveResources();
	//
	// } else {
	// // bodyView.setImageBitmap(null);
	// // bodyView.setBackgroundColor(Color.TRANSPARENT);
	// // resourceController.releaseInctiveResources();
	// }
	// }
	// });
	//
	// }
	//
	// }

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public boolean hasPhotoGalleryLink() {
		return hasPhotoGalleryLink;
	}

	public void setHasPhotoGalleryLink(boolean hasPhotoGalleryLink) {
		this.hasPhotoGalleryLink = hasPhotoGalleryLink;
	}

	public boolean needToShowTopLayer() {
		return needToShowTopLayer;
	}

	public void setNeedToShowTopLayer(boolean needToShowTopLayer) {
		this.needToShowTopLayer = needToShowTopLayer;
	}

	@Override
	public void setElementBackgroundColor(int elementBackgroundColor) {
		super.setElementBackgroundColor(elementBackgroundColor);
		if (bodyView != null) {
			bodyView.setBackgroundColor(elementBackgroundColor);
		}
	}

	ImageViewGroup imageViewController;

	@Override
	public View getView(Context context) {

		if (bodyView == null) {
			// contentScrollingBody = new ContentScrollingView(context,
			// getParentPageView().getColor());

			RelativeLayout.LayoutParams layoutParamsScrollingView = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.FILL_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParamsScrollingView.addRule(RelativeLayout.CENTER_IN_PARENT);

			// contentScrollingBody.setLayoutParams(layoutParamsScrollingView);
			//
			// RelativeLayout.LayoutParams rlView = new
			// RelativeLayout.LayoutParams(
			// android.view.ViewGroup.LayoutParams.FILL_PARENT,
			// android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			// rlView.addRule(RelativeLayout.CENTER_IN_PARENT);
			// imageViewController = new ImageViewGroup(context);
			// imageViewController.setLayoutParams(layoutParamsScrollingView);
			//
			// containerPartedImages = new LinearLayout(context);
			// containerPartedImages.setOrientation(LinearLayout.VERTICAL);

			bodyView = new ImageViewGroup(context);
			bodyView.setLayoutParams(layoutParamsScrollingView);

			bodyView.setBackgroundColor(getElementBackgroundColor());

//			((PartedImageViewController) resourceController)
//					.setImageViewGroup(bodyView);

			// imageViewController.addView(bodyView);
			// imageViewController.addView(containerPartedImages,
			// new ViewGroup.LayoutParams(-1, -1));

			// contentScrollingBody.addView(scrillingContainer, rlView);
			//
			initViewWithActiveZone(bodyView);

		}
		return bodyView;
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

	@Override
	public View getShowingView() {
		// TODO Auto-generated method stub
		return bodyView;
	}

	// public void destroyElementView() {
	// if (contentScrollingBody != null)
	// contentScrollingBody.removeAllViews();
	// contentScrollingBody = null;
	//
	// }

}
