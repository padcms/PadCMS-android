package padcms.magazine.controls.switcher;

import java.util.ArrayList;

import padcms.dao.application.IssueFactory;
import padcms.dao.issue.MenuFactory;
import padcms.magazine.controls.switcher.BaseRealViewSwitcher.OnScreenSwitchListener;
import padcms.magazine.factory.IssueViewFactory;
import padcms.magazine.page.BasePageView;
import padcms.magazine.page.HorisontalPageView;
import android.content.Context;
import android.util.Log;
import android.view.View;

public class HorisontalPageSwitcherController {
	// private RealViewSwitcherVertical realViewSwitcherVertical;

	private RealViewSwitcherHorisontal realViewSwitcherHorisontal;
	ArrayList<HorisontalPageView> horisontalPageCollection;

	public HorisontalPageSwitcherController(Context mContext,
			ArrayList<HorisontalPageView> horisontalPageCollection) {
		this.horisontalPageCollection = horisontalPageCollection;
		realViewSwitcherHorisontal = new RealViewSwitcherHorisontal(mContext);

		for (HorisontalPageView horisontalPageView : this.horisontalPageCollection) {
			realViewSwitcherHorisontal.addView(horisontalPageView.getView(mContext));

		}

		realViewSwitcherHorisontal
				.setOnScreenSwitchListener(horisontalSwitcherListner);

	}

	public RealViewSwitcherHorisontal getRealViewSwitcherHorisontal() {
		return realViewSwitcherHorisontal;
	}

	RealViewSwitcherHorisontal.OnScreenSwitchListener horisontalSwitcherListner = new BaseRealViewSwitcher.OnScreenSwitchListener() {

		@Override
		public void onScreenSwitched(int screen, View swichedView) {
			activateSelectedView();
		}

		@Override
		public void onReleseScreen(int screen) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onScreenSingleTap() {
			BasePageView pageView = (BasePageView) realViewSwitcherHorisontal
					.getChildAt(realViewSwitcherHorisontal.mCurrentScreen)
					.getTag();
			if (pageView != null)
				pageView.pageViewClicked();
		}
	};

	public void activateSelectedView() {

		for (int i = 0; i < realViewSwitcherHorisontal.getChildCount(); i++) {
			if (i == realViewSwitcherHorisontal.mCurrentScreen) {
				HorisontalPageView horisontalPage = getPageObjectInView(i);
				if (horisontalPage != null)
					horisontalPage.setActiveState();
			} else if (i >= realViewSwitcherHorisontal.mCurrentScreen - 2
					&& i <= realViewSwitcherHorisontal.mCurrentScreen + 2) {
				HorisontalPageView horisontalPage = getPageObjectInView(i);
				if (horisontalPage != null)
					horisontalPage.setDisactiveState();
			} else {
				HorisontalPageView horisontalPage = getPageObjectInView(i);
				if (horisontalPage != null)
					horisontalPage.setReleaseState();
			}
		}

	}

	public HorisontalPageView getCurrentPage() {
		return getPageObjectInView(realViewSwitcherHorisontal.mCurrentScreen);
	}

	public void setCurrentPage(HorisontalPageView currentHorisontalPage) {
		currentHorisontalPage.setDisactiveState();
		for (int i = 0; i < realViewSwitcherHorisontal.getChildCount(); i++) {
			HorisontalPageView horisontalPage = getPageObjectInView(i);
			if (horisontalPage != null
					&& horisontalPage == currentHorisontalPage) {
				Log.e("was find and macke scroll", "make");
				realViewSwitcherHorisontal.snapFastToSelectedScreen(i);
				return;
			}
		}
	}

	public HorisontalPageView getPageObjectInView(int viewPosition) {
		if (viewPosition >= 0
				&& viewPosition <= realViewSwitcherHorisontal.getChildCount() - 1)
			return (HorisontalPageView) realViewSwitcherHorisontal.getChildAt(
					viewPosition).getTag();
		return null;
	}

	public void swichToViewByBasePage(HorisontalPageView pageView) {
		pageView.setDisactiveState();

		for (int i = 0; i < realViewSwitcherHorisontal.getChildCount(); i++) {
			HorisontalPageView horisontalPage = getPageObjectInView(i);
			if (horisontalPage != null && horisontalPage == pageView) {
				realViewSwitcherHorisontal.snapToSelectedScreen(i);
				return;
			}
		}
	}

	public void releaseController() {
		for (int i = 0; i < realViewSwitcherHorisontal.getChildCount(); i++) {

			HorisontalPageView horisontalPage = getPageObjectInView(i);
			if (horisontalPage != null)
				horisontalPage.setReleaseState();

		}
	}

}
