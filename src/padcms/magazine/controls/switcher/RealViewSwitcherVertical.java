package padcms.magazine.controls.switcher;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;

public class RealViewSwitcherVertical extends BaseRealViewSwitcher {

	public RealViewSwitcherVertical(Context context) {
		super(context);
		init();
	}

	public RealViewSwitcherVertical(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		if (widthMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException(
					"ViewSwitcher can only be used in EXACTLY mode.");
		}

		final int height = MeasureSpec.getSize(heightMeasureSpec);

		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException(
					"ViewSwitcher can only be used in EXACTLY mode.");
		}

		// The children are given the same width and height as the workspace
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}

		if (mFirstLayout) {
			scrollTo(0, mCurrentScreen * height);
			mFirstLayout = false;
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childTop = 0;

		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				final int childHeight = child.getMeasuredHeight();
				child.layout(0, childTop, child.getMeasuredWidth(), childTop
						+ childHeight);
				childTop += childHeight;
			}
		}
	}

//	float mFirstMotionX, mFirstMotionY;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Log.i("###### RealViewSwitcherVertical.onTouchEvent ######", "event:"
		// + event.getAction() + " x:" + event.getRawX());
		// if (mVelocityTracker == null) {
		// mVelocityTracker = VelocityTracker.obtain();
		// }
		// mVelocityTracker.addMovement(event);

		final int action = event.getAction();
		final float x = event.getRawX();
		final float y = event.getRawY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (mScroller.isFinished()) {

				// mScroller.abortAnimation();

			}

			// Remember where the motion event started
			mFirstMotionX = mLastMotionX = x;
			mFirstMotionY = mLastMotionY = y;
			// mModeSide = SCROLL_MODE_NONE;
			// mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
			// : TOUCH_STATE_SCROLLING_HORISONTAL;
			mModeSide = SCROLL_MODE_NONE;
			mTouchState = TOUCH_STATE_REST;
			// mTouchState = TOUCH_STATE_SCROLLING_HORISONTAL;
			makeMoveParent(event);
			break;

		case MotionEvent.ACTION_MOVE:

			final int xDiff = (int) Math.abs(x - mLastMotionX);
			final int yDiff = (int) Math.abs(y - mLastMotionY);
			boolean xMoved = xDiff > mTouchSlop;
			boolean yMoved = yDiff > mTouchSlop;

			if (mTouchState == TOUCH_STATE_REST) {
				mModeSide = (y - mLastMotionY) > 0 ? SCROLL_MODE_TOP
						: SCROLL_MODE_BOTTOM;

				if (xMoved) {
					mTouchState = TOUCH_STATE_SCROLLING_HORISONTAL;
				} else if (allowScroll(mModeSide) && yMoved) {
					mTouchState = TOUCH_STATE_SCROLLING_VERTICAL;
				} else {
					return makeMoveParent(event);
				}
			}

			if (mTouchState == TOUCH_STATE_SCROLLING_HORISONTAL) {
				mLastMotionX = x;
				makeMoveParent(event);
			}

			if (mTouchState == TOUCH_STATE_SCROLLING_VERTICAL) {
				// Scroll to follow the motion event
				final int deltaY = (int) (mLastMotionY - y);
				mLastMotionY = y;

				final int scrollY = getScrollY();
				if (deltaY < 0) {
					if (scrollY > 0) {
						scrollBy(0, Math.max(-scrollY, deltaY));
					} else
						mTouchState = TOUCH_STATE_REST;
				} else if (deltaY > 0) {
					final int availableToScroll = getChildAt(
							getChildCount() - 1).getBottom()
							- scrollY - getHeight();
					if (availableToScroll > 0) {
						scrollBy(0, Math.min(availableToScroll, deltaY));
					} else
						mTouchState = TOUCH_STATE_REST;
				}
			}

			break;

		case MotionEvent.ACTION_UP:
			if (mTouchState == TOUCH_STATE_SCROLLING_VERTICAL) {
				// final VelocityTracker velocityTracker = mVelocityTracker;
				// velocityTracker.computeCurrentVelocity(1000,
				// mMaximumVelocity);

				// if (velocityY > SNAP_VELOCITY && allowScroll(mModeSide))
				// {
				//
				// snapToScreen(mCurrentScreen - 1);
				// } else if (velocityY < -SNAP_VELOCITY &&
				// allowScroll(mModeSide)) {
				// // Fling hard enough to move right
				// snapToScreen(mCurrentScreen + 1);
				// } else {
				snapToDestination();
				// }

			}
			if (mTouchState == TOUCH_STATE_SCROLLING_HORISONTAL) {
				snapToDestination();

			}
			if (mTouchState == TOUCH_STATE_REST
					|| mTouchState == TOUCH_STATE_SCROLLING_HORISONTAL)
				if (Math.abs(mFirstMotionY - mLastMotionY) < ViewConfiguration
						.getTouchSlop())
					makeMoveParent(event);
			// if (mVelocityTracker != null) {
			// mVelocityTracker.recycle();
			// mVelocityTracker = null;
			// }
			// if (mTouchState == TOUCH_STATE_REST)
			// makeMoveParent(event);

			mTouchState = TOUCH_STATE_REST;

			break;
		case MotionEvent.ACTION_CANCEL:
			// mTouchState = TOUCH_STATE_REST;
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
			parentViewSwitcher = getParentFliper(this);
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
		final int screenHeight = getHeight();

		boolean isOnRight = getScrollY() > screenHeight * mCurrentScreen;
		boolean limitToSnap = Math.abs(getScrollY() - screenHeight
				* mCurrentScreen) > ViewConfiguration.get(getContext())
				.getScaledPagingTouchSlop();

		int whichScreen;
		if (isOnRight && limitToSnap)
			whichScreen = mCurrentScreen + 1;
		else if (!isOnRight && limitToSnap)
			whichScreen = mCurrentScreen - 1;
		else
			whichScreen = mCurrentScreen;

		snapToScreen(whichScreen);

		// snapToScreen(whichScreen);
	}

	private void snapToScreen(int whichScreen) {
		if (!mScroller.isFinished())
			return;

		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));

		mNextScreen = whichScreen;

		final int newY = whichScreen * getHeight();
		final int delta = newY - getScrollY();
		mScroller.startScroll(0, getScrollY(), 0, delta, Math.abs(delta));
		invalidate();
	}

	public void snapToSelectedScreen(int whichScreen) {
		if (!mScroller.isFinished())
			return;

		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));

		mNextScreen = whichScreen;

		final int newY = whichScreen * getHeight();
		final int delta = newY - getScrollY();
		mScroller.startScroll(0, getScrollY(), 0, delta,
				Math.abs(delta / getHeight()) * 100);
		invalidate();
	}

	public void snapFastToSelectedScreen(int whichScreen) {
		// if (!mScroller.isFinished())
		// return;

		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		mNextScreen = mCurrentScreen = whichScreen;

		final int newY = whichScreen * getMeasuredHeight();

		scrollTo(0, newY);

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

				if (mOnScreenSwitchListener != null) {
					mOnScreenSwitchListener.onScreenSwitched(mCurrentScreen,
							getChildAt(mCurrentScreen));
				}
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
		scrollTo(0, mCurrentScreen * getHeight());
		invalidate();
	}

}
