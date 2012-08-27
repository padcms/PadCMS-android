package padcms.bll;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;

public class DialogHelper {

	public static void ShowErrorWindow(Context mContext, String errorMessage) {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
		alertBuilder.setMessage(errorMessage).setTitle("Error");
		alertBuilder.setNegativeButton("Close", null);
		alertBuilder.show();
	}

	public static void ShowErrorWindow(Context mContext, String errorMessage,
			OnClickListener onClicklistner) {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
		alertBuilder.setMessage(errorMessage).setTitle("Error");
		alertBuilder.setNegativeButton("Close",
				(OnClickListener) onClicklistner);

		alertBuilder.show();
	}

	public static void ShowMessageWindow(Context mContext, String title,
			String message) {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
		alertBuilder.setMessage(message).setTitle(title);
		alertBuilder.setNegativeButton("Close", null);
		alertBuilder.show();
	}

	public static void ShowClosedApplication(final Context mContext) {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
		alertBuilder
				.setMessage(
						"This Appliction was closed by Owner please contact with him for get more information <email>")
				.setTitle("Applicatio permision");

		alertBuilder.setNegativeButton("Close", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				((Activity) mContext).finish();
				dialog.dismiss();
			}
		});

		alertBuilder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {

				((Activity) mContext).finish();
				dialog.dismiss();
			}
		});
		alertBuilder.show();
	}

	public static void makeAsynckMessageShow(final Context mContext,
			final String message) {
		((Activity) mContext).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				DialogHelper.ShowErrorWindow(mContext, message);
				// Toast.makeText(
				// mContext,
				// mContext.getString(R.string.download_impossible_no_space),
				// 1000).show();
			}
		});
	}

	public static void makeAsynckMessageShow(Context mContext, int message) {
		makeAsynckMessageShow(mContext, mContext.getString(message));
	}

	public static void showMessageNoSDCard(Context mContext) {
		Builder alertBuilderSD = new AlertDialog.Builder(mContext)
				.setMessage(
						"The SD Card was removed from the device.\nPlease insert the SD Card and restart the app.")
				.setTitle("SD Card Error");
		alertBuilderSD.setCancelable(false);
		alertBuilderSD.setPositiveButton("Force restart",

		new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
				System.exit(0);
			}
		});
		alertBuilderSD.show();

	}
}
