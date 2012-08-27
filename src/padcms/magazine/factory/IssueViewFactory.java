package padcms.magazine.factory;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import padcms.application11.R;
import padcms.bll.ApplicationFileHelper;
import padcms.dao.application.bean.Application;
import padcms.dao.application.bean.Revision;
import padcms.dao.factory.DataStorageFactory;
import padcms.dao.issue.bean.Menu;
import padcms.dao.issue.bean.Page;
import padcms.dao.issue.bean.PageHorisontal;
import padcms.dao.issue.bean.PageImposition;
import padcms.kiosk.RevisionStateManager;
import padcms.magazine.PhotoGalleryActivity;
import padcms.magazine.VideoActivity;
import padcms.magazine.controls.HorisontalPageSmoothFliperAdapter;
import padcms.magazine.controls.PageSmoothFliperAdapter;
import padcms.magazine.controls.switcher.HorisontalPageSwitcherController;
import padcms.magazine.controls.switcher.VerticalPageSwitcherController;
import padcms.magazine.menu.MenuController;
import padcms.magazine.page.ArticleWithFixedIllustrationPageTemplateView;
import padcms.magazine.page.ArticleWithOverlayPageTemplateView;
import padcms.magazine.page.BasePageView;
import padcms.magazine.page.BasicArticlePageTemplateView;
import padcms.magazine.page.CoverPageTemplateView;
import padcms.magazine.page.DragAndDropPageTemplateView;
import padcms.magazine.page.FixedIllustrationArticleTouchablePageTemplateView;
import padcms.magazine.page.FlashBulletInteractivePageTemplateView;
import padcms.magazine.page.GalleryFlashBulletInteractivePageTemplateView;
import padcms.magazine.page.HTMLPageTemplateView;
import padcms.magazine.page.HorisontalPageView;
import padcms.magazine.page.Html5PageTemplateView;
import padcms.magazine.page.InteractiveBulletsPageTemplateView;
import padcms.magazine.page.ScrollingPageTemplateView;
import padcms.magazine.page.SimplePageTemplateView;
import padcms.magazine.page.SlidersBasedMiniArticlesHorizontalPageTemplateView;
import padcms.magazine.page.SlidersBasedMiniArticlesTopPageTemplateView;
import padcms.magazine.page.SlidersBasedMiniArticlesVerticalPageTemplateView;
import padcms.magazine.page.SlideshowLandscapePageTemplateView;
import padcms.magazine.page.SlideshowPageTemplateView;
import padcms.magazine.page.State;
import padcms.magazine.page.elementview.VideoElementView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;

public class IssueViewFactory {

	public static String issueFolderName = "";
	private static IssueViewFactory inscance;

	private Context mContext;
	private Revision revisionBin;
	private ArrayList<Page> pageBinCollection;

	private ArrayList<Menu> menuBinCollection;
	private ArrayList<BasePageView> pageViewCollection;
	private ArrayList<HorisontalPageView> horisontalPageViewCollection;
	// private PageSmoothFliperAdapter pageAdapter;

	// private HorisontalPageSmoothFliperAdapter horisontalPageAdapter;
	private MenuController menuController;
	private boolean isLandscapeAllow;
	private int issueColor;
	private Application applicationBin;

	private VerticalPageSwitcherController verticalPageSwitcherController;
	private HorisontalPageSwitcherController horisontalPageSwitcherController;

	public static IssueViewFactory getIssueViewFactoryIstance(Context context) {
		if (inscance == null) {
			inscance = new IssueViewFactory(context);
		}
		return inscance;

	}

	public void setSensorConfiguration() {
		if (!isLandscapeAllow()) {
			((Activity) mContext).setRequestedOrientation(-1);
		}
	}

