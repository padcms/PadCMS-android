package padcms.magazine.page;

import padcms.dao.issue.bean.Page;
import padcms.magazine.factory.ElementViewFactory;
import padcms.magazine.page.elementview.BodyElementView;
import padcms.magazine.page.elementview.HtmlElementView;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

public class HTMLPageTemplateView extends BasePageView {

	private HtmlElementView htmlElementView;
	private BodyElementView bodyElementView;

	public HTMLPageTemplateView(Context context,
			Page pageBin) {
		super(context, pageBin);

		// rebuildView(context);
	}

	@Override
	protected void initElementData(Context mContext) {
		super.initElementData(mContext);
		bodyElementView = ElementViewFactory
				.getBodyElementView(elementViewCollection);
		htmlElementView = ElementViewFactory
				.getHtmlElementView(elementViewCollection);

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

				setPageWidth((int) bodyElementView.getWidth());
				setPageHeight((int) bodyElementView.getHeight());

				bodyElementView.setElementBackgroundColor(Color.WHITE);
			}
			addPhotoGallaryButton(mContext);
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
