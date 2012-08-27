package padcms.magazine.menu;

import java.util.ArrayList;

import padcms.application11.R;
import padcms.dao.issue.bean.Menu;
import padcms.magazine.factory.IssueViewFactory;
import padcms.magazine.menu.elementView.VerticalStripeMenuElementView;
import padcms.magazine.page.BasePageView;
import padcms.magazine.page.State;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class TopMenuListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<Menu> arrayMenu;
	private ArrayList<View> arrayView;

	public TopMenuListAdapter(Context mContext, ArrayList<Menu> arrayMenu) {
		mInflater = LayoutInflater.from(mContext);
		this.arrayMenu = arrayMenu;
		arrayView = new ArrayList<View>();
		IssueViewFactory issueViewFactory = IssueViewFactory
				.getIssueViewFactoryIstance(mContext);

		for (Menu menuItem : arrayMenu) {
			BasePageView pageView = issueViewFactory
					.findPageViewByPageID(menuItem.getFirstpage_id().intValue());
			if (pageView != null && menuItem.getThumb_summary() != null
					&& menuItem.getThumb_summary().length() > 0) {
				View view = mInflater.inflate(R.layout.summary_list_item, null);
				Log.i("summery image:", ":" + menuItem.getThumb_summary());
				VerticalStripeMenuElementView menuElementView = new VerticalStripeMenuElementView(
						pageView, menuItem.getThumb_summary());

				ViewGroup containerView = (ViewGroup) view
						.findViewById(R.id.summary_list_item_image_container);
				containerView.addView(menuElementView.getView(mContext));
				menuElementView.setState(State.DOWNLOAD);
				TextView textView = (TextView) view
						.findViewById(R.id.summary_list_item_text);

				textView.setText(menuItem.getTitle());

				textView.setTag(menuItem);
				view.setTag(menuElementView);

				arrayView.add(view);
			}
		}
	}

	@Override
	public int getCount() {
		return arrayView.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayView.get(position).getTag();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		VerticalStripeMenuElementView menuElementView = (VerticalStripeMenuElementView) arrayView
				.get(position).getTag();
		menuElementView.setState(State.ACTIVE);

		return arrayView.get(position);
	}

	public void activate() {
		if (arrayView != null)
			for (View view : arrayView) {
				((VerticalStripeMenuElementView) view.getTag())
						.setState(State.ACTIVE);

			}
	}

	public void destroy() {
		if (arrayView != null)
			for (View view : arrayView) {
				((VerticalStripeMenuElementView) view.getTag())
						.setState(State.RELEASE);
			}
	}

}
