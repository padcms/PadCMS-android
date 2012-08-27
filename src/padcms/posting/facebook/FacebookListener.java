package padcms.posting.facebook;

import padcms.posting.SocialSomewhat;
import padcms.posting.com.facebook.android.DialogError;
import padcms.posting.com.facebook.android.Facebook;
import padcms.posting.com.facebook.android.Facebook.DialogListener;
import padcms.posting.com.facebook.android.FacebookError;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

public class FacebookListener implements OnClickListener {

	// Your Facebook Application ID must be set before running this example
	// See http://www.facebook.com/developers/createapp.php
	private static final String APP_ID = "159991224033644";

	private Facebook mFacebook;
	private Context context;
	private String faceBookText;

	public FacebookListener(Context context, String faceBookText) {

		this.faceBookText = faceBookText;
		dialogProgress = new ProgressDialog(context);
		dialogProgress.setMessage("Checking internet connection");

		this.context = context;
		mFacebook = new Facebook(APP_ID);
	}

	ProgressDialog dialogProgress;

	@Override
	public void onClick(View v) {
		// PageController.ShowAnimationOnClick(v);
		dialogProgress.show();
		SocialSomewhat.isOnline(context, isConnectionAvalible);
	}

	Handler handelConnectDialog = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			dialogProgress.hide();
			if (msg.obj != null) {
				Bundle parameters = (Bundle) msg.obj;
				mFacebook.dialog(context, "feed", parameters,
						new DialogListener() {
							@Override
							public void onFacebookError(FacebookError e) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onError(DialogError e) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onComplete(Bundle values) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onCancel() {
								// TODO Auto-generated method stub

							}
						});
				try {
					mFacebook.logout(context);
				} catch (Exception e) {

				}
			}
			return false;
		}
	});
	Handler isConnectionAvalible = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub

			if (msg.arg1 == 0) {
				dialogProgress.hide();
				SocialSomewhat.toSorry(context);
			} else {
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Bundle parameters = new Bundle();
						// parameters.putString("link",
						// "http://facebook.com/sharer.php?u="
						// + faceBookText);

						// if (!SocialSomewhat.isSpecificUrlAvalible(context,
						// faceBookText)) {
						// parameters//.putString("name", "");
						// .putString("name",
						// "http://facebook.com/sharer.php?u="
						// + faceBookText);
//						parameters.putString("description",
//								"http://facebook.com/sharer.php?u="
//										+ faceBookText);

						parameters.putString("link",
								"http://facebook.com/sharer.php?u="
										+ faceBookText);

						if (!SocialSomewhat.isSpecificUrlAvalible(context,
								"http://facebook.com/sharer.php?u="
										+ faceBookText)) {
							parameters.putString("name",
									"http://facebook.com/sharer.php?u="
											+ faceBookText);
							parameters.putString("description", " ");
						}
						// }
						Message msg = new Message();
						msg.obj = parameters;
						handelConnectDialog.sendMessage(msg);
					}
				}).start();

			}
			return false;
		}
	});

}