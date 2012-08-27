package padcms.kiosk;

import padcms.application11.R;
import padcms.bll.ApplicationManager;
import padcms.bll.DialogHelper;
import padcms.bll.ImageLoader;
import padcms.dao.application.bean.Application;
import padcms.dao.application.bean.Revision;
import padcms.dao.factory.DataStorageFactory;
import padcms.kiosk.RevisionStateManager.RevisionInstallState;
import padcms.kiosk.view.adapter.GalleryAdapter;
import padcms.kiosk.view.adapter.GalleryAdapter.Image;
import padcms.kiosk.view.adapter.TableViewAdapter;
import padcms.kiosk.view.gallery.CoverControlsComponent;
import padcms.kiosk.view.gallery.CoversGalleryComponent;
import padcms.kiosk.view.grid.TableViewController;
import padcms.magazine.factory.IssueViewFactory;
import padcms.net.NetHepler;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * Main Activity which gives to user gallery and grid views of magazines
 * available for download and downloaded earlier.
 * 
 */
public class KioskActivity extends Activity {
	/** Called when the activity is first created. */

	private Gallery theGallery;
	// private ImageButton topGalleryButton;
	private ImageButton topGridGalleryButton;
	// private ImageButton topGridButton;
	// private ImageButton topTriangleButton;
	private RelativeLayout theGalleryLayout;
	private CoverControlsComponent coverControlsComponent;
	private TableViewController theGridViewController;
	// private ProgressDialog progressDialog;
	private DataStorageFactory dataStrorageFactory;
	private SharedPreferences prefs;
	private ScrollView scrollingViewTable;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kiosk_layout);
		getLayoutControls();
		ApplicationManager.initApplication(this);

		// FileStateManager fileStateManager = new FileStateManager(this.prefs);
		// this.coverControlsComponent.setFileStateManager(fileStateManager);
		// this.theGridViewController.setFileStateManager(fileStateManager);

		initLayoutControls(getWindowManager().getDefaultDisplay());
		initButtonListners();

	}

	@Override
	protected void onStart() {
		super.onStart();
		getUpdateForIssues();
		initLayoutControls(getWindowManager().getDefaultDisplay());

	}

	private void getUpdateForIssues() {

		if (dataStrorageFactory == null)
			dataStrorageFactory = new DataStorageFactory(this);

		if (NetHepler.isOnline(this)) {
			NetHepler.getApplicationData(new Handler() {

				@Override
				public void handleMessage(Message msg) {
					if (msg.what < 0) {
						DialogHelper.ShowErrorWindow(KioskActivity.this,
								msg.obj.toString());
						if (dataStrorageFactory.getApplication() != null) {
							setContentView(dataStrorageFactory.getApplication());
						}
					} else {
						// msg.obj.toString();

						final Application application = (Application) msg.obj;
						setContentView(application);
						// checkInstalledMagazin(KioskActivity.this,
						// application);

						new Thread() {
							public void run() {
								dataStrorageFactory
										.saveChangesInDatabse(application);
							}
						}.start();
					}
				}
			});
		} else {
			if (dataStrorageFactory.getApplication() == null)
				DialogHelper.ShowMessageWindow(this, "",
						getString(R.string.error_no_connection));
			else
				setContentView(dataStrorageFactory.getApplication());
		}
	}

	/**
	 * Specifies the default sources of content for the elements of the
	 * graphical user interface, the listeners for the buttons of view reseting.
	 * 
	 * @param application
	 */
	public void setContentView(Application application) {
		if (application != null && application.getIssueList().size() > 0) {
			initGallaryViewData(application);
			initGridViewData(application);
		}

	}

	public void ShowProgressDialogForGetIssues() {

	}

	/**
	 * Defines number of colums in grid view and demensions of root element of
	 * controls in gallery view when display orientation changes.
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		defineNumColumnsForGridView();
		initLayoutControls(getWindowManager().getDefaultDisplay());
	}

	private void getLayoutControls() {
		coverControlsComponent = (CoverControlsComponent) findViewById(R.id.footerLayout);
		coverControlsComponent.setVisibility(View.INVISIBLE);
		// topGalleryButton = (ImageButton) findViewById(R.id.toGalleryButton);
		// topGridButton = (ImageButton) findViewById(R.id.toGridButton);
		topGridGalleryButton = (ImageButton) findViewById(R.id.GalleryGridButton);
		// topTriangleButton = (ImageButton) findViewById(R.id.triangleButton);
		theGallery = (Gallery) findViewById(R.id.theGallery);
		theGridViewController = (TableViewController) findViewById(R.id.theGridView);
		theGalleryLayout = (RelativeLayout) findViewById(R.id.theGalleryLayout);
		scrollingViewTable = (ScrollView) findViewById(R.id.scrollingViewTable);
	}

	private void initLayoutControls(Display display) {
		if (coverControlsComponent.getGallaryView() != null
				&& coverControlsComponent.getGallaryView().getAdapter() != null) {
			((GalleryAdapter) coverControlsComponent.getGallaryView()
					.getAdapter()).dismisZoomForCover();
			coverControlsComponent.refresh();
			((CoversGalleryComponent) theGallery).requestLayout();
		}

		coverControlsComponent.getLayoutParams().width = 2 * display.getWidth() / 3;

		Image image = GalleryAdapter.getImage(display);
		if (((GalleryAdapter) theGallery.getAdapter()) != null)
			((GalleryAdapter) theGallery.getAdapter())
					.initDisplayForItems(display);

		((RelativeLayout.LayoutParams) coverControlsComponent.getLayoutParams()).bottomMargin = (int) (display
				.getHeight() / 2 - image.getHeight() * 5 / 4.f);
	}

	private void initButtonListners() {
		// topGalleryButton.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// showGallary();
		// }
		//
		//
		// });
		topGridGalleryButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (scrollingViewTable.getVisibility() == View.GONE) {
					showGrid();
					topGridGalleryButton
							.setBackgroundResource(R.drawable.gallery_button_selector);
				} else {
					showGallary();
					topGridGalleryButton
							.setBackgroundResource(R.drawable.grid_button_selector);
				}
			}

		});

		// topGridButton.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// Log.d("TOPGRIDBUTTON PRESSED", "TOPGRIDBUTTON PRESSED");
		// showGrid();
		// }
		//
		// });
	}

	private void showGrid() {
		if (theGridViewController != null) {
			if (scrollingViewTable.getVisibility() == View.GONE) {
				scrollingViewTable.setVisibility(View.VISIBLE);
				theGalleryLayout.setVisibility(View.GONE);
				// Downloader downloaderThread = coverControlsComponent
				// .getDownloaderThread();
				// Extractor extractorThread = coverControlsComponent
				// .getExtractorThread();
				// if (downloaderThread != null
				// && downloaderThread.isAlive()
				// && !downloaderThread.isInterrupted()) {
				// downloaderThread
				// .setParentView(theGridViewController);
				// theGridViewController
				// .setProcessingIssueOnChangeViewMode(coverControlsComponent
				// .getProcessingRevision());
				// theGridViewController
				// .setDownloaderThread(downloaderThread);
				// coverControlsComponent
				// .setProcessingIssueOnChangeViewMode(null);
				// coverControlsComponent.setDownloaderThread(null);
				// } else if (extractorThread != null
				// && extractorThread.isAlive()
				// && !extractorThread.isInterrupted()) {
				// extractorThread
				// .setParentView(theGridViewController);
				// theGridViewController
				// .setProcessingIssueOnChangeViewMode(coverControlsComponent
				// .getProcessingRevision());
				// theGridViewController
				// .setExtractorThread(extractorThread);
				// coverControlsComponent
				// .setProcessingIssueOnChangeViewMode(null);
				// coverControlsComponent.setExtractorThread(null);
				// } else {
				//
				// }
				theGridViewController.setInstallThread(coverControlsComponent
						.getInstallThread());
				if (theGridViewController.getTableViewAdapter() != null) {
					theGridViewController.getTableViewAdapter().updateItems();
				}

				if (coverControlsComponent.getProcessingRevision() != null) {
					theGridViewController
							.setProcessingRevision(coverControlsComponent
									.getProcessingRevision());
					theGridViewController.refresh();
				}
			}
		}
	}

	private void showGallary() {
		if (theGalleryLayout.getVisibility() == View.GONE) {
			scrollingViewTable.setVisibility(View.GONE);
			theGalleryLayout.setVisibility(View.VISIBLE);

			// Downloader downloaderThread = theGridViewController
			// .getDownloaderThread();
			// Extractor extractorTrhead = theGridViewController
			// .getExtractorThread();
			//
			// if (downloaderThread != null &&
			// downloaderThread.isAlive()
			// && !downloaderThread.isInterrupted()) {
			// downloaderThread.setParentView(coverControlsComponent);
			// coverControlsComponent
			// .setProcessingIssueOnChangeViewMode(theGridViewController
			// .getProcessingRevision());
			// coverControlsComponent
			// .setDownloaderThread(downloaderThread);
			// theGridViewController
			// .setProcessingIssueOnChangeViewMode(null);
			// theGridViewController.setDownloaderThread(null);
			// } else if (extractorTrhead != null
			// && extractorTrhead.isAlive()
			// && !extractorTrhead.isInterrupted()) {
			// extractorTrhead.setParentView(coverControlsComponent);
			// coverControlsComponent
			// .setProcessingIssueOnChangeViewMode(theGridViewController
			// .getProcessingRevision());
			// coverControlsComponent
			// .setExtractorThread(theGridViewController
			// .getExtractorThread());
			// theGridViewController
			// .setProcessingIssueOnChangeViewMode(null);
			// theGridViewController.setExtractorThread(null);
			// } else {
			//
			// }

			coverControlsComponent.setInstallThread(theGridViewController
					.getInstallThread());
			coverControlsComponent.refresh();
		}
	}

	private void initGallaryViewData(final Application application) {

		theGallery.setAdapter(new GalleryAdapter(this, application
				.getPublicshedRevisionsList()));
		coverControlsComponent.setGallaryView(theGallery);
		theGallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				Revision revision = (Revision) v.getTag();
				// application.getPublicshedRevisionsList().get(position);
				// if
				// (issue.equals(coverControlsComponent.getCurrentIssue()))
				// {
				// Toast.makeText(KioskActivity.this, "click", 200).show();
				// coverControlsComponent.getOpenButtonLisner().onClick(
				// coverControlsComponent.getFirstButton());
				// }
				coverControlsComponent.setCurrentRevision(revision);

			}

			public void onNothingSelected(AdapterView<?> parent) {
			}

		});
		theGallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				// Issue issue = application.getIssueList().get(position);
				Revision revision = (Revision) v.getTag();
				if (revision.equals(coverControlsComponent.getCurrenRevision())) {
					// Toast.makeText(KioskActivity.this, "click", 200).show();

					coverControlsComponent.getOpenButtonLisner().onClick(
							coverControlsComponent.getFirstButton());
					coverControlsComponent.getFirstButton().setClickable(false);
				}

			}

		});

		coverControlsComponent.setVisibility(View.VISIBLE);
	}

	private void initGridViewData(final Application application) {
		defineNumColumnsForGridView();

		theGridViewController.setTableViewAdapter(new TableViewAdapter(this,
				application.getPublicshedRevisionsList(),
				this.theGridViewController));
	}

	private void defineNumColumnsForGridView() {
		Display display = getWindowManager().getDefaultDisplay();
		int dispWidth = display.getWidth();
		int dispHeight = display.getHeight();

		Log.d("DISPLAY", "WIDTH:" + dispWidth + " HEIGHT:" + dispHeight);

		if (dispWidth < 600) {
			theGridViewController.setCoulumsCount(1);
		} else if (dispWidth >= 600 && dispWidth <= 800) {
			theGridViewController.setCoulumsCount(2);
		} else {
			theGridViewController.setCoulumsCount(3);
		}
		//
		// theGridViewController.updateDataAdapter();

	}

	@Override
	protected void onStop() {

		super.onStop();
		ImageLoader.getInstance(this).destroy();
	}

	// /????????????? NEED REFACTOR TEST
	public static void checkInstalledMagazin(Context context,
			Application application) {
		RevisionStateManager revStateManager = RevisionStateManager
				.getInstance(context);

		for (Revision revision : application.getPublicshedRevisionsList()) {
			RevisionInstallState revState = revStateManager.getState(revision
					.getId().intValue());
			if (revState == RevisionInstallState.INSTALLED) {
				startDownloadResource(context, revision);
				return;
			}
		}

	}

	private static void startDownloadResource(final Context context,
			Revision revision) {

		DataStorageFactory.getInstance(context).initRevisionPages(revision,
				new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);

						if (msg.what == 1) {

							new IssueViewFactory(context)
									.initPageViewCollection(context);
						}
					}
				});
	}

}