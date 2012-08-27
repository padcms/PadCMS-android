package padcms.magazine.page;

import java.util.List;

import padcms.dao.issue.bean.Page;
import padcms.magazine.factory.ElementViewFactory;
import padcms.magazine.page.elementview.BackgroundElementView;
import padcms.magazine.page.elementview.BaseElementView;
import padcms.magazine.page.elementview.MiniArticleElementView;
import padcms.magazine.resource.ImageViewController;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class InteractiveBulletsPageTemplateView extends BasePageView {

	private BackgroundElementView backgroundElementView;
	private List<MiniArticleElementView> miniArticleElementViewCollection;

	private ViewGroup viewGroupMinArticleBG;

	// private BackgroundElementView backgroundMinArticleElementView;

	public InteractiveBulletsPageTemplateView(Context context,
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
				backgroundElementView.setElementBackgroundColor(Color.WHITE);
				setPageWidth((int) backgroundElementView.getWidth());
				setPageHeight((int) backgroundElementView.getHeight());
				 backgroundElementView
				 .initViewWithActiveZone(activeZoneViewLayer);
			}
			if (miniArticleElementViewCollection != null
					&& miniArticleElementViewCollection.size() > 0) {
				// pageLayer.addView(backgroundMinArticleElementView
				// .getView(getContext()));

				// HorizontalScrollViewController horisontallScroller = new
				// HorizontalScrollViewController(
				// context);

				// LinearLayout layoutMinArticle = new LinearLayout(context);
				// layoutMinArticle.setBackgroundColor(Color.BLACK);
				int count = 0;
				viewGroupMinArticleBG = new RelativeLayout(mContext);
				for (MiniArticleElementView miniArticleElement : miniArticleElementViewCollection) {
					// layoutMinArticle.addView(miniArticleElement.getView(context));
					View bgView = miniArticleElement.getBackgroundElementView()
							.getView(getContext());

					if (backgroundElementView == null) {
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

					// if (backgroundElementView == null) {
					// miniArticleElement.getBackgroundElementView()
					// .setElementBackgroundColor(Color.WHITE);
					// }
					//
					// miniArticleElement.getBackgroundElementView()
					// .getView(mContext)
					// .setLayoutParams(layoutParamsbackgroundView);

					count++;
				}
				pageLayer.addView(viewGroupMinArticleBG,
						new RelativeLayout.LayoutParams(-1, -2));
				// horisontallScroller.setHorizontalFadingEdgeEnabled(false);
				// horisontallScroller.addView(layoutMinArticle,
				// new LinearLayout.LayoutParams(-1, -2));

				// RelativeLayout.LayoutParams layoutParamsHorisontalView = new
				// RelativeLayout.LayoutParams(
				// -1, -2);
				// layoutParamsHorisontalView
				// .addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				//
				// pageViewLayer.addView(horisontallScroller,
				// layoutParamsHorisontalView);

			}
		}
		return pageViewLayer;
	}

	// @Override
	// public void setActiveState() {
	// state = State.ACTIVE;
	//
	// if (backgroundElementView != null)
	// backgroundElementView.setState(state);
	// for (MiniArticleElementView miniArticleElement :
	// miniArticleElementViewCollection) {
	// miniArticleElement.setState(state);
	// }
	// }
	//
	// @Override
	// public void setDisactiveState() {
	// state = State.DISACTIVE;
	//
	// if (backgroundElementView != null) {
	// backgroundElementView.setState(state);
	// }
	//
	// for (MiniArticleElementView miniArticleElement :
	// miniArticleElementViewCollection) {
	// miniArticleElement.setState(state);
	//
	// }
	// }
	//
	// @Override
	// public void setReleaseState() {
	// state = State.RELEASE;
	// if (backgroundElementView != null)
	// backgroundElementView.setState(state);
	//
	// for (MiniArticleElementView miniArticleElement :
	// miniArticleElementViewCollection) {
	// miniArticleElement.setState(state);
	// }
	//
	// }
	//
	// @Override
	// public void activateElementView(BaseElementView elementView) {
	// for (MiniArticleElementView miniArticleElement :
	// miniArticleElementViewCollection) {
	// if (miniArticleElement.equals(elementView)) {
	// miniArticleElement.activateElement();
	// } else
	// miniArticleElement.disactivateElement();
	// }
	//
	// }
	//
	@Override
	public void activateElementView(int number) {
		if (miniArticleElementViewCollection != null)

			for (int i = 0; i < miniArticleElementViewCollection.size(); i++) {
				View bgView = miniArticleElementViewCollection.get(i)
						.getBackgroundElementView().getView(getContext());
				if (i == number - 1) {
					if (bgView.getParent() == null) {
						viewGroupMinArticleBG.addView(bgView);

					}
					bgView.bringToFront();

					miniArticleElementViewCollection.get(i).activateElement();
				} else {
					// if (bgView.getParent() != null) {
					// viewGroupMinArticleBG.removeView(bgView);
					// }
					miniArticleElementViewCollection.get(i)
							.disactivateElement();
				}
			}

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

	// @Override
	// public void activateElementView(BaseElementView elementView) {
	//
	// if (miniArticleElementViewCollection != null)
	// for (MiniArticleElementView miniArticleElement :
	// miniArticleElementViewCollection) {
	//
	// View bgView = miniArticleElement.getBackgroundElementView()
	// .getView(getContext());
	//
	// if (miniArticleElement.equals(elementView)) {
	// if (bgView.getParent() == null) {
	// viewGroupMinArticleBG.addView(bgView);
	// bgView.bringToFront();
	// }
	//
	// miniArticleElement.activateElement();
	//
	// } else {
	// if (bgView.getParent() != null) {
	// viewGroupMinArticleBG.removeView(bgView);
	// }
	// miniArticleElement.disactivateElement();
	//
	// }
	// }
	//
	// }

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
}
