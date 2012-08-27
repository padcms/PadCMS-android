package padcms.bll.youtube;

public class StringsStore {
	public static final String YOUTUBE_VIDEO_INFORMATION_URL = "http://www.youtube.com/get_video_info?&video_id=";
	public static final String YOUTUBE_PLAYLIST_ATOM_FEED_URL = "http://gdata.youtube.com/feeds/api/playlists/";

	public final static String MSG_INIT = "com.keyes.video.msg.init";
	public final static String mMsgInit = "Initializing";

	public final static String MSG_DETECT = "com.keyes.video.msg.detect";
	public final static String mMsgDetect = "Detecting Bandwidth";

	public final static String MSG_PLAYLIST = "com.keyes.video.msg.playlist";
	public final static String mMsgPlaylist = "Determining Latest Video in YouTube Playlist";

	public final static String MSG_TOKEN = "com.keyes.video.msg.token";
	public final static String mMsgToken = "Retrieving YouTube Video Token";

	public final static String MSG_LO_BAND = "com.keyes.video.msg.loband";
	public final static String mMsgLowBand = "Buffering Low-bandwidth Video";

	public final static String MSG_HI_BAND = "com.keyes.video.msg.hiband";
	public final static String mMsgHiBand = "Buffering High-bandwidth Video";

	public final static String MSG_ERROR_TITLE = "com.keyes.video.msg.error.title";
	public final static String mMsgErrorTitle = "Communications Error";

	public final static String MSG_ERROR_MSG = "com.keyes.video.msg.error.msg";
	public final static String mMsgError = "An error occurred during the retrieval of the video.  This could be due to network issues or YouTube protocols.  Please try again later.";

	public static final String SCHEME_YOUTUBE_VIDEO = "ytv";
	public static final String SCHEME_YOUTUBE_PLAYLIST = "ytpl";
	
	public static final String MSG_TITLE_PLAY_VIDEO_ON_CONCURRENT_DOWNLOAD = "Sorry cannot play media file";
	public static final String MSG_PLAY_VIDEO_ON_CONCURRENT_DOWNLOAD = "Try wait until file download";
	
	public static final String YOUTUBE_COM = "youtube.com";
	public static final String URL_PATTERN = "^.*\\.[a-zA-Z0-9]{2,4}$";
}
