package padcms.magazine.page;

import java.util.ArrayList;

import padcms.dao.issue.bean.Page;
import padcms.magazine.controls.switcher.SmoothSwitcherController;
import padcms.magazine.factory.ElementViewFactory;
import padcms.magazine.page.elementview.ActiveZoneElementDataView;
import padcms.magazine.page.elementview.BackgroundElementView;
import padcms.magazine.page.elementview.SlideElementView;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;

public class SlideshowPageTemplateView extends BasePageView {

	private ArrayList<SlideElementView> slideElementViewCollection;
	private BackgroundElementView backgroundElementView;
	private SmoothSwitcherController smoothSwitcherController;

	public SlideshowPageTemplateView(Context context,
			Page pageBin) {
		super(context, pageBin);

	}

	@Override
	protected void initElementData(Context mContext) {
		super.initElementData(mContext);
		slideElementViewCollection = (ArrayList<SlideElementView>) ElementViewFactory
				.getSlideElementViewCollection(elementViewCollection);
		for (SlideElementView slideElementView : slideElementViewCollection) {
			slideElementView
					.setProgressDownloadElementView(progressElementView);
		}
		backgroundElementView = ElementViewFactory
				.getBackgroundElementView(elementViewCollection);
	}

	@Override
	public View getView(Context mContext) {
		if (pageViewLayer == null) {
			initElementData(mContext);
			super.getView(mContext);
			if (backgroundElementView != null) {
				pageLayer.addView(backgroundElementView.getView(getContext()));
				setPageWidth((int) backgroundElementView.getWidth());
				setPageHeight((int) backgroundElementView.getHeight());

				backgroundElementView.setElementBackgroundColor(Color.WHITE);
			}
			if (slideElementViewCollection != null
					&& slideElementViewCollection.size() > 0) {
				RelativeLayout relativeLayoutSmoothContainer = new RelativeLayout(
						mContext);
				if (backgroundElementView != null) {
					relativeLayoutSmoothContainer
							.setLayoutParams(new RelativeLayout.LayoutParams(
									backgroundElementView.getWidth(),
									backgroundElementView.getHeight()));

				} else {
					relativeLayoutSmoothContainer
							.setLayoutParams(new RelativeLayout.LayoutParams(
									-1, -1));
				}
				((RelativeLayout.LayoutParams) relativeLayoutSmoothContainer
						.getLayoutParams())
						.addRule(RelativeLayout.CENTER_IN_PARENT);

				smoothSwitcherController = new SmoothSwitcherController(
						mContext, slideElementViewCollection, getColor());

				RelativeLayout.LayoutParams layoutParamsContainetFlipper = new RelativeLayout.LayoutParams(

				-1, -2);
				// layoutParamsContainetFlipper
				// .addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				ActiveZoneElementDataView activeZoneElementView = getPageActiveZoneViewScrolling();
				if (activeZoneElementView != null) {
					// layoutParamsContainetFlipper.width =
					// activeZoneElementView
					// .getWidthActiveZone();
					layoutParamsContainetFlipper.height = activeZoneElementView
							.getHeightActiveZone();
					layoutParamsContainetFlipper.topMargin = activeZoneElementView
							.getTopActiveZone();
					layoutParamsContainetFlipper.leftMargin = activeZoneElementView
							.getLeftActiveZone();

					smoothSwitcherController.setPadding(
							-activeZoneElementView.getLeftActiveZone(),
							-activeZoneElementView.getTopActiveZone(), 0, 0);

				}
				relativeLayoutSmoothContainer.addView(smoothSwitcherController,
						layoutParamsContainetFlipper);

				pageLayer.addView(relativeLayoutSmoothContainer);

			}
		}
		return pageViewLayer;
	}

	@Override
	public void setActiveState() {
		state = State.ACTIVE;

		if (backgroundElementView != null)
			backgroundElementView.setState(state);

		if (smoothSwitcherController != null) {
			smoothSwitcherController.activateControllerView();
		}
		// for (int i = 0; i < slideElementViewCollection.size(); i++) {
		// if (slideElementViewCollection.get(i).isActive()) {
		// slideElementViewCollection.get(i).setState(State.ACTIVE);
		// activeElement = i;
		// } else if ((i == activeElement - 1) || (i == activeElement + 1)) {
		// slideElementViewCollection.get(i).setState(State.ACTIVE);
		// } else {
		// slideElementViewCollection.get(i).setState(State.RELEASE);
		// }
		// }
	}

	@Override
	public void setDisactiveState() {
		state = State.DISACTIVE;

		if (backgroundElementView != null) {
			backgroundElementView.setState(state);
		}
		if (smoothSwitcherController != null) {
			smoothSwitcherController.disactivateControllerView();
		}

	}

	@Override
	public void setReleaseState() {
		state = State.RELEASE;
		if (backgroundElementView != null)
			backgroundElementView.setState(state);

		if (smoothSwitcherController != null) {
			smoothSwitcherController.releaseControllerView();
		}

	}

}
