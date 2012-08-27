package padcms.magazine.menu;

import padcms.application11.R;
import padcms.bll.ApplicationSkinFactory;
import padcms.magazine.factory.IssueViewFactory;
import padcms.magazine.factory.ResourceFactory;
import padcms.magazine.menu.elementView.VerticalSummaryMenuElementView;
import padcms.magazine.page.BasePageView;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class BottomMenuController {

	private ViewGroup rootBottomMenu;
	private RelativeLayout gallarySripeMenuContainer;
	private ImageButton somairMenuButtonImage;
	private Gallery gallarySummaryView;
	// private HorizontalScrollView scrollViewSripeMenu;
	// private LinearLayout litearLayoutSripeMenu;
	private MenuController rootMenuController;

	private IssueViewFactory issueViewFactory;
	boolean isVisibleBottomMenu;
	int height;

	private Animation open, close;

	public BottomMenuController(ViewGroup rootBottomMenu) {

		this.rootBottomMenu = rootBottomMenu;
		issueViewFactory = IssueViewFactory
				.getIssueViewFactoryIstance(rootBottomMenu.getContext());

		initMenuControls(rootBottomMenu);

		configTopMenuSommaryPopUp(rootBottomMenu.getContext());

		setClickListners();
		initAnimations();

		if (issueViewFactory.getIssueColor() != Color.WHITE)
			somairMenuButtonImage.getBackground().setColorFilter(
					issueViewFactory.getIssueColor(), PorterDuff.Mode.MULTIPLY);

	}

	public void initData() {
		BottomMenuGalleryAdapter menuAdapter = new BottomMenuGalleryAdapter(
				rootBottomMenu.getContext(),
				issueViewFactory.getMenuBinCollection());
		gallarySummaryView.setAdapter(menuAdapter);
		ApplicationSkinFactory.animateHideFast(rootBottomMenu);
		setEnableControls(false);

	}

	public void destroyData() {
		if (gallarySummaryView.getAdapter() != null) {
			((BottomMenuGalleryAdapter) gallarySummaryView.getAdapter())
					.destroy();
		}
		gallarySummaryView.setAdapter(null);

		ApplicationSkinFactory.animateHideFast(rootBottomMenu);
		setEnableControls(false);
	}

	private void initAnimations() {
		open = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
				0.82f, Animation.RELATIVE_TO_SELF, 0);
		open.setDuration(200);
		open.setFillAfter(true);
		open.setFillBefore(true);
		open.setFillEnabled(true);

		close = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0.82f);
		close.setDuration(200);
		close.setFillAfter(false);
		close.setFillBefore(true);
		close.setFillEnabled(true);
		close.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				gallarySripeMenuContainer.setVisibility(View.GONE);

			}
		});
	}

	private void initMenuControls(ViewGroup rootTopMenu) {

		somairMenuButtonImage = ((ImageButton) rootTopMenu
				.findViewById(R.id.ImageButtonStripeMenu));
		gallarySripeMenuContainer = (RelativeLayout) rootTopMenu
				.findViewById(R.id.RelativeStripeMenuGapparyContainer);

		gallarySummaryView = (Gallery) rootTopMenu
				.findViewById(R.id.GallerySripeMenu);

	}

	private void setClickListners() {

		somairMenuButtonImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showHideGallaryMenu();
				rootMenuController.menuDisappearanceTimerRun();

			}
		});
		gallarySummaryView
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						rootMenuController.menuDisappearanceTimerRun();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		gallarySummaryView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapter, View view,
							int position, long arg3) {

						VerticalSummaryMenuElementView menuElementView = (VerticalSummaryMenuElementView) adapter
								.getItemAtPosition(position);

						BasePageView pageView = menuElementView
								.getParentPageView();

						issueViewFactory.flipToPage(pageView);

						rootMenuController.hideMenu();
					}
				});

	}

	private void configTopMenuSommaryPopUp(Context mContext) {
		// Display display = ((Activity) mContext).getWindowManager()
		// .getDefaultDisplay();
		// height = Math.max(display.getWidth(), display.getHeight()) / 2;
		// ((RelativeLayout.LayoutParams)
		// gallarySummaryView.getLayoutParams()).leftMargin = -display
		// .getWidth() / 2;
		// ((RelativeLayout.LayoutParams)
		// gallarySummaryView.getLayoutParams()).rightMargin = (int) (display
		// .getWidth() * 3 / 2.f);
		// ((RelativeLayout.LayoutParams)
		// gallarySummaryView.getLayoutParams()).width=display.getWidth()*2;

		gallarySummaryView.getLayoutParams().height = -2;
		gallarySripeMenuContainer.getLayoutParams().height = -2;

	}

	public MenuController getRootMenuController() {
		return rootMenuController;
	}

	public void setRootMenuController(MenuController rootMenuController) {
		this.rootMenuController = rootMenuController;
	}

	public void showMenu() {
		if (ResourceFactory.isAllMenuSummeryDownloaded()) {
			ApplicationSkinFactory.animateShow(rootBottomMenu);
			setEnableControls(true);
		}

	}

	public void hideMenu() {
		if (ResourceFactory.isAllMenuSummeryDownloaded()) {
			if (gallarySripeMenuContainer.getVisibility() != View.GONE) {
				setForegraund(true);
				gallarySripeMenuContainer.setVisibility(View.GONE);
				// if (gallarySummaryView.getAdapter() != null) {
				// ((BottomMenuGalleryAdapter) gallarySummaryView.getAdapter())
				// .destroy();
				// }
			}
			if (rootBottomMenu.getVisibility() != View.GONE) {
				setEnableControls(false);
				ApplicationSkinFactory.animateHide(rootBottomMenu);
			}
		}
	}

	public void showHideGallaryMenu() {
		if (gallarySripeMenuContainer.getVisibility() == View.GONE) {

			animateShowBottomSommary();
		} else {
			animateHideBottomSommary();
		}

	}

	public void animateHideBottomSommary() {
		if (gallarySripeMenuContainer != null
				&& gallarySripeMenuContainer.getVisibility() != View.GONE) {
			setForegraund(true);
			rootBottomMenu.startAnimation(close);
			// if (gallarySummaryView.getAdapter() != null) {
			// ((BottomMenuGalleryAdapter) gallarySummaryView.getAdapter())
			// .destroy();
			// }
		}
	}

	public void animateShowBottomSommary() {
		gallarySripeMenuContainer.setVisibility(View.VISIBLE);
		setForegraund(false);
		if (gallarySummaryView.getAdapter() != null) {
			((BottomMenuGalleryAdapter) gallarySummaryView.getAdapter())
					.activate();
			int height2 = ((BottomMenuGalleryAdapter) gallarySummaryView
					.getAdapter()).getHeight();
			gallarySummaryView.getLayoutParams().height = height2;
			gallarySripeMenuContainer.getLayoutParams().height =  height2;
		}
		rootBottomMenu.startAnimation(open);
		
	}

	private void setForegraund(boolean isDown) {
		if (isDown) {
			if (issueViewFactory.getIssueColor() != Color.WHITE) {
				somairMenuButtonImage.getBackground().setColorFilter(
						issueViewFactory.getIssueColor(),
						PorterDuff.Mode.MULTIPLY);
			} else
				somairMenuButtonImage.getBackground().setColorFilter(null);
		} else {
			somairMenuButtonImage.getBackground().setColorFilter(0xFFAAAAAA,
					PorterDuff.Mode.MULTIPLY);
			// somairMenuButtonImage.setColorFilter(0xFFFFFFFF);
		}

	}

	private void setEnableControls(boolean isEnabled) {

		somairMenuButtonImage.setEnabled(isEnabled);

	}

}