	public enum PageTemplateEnum {
		BasicArticlePageTemplate(1), ArticleWithFixedIllustrationPageTemplate(2), SimplePageTemplate(
				3), ScrollingPageTemplate(4), SlidersBasedMiniArticlesHorizontalPageTemplate(
				5), SlideshowPageTemplate(6), CoverPageTemplate(7), SlidersBasedMiniArticlesVerticalPageTemplate(
				8), ArticleWithOverlayPageTemplate(9), FixedIllustrationArticleTouchablePageTemplate(
				10), InteractiveBulletsPageTemplate(11), SlideshowLandscapePageTemplate(
				12), SlidersBasedMiniArticlesTopPageTemplate(13), HTMLPageTemplate(
				14), DragAndDropPageTemplate(15), FlashBulletInteractivePageTemplate(
				16), GalleryFlashBulletInteractivePageTemplate(17), Html5PageTemplate(
				18), UnnownPageTemplate(-1);

		PageTemplateEnum(int templateID) {
			this.templateID = templateID;
		}

		int templateID;
	};

	public IssueViewFactory(Context mContext) {
		this.mContext = mContext;

		revisionBin = DataStorageFactory.getInstance(mContext)
				.getCurrentRevisionIssue();
		applicationBin = DataStorageFactory.getInstance(mContext)
				.getApplication();
		pageBinCollection = DataStorageFactory.getInstance(mContext)
				.getIssuePageCollection();
		menuBinCollection = DataStorageFactory.getInstance(mContext)
				.getIssueMenuCollection();
		RevisionStateManager.getInstance(mContext);
		// issueFolderName =
		// DataStorageFactory.getInstance(mContext).nameCurrentIssueFolder;

		if (revisionBin.getHorizontal_mode().toLowerCase().equals("none"))
			isLandscapeAllow = false;
		else
			isLandscapeAllow = true;

		issueColor = convertColor(revisionBin.getColor());
	}

	public void initData() {

		// initPageViewCollection();

		setPageViewRelations();
		verticalPageSwitcherController = new VerticalPageSwitcherController(
				mContext, getPageViewCollection().get(0));
		if (horisontalPageViewCollection.size() > 0)
			horisontalPageSwitcherController = new HorisontalPageSwitcherController(
					mContext, horisontalPageViewCollection);
	}

	public VerticalPageSwitcherController getVerticalPageSwitcherController() {
		return verticalPageSwitcherController;
	}

	public HorisontalPageSwitcherController getHorisontalPageSwitcherController() {
		return horisontalPageSwitcherController;
	}

	public File getPathToResourceFolder() {
		return ApplicationFileHelper
				.getFileRevisionResourcesFolder(getRevisionBin().getId()
						.intValue());
	}

	private int convertColor(String colorString) {
		int colorInt = Color.WHITE;
		if (colorString != null && colorString.length() > 0) {
			colorString = colorString.replace("#", "");
			if (colorString.length() < 6) {
				String start = "";// colorString.length();
				for (int i = 0; i < 6 - colorString.length(); i++) {
					start += "F";
				}
				colorString = "#FF" + start + colorString;
			} else if (colorString.length() == 6) {
				colorString = "#FF" + colorString;
			} else if (colorString.length() > 6) {
				colorString = "#FF"
						+ colorString.substring(colorString.length() - 6);
			}

			colorInt = Color.parseColor(colorString);
		}
		return colorInt;
	}

	public void initPageViewCollection(Context mContext) {
		if (pageViewCollection == null) {
			pageViewCollection = new ArrayList<BasePageView>();
			horisontalPageViewCollection = new ArrayList<HorisontalPageView>();

			HorisontalPageView horisontalPageView = null;
			// int i = 2;
			for (Page page : pageBinCollection) {

				BasePageView pageView = createPageViewBaseOnTemplate(mContext,
						page);
				pageViewCollection.add(pageView);

				PageHorisontal horisontalpageBin = page.getPageHorisontal();
				if (horisontalpageBin != null
						&& !isHorisontalPageExistInList(horisontalpageBin)) {

					horisontalPageView = new HorisontalPageView(mContext,
							horisontalpageBin);
					horisontalPageViewCollection.add(horisontalPageView);
					pageView.setHorisontalPageView(horisontalPageView);

					horisontalPageView.setRootPageView(pageView);

					horisontalPageView.setState(State.DOWNLOAD);

				} else if (horisontalPageView != null) {

					pageView.setHorisontalPageView(horisontalPageView);

				}
				pageView.setState(State.DOWNLOAD);
			}
			// i++;
		}
	}

