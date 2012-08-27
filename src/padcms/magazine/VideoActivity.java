package padcms.magazine;

import java.io.File;
import java.io.FileInputStream;

import padcms.application11.R;
import padcms.bll.ApplicationFileHelper;
import padcms.bll.youtube.QueryYouTubeTask;
import padcms.bll.youtube.StringsStore;
import padcms.bll.youtube.VideoDownloader;
import padcms.bll.youtube.VideoId;
import padcms.bll.youtube.YouTubeId;
import padcms.dao.application.bean.Revision;
import padcms.magazine.factory.IssueViewFactory;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * TODO: Provide for cases: * dowloading file was dowloaded not fully; * user
 * must be able to clean video_cash or one cashed video;
 */

public class VideoActivity extends Activity {
	private Uri __uri;
	private static boolean isLonger = false;

	private boolean closeOnFinish;
	// private int fileSize = 0;
	// private int downloadingProgress = 0;
	// private long dwnldProgrOnPause = 0;

	// private String pathToCashFolder = android.os.Environment
	// .getExternalStorageDirectory().getAbsolutePath()
	// + File.separator
	// + "video_cash" + File.separator;
	private String pathToFile;
	private String embedUrlStr;
	private File tempFile;
	// private int videoDuration = 0;

	private Thread menuVisibilityThread = null;
	private Thread seekUpdaterThread = null;
	public VideoDownloader downloadThread;

	private View topMenu;
	private View bottomMenu;
	private SeekBar seekBar;
	private VideoView videoView;
	private TextView timeTextView;

