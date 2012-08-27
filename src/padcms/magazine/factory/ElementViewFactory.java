package padcms.magazine.factory;

import java.util.ArrayList;
import java.util.List;

import padcms.dao.issue.bean.Element;
import padcms.dao.issue.bean.ElementData;
import padcms.magazine.page.BasePageView;
import padcms.magazine.page.elementview.ActiveZoneElementDataView;
import padcms.magazine.page.elementview.AdvertElementView;
import padcms.magazine.page.elementview.BackgroundElementView;
import padcms.magazine.page.elementview.BaseElementView;
import padcms.magazine.page.elementview.BodyElementView;
import padcms.magazine.page.elementview.DragAndDropElementView;
import padcms.magazine.page.elementview.GalaryElementView;
import padcms.magazine.page.elementview.Html5ElementView;
import padcms.magazine.page.elementview.HtmlElementView;
import padcms.magazine.page.elementview.MiniArticleElementView;
import padcms.magazine.page.elementview.OverlayElementView;
import padcms.magazine.page.elementview.PopupElementView;
import padcms.magazine.page.elementview.ScrollingPaneElementView;
import padcms.magazine.page.elementview.SlideElementView;
import padcms.magazine.page.elementview.SoundElementView;
import padcms.magazine.page.elementview.VideoElementView;

public class ElementViewFactory {

	public static final String ELEMENT_NAME_BODY = "body";
	public static final String ELEMENT_NAME_GALLERY = "gallery";
	public static final String ELEMENT_NAME_VIDEO = "video";
	public static final String ELEMENT_NAME_BACKGROUND = "background";
	public static final String ELEMENT_NAME_ADVERT = "advert";
	public static final String ELEMENT_NAME_SCROLLING_PENE = "scrolling_pane";
	public static final String ELEMENT_NAME_MIN_ARTICLE = "mini_article";
	public static final String ELEMENT_NAME_SLIDE = "slide";
	public static final String ELEMENT_NAME_OVERLAY = "overlay";
	public static final String ELEMENT_NAME_SOUND = "sound";
	public static final String ELEMENT_NAME_HTML = "html";
	public static final String ELEMENT_NAME_HTML5 = "html5";
	public static final String ELEMENT_NAME_DRAG_AND_DROP = "drag_and_drop";
	public static final String ELEMENT_NAME_POPUP = "popup";
	public static final String ELEMENT_NAME_ACTIVE_ZONE = "active_zone";

	public static ArrayList<BaseElementView> getElementViewCollectionFromListElementBin(
			BasePageView parentPageView, List<Element> elementCollection) {
		ArrayList<BaseElementView> elementViewCollection = new ArrayList<BaseElementView>();

		for (Element element : elementCollection) {
			String elementTypeName = element.getElement_type_name();
			BaseElementView elementView = null;

			if (elementTypeName.equals(ELEMENT_NAME_ADVERT)) {
				elementView = new AdvertElementView(parentPageView, element);
			} else if (elementTypeName.equals(ELEMENT_NAME_BACKGROUND)) {
				elementView = new BackgroundElementView(parentPageView, element);
			} else if (elementTypeName.equals(ELEMENT_NAME_BODY)) {
				elementView = new BodyElementView(parentPageView, element);
			} else if (elementTypeName.equals(ELEMENT_NAME_DRAG_AND_DROP)) {
				elementView = new DragAndDropElementView(parentPageView,
						element);
			} else if (elementTypeName.equals(ELEMENT_NAME_GALLERY)) {
				elementView = new GalaryElementView(parentPageView, element);
			} else if (elementTypeName.equals(ELEMENT_NAME_HTML)) {
				elementView = new HtmlElementView(parentPageView, element);
			} else if (elementTypeName.equals(ELEMENT_NAME_HTML5)) {
				elementView = new Html5ElementView(parentPageView, element);
			} else if (elementTypeName.equals(ELEMENT_NAME_MIN_ARTICLE)) {
				elementView = new MiniArticleElementView(parentPageView,
						element);
			} else if (elementTypeName.equals(ELEMENT_NAME_OVERLAY)) {
				elementView = new OverlayElementView(parentPageView, element);
			} else if (elementTypeName.equals(ELEMENT_NAME_POPUP)) {
				elementView = new PopupElementView(parentPageView, element);
			} else if (elementTypeName.equals(ELEMENT_NAME_SCROLLING_PENE)) {
				elementView = new ScrollingPaneElementView(parentPageView,
						element);
			} else if (elementTypeName.equals(ELEMENT_NAME_SLIDE)) {
				elementView = new SlideElementView(parentPageView, element);
			} else if (elementTypeName.equals(ELEMENT_NAME_SOUND)) {
				elementView = new SoundElementView(parentPageView, element);
			} else if (elementTypeName.equals(ELEMENT_NAME_VIDEO)) {
				elementView = new VideoElementView(parentPageView, element);
			}
			if (elementView != null)
				elementViewCollection.add(elementView);
		}

		return elementViewCollection;
	}