	// public void setPageDownloadState() {
	//
	// for (Page page : pageViewCollection) {
	//
	// BasePageView pageView = createPageViewBaseOnTemplate(mContext, page);
	//
	// pageView.setState(State.DOWNLOAD);
	//
	// }
	// }

	private void setPageViewRelations() {
		for (BasePageView page : pageViewCollection) {
			page.setColor(issueColor);
			for (PageImposition pageImposition : page.getPageBin()
					.getPageImpositions()) {

				if (pageImposition.getPosition_type().equals("right")) {
					page.setRightPage(findPageViewByPageID(pageViewCollection,
							pageImposition.getIs_linked_to().intValue()));
				} else if (pageImposition.getPosition_type().equals("bottom")) {
					page.setBottompage(findPageViewByPageID(pageViewCollection,
							pageImposition.getIs_linked_to().intValue()));
				}

			}
		}
		BasePageView page = pageViewCollection.get(0);
		page.setActiveState();
		while (page != null) {
			BasePageView pageTop = page;
			int color = pageTop.getColor();
			Menu menu = findMenuBinByPageID(pageTop.getPageID());
			if (menu != null) {
				color = convertColor(menu.getColor());
				pageTop.setColor(color);
			}

			while (pageTop.getBottomPage() != null) {
				pageTop.getBottomPage().setColor(pageTop.getColor());
				pageTop = pageTop.getBottomPage();
			}
			page = page.getRightPage();
		}

		page = pageViewCollection.get(0);
		HorisontalPageView currentHorisontal = page.getHorisontalPageView();
		// if (currentHorisontal != null)
		// currentHorisontal.setState(State.DOWNLOAD);

		while (page.getRightPage() != null) {
			if (currentHorisontal == null)
				currentHorisontal = page.getHorisontalPageView();
			page = page.getRightPage();
			if (currentHorisontal != null) {
				HorisontalPageView nextRightHorisontal = page
						.getHorisontalPageView();
				if (nextRightHorisontal != null
						&& nextRightHorisontal != currentHorisontal) {
					// currentHorisontal.setState(State.DOWNLOAD);
					currentHorisontal.setRightPage(nextRightHorisontal);
					currentHorisontal = nextRightHorisontal;

				}
			}

		}

	}

	private boolean isHorisontalPageExistInList(PageHorisontal horisontalpageBin) {
		for (HorisontalPageView currentHorisontalPageView : horisontalPageViewCollection) {
			if (currentHorisontalPageView.getHorisontalPageBin().getId()
					.equals(horisontalpageBin.getId())) {
				return true;
			}
		}
		return false;
	}

