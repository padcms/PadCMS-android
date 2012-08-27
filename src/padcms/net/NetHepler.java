package padcms.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import padcms.bll.ApplicationManager;
import padcms.net.listener.ResponseListenerGetIssues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;

public class NetHepler {

	public static String serviceHost;

	public static void isOnline(final Context context,
			final Handler handlerInternetConnetion) {
		new Thread(new Runnable() {
			public synchronized void run() {
				NetworkInfo ni = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
				Message msg = new Message();
				if (ni == null)
					msg.arg1 = 0;
				else
					msg.arg1 = ni.isConnectedOrConnecting() ? 1 : 0;
				handlerInternetConnetion.sendMessage(msg);
			}
		}, "isOnline").start();

	}

	public static String getUrlToDownloadRevision(Long revisionID) {
		return serviceHost + "/export/revision/id/" + revisionID;

	}

	public static String getAbsoulutePathToDownloadResourceImage(
			String pathToImage) {
		return serviceHost + pathToImage;
	}

	public static String getAbsoluteUrlToVideo(String relevantVideoUrl) {
		if (relevantVideoUrl.startsWith("http://")) {
			return relevantVideoUrl;
		}
		return serviceHost + "/resources/none"
				+ relevantVideoUrl.replace("/resources/none", "");

	}

	public static String getAbsoluteUrlCoverImage(String relevantVideoUrl) {
		return serviceHost + relevantVideoUrl;
	}

	public static String getUrlToResourceByDemetion(String pathToImage,
			String dimention) {
		return serviceHost + "/resources/" + dimention +  pathToImage;
	}

	public static String getUrlToResourceByDemetion(String pathToImage) {

		return serviceHost + "/resources/"
				+ ApplicationManager.elementResolution + pathToImage;
	}

	public static boolean isOnline(Context context) {
		NetworkInfo ni = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if (ni == null)
			return false;
		return ni.isConnectedOrConnecting();
	}

	public static boolean isUrlAvalible(Context context, String sUrl) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm.getActiveNetworkInfo().isConnectedOrConnecting()) {
				URL url = new URL(sUrl);
				HttpURLConnection urlc = (HttpURLConnection) url
						.openConnection();
				urlc.setRequestProperty("User-Agent", "PadCMS Android client");
				urlc.setRequestProperty("Connection", "close");
				urlc.setConnectTimeout(1000);

				urlc.connect();

				if (urlc.getResponseCode() == 200) {
					return true;
				} else {
					return false;
				}
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		return false;
	}

	public static void getApplicationData(Handler callBackHandler) {
		Client.sendRequest(RequestBuilder
				.buildRequest_GetApplication(ApplicationManager
						.getAppicationID()), new ResponseListenerGetIssues(
				callBackHandler));
	}

	// private static JSONObject makeRequest(Context context) {
	// HttpClient client = new DefaultHttpClient();
	// HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //
	// Timeout
	// // Limit
	// HttpResponse response;
	// try {
	// HttpPost post = new HttpPost(
	// "http://padcms.adyax.com/api/v1/jsonrpc.php"); // Consts.getUUID(context)
	// StringEntity se = new StringEntity(
	// String.format(
	// "{\"method\":\"client.getIssues\",\"params\":{\"clientId\":\"%d\",\"udid\":\"%s\"},\"id\":%d}",
	// Constants.CLIENT_ID,
	// "63b83b48eb90d49070e87bfce2762a649947e0ce", 3));
	// // StringEntity se = new
	// //
	// StringEntity(String.format("{\"method\":\"client.getIssues\",\"params\":{\"clientId\":\"%d\"},\"id\":%d}",
	// // Constants.CLIENT_ID, 3));
	// se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
	// "application/json"));
	// post.setEntity(se);
	// response = client.execute(post);
	// if (response != null) {
	// Scanner in = new Scanner(response.getEntity().getContent());
	// StringBuilder sb = new StringBuilder();
	// while (in.hasNext()) {
	// sb.append(in.next());
	// }
	//
	// return new JSONObject(sb.toString());
	// }
	// return null;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// }
}
