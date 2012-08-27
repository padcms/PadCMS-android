package padcms.magazine.page.elementview;

import padcms.dao.issue.bean.Element;
import padcms.magazine.page.BasePageView;
import android.content.Context;
import android.view.View;

/**
 * advert
 * 
 * The PDF, JPEG, PNG with advertising that will display within advert_duration
 * seconds
 * 
 * resource (string) relative path to the resource advert_duration (int) time
 * inseconds
 * 
 */
public class AdvertElementView extends BaseElementView {

	public AdvertElementView(BasePageView parentPageView, Element element) {
		super(parentPageView, element);

	}

	private int advertDurationSec;

	public void setAdvertDurationSec(int advertDurationSec) {
		this.advertDurationSec = advertDurationSec;
	}

	public int getAdvertDurationSec() {
		return advertDurationSec;
	}

	@Override
	public View getView(Context context) {
		// TODO
		return null;
	}

	@Override
	public View getShowingView() {
		// TODO Auto-generated method stub
		return null;
	}
}