	private BasePageView createPageViewBaseOnTemplate(Context mContext,
			Page page) {
		BasePageView pageView;
		PageTemplateEnum pageTemplate = getPageTemplateName(page.getTemplate()
				.intValue());
		switch (pageTemplate) {
		case BasicArticlePageTemplate:
			pageView = new BasicArticlePageTemplateView(mContext, page);
			break;
		case ArticleWithFixedIllustrationPageTemplate:
			pageView = new ArticleWithFixedIllustrationPageTemplateView(
					mContext, page);
			break;
		case SimplePageTemplate:
			pageView = new SimplePageTemplateView(mContext, page);
			break;
		case ScrollingPageTemplate:
			pageView = new ScrollingPageTemplateView(mContext, page);
			break;
		case SlidersBasedMiniArticlesHorizontalPageTemplate:
			pageView = new SlidersBasedMiniArticlesHorizontalPageTemplateView(
					mContext, page);
			break;
		case SlideshowPageTemplate:
			pageView = new SlideshowPageTemplateView(mContext, page);
			break;
		case CoverPageTemplate:
			pageView = new CoverPageTemplateView(mContext, page);
			break;
		case SlidersBasedMiniArticlesVerticalPageTemplate:
			pageView = new SlidersBasedMiniArticlesVerticalPageTemplateView(
					mContext, page);
			break;
		case ArticleWithOverlayPageTemplate:
			pageView = new ArticleWithOverlayPageTemplateView(mContext, page);
			break;
		case FixedIllustrationArticleTouchablePageTemplate:
			pageView = new FixedIllustrationArticleTouchablePageTemplateView(
					mContext, page);
			break;
		case InteractiveBulletsPageTemplate:
			pageView = new InteractiveBulletsPageTemplateView(mContext, page);
			break;
		case SlideshowLandscapePageTemplate:
			pageView = new SlideshowLandscapePageTemplateView(mContext, page);
			break;
		case SlidersBasedMiniArticlesTopPageTemplate:
			pageView = new SlidersBasedMiniArticlesTopPageTemplateView(
					mContext, page);
			break;
		case HTMLPageTemplate:
			pageView = new HTMLPageTemplateView(mContext, page);
			break;
		case DragAndDropPageTemplate:
			pageView = new DragAndDropPageTemplateView(mContext, page);
			break;
		case FlashBulletInteractivePageTemplate:
			pageView = new FlashBulletInteractivePageTemplateView(mContext,
					page);
			break;
		case GalleryFlashBulletInteractivePageTemplate:
			pageView = new GalleryFlashBulletInteractivePageTemplateView(
					mContext, page);
			break;
		case Html5PageTemplate:
			pageView = new Html5PageTemplateView(mContext, page);
			break;
		default:
			pageView = new BasePageView(mContext, page);
			break;
		}
		return pageView;
	}

	private PageTemplateEnum getPageTemplateName(int templateID) {
		for (PageTemplateEnum tepalte : PageTemplateEnum.values()) {
			if (tepalte.templateID == templateID)
				return tepalte;
		}
		return PageTemplateEnum.UnnownPageTemplate;
	}

	public BasePageView findPageViewByPageID(
			ArrayList<BasePageView> pageViewCollection, int pageID) {
		BasePageView pageView = null;
		for (BasePageView pageViewCurrent : pageViewCollection) {
			if (pageViewCurrent.getPageID() == pageID) {
				pageView = pageViewCurrent;
				break;
			}
		}
		return pageView;
	}

	public Menu findMenuBinByPageID(int pageID) {
		Menu menuBin = null;
		for (Menu menu : menuBinCollection) {
			if (menu.getFirstpage_id().intValue() == pageID) {
				menuBin = menu;
				break;
			}
		}
		return menuBin;
	}

	public BasePageView findPageViewByPageID(int pageID) {
		BasePageView pageView = null;
		for (BasePageView pageViewCurrent : pageViewCollection) {
			if (pageViewCurrent.getPageID() == pageID) {
				pageView = pageViewCurrent;
				break;
			}
		}
		return pageView;
	}

	public BasePageView findPageViewByPageMachineName(String machineName) {
		BasePageView pageView = null;
		for (BasePageView pageViewCurrent : pageViewCollection) {

			if (pageViewCurrent.getPageMachineName() != null
					&& pageViewCurrent.getPageMachineName().equals(machineName)) {
				pageView = pageViewCurrent;
				break;
			}
		}
		return pageView;
	}

	public boolean isLandscapeAllow() {
		return isLandscapeAllow;
	}

	public void setLandscapeAllow(boolean isLandscapeAllow) {
		this.isLandscapeAllow = isLandscapeAllow;
	}

	public ArrayList<Page> getPageBinCollection() {
		return pageBinCollection;
	}

	public void setPageBinCollection(ArrayList<Page> pageBinCollection) {
		this.pageBinCollection = pageBinCollection;
	}

	public ArrayList<BasePageView> getPageViewCollection() {
		return pageViewCollection;
	}

	public void setPageViewCollection(ArrayList<BasePageView> pageViewCollection) {
		this.pageViewCollection = pageViewCollection;
	}

	public ArrayList<Menu> getMenuBinCollection() {
		return menuBinCollection;
	}

