package padcms.magazine.controls.switcher;

import padcms.magazine.factory.IssueViewFactory;
import padcms.magazine.page.BasePageView;
import android.content.Context;
import android.util.Log;
import android.view.View;

public class VerticalPageSwitcherController {
	// private RealViewSwitcherVertical realViewSwitcherVertical;

	private RealViewSwitcherHorisontal realViewSwitcherHorisontal;
	private BasePageView firstBasePage;

	public VerticalPageSwitcherController(Context mContext,
			BasePageView firstBasePage) {
		this.firstBasePage = firstBasePage;
		realViewSwitcherHorisontal = new RealViewSwitcherHorisontal(mContext);
		BasePageView page = firstBasePage;
		while (page != null) {

			BasePageView pageTop = page;
			if (pageTop.getBottomPage() != null) {
				RealViewSwitcherVertical realViewSwitcherVertical = new RealViewSwitcherVertical(
						mContext);
				while (pageTop != null) {
					Log.i("template:", "t:"
							+ pageTop.getPageBin().getTemplate());
					View viewVertical = pageTop.getView(mContext);
					// viewVertical.setTag(pageTop);
					realViewSwitcherVertical.addView(viewVertical);
					pageTop = pageTop.getBottomPage();

				}

				realViewSwitcherVertical
						.setOnScreenSwitchListener(verticalSwitcherListner);
				realViewSwitcherVertical.setTag(page);
				realViewSwitcherHorisontal.addView(realViewSwitcherVertical);
			} else {

				realViewSwitcherHorisontal.addView(page.getView(mContext));
			}
			page = page.getRightPage();
		}

		realViewSwitcherHorisontal
				.setOnScreenSwitchListener(horisontalSwitcherListner);

	}

	public BasePageView getCurrentPage() {
		return (BasePageView) realViewSwitcherHorisontal.getChildAt(
				realViewSwitcherHorisontal.mCurrentScreen).getTag();
	}

	public void setCurrentPage(BasePageView currentVerticalPage) {

		for (int i = 0; i < realViewSwitcherHorisontal.getChildCount(); i++) {
			BasePageView vericalPage = (BasePageView) realViewSwitcherHorisontal
					.getChildAt(i).getTag();

			if (vericalPage != null && vericalPage == currentVerticalPage) {
				currentVerticalPage.setDisactiveState();

				realViewSwitcherHorisontal.snapFastToSelectedScreen(i);
				releaseInactive();
				return;
			} else if (realViewSwitcherHorisontal.getChildAt(i) instanceof RealViewSwitcherVertical) {
				RealViewSwitcherVertical realVertical = (RealViewSwitcherVertical) realViewSwitcherHorisontal
						.getChildAt(i);
				BasePageView childePageViewVertical = (BasePageView) realVertical
						.getChildAt(0).getTag();
				if (childePageViewVertical != null
						&& childePageViewVertical == currentVerticalPage) {
					realVertical.snapFastToSelectedScreen(0);
					realViewSwitcherHorisontal.snapFastToSelectedScreen(i);
					releaseInactive();
					return;
				}
			}
		}
	}

	public void activateController() {
		BasePageView currentPage = (BasePageView) realViewSwitcherHorisontal
				.getChildAt(realViewSwitcherHorisontal.mCurrentScreen).getTag();
		if (currentPage != null) {
			currentPage.setActiveState();
			if (currentPage.getRightPage() != null) {
				currentPage.getRightPage().setDisactiveState();
			}
			if (currentPage.getLeftPage() != null) {
				currentPage.getLeftPage().setDisactiveState();
			}
			if (currentPage.getTopPage() != null) {
				currentPage.getTopPage().setDisactiveState();
			}
			if (currentPage.getBottomPage() != null) {
				currentPage.getBottomPage().setDisactiveState();
			}
		}
	}

	public RealViewSwitcherHorisontal getRealViewSwitcherVertical() {
		return realViewSwitcherHorisontal;
	}

