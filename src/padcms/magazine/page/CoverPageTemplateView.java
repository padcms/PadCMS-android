package padcms.magazine.page;

import padcms.dao.issue.bean.Page;
import padcms.magazine.factory.ElementViewFactory;
import padcms.magazine.page.elementview.AdvertElementView;
import padcms.magazine.page.elementview.BackgroundElementView;
import padcms.magazine.page.elementview.BodyElementView;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

public class CoverPageTemplateView extends BasePageView {

	private BodyElementView bodyElementView;
	private AdvertElementView advertElementView;
	private BackgroundElementView backgroundElementView;

	public CoverPageTemplateView(Context context,
			Page pageBin) {
		super(context, pageBin);
	}

	@Override
	protected void initElementData(Context mContext) {
		super.initElementData(mContext);

		bodyElementView = ElementViewFactory
				.getBodyElementView(elementViewCollection);

		backgroundElementView = ElementViewFactory
				.getBackgroundElementView(elementViewCollection);

		advertElementView = ElementViewFactory
				.getAdvertElementView(elementViewCollection);

		if (bodyElementView != null)
			bodyElementView.setProgressDownloadElementView(progressElementView);

		if (backgroundElementView != null)
			backgroundElementView
					.setProgressDownloadElementView(progressElementView);
	}

	@Override
	public View getView(Context mContext) {
		if (pageViewLayer == null) {
			initElementData(mContext);
			super.getView(mContext);
			if (bodyElementView != null) {
				pageLayer.addView(bodyElementView.getView(getContext()), 0);
				bodyElementView.setElementBackgroundColor(Color.WHITE);
			}

			if (backgroundElementView != null) {
				pageLayer.addView(backgroundElementView
						.getView(getContext()));
			}
		}
		return pageViewLayer;
	}

	@Override
	public void setActiveState() {
		state = State.ACTIVE;
		if (bodyElementView != null)
			bodyElementView.setState(state);
		if (backgroundElementView != null)
			backgroundElementView.setState(state);
		if (advertElementView != null)
			advertElementView.setState(state);

	}

	@Override
	public void setDisactiveState() {
		state = State.DISACTIVE;

		if (bodyElementView != null)
			bodyElementView.setState(state);
		if (backgroundElementView != null)
			backgroundElementView.setState(State.RELEASE);
		if (advertElementView != null)
			advertElementView.setState(State.RELEASE);

	}

	@Override
	public void setReleaseState() {
		state = State.RELEASE;
		if (bodyElementView != null)
			bodyElementView.setState(state);
		if (backgroundElementView != null)
			backgroundElementView.setState(state);
		if (advertElementView != null)
			advertElementView.setState(state);
	}

}
