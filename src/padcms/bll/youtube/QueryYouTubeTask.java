package padcms.bll.youtube;

import padcms.magazine.VideoActivity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

public class QueryYouTubeTask extends AsyncTask<YouTubeId, String, Uri> {

	private boolean mShowedError = false;
	private VideoActivity videoActivity;

	public QueryYouTubeTask(VideoActivity va) {
		this.videoActivity = va;
	}

	@Override
	protected Uri doInBackground(YouTubeId... pParams) {
		String lUriStr = null;
		String lYouTubeFmtQuality = "17"; // 3gpp medium quality, which
											// should be fast enough to view
											// over EDGE connection
		String lYouTubeVideoId = null;

		if (isCancelled())
			return null;

		try {
			publishProgress(StringsStore.mMsgDetect);

			WifiManager lWifiManager = (WifiManager) this.videoActivity
					.getSystemService(Context.WIFI_SERVICE);
			TelephonyManager lTelephonyManager = (TelephonyManager) this.videoActivity
					.getSystemService(Context.TELEPHONY_SERVICE);

			// //////////////////////////
			// if we have a fast connection (wifi or 3g), then we'll get a
			// high quality YouTube video
			if ((lWifiManager.isWifiEnabled()
					&& lWifiManager.getConnectionInfo() != null && lWifiManager
					.getConnectionInfo().getIpAddress() != 0)
					|| ((lTelephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS
							||

							/*
							 * icky... using literals to make backwards
							 * compatible with 1.5 and 1.6
							 */
							lTelephonyManager.getNetworkType() == 9 /* HSUPA */
							|| lTelephonyManager.getNetworkType() == 10 /* HSPA */
							|| lTelephonyManager.getNetworkType() == 8 /* HSDPA */
							|| lTelephonyManager.getNetworkType() == 5 /* EVDO_0 */
					|| lTelephonyManager.getNetworkType() == 6 /* EVDO A */)

					&& lTelephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED)) {
				lYouTubeFmtQuality = "18";
			}

			// /////////////////////////////////
			// if the intent is to show a playlist, get the latest video id
			// from the playlist, otherwise the video
			// id was explicitly declared.
			if (pParams[0] instanceof PlaylistId) {
				publishProgress(StringsStore.mMsgPlaylist);
				lYouTubeVideoId = YouTubeUtility
						.queryLatestPlaylistVideo((PlaylistId) pParams[0]);
			} else if (pParams[0] instanceof VideoId) {
				lYouTubeVideoId = pParams[0].getId();
			}

			// mVideoId = lYouTubeVideoId;
			// this.videoActivity.setMVideoId(lYouTubeVideoId);

			publishProgress(StringsStore.mMsgToken);

			if (isCancelled())
				return null;

			// //////////////////////////////////
			// calculate the actual URL of the video, encoded with proper
			// YouTube token
			lUriStr = YouTubeUtility.calculateYouTubeUrl(lYouTubeFmtQuality,
					true, lYouTubeVideoId);

			if (isCancelled())
				return null;

			if (lYouTubeFmtQuality.equals("17")) {
				publishProgress(StringsStore.mMsgLowBand);
			} else {
				publishProgress(StringsStore.mMsgHiBand);
			}

		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(),
					"Error occurred while retrieving information from YouTube.",
					e);
		}

		if (lUriStr != null) {
			Uri _uri = Uri.parse(lUriStr);
			/* videoActivity.setYoutubeUri(_uri); */// XXX setting Youtube Uri
													// in background in
													// QueryYouTubeTask
			return _uri;
		} else {
			return null;
		}
	}

	@Override
	protected void onPostExecute(Uri pResult) {
		super.onPostExecute(pResult);
		Log.i("&&&&&&&&&&&&&&&&& onPostExecute &&&&&&&&&&&&&&&&&&&", "pResult"
				+ pResult);
		try {
			if (isCancelled())
				return;

			if (pResult == null) {
				throw new RuntimeException("Invalid NULL Url.");
			}

			if (isCancelled())
				return;

			// TODO: add listeners for finish of video
			// video.setOnCompletionListener(new OnCompletionListener() {
			//
			// public void onCompletion(MediaPlayer pMp) {
			// if (isCancelled())
			// return;
			// VideoActivity.this.finish();
			// }
			//
			// });

			if (isCancelled())
				return;

			// final MediaController lMediaController = new MediaController(
			// VideoActivity.this);
			// video.setMediaController(lMediaController);
			// lMediaController.show(0);
			// mVideoView.setKeepScreenOn(true);
			// video
			// .setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			//
			// public void onPrepared(MediaPlayer pMp) {
			// if (isCancelled())
			// return;
			// // VideoActivity.this.mProgressBar
			// // .setVisibility(View.GONE);
			// // VideoActivity.this.mProgressMessage
			// // .setVisibility(View.GONE);
			// }
			//
			// });

			if (isCancelled())
				return;

			/*
			 * VideoView video = this.videoActivity.getVideoView();
			 * video.setVideoURI(pResult); //
			 * this.videoActivity.getVideoView().setVideoURI(pResult);
			 * video.requestFocus(); video.start();
			 */
			this.videoActivity.playVideoInUi(pResult);

		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), "Error playing video!", e);

			if (!mShowedError) {
				showErrorAlert();
			}
		}
	}

	private void showErrorAlert() {
		try {
			Builder lBuilder = new AlertDialog.Builder(this.videoActivity);
			lBuilder.setTitle(StringsStore.mMsgErrorTitle);
			lBuilder.setCancelable(false);
			lBuilder.setMessage(StringsStore.mMsgError);

			lBuilder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface pDialog, int pWhich) {
							videoActivity.finish();
						}

					});

			AlertDialog lDialog = lBuilder.create();
			lDialog.show();
		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(),
					"Problem showing error dialog.", e);
		}
	}

	@Override
	protected void onProgressUpdate(String... pValues) {
		super.onProgressUpdate(pValues);
		// VideoActivity.this.updateProgress(pValues[0].mMsg);
	}

}
