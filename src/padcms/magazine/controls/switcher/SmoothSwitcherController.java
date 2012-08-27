package padcms.magazine.controls.switcher;

import java.util.ArrayList;

import padcms.application11.R;
import padcms.magazine.factory.ResourceResolutionHelper;
import padcms.magazine.page.State;
import padcms.magazine.page.elementview.BaseElementView;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SmoothSwitcherController extends RelativeLayout implements
		BaseRealViewSwitcher.OnScreenSwitchListener, View.OnClickListener {

	private RealViewSwitcherHorisontal realViewSwitcher;
	private ArrayList<BaseElementView> elementViewCollection;
	private BaseElementView activeElementView;
	private int positionCurrentView;
	private LinearLayout layoutBooletsContainer;
	private int color;
	private int inactiveColor = Color.LTGRAY;

	public SmoothSwitcherController(Context context,
			ArrayList<?> elementViewCollection, int color) {
		super(context);
		this.color = color;
		this.elementViewCollection = (ArrayList<BaseElementView>) elementViewCollection;
		realViewSwitcher = new RealViewSwitcherHorisontal(context);
		positionCurrentView = 0;
		realViewSwitcher.setOnScreenSwitchListener(this);

		for (BaseElementView elementView : this.elementViewCollection) {
			realViewSwitcher.addView(elementView.getView(context));
		}

		RelativeLayout.LayoutParams relalSwitcherParams = new RelativeLayout.LayoutParams(
				-1, -1);
		addView(realViewSwitcher, relalSwitcherParams);

		setSwitcherControls();
	}

	public int getPositionCurrentView() {
		return positionCurrentView;
	}

	public void setPositionCurrentView(int positionCurrentView) {
		this.positionCurrentView = positionCurrentView;
		realViewSwitcher.snapFastToSelectedScreen(positionCurrentView);
	}

	public void setSwitcherControls() {
		layoutBooletsContainer = new LinearLayout(getContext());

		Display defaultDisplay = ResourceResolutionHelper
				.getDefaultDisplay(getContext());
		int viewWidth = defaultDisplay.getWidth() / 20;
		if (defaultDisplay.getWidth() > defaultDisplay.getHeight())
			viewWidth = defaultDisplay.getHeight() / 20;
		for (int i = 0; i < elementViewCollection.size(); i++) {
			View view = new View(getContext());
			view.setLayoutParams(new LinearLayout.LayoutParams(viewWidth,
					viewWidth));
			((LinearLayout.LayoutParams) view.getLayoutParams()).leftMargin = 10;

			view.setBackgroundResource(R.drawable.circle_shape);
			view.setTag(i);
			view.setOnClickListener(this);
			if (i == positionCurrentView) {
				setColorFilterForView(view, color);

			} else
				setColorFilterForView(view, inactiveColor);

			layoutBooletsContainer.addView(view);

		}

		RelativeLayout.LayoutParams booletsParams = new RelativeLayout.LayoutParams(
				-2, -2);
		booletsParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		booletsParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		booletsParams.bottomMargin = 10;
		addView(layoutBooletsContainer, booletsParams);
	}

	public RealViewSwitcherHorisontal getRealViewSwitcher() {
		return realViewSwitcher;
	}

	public void setRealViewSwitcher(RealViewSwitcherHorisontal realViewSwitcher) {
		this.realViewSwitcher = realViewSwitcher;
	}

	public BaseElementView getActiveElementView() {
		return activeElementView;
	}

	public void setActiveElementView(BaseElementView activeElementView) {
		this.activeElementView = activeElementView;
	}

	@Override
	public void onScreenSwitched(int screenPosition, View view) {
		Log.i("SmoothSwitcherController ", "screen:" + positionCurrentView);
		
		positionCurrentView = screenPosition;
		
		activateView(screenPosition);
	}

	public void activateControllerView() {
		activateView(positionCurrentView);
	}

	public void disactivateControllerView() {
		for (int i = 0; i < elementViewCollection.size(); i++) {
			if (i == positionCurrentView)
				elementViewCollection.get(i).setState(State.DISACTIVE);
			else
				elementViewCollection.get(i).setState(State.RELEASE);

		}
	}

	public void releaseControllerView() {
		for (int i = 0; i < elementViewCollection.size(); i++) {

			elementViewCollection.get(i).setState(State.RELEASE);

		}
	}

	private void activateView(int screenPosition) {
		for (int i = 0; i < elementViewCollection.size(); i++) {

			if (i == screenPosition) {

				setColorFilterForView(layoutBooletsContainer.getChildAt(i),
						color);
				activeElementView = elementViewCollection.get(i);
				activeElementView.setState(State.ACTIVE);

			} else if ((i >= screenPosition - 1) && (i <= screenPosition + 1)) {
				setColorFilterForView(layoutBooletsContainer.getChildAt(i),
						inactiveColor);
				elementViewCollection.get(i).setState(State.DISACTIVE);

			} else {

				setColorFilterForView(layoutBooletsContainer.getChildAt(i),
						inactiveColor);
				elementViewCollection.get(i).setState(State.RELEASE);

			}
		}
	}

	public void setColorFilterForView(View view, int color) {
		view.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);

	}

	@Override
	public void onClick(View v) {

		int screenPosition = (Integer) v.getTag();
		if (screenPosition != positionCurrentView) {
			if (realViewSwitcher.getmScroller().isFinished()) {
				for (int i = 0; i < elementViewCollection.size(); i++) {
					if (i != positionCurrentView) {
						elementViewCollection.get(i).setState(State.DISACTIVE);
					}
					setColorFilterForView(layoutBooletsContainer.getChildAt(i),
							inactiveColor);
				}

				setColorFilterForView(
						layoutBooletsContainer.getChildAt(screenPosition),
						color);

				realViewSwitcher.snapToSelectedScreen(screenPosition);
			}
		}
	}

	@Override
	public void onReleseScreen(int screen) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScreenSingleTap() {
		// TODO Auto-generated method stub

	}

}
