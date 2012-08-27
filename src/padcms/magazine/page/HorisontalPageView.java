package padcms.magazine.page;

import padcms.dao.issue.bean.PageHorisontal;
import padcms.magazine.page.elementview.HorisontalElementView;
import padcms.magazine.page.elementview.ProgressDownloadElementView;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class HorisontalPageView extends BasePageView {

	// private Context context;
	private PageHorisontal horisontalpageBin;
	private int pageWidth;
	private int pageHeight;

	// protected State state = null;

	private HorisontalPageView rightPage;
	private HorisontalPageView leftPage;
	private BasePageView rootPageView;

	protected HorisontalElementView horisontalElementView;

	// protected ProgressDownloadElementView progressElementView;

	// protected RelativeLayout pageViewLayer;

	public HorisontalPageView(Context context,PageHorisontal horisontalpageBin) {
		super(context,null);
		// this.context = context;
		this.horisontalpageBin = horisontalpageBin;

		this.state = State.DISACTIVE;
		horisontalElementView = new HorisontalElementView(this,
				horisontalpageBin.getResource());
		// getView();
	}

	public BasePageView getRootPageView() {
		return rootPageView;
	}

	public void setRootPageView(BasePageView rootPageView) {
		this.rootPageView = rootPageView;
	}

	public PageHorisontal getHorisontalPageBin() {

		return horisontalpageBin;

	}

	public void setState(State state) {

		this.state = state;
		if (horisontalElementView != null)
			horisontalElementView.setState(state);

	}

	public HorisontalPageView getRightPage() {

		return rightPage;

	}

	public HorisontalPageView getLeftPage() {

		return leftPage;

	}

	public void setRightPage(HorisontalPageView rightPage) {

		rightPage.setLeftPage(this);
		this.rightPage = rightPage;

	}

	public void setLeftPage(HorisontalPageView leftPage) {

		this.leftPage = leftPage;

	}

	public int getPageWidth() {

		return pageWidth;

	}

	public void setPageWidth(int pageWidth) {

		pageViewLayer.getLayoutParams().width = pageWidth;
		this.pageWidth = pageWidth;

	}

	public PageHorisontal getHorisontalpageBin() {
		return horisontalpageBin;
	}

	public void setHorisontalpageBin(PageHorisontal horisontalpageBin) {
		this.horisontalpageBin = horisontalpageBin;
	}

	public int getPageHeight() {
		return pageHeight;
	}

	public void setPageHeight(int pageHeight) {

		pageViewLayer.getLayoutParams().height = pageHeight;
		this.pageHeight = pageHeight;

	}

	public View getView(Context mContext) {
		setContext(mContext);
		if (pageViewLayer == null) {

			progressElementView = new ProgressDownloadElementView(getContext());
			progressElementView.setTitle(horisontalpageBin.getName() + " id: "
					+ horisontalpageBin.getId());

			progressElementView.hideFastView();

			horisontalElementView
					.setProgressDownloadElementView(progressElementView);

			pageViewLayer = new RelativeLayout(getContext());

			pageViewLayer.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
			RelativeLayout.LayoutParams layoutParamsProgress = new RelativeLayout.LayoutParams(
					-2, -2);

			layoutParamsProgress.addRule(RelativeLayout.CENTER_IN_PARENT);
			pageViewLayer.addView(progressElementView, layoutParamsProgress);

			pageViewLayer.addView(horisontalElementView.getView(getContext()),
					0);
			pageViewLayer.setTag(this);
			// setPageWidth((int) horisontalElementView.getWidth());
			// setPageHeight((int) horisontalElementView.getHeight());
		}

		return pageViewLayer;
	}

	public void setActiveState() {

		state = State.ACTIVE;
		if (horisontalElementView != null)
			horisontalElementView.setState(state);

	}

	public void setDisactiveState() {

		state = State.DISACTIVE;
		if (horisontalElementView != null)
			horisontalElementView.setState(state);

	}

	public void setReleaseState() {

		state = State.RELEASE;
		if (horisontalElementView != null)
			horisontalElementView.setState(state);

	}

}
