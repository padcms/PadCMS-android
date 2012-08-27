package padcms.magazine.page.elementview;

import padcms.application11.R;
import padcms.dao.issue.bean.ElementData;
import padcms.dao.issue.bean.ElementDataPosition;
import padcms.magazine.controls.imagecontroller.ImageViewGroup;
import padcms.magazine.controls.switcher.BaseRealViewSwitcher;
import padcms.magazine.factory.ElementViewFactory;
import padcms.magazine.factory.IssueViewFactory;
import padcms.magazine.page.BasePageView;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ActiveZoneElementDataView extends ImageView implements
		OnClickListener {

	private String activeZoneUrl;
	private ElementDataPosition position;
	private BasePageView pageView;
	private String activeZoneKey;
	private String activeZoneValue;
	private int widthActiveZone;
	private int heightActiveZone;
	private int topActiveZone;
	private int leftActiveZone;
	private ViewGroup parent;

	public ActiveZoneElementDataView(Context context, BasePageView pageView,
			ElementData elementData, float parentWidth, float parentHeight) {
		super(context);
		this.pageView = pageView;
		activeZoneUrl = ElementViewFactory
				.getElementDataStringValue(elementData);
		activeZoneKey = activeZoneUrl.substring("local://".length());
		if (activeZoneKey.contains("/")) {
			activeZoneKey = activeZoneKey.substring(0,
					activeZoneKey.indexOf("/"));
			activeZoneValue = activeZoneUrl.substring(("local://"
					+ activeZoneKey + "/").length());
		} else {
			activeZoneValue = "";
		}
		// setOnTouchListener(ApplicationSkinFactory
		// .getOnTouchListnerForDrckPress(this));
		setOnClickListener(this);

		position = elementData.getElementDataPosition();
		Display display = ((Activity) getContext()).getWindowManager()
				.getDefaultDisplay();
		int fullOriginalWidth = display.getWidth();
		int displayHeight = display.getHeight();
		if (display.getWidth() > display.getHeight()) {
			fullOriginalWidth = display.getHeight();
			displayHeight = display.getWidth();
		}

		float scaleSize = fullOriginalWidth / parentWidth;

		int fullOriginalHeight = (int) Math.round(parentHeight * scaleSize);
		if (position.getStart_x() < 0) {
			position.setStart_x(new Long(0));
		}
		if (position.getStart_y() < 0) {
			position.setEnd_y(new Long(position.getEnd_y().intValue()
					+ position.getStart_y().intValue()));
			position.setStart_y(new Long(0));
		}
		if (position.getEnd_x() < 0) {
			position.setEnd_x(new Long(0));
		}
		if (position.getEnd_y() < 0) {
			position.setStart_y(new Long(position.getStart_y().intValue()
					+ position.getEnd_y().intValue()));
			position.setEnd_y(new Long(0));
		}
		widthActiveZone = (int) (Math.abs(position.getStart_x()
				- position.getEnd_x()) * scaleSize);

		heightActiveZone = (int) (Math.abs(position.getStart_y()
				- position.getEnd_y()) * scaleSize);

		RelativeLayout.LayoutParams activeLayoutParams = new RelativeLayout.LayoutParams(
				widthActiveZone, heightActiveZone);
		int startX = position.getStart_x().intValue();
		if (position.getStart_x().intValue() > position.getEnd_x()) {
			startX = position.getEnd_x().intValue();
		}

		activeLayoutParams.leftMargin = leftActiveZone = (int) (startX * scaleSize);

		// Log.i("Active Zone Params:", "p_height:" + parentHeight + "p_width:"
		// + parentWidth + "new_height:" + fullOriginalHeight
		// + "new_width:" + fullOriginalWidth);

		int startY = position.getStart_y().intValue();
		if (position.getStart_y().intValue() < position.getEnd_y()) {
			startY = position.getEnd_y().intValue();
		}
		activeLayoutParams.topMargin = topActiveZone = (int) (fullOriginalHeight - startY
				* scaleSize);

		setLayoutParams(activeLayoutParams);

		setBackgroundResource(R.drawable.activezone_selector);
		// setBackgroundColor(Color.RED);
		// getBackground().setAlpha(150);

		if (activeZoneKey.startsWith("scroller")
				|| activeZoneKey.startsWith("thumbs")) {
			// setBackgroundColor(Color.BLUE);
			// getBackground().setAlpha(150);
			setOnTouchListener(null);
			setClickable(false);

		}
		layout(leftActiveZone, topActiveZone, leftActiveZone + widthActiveZone,
				topActiveZone + heightActiveZone);
		// Log.i("Active Zone Params:" + activeZoneUrl, "start_X:"
		// + activeLayoutParams.leftMargin + " End_X:"
		// + (activeLayoutParams.leftMargin + widthActiveZone)
		// + " start_Y:" + activeLayoutParams.topMargin + " End_Y:"
		// + (activeLayoutParams.topMargin + heightActiveZone));
	}

	public String getActiveZoneValue() {
		return activeZoneValue;
	}

	public int getWidthActiveZone() {
		return widthActiveZone;
	}

	public int getTopActiveZone() {
		return topActiveZone;
	}

	public int getLeftActiveZone() {
		return leftActiveZone;
	}

	public int getHeightActiveZone() {
		return heightActiveZone;
	}

	public String getActiveZoneKey() {
		return activeZoneKey;
	}

	public void onClickActionView() {
		Toast.makeText(getContext(), "This is Action zone:" + activeZoneUrl,
				200).show();
		if (activeZoneUrl.toLowerCase().startsWith("http://")) {
			if (activeZoneUrl.contains(".mp4")
					|| activeZoneKey.contains("youtube.com")) {
				// Intent myIntent = new Intent(getContext(),
				// VideoActivity.class);
				// myIntent.putExtra("videoUrl", activeZoneUrl);
				// ((Activity) getContext()).startActivityForResult(myIntent,
				// 1);
				IssueViewFactory.getIssueViewFactoryIstance(getContext())
						.runVideo(activeZoneUrl, false, true);
			} else {
				IssueViewFactory.getIssueViewFactoryIstance(getContext())
						.runWebView(activeZoneUrl);
			}
		} else if (activeZoneKey.startsWith("navigation")) {
			IssueViewFactory instanceIssueView = IssueViewFactory
					.getIssueViewFactoryIstance(pageView.getContext());
			BasePageView pageVideFlipTo = instanceIssueView
					.findPageViewByPageMachineName(activeZoneValue);
			instanceIssueView.flipToPage(pageVideFlipTo);
			// ApplicationController.pageViewFliper
			// .flipToPageByFolderName(activeZoneValue);
		} else if (activeZoneKey.startsWith("action")) {
			if (activeZoneValue.startsWith("photo")) {
				String array[] = activeZoneValue.split("/");
				int gallaryID = array.length >= 2 ? Integer.parseInt(array[1])
						: 1;
				int currentPosition = array.length >= 3 ? Integer
						.parseInt(array[2]) : 0;

				IssueViewFactory.getIssueViewFactoryIstance(
						pageView.getContext()).showGallary(
						pageView.getPageID(), gallaryID, currentPosition);
			} else if (activeZoneValue.startsWith("video")) {
				IssueViewFactory.getIssueViewFactoryIstance(
						pageView.getContext()).playVideoCurrentPage(false);
			}
		} else if (activeZoneKey.startsWith("video")) {

			IssueViewFactory.getIssueViewFactoryIstance(pageView.getContext())
					.playVideoCurrentPage(false);

		} else if (activeZoneKey.startsWith("thumbs")) {

		} else if (activeZoneKey.startsWith("button")) {
			int thumbleNumber = activeZoneValue.length() > 0 ? Integer
					.parseInt(activeZoneValue) : Integer.parseInt(activeZoneKey
					.replace("button", ""));
			pageView.activateElementView(thumbleNumber);
		} else if (activeZoneKey.startsWith("popup")) {
			int thumbleNumber = activeZoneValue.length() > 0 ? Integer
					.parseInt(activeZoneValue) : Integer.parseInt(activeZoneKey
					.replace("button", ""));
			pageView.activateElementView(thumbleNumber);
		}
	}

	public int pontX;
	public int pontY;
	int deltaX = 0;
	int deltaY = 0;
	boolean onTouchReturn;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		onTouchReturn = true;
		Log.i("Active zone", "Action!!!!!" + event.getAction());
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {

			deltaX = 0;
			deltaY = 0;
			onTouchReturn = super.onTouchEvent(event);
			// event.setLocation(event.getRawX(), event.getRawY());
			getParentView().onTouchEvent(event);

		} else if (action == MotionEvent.ACTION_UP) {

			onTouchReturn = super.onTouchEvent(event);
			if (deltaX >= ViewConfiguration.getTouchSlop()
					|| deltaY >= ViewConfiguration.getTouchSlop()) {
				getParentView().onTouchEvent(event);
			}

		} else {

			deltaX += Math.abs(pontX - event.getRawX());
			deltaY += Math.abs(pontY - event.getRawY());

			if (deltaX >= ViewConfiguration.getTouchSlop()
					|| deltaY >= ViewConfiguration.getTouchSlop()) {

				if (event.getAction() != MotionEvent.ACTION_CANCEL) {
					getParentView().onTouchEvent(event);
				}
				event.setAction(MotionEvent.ACTION_CANCEL);

			}

			onTouchReturn = super.onTouchEvent(event);
			if (event.getAction() != MotionEvent.ACTION_CANCEL) {
				getParentView().onTouchEvent(event);
			}

		}

		pontX = (int) event.getRawX();
		pontY = (int) event.getRawY();

		return onTouchReturn;
	}

	private ViewGroup getParentView() {

		if (parent == null) {
			parent = getParentScroll((ViewGroup) this.getParent());
		}
		return parent;
	}

	private ViewGroup getParentScroll(ViewGroup view) {

		if (view != null) {

			if (view instanceof ImageViewGroup) {

				return view;

			} else if (view instanceof BaseRealViewSwitcher) {

				return view;

			} else {

				return getParentScroll((ViewGroup) view.getParent());

			}
		}

		return null;
	}

	@Override
	public void onClick(View view) {
		onClickActionView();
	}

}
