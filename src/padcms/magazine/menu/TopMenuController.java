package padcms.magazine.menu;

import padcms.application11.R;
import padcms.bll.ApplicationManager;
import padcms.bll.ApplicationSkinFactory;
import padcms.magazine.DialogHelp;
import padcms.magazine.factory.IssueViewFactory;
import padcms.magazine.factory.ResourceFactory;
import padcms.magazine.menu.elementView.VerticalStripeMenuElementView;
import padcms.magazine.page.BasePageView;
import padcms.posting.email.EmailListener;
import padcms.posting.facebook.FacebookListener;
import padcms.posting.twitter.TwitterListener;
import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class TopMenuController {

	private ViewGroup rootTopMenu;
	// private Timer menuDisappearanceTimer;
	private LinearLayout topMenuSummaryLayout;
	private LinearLayout topMenuSocialLayout;
	private ImageButton homeButtonImage;
	private ImageButton summaryMenuButtonImage;;
	private ImageButton socialMenuButtonImage;
	private ImageButton helpButtonImage;
	private LinearLayout twitterLayout;
	private LinearLayout faceBookLayout;
	private LinearLayout mailButtonLayout;
	private ListView stripeListView;
	private IssueViewFactory issueViewFactory;
	private DialogHelp dialogHelp;
	private MenuController rootMenuController;

	public TopMenuController(ViewGroup rootTopMenu) {

		this.rootTopMenu = rootTopMenu;
		issueViewFactory = IssueViewFactory
				.getIssueViewFactoryIstance(rootTopMenu.getContext());
		initMenuControls(rootTopMenu);

		configTopMenuSommaryPopUp(rootTopMenu.getContext());

		setClickListners();

	}

	public void initDataVertical() {

		String helpPageSourse = issueViewFactory.getRevisionBin()
				.getHelp_page_vertical();

		if (helpPageSourse != null && helpPageSourse.length() > 0) {
			helpButtonImage.setVisibility(View.VISIBLE);
			((RelativeLayout.LayoutParams) topMenuSocialLayout
					.getLayoutParams()).rightMargin = (int) (40 * ApplicationManager
					.getDensity());

		} else {
			helpButtonImage.setVisibility(View.GONE);
			((RelativeLayout.LayoutParams) topMenuSocialLayout
					.getLayoutParams()).rightMargin = 0;
		}

		if (dialogHelp != null && dialogHelp.isShowing())
			dialogHelp.onShow(null);
		if (issueViewFactory.getMenuBinCollection().size() > 0) {
			TopMenuListAdapter menuAdapter = new TopMenuListAdapter(
					rootTopMenu.getContext(),
					issueViewFactory.getMenuBinCollection());
			stripeListView.setAdapter(menuAdapter);
		}
		summaryMenuButtonImage.setVisibility(View.VISIBLE);
		ApplicationSkinFactory.animateHideFast(rootTopMenu);

	}

	public void initDataHorisontal() {

		String helpPageSourse = issueViewFactory.getRevisionBin()
				.getHelp_page_horizontal();

		if (helpPageSourse != null && helpPageSourse.length() > 0) {

			helpButtonImage.setVisibility(View.VISIBLE);
			((RelativeLayout.LayoutParams) topMenuSocialLayout
					.getLayoutParams()).rightMargin = (int) (40 * ApplicationManager
					.getDensity());
		} else {

			helpButtonImage.setVisibility(View.GONE);
			((RelativeLayout.LayoutParams) topMenuSocialLayout
					.getLayoutParams()).rightMargin = 0;

		}

		if (dialogHelp != null && dialogHelp.isShowing())
			dialogHelp.onShow(null);

		if (stripeListView.getAdapter() != null) {
			((TopMenuListAdapter) stripeListView.getAdapter()).destroy();
		}

		stripeListView.setAdapter(null);
		summaryMenuButtonImage.setVisibility(View.GONE);

		ApplicationSkinFactory.animateHideFast(rootTopMenu);

	}

	public void destroyData() {

		ApplicationSkinFactory.animateHideFast(rootTopMenu);

		stripeListView.setAdapter(null);
	}

	private void initMenuControls(ViewGroup rootTopMenu) {
		topMenuSummaryLayout = (LinearLayout) rootTopMenu
				.findViewById(R.id.Top_menu_summary_layout);
		topMenuSocialLayout = (LinearLayout) rootTopMenu
				.findViewById(R.id.Top_menu_social_layout);

		homeButtonImage = ((ImageButton) rootTopMenu
				.findViewById(R.id.ImageButton_Home_button));

		socialMenuButtonImage = ((ImageButton) rootTopMenu
				.findViewById(R.id.ImageButton_SocialMenu_button));
		helpButtonImage = ((ImageButton) rootTopMenu
				.findViewById(R.id.ImageButton_Help_button));
		stripeListView = (ListView) rootTopMenu.findViewById(R.id.summary_list);
		summaryMenuButtonImage = ((ImageButton) rootTopMenu
				.findViewById(R.id.ImageButton_Summary_button));
		twitterLayout = (LinearLayout) rootTopMenu
				.findViewById(R.id.ImageViewTwitter);
		faceBookLayout = (LinearLayout) rootTopMenu
				.findViewById(R.id.ImageViewFaceBook);
		mailButtonLayout = (LinearLayout) rootTopMenu
				.findViewById(R.id.ImageViewEmail);
	}

	private void setClickListners() {
		homeButtonImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((Activity) v.getContext()).onBackPressed();

				rootMenuController.hideMenu();
			}
		});

		summaryMenuButtonImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				rootMenuController.menuDisappearanceTimerRun();

				if (topMenuSummaryLayout.getVisibility() == View.GONE) {
					topMenuSummaryLayout.setVisibility(View.VISIBLE);
					topMenuSummaryLayout.bringToFront();
					if (stripeListView.getAdapter() != null) {
						((TopMenuListAdapter) stripeListView.getAdapter())
								.activate();
					}
					topMenuSocialLayout.setVisibility(View.GONE);
				} else {
					topMenuSummaryLayout.setVisibility(View.GONE);
				}
			}
		});

		socialMenuButtonImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				rootMenuController.menuDisappearanceTimerRun();

				if (topMenuSocialLayout.getVisibility() == View.GONE) {
					topMenuSocialLayout.setVisibility(View.VISIBLE);
					topMenuSocialLayout.bringToFront();
					topMenuSummaryLayout.setVisibility(View.GONE);
				} else {
					topMenuSocialLayout.setVisibility(View.GONE);
				}
			}
		});

		helpButtonImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rootMenuController.hideMenu();
				if (dialogHelp == null)
					dialogHelp = new DialogHelp(v.getContext());
				dialogHelp.show();
			}
		});

		stripeListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View view,
							int arg2, long arg3) {

						VerticalStripeMenuElementView menuItemElement = (VerticalStripeMenuElementView) view
								.getTag();

						BasePageView pageView = menuItemElement
								.getParentPageView();
						issueViewFactory.flipToPage(pageView);

						rootMenuController.hideMenu();

					}
				});

		stripeListView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				rootMenuController.menuDisappearanceTimerRun();
				return false;
			}
		});

		twitterLayout.setOnClickListener(new TwitterListener(rootTopMenu
				.getContext(), issueViewFactory.getApplicationBin()
				.getNm_twitter()));
		faceBookLayout.setOnClickListener(new FacebookListener(rootTopMenu
				.getContext(), issueViewFactory.getApplicationBin()
				.getNm_fbook()));
		mailButtonLayout.setOnClickListener(new EmailListener(rootTopMenu
				.getContext(), issueViewFactory.getApplicationBin()
				.getNt_email(), issueViewFactory.getApplicationBin()
				.getNm_email()));

	}

	public MenuController getRootMenuController() {
		return rootMenuController;
	}

	public void setRootMenuController(MenuController rootMenuController) {
		this.rootMenuController = rootMenuController;
	}

	private void configTopMenuSommaryPopUp(Context mContext) {
		Display display = ((Activity) mContext).getWindowManager()
				.getDefaultDisplay();
		int height = Math.max(display.getWidth(), display.getHeight()) / 2;
		topMenuSummaryLayout.getLayoutParams().height = height;

	}

	public void showMenu() {
		ApplicationSkinFactory.animateShow(rootTopMenu);
		setEnableControls(true);
		if (ResourceFactory.isAllMenuSummeryDownloaded()
				&& stripeListView.getAdapter() != null
				&& stripeListView.getAdapter().getCount() > 0) {
			summaryMenuButtonImage.setVisibility(View.VISIBLE);
			summaryMenuButtonImage.setClickable(true);
		} else {
			summaryMenuButtonImage.setVisibility(View.INVISIBLE);
			summaryMenuButtonImage.setClickable(false);
		}

	}

	public void hideMenu() {
		setEnableControls(false);
		topMenuSocialLayout.setVisibility(View.GONE);
		topMenuSummaryLayout.setVisibility(View.GONE);
		ApplicationSkinFactory.animateHide(rootTopMenu);
		if (stripeListView.getAdapter() != null) {
			((TopMenuListAdapter) stripeListView.getAdapter()).destroy();
		}
	}

	private void setEnableControls(boolean isEnabled) {

		homeButtonImage.setEnabled(isEnabled);
		socialMenuButtonImage.setEnabled(isEnabled);
		helpButtonImage.setEnabled(isEnabled);
		summaryMenuButtonImage.setEnabled(isEnabled);

	}

}
