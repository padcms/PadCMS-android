package padcms.magazine.page.elementview;

import padcms.dao.issue.bean.Element;
import padcms.magazine.page.BasePageView;
import android.content.Context;
import android.view.View;

/**
 * html
 * 
 * Displays HTML page when user rotate device (template_type=“rotation”) or user
 * touches the display (template_type=“touch”)
 * 
 * resource (string) relative path to the resource html_url (string) page's URL
 * template_type (enum) touch | rotation
 */
public class HtmlElementView extends BaseElementView {
	private String htmlUrlStr;
	private TemplateType templateType;

	public HtmlElementView(BasePageView parentPageView, Element element) {
		super(parentPageView, element);
		// TODO Auto-generated constructor stub
	}

	public String getHtmlUrlStr() {
		return htmlUrlStr;
	}

	public void setHtmlUrlStr(String htmlUrlStr) {
		this.htmlUrlStr = htmlUrlStr;
	}

	public TemplateType getTemplateType() {
		return templateType;
	}

	public void setTemplateType(TemplateType templateType) {
		this.templateType = templateType;
	}

	public enum TemplateType {
		TOUCH(20), ROTATION(21);
		private int typeValue;

		private TemplateType(int typeValue) {
			this.typeValue = typeValue;
		}

		public int getCode() {
			return this.typeValue;
		}

		/**
		 * @param val
		 *            - int value
		 * @return Instance of this enum or null if no instance for
		 *         <code>val</code>
		 */
		static public TemplateType getTypeValueOf(int val) {
			for (TemplateType typeValue : TemplateType.values()) {
				if (val == typeValue.getCode()) {
					return typeValue;
				}
			}
			return null;
		}
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