	public static ElementData getElementDataCurrentType(
			List<ElementData> elementDataCollection, String currentElementType) {

		for (ElementData elementData : elementDataCollection) {

			if (elementData.getType().equals(currentElementType)) {
				return elementData;
			}
		}

		return null;
	}

	public static List<ElementData> getElementDataCollectionCurrentType(
			List<ElementData> elementDataCollection, String typeElementData) {
		List<ElementData> elementDataCollectionCurrentType = new ArrayList<ElementData>();
		for (ElementData elementData : elementDataCollection) {
			String elementDataTypeName = elementData.getType();

			if (elementDataTypeName.equals(typeElementData)) {
				elementDataCollectionCurrentType.add(elementData);
			}
		}

		return elementDataCollectionCurrentType;
	}

	public static int getElementDataIntValue(ElementData elementData) {
		int valueElementData = 0;
		if (elementData != null) {
			try {
				valueElementData = Integer.parseInt(elementData.getValue());
			} catch (Exception e) {

			}
		}
		return valueElementData;
	}

	public static String getElementDataStringValue(ElementData elementData) {
		String valueElementData = "";
		if (elementData != null) {
			try {
				valueElementData = elementData.getValue();
			} catch (Exception e) {

			}
		}
		return valueElementData;
	}

	public static float getElementDataFloatValue(ElementData elementData) {
		float valueElementData = 0;
		if (elementData != null) {
			try {
				valueElementData = Float.parseFloat(elementData.getValue());
			} catch (Exception e) {

			}
		}
		return valueElementData;
	}

	public static boolean getElementDataBooleanValue(ElementData elementData) {
		boolean valueElementData = false;
		if (elementData != null) {
			try {
				valueElementData = Boolean.parseBoolean(elementData.getValue());
			} catch (Exception e) {

			}
		}
		return valueElementData;
	}

	public static int getElementDataIntValue(ElementData elementData,
			int defaultValue) {
		int valueElementData = defaultValue;
		if (elementData != null) {
			try {
				valueElementData = Integer.parseInt(elementData.getValue());
			} catch (Exception e) {

			}
		}
		return valueElementData;
	}

	public static String getElementDataStringValue(ElementData elementData,
			String defaultValue) {
		String valueElementData = defaultValue;
		if (elementData != null) {
			try {
				valueElementData = elementData.getValue();
			} catch (Exception e) {

			}
		}
		return valueElementData;
	}

	public static float getElementDataFloatValue(ElementData elementData,
			float defaultValue) {
		float valueElementData = defaultValue;
		if (elementData != null) {
			try {
				valueElementData = Float.parseFloat(elementData.getValue());
			} catch (Exception e) {

			}
		}
		return valueElementData;
	}

	public static boolean getElementDataBooleanValue(ElementData elementData,
			boolean defaultValue) {
		boolean valueElementData = defaultValue;
		if (elementData != null) {
			try {
				valueElementData = Boolean.parseBoolean(elementData.getValue());
			} catch (Exception e) {

			}
		}
		return valueElementData;
	}

	public static BaseElementView getElementViewByClassName(
			List<BaseElementView> elementViewCollection, Class<?> elementClass) {

		for (BaseElementView element : elementViewCollection) {
			if (element.getClass().equals(elementClass)) {
				return element;
			}
		}

		return null;
	}

	public static List<?> getElementViewCollectionByClassName(
			List<BaseElementView> elementViewCollection, Class<?> elementClass) {

		List<BaseElementView> elementViewCollectionFinded = new ArrayList<BaseElementView>();

		for (BaseElementView element : elementViewCollection) {

			if (element.getClass().equals(elementClass)) {
				elementViewCollectionFinded.add(element);
			}

		}

		return elementViewCollectionFinded;
	}

	public static SlideElementView getSlideElementView(
			List<BaseElementView> elementViewCollection) {
		SlideElementView elementView = null;
		BaseElementView baseElementView = getElementViewByClassName(
				elementViewCollection, SlideElementView.class);
		if (baseElementView != null) {
			elementView = (SlideElementView) baseElementView;
		}
		return elementView;
	}

	public static BodyElementView getBodyElementView(
			List<BaseElementView> elementViewCollection) {
		BodyElementView elementView = null;
		BaseElementView baseElementView = getElementViewByClassName(
				elementViewCollection, BodyElementView.class);
		if (baseElementView != null) {
			elementView = (BodyElementView) baseElementView;
		}
		return elementView;
	}

	public static BackgroundElementView getBackgroundElementView(
			List<BaseElementView> elementViewCollection) {
		BackgroundElementView elementView = null;
		BaseElementView baseElementView = getElementViewByClassName(
				elementViewCollection, BackgroundElementView.class);
		if (baseElementView != null) {
			elementView = (BackgroundElementView) baseElementView;
		}
		return elementView;
	}

	public static VideoElementView getVideoElementView(
			List<BaseElementView> elementViewCollection) {
		VideoElementView elementView = null;
		BaseElementView baseElementView = getElementViewByClassName(
				elementViewCollection, VideoElementView.class);
		if (baseElementView != null) {
			elementView = (VideoElementView) baseElementView;
		}
		return elementView;
	}

