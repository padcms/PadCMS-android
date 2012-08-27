package padcms.magazine;

import java.util.ArrayList;

import padcms.application11.R;
import padcms.bll.ApplicationSkinFactory;
import padcms.magazine.controls.switcher.SmoothSwitcherController;
import padcms.magazine.factory.IssueViewFactory;
import padcms.magazine.page.BasePageView;
import padcms.magazine.page.elementview.GalaryElementView;
import padcms.magazine.page.elementview.ProgressDownloadElementView;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class PhotoGalleryActivity extends Activity {

	private ArrayList<GalaryElementView> galaryViewColletion;
	private IssueViewFactory issueViewFactory;
	SmoothSwitcherController smoothSwitcherController;
	private BasePageView pageViewOfGalary;
	private int gallaryID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_layout);
		int pageID = -1;
		Bundle bundelExtras = getIntent().getExtras();
		int currentPosition = 0;
		if (bundelExtras != null) {
			pageID = bundelExtras.getInt("pageID");
			if (bundelExtras.containsKey("gallaryID"))
				gallaryID = bundelExtras.getInt("gallaryID");
			if (bundelExtras.containsKey("currentPosition"))
				currentPosition = bundelExtras.getInt("currentPosition");

			issueViewFactory = IssueViewFactory
					.getIssueViewFactoryIstance(this);

			pageViewOfGalary = issueViewFactory.findPageViewByPageID(pageID);

			if (gallaryID > 0) {
				galaryViewColletion = new ArrayList<GalaryElementView>();
				ArrayList<GalaryElementView> gallaryListFull = pageViewOfGalary
						.getGalaryElementViewCollection();
				for (GalaryElementView gallaryElementView : gallaryListFull) {
					if (gallaryElementView.getGallery_id() == gallaryID
							|| gallaryElementView.getGallery_id() == 0) {
						galaryViewColletion.add(gallaryElementView);
					}
				}
			} else {
				galaryViewColletion = pageViewOfGalary
						.getGalaryElementViewCollection();
			}

		}
		if (galaryViewColletion != null && galaryViewColletion.size() > 0) {

			RelativeLayout.LayoutParams layoutParamsProgress = new RelativeLayout.LayoutParams(
					-2, -2);

			layoutParamsProgress.addRule(RelativeLayout.CENTER_IN_PARENT);

			// int count = 0;
			for (GalaryElementView galaryElementView : galaryViewColletion) {

				if (galaryElementView.getProgressDownloadElementView() == null) {
					ProgressDownloadElementView progressDownloadElement = new ProgressDownloadElementView(
							this);

					((ViewGroup) galaryElementView.getView(this)).addView(
							progressDownloadElement, layoutParamsProgress);
					galaryElementView
							.setProgressDownloadElementView(progressDownloadElement);
				}

				View elementView = galaryElementView.getView(this);
				if (elementView.getParent() != null) {
					((ViewGroup) elementView.getParent())
							.removeView(elementView);
				}

			}
			smoothSwitcherController = new SmoothSwitcherController(this,
					galaryViewColletion, pageViewOfGalary.getColor());
			smoothSwitcherController
					.setPositionCurrentView(currentPosition - 1);
			smoothSwitcherController.activateControllerView();

			((RelativeLayout) findViewById(R.id.RelativeLayoutGalleryContainer))
					.addView(smoothSwitcherController,
							new RelativeLayout.LayoutParams(-1, -1));

		}

		addReturnButton();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// if (smoothSwitcherController != null)
		// smoothSwitcherController.activateControllerView();
	}

	private void addReturnButton() {

		View routeBurron = ApplicationSkinFactory
				.getPhotoGallaryButtonReture(pageViewOfGalary);

		routeBurron.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();

			}
		});
		// routeBurron.setBackgroundResource(R.drawable.gallery_button_selector);

		((RelativeLayout) findViewById(R.id.RelativeLayoutGalleryContainer))
				.addView(routeBurron);
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.hold_slide, R.anim.top_to_bottom);

	}

	@Override
	protected void onDestroy() {
		if (galaryViewColletion != null && galaryViewColletion.size() > 0) {
			smoothSwitcherController.removeAllViews();
			smoothSwitcherController.releaseControllerView();
		}
		super.onDestroy();

	}
}
