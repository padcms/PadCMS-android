package padcms.magazine.page;

import padcms.dao.issue.bean.Page;
import padcms.magazine.factory.ElementViewFactory;
import padcms.magazine.page.elementview.BackgroundElementView;
import padcms.magazine.page.elementview.DragAndDropElementView;
import android.content.Context;
import android.view.View;

public class DragAndDropPageTemplateView extends BasePageView {

	private DragAndDropElementView dragAndDropElementView;
	private BackgroundElementView backgroundElementView;

	public DragAndDropPageTemplateView(Context context,
			Page pageBin) {
		super(context, pageBin);

	}

	@Override
	protected void initElementData(Context mContext) {
		super.initElementData(mContext);
		backgroundElementView = ElementViewFactory
				.getBackgroundElementView(elementViewCollection);
		dragAndDropElementView = ElementViewFactory
				.getDragAndDropElementView(elementViewCollection);
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
