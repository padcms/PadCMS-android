package padcms.magazine.controls.switcher;

import java.util.ArrayList;

import padcms.magazine.page.BasePageView;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Scroller;
import android.widget.ViewSwitcher;

/**
 * RealViewSwitcher allows users to switch between multiple screens (layouts) in
 * the same way as the Android home screen (Launcher application).
 * <p>
 * You can add and remove views using the normal methods
 * {@link ViewGroup#addView(View)}, {@link ViewGroup#removeView(View)} etc. You
 * may want to listen for updates by calling
 * {@link BaseRealViewSwitcher#setOnScreenSwitchListener(OnScreenSwitchListener)}
 * in order to perform operations once a new screen has been selected.
 * 
 */
public class BaseRealViewSwitcher extends ViewGroup {

	// TODO: This class does the basic stuff right now, but it would be cool to
	// have certain things implemented,
	// e.g. using an adapter for getting views instead of setting them directly,
	// memory management and the
	// possibility of scrolling vertically instead of horizontally. If you have
	// ideas or patches, please visit
	// my website and drop me a mail. :-)

	/**
	 * Listener for the event that the RealViewSwitcher switches to a new view.
	 */
	public static interface OnScreenSwitchListener {

		// void startScreenSwitch(View swichedView);

		void onScreenSwitched(int screen, View swichedView);

		void onReleseScreen(int screen);

		void onScreenSingleTap();

	}

	protected static final int SNAP_VELOCITY = 500;
	protected static final int INVALID_SCREEN = -1;

	protected Scroller mScroller;
	// protected VelocityTracker mVelocityTracker;

	protected final static int TOUCH_STATE_REST = 0;
	protected final static int TOUCH_STATE_SCROLLING_HORISONTAL = 1;
	protected final static int TOUCH_STATE_SCROLLING_VERTICAL = 2;

	protected int mTouchState = TOUCH_STATE_REST;
	protected float mLastMotionX;
	protected float mLastMotionY;
	protected float mFirstMotionX;
	protected float mFirstMotionY;

	protected int mTouchSlop;
	protected int mMaximumVelocity;
	protected int mCurrentScreen;
	protected int mNextScreen = INVALID_SCREEN;
	protected int mModeSide;
	protected boolean mFirstLayout = true;

	protected OnScreenSwitchListener mOnScreenSwitchListener;
	protected final static int SCROLL_MODE_LEFT = -1;
	protected final static int SCROLL_MODE_RIGHT = 1;
	protected final static int SCROLL_MODE_TOP = -2;
	protected final static int SCROLL_MODE_BOTTOM = 2;
	protected final static int SCROLL_MODE_NONE = 1;

	public BaseRealViewSwitcher(Context context) {
		super(context);
		init();
	}

	public BaseRealViewSwitcher(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	protected void init() {
		mScroller = new Scroller(getContext());
		mNextScreen = 0;
		final ViewConfiguration configuration = ViewConfiguration
				.get(getContext());
		mTouchSlop = ViewConfiguration.getWindowTouchSlop();
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

	}

	public Scroller getmScroller() {
		return mScroller;
	}

	public void setmScroller(Scroller mScroller) {
		this.mScroller = mScroller;
	}

	/**
	 * Sets the {@link ViewSwitcher.OnScreenSwitchListener}.
	 * 
	 * @param onScreenSwitchListener
	 *            The listener for switch events.
	 */
	public void setOnScreenSwitchListener(
			OnScreenSwitchListener onScreenSwitchListener) {
		mOnScreenSwitchListener = onScreenSwitchListener;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub

	}

	protected boolean allowScroll(int mode) {
		boolean allow = false;
		switch (mode) {
		case SCROLL_MODE_LEFT:
		case SCROLL_MODE_TOP:
			if (mCurrentScreen != 0)
				allow = true;
			break;
		case SCROLL_MODE_RIGHT:
		case SCROLL_MODE_BOTTOM:
			if (mCurrentScreen < getChildCount() - 1)
				allow = true;
			break;

		}

		return allow;
	}

}
