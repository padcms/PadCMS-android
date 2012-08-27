package padcms.magazine.page;

import padcms.bll.ApplicationSkinFactory;
import padcms.dao.issue.bean.Page;
import padcms.magazine.factory.ElementViewFactory;
import padcms.magazine.page.elementview.BackgroundElementView;
import padcms.magazine.page.elementview.OverlayElementView;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

public class ArticleWithOverlayPageTemplateView extends BasePageView {

	private OverlayElementView overlayElementView;
	private BackgroundElementView backgroundElementView;
	private boolean showingTopLayer;

	public ArticleWithOverlayPageTemplateView(Context context,
			Page pageBin) {
		super(context, pageBin);
		showingTopLayer = true;
	}

	@Override
	protected void initElementData(Context mContext) {
		super.initElementData(mContext);
		backgroundElementView = ElementViewFactory
				.getBackgroundElementView(elementViewCollection);

		overlayElementView = ElementViewFactory
				.getOverlayElementView(elementViewCollection);

		backgroundElementView = ElementViewFactory
				.getBackgroundElementView(elementViewCollection);

		progressElementView.hideFastView();

		if (overlayElementView != null) {
			overlayElementView
					.setProgressDownloadElementView(progressElementView);
			overlayElementView.setElementBackgroundColor(Color.TRANSPARENT);

		}
		if (backgroundElementView != null)
			backgroundElementView
					.setProgressDownloadElementView(progressElementView);

	}

	@Override
	public View getView(Context mContext) {
		if (pageViewLayer == null) {
			initElementData(mContext);
			super.getView(mContext);
			if (backgroundElementView != null) {
				pageLayer.addView(backgroundElementView.getView(getContext()));
				setPageHeight(backgroundElementView.getHeight());
				setPageWidth(backgroundElementView.getWidth());

			}

			if (overlayElementView != null) {
				pageLayer.addView(overlayElementView.getView(getContext()));
				if (!showingTopLayer)
					ApplicationSkinFactory.animateHideFast(overlayElementView
							.getView(mContext));
			}
		}
		return pageViewLayer;
	}

	@Override
	public void pageViewClicked() {

		if (showingTopLayer) {
			if (overlayElementView != null) {
				ApplicationSkinFactory.animateHide(overlayElementView
						.getView(getContext()));
			}
		} else {
			if (overlayElementView != null) {

				ApplicationSkinFactory.animateShow(overlayElementView
						.getView(getContext()));

			}
		}
		showingTopLayer = !showingTopLayer;

	}

	@Override
	public void setActiveState() {
		state = State.ACTIVE;

		if (overlayElementView != null)
			overlayElementView.setState(state);

		if (backgroundElementView != null)
			backgroundElementView.setState(state);
	}

	@Override
	public void setDisactiveState() {
		state = State.DISACTIVE;

		if (backgroundElementView != null)
			backgroundElementView.setState(state);

		if (overlayElementView != null)
			overlayElementView.setState(State.RELEASE);

	}

	@Override
	public void setReleaseState() {
		state = State.RELEASE;
		if (overlayElementView != null)
			overlayElementView.setState(state);

		if (backgroundElementView != null)
			backgroundElementView.setState(state);
	}

}
