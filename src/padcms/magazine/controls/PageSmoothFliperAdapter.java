package padcms.magazine.controls;

import padcms.magazine.controls.SmoothFliperView.MoveSideAction;
import padcms.magazine.page.BasePageView;
import padcms.magazine.page.State;
import android.content.Context;
import android.view.View;

public class PageSmoothFliperAdapter extends BaseSmoothFliperAdapter {

	private BasePageView currentPage;
	private BasePageView customNextPage;

	public PageSmoothFliperAdapter(Context context, BasePageView currentPage) {
		super(context);
		this.currentPage = currentPage;// pageCollection.get(0);
	}

	public BasePageView getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(BasePageView currentPage) {
		this.currentPage = currentPage;
	}

	@Override
	public void initFirstView() {
		getSmoothFliperView().setFirstView(currentPage.getView(getContext()));
		currentPage.setState(State.ACTIVE);

		if (currentPage.getRightPage() != null) {
			currentPage.getRightPage().getView(getContext());
			currentPage.getRightPage().setDisactiveState();
		}

		if (currentPage.getLeftPage() != null) {
			currentPage.getLeftPage().getView(getContext());
			currentPage.getLeftPage().setDisactiveState();
		}
		if (currentPage.getTopPage() != null) {
			currentPage.getTopPage().getView(getContext());
			currentPage.getTopPage().setDisactiveState();
		}
		if (currentPage.getBottomPage() != null) {
			currentPage.getBottomPage().getView(getContext());
			currentPage.getBottomPage().setDisactiveState();
		}

	}

	@Override
	public boolean allowToFlip(SmoothFliperView.MoveSideAction sideAction) {
		boolean allowToFlip = false;
		BasePageView nextPage = getNextPageBySlideAction(sideAction);

		if (nextPage != null)
			allowToFlip = true;

		return allowToFlip;
	}

	@Override
	public View getSideView(SmoothFliperView.MoveSideAction sideAction) {

		BasePageView nextPage = getNextPageBySlideAction(sideAction);

		if (nextPage != null) {
			nextPage.setDisactiveState();
			return nextPage.getView(getContext());
		}
		return null;
	}

	@Override
	public void activationView(MoveSideAction sideAction, boolean flipToFinish) {

		if (flipToFinish) {
			BasePageView nextPage = null;

			if (customNextPage == null) {
				switch (sideAction) {
				case HORISONTAL_FROM_RIGHT_TO_LEFT:
					if (currentPage.getRightPage() != null) {
						nextPage = currentPage.getRightPage();

					}
					break;

				case HORISONTAL_FROM_LEFT_TO_RIGHT:
					if (currentPage.getLeftPage() != null) {
						nextPage = currentPage.getLeftPage();
					}
					break;

				case VERTICAL_FROM_BOTTOM_TO_TOP:
					if (currentPage.getBottomPage() != null) {
						nextPage = currentPage.getBottomPage();

						if (currentPage.getRightPage() != null)
							nextPage.setRightPage(currentPage.getRightPage());

						if (currentPage.getLeftPage() != null) {
							currentPage.getLeftPage().setRightPage(nextPage);
						}

					}
					break;

				case VERTICAL_FROM_TOP_TO_BOTTOM:
					if (currentPage.getTopPage() != null) {
						nextPage = currentPage.getTopPage();

						if (currentPage.getRightPage() != null)
							nextPage.setRightPage(currentPage.getRightPage());

						if (currentPage.getLeftPage() != null) {
							currentPage.getLeftPage().setRightPage(nextPage);
						}

					}
					break;
				}
			} else {
				nextPage = customNextPage;
				currentPage.setReleaseState();
				customNextPage = null;
			}
			if (nextPage != null) {

				currentPage = nextPage;
				currentPage.setActiveState();
				switch (sideAction) {
				case HORISONTAL_FROM_LEFT_TO_RIGHT:
				case HORISONTAL_FROM_RIGHT_TO_LEFT:
					if (currentPage.getRightPage() != null) {
						currentPage.getRightPage().getView(getContext());
						currentPage.getRightPage().setDisactiveState();
						setPageSidesReleaseState(currentPage.getRightPage());
					}

					if (currentPage.getLeftPage() != null) {
						currentPage.getLeftPage().getView(getContext());
						currentPage.getLeftPage().setDisactiveState();
						setPageSidesReleaseState(currentPage.getLeftPage());
					}

					if (currentPage.getTopPage() != null) {
						currentPage.getTopPage().getView(getContext());
						currentPage.getTopPage().setDisactiveState();
						setPageSidesReleaseState(currentPage.getTopPage());

					}

					if (currentPage.getBottomPage() != null) {
						currentPage.getBottomPage().getView(getContext());
						currentPage.getBottomPage().setDisactiveState();
						setPageSidesReleaseState(currentPage.getBottomPage());
					}

					break;
				case VERTICAL_FROM_TOP_TO_BOTTOM:
				case VERTICAL_FROM_BOTTOM_TO_TOP:
					if (currentPage.getRightPage() != null) {
						currentPage.getRightPage().getView(getContext());

						currentPage.getRightPage().setDisactiveState();
						setPageSidesReleaseState(currentPage.getRightPage());
					}

					if (currentPage.getLeftPage() != null) {
						currentPage.getLeftPage().getView(getContext());

						currentPage.getLeftPage().setDisactiveState();
						setPageSidesReleaseState(currentPage.getLeftPage());
					}

					if (currentPage.getTopPage() != null) {
						currentPage.getTopPage().getView(getContext());

						currentPage.getTopPage().setDisactiveState();
						setPageSidesVerticalReleaseState(currentPage
								.getTopPage());

					}

					if (currentPage.getBottomPage() != null) {
						currentPage.getBottomPage().getView(getContext());
						currentPage.getBottomPage().setDisactiveState();
						setPageSidesVerticalReleaseState(currentPage
								.getBottomPage());
					}
					break;
				default:
					break;
				}

			}
		}
	}

