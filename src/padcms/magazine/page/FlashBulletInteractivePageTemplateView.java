package padcms.magazine.page;

import padcms.dao.issue.bean.Page;
import padcms.magazine.factory.ElementViewFactory;
import padcms.magazine.page.elementview.BackgroundElementView;
import padcms.magazine.page.elementview.BodyElementView;
import padcms.magazine.page.elementview.MiniArticleElementView;
import android.content.Context;
import android.view.View;

public class FlashBulletInteractivePageTemplateView extends BasePageView {

	private BackgroundElementView backgroundElementView;
	private BodyElementView bodyElementView;
	private MiniArticleElementView miniArticleElementView;

	public FlashBulletInteractivePageTemplateView(Context context,
			Page pageBin) {
		super(context, pageBin);
	}

	@Override
	protected void initElementData(Context mContext) {
		super.initElementData(mContext);
		backgroundElementView = ElementViewFactory
				.getBackgroundElementView(elementViewCollection);
		bodyElementView = ElementViewFactory
				.getBodyElementView(elementViewCollection);

	}

	@Override
	public View getView(Context mContext) {
		if (pageViewLayer == null) {
			initElementData(mContext);
			super.getView(mContext);

		}
		return pageViewLayer;
	}

}
