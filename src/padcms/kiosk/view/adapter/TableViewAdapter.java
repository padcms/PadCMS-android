package padcms.kiosk.view.adapter;

import java.util.List;
import java.util.NoSuchElementException;

import padcms.application11.R;
import padcms.dao.application.bean.Issue;
import padcms.dao.application.bean.Revision;
import padcms.kiosk.RevisionStateManager;
import padcms.kiosk.RevisionStateManager.RevisionInstallState;
import padcms.kiosk.view.BasicViewController;
import padcms.kiosk.view.gallery.CoverView;
import padcms.kiosk.view.grid.TableViewController;
import padcms.kiosk.view.listener.DeleteButtonController;
import padcms.kiosk.view.listener.InstallButtonController;
import padcms.net.NetHepler;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class TableViewAdapter {
	BasicViewController parentViewController;
	Context context;
	List<Revision> revisionList;
	int imageWidth;
	int imageHeight;
	View[] views;

	public TableViewAdapter(Context context, List<Revision> list,
			BasicViewController parentViewController) {
		this.parentViewController = parentViewController;
		// this.fileStateManager = parentView.getFileStateManager();
		this.context = context;
		this.revisionList = list;

		views = new View[list.size()];
		calculateImageSize();

		// LayoutInflater inflater = (LayoutInflater)
		// context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LayoutInflater inflater = LayoutInflater.from(context);

		InstallButtonController installButtonListener = new InstallButtonController(
				context, parentViewController);
		DeleteButtonController deleteButtonListener = new DeleteButtonController(
				parentViewController);
		RelativeLayout coverView;
		TextView textTitleInGridItem;
		for (int i = 0; i < views.length; i++) {
			views[i] = new View(context);
			views[i] = inflater.inflate(R.layout.grid_view_item, null, false);

			Revision revision = list.get(Math.min(i, list.size() - 1));
			views[i].setTag(revision);
			textTitleInGridItem = (TextView) views[i]
					.findViewById(R.id.textViewTitleInGrid);
			coverView = (RelativeLayout) views[i].findViewById(R.id.gridCover);
			String coverImageUrl = NetHepler.getAbsoluteUrlCoverImage(revision
					.getCover_image_list());
			
			coverView.addView(new CoverView(context, coverImageUrl, imageWidth,
					imageHeight));

			
			textTitleInGridItem.setText(revision.getParenIssue().getTitle());

			initializeButtons(installButtonListener, deleteButtonListener, i,
					revision);
			coverView.setTag(revision);
			RevisionInstallState state = RevisionStateManager.getInstance(
					context).getState(revision.getId().intValue());
			((TableViewController) parentViewController)
					.setVisibleStateForView(state, revision, views[i]);
		}

	}

	private void calculateImageSize() {
		Display display = ((Activity) this.context).getWindowManager()
				.getDefaultDisplay();
		imageWidth = (display.getWidth() < display.getHeight()) ? display
				.getWidth() / 6 : display.getHeight() / 6;
		imageHeight = (int) (imageWidth * (260.0 / 180.0));
	}

	private void initializeButtons(InstallButtonController installListener,
			DeleteButtonController deleteListener, int i, Revision revision) {
		Button firstGridItemButton = (Button) views[i]
				.findViewById(R.id.buttonFirstInGrid);
		Button downloadButton = (Button) views[i]
				.findViewById(R.id.buttonDownload);
		Button cancelButton = (Button) views[i].findViewById(R.id.buttonCancel);
		Button installButton = (Button) views[i]
				.findViewById(R.id.buttonInstall);
		Button openButton = (Button) views[i].findViewById(R.id.buttonOpen);
		Button secondGridItemButton = (Button) views[i]
				.findViewById(R.id.buttonSecondInGrid);

		firstGridItemButton.setTag(revision);
		downloadButton.setTag(revision);
		cancelButton.setTag(revision);
		installButton.setTag(revision);
		openButton.setTag(revision);
		secondGridItemButton.setTag(revision);

		firstGridItemButton.setOnClickListener(installListener);
		downloadButton.setOnClickListener(installListener);
		cancelButton.setOnClickListener(installListener);
		installButton.setOnClickListener(installListener);
		openButton.setOnClickListener(installListener);
		secondGridItemButton.setOnClickListener(deleteListener);
	}

	public void setTableViewContent(TableLayout tableLayout, int columnsCount) {
		Display display = ((Activity) this.context).getWindowManager()
				.getDefaultDisplay();
		int widthOfColumn = display.getWidth() / columnsCount;
		int countRows = views.length;
		for (int i = 0; i < countRows; i += columnsCount) {
			TableRow tableRow = new TableRow(tableLayout.getContext());
			// tableRow.setLayoutParams(new TableLayout.LayoutParams(
			// LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

			for (int j = i; j < i + columnsCount; j++) {
				if (j < views.length) {

					views[j].setLayoutParams(new LayoutParams(widthOfColumn, -2));

					removeViewFromParentView(views[j]);

					tableRow.addView(views[j]);
				}
			}
			if (tableRow.getChildCount() > 0)
				tableLayout.addView(tableRow);

		}
	}

	private void removeViewFromParentView(View v) {
		if (v != null) {
			if (v.getParent() != null) {
				((ViewGroup) v.getParent()).removeView(v);
			}
		}
	}

	public void updateItems() {
		for (View view : views) {
			Revision revision = (Revision) view.getTag();
			parentViewController.setVisibleOnState(revision);
			// view.invalidate();
		}
	}

	public View getView(int position) {
		Log.d("GRID ADAPTER", "GET VIEW position=" + position);

		return views[position];

	}

	/**
	 * Looks though all objects of View in GridView for one with object of Issue
	 * delivered in <code>issue</code> parameter.
	 * 
	 * @param issue
	 *            is object of <code>Issue</code> class.
	 * @return View object with <code>issue</code> in tag
	 * @throws NoSuchElementException
	 *             if there is no any view with such <code>issue</code> in tag
	 */
	public View findViewInGridByRevision(Revision revision) {
		if (revision == null)
			Log.d("PROCESSING ISSUE NULL", "PROCESSING ISSUE NULL");
		View fButton;
		Log.d("!!!", "views.length=" + views.length);
		for (int i = 0; i < views.length; i++) {
			fButton = views[i].findViewById(R.id.buttonFirstInGrid);
			Revision revisionInView = (Revision) fButton.getTag();

			if (revisionInView.equals(revision)) {
				return views[i];
			}
		}
		return null;

	}

	/*
	 * private void setVisibility(int visibility, View... views) { for (View
	 * view : views) { view.setVisibility(visibility); } }
	 * 
	 * private void setTags(int tag, View... views) { for (View view : views) {
	 * view.setTag(tag); } }
	 */
}
