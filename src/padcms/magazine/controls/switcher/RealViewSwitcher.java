package padcms.magazine.controls.switcher;

import padcms.magazine.controls.SmoothFliperView;
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
 * {@link RealViewSwitcher#setOnScreenSwitchListener(OnScreenSwitchListener)} in
 * order to perform operations once a new screen has been selected.
 * 
 */
public class RealViewSwitcher extends ViewGroup {

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

		/**
		 * Notifies listeners about the new screen. Runs after the animation
		 * completed.
		 * 
		 * @param screen
		 *            The new screen index.
		 */
		void onScreenSwitched(int screen);

	}

	private static final int SNAP_VELOCITY = 1000;
	private static final int INVALID_SCREEN = -1;

	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;

	private final static int TOUCH_STATE_REST = 0;
	private final static int TOUCH_STATE_SCROLLING_HORISONTAL = 1;
	private final static int TOUCH_STATE_SCROLLING_VERTICAL = 2;

	private int mTouchState = TOUCH_STATE_REST;
	private float mLastMotionX;
	private float mLastMotionY;
	private int mTouchSlop;
	private int mMaximumVelocity;
	private int mCurrentScreen;
	private int mNextScreen = INVALID_SCREEN;

	private boolean mFirstLayout = true;

	private OnScreenSwitchListener mOnScreenSwitchListener;

	public RealViewSwitcher(Context context) {
		super(context);
		init();
	}

	public RealViewSwitcher(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mScroller = new Scroller(getContext());

		final ViewConfiguration configuration = ViewConfiguration
				.get(getContext());
		mTouchSlop = configuration.getScaledTouchSlop();
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
	}
	
	public Scroller getmScroller() {
		return mScroller;
	}

	public void setmScroller(Scroller mScroller) {
		this.mScroller = mScroller;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		if (widthMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException(
					"ViewSwitcher can only be used in EXACTLY mode.");
		}

		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		// if (heightMode != MeasureSpec.EXACTLY) {
		// throw new IllegalStateException(
		// "ViewSwitcher can only be used in EXACTLY mode.");
		// }

		// The children are given the same width and height as the workspace
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}

		if (mFirstLayout) {
			scrollTo(mCurrentScreen * width, 0);
			mFirstLayout = false;
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childLeft = 0;

		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				final int childWidth = child.getMeasuredWidth();
				child.layout(childLeft, 0, childLeft + childWidth,
						child.getMeasuredHeight());
				childLeft += childWidth;
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i("###### RealViewSwitcher.onTouchEvent ######",
				"event:" + event.getAction());
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);

		final int action = event.getAction();
		final float x = event.getX();
		final float y = event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			/*
			 * If being flinged and user touches, stop the fling. isFinished
			 * will be false if being flinged.
			 */
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}

			// Remember where the motion event started
			mLastMotionX = x;
			mLastMotionY = y;

			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
					: TOUCH_STATE_SCROLLING_HORISONTAL;
			mTouchState = TOUCH_STATE_SCROLLING_HORISONTAL;
			makeMoveParent(event);
			break;

		case MotionEvent.ACTION_MOVE:

			final int xDiff = (int) Math.abs(x - mLastMotionX);

			boolean xMoved = xDiff > mTouchSlop;

			if (mTouchState != TOUCH_STATE_REST) {
				if (xMoved) {
					// Scroll if the user moved far enough along the X axis
					mTouchState = TOUCH_STATE_SCROLLING_HORISONTAL;
				}

				if (mTouchState == TOUCH_STATE_SCROLLING_HORISONTAL) {
					// Scroll to follow the motion event
					final int deltaX = (int) (mLastMotionX - x);
					mLastMotionX = x;

					final int scrollX = getScrollX();
					if (deltaX < 0) {
						if (scrollX > 0) {
							scrollBy(Math.max(-scrollX, deltaX), 0);
						} else
							mTouchState = TOUCH_STATE_REST;
					} else if (deltaX > 0) {
						final int availableToScroll = getChildAt(
								getChildCount() - 1).getRight()
								- scrollX - getWidth();
						if (availableToScroll > 0) {
							scrollBy(Math.min(availableToScroll, deltaX), 0);
						} else
							mTouchState = TOUCH_STATE_REST;
					}
				}
			} else
				return makeMoveParent(event);

			break;

		case MotionEvent.ACTION_UP:
			if (mTouchState == TOUCH_STATE_SCROLLING_HORISONTAL) {
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
				int velocityX = (int) velocityTracker.getXVelocity();

				if (velocityX > SNAP_VELOCITY && mCurrentScreen > 0) {
					// Fling hard enough to move left
					snapToScreen(mCurrentScreen - 1);
				} else if (velocityX < -SNAP_VELOCITY
						&& mCurrentScreen < getChildCount() - 1) {
					// Fling hard enough to move right
					snapToScreen(mCurrentScreen + 1);
				} else {
					snapToDestination();
				}

				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
			}
			makeMoveParent(event);

			mTouchState = TOUCH_STATE_REST;

			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_REST;
		}

		return true;
	}

	private ViewGroup parentViewSwitcher;
	private boolean isNoParent;

	private boolean makeMoveParent(MotionEvent event) {
		if (!isNoParent) {
			if (getParentView() != null)
				return getParentView().onTouchEvent(event);
		}
		return true;
	}

	private ViewGroup getParentView() {

		if (parentViewSwitcher == null) {
			parentViewSwitcher = getParentFliper((View) getParent());
			if (parentViewSwitcher == null)
				isNoParent = true;
		}
		return parentViewSwitcher;
	}

	public ViewGroup getParentFliper(View view) {
		if (view.getParent() instanceof SmoothFliperView) {
			return (ViewGroup) view.getParent();
		} else {
			ViewParent parentView = view.getParent();
			if (parentView != null && parentView.getParent() == null)
				return null;
			else
				return getParentFliper((View) parentView);
		}

	}

	private void snapToDestination() {
		final int screenWidth = getWidth();
		final int whichScreen = (getScrollX() + (screenWidth / 2))
				/ screenWidth;

		snapToScreen(whichScreen);
	}

	private void snapToScreen(int whichScreen) {
		if (!mScroller.isFinished())
			return;

		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));

		mNextScreen = whichScreen;

		final int newX = whichScreen * getWidth();
		final int delta = newX - getScrollX();
		mScroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2);
		invalidate();
	}

	public void snapToSelectedScreen(int whichScreen) {
		if (!mScroller.isFinished())
			return;

		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));

		mNextScreen = whichScreen;

		final int newX = whichScreen * getWidth();
		final int delta = newX - getScrollX();
		mScroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta/getWidth()) * 500);
		invalidate();
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		} else if (mNextScreen != INVALID_SCREEN) {
			mCurrentScreen = Math.max(0,
					Math.min(mNextScreen, getChildCount() - 1));

			// notify observer about screen change
			if (mOnScreenSwitchListener != null)
				mOnScreenSwitchListener.onScreenSwitched(mCurrentScreen);

			mNextScreen = INVALID_SCREEN;
		}
	}

	/**
	 * Returns the index of the currently displayed screen.
	 * 
	 * @return The index of the currently displayed screen.
	 */
	public int getCurrentScreen() {
		return mCurrentScreen;
	}

	/**
	 * Sets the current screen.
	 * 
	 * @param currentScreen
	 *            The new screen.
	 */
	public void setCurrentScreen(int currentScreen) {
		mCurrentScreen = Math.max(0,
				Math.min(currentScreen, getChildCount() - 1));
		scrollTo(mCurrentScreen * getWidth(), 0);
		invalidate();
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

}