	public void setMenuBinCollection(ArrayList<Menu> menuBinCollection) {
		this.menuBinCollection = menuBinCollection;
	}

	public MenuController getMenuController() {
		return menuController;
	}

	public void setMenuController(MenuController menuController) {
		this.menuController = menuController;
	}

	public ArrayList<HorisontalPageView> getHorisontalPageViewCollection() {
		return horisontalPageViewCollection;
	}

	public void setHorisontalPageViewCollection(
			ArrayList<HorisontalPageView> horisontalPageViewCollection) {
		this.horisontalPageViewCollection = horisontalPageViewCollection;
	}

	public int getIssueColor() {
		return issueColor;
	}

	public void setIssueColor(int issueColor) {
		this.issueColor = issueColor;
	}

	public Application getApplicationBin() {
		return applicationBin;
	}

	public void setApplicationBin(Application applicationBin) {
		this.applicationBin = applicationBin;
	}

	public void flipToPage(BasePageView basePageView) {
		if (basePageView != null) {
			verticalPageSwitcherController.swichToViewByBasePage(basePageView);
			menuController.hideMenu();
		}
	}

	public void flipToHorisontalPage(HorisontalPageView horisontalPageView) {
		if (horisontalPageView != null) {

			horisontalPageSwitcherController
					.swichToViewByBasePage(horisontalPageView);

			menuController.hideMenu();
		}
	}

	public void refuseDownloadBG() {
		ResourceFactory.refuseDownloadBG();
	}

	public void destroy() {

		// inscance = null;
		ResourceFactory.destroy();
		if (getMenuController() != null)
			getMenuController().destroy();
		if (pageViewCollection != null)
			for (BasePageView pageView : pageViewCollection) {
				pageView.setReleaseState();
			}
		pageViewCollection = null;
		if (horisontalPageViewCollection != null)
			for (HorisontalPageView horisontalPageView : horisontalPageViewCollection) {
				horisontalPageView.setReleaseState();
			}

	}

	public void playVideoCurrentPage(boolean isCloseOnFinish) {
		BasePageView pageView = verticalPageSwitcherController.getCurrentPage();

		if (pageView != null) {
			VideoElementView videoElement = pageView.getVideoElementView();
			if (videoElement != null)
				if (videoElement.getResourcePathStr().length() > 0) {
					runVideo(videoElement.getResourcePathStr(),
							isCloseOnFinish, false);
				} else if (videoElement.getPathToStreamStr().length() > 0) {
					runVideo(videoElement.getPathToStreamStr(),
							isCloseOnFinish, true);
				}
		}
	}

	public void runVideo(String videoUrl, boolean isCloseOnFinish,
			boolean isStreamMode) {
		Intent videoIntent = new Intent(mContext, VideoActivity.class);
		videoIntent.putExtra("closeOnFinish", isCloseOnFinish);
		videoIntent.putExtra("videoUrl", videoUrl);
		videoIntent.putExtra("streamMode", isStreamMode);
		videoIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		mContext.startActivity(videoIntent);

	}

	public void runWebView(String url) {
		Intent viewIntent = new Intent("android.intent.action.VIEW",
				Uri.parse(url));
		((Activity) mContext).startActivity(viewIntent);
	}

	public void showGallary(int pageID, int gallaryID, int position) {
		Intent intentGalary = new Intent(mContext, PhotoGalleryActivity.class);
		intentGalary.putExtra("pageID", pageID);
		intentGalary.putExtra("gallaryID", gallaryID);
		intentGalary.putExtra("currentPosition", position);

		mContext.startActivity(intentGalary);
		((Activity) mContext).overridePendingTransition(R.anim.bottom_to_top,
				R.anim.hold_slide);
	}

	public void clickedOnScreen() {
		if (menuController != null)
			menuController.showHideMenu();
	}

	public Revision getRevisionBin() {
		return revisionBin;
	}

	public void setRevisionBin(Revision revisionBin) {
		this.revisionBin = revisionBin;
	}

}
