package padcms.magazine.page.elementview;

import padcms.dao.issue.bean.Element;
import padcms.magazine.page.BasePageView;
import android.content.Context;
import android.view.View;

/**
 * html5
 * 
 * html5_body (string) the type of html data: code, google_maps, rss_feed,
 * facebook_like, twitter post_code (string) HTML5 code (used with code type of
 * body) google_link_to_map (string) Link to google map (google_maps) rss_link
 * (string) Link to the rss (rss_feed) rss_link_number (integer) Amount of rss
 * links (rss_feed) facebook_name_page (string) Facebook page (facebook_like)
 * twitter_account (string) Twitter account name (twitter) twitter_tweet_number
 * (integer) Amount of displayed twites (twitter)
 */
public class Html5ElementView extends BaseElementView {
	private String html5BodyStr;
	private String postCodeStr;
	private String googleLinkToMapStr;
	private String rssLinkStr;
	private int rss_linkNumber;
	private String facebookNamePageStr;
	private String twitterAccountStr;
	private int twitterTweetNumber;

	public Html5ElementView(BasePageView parentPageView, Element element) {
		super(parentPageView, element);
		// TODO Auto-generated constructor stub
	}

	public String getHtml5BodyStr() {
		return html5BodyStr;
	}

	public void setHtml5BodyStr(String html5BodyStr) {
		this.html5BodyStr = html5BodyStr;
	}

	public String getPostCodeStr() {
		return postCodeStr;
	}

	public void setPostCodeStr(String postCodeStr) {
		this.postCodeStr = postCodeStr;
	}

	public String getGoogleLinkToMapStr() {
		return googleLinkToMapStr;
	}

	public void setGoogleLinkToMapStr(String googleLinkToMapStr) {
		this.googleLinkToMapStr = googleLinkToMapStr;
	}

	public String getRssLinkStr() {
		return rssLinkStr;
	}

	public void setRssLinkStr(String rssLinkStr) {
		this.rssLinkStr = rssLinkStr;
	}

	public int getRss_linkNumber() {
		return rss_linkNumber;
	}

	public void setRss_linkNumber(int rss_linkNumber) {
		this.rss_linkNumber = rss_linkNumber;
	}

	public String getFacebookNamePageStr() {
		return facebookNamePageStr;
	}

	public void setFacebookNamePageStr(String facebookNamePageStr) {
		this.facebookNamePageStr = facebookNamePageStr;
	}

	public String getTwitterAccountStr() {
		return twitterAccountStr;
	}

	public void setTwitterAccountStr(String twitterAccountStr) {
		this.twitterAccountStr = twitterAccountStr;
	}

	public int getTwitterTweetNumber() {
		return twitterTweetNumber;
	}

	public void setTwitterTweetNumber(int twitterTweetNumber) {
		this.twitterTweetNumber = twitterTweetNumber;
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
