package padcms.magazine.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import padcms.bll.ApplicationSkinFactory;
import padcms.dao.issue.bean.Page;
import padcms.magazine.factory.ElementViewFactory;
import padcms.magazine.factory.IssueViewFactory;
import padcms.magazine.page.elementview.ActiveZoneElementDataView;
import padcms.magazine.page.elementview.BaseElementView;
import padcms.magazine.page.elementview.GalaryElementView;
import padcms.magazine.page.elementview.ProgressDownloadElementView;
import padcms.magazine.page.elementview.VideoElementView;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class BasePageView {
	private int pageID;
	private String pageMachineName;
	private Context context;
	protected Page pageBin;
	private int pageWidth;
	private int pageHeight;
	private int color;

	private boolean allowClick = true;
	protected State state = null;

	private BasePageView rightPage;
	private BasePageView leftPage;
	private BasePageView topPage;
	private BasePageView bottompage;

	private HorisontalPageView horisontalPageView;

	protected List<BaseElementView> elementViewCollection;
	private ArrayList<GalaryElementView> gallaryElementViewCollection;
	private VideoElementView videoElementView;

	protected ProgressDownloadElementView progressElementView;

	protected RelativeLayout pageViewLayer;
	protected RelativeLayout pageLayer;
	protected RelativeLayout activeZoneViewLayer;

	public BasePageView(Context context, Page pageBin) {

		this.context = context;
		if (pageBin != null) {
			this.pageBin = pageBin;
			this.pageID = pageBin.getId().intValue();
			this.pageMachineName = pageBin.getMachine_name();
			this.state = State.DISACTIVE;
			this.elementViewCollection = ElementViewFactory
					.getElementViewCollectionFromListElementBin(this,
							pageBin.getListElemnts());
		}

	}

	protected void initElementData(Context mContext) {
		this.context = mContext;

		for (BaseElementView elementView : elementViewCollection) {
			elementView.initElementViewData();
		}

		Collections.sort(elementViewCollection);

		gallaryElementViewCollection = (ArrayList<GalaryElementView>) ElementViewFactory
				.getGalaryElementViewCollection(elementViewCollection);
		videoElementView = ElementViewFactory
				.getVideoElementView(elementViewCollection);

		progressElementView = new ProgressDownloadElementView(context);
		progressElementView.setTitle(pageBin.getTitle() + " id: "
				+ pageBin.getId());
		// progressElementView.hideFastView();

	}

	public View getView(Context mContext) {

		if (pageViewLayer == null) {
			pageViewLayer = new RelativeLayout(context);
			pageLayer = new RelativeLayout(context);

			activeZoneViewLayer = new RelativeLayout(context);
			// activeZoneViewLayer.setBackgroundColor(Color.BLUE);
			// activeZoneViewLayer.getBackground().setAlpha(100);
			pageViewLayer.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));

			RelativeLayout.LayoutParams layoutParamsProgress = new RelativeLayout.LayoutParams(
					-2, -2);
			layoutParamsProgress.addRule(RelativeLayout.CENTER_IN_PARENT);
			RelativeLayout.LayoutParams layoutParamsActiveZone = new RelativeLayout.LayoutParams(
					-1, -2);
			layoutParamsActiveZone.addRule(RelativeLayout.CENTER_IN_PARENT);
			if (progressElementView != null) {
				pageViewLayer
						.addView(progressElementView, layoutParamsProgress);
			}
			pageViewLayer.addView(pageLayer, new RelativeLayout.LayoutParams(
					-1, -1));
			pageViewLayer.addView(activeZoneViewLayer, layoutParamsActiveZone);

			pageViewLayer.setTag(this);
		}

		return pageViewLayer;
	}

	public void onConfiguretionChenged(Context mContext) {
		if (gallaryElementViewCollection != null
				&& gallaryElementViewCollection.size() > 0) {
			Toast.makeText(context, "onConfiguretionChenged", 1000).show();
		} else
			((Activity) mContext)
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	}

	public Page getPageBin() {
		return pageBin;
	}

	public List<BaseElementView> getElementsList() {
		return elementViewCollection;
	}

	public void setElementsList(List<BaseElementView> elementsList) {
		this.elementViewCollection = elementsList;
	}

	public void setState(State state) {
		this.state = state;
		for (BaseElementView elementView : elementViewCollection) {
			elementView.setState(state);
		}
	}

	public Context getContext() {
		return context;
	}

	public int getPageID() {
		return pageID;
	}

	public void setPageID(int pageID) {
		this.pageID = pageID;
	}

	public String getPageMachineName() {
		return pageMachineName;
	}

	public void setPageMachineName(String pageMachineName) {
		this.pageMachineName = pageMachineName;
	}

	public void setContext(Context mContext) {
		this.context = mContext;
	}

	public BasePageView getRightPage() {
		return rightPage;
	}

	public BasePageView getLeftPage() {
		return leftPage;
	}

	public BasePageView getTopPage() {
		return topPage;
	}

	public BasePageView getBottomPage() {
		return bottompage;
	}

	public void setRightPage(BasePageView rightPage) {
		rightPage.setLeftPage(this);
		this.rightPage = rightPage;
	}

	public void setLeftPage(BasePageView leftPage) {
		this.leftPage = leftPage;
	}

	public void setTopPage(BasePageView topPage) {
		this.topPage = topPage;
	}

	public void setBottompage(BasePageView bottompage) {
		bottompage.setTopPage(this);
		this.bottompage = bottompage;
	}

	public int getPageWidth() {
		return pageWidth;
	}

	public void setPageWidth(int pageWidth) {
		pageViewLayer.getLayoutParams().width = pageWidth;
		this.pageWidth = pageWidth;
	}

	public int getPageHeight() {
		return pageHeight;
	}

	public void setPageHeight(int pageHeight) {
		pageViewLayer.getLayoutParams().height = pageHeight;
		activeZoneViewLayer.getLayoutParams().height = pageHeight;
		this.pageHeight = pageHeight;

	}

	public ArrayList<GalaryElementView> getGalaryElementViewCollection() {
		return gallaryElementViewCollection;
	}

	public void setGalaryElementViewCollection(
			ArrayList<GalaryElementView> galaryElementViewCollection) {
		this.gallaryElementViewCollection = galaryElementViewCollection;
	}

	public VideoElementView getVideoElementView() {
		return videoElementView;
	}

	public void setVideoElementView(VideoElementView videoElementView) {
		this.videoElementView = videoElementView;
	}

	public HorisontalPageView getHorisontalPageView() {
		return horisontalPageView;
	}

	public void setHorisontalPageView(HorisontalPageView horisontalPageView) {
		this.horisontalPageView = horisontalPageView;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void cleanPageView() {
		setReleaseState();

	}

	public void setActiveState() {
		state = State.ACTIVE;
		for (BaseElementView elementView : elementViewCollection) {
			elementView.setState(state);
		}
	}

	public void setDisactiveState() {

		state = State.DISACTIVE;
		for (BaseElementView elementView : elementViewCollection) {
			elementView.setState(state);
		}
	}

	public void setReleaseState() {
		state = State.RELEASE;

		for (BaseElementView elementView : elementViewCollection) {
			elementView.setState(state);
		}

	}

	public void activateElementView(BaseElementView elementView) {

		elementView.activateElement();
	}

	public void activateElementView(int numberOfComponent) {

	}

	public boolean isAllowClick() {
		return allowClick;
	}

	public void setAllowClick(boolean allowClick) {
		this.allowClick = allowClick;
	}

	public void pageViewClicked() {
		if (allowClick)
			IssueViewFactory.getIssueViewFactoryIstance(context)
					.clickedOnScreen();
		allowClick = true;
	}

	public ActiveZoneElementDataView getPageActiveZoneViewScrolling() {
		for (BaseElementView elementView : elementViewCollection) {
			if (elementView.getActiveZonewViewCollection() != null) {
				for (ActiveZoneElementDataView activeZoneView : elementView
						.getActiveZonewViewCollection()) {
					if (activeZoneView.getActiveZoneKey().equals("scroller")) {
						return activeZoneView;
					}
				}
			}
		}
		return null;
	}

	public ActiveZoneElementDataView getPageActiveZoneThumbs() {
		for (BaseElementView elementView : elementViewCollection) {
			if (elementView.getActiveZonewViewCollection() != null) {
				for (ActiveZoneElementDataView activeZoneView : elementView
						.getActiveZonewViewCollection()) {
					if (activeZoneView.getActiveZoneKey().equals("thumbs")) {
						return activeZoneView;
					}
				}
			}
		}
		return null;
	}

	public ActiveZoneElementDataView getPageActiveZonePhoto() {
		for (BaseElementView elementView : elementViewCollection) {
			if (elementView.getActiveZonewViewCollection() != null) {
				for (ActiveZoneElementDataView activeZoneView : elementView
						.getActiveZonewViewCollection()) {
					if (activeZoneView.getActiveZoneKey().equals("action")
							&& activeZoneView.getActiveZoneValue().startsWith(
									"photos")) {
						return activeZoneView;
					}
				}
			}
		}
		return null;
	}

	protected void addPhotoGallaryButton(Context context) {
		if (getGalaryElementViewCollection() != null
				&& getGalaryElementViewCollection().size() > 0) {

			if (getPageActiveZonePhoto() == null) {
				pageViewLayer.addView(ApplicationSkinFactory
						.getPhotoGallaryButton(this));
			}
			// pageViewLayer.addView(ApplicationSkinFactory
			// .getPhotoGallaryButton_Air_le_mag(this));
		}
	}

	// {
	// {
	// RelativeLayout view = new RelativeLayout(context);
	// view.setBackgroundColor(Color.argb(255, new Random().nextInt(255),
	// new Random().nextInt(255), new Random().nextInt(255)));
	// TextView viewText = new TextView(context);
	// viewText.setText("ID: " + pageBin.getId() + "\nTitle:"
	// + pageBin.getTitle());
	// // view.setGravity(Gravity.CENTER);
	// view.addView(viewText, new RelativeLayout.LayoutParams(-2, -2));
	//
	// view.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
	// return view;
	// }
}
