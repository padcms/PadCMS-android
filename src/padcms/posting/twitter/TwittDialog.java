package padcms.posting.twitter;

import padcms.application11.R;
import padcms.posting.SocialSomewhat;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.socialsharing.common.AuthListener;
import com.nostra13.socialsharing.common.LogoutListener;
import com.nostra13.socialsharing.common.PostListener;
import com.nostra13.socialsharing.twitter.TwitterEvents;
import com.nostra13.socialsharing.twitter.TwitterFacade;
import com.nostra13.socialsharing.twitter.extpack.oauth.signpost.OAuth;

public class TwittDialog extends Dialog implements OnShowListener {

	private EditText textArea;
	private TextView counter;
	private String tweeterMessage;
	private TwitterFacade twitter;
	private Context mContext;

	public TwittDialog(Context context, String tweeterMessage) {
		super(context);
		mContext = context;
		this.tweeterMessage = tweeterMessage;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.twittdialog);
		clearCredentials();

		View view = findViewById(R.id.twittDialogLayout);
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		int[] size;

		if (display.getWidth() < 310) {
			size = new int[] { 200, 270 };
		} else if (display.getWidth() < 470) {
			size = new int[] { 280, 420 };
		} else {
			size = new int[] { 400, 700 };
		}
		if (display.getWidth() > display.getHeight()) {
			if (display.getWidth() < 320) {
				size = new int[] { 200, 230 };
			} else if (display.getWidth() < 480) {
				size = new int[] { 280, 320 };
			} else {
				size = new int[] { 400, 500 };
			}
		}
		Log.i("The log", "" + size[0] + "    " + size[1] + display.getWidth()
				+ "  " + display.getHeight());
		view.setLayoutParams(new FrameLayout.LayoutParams(size[0], size[1]));

		counter = (TextView) findViewById(R.id.counter);
		final ImageButton twitt = (ImageButton) findViewById(R.id.btn_tweet);
		textArea = (EditText) findViewById(R.id.textArea);
		textArea.setText(tweeterMessage);
		textArea.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				countSymbols();
			}
		});
		countSymbols();
		twitt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SocialSomewhat.isOnline(getContext())) {
					if (twitter.isAuthorized()) {
						twitter.publishMessage(getTweetMsg());

					} else {
						// Start authentication dialog and publish message after
						// successful authentication
						twitter.authorize(new AuthListener() {
							@Override
							public void onAuthSucceed() {
								twitter.publishMessage(getTweetMsg());
							}

							@Override
							public void onAuthFail(String error) { // Do nothing
							}
						});
					}
					dismiss();
				} else {
					SocialSomewhat.toSorry(getContext());
				}
			}
		});// Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET

		twitter = new TwitterFacade(getContext(), Constants.CONSUMER_KEY,
				Constants.CONSUMER_SECRET);

	}

	private void countSymbols() {
		counter.setText((140 - getTweetMsg().length())
				+ (140 - getTweetMsg().length() == 1 ? " symbol " : " symbols ")
				+ "left");
	}

	private String getTweetMsg() {
		return textArea.getText().toString();
	}

	private void clearCredentials() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getContext());
		final Editor edit = prefs.edit();
		edit.remove(OAuth.OAUTH_TOKEN);
		edit.remove(OAuth.OAUTH_TOKEN_SECRET);
		edit.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	private AuthListener authListener = new AuthListener() {
		@Override
		public void onAuthSucceed() {
			showToastOnUIThread("twitter auth success");
		}

		@Override
		public void onAuthFail(String error) {
			showToastOnUIThread("twitter auth fail");
		}
	};

	private PostListener postListener = new PostListener() {
		@Override
		public void onPostPublishingFailed() {
			showToastOnUIThread("twitter post publishing failed");
		}

		@Override
		public void onPostPublished() {
			showToastOnUIThread("twitter post published");
		}
	};

	private LogoutListener logoutListener = new LogoutListener() {
		@Override
		public void onLogoutComplete() {
			showToastOnUIThread("twitter logged out");
		}
	};

	private void showToastOnUIThread(final String text) {

		((Activity) mContext).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();
		TwitterEvents.removeAuthListener(authListener);
		TwitterEvents.removePostListener(postListener);
		TwitterEvents.removeLogoutListener(logoutListener);
	}

	@Override
	public void onShow(DialogInterface dialog) {
		// TODO Auto-generated method stub
		TwitterEvents.addAuthListener(authListener);
		TwitterEvents.addPostListener(postListener);
		TwitterEvents.addLogoutListener(logoutListener);
	}

}