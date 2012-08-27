package padcms.magazine.page;

import padcms.dao.issue.bean.Page;
import padcms.magazine.factory.ElementViewFactory;
import padcms.magazine.page.elementview.BackgroundElementView;
import padcms.magazine.page.elementview.BodyElementView;
import android.content.Context;
import android.view.View;

public class ArticleWithFixedIllustrationPageTemplateView extends BasePageView {
	private BodyElementView bodyElementView;
	private BackgroundElementView backgroundElementView;

	public ArticleWithFixedIllustrationPageTemplateView(Context context,
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

		if (bodyElementView != null) {
			bodyElementView.setProgressDownloadElementView(progressElementView);
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
			}
			if (bodyElementView != null) {
				pageLayer.addView(bodyElementView.getView(getContext()));
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
	}

	@Override
	public void setDisactiveState() {

		state = State.DISACTIVE;

		if (backgroundElementView != null) {

			backgroundElementView.setState(state);

			if (bodyElementView != null)
				bodyElementView.setState(State.RELEASE);

		} else if (bodyElementView != null)
			bodyElementView.setState(state);

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