	public static SoundElementView getSoundElementView(
			List<BaseElementView> elementViewCollection) {
		SoundElementView elementView = null;
		BaseElementView baseElementView = getElementViewByClassName(
				elementViewCollection, SoundElementView.class);
		if (baseElementView != null) {
			elementView = (SoundElementView) baseElementView;
		}
		return elementView;
	}

	public static GalaryElementView getGalaryElementView(
			List<BaseElementView> elementViewCollection) {
		GalaryElementView elementView = null;
		BaseElementView baseElementView = getElementViewByClassName(
				elementViewCollection, GalaryElementView.class);
		if (baseElementView != null) {
			elementView = (GalaryElementView) baseElementView;
		}
		return elementView;
	}

	public static ScrollingPaneElementView getScrollingPaneElementView(
			List<BaseElementView> elementViewCollection) {
		ScrollingPaneElementView elementView = null;
		BaseElementView baseElementView = getElementViewByClassName(
				elementViewCollection, ScrollingPaneElementView.class);
		if (baseElementView != null) {
			elementView = (ScrollingPaneElementView) baseElementView;
		}
		return elementView;
	}

	@SuppressWarnings("unchecked")
	public static List<MiniArticleElementView> getMiniArticleElementView(
			List<BaseElementView> elementViewCollection) {

		List<MiniArticleElementView> elementViewMinArticleCollection = (List<MiniArticleElementView>) getElementViewCollectionByClassName(
				elementViewCollection, MiniArticleElementView.class);

		return elementViewMinArticleCollection;
	}

	@SuppressWarnings("unchecked")
	public static List<SlideElementView> getSlideElementViewCollection(
			List<BaseElementView> elementViewCollection) {

		List<SlideElementView> elementViewSlideElementCollection = (List<SlideElementView>) getElementViewCollectionByClassName(
				elementViewCollection, SlideElementView.class);

		return elementViewSlideElementCollection;
	}

	@SuppressWarnings("unchecked")
	public static List<GalaryElementView> getGalaryElementViewCollection(
			List<BaseElementView> elementViewCollection) {

		List<GalaryElementView> elementViewGalaryCollection = (List<GalaryElementView>) getElementViewCollectionByClassName(
				elementViewCollection, GalaryElementView.class);

		return elementViewGalaryCollection;
	}

	@SuppressWarnings("unchecked")
	public static List<PopupElementView> getPopupElementViewCollection(
			List<BaseElementView> elementViewCollection) {

		List<PopupElementView> elementViewGalaryCollection = (List<PopupElementView>) getElementViewCollectionByClassName(
				elementViewCollection, PopupElementView.class);

		return elementViewGalaryCollection;
	}

	public static AdvertElementView getAdvertElementView(
			List<BaseElementView> elementViewCollection) {
		AdvertElementView elementView = null;
		BaseElementView baseElementView = getElementViewByClassName(
				elementViewCollection, AdvertElementView.class);
		if (baseElementView != null) {
			elementView = (AdvertElementView) baseElementView;
		}
		return elementView;
	}

	public static OverlayElementView getOverlayElementView(
			List<BaseElementView> elementViewCollection) {
		OverlayElementView elementView = null;
		BaseElementView baseElementView = getElementViewByClassName(
				elementViewCollection, OverlayElementView.class);
		if (baseElementView != null) {
			elementView = (OverlayElementView) baseElementView;
		}
		return elementView;
	}

	public static HtmlElementView getHtmlElementView(
			List<BaseElementView> elementViewCollection) {
		HtmlElementView elementView = null;
		BaseElementView baseElementView = getElementViewByClassName(
				elementViewCollection, HtmlElementView.class);
		if (baseElementView != null) {
			elementView = (HtmlElementView) baseElementView;
		}
		return elementView;
	}

	public static Html5ElementView getHtml5ElementView(
			List<BaseElementView> elementViewCollection) {
		Html5ElementView elementView = null;
		BaseElementView baseElementView = getElementViewByClassName(
				elementViewCollection, Html5ElementView.class);
		if (baseElementView != null) {
			elementView = (Html5ElementView) baseElementView;
		}
		return elementView;
	}

	public static DragAndDropElementView getDragAndDropElementView(
			List<BaseElementView> elementViewCollection) {
		DragAndDropElementView elementView = null;
		BaseElementView baseElementView = getElementViewByClassName(
				elementViewCollection, DragAndDropElementView.class);
		if (baseElementView != null) {
			elementView = (DragAndDropElementView) baseElementView;
		}
		return elementView;
	}

	public static PopupElementView getPopupElementView(
			List<BaseElementView> elementViewCollection) {
		PopupElementView elementView = null;
		BaseElementView baseElementView = getElementViewByClassName(
				elementViewCollection, PopupElementView.class);
		if (baseElementView != null) {
			elementView = (PopupElementView) baseElementView;
		}
		return elementView;
	}

	public static List<ActiveZoneElementDataView> getActiveZoneElementView(
			List<BaseElementView> elementViewCollection) {

		return (List<ActiveZoneElementDataView>) getElementViewCollectionByClassName(
				elementViewCollection, ActiveZoneElementDataView.class);

	}
}
