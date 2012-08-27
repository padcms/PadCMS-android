package padcms.magazine.page.elementview;

import java.util.ArrayList;
import java.util.List;

import padcms.dao.issue.bean.Element;
import padcms.dao.issue.bean.ElementData;
import padcms.magazine.factory.ElementViewFactory;
import padcms.magazine.factory.ResourceResolutionHelper;
import padcms.magazine.page.BasePageView;
import padcms.magazine.page.State;
import padcms.magazine.resource.ResourceController;
import padcms.magazine.resource.ResourceController.onUpdateProgress;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

/**
 * This is basic class for all view elements of page.
 * 
 */
public abstract class BaseElementView implements Comparable<BaseElementView>,
		onUpdateProgress {
	protected String resourcePathStr;
	protected float originalWidth = -1;
	protected float originalHeight = -1;
	protected int width;
	protected int height;
	private int weight;

	protected int elementBackgroundColor = Color.WHITE;

	private Element element;
	private State state = State.RELEASE;

	private BasePageView parentPageView;
	protected List<ElementData> elementDataCollection;
	protected List<ElementData> activeZoneElementDataCollection;
	protected List<ActiveZoneElementDataView> activeZoneViewCollection;
	protected ResourceController resourceController;
	private ProgressDownloadElementView progressDownloadElementView;

	public BaseElementView(BasePageView parentPageView, Element element) {
		this.parentPageView = parentPageView;
		this.element = element;
		if (element != null) {
			this.elementDataCollection = element.getListElementData();
			resourcePathStr = ElementViewFactory
					.getElementDataStringValue(ElementViewFactory
							.getElementDataCurrentType(elementDataCollection,
									"resource"));

			originalWidth = ElementViewFactory.getElementDataFloatValue(
					ElementViewFactory.getElementDataCurrentType(
							elementDataCollection, "width"), -1);
			originalHeight = ElementViewFactory.getElementDataFloatValue(
					ElementViewFactory.getElementDataCurrentType(
							elementDataCollection, "height"), -1);

			// initElementViewData(parentPageView, element);

		}

	}

	public void initElementViewData() {
		if (element != null) {
			ResourceResolutionHelper resourceResolution = ResourceResolutionHelper
					.getResourceResolutionScaled(parentPageView.getContext(),
							originalWidth, originalHeight);

			width = resourceResolution.width;
			height = resourceResolution.height;

			weight = element.getWeight().intValue();
			activeZoneElementDataCollection = ElementViewFactory
					.getElementDataCollectionCurrentType(elementDataCollection,
							"active_zone");

			loadActiveZoneView();
		}
	}

	public ProgressDownloadElementView getProgressDownloadElementView() {
		return progressDownloadElementView;
	}

	public void setProgressDownloadElementView(
			ProgressDownloadElementView progressDownloadElementView) {
		this.progressDownloadElementView = progressDownloadElementView;
	}

	public abstract View getView(Context context);

	public String getResourcePathStr() {
		return resourcePathStr;
	}

	public void setResourcePathStr(String resource) {
		this.resourcePathStr = resource;
	}

	public void setState(State state) {
		this.state = state;

	}

	public State getState() {
		return this.state;
	}

	public void downloadElement() {

	}

	public void activateElement() {

	}

	public void disactivateElement() {

	}

	public int getElementBackgroundColor() {
		return elementBackgroundColor;
	}

	public void setElementBackgroundColor(int elementBackgroundColor) {
		this.elementBackgroundColor = elementBackgroundColor;
	}

	public ResourceController getResourceController() {
		return resourceController;
	}

	public void setResourceController(ResourceController resourceController) {
		this.resourceController = resourceController;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public BasePageView getParentPageView() {
		return parentPageView;
	}

	public List<ActiveZoneElementDataView> getActiveZonewViewCollection() {
		return activeZoneViewCollection;
	}

	public void setActiveZonewViewCollection(
			List<ActiveZoneElementDataView> activeZonewViewCollection) {
		this.activeZoneViewCollection = activeZonewViewCollection;
	}

	public void loadActiveZoneView() {
		if (activeZoneElementDataCollection != null) {
			activeZoneViewCollection = new ArrayList<ActiveZoneElementDataView>();
			for (ElementData activeZoneData : activeZoneElementDataCollection) {

				ActiveZoneElementDataView activeZoneElementView = new ActiveZoneElementDataView(
						parentPageView.getContext(), parentPageView,
						activeZoneData, originalWidth, originalHeight);

				activeZoneViewCollection.add(activeZoneElementView);

			}
		}
	}

	public void initViewWithActiveZone(ViewGroup parentView) {
		if (activeZoneViewCollection != null) {
			for (ActiveZoneElementDataView activeZoneElementView : activeZoneViewCollection) {
				if (activeZoneElementView.getActiveZoneKey().equals("scroller")
						|| activeZoneElementView.getActiveZoneKey().startsWith(
								"thumbs")) {
				} else {
					if (activeZoneElementView.getParent() != null) {
						((ViewGroup) activeZoneElementView.getParent())
								.removeView(activeZoneElementView);
					}
					parentView.addView(activeZoneElementView);
					activeZoneElementView.bringToFront();
				}
			}
		}

	}

	public void destroyActiveZoneViews() {
		if (activeZoneViewCollection != null) {
			for (ActiveZoneElementDataView activeZoneElementView : activeZoneViewCollection) {
				if (activeZoneElementView.getParent() != null) {
					((ViewGroup) activeZoneElementView.getParent())
							.removeView(activeZoneElementView);

				}
			}
		}

	}

	public int compareTo(BaseElementView baseElementCommpare) {
		int rez = 0;
		if (getWeight() > baseElementCommpare.getWeight()) {
			rez = 1;
		} else if (getWeight() < baseElementCommpare.getWeight()) {
			rez = -1;
		}

		return rez;
	}

	public void destroyElementView() {
	}

	@Override
	public void showProgress() {

		if (getProgressDownloadElementView() != null) {
			runInUI(new Runnable() {
				public void run() {
					getProgressDownloadElementView().showProgress();
				}
			});
		}
	}

	@Override
	public void startProgress(final int maxValue) {
		if (getProgressDownloadElementView() != null) {
			runInUI(new Runnable() {
				public void run() {
					getProgressDownloadElementView().setMaxValue(maxValue);
				}
			});
		}
	}

	@Override
	public void setProgress(final int valueProgress) {
		if (getProgressDownloadElementView() != null) {
			runInUI(new Runnable() {
				public void run() {
					getProgressDownloadElementView().setProgressValue(
							valueProgress);
				}
			});
		}
	}

	@Override
	public void hideProgress() {

		if (getProgressDownloadElementView() != null) {
			runInUI(new Runnable() {
				public void run() {
					getProgressDownloadElementView().hideFastView();
				}
			});

		}

	}

	public ResourceResolutionHelper getResolutionForController(Bitmap bitmap) {

		return ResourceResolutionHelper.getBitmapResolution(getParentPageView()
				.getContext(), bitmap.getWidth(), bitmap.getHeight());
	}

	public abstract View getShowingView();

	public void runInUI(Runnable runnable) {
		if (parentPageView != null)
			((Activity) parentPageView.getContext()).runOnUiThread(runnable);
	}
	// @Override
	// public int compare(BaseElementView baseElement1,
	// BaseElementView baseElement2) {
	// int rez = 0;
	// if (baseElement1.getWeight() > baseElement2.getWeight()) {
	// rez = 1;
	// } else if (baseElement1.getWeight() > baseElement2.getWeight()) {
	// rez = 2;
	// }
	// return rez;
	// }

}