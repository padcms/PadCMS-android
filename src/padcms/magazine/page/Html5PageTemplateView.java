package padcms.magazine.page;

import padcms.dao.issue.bean.Page;
import padcms.magazine.factory.ElementViewFactory;
import padcms.magazine.page.elementview.Html5ElementView;
import android.content.Context;
import android.view.View;

public class Html5PageTemplateView extends BasePageView {

	private Html5ElementView html5ElementView;

	public Html5PageTemplateView(Context context,
			Page pageBin) {
		super(context, pageBin);

	}

	@Override
	protected void initElementData(Context mContext) {
		super.initElementData(mContext);
		html5ElementView = ElementViewFactory
				.getHtml5ElementView(elementViewCollection);
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
