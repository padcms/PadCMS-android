package padcms.magazine.page;

import java.util.List;

import padcms.dao.issue.bean.Page;
import padcms.magazine.controls.HorizontalScrollViewController;
import padcms.magazine.factory.ElementViewFactory;
import padcms.magazine.page.elementview.ActiveZoneElementDataView;
import padcms.magazine.page.elementview.BackgroundElementView;
import padcms.magazine.page.elementview.BaseElementView;
import padcms.magazine.page.elementview.MiniArticleElementView;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SlidersBasedMiniArticlesHorizontalPageTemplateView extends
		BasePageView {

	private ViewGroup viewGroupMinArticleBG;
	private BackgroundElementView backgroundElementView;
	// private BackgroundElementView backgroundMinArticleElementView;
	private List<MiniArticleElementView> miniArticleElementViewCollection;

	public SlidersBasedMiniArticlesHorizontalPageTemplateView(Context context,
			Page pageBin) {
		super(context, pageBin);
		for (BaseElementView baseElementView : elementViewCollection) {
			if (baseElementView instanceof MiniArticleElementView) {
				BackgroundElementView backgroundMinArticleElementView = new BackgroundElementView(
						this, baseElementView.getResourcePathStr());
				backgroundMinArticleElementView
						.setElementBackgroundColor(Color.TRANSPARENT);

				((MiniArticleElementView) baseElementView)
						.setBackgroundElementView(backgroundMinArticleElementView);

			}
		}

	}

	@Override
	protected void initElementData(Context mContext) {
		super.initElementData(mContext);
		backgroundElementView = ElementViewFactory
				.getBackgroundElementView(elementViewCollection);
		if (backgroundElementView != null)
			backgroundElementView
					.setProgressDownloadElementView(progressElementView);

		miniArticleElementViewCollection = ElementViewFactory
				.getMiniArticleElementView(elementViewCollection);

		for (MiniArticleElementView miniArticleElement : miniArticleElementViewCollection) {
			miniArticleElement.setParentPageView(this);
			miniArticleElement
					.setProgressDownloadElementView(progressElementView);
			miniArticleElement.getBackgroundElementView()
					.setProgressDownloadElementView(progressElementView);

		}
	}

	@Override
	public View getView(Context mContext) {
		if (pageViewLayer == null) {
			initElementData(mContext);
			super.getView(mContext);
			if (backgroundElementView != null) {
				pageLayer.addView(backgroundElementView.getView(getContext()));
				setPageHeight(backgroundElementView.getHeight());
				setPageWidth(backgroundElementView.getWidth());
				backgroundElementView.setElementBackgroundColor(Color.WHITE);
				backgroundElementView
						.initViewWithActiveZone(activeZoneViewLayer);
			}
			if (miniArticleElementViewCollection != null
					&& miniArticleElementViewCollection.size() > 0) {

				RelativeLayout.LayoutParams layoutParamsHorisontalView = new RelativeLayout.LayoutParams(
						-1, -2);

				RelativeLayout relativeLayoutHorisontalScrollingContainer = new RelativeLayout(
						mContext);

				if (backgroundElementView != null) {
					relativeLayoutHorisontalScrollingContainer
							.setLayoutParams(new RelativeLayout.LayoutParams(
									backgroundElementView.getWidth(),
									backgroundElementView.getHeight()));

				} else {
					relativeLayoutHorisontalScrollingContainer
							.setLayoutParams(new RelativeLayout.LayoutParams(
									-1, -1));
				}

				((RelativeLayout.LayoutParams) relativeLayoutHorisontalScrollingContainer
						.getLayoutParams())
						.addRule(RelativeLayout.CENTER_IN_PARENT);

				ActiveZoneElementDataView thambsActiveZone = getPageActiveZoneThumbs();

				if (thambsActiveZone != null) {
					layoutParamsHorisontalView.topMargin = thambsActiveZone
							.getTopActiveZone();
					layoutParamsHorisontalView.height = thambsActiveZone
							.getHeightActiveZone();
				} else {
					layoutParamsHorisontalView
							.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				}

				HorizontalScrollViewController horisontallScroller = new HorizontalScrollViewController(
						mContext);

				LinearLayout layoutMinArticle = new LinearLayout(mContext);
				layoutMinArticle.setBackgroundColor(Color.BLACK);
				int count = 0;
				viewGroupMinArticleBG = new RelativeLayout(mContext);

				for (MiniArticleElementView miniArticleElement : miniArticleElementViewCollection) {

					layoutMinArticle.addView(miniArticleElement
							.getView(mContext));
					View bgView = miniArticleElement.getBackgroundElementView()
							.getView(getContext());
					if (backgroundElementView == null
							|| backgroundElementView.getResourcePathStr() == null
							|| backgroundElementView.getResourcePathStr()
									.length() == 0) {
						miniArticleElement.getBackgroundElementView()
								.setElementBackgroundColor(Color.WHITE);
					}
					if (count == 0) {
						miniArticleElement.setActive(true);

						if (bgView.getParent() == null)
							viewGroupMinArticleBG.addView(bgView,
									new RelativeLayout.LayoutParams(-1, -1));
					} else
						miniArticleElement.setActive(false);

					count++;
				}

				horisontallScroller.setHorizontalFadingEdgeEnabled(false);
				horisontallScroller.addView(layoutMinArticle,
						new LinearLayout.LayoutParams(-1, -2));

				relativeLayoutHorisontalScrollingContainer.addView(
						horisontallScroller, layoutParamsHorisontalView);

				pageLayer.addView(viewGroupMinArticleBG,
						new RelativeLayout.LayoutParams(-1, -2));
				pageLayer.addView(relativeLayoutHorisontalScrollingContainer);
			}
		}
		return pageViewLayer;
	}

	@Override
	public void setActiveState() {
		state = State.ACTIVE;

		if (backgroundElementView != null)
			backgroundElementView.setState(state);

		for (MiniArticleElementView miniArticleElement : miniArticleElementViewCollection) {

			if (miniArticleElement.isActive()) {
				View bgView = miniArticleElement.getBackgroundElementView()
						.getView(getContext());
				if (bgView.getParent() == null)
					viewGroupMinArticleBG.addView(bgView,
							new RelativeLayout.LayoutParams(-1, -1));
				bgView.invalidate();
			}
			miniArticleElement.setState(state);

		}
	}

	@Override
	public void setDisactiveState() {
		state = State.DISACTIVE;

		if (backgroundElementView != null) {
			backgroundElementView.setState(state);
		}

		if (miniArticleElementViewCollection != null) {
			viewGroupMinArticleBG.removeAllViews();
			for (MiniArticleElementView miniArticleElement : miniArticleElementViewCollection) {
				
				if (miniArticleElement.isActive()) {
					View bgView = miniArticleElement.getBackgroundElementView()
							.getView(getContext());
					if (bgView.getParent() == null)
						viewGroupMinArticleBG.addView(bgView,
								new RelativeLayout.LayoutParams(-1, -1));
					bgView.invalidate();
				}
				miniArticleElement.setState(State.ACTIVE);

			}
		}
	}

	@Override
	public void setReleaseState() {
		state = State.RELEASE;
		if (backgroundElementView != null)
			backgroundElementView.setState(state);

		if (miniArticleElementViewCollection != null) {
			viewGroupMinArticleBG.removeAllViews();
			for (MiniArticleElementView miniArticleElement : miniArticleElementViewCollection) {
				miniArticleElement.setState(state);

			}
		}

	}

	@Override
	public void activateElementView(BaseElementView elementView) {

		if (miniArticleElementViewCollection != null)
			for (MiniArticleElementView miniArticleElement : miniArticleElementViewCollection) {

				View bgView = miniArticleElement.getBackgroundElementView()
						.getView(getContext());

				if (miniArticleElement.equals(elementView)) {
					if (bgView.getParent() == null) {
						viewGroupMinArticleBG.addView(bgView);
						bgView.bringToFront();
					}

					miniArticleElement.activateElement();

				} else {
					if (bgView.getParent() != null) {
						viewGroupMinArticleBG.removeView(bgView);
					}
					miniArticleElement.disactivateElement();

				}
			}

	}

	public void setState(State state) {
		super.setState(state);
		if (state == State.DOWNLOAD) {
			if (miniArticleElementViewCollection != null)
				for (MiniArticleElementView miniArticleElement : miniArticleElementViewCollection) {
					miniArticleElement.getBackgroundElementView().setState(
							state);
				}
		}

	}

	@Override
	public void cleanPageView() {
		super.cleanPageView();
	}
}
