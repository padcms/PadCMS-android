package padcms.magazine.page.elementview;

import padcms.dao.issue.bean.Element;
import padcms.magazine.controls.imagecontroller.ImageViewGroup;
import padcms.magazine.factory.ResourceFactory;
import padcms.magazine.page.BasePageView;
import padcms.magazine.page.State;
import padcms.magazine.resource.PartedImageViewController;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * popup
 * 
 * The same as gallery
 * 
 * resource (string) relative path to the resource
 */
public class PopupElementView extends BaseElementView implements
		OnClickListener {
	private ImageViewGroup popUpView;
	private boolean isActive;

	// private RelativeLayout overlayContainer;

	// ImageViewGroup imageViewController;

	public PopupElementView(BasePageView parentPageView, Element element) {
		super(parentPageView, element);
		resourceController = new PartedImageViewController(
				parentPageView.getContext(), resourcePathStr);
		resourceController.setOnUpdateProgress(this);
		resourceController.setBaseElemetView(this);
	}

	@Override
	public void activateElement() {

		super.activateElement();
		if (!isActive) {
			// ?popUpView.setVisibility(View.VISIBLE);
			setState(State.ACTIVE);

			isActive = true;
		}
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public void disactivateElement() {
		if (isActive) {
			if (popUpView.getParent() != null) {
				((ViewGroup) popUpView.getParent()).removeView(popUpView);
			}
			setState(State.RELEASE);

			isActive = false;
		}
	}

	@Override
	public void setElementBackgroundColor(int elementBackgroundColor) {
		super.setElementBackgroundColor(elementBackgroundColor);
		if (popUpView != null) {
			popUpView.setBackgroundColor(elementBackgroundColor);
		}
	}

	@Override
	public View getView(Context context) {

		if (popUpView == null) {

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

			popUpView = new ImageViewGroup(context);
			popUpView.setLayoutParams(layoutParamsScrollingView);
			popUpView.setOnClickListener(this);
			popUpView.setBackgroundColor(getElementBackgroundColor());

			// ((PartedImageViewController) resourceController)
			// .setImageViewGroup(bodyView);

			// imageViewController.addView(bodyView);
			// imageViewController.addView(containerPartedImages,
			// new ViewGroup.LayoutParams(-1, -1));

			// contentScrollingBody.addView(scrillingContainer, rlView);
			//
			initViewWithActiveZone(popUpView);

		}
		return popUpView;
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

		return popUpView;
	}

	@Override
	public void onClick(View v) {
		if (isActive)
			disactivateElement();

	}
	

}
