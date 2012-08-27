package padcms.magazine.controls.switcher;

import padcms.magazine.menu.MenuController;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * RealViewSwitcher allows users to switch between multiple screens (layouts) in
 * the same way as the Android home screen (Launcher application).
 * <p>
 * You can add and remove views using the normal methods
 * {@link ViewGroup#addView(View)}, {@link ViewGroup#removeView(View)} etc. You
 * may want to listen for updates by calling
 * {@link RealViewSwitcherHorisontal#setOnScreenSwitchListener(OnScreenSwitchListener)}
 * in order to perform operations once a new screen has been selected.
 * 
 */
public class RealViewSwitcherHorisontal extends BaseRealViewSwitcher {

	private GestureDetector mGestureDetector;
	private GestureListener mGestureListener;
	private ViewGroup parentViewSwitcher;
	private boolean isNoParent;

	public RealViewSwitcherHorisontal(Context context) {
		super(context);
		mGestureListener = new GestureListener();
		mGestureDetector = new GestureDetector(getContext(), mGestureListener,
				null, true);

		init();
	}

	public RealViewSwitcherHorisontal(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
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

		// final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
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
		//
		// if (mNextScreen != mCurrentScreen) {
		//
		// final int newX = mNextScreen * getWidth();
		// final int delta = newX - getScrollX();
		//
		// mScroller.startScroll(getScrollX(), 0, delta, 0, 0);
		//
		// }

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
		// Log.e("###### RealViewSwitcherHorisontal.onTouchEvent ######",
		// "event:"
		// + event.getAction() + "  X:" + event.getRawX());
		// // if (mVelocityTracker == null) {
		// mVelocityTracker = VelocityTracker.obtain();
		// }
		mGestureDetector.onTouchEvent(event);
		// mVelocityTracker.addMovement(event);

		final int action = event.getAction();
		final float x = event.getRawX();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			/*
			 * If being flinged and user touches, stop the fling. isFinished
			 * will be false if being flinged.
			 */
			if (!mScroller.isFinished()) {
				// mScroller.abortAnimation();
			}

			// Remember where the motion event started
			mLastMotionX = mFirstMotionX = x;

			mModeSide = SCROLL_MODE_NONE;
			mTouchState = TOUCH_STATE_REST;// mScroller.isFinished() ?
											// TOUCH_STATE_REST
			// : TOUCH_STATE_SCROLLING_HORISONTAL;

			// mTouchState = TOUCH_STATE_SCROLLING_HORISONTAL;
			makeMoveParent(event);
			break;

		case MotionEvent.ACTION_MOVE:

			// int side=
			final int xDiff = (int) Math.abs(x - mLastMotionX);
			boolean xMoved = xDiff > mTouchSlop;
			mModeSide = (x - mLastMotionX) > 0 ? SCROLL_MODE_LEFT
					: SCROLL_MODE_RIGHT;
			if (mTouchState == TOUCH_STATE_REST) {
				mModeSide = (x - mLastMotionX) > 0 ? SCROLL_MODE_LEFT
						: SCROLL_MODE_RIGHT;

				if (xMoved) {
					mTouchState = TOUCH_STATE_SCROLLING_HORISONTAL;
				} else {
					return makeMoveParent(event);
				}

			}
			if (mTouchState == TOUCH_STATE_SCROLLING_HORISONTAL) {
				final int deltaX = (int) (mLastMotionX - x);
				mLastMotionX = x;

				final int scrollX = getScrollX();
				if (deltaX < 0) {
					if (scrollX > 0) {
						scrollBy(Math.max(-scrollX, deltaX), 0);
					} else
						mTouchState = TOUCH_STATE_REST;
				} else if (deltaX > 0) {
					View childAt = getChildAt(getChildCount() - 1);
					if (childAt != null) {
						final int availableToScroll = childAt.getRight()
								- scrollX - getWidth();

						if (availableToScroll > 0) {
							scrollBy(Math.min(availableToScroll, deltaX), 0);
						} else
							mTouchState = TOUCH_STATE_REST;
					}
					// mTouchState = TOUCH_STATE_REST;
				}
			}

			break;

		case MotionEvent.ACTION_UP:
			if (mTouchState == TOUCH_STATE_SCROLLING_HORISONTAL) {
				// final VelocityTracker velocityTracker = mVelocityTracker;
				// velocityTracker.computeCurrentVelocity(1000,
				// mMaximumVelocity);
				// int velocityX = (int) velocityTracker.getXVelocity();

				// final int deltaX = (int) (mFirstMotionX - x);
				// Log.e("###### RealViewSwitcherHorisontal.onTouchEvent ######",
				// "deltaX :" + 0 + "  velocityX:" + velocityX);

				// if (velocityX > SNAP_VELOCITY && allowScroll(mModeSide)) {
				//
				// snapToScreen(mCurrentScreen - 1);
				//
				// } else if (velocityX < -SNAP_VELOCITY &&
				// allowScroll(mModeSide)) {
				//
				// snapToScreen(mCurrentScreen + 1);
				//
				// } else {

				snapToDestination();

				// }

			}
			if (Math.abs(mFirstMotionX - mLastMotionX) > ViewConfiguration
					.getTouchSlop())
				makeMoveParent(event);
			// makeMoveParent(event);

			mTouchState = TOUCH_STATE_REST;
			// if (mVelocityTracker != null) {
			// mVelocityTracker.recycle();
			// mVelocityTracker = null;
			// }
			break;
		case MotionEvent.ACTION_CANCEL:

			mTouchState = TOUCH_STATE_REST;

		}

		return true;
	}

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
		if (view.getParent() instanceof BaseRealViewSwitcher) {
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
		// int whichScreen = (getScrollX() + (screenWidth / 5) * 4) /
		// screenWidth;
		int whichScreen = 0;
		boolean isOnRight = getScrollX() > screenWidth * mCurrentScreen;
		boolean limitToSnap = Math.abs(getScrollX() - screenWidth
				* mCurrentScreen) > ViewConfiguration.get(getContext())
				.getScaledPagingTouchSlop();

		if (isOnRight && limitToSnap) {

			whichScreen = mCurrentScreen + 1;
		} else if (!isOnRight && limitToSnap)
			whichScreen = mCurrentScreen - 1;
		else
			whichScreen = mCurrentScreen;

		snapToScreen(whichScreen);
	}

	private void snapToScreen(int whichScreen) {
		if (!mScroller.isFinished())
			return;
		// mOnScreenSwitchListener.startScreenSwitch(getChildAt(whichScreen));

		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));

		mNextScreen = whichScreen;

		final int newX = whichScreen * getWidth();
		final int delta = newX - getScrollX();
		mScroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta));
		invalidate();
	}

	public void snapToSelectedScreen(int whichScreen) {
		if (!mScroller.isFinished())
			return;

		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));

		if (mNextScreen + 1 != whichScreen || mNextScreen - 1 != whichScreen) {
			mOnScreenSwitchListener.onReleseScreen(mCurrentScreen);
		}

		mNextScreen = whichScreen;

		final int newX = whichScreen * getWidth();
		final int delta = newX - getScrollX();
		mScroller.startScroll(getScrollX(), 0, delta, 0,
				Math.abs(delta / getWidth()) * 100);
		invalidate();
	}

	public void snapFastToSelectedScreen(int whichScreen) {
		// if (!mScroller.isFinished())
		// return;

		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));

		mNextScreen = mCurrentScreen = whichScreen;

		final int newX = whichScreen * getMeasuredWidth();

		scrollTo(newX, 0);

		mOnScreenSwitchListener.onScreenSwitched(mCurrentScreen,
				getChildAt(mCurrentScreen));

		invalidate();
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		} else if (mNextScreen != INVALID_SCREEN) {
			if (mCurrentScreen != mNextScreen) {
				mCurrentScreen = Math.max(0,
						Math.min(mNextScreen, getChildCount() - 1));

				// notify observer about screen change
				mOnScreenSwitchListener.onScreenSwitched(mCurrentScreen,
						getChildAt(mCurrentScreen));
			}
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

	class GestureListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			return super.onDoubleTap(e);
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			mOnScreenSwitchListener.onScreenSingleTap();
			// MenuController.getIstance(getContext()).showHideMenu();
			return true;
		}
	}

}