	public void setPageSidesReleaseState(BasePageView pageView) {
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

	public void setPageSidesHorisontalReleaseState(BasePageView pageView) {
		if (pageView.getRightPage() != null
				&& pageView.getRightPage() != currentPage)
			pageView.getRightPage().setReleaseState();

		if (pageView.getLeftPage() != null
				&& pageView.getLeftPage() != currentPage)
			pageView.getLeftPage().setReleaseState();
	}

	public void setPageSidesVerticalReleaseState(BasePageView pageView) {

	}

	private BasePageView getNextPageBySlideAction(
			SmoothFliperView.MoveSideAction sideAction) {
		BasePageView nextPage = null;
		switch (sideAction) {
		case HORISONTAL_FROM_RIGHT_TO_LEFT:
			nextPage = currentPage.getRightPage();
			break;

		case HORISONTAL_FROM_LEFT_TO_RIGHT:
			nextPage = currentPage.getLeftPage();
			break;

		case VERTICAL_FROM_BOTTOM_TO_TOP:
			nextPage = currentPage.getBottomPage();
			break;

		case VERTICAL_FROM_TOP_TO_BOTTOM:
			nextPage = currentPage.getTopPage();
			break;
		}
		return nextPage;
	}

	@Override
	public void onViewClicked() {
		currentPage.pageViewClicked();

	}

	private void flipToPage(BasePageView pageFlipTo,
			MoveSideAction motionActionSlide) {
		customNextPage = pageFlipTo;
		pageFlipTo.setDisactiveState();
		getSmoothFliperView().changeSideView(pageFlipTo.getView(getContext()),
				motionActionSlide);
		getSmoothFliperView().flipToNextView(motionActionSlide);

	}

	public void flipToRightPage(BasePageView pageFlipTo) {
		flipToPage(pageFlipTo, MoveSideAction.HORISONTAL_FROM_RIGHT_TO_LEFT);
	}

	public void flipToLeftPage(BasePageView pageFlipTo) {
		flipToPage(pageFlipTo, MoveSideAction.HORISONTAL_FROM_LEFT_TO_RIGHT);
	}

	public void flipToBottomPage(BasePageView pageFlipTo) {
		flipToPage(pageFlipTo, MoveSideAction.VERTICAL_FROM_TOP_TO_BOTTOM);
	}

	public void flipToTopPage(BasePageView pageFlipTo) {
		flipToPage(pageFlipTo, MoveSideAction.VERTICAL_FROM_BOTTOM_TO_TOP);
	}

	@Override
	public void cleanAdapter() {

		currentPage.cleanPageView();// setReleaseState();

		if (currentPage.getRightPage() != null)
			currentPage.getRightPage().cleanPageView();

		if (currentPage.getLeftPage() != null)
			currentPage.getLeftPage().cleanPageView();

		if (currentPage.getTopPage() != null)
			currentPage.getTopPage().cleanPageView();

		if (currentPage.getBottomPage() != null)
			currentPage.getBottomPage().cleanPageView();
	}

	@Override
	public void startFlipAnimation() {
		// currentPage.setDisactiveState();
	}
}
