package padcms.magazine.page;

import padcms.bll.ApplicationSkinFactory;
import padcms.dao.issue.bean.Page;
import padcms.magazine.factory.ElementViewFactory;
import padcms.magazine.page.elementview.BackgroundElementView;
import padcms.magazine.page.elementview.BodyElementView;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

public class FixedIllustrationArticleTouchablePageTemplateView extends
		BasePageView {

	private BodyElementView bodyElementView;
	private BackgroundElementView backgroundElementView;
	private boolean showingTopLayer;

	public FixedIllustrationArticleTouchablePageTemplateView(Context context,
			Page pageBin) {
		super(context, pageBin);
		showingTopLayer = true;
	}

	@Override
	protected void initElementData(Context mContext) {
		super.initElementData(mContext);
		bodyElementView = ElementViewFactory
				.getBodyElementView(elementViewCollection);
		backgroundElementView = ElementViewFactory
				.getBackgroundElementView(elementViewCollection);

		if (bodyElementView != null) {
			bodyElementView.setProgressDownloadElementView(progressElementView);
			bodyElementView.setElementBackgroundColor(Color.TRANSPARENT);
			showingTopLayer = bodyElementView.needToShowTopLayer();
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

			if (bodyElementView != null) {
				pageLayer.addView(bodyElementView.getView(getContext()));
				if (!showingTopLayer)
					ApplicationSkinFactory.animateHideFast(bodyElementView
							.getView(mContext));
			}
		}
		return pageViewLayer;
	}

	@Override
	public void pageViewClicked() {
		
		if (showingTopLayer) {
			if (bodyElementView != null) {
				ApplicationSkinFactory.animateHide(bodyElementView
						.getView(getContext()));
			}
		} else {
			if (bodyElementView != null) {

				ApplicationSkinFactory.animateShow(bodyElementView
						.getView(getContext()));

			}
		}
		showingTopLayer = !showingTopLayer;

	}

	@Override
	public void setActiveState() {
		state = State.ACTIVE;

		if (bodyElementView != null)
			bodyElementView.setState(state);

		if (backgroundElementView != null)
			backgroundElementView.setState(state);
	}

	@Override
	public void setDisactiveState() {
		state = State.DISACTIVE;

		if (bodyElementView != null) {

			if (showingTopLayer) {
				bodyElementView.setState(state);
				//
				 if (backgroundElementView != null)
				 backgroundElementView.setState(State.RELEASE);
			} else {
				bodyElementView.setState(State.RELEASE);

				if (backgroundElementView != null)
					backgroundElementView.setState(state);
			}
			// if (bodyElementView.needToShowTopLayer()) {
			//
			// bodyElementView.setState(state);
			//
			// if (backgroundElementView != null)
			// backgroundElementView.setState(State.RELEASE);
			//
			// } else {
			// bodyElementView.setState(State.RELEASE);
			//
			// if (backgroundElementView != null)
			// backgroundElementView.setState(state);
			// }
		} else {
			if (backgroundElementView != null)
				backgroundElementView.setState(state);
		}

	}

	@Override
	public void setReleaseState() {
		state = State.RELEASE;
		if (bodyElementView != null)
			bodyElementView.setState(state);
		if (backgroundElementView != null)
			backgroundElementView.setState(state);
	}

}
