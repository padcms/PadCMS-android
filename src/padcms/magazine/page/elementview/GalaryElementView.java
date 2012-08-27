package padcms.magazine.page.elementview;

import padcms.dao.issue.bean.Element;
import padcms.magazine.controls.imagecontroller.ImageViewGroup;
import padcms.magazine.factory.ElementViewFactory;
import padcms.magazine.factory.ResourceFactory;
import padcms.magazine.page.BasePageView;
import padcms.magazine.page.State;
import padcms.magazine.resource.PartedImageViewController;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * gallery
 * 
 * Gallery is a field that must contain at least 2 images or be empty. Images
 * may be JPEG, PDF, PNG. If at least 2 images are associated with the article a
 * button “More images…” appears (if background parameter “Has photo gallery
 * link” is set to FALSE)
 * 
 * resource (string) relative path to the resource
 */
public class GalaryElementView extends BaseElementView {
	// private File imageFile;

	private ImageViewGroup gallaryView;
	private RelativeLayout gallaryViewContainer;
	private boolean isActive;
	private int gallery_id;

	public GalaryElementView(BasePageView parentPageView, Element element) {
		super(parentPageView, element);
		gallery_id = ElementViewFactory
				.getElementDataIntValue(ElementViewFactory
						.getElementDataCurrentType(elementDataCollection,
								"gallery_id"));
		resourceController = new PartedImageViewController(
				parentPageView.getContext(), resourcePathStr);
		// resourceController.setOnDrowBitmapListner(this);
		resourceController.setOnUpdateProgress(this);
		resourceController.setBaseElemetView(this);
	}

	// @Override
	// public void onDrow(final Bitmap drowBitmap) {
	// if (gallaryView != null) {
	//
	// runInUI(new Runnable() {
	// @Override
	// public void run() {
	//
	// if (drowBitmap != null && !drowBitmap.isRecycled()) {
	// gallaryView.setImageBitmap(drowBitmap);
	//
	// gallaryView
	// .setBackgroundColor(getElementBackgroundColor());
	//
	// ResourceResolutionHelper bitmapResolution = ResourceResolutionHelper
	// .getBitmapResolution(gallaryView.getContext(),
	// drowBitmap.getWidth(),
	// drowBitmap.getHeight());
	//
	// gallaryView.getLayoutParams().width = bitmapResolution.width;
	// gallaryView.getLayoutParams().height = bitmapResolution.height;
	//
	// resourceController.releaseInctiveResources();
	// if (getProgressDownloadElementView() != null)
	// getProgressDownloadElementView().hideFastView();
	// } else {
	// gallaryView.setImageBitmap(null);
	// gallaryView.setBackgroundColor(Color.TRANSPARENT);
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
	// gallaryView.invalidate();
	// }
	// public File getImageFile() {
	// return imageFile;
	// }
	//
	// public void setImageFile(File imageFile) {
	// this.imageFile = imageFile;
	// }

	public int getGallery_id() {
		return gallery_id;
	}

	public void setGallery_id(int gallery_id) {
		this.gallery_id = gallery_id;
	}

	@Override
	public void setElementBackgroundColor(int elementBackgroundColor) {
		super.setElementBackgroundColor(elementBackgroundColor);
		if (gallaryView != null) {
			gallaryView.setBackgroundColor(elementBackgroundColor);
		}
	}

	@Override
	public View getView(Context context) {

		if (gallaryViewContainer == null) {

			gallaryViewContainer = new RelativeLayout(context);
			gallaryView = new ImageViewGroup(context);

			RelativeLayout.LayoutParams backLayoutParams = new RelativeLayout.LayoutParams(
					-1, -2);
			// backLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			gallaryView.setLayoutParams(backLayoutParams);
			// gallaryView.setScaleType(ScaleType.FIT_XY);

			gallaryView.setBackgroundColor(getElementBackgroundColor());

			gallaryViewContainer.addView(gallaryView);

			// RelativeLayout.LayoutParams backgroundContainerLayoutParams = new
			// RelativeLayout.LayoutParams(
			// -1, -2);
			// backgroundContainerLayoutParams
			// .addRule(RelativeLayout.CENTER_IN_PARENT);

			// gallaryViewContainer
			// .setLayoutParams(backgroundContainerLayoutParams);

			initViewWithActiveZone(gallaryView);

			// ((PartedImageViewController) resourceController)
			// .setImageViewGroup(gallaryView);

		}
		return gallaryViewContainer;
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
		// if (gallaryView != null)
		// gallaryView.removeAllViews();
		gallaryView = null;

	}

	@Override
	public View getShowingView() {

		return gallaryView;
	}

}
