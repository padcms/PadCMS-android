package padcms.posting;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class SocialSomewhat{
	private static boolean twittDialogWasShown = false;
	private static String twittText = "";
	private static SharedPreferences prefs;

	public static void isOnline(final Context context,
			final Handler handlerInternetConnetion) {
		new Thread(new Runnable() {

			@Override
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

	public static boolean isOnline(Context context) {
		NetworkInfo ni = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if (ni == null)
			return false;
		return ni.isConnectedOrConnecting();
	}

	

	public static boolean twittDialogWasShown() {
		if (twittDialogWasShown == true) {
			twittDialogWasShown = false;
			return true;
		}
		return false;
	}

	public static void setTwittText(String s) {
		twittText = s;
	}

//	public static void sendTwitt(final Context context) {
//		if (TwitterUtils.isAuthenticated(prefs)) {
//			Thread t = new Thread() {
//				@Override
//				public void run() {
//
//					try {
//						TwitterUtils.sendTweet(prefs, twittText);
//						Toast.makeText(context, "Tweet sent !",
//								Toast.LENGTH_SHORT).show();
//					} catch (Exception ex) {
//						Toast.makeText(context, "Tweet not sent !",
//								Toast.LENGTH_SHORT).show();
//						ex.printStackTrace();
//					}
//				}
//			};
//			t.start();
//		}
//	}

	public static void toSorry(Context context) {
		Toast.makeText(context, "No internet connection!", Toast.LENGTH_LONG)
				.show();
	}

	public static boolean isSpecificUrlAvalible(Context context, String sUrl) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm.getActiveNetworkInfo().isConnectedOrConnecting()) {
				URL url = new URL(sUrl);
				HttpURLConnection urlc = (HttpURLConnection) url
						.openConnection();
				urlc.setRequestProperty("User-Agent", "My Android Demo");
				urlc.setRequestProperty("Connection", "close");
				urlc.setConnectTimeout(1000); // mTimeout is in seconds

				urlc.connect();

				if (urlc.getResponseCode() == 200) {
					return true;
				} else {
					return false;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
