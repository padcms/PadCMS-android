package padcms.magazine.controls;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class SmoothFliperView extends FrameLayout {
	private RelativeLayout firstViewContainer;
	private RelativeLayout secondViewContainer;
	private RelativeContainerSmoothFliper parentViewContainer;
	private BaseSmoothFliperAdapter fliperAdapter;

	public static enum MoveSideAction {
		HORISONTAL_FROM_RIGHT_TO_LEFT, HORISONTAL_FROM_LEFT_TO_RIGHT, VERTICAL_FROM_BOTTOM_TO_TOP, VERTICAL_FROM_TOP_TO_BOTTOM
	}

	private int scrollPositionX;
	private int scrollPositionY;

	private int containerViewWidth = 0;
	private int containerViewHeight = 0;

	private boolean isFirstActive = true;

	private static final int NONE = -1;
	private static final int VERTICAL = 0;
	private static final int HORISONTAL = 1;

	private int moveDirection = NONE;
	private MoveSideAction moveDirectionSide;
	private MoveSideAction moveDirectionSideChanged;

	private float lastTouchX = 0;
	private float lastTouchY = 0;
	private float firstTouchX = 0;
	private float firstTouchY = 0;

	private boolean firstclick = true;
	private int limitMoveVertical;
	private int limitMoveHorisontal;
	private int positionStartScrollX;
	private int positionStartScrollY;

	private boolean isTouchAllow = true;

	private int flipMinWidth;
	private int flipMinHeight;
	private long longDuration = 300;
	private long shortDuration = 200;

	public SmoothFliperView(Context context) {
		super(context);
		// initViewLayoutParams();
	}

	public void initViewLayoutParams() {
		Display defaultDisplay = ((Activity) getContext()).getWindowManager()
				.getDefaultDisplay();
		// DisplayMetrics dm = new DisplayMetrics();
		// defaultDisplay.getMetrics(dm);

		scrollPositionX = 0;
		scrollPositionY = 0;
		positionStartScrollX = 0;
		positionStartScrollY = 0;

		isFirstActive = true;
		// Rect rectgle = new Rect();
		int buttom = ((Activity) getContext()).getWindow()
				.findViewById(Window.ID_ANDROID_CONTENT).getBottom();

		containerViewWidth = defaultDisplay.getWidth();
		containerViewHeight = defaultDisplay.getHeight();

		flipMinWidth = containerViewWidth / 10;
		flipMinHeight = containerViewHeight / 12;

		limitMoveHorisontal = ViewConfiguration.get(getContext())
				.getScaledTouchSlop();// containerViewWidth / 25;
		limitMoveVertical = ViewConfiguration.get(getContext())
				.getScaledTouchSlop();// containerViewHeight / 25;

		firstViewContainer = new RelativeLayout(getContext());
		firstViewContainer.setLayoutParams(new RelativeLayout.LayoutParams(
				containerViewWidth, -1));
		firstViewContainer.setGravity(Gravity.CENTER);

		secondViewContainer = new RelativeLayout(getContext());
		secondViewContainer.setLayoutParams(new RelativeLayout.LayoutParams(
				containerViewWidth, -1));
		secondViewContainer.setGravity(Gravity.CENTER);

		if (parentViewContainer != null)
			parentViewContainer.removeAllViews();

		parentViewContainer = new RelativeContainerSmoothFliper(getContext());

		addView(parentViewContainer, new LayoutParams(containerViewWidth, -1));

		requestLayout();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	public void setAdapter(BaseSmoothFliperAdapter adapter) {
		if (fliperAdapter != null) {
			if (parentViewContainer != null)
				parentViewContainer.removeAllViews();
			removeAllViews();
			fliperAdapter.cleanAdapter();
		}
		initViewLayoutParams();
		fliperAdapter = adapter;
		fliperAdapter.setSmoothFliperView(this);
	}

	@Override
	protected void onDetachedFromWindow() {
		try {
			super.onDetachedFromWindow();
		} catch (IllegalArgumentException e) {
			// Log.v("onDetachedFromWindow","onDetachedFromWindow");
		}
	}

	private void addViewLayout(View v) {
		RelativeLayout.LayoutParams rL = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT);
		rL.addRule(RelativeLayout.CENTER_IN_PARENT);

		if (v != null) {
			removeViewFromParentView(v);
			if (isFirstActive) {
				secondViewContainer.removeAllViews();
				secondViewContainer.addView(v);
			} else {
				firstViewContainer.removeAllViews();
				firstViewContainer.addView(v);
			}
		}
	}

	public void setFirstView(View v) {
		// RelativeLayout.LayoutParams rL = new RelativeLayout.LayoutParams(
		// android.view.ViewGroup.LayoutParams.FILL_PARENT,
		// android.view.ViewGroup.LayoutParams.FILL_PARENT);
		// rL.addRule(RelativeLayout.CENTER_IN_PARENT);
		removeViewFromParentView(firstViewContainer);

		RelativeLayout.LayoutParams rlP2 = new RelativeLayout.LayoutParams(
				containerViewWidth, containerViewHeight);

		firstViewContainer.setLayoutParams(rlP2);
		parentViewContainer.addView(firstViewContainer);
		firstViewContainer.bringToFront();

		if (v != null) {
			removeViewFromParentView(v);
			firstViewContainer.removeAllViews();
			firstViewContainer.addView(v);

		}
		isFirstActive = true;
	}

	private void removeViewFromParentView(View v) {
		if (v != null) {
			if (v.getParent() != null) {
				((ViewGroup) v.getParent()).removeView(v);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean touchReturn = true;

		if (isTouchAllow) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				firstTouchX = lastTouchX = event.getRawX();
				firstTouchY = lastTouchY = event.getRawY();

				firstclick = true;

				moveDirection = NONE;
				touchReturn = true;

				break;
			case MotionEvent.ACTION_MOVE:
				if ((moveDirection == NONE)) {
					if (lastTouchX == firstTouchX) {
						touchReturn = super.onTouchEvent(event);
					} else {
						float deltaXAbs = Math.abs(firstTouchX
								- event.getRawX());
						float deltaYAbs = Math.abs(firstTouchY
								- event.getRawY());

						if (deltaXAbs > limitMoveHorisontal) {
							moveDirection = HORISONTAL;
							if (event.getRawX() < firstTouchX) {
								moveDirectionSide = moveDirectionSideChanged = MoveSideAction.HORISONTAL_FROM_RIGHT_TO_LEFT;
							} else {
								moveDirectionSide = moveDirectionSideChanged = MoveSideAction.HORISONTAL_FROM_LEFT_TO_RIGHT;
							}
							changeSideView();

						} else if ((deltaYAbs) > limitMoveVertical) {
							moveDirection = VERTICAL;
							if (event.getRawY() < firstTouchY) {
								moveDirectionSide = moveDirectionSideChanged = MoveSideAction.VERTICAL_FROM_BOTTOM_TO_TOP;
							} else {
								moveDirectionSide = moveDirectionSideChanged = MoveSideAction.VERTICAL_FROM_TOP_TO_BOTTOM;
							}
							changeSideView();

						} else {
							touchReturn = super.onTouchEvent(event);
							moveDirection = NONE;
						}
					}
				}

				if (moveDirection == HORISONTAL) {

					float deltaX = event.getRawX() - lastTouchX;
					float deltaY = event.getRawY() - lastTouchY;
					if ((deltaX) <= 0) {
						moveDirectionSideChanged = MoveSideAction.HORISONTAL_FROM_RIGHT_TO_LEFT;
					} else {
						moveDirectionSideChanged = MoveSideAction.HORISONTAL_FROM_LEFT_TO_RIGHT;
					}

					if (!firstclick) {
						float offsetx = (((event.getRawX() - lastTouchX) - (event
								.getRawX() - lastTouchX)) * -1);
						float offsety = (((event.getRawY() - lastTouchY) - (event
								.getRawY() - lastTouchY)) * -1);
						event.offsetLocation(offsetx, offsety);
					}

					slideViewTo((int) deltaX, (int) deltaY);
					touchReturn = super.onTouchEvent(event);

					firstclick = false;

				} else if (moveDirection == VERTICAL) {

					float ydiff = event.getRawY() - lastTouchY;
					float xdiff = event.getRawX() - lastTouchX;

					if ((ydiff) < 0) {
						moveDirectionSideChanged = MoveSideAction.VERTICAL_FROM_BOTTOM_TO_TOP;
					} else {
						moveDirectionSideChanged = MoveSideAction.VERTICAL_FROM_TOP_TO_BOTTOM;
					}

					slideViewTo((int) xdiff, (int) ydiff);
					touchReturn = super.onTouchEvent(event);

				}

				break;
			case MotionEvent.ACTION_UP:
				touchReturn = true;// super.onTouchEvent(ev);

				if (moveDirection == VERTICAL) {
					if (lastTouchY < firstTouchY) {
						moveDirectionSide = MoveSideAction.VERTICAL_FROM_BOTTOM_TO_TOP;
					} else {
						moveDirectionSide = MoveSideAction.VERTICAL_FROM_TOP_TO_BOTTOM;
					}
					setOnFlingView();
				} else if (moveDirection == HORISONTAL) {
					if (lastTouchX < firstTouchX) {
						moveDirectionSide = MoveSideAction.HORISONTAL_FROM_RIGHT_TO_LEFT;
					} else {
						moveDirectionSide = MoveSideAction.HORISONTAL_FROM_LEFT_TO_RIGHT;
					}
					setOnFlingView();
				} else {
					float deltaX = Math.abs(firstTouchX - event.getRawX());
					float deltaY = Math.abs(firstTouchY - event.getRawY());
					if (deltaX < 5 && deltaY < 5)
						fliperAdapter.onViewClicked();
				}
				break;
			default:
				touchReturn = super.onTouchEvent(event);
				break;
			}

			lastTouchX = event.getRawX();
			lastTouchY = event.getRawY();

		} else
			return false;
		return touchReturn;
	}

	private void slideViewTo(int deltaX, int deltaY) {
		int containerScrollX = parentViewContainer.getScrollX()
				- scrollPositionX;
		int containerScrollY = parentViewContainer.getScrollY()
				- scrollPositionY;
		Log.i(this.getClass().getName(), "slideViewTo:" + moveDirectionSide
				+ " scrollX:" + containerScrollX);

		switch (moveDirectionSide) {

		case HORISONTAL_FROM_RIGHT_TO_LEFT:
			if (moveDirectionSide != moveDirectionSideChanged) {

				if (containerScrollX <= 0) {
					moveDirectionSide = moveDirectionSideChanged;
					if (fliperAdapter.allowToFlip(moveDirectionSide))
						changeSideView();
				} else
					parentViewContainer.scrollBy(-deltaX, 0);

			} else if (fliperAdapter.allowToFlip(moveDirectionSide))
				parentViewContainer.scrollBy(-deltaX, 0);
			break;

		case HORISONTAL_FROM_LEFT_TO_RIGHT:

			if (moveDirectionSide != moveDirectionSideChanged) {
				if (containerScrollX >= 0) {
					moveDirectionSide = moveDirectionSideChanged;
					if (fliperAdapter.allowToFlip(moveDirectionSide))
						changeSideView();

				} else
					parentViewContainer.scrollBy(-deltaX, 0);

			} else if (fliperAdapter.allowToFlip(moveDirectionSide))
				parentViewContainer.scrollBy(-deltaX, 0);
			break;

		case VERTICAL_FROM_BOTTOM_TO_TOP:

			if (moveDirectionSide != moveDirectionSideChanged) {
				if (containerScrollY <= 0) {
					moveDirectionSide = moveDirectionSideChanged;
					if (fliperAdapter.allowToFlip(moveDirectionSide))
						changeSideView();

				} else
					parentViewContainer.scrollBy(0, -deltaY);
			} else if (fliperAdapter.allowToFlip(moveDirectionSide))
				parentViewContainer.scrollBy(0, -deltaY);
			break;

		case VERTICAL_FROM_TOP_TO_BOTTOM:

			if (moveDirectionSide != moveDirectionSideChanged) {
				if (containerScrollY >= 0) {
					moveDirectionSide = moveDirectionSideChanged;
					if (fliperAdapter.allowToFlip(moveDirectionSide))
						changeSideView();

				} else
					parentViewContainer.scrollBy(0, -deltaY);

			} else if (fliperAdapter.allowToFlip(moveDirectionSide))
				parentViewContainer.scrollBy(0, -deltaY);
			break;

		}
	}

	private void changeSideView() {
		Log.i(this.getClass().getName(), "changeSideView:" + moveDirectionSide);
		positionStartScrollX = parentViewContainer.getScrollX();
		positionStartScrollY = parentViewContainer.getScrollY();

		Log.i(this.getClass().getName() + "#####################",
				"positionStartScrollX:" + positionStartScrollX);
		if (fliperAdapter.allowToFlip(moveDirectionSide)) {
			View view = fliperAdapter.getSideView(moveDirectionSide);
			switch (moveDirectionSide) {

			case HORISONTAL_FROM_RIGHT_TO_LEFT:
				setLeftSideView();
				break;

			case HORISONTAL_FROM_LEFT_TO_RIGHT:
				setRightSideView();
				break;

			case VERTICAL_FROM_BOTTOM_TO_TOP:
				setTopSideView();
				break;

			case VERTICAL_FROM_TOP_TO_BOTTOM:
				setBottomSideView();
				break;

			}
			addViewLayout(view);
		}
	}

	public void changeSideView(View view, MoveSideAction moveDirectionToSide) {
		Log.i(this.getClass().getName(), "changeSideView:" + moveDirectionSide);
		positionStartScrollX = parentViewContainer.getScrollX();
		positionStartScrollY = parentViewContainer.getScrollY();

		switch (moveDirectionToSide) {

		case HORISONTAL_FROM_RIGHT_TO_LEFT:
			setLeftSideView();
			break;

		case HORISONTAL_FROM_LEFT_TO_RIGHT:
			setRightSideView();
			break;

		}
		addViewLayout(view);

	}

	private void setBottomSideView() {
		View actionView = null;
		if (isFirstActive) {
			actionView = secondViewContainer;
		} else
			actionView = firstViewContainer;

		if (positionStartScrollY > 0) {
			removeViewFromParentView(actionView);

			RelativeLayout.LayoutParams rlP2 = new RelativeLayout.LayoutParams(
					containerViewWidth, containerViewHeight);
			rlP2.setMargins(scrollPositionX, scrollPositionY
					- containerViewHeight, 0, 0);

			parentViewContainer.setLayoutParams(new LayoutParams(
					scrollPositionX + containerViewWidth * 2, scrollPositionY
							+ containerViewHeight * 2));

			actionView.setLayoutParams(rlP2);

			parentViewContainer.addView(actionView);
			actionView.bringToFront();
		}

	}

	private void setTopSideView() {
		View actionView = null;
		if (isFirstActive) {
			actionView = secondViewContainer;
		} else
			actionView = firstViewContainer;

		removeViewFromParentView(actionView);

		RelativeLayout.LayoutParams rlP2 = null;
		rlP2 = new RelativeLayout.LayoutParams(containerViewWidth,
				containerViewHeight);
		rlP2.setMargins(scrollPositionX, scrollPositionY + containerViewHeight,
				0, 0);
		parentViewContainer.setLayoutParams(new LayoutParams(scrollPositionX
				+ containerViewWidth * 2, scrollPositionY + containerViewHeight
				* 2));
		actionView.setLayoutParams(rlP2);

		parentViewContainer.addView(actionView);
		actionView.bringToFront();

	}

	private void setRightSideView() {
		View actionView = null;
		View inactionView = null;
		if (isFirstActive) {
			actionView = secondViewContainer;
			inactionView = firstViewContainer;
		} else {
			actionView = firstViewContainer;
			inactionView = secondViewContainer;
		}

		if (positionStartScrollX <= 0) {
			scrollPositionX += containerViewWidth;
			RelativeLayout.LayoutParams rlP1 = new RelativeLayout.LayoutParams(
					containerViewWidth, containerViewHeight);
			rlP1.setMargins(scrollPositionX, scrollPositionY, 0, 0);
			inactionView.setLayoutParams(rlP1);
			parentViewContainer.scrollTo(scrollPositionX, scrollPositionY);
		}
		// else
		{

			removeViewFromParentView(actionView);

			RelativeLayout.LayoutParams rlP2 = new RelativeLayout.LayoutParams(
					containerViewWidth, containerViewHeight);
			rlP2.setMargins(scrollPositionX - containerViewWidth,
					scrollPositionY, 0, 0);
			parentViewContainer.setLayoutParams(new LayoutParams(
					scrollPositionX + containerViewWidth * 2, scrollPositionY
							+ containerViewHeight * 2));
			actionView.setLayoutParams(rlP2);
			parentViewContainer.addView(actionView);
			actionView.bringToFront();
		}

	}

	private void setLeftSideView() {
		View actionView = null;
		if (isFirstActive) {
			actionView = secondViewContainer;
		} else
			actionView = firstViewContainer;

		removeViewFromParentView(actionView);

		RelativeLayout.LayoutParams rlP2 = null;
		rlP2 = new RelativeLayout.LayoutParams(containerViewWidth,
				containerViewHeight);
		rlP2.setMargins(scrollPositionX + containerViewWidth, scrollPositionY,
				0, 0);

		parentViewContainer.setLayoutParams(new LayoutParams(scrollPositionX
				+ containerViewWidth * 2, scrollPositionY + containerViewHeight
				* 2));

		actionView.setLayoutParams(rlP2);
		parentViewContainer.addView(actionView);
		actionView.bringToFront();
	}

	private void setOnFlingView() {

		int scrollX = Math.abs(scrollPositionX)
				- Math.abs(parentViewContainer.getScrollX());
		int scrollY = Math.abs(scrollPositionY)
				- Math.abs(parentViewContainer.getScrollY());

		boolean allowToFlip = fliperAdapter.allowToFlip(moveDirectionSide);

		if (moveDirection != NONE) {

			switch (moveDirectionSide) {

			case HORISONTAL_FROM_RIGHT_TO_LEFT:

				if (Math.abs(scrollX) < flipMinWidth || !allowToFlip) {

					makeAnimationOnFling(-Math.abs(scrollX), 0, 0, 0,
							scrollPositionX, scrollPositionY, true, true,
							shortDuration);

					isTouchAllow = true;

				} else {

					makeAnimationOnFling(0, -(containerViewWidth + scrollX), 0,
							0, scrollPositionX + containerViewWidth,
							scrollPositionY, true, false, longDuration);

					scrollPositionX += containerViewWidth;

					setActiveView();
				}

				break;

			case HORISONTAL_FROM_LEFT_TO_RIGHT:

				if (Math.abs(scrollX) < flipMinWidth || !allowToFlip) {

					makeAnimationOnFling(0, -Math.abs(scrollX), 0, 0,
							scrollPositionX, scrollPositionY, true, false,
							shortDuration);

					isTouchAllow = true;
				} else {

					makeAnimationOnFling(-(containerViewWidth - scrollX), 0, 0,
							0, scrollPositionX - containerViewWidth,
							scrollPositionY, true, true, longDuration);

					scrollPositionX -= containerViewWidth;
					setActiveView();
				}
				break;

			case VERTICAL_FROM_BOTTOM_TO_TOP:

				if (Math.abs(scrollY) < flipMinHeight || !allowToFlip) {

					makeAnimationOnFling(0, 0, -Math.abs(scrollY), 0,
							scrollPositionX, scrollPositionY, false, true,
							shortDuration);

					isTouchAllow = true;

				} else {

					makeAnimationOnFling(0, 0, 0,
							-(containerViewHeight + scrollY), scrollPositionX,
							scrollPositionY + containerViewHeight, false,
							false, longDuration);

					scrollPositionY += containerViewHeight;
					setActiveView();

				}
				break;

			case VERTICAL_FROM_TOP_TO_BOTTOM:
				if (Math.abs(scrollY) < flipMinHeight || !allowToFlip) {

					makeAnimationOnFling(0, 0, 0, -Math.abs(scrollY),
							scrollPositionX, scrollPositionY, false, false,
							shortDuration);

					isTouchAllow = true;
				} else {

					makeAnimationOnFling(0, 0,
							-(containerViewHeight - scrollY), 0,
							scrollPositionX, scrollPositionY
									- containerViewHeight, false, true,
							longDuration);

					scrollPositionY -= containerViewHeight;
					setActiveView();
				}
				break;
			}
		}
	}

	public void flipToNextView(MoveSideAction moveDirectionToSide) {

		int scrollX = Math.abs(scrollPositionX)
				- Math.abs(parentViewContainer.getScrollX());

		moveDirectionSide = moveDirectionToSide;

		switch (moveDirectionToSide) {

		case HORISONTAL_FROM_RIGHT_TO_LEFT:

			makeAnimationOnFling(0, -(containerViewWidth + scrollX), 0, 0,
					scrollPositionX + containerViewWidth, scrollPositionY,
					true, false, longDuration);

			scrollPositionX += containerViewWidth;

			setActiveView();

			break;

		case HORISONTAL_FROM_LEFT_TO_RIGHT:

			makeAnimationOnFling(-(containerViewWidth - scrollX), 0, 0, 0,
					scrollPositionX - containerViewWidth, scrollPositionY,
					true, true, longDuration);

			scrollPositionX -= containerViewWidth;
			setActiveView();
			break;

		}
	}

	public void flipAnimationStart(boolean flipToFinish) {
		isTouchAllow = false;
		if (flipToFinish)
			fliperAdapter.startFlipAnimation();
	}

	public void flipAnimationEnd(boolean flipToFinish) {
		fliperAdapter.activationView(moveDirectionSide, flipToFinish);
		isTouchAllow = true;

	}

	private void setActiveView() {
		if (isFirstActive) {
			isFirstActive = false;
		} else {
			isFirstActive = true;
		}
	}

	private void makeAnimationOnFling(int fromX, int toX, int fromY, int toY,
			final int scrollX, final int scrollY, boolean isHorisontal,
			final boolean isMakeScrollBefore, long duration) {
		Log.i("################ makeAnimationOnFling", "fromX:" + fromX
				+ "toX:" + toX + "scrollX:" + scrollX);
		int scrollParam = 0;
		if (isHorisontal)
			scrollParam = scrollPositionX;
		else
			scrollParam = scrollPositionY;

		if (scrollParam >= 0) {
			Animation animation = new TranslateAnimation(fromX, toX, fromY, toY);
			animation.setDuration(duration);

			if (duration == longDuration) {
				animation.setInterpolator(new DecelerateInterpolator());
				parentViewContainer.flipToFinish = true;
			} else {
				animation.setInterpolator(new AccelerateInterpolator());
				parentViewContainer.flipToFinish = false;
			}
			parentViewContainer.scrollToPositionX = scrollX;
			parentViewContainer.scrollToPositionY = scrollY;

			if (isMakeScrollBefore)
				parentViewContainer.makeScroll();

			parentViewContainer.startAnimation(animation);

		} else {
			parentViewContainer.scrollTo(scrollX, scrollY);
		}
		isTouchAllow = false;
	}
}
