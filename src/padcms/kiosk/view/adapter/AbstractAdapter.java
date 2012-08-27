package padcms.kiosk.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class AbstractAdapter extends BaseAdapter
{
 	protected View[] views;
		
	
	public int getCount()
	{
		return views.length;
	}

	
	public View getItem(int position)
	{
		return views[position];
	}

	
	public long getItemId(int position)
	{
		return position;
	}

	
	public abstract View getView(int position, View convertView, ViewGroup parent);   
}
