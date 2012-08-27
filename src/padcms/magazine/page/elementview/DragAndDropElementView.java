package padcms.magazine.page.elementview;

import padcms.dao.issue.bean.Element;
import padcms.magazine.page.BasePageView;
import android.content.Context;
import android.view.View;

/**
 * drag_and_drop
 * 
 * We can add many mini-articles, there are displayed with different orientation
 * (gradient) and positionned randomly. You can define a top area (in pixel)
 * where there is no mini-articles.
 * 
 * resource (string) relative path to the resource thumbnail (string) relative
 * path to the thumbnail thumbnail_selected (string) relative path to the
 * thumbnail_selected video (string) relative path to the video top_area (int) a
 * top area (in pixel) where there is no mini-articles
 */
public class DragAndDropElementView extends BaseElementView {
	private String pathToThumbnailStr;
	private String pathToThumbnailSelectedStr;
	private String pathToVideoStr;
	private int topAreaNoArticlesHeight;

	public DragAndDropElementView(BasePageView parentPageView, Element element) {
		super(parentPageView, element);
		// TODO Auto-generated constructor stub
	}

	public String getPathToThumbnailStr() {
		return pathToThumbnailStr;
	}

	public void setPathToThumbnailStr(String pathToThumbnailStr) {
		this.pathToThumbnailStr = pathToThumbnailStr;
	}

	public String getPathToThumbnailSelectedStr() {
		return pathToThumbnailSelectedStr;
	}

	public void setPathToThumbnailSelectedStr(String pathToThumbnailSelectedStr) {
		this.pathToThumbnailSelectedStr = pathToThumbnailSelectedStr;
	}

	public String getPathToVideoStr() {
		return pathToVideoStr;
	}

	public void setPathToVideoStr(String pathToVideoStr) {
		this.pathToVideoStr = pathToVideoStr;
	}

	public int getTopAreaNoArticlesHeight() {
		return topAreaNoArticlesHeight;
	}

	public void setTopAreaNoArticlesHeight(int topAreaNoArticlesHeight) {
		this.topAreaNoArticlesHeight = topAreaNoArticlesHeight;
	}

	@Override
	public View getView(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getShowingView() {
		// TODO Auto-generated method stub
		return null;
	}

}
