package padcms.posting.twitter;

public class Constants {

	public static final String CONSUMER_KEY = "hvYX9rszFUEeFLyXiIAgg";
	public static final String CONSUMER_SECRET = "Md9vvgxZ1eXC7sCiMqtFGbeDV1tlln2JbX663ONrw4";

	public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
	public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
	public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";

	public static final String OAUTH_CALLBACK_SCHEME = "x-oauthflow-twitter";
	public static final String OAUTH_CALLBACK_HOST = "callback";
	public static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME
			+ "://" + OAUTH_CALLBACK_HOST;

}
