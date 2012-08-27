package padcms.magazine.page;

import java.util.ArrayList;

import padcms.dao.issue.bean.Page;
import padcms.magazine.factory.ElementViewFactory;
import padcms.magazine.page.elementview.BackgroundElementView;
import padcms.magazine.page.elementview.GalaryElementView;
import padcms.magazine.page.elementview.PopupElementView;
import padcms.magazine.page.elementview.ScrollingPaneElementView;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class GalleryFlashBulletInteractivePageTemplateView extends BasePageView {

	private BackgroundElementView backgroundElementView;
	private ScrollingPaneElementView scrollingPaneElementView;
	private RelativeLayout viewGroupMinArticleBG;
	private ArrayList<PopupElementView> popupElementViewList;

	public GalleryFlashBulletInteractivePageTemplateView(Context context,
			Page pageBin) {
		super(context, pageBin);
	}

	@Override
	protected void initElementData(Context mContext) {
		super.initElementData(mContext);
		backgroundElementView = ElementViewFactory
				.getBackgroundElementView(elementViewCollection);
		scrollingPaneElementView = ElementViewFactory
				.getScrollingPaneElementView(elementViewCollection);
		popupElementViewList = (ArrayList<PopupElementView>) ElementViewFactory
				.getPopupElementViewCollection(elementViewCollection);

		if (backgroundElementView != null)
			backgroundElementView
					.setProgressDownloadElementView(progressElementView);
		if (scrollingPaneElementView != null)
			scrollingPaneElementView
					.setProgressDownloadElementView(progressElementView);
		if (popupElementViewList != null)
			for (PopupElementView popElementView : popupElementViewList) {
				popElementView
						.setProgressDownloadElementView(progressElementView);
				popElementView.setElementBackgroundColor(Color.TRANSPARENT);
			}
	}

	@Override
	public View getView(Context mContext) {
		if (pageViewLayer == null) {
			initElementData(mContext);
			super.getView(mContext);
			viewGroupMinArticleBG = new RelativeLayout(mContext);
			if (backgroundElementView != null) {
				pageLayer.addView(backgroundElementView.getView(mContext), 0);
				setPageWidth((int) backgroundElementView.getWidth());
				setPageHeight((int) backgroundElementView.getHeight());
				if (scrollingPaneElementView != null) {
					RelativeLayout layout = new RelativeLayout(mContext);
					layout.setLayoutParams(new RelativeLayout.LayoutParams(
							backgroundElementView.getWidth(),
							backgroundElementView.getHeight()));
					((RelativeLayout.LayoutParams) layout.getLayoutParams())
							.addRule(RelativeLayout.CENTER_IN_PARENT);
					layout.addView(scrollingPaneElementView.getView(mContext));
					pageLayer.addView(layout);
					backgroundElementView
							.initViewWithActiveZone(activeZoneViewLayer);
				}
			} else if (scrollingPaneElementView != null) {
				pageLayer
						.addView(scrollingPaneElementView.getView(mContext), 0);
				setPageWidth((int) scrollingPaneElementView.getWidth());
				setPageHeight((int) scrollingPaneElementView.getHeight());

			}

			pageLayer.addView(viewGroupMinArticleBG,
					new RelativeLayout.LayoutParams(-1, -2));
			addPhotoGallaryButton(mContext);
		}
		return pageViewLayer;
	}

	@Override
	public void setActiveState() {
		state = State.ACTIVE;
		if (scrollingPaneElementView != null)
			scrollingPaneElementView.setState(state);
		if (backgroundElementView != null)
			backgroundElementView.setState(state);
		if (popupElementViewList != null)
			for (PopupElementView popElementView : popupElementViewList) {
				if (popElementView.isActive())
					popElementView.setState(State.ACTIVE);
				else
					popElementView.setState(State.RELEASE);

			}
	}

	@Override
	public void setDisactiveState() {
		state = State.DISACTIVE;
		if (backgroundElementView == null) {
			if (scrollingPaneElementView != null)
				scrollingPaneElementView.setState(state);

			if (backgroundElementView != null)
				backgroundElementView.setState(State.RELEASE);
		} else {
			if (scrollingPaneElementView != null)
				scrollingPaneElementView.setState(State.RELEASE);

			if (backgroundElementView != null)
				backgroundElementView.setState(state);
		}
		if (popupElementViewList != null)
			for (PopupElementView popElementView : popupElementViewList) {
				popElementView.setState(State.RELEASE);
			}

	}

	@Override
	public void activateElementView(int numberOfComponent) {

		super.activateElementView(numberOfComponent);
		if (popupElementViewList != null)
			for (PopupElementView popElementView : popupElementViewList) {

				View view = popElementView.getView(getContext());
				if (popElementView.getWeight() + 1 == numberOfComponent) {
					for (GalaryElementView gallaryView : getGalaryElementViewCollection()) {
						if (gallaryView.getState() == State.ACTIVE) {
							((ViewGroup) gallaryView.getView(getContext()))
									.addView(view);

							popElementView.activateElement();
							break;
						}
					}
				} else {

					popElementView.disactivateElement();

				}

			}
	}

	@Override
	public void setReleaseState() {

		state = State.RELEASE;
		if (scrollingPaneElementView != null)
			scrollingPaneElementView.setState(state);
		if (backgroundElementView != null)
			backgroundElementView.setState(state);
		if (popupElementViewList != null)
			for (PopupElementView popElementView : popupElementViewList) {
				popElementView.setState(state);
			}
	}
}
