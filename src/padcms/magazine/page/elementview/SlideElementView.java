package padcms.magazine.page.elementview;

import padcms.dao.issue.bean.Element;
import padcms.magazine.controls.imagecontroller.ImageViewGroup;
import padcms.magazine.factory.ResourceFactory;
import padcms.magazine.page.BasePageView;
import padcms.magazine.page.State;
import padcms.magazine.resource.PartedImageViewController;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * slide
 * 
 * Slide is a multiple field which may be PDF / HTML or PNG/JPEG. On each slide
 * it’s possible to associate an optional video file, which will be launched
 * from a PDF or HTML using local://video. Minimum number of slides is 2 and
 * maximum is 10. Pagination will be generated automatically.
 * 
 * resource (string) relative path to the resource video (string) relative path
 * to the video
 */
public class SlideElementView extends BaseElementView {

	private ImageViewGroup slideView;
	private boolean isActive;

	public SlideElementView(BasePageView parentPageView, Element element) {
		super(parentPageView, element);
		setElementBackgroundColor(Color.TRANSPARENT);
		resourceController = new PartedImageViewController(
				parentPageView.getContext(), resourcePathStr);
		resourceController.setOnUpdateProgress(this);
		resourceController.setBaseElemetView(this);
	}

	@Override
	public void setElementBackgroundColor(int elementBackgroundColor) {
		super.setElementBackgroundColor(elementBackgroundColor);
		if (slideView != null) {
			slideView.setBackgroundColor(elementBackgroundColor);
		}
	}

	@Override
	public View getView(Context context) {

		if (slideView == null) {
			slideView = new ImageViewGroup(context);
			RelativeLayout.LayoutParams backLayoutParams = new RelativeLayout.LayoutParams(
					-1, -2);
			slideView.setScrollable(false);
			slideView.setLayoutParams(backLayoutParams);
			slideView.setBackgroundColor(getElementBackgroundColor());

			initViewWithActiveZone(slideView);

//			((PartedImageViewController) resourceController)
//					.setImageViewGroup(slideView);
		}
		return slideView;
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
		// if (slideView != null)
		// slideView.removeAllViews();
		slideView = null;

	}

	@Override
	public View getShowingView() {

		return slideView;
	}
}
