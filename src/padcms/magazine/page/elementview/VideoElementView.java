package padcms.magazine.page.elementview;

import padcms.dao.issue.bean.Element;
import padcms.magazine.factory.ElementViewFactory;
import padcms.magazine.page.BasePageView;
import padcms.net.NetHepler;
import android.content.Context;
import android.view.View;

/**
 * video
 * 
 * Video is an MP4 or a stream URL. Video must be launched using active zones
 * inside PDF or HTML. Once launched it opens classic full screen video player.
 * 
 * resource (string) relative path to the resource stream (string) the URL to
 * the video stream
 */
public class VideoElementView extends BaseElementView {
	private String pathToStreamStr;
	private String stream;

	public VideoElementView(BasePageView parentPageView, Element element) {

		super(parentPageView, element);

		if (resourcePathStr != null && resourcePathStr.length() > 0) {
			resourcePathStr = NetHepler.getAbsoluteUrlToVideo(resourcePathStr);
		}

		pathToStreamStr = ElementViewFactory
				.getElementDataStringValue(ElementViewFactory
						.getElementDataCurrentType(elementDataCollection,
								"stream"));
		// if (pathToStreamStr != null && pathToStreamStr.length() > 0) {
		// pathToStreamStr = NetHepler.getAbsoluteUrlToVideo(pathToStreamStr);
		// }

	}

	public String getPathToStreamStr() {
		return pathToStreamStr;
	}

	public void setPathToStreamStr(String pathToStreamStr) {
		this.pathToStreamStr = pathToStreamStr;
	}

	@Override
	public View getView(Context context) {
		return null;
	}

	@Override
	public View getShowingView() {
		// TODO Auto-generated method stub
		return null;
	}
}
