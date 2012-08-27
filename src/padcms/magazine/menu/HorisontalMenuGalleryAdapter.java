package padcms.magazine.menu;

import java.util.ArrayList;

import padcms.magazine.menu.elementView.HorisontalMenuElementView;
import padcms.magazine.page.HorisontalPageView;
import padcms.magazine.page.State;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class HorisontalMenuGalleryAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HorisontalMenuElementView> horisontalMenuArray;

	public HorisontalMenuGalleryAdapter() {
		super();
	}

	public HorisontalMenuGalleryAdapter(Context mContext,
			ArrayList<HorisontalPageView> horisontalPageArray) {
		this.mContext = mContext;

		this.horisontalMenuArray = new ArrayList<HorisontalMenuElementView>();
		for (HorisontalPageView horisontalPageItem : horisontalPageArray) {
			String horisontalPageResource = horisontalPageItem
					.getHorisontalpageBin().getResource();
			if (horisontalPageResource != null
					&& horisontalPageResource.length() != 0) {
				HorisontalMenuElementView menuElementView = new HorisontalMenuElementView(
						horisontalPageItem, horisontalPageResource);
				horisontalMenuArray.add(menuElementView);
				menuElementView.getView(mContext);
			}
		}
	}

	@Override
	public int getCount() {

		return horisontalMenuArray.size();
	}

	@Override
	public Object getItem(int position) {

		return horisontalMenuArray.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {

		HorisontalMenuElementView horisontalMenuElementView = horisontalMenuArray
				.get(position);
		horisontalMenuElementView.setState(State.ACTIVE);
		return horisontalMenuElementView.getView(mContext);
	}

	public void activate() {
		if (horisontalMenuArray != null)
			for (HorisontalMenuElementView menuItem : horisontalMenuArray) {
				menuItem.setState(State.ACTIVE);

			}
	}

	public void destroy() {
		if (horisontalMenuArray != null)
			for (HorisontalMenuElementView menuItem : horisontalMenuArray) {
				menuItem.setState(State.RELEASE);
			}
	}
}
