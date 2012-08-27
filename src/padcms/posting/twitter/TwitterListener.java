package padcms.posting.twitter;

import padcms.posting.SocialSomewhat;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

public class TwitterListener implements OnClickListener {
	private Context context;
	private String tweeterMessage;

	public TwitterListener(Context context, String tweeterMessage) {

		this.tweeterMessage = tweeterMessage;
		dialogProgress = new ProgressDialog(context);
		dialogProgress.setMessage("Checking internet connection");

		this.context = context;
	}

	ProgressDialog dialogProgress;

	@Override
	public void onClick(View v) {
		// PageController.ShowAnimationOnClick(v);
		dialogProgress.show();
		SocialSomewhat.isOnline(context, isConnectionAvalible);
	}

	Handler isConnectionAvalible = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			dialogProgress.hide();
			if (msg.arg1 == 0) {
				SocialSomewhat.toSorry(context);
			} else {
				new TwittDialog(context, tweeterMessage).show();
			}
			return false;
		}
	});
}
