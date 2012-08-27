package padcms.magazine.page;

import padcms.dao.issue.bean.Page;
import padcms.magazine.factory.ElementViewFactory;
import padcms.magazine.page.elementview.BackgroundElementView;
import padcms.magazine.page.elementview.ScrollingPaneElementView;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

public class ScrollingPageTemplateView extends BasePageView {

	private ScrollingPaneElementView scrollingPaneElementView;

	private BackgroundElementView backgroundElementView;

	public ScrollingPageTemplateView(Context context,
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
		if (backgroundElementView != null)
			backgroundElementView
					.setProgressDownloadElementView(progressElementView);
		if (scrollingPaneElementView != null)
			scrollingPaneElementView
					.setProgressDownloadElementView(progressElementView);
	}

	@Override
	public View getView(Context mContext) {
		if (pageViewLayer == null) {
			initElementData(mContext);
			super.getView(mContext);
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
			} else {
				pageLayer
						.addView(scrollingPaneElementView.getView(mContext), 0);
				setPageWidth((int) scrollingPaneElementView.getWidth());
				setPageHeight((int) scrollingPaneElementView.getHeight());

			}
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

	}

	@Override
	public void setReleaseState() {
		state = State.RELEASE;
		if (scrollingPaneElementView != null)
			scrollingPaneElementView.setState(state);
		if (backgroundElementView != null)
			backgroundElementView.setState(state);
	}

}
