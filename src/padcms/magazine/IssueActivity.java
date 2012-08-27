package padcms.magazine;

import padcms.application11.R;
import padcms.magazine.controls.switcher.RealViewSwitcherHorisontal;
import padcms.magazine.factory.IssueViewFactory;
import padcms.magazine.menu.MenuController;
import padcms.magazine.page.BasePageView;
import padcms.magazine.page.HorisontalPageView;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
import android.widget.RelativeLayout;

public class IssueActivity extends Activity {

	private RelativeLayout layoutConteinerView;
	private IssueViewFactory issueViewFactory;

	// private SmoothFliperView smoothFlipper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.issue_layout);
		layoutConteinerView = ((RelativeLayout) findViewById(R.id.RelativeLayoutIssueView));

		issueViewFactory = IssueViewFactory.getIssueViewFactoryIstance(this);
		issueViewFactory.initPageViewCollection(this);
		issueViewFactory.initData();
		// if (!issueViewFactory.isLandscapeAllow())
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// smoothFlipper = new SmoothFliperView(this);
		// layoutConteinerView.addView(smoothFlipper,
		// new RelativeLayout.LayoutParams(-1, -1));
		// ImageLoader.destroy();

		// ImageLoader
		// .getInstance(this)
		// .setCacheDir(
		// ApplicationFileHelper
		// .getFileRevisionResourcesFolder(IssueViewFactory.issueFolderName));

		issueViewFactory.playVideoCurrentPage(true);
		// setPageSmoothAdapter(false);
	}

	

	@Override
	protected void onRestart() {

		super.onRestart();
		// setPageSmoothAdapter(false);
		// issueViewFactory.setMenuController(MenuController.getIstance(this));

	}

	@Override
	protected void onStart() {
		super.onStart();

		issueViewFactory.setMenuController(MenuController.getIstance(this));
		setPageControllerOnCurrentDisplay(false);
		// issueViewFactory.getMenuController().configMenuController(this);

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (!issueViewFactory.isLandscapeAllow()) {
			issueViewFactory.getVerticalPageSwitcherController()
					.getCurrentPage().onConfiguretionChenged(this);

		} else {

			setPageControllerOnCurrentDisplay(true);
		}
		issueViewFactory.getMenuController().configMenuController(this);

	}

	// private void setPageSmoothAdapter(boolean isDisplayConfigChenge) {
	//
	// Display display = getWindowManager().getDefaultDisplay();
	//
	// if (display.getWidth() < display.getHeight()) {
	//
	// if (isDisplayConfigChenge
	// && issueViewFactory.getHorisontalPageAdapter()
	// .getCurrentPage() != null) {
	// HorisontalPageView currentHorisontalPageView = issueViewFactory
	// .getHorisontalPageAdapter().getCurrentPage();
	// if (currentHorisontalPageView != null
	// && currentHorisontalPageView.getRootPageView() != null)
	// issueViewFactory.getPageAdapter().setCurrentPage(
	// currentHorisontalPageView.getRootPageView());
	// }
	// // smoothFlipper.setAdapter(issueViewFactory.getPageAdapter());
	//
	// } else {
	//
	// if (isDisplayConfigChenge
	// && issueViewFactory.getPageAdapter().getCurrentPage() != null) {
	// BasePageView currentPageView = issueViewFactory
	// .getPageAdapter().getCurrentPage();
	// if (currentPageView != null
	// && currentPageView.getHorisontalPageView() != null)
	// issueViewFactory.getHorisontalPageAdapter().setCurrentPage(
	// currentPageView.getHorisontalPageView());
	// }
	// // if (issueViewFactory.getHorisontalPageAdapter() != null)
	// // smoothFlipper.setAdapter(issueViewFactory
	// // .getHorisontalPageAdapter());
	//
	// }
	// }

	private void setPageControllerOnCurrentDisplay(boolean onConfigChange) {

		Display display = getWindowManager().getDefaultDisplay();

		if (display.getWidth() > display.getHeight()) {

			if (issueViewFactory.getHorisontalPageSwitcherController() != null) {
				BasePageView currentVerticalPage = issueViewFactory
						.getVerticalPageSwitcherController().getCurrentPage();
				issueViewFactory.getVerticalPageSwitcherController()
						.releaseController();

				layoutConteinerView.removeAllViews();

				RealViewSwitcherHorisontal realViewSwitcherHorisontal = issueViewFactory
						.getHorisontalPageSwitcherController()
						.getRealViewSwitcherHorisontal();

				layoutConteinerView.addView(realViewSwitcherHorisontal,
						new RelativeLayout.LayoutParams(-1, -1));

				issueViewFactory.getHorisontalPageSwitcherController()
						.setCurrentPage(
								currentVerticalPage.getHorisontalPageView());

			}

		} else {

			if (issueViewFactory.getVerticalPageSwitcherController() != null) {
				BasePageView currentPage = issueViewFactory
						.getVerticalPageSwitcherController().getCurrentPage();
				HorisontalPageView currentHorisontalPage;
				if (onConfigChange
						&& issueViewFactory
								.getHorisontalPageSwitcherController() != null) {
					currentHorisontalPage = issueViewFactory
							.getHorisontalPageSwitcherController()
							.getCurrentPage();
					issueViewFactory.getHorisontalPageSwitcherController()
							.releaseController();
					currentPage = currentHorisontalPage.getRootPageView();
				}

				layoutConteinerView.removeAllViews();
				layoutConteinerView.addView(issueViewFactory
						.getVerticalPageSwitcherController()
						.getRealViewSwitcherVertical(),
						new RelativeLayout.LayoutParams(-1, -1));

				issueViewFactory.getVerticalPageSwitcherController()
						.setCurrentPage(currentPage);

			}

		}
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.hold_slide, R.anim.zoom_exit);

	}

	@Override
	protected void onDestroy() {
		issueViewFactory.destroy();
		super.onDestroy();
	}
}