package padcms.posting.email;

import padcms.posting.SocialSomewhat;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

public class EmailListener implements OnClickListener {
	private Context context;
	private String emailSubject, emailText;

	public EmailListener(Context context, String emailSubject, String emailText) {

		this.emailSubject = emailSubject;
		this.emailText = emailText;
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
				Intent emailIntent = new Intent(
						android.content.Intent.ACTION_SEND);

				emailIntent.setType("plain/text");
				// emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				// new String[] { "gmail@gmail.com" });
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
				emailIntent.putExtra(Intent.EXTRA_TEXT, emailText);

				context.startActivity(Intent.createChooser(emailIntent,
						"Choose Email Client"));

				// Intent i = new Intent();
				// i.setClassName("com.google.android.gm",
				// "com.google.android.gm.ComposeActivityGmail");
				// //i.putExtra(Intent.EXTRA_EMAIL, new
				// String[]{"arrancar4smart@gmail.com"});
				// // i.putExtra(Intent.EXTRA_SUBJECT,
				// ApplicationController.emailTitle);
				// // i.putExtra(Intent.EXTRA_TEXT,
				// ApplicationController.emailMessage);
				// context.startActivity(i);
			}
			return false;
		}
	});

}