	protected QueryYouTubeTask mQueryYouTubeTask;
	private int persentsDownloadToStartPlay = 50;
	private boolean streamMode;
	private Handler handler = new Handler();
	// protected String mVideoId = null;
	private final int DIALOG_PREPARING = 1101;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_activity_layout);
		Bundle budleIn = getIntent().getExtras();
		if (budleIn != null) {
			if (budleIn.containsKey("closeOnFinish")) {
				closeOnFinish = budleIn.getBoolean("closeOnFinish");
				if (closeOnFinish)
					persentsDownloadToStartPlay = 1000;
			}
			if (budleIn.containsKey("videoUrl")) {
				{
					this.embedUrlStr = budleIn.getString("videoUrl");
				}
			}
			if (budleIn.containsKey("streamMode")) {
				{
					this.streamMode = budleIn.getBoolean("streamMode");
					// streamMode=false;
				}
			}

		}
		// String url = getIntent().getExtras().getString("videoUrl");
		// this.embedUrlStr = "http://www.youtube.com/embed/n7hfg1Zd-_o";
		// "http://new.padcms.adyax.com//resize/thumbnail/none/element/00/00/01/52/resource.mp4";

		// "http://www.mp4point.com/downloads/55ecd9809707.mp4";
		// "http://www.youtube.com/embed/n7hfg1Zd-_o";//
		// "http://gsmnet.ru/3gp/kipelov_i_zdes.3gp";
		//
		// closeOnFinish = getIntent().getExtras().getBoolean("close");

		initializeContols();
		defineTopContols();
		defineBottomControls();

		// this.prepareCashFolder();
		showDialog(DIALOG_PREPARING);
		this.initializeCashFile();
		if (this.isOnline()) {
			this.makePreparationOfVideoSourceOnBaseURL(embedUrlStr);
		} else {
			if (!streamMode)
				this.playVideoInUi();
			else
				this.showErrorAlert(getString(R.string.error_message_title),
						getString(R.string.error_no_connection));
		}

		/*
		 * initializeContols(); defineTopContols(); defineBottomControls();
		 */

		/* startVideo(url); */

		/* playVideoInUi(); */

		// seekBarUpdaterStart();
		defineRootOnClickListener();

	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
		case DIALOG_PREPARING:
			dialog = new ProgressDialog(this);

			((ProgressDialog) dialog)
					.setMessage(getString(R.string.title_waiting));
			dialog.setOnCancelListener(new Dialog.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					finish();
				}
			});
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

	private void initializeCashFile() {
		Revision revisionBin = IssueViewFactory
				.getIssueViewFactoryIstance(this).getRevisionBin();
		// ApplicationFileHelper.getFileRevisionResourcesFolder(revisionBin
		// .getId().intValue(), String.valueOf(embedUrlStr.hashCode()));

		// this.pathToFile =
		// ApplicationFileHelper.getFileRevisionResourcesFolder(
		// revisionBin.getId().intValue()).getAbsolutePath()
		// + "/" + String.valueOf(embedUrlStr.hashCode());
		// this.pathToCashFolder + embedUrlStr.hashCode();
		this.tempFile = new File(
				ApplicationFileHelper.getFileRevisionResourcesFolder(revisionBin
						.getId().intValue()), String.valueOf(embedUrlStr
						.hashCode()));
		this.pathToFile = tempFile.getAbsolutePath();
		// if (this.tempFile.exists()) {
		// this.downloadingProgress = (int)this.tempFile.length();
		// }
	}

	// private void prepareCashFolder() {
	// File dir = new File(this.pathToCashFolder);
	// if (dir.exists() == false) {
	// dir.mkdirs();
	// }
	// }

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	/**
	 * If passed <code>url</code> is direct link to file, it looks if it is
	 * youtube link. And if it is invokes
	 * <code>prepareToYoutubeVideoShowing</code> method. In another case it
	 * invokes <code>webActivity</code> and finishes video activity.
	 * 
	 * @param url
	 *            is <code>String</code> contains URL.
	 */
	private void makePreparationOfVideoSourceOnBaseURL(String url) {
		if (!url.matches(StringsStore.URL_PATTERN)) {
			if (url.contains(StringsStore.YOUTUBE_COM)) {
				prepareToYoutubeVideoShowing(url);
			} else {
				// ApplicationController.RunWebActivity(this, url);
				Log.e("1 unsupported URL", "!!!");
				// finish();
			}
		} else {
			playVideoInUi();
		}
	}

	public void setYoutubeUri(android.net.Uri _uri) {
		this.embedUrlStr = _uri.getPath();
		Log.i("INSIDE setYoutubeUri", "this.url=" + this.embedUrlStr);
	}

	/**
	 * Looks for youtube parameters in URL and if finds it, calculats direct
	 * URL.
	 * 
	 * @param url
	 */
	private void prepareToYoutubeVideoShowing(String url) {
		int indexStart = url.indexOf("/embed/") + "/embed/".length();
		int indexEnd = url.contains("?") ? url.indexOf("?") : url.length();
		if (indexStart < indexEnd) {
			String lVideoIdStr = url.substring(indexStart, indexEnd);
			Log.i("Youtube id:=============>", lVideoIdStr);
			YouTubeId lYouTubeId = new VideoId(lVideoIdStr);
			// if (lYouTubeId == null) {
			// Log.i(this.getClass().getSimpleName(),
			// "Unable to extract video ID from the intent.  Closing video activity.");
			// finish();
			// }

			mQueryYouTubeTask = (QueryYouTubeTask) new QueryYouTubeTask(this)
					.execute(lYouTubeId);
		}
	}

	/**
	 * Starts new <code>Thread</code> on base of <code>Runnable</code> that
	 * updates <code>SeekBar</code> progress.
	 */
	// private void seekBarUpdaterStart() {
	//
	// seekUpdaterThread = new Thread(seekUpdater, "seekUpdaterThread");
	// seekUpdaterThread.start();
	// }

	/**
	 * Sets <code>url</code> to object of <code>VideoView</code> class (that is
	 * in fild of VideoActivity) and starts it.
	 * 
	 * @param uri
	 *            is string that contains URL
	 */
	private void startVideo(Uri uri) {
		Log.i("SetVideo url here:", "url:" + uri.toString());
		if (uri.toString().matches(StringsStore.URL_PATTERN)
				|| uri.toString().contains("file://")) {
			this.__uri = uri;
			videoView.setVideoURI(uri);
			videoView.requestFocus();
			videoView.start();
		} else if (uri.toString().matches(StringsStore.URL_PATTERN)) {
			videoView.setVideoURI(uri);
			videoView.requestFocus();
			videoView.start();
		}

	}

	/**
	 * Initializes <code>VideoActivity</code> filds of <code>View</code> type.
	 */
	private void initializeContols() {
		topMenu = findViewById(R.id.vaTopMenu);
		timeTextView = (TextView) findViewById(R.id.vaTime);
		bottomMenu = findViewById(R.id.vaBottomMenu);
		videoView = (VideoView) findViewById(R.id.vaVideo);
		/*
		 * InvocationHandler handler = new TraceHandler(videoViewNotProxy);
		 * videoView = (VideoView)Proxy.newProxyInstance(null,
		 * VideoView.class.getInterfaces(), handler);
		 */

		seekBar = (SeekBar) findViewById(R.id.vaSeek);
	}

	public void setDownloadProgress(final int percents) {
		handler.post(new Runnable() {

			public void run() {
				seekBar.setSecondaryProgress(percents);

				if (percents == persentsDownloadToStartPlay)
					playVideoInUi();
				else if (percents < persentsDownloadToStartPlay)
					isLonger = true;
			}
		});
	}

	private Runnable onEveryHalfSecond = new Runnable() {

		public void run() {

			if (seekBar != null) {
				int percents = (int) (1000 * (float) videoView
						.getCurrentPosition() / (float) videoView.getDuration());
				seekBar.setProgress(percents);
				setTimeText();
			}

			if (videoView.isPlaying()) {

				seekBar.postDelayed(onEveryHalfSecond, 500);
			}

		}

	};

	/**
	 * Defines <code>OnClickListener</code> objects for buttons in the bottom of
	 * layout, root element in video_activity_layout.xml, and defines size for
	 * them on base of display size.
	 */
	private void defineBottomControls() {
		int controlElementSize = calculateControlElementsSize();

		definePlayPauseButton(controlElementSize);
		defineFastBackButton(controlElementSize);
		defineFastForwardButton(controlElementSize);
		setPlayPauseAndVideoCompletionListener(controlElementSize);
	}

	/**
	 * Defines button "OK" <code>OnClickListener</code> and SeekBar
	 * <code>OnSeekBarChangeListener</code>
	 */
	private void defineTopContols() {
		defineOkButton();
		defineSeekBar();
	}

	private void defineSeekBar() {
		seekBar.setMax(1000);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
				isLonger = true;

			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				isLonger = true;
			}

			// TODO: forbid to user to seek in part in which no dowloading`````
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					int positionVideo = (int) (progress / 1000.f * videoView
							.getDuration());

					videoView.seekTo(positionVideo);
					isLonger = true;
				}
			}
		});

	}

	/**
	 * Sets <code>OnClickListener</code> for play/pause button, its size. and
	 * <code>OnCompletionListener</code> for <code>VideoView</code>
	 * 
	 * @param controlElementSize
	 */
	private void setPlayPauseAndVideoCompletionListener(int controlElementSize) {

		videoView.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				int duractionVide = mp.getDuration();
				int currentposition = mp.getCurrentPosition();
				if (currentposition < duractionVide) {
					int extreemCurrentDuraction = mp.getCurrentPosition();
					FileInputStream fis;
					try {
						fis = new FileInputStream(pathToFile);
						mp.reset();
						mp.setDataSource(fis.getFD());
						mp.prepare();

						mp.seekTo(extreemCurrentDuraction);
						mp.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					if (closeOnFinish) {
						videoView.seekTo(mp.getDuration() - 1);
						videoView.pause();
						onBackPressed();
					} else {
						returnToBeginningOfPlayback();

					}
				}
			}

			private void returnToBeginningOfPlayback() {
				((ImageView) findViewById(R.id.vaPlayPause))
						.setImageResource(R.drawable.arrow);
				videoView.seekTo(0);
				videoView.setVisibility(View.INVISIBLE);
			}
		});
		videoView.setOnPreparedListener(new OnPreparedListener() {

			public void onPrepared(MediaPlayer arg0) {
				seekBar.postDelayed(onEveryHalfSecond, 200);

				// if (extreemCurrentDuraction > 0) {
				// arg0.seekTo(extreemCurrentDuraction);
				// }

			}
		});
		videoView.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {

				// startVideo(Uri.parse(embedUrlStr));
				// mp.pause();
				// if (what == 1 && extra == -17)
				// return true;
				finish();
				dismissDialog(DIALOG_PREPARING);
				return false;
			}
		});
	}

	public void setTimeText() {

		if (videoView.getDuration() > 1) {
			dismissDialog(DIALOG_PREPARING);

			int remain = videoView.getDuration()
					- videoView.getCurrentPosition();
			int min = remain / 60000;
			int sec = (remain - (min * 60000)) / 1000;
			String sMin = (min < 10 ? "0" : "") + min;
			String sSec = (sec < 10 ? "0" : "") + sec;
			timeTextView.setText("-" + sMin + ":" + sSec);
		}

	}

	private void checkDownloadingFile() {
		this.seekBar.setSecondaryProgress((int) tempFile.length() / 1000);
		// Log.e("lengthFile" + fileSize, "" + tempFile.length());
		long estimatedFullFileSize = 0L;
		if (this.downloadThread != null && this.downloadThread.isAlive()) {
			estimatedFullFileSize = this.downloadThread.getContentLength();
		}
		if (estimatedFullFileSize > tempFile.length()
				&& estimatedFullFileSize > 0) {
			Runnable notification = new Runnable() {
				public void run() {
					checkDownloadingFile();
				}
			};
			handler.postDelayed(notification, 500);
		} else {
			playVideoInUi();
		}
	}

	/**
	 * Sets <code>OnClickListener</code> for "OK" button
	 */
	private void defineOkButton() {
		Button ok = (Button) findViewById(R.id.vaOK);
		int controlElementSize = calculateControlOkElementsSize();
		ok.getLayoutParams().height = controlElementSize;
		ok.getLayoutParams().width = controlElementSize;
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	// public VideoView getVideoView() {
	// return this.videoView;
	// }

	private View defineRootOnClickListener() {
		View root = findViewById(R.id.vaRoot);
		root.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (topMenu.getVisibility() == View.GONE) {
					setVisibilityToMenu(View.VISIBLE);
					if (menuVisibilityThread == null) {
						menuVisibilityThread = new Thread(makeMenuInvisible,
								"makeMenuInvisible");
					}
					menuVisibilityThread.start();
				} else {
					if (menuVisibilityThread != null) {
						menuVisibilityThread.interrupt();
					}
					setVisibilityToMenu(View.GONE);
				}
			}
		});
		return root;
	}

	private void defineFastForwardButton(int controlElementSize) {
		ImageButton fastForward = (ImageButton) findViewById(R.id.vaFastForward);
		fastForward.getLayoutParams().height = controlElementSize;
		fastForward.getLayoutParams().width = controlElementSize;

		fastForward.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				isLonger = true;
				int duration = videoView.getDuration();
				if (duration - videoView.getCurrentPosition() > 5000)
					videoView.seekTo(videoView.getCurrentPosition() + 5000);
				else {
					videoView.seekTo(duration);
					if (closeOnFinish) {
						finish();
					}
				}

				// runOnUiThread(updateAction);
			}
		});
	}

	private ImageButton definePlayPauseButton(int controlElementSize) {
		final ImageButton playPause = (ImageButton) findViewById(R.id.vaPlayPause);
		playPause.getLayoutParams().height = controlElementSize;
		playPause.getLayoutParams().width = controlElementSize;

		playPause.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				isLonger = true;
				if (videoView.isPlaying()) {
					videoView.pause();
					playPause.setImageResource(R.drawable.arrow);
					// seekUpdaterThread.interrupt();
				} else {
					if (videoView.getVisibility() == View.INVISIBLE) {
						videoView.setVisibility(View.VISIBLE);
					}

					videoView.start();
					playPause.setImageResource(R.drawable.pause);
					// seekUpdaterThread = new Thread(seekUpdater,
					// "seekUpdater");
					// seekUpdaterThread.start();
				}
			}
		});
		return playPause;
	}

	private void defineFastBackButton(int controlElementSize) {
		ImageButton fastBack = (ImageButton) findViewById(R.id.vaFastBack);
		fastBack.getLayoutParams().height = controlElementSize;
		fastBack.getLayoutParams().width = controlElementSize;

		fastBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				isLonger = true;
				videoView.seekTo(videoView.getCurrentPosition() > 5000 ? videoView
						.getCurrentPosition() - 5000 : 0);
			}
		});
	}

	private int calculateControlElementsSize() {
		Display display = getWindowManager().getDefaultDisplay();
		int controlElementSize = display.getHeight() > display.getWidth() ? display
				.getWidth() : display.getHeight();
		controlElementSize /= 8;
		return controlElementSize;
	}

	private int calculateControlOkElementsSize() {
		Display display = getWindowManager().getDefaultDisplay();
		int controlElementSize = display.getHeight() > display.getWidth() ? display
				.getWidth() : display.getHeight();
		controlElementSize /= 12;
		return controlElementSize;
	}

	private void setVisibilityToMenu(int visibility) {
		topMenu.setVisibility(visibility);
		bottomMenu.setVisibility(visibility);
	}

	private Runnable makeMenuInvisible = new Runnable() {
		public void run() {
			try {
				do {
					isLonger = false;
					Thread.sleep(4000);
				} while (isLonger);
				Thread.sleep(1000);
				runOnUiThread(action);
			} catch (InterruptedException e) {
				menuVisibilityThread = null;
			}
		}

		private Runnable action = new Runnable() {
			public void run() {
				menuVisibilityThread = null;
				setVisibilityToMenu(View.GONE);
			}
		};
	};

	/*
	 * ##########################################################################
	 * ########################################
	 */
	// XXX: in background dowloading methods
	/**
	 * playVideoInUi - invokes first!
	 */
	public void playVideoInUi(Uri uri) {
		this.setYoutubeUri(uri);
		Log.i("INSIDE PLAY VIDEO IN UI with uri parameter",
				"INSIDE PLAY VIDEO IN UI with uri parameter");

		runOnUiThread(new Runnable() {
			public void run() {
				playVideoOrDownload();
			}
		});

	}

	public void playVideoInUi() {
		Log.i("INSIDE PLAY VIDEO IN UI", "INSIDE PLAY VIDEO IN UI");
		runOnUiThread(new Runnable() {
			public void run() {
				playVideoOrDownload();
			}
		});

	}

	/**
	 * 2nd
	 */
	private void playVideoOrDownload() {
		Log.d("INSIDE playVideoOrDownload", "INSIDE playVideoOrDownload");

		try {
			if (streamMode) {
				// seekBar.setSecondaryProgress(seekBar.getMax());
				this.startVideo(Uri.parse(embedUrlStr));

			} else {
				if (this.isOnline()
						&& (!tempFile.exists() || (tempFile.exists() && tempFile
								.length() == 0L))) {
					this.downloadThread = new VideoDownloader(this.embedUrlStr,
							this.pathToFile, this.tempFile, this);
					this.downloadThread.start();
					// if (!videoView.isPlaying())
					// startVideo(Uri.parse(embedUrlStr));
				} else if (!this.isOnline() && !tempFile.exists()) {
					this.showErrorAlert(
							getString(R.string.error_message_title),
							getString(R.string.error_no_connection));
				}

				if (tempFile.exists() && tempFile.length() > 0) {
					Log.d("Start VIDEO with PATH", this.pathToFile);
					seekBar.setSecondaryProgress(seekBar.getMax());
					this.startVideo(Uri.parse("file:/" + this.pathToFile));
				}
			}
		} catch (Exception e) {
			Log.e("TAG", "error: " + e.getMessage(), e);
			if (videoView != null) {
				videoView.stopPlayback();
			}
		}
	}

	/*
	 * ##########################################################################
	 * ###################################
	 */
	private void showErrorAlert(String title, String message) {
		dismissDialog(DIALOG_PREPARING);
		try {
			Builder lBuilder = new AlertDialog.Builder(this);
			lBuilder.setTitle(title);
			lBuilder.setCancelable(false);
			lBuilder.setMessage(message);

			lBuilder.setPositiveButton(R.string.dialog_ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface pDialog, int pWhich) {
							finish();
						}

					});

			AlertDialog lDialog = lBuilder.create();
			lDialog.show();
		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(),
					"Problem showing error dialog.", e);
		}
	}

	private void showDownloadAlert() {
		try {
			Builder lBuilder = new AlertDialog.Builder(this);
			lBuilder.setTitle(StringsStore.MSG_TITLE_PLAY_VIDEO_ON_CONCURRENT_DOWNLOAD);
			lBuilder.setCancelable(false);
			lBuilder.setMessage(StringsStore.MSG_PLAY_VIDEO_ON_CONCURRENT_DOWNLOAD);

			lBuilder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface pDialog, int pWhich) {
							// NOP
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
	protected void onDestroy() {

		if (downloadThread != null && downloadThread.isAlive()) {
			downloadThread.interrupt();

		}
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		if (downloadThread != null && downloadThread.isAlive()) {
			downloadThread.interrupt();

		}
		finish();
		overridePendingTransition(0, 0);
		// super.onBackPressed();

	}
}