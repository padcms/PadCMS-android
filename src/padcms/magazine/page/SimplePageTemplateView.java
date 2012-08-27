package padcms.magazine.page;

import padcms.dao.issue.bean.Page;
import padcms.magazine.factory.ElementViewFactory;
import padcms.magazine.page.elementview.BodyElementView;
import android.content.Context;
import android.view.View;

public class SimplePageTemplateView extends BasePageView {

	private BodyElementView bodyElementView;

	public SimplePageTemplateView(Context context,
			Page pageBin) {
		super(context, pageBin);

	}

	@Override
	protected void initElementData(Context mContext) {
		super.initElementData(mContext);
		bodyElementView = ElementViewFactory
				.getBodyElementView(elementViewCollection);

		if (bodyElementView != null)
			bodyElementView.setProgressDownloadElementView(progressElementView);

	}

	@Override
	public View getView(Context mContext) {
		if (pageViewLayer == null) {
			initElementData(mContext);
			super.getView(mContext);
			if (bodyElementView != null) {
				pageLayer.addView(bodyElementView.getView(getContext()), 0);

			}
		}
		return pageViewLayer;
	}

	@Override
	public void setActiveState() {
		state = State.ACTIVE;
		if (bodyElementView != null)
			bodyElementView.setState(state);

	}

	@Override
	public void setDisactiveState() {
		state = State.DISACTIVE;
		if (bodyElementView != null)
			bodyElementView.setState(state);
	}

	@Override
	public void setReleaseState() {
		state = State.RELEASE;
		if (bodyElementView != null)
			bodyElementView.setState(state);

	}

}
