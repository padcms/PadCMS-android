package padcms.magazine.menu;

import java.util.ArrayList;

import padcms.dao.issue.bean.Menu;
import padcms.magazine.factory.IssueViewFactory;
import padcms.magazine.menu.elementView.VerticalSummaryMenuElementView;
import padcms.magazine.page.BasePageView;
import padcms.magazine.page.State;
import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BottomMenuGalleryAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Menu> arrayMenu;
	private ArrayList<VerticalSummaryMenuElementView> arrayElementView;

	int height = -2;

	public BottomMenuGalleryAdapter() {
		super();
	}

	public BottomMenuGalleryAdapter(Context mContext, ArrayList<Menu> arrayMenu) {
		this.mContext = mContext;
		Display defaultDisplay = ((Activity) mContext).getWindowManager()
				.getDefaultDisplay();

		// width = defaultDisplay.getWidth() / 4;
		// height = defaultDisplay.getHeight() / 2;

		this.arrayMenu = arrayMenu;
		arrayElementView = new ArrayList<VerticalSummaryMenuElementView>();
		IssueViewFactory issueViewFactory = IssueViewFactory
				.getIssueViewFactoryIstance(mContext);
		for (Menu menuItem : arrayMenu) {
			if (menuItem.getThumb_stripe() != null
					&& menuItem.getThumb_stripe().length() != 0) {
				BasePageView pageView = issueViewFactory
						.findPageViewByPageID(menuItem.getFirstpage_id()
								.intValue());
				if (pageView != null) {
					VerticalSummaryMenuElementView verticalMenuElement = new VerticalSummaryMenuElementView(
							pageView, menuItem.getThumb_stripe());
					verticalMenuElement.setState(State.DOWNLOAD);
					arrayElementView.add(verticalMenuElement);
					verticalMenuElement.getView(mContext);
				}
			}
		}
	}

	public int getHeight() {
		// if (height == -2) {
		if (arrayElementView != null)
			for (VerticalSummaryMenuElementView menuItem : arrayElementView) {
				if (menuItem.getMenuHeight() != -2
						&& menuItem.getMenuHeight() > height) {
					height = menuItem.getMenuHeight();
				}

			}
		// }
		return height;
	}

	@Override
	public int getCount() {

		return arrayElementView.size();
	}

	@Override
	public Object getItem(int position) {

		return arrayElementView.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {

		VerticalSummaryMenuElementView elementView = arrayElementView
				.get(position);
		elementView.setState(State.ACTIVE);
		return elementView.getView(mContext);

	}

	public void activate() {
		if (arrayElementView != null)
			for (VerticalSummaryMenuElementView menuItem : arrayElementView) {
				menuItem.setState(State.ACTIVE);

			}
	}

	public void destroy() {
		if (arrayElementView != null)
			for (VerticalSummaryMenuElementView menuItem : arrayElementView) {
				menuItem.setState(State.RELEASE);
			}
	}

}
