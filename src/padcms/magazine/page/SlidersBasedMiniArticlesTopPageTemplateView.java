package padcms.magazine.page;

import padcms.dao.issue.bean.Page;
import padcms.magazine.factory.ElementViewFactory;
import padcms.magazine.page.elementview.BackgroundElementView;
import padcms.magazine.page.elementview.MiniArticleElementView;
import android.content.Context;
import android.view.View;

public class SlidersBasedMiniArticlesTopPageTemplateView extends BasePageView {

	private BackgroundElementView backgroundElementView;
	private MiniArticleElementView miniArticleElementView;

	public SlidersBasedMiniArticlesTopPageTemplateView(Context context,
			Page pageBin) {
		super(context, pageBin);

		backgroundElementView = ElementViewFactory
				.getBackgroundElementView(elementViewCollection);
		// miniArticleElementView= ElementViewFactory
		// .getMiniArticleElementView(elementViewCollection);
	}

	@Override
	protected void initElementData(Context mContext) {
		super.initElementData(mContext);
		backgroundElementView = ElementViewFactory
				.getBackgroundElementView(elementViewCollection);
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
