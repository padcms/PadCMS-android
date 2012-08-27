package padcms.magazine.controls;

import java.util.ArrayList;

import padcms.magazine.controls.SmoothFliperView.MoveSideAction;
import padcms.magazine.page.BasePageView;
import padcms.magazine.page.HorisontalPageView;
import android.content.Context;
import android.view.View;

public class HorisontalPageSmoothFliperAdapter extends BaseSmoothFliperAdapter {
	private HorisontalPageView currentPage;
	private HorisontalPageView customNextPage;

	public HorisontalPageSmoothFliperAdapter(Context context,
			ArrayList<HorisontalPageView> pageCollection) {
		super(context);

		currentPage = pageCollection.get(0);
	}

	public HorisontalPageView getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(HorisontalPageView currentPage) {
		this.currentPage = currentPage;
	}

	@Override
	public void initFirstView() {
		getSmoothFliperView().setFirstView(currentPage.getView(getContext()));

		currentPage.setActiveState();

		if (currentPage.getRightPage() != null)
			currentPage.getRightPage().setDisactiveState();

		if (currentPage.getLeftPage() != null)
			currentPage.getLeftPage().setDisactiveState();

	}

	@Override
	public boolean allowToFlip(SmoothFliperView.MoveSideAction sideAction) {
		boolean allowToFlip = false;
		HorisontalPageView nextPage = getNextPageBySlideAction(sideAction);

		if (nextPage != null)
			allowToFlip = true;

		return allowToFlip;
	}

	@Override
	public View getSideView(SmoothFliperView.MoveSideAction sideAction) {

		HorisontalPageView nextPage = getNextPageBySlideAction(sideAction);

		if (nextPage != null) {
			// nextPage.setDisactiveState();
			nextPage.setDisactiveState();
			return nextPage.getView(getContext());
		}
		return null;
	}

	@Override
	public void activationView(MoveSideAction sideAction, boolean flipToFinish) {

		if (flipToFinish) {
			HorisontalPageView nextPage = null;

			if (customNextPage == null) {
				switch (sideAction) {
				case HORISONTAL_FROM_RIGHT_TO_LEFT:
					if (currentPage.getRightPage() != null) {
						nextPage = currentPage.getRightPage();

						// nextPage.setLeftPage(currentPage);
					}
					break;

				case HORISONTAL_FROM_LEFT_TO_RIGHT:
					if (currentPage.getLeftPage() != null) {
						nextPage = currentPage.getLeftPage();
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
						currentPage.getRightPage().getLeftPage();
						currentPage.getLeftPage().setDisactiveState();
						setPageSidesReleaseState(currentPage.getLeftPage());
					}

					break;
				default:
					break;
				}

			}
		}
	}

	public void setPageSidesReleaseState(HorisontalPageView pageView) {
		if (pageView.getRightPage() != null
				&& pageView.getRightPage() != currentPage) {
			pageView.getRightPage().setReleaseState();
		}
		if (pageView.getLeftPage() != null
				&& pageView.getLeftPage() != currentPage) {
			pageView.getLeftPage().setReleaseState();
		}
	}

	public void setPageSidesHorisontalReleaseState(HorisontalPageView pageView) {
		if (pageView.getRightPage() != null
				&& pageView.getRightPage() != currentPage)
			pageView.getRightPage().setReleaseState();

		if (pageView.getLeftPage() != null
				&& pageView.getLeftPage() != currentPage)
			pageView.getLeftPage().setReleaseState();
	}

	public void setPageSidesVerticalReleaseState(BasePageView pageView) {

	}

	private HorisontalPageView getNextPageBySlideAction(
			SmoothFliperView.MoveSideAction sideAction) {
		HorisontalPageView nextPage = null;
		switch (sideAction) {
		case HORISONTAL_FROM_RIGHT_TO_LEFT:
			nextPage = currentPage.getRightPage();
			break;

		case HORISONTAL_FROM_LEFT_TO_RIGHT:
			nextPage = currentPage.getLeftPage();
			break;
		}
		return nextPage;
	}

	@Override
	public void onViewClicked() {
		// currentPage.pageViewClicked();

	}

	private void flipToPage(HorisontalPageView pageFlipTo,
			MoveSideAction motionActionSlide) {
		customNextPage = pageFlipTo;
		pageFlipTo.setDisactiveState();
		getSmoothFliperView().changeSideView(pageFlipTo.getView(getContext()),
				motionActionSlide);
		getSmoothFliperView().flipToNextView(motionActionSlide);

	}

	public void flipToRightPage(HorisontalPageView pageFlipTo) {
		flipToPage(pageFlipTo, MoveSideAction.HORISONTAL_FROM_RIGHT_TO_LEFT);
	}

	public void flipToLeftPage(HorisontalPageView pageFlipTo) {
		flipToPage(pageFlipTo, MoveSideAction.HORISONTAL_FROM_LEFT_TO_RIGHT);
	}

	@Override
	public void cleanAdapter() {
		currentPage.setReleaseState();

		if (currentPage.getRightPage() != null)
			currentPage.getRightPage().setReleaseState();

		if (currentPage.getLeftPage() != null)
			currentPage.getLeftPage().setReleaseState();
	}

	@Override
	public void startFlipAnimation() {
		// currentPage.setDisactiveState();

	}
}
