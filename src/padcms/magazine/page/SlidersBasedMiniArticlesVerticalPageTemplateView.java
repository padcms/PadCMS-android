package padcms.magazine.page;

import java.util.List;

import padcms.dao.issue.bean.Page;
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
import android.widget.ScrollView;

public class SlidersBasedMiniArticlesVerticalPageTemplateView extends
		BasePageView {

	private BackgroundElementView backgroundElementView;
	// private List<BackgroundElementView>
	// backgroundMinArticleElementViewCollection;
	private ViewGroup viewGroupMinArticleBG;
	private List<MiniArticleElementView> miniArticleElementViewCollection;

	public SlidersBasedMiniArticlesVerticalPageTemplateView(Context context,
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
			}
			if (miniArticleElementViewCollection != null
					&& miniArticleElementViewCollection.size() > 0) {

				ActiveZoneElementDataView activeZoneScroller = getPageActiveZoneViewScrolling();

				ScrollView verticalScroller = new ScrollView(mContext);
				RelativeLayout.LayoutParams layoutParamsVerticalView = new RelativeLayout.LayoutParams(
						-2, -2);
				layoutParamsVerticalView
						.addRule(RelativeLayout.CENTER_VERTICAL);
				RelativeLayout.LayoutParams layoutParamsbackgroundView = new RelativeLayout.LayoutParams(
						-2, -2);
				layoutParamsbackgroundView
						.addRule(RelativeLayout.CENTER_VERTICAL);

				if (activeZoneScroller != null) {

					if (activeZoneScroller.getLeftActiveZone() > 0) {
						layoutParamsVerticalView
								.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					} else
						layoutParamsVerticalView
								.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					layoutParamsbackgroundView.topMargin = activeZoneScroller
							.getTopActiveZone();
					layoutParamsVerticalView.topMargin = activeZoneScroller
							.getTopActiveZone();
					layoutParamsVerticalView.height = activeZoneScroller
							.getHeightActiveZone();
				}

				viewGroupMinArticleBG = new RelativeLayout(mContext);
				int count = 0;
				LinearLayout layoutMinArticle = new LinearLayout(mContext);
				layoutMinArticle.setOrientation(LinearLayout.VERTICAL);

				for (MiniArticleElementView miniArticleElement : miniArticleElementViewCollection) {

					layoutMinArticle.addView(miniArticleElement
							.getView(mContext));
					View bgView = miniArticleElement
							.getBackgroundElementView().getView(
									getContext());
					if (count == 0) {
						miniArticleElement.setActive(true);
						
						if (bgView.getParent() == null)
							viewGroupMinArticleBG.addView(bgView);
					} else
						miniArticleElement.setActive(false);

					if (backgroundElementView == null) {
						miniArticleElement.getBackgroundElementView()
								.setElementBackgroundColor(Color.WHITE);
					}

					count++;
				}
				verticalScroller.addView(layoutMinArticle,
						new LinearLayout.LayoutParams(-2, -2));

				pageLayer.addView(viewGroupMinArticleBG,
						layoutParamsbackgroundView);
				pageLayer.addView(verticalScroller, layoutParamsVerticalView);

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

			View bgView = miniArticleElement.getBackgroundElementView()
					.getView(getContext());

			if (miniArticleElement.isActive()) {
				if (bgView.getParent() == null)
					viewGroupMinArticleBG.addView(bgView);
				viewGroupMinArticleBG.requestLayout();
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