	RealViewSwitcherHorisontal.OnScreenSwitchListener horisontalSwitcherListner = new BaseRealViewSwitcher.OnScreenSwitchListener() {

		@Override
		public void onScreenSwitched(int screen, View swichedView) {
			Log.i("onScreenSwitched", "screen:" + screen);
			IssueViewFactory.getIssueViewFactoryIstance(
					swichedView.getContext()	).setSensorConfiguration();
			BasePageView pageView = (BasePageView) swichedView.getTag();
			if (pageView != null) {
				pageView.setActiveState();

				if (pageView.getRightPage() != null) {
					BasePageView sidePage = pageView.getRightPage();
					sidePage.setDisactiveState();
					// setDisativaStateForSidePages(sidePage, pageView);

					setPageSidesReleaseState(sidePage, pageView);
				}

				if (pageView.getLeftPage() != null) {
					BasePageView sidePage = pageView.getLeftPage();
					sidePage.setDisactiveState();
					// setDisativaStateForSidePages(sidePage, pageView);
					setPageSidesReleaseState(pageView.getLeftPage(), pageView);
				}

				if (pageView.getTopPage() != null) {
					pageView.getTopPage().setDisactiveState();
					setPageSidesReleaseStateVertical(pageView.getTopPage(),
							pageView);
				}

				if (pageView.getBottomPage() != null) {
					pageView.getBottomPage().setDisactiveState();
					setPageSidesReleaseStateVertical(pageView.getBottomPage(),
							pageView);
				}
			}
		}

		// private void setDisativaStateForSidePages(BasePageView sidePage,
		// BasePageView pageView) {
		// if (sidePage.getRightPage() != null
		// && sidePage.getRightPage() != pageView) {
		// sidePage.setDisactiveState();
		// setPageSidesReleaseState(sidePage.getRightPage(), sidePage);
		// }
		//
		// if (sidePage.getLeftPage() != null
		// && sidePage.getLeftPage() != pageView) {
		// sidePage.getLeftPage().setDisactiveState();
		// setPageSidesReleaseState(sidePage.getLeftPage(), sidePage);
		// }
		//
		// // if (sidePage.getTopPage() != null) {
		// // sidePage.getTopPage().setDisactiveState();
		// // setPageSidesReleaseStateVertical(sidePage.getTopPage(),
		// // sidePage);
		// //
		// // }
		// //
		// // if (sidePage.getBottomPage() != null) {
		// // sidePage.getBottomPage().setDisactiveState();
		// // setPageSidesReleaseStateVertical(sidePage.getBottomPage(),
		// // sidePage);
		// // }
		// }
		//
		// @Override
		// public void startScreenSwitch(View swichedView) {
		// // BasePageView currentPage = getCurrentPage();
		// //
		// // BasePageView pageView = (BasePageView) swichedView.getTag();
		// //
		// // if (pageView != null) {
		// // // pageView.setActiveState();
		// // if (pageView.getRightPage() != null
		// // && pageView.getRightPage() != currentPage) {
		// // pageView.getRightPage().setDisactiveState();
		// // // setPageSidesReleaseState(pageView.getRightPage(),
		// // // pageView);
		// // }
		// //
		// // if (pageView.getLeftPage() != null
		// // && pageView.getLeftPage() != currentPage) {
		// // pageView.getLeftPage().setDisactiveState();
		// // // setPageSidesReleaseState(pageView.getLeftPage(),
		// // // pageView);
		// // }
		// //
		// // }
		// }

		@Override
		public void onReleseScreen(int screen) {
			Log.i("onReleseScreen", "screen:" + screen);

			BasePageView pageView = (BasePageView) realViewSwitcherHorisontal
					.getChildAt(screen).getTag();

			if (pageView != null) {
				pageView.setReleaseState();
				if (pageView.getRightPage() != null) {
					BasePageView sidePage = pageView.getRightPage();
					sidePage.setReleaseState();
				}

				if (pageView.getLeftPage() != null) {
					BasePageView sidePage = pageView.getLeftPage();
					sidePage.setReleaseState();

				}

				if (pageView.getTopPage() != null) {
					pageView.getTopPage().setReleaseState();
				}

				if (pageView.getBottomPage() != null) {
					pageView.getBottomPage().setReleaseState();

				}
			}
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

	RealViewSwitcherVertical.OnScreenSwitchListener verticalSwitcherListner = new RealViewSwitcherVertical.OnScreenSwitchListener() {

		@Override
		public void onScreenSwitched(int screen, View swichedView) {

			BasePageView pageView = (BasePageView) swichedView.getTag();

			if (pageView != null) {
				Log.i("verticalSwitcherListner", "screen:" + screen
						+ " pageID:" + pageView.getPageID());
				pageView.setActiveState();
				if (realViewSwitcherHorisontal
						.getChildAt(realViewSwitcherHorisontal.mCurrentScreen) instanceof RealViewSwitcherVertical)
					realViewSwitcherHorisontal.getChildAt(
							realViewSwitcherHorisontal.mCurrentScreen).setTag(
							pageView);

				if (pageView.getTopPage() != null) {
					BasePageView topSidePage = pageView.getTopPage();
					topSidePage.setDisactiveState();

					if (topSidePage.getRightPage() != null)
						pageView.setRightPage(topSidePage.getRightPage());

					if (topSidePage.getLeftPage() != null) {
						topSidePage.getLeftPage().setRightPage(pageView);
					}

					setPageSidesReleaseStateVertical(topSidePage, pageView);

				}

				if (pageView.getBottomPage() != null) {
					BasePageView bottomSidePage = pageView.getBottomPage();
					bottomSidePage.setDisactiveState();

					if (bottomSidePage.getRightPage() != null)
						pageView.setRightPage(bottomSidePage.getRightPage());

					if (bottomSidePage.getLeftPage() != null) {
						bottomSidePage.getLeftPage().setRightPage(pageView);
					}
					setPageSidesReleaseStateVertical(bottomSidePage, pageView);
				}

			}
		}

		// @Override
		// public void startScreenSwitch(View swichedView) {
		// // TODO Auto-generated method stub
		//
		// }

		@Override
		public void onReleseScreen(int screen) {

			Log.i("onReleseScreen verticalSwitcherListner", "screen:" + screen);
			BasePageView pageView = (BasePageView) realViewSwitcherHorisontal
					.getChildAt(screen).getTag();
			if (pageView != null) {
				pageView.setReleaseState();
				if (pageView.getRightPage() != null) {
					BasePageView sidePage = pageView.getRightPage();
					sidePage.setReleaseState();
				}

				if (pageView.getLeftPage() != null) {
					BasePageView sidePage = pageView.getLeftPage();
					sidePage.setReleaseState();
				}

				if (pageView.getTopPage() != null) {
					pageView.getTopPage().setReleaseState();
				}

				if (pageView.getBottomPage() != null) {
					pageView.getBottomPage().setReleaseState();

				}
			}
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

	public void setPageSidesReleaseState(BasePageView pageView,
			BasePageView currentPage) {
		if (pageView.getRightPage() != null
				&& pageView.getRightPage() != currentPage)
			pageView.getRightPage().setReleaseState();

		if (pageView.getLeftPage() != null
				&& pageView.getLeftPage() != currentPage)
			pageView.getLeftPage().setReleaseState();

		if (pageView.getTopPage() != null
				&& pageView.getTopPage() != currentPage)
			pageView.getTopPage().setReleaseState();

		if (pageView.getBottomPage() != null
				&& pageView.getBottomPage() != currentPage)
			pageView.getBottomPage().setReleaseState();

	}

	public void setPageSidesReleaseStateVertical(BasePageView pageView,
			BasePageView currentPage) {
		if (pageView.getTopPage() != null
				&& pageView.getTopPage() != currentPage)
			pageView.getTopPage().setReleaseState();

		if (pageView.getBottomPage() != null
				&& pageView.getBottomPage() != currentPage)
			pageView.getBottomPage().setReleaseState();

	}

	public void releaseController() {
		for (int i = 0; i < realViewSwitcherHorisontal.getChildCount(); i++) {
			BasePageView childePageView = (BasePageView) realViewSwitcherHorisontal
					.getChildAt(i).getTag();
			if (realViewSwitcherHorisontal.getChildAt(i) instanceof RealViewSwitcherVertical) {
				RealViewSwitcherVertical realVertical = (RealViewSwitcherVertical) realViewSwitcherHorisontal
						.getChildAt(i);
				for (int j = 0; j < realVertical.getChildCount(); j++) {
					BasePageView childePageViewVertical = (BasePageView) realViewSwitcherHorisontal
							.getChildAt(j).getTag();
					if (childePageViewVertical != null)
						childePageViewVertical.setReleaseState();

				}
			}
			if (childePageView != null)
				childePageView.setReleaseState();

		}

	}

	public void releaseInactive() {
		BasePageView currentPage = getCurrentPage();
		BasePageView page = firstBasePage;
		while (page != null) {

			BasePageView pageTop = page;

			if (pageTop.getBottomPage() != null) {
				while (pageTop != null) {
					if (pageTop != currentPage)
						pageTop.setReleaseState();
					pageTop = pageTop.getBottomPage();
				}
			} else {
				if (pageTop != currentPage)
					pageTop.setReleaseState();
			}
			page = page.getRightPage();
		}

	}

	public void swichToViewByBasePage(BasePageView pageView) {

		for (int i = 0; i < realViewSwitcherHorisontal.getChildCount(); i++) {
			BasePageView childePageView = (BasePageView) realViewSwitcherHorisontal
					.getChildAt(i).getTag();
			if (childePageView != null && childePageView == pageView) {
				pageView.setDisactiveState();
				if (realViewSwitcherHorisontal.getChildAt(i) instanceof RealViewSwitcherVertical) {
					RealViewSwitcherVertical realVertical = (RealViewSwitcherVertical) realViewSwitcherHorisontal
							.getChildAt(i);
					realVertical.snapFastToSelectedScreen(0);
				}
				realViewSwitcherHorisontal.snapToSelectedScreen(i);

				return;
			} else if (realViewSwitcherHorisontal.getChildAt(i) instanceof RealViewSwitcherVertical) {
				RealViewSwitcherVertical realVertical = (RealViewSwitcherVertical) realViewSwitcherHorisontal
						.getChildAt(i);
				BasePageView childePageViewVertical = (BasePageView) realVertical
						.getChildAt(0).getTag();
				if (childePageViewVertical != null
						&& childePageViewVertical == pageView) {
					childePageViewVertical.setDisactiveState();

					realVertical.snapFastToSelectedScreen(0);

					realViewSwitcherHorisontal.snapToSelectedScreen(i);
					return;
				}
			}
		}

	}
}
