package padcms.magazine.controls.imagecontroller;

import padcms.magazine.controls.switcher.BaseRealViewSwitcher;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;

public class ImageViewTouch extends ImageViewTouchBase {

	static final float MIN_ZOOM = 0.9f;
	protected ScaleGestureDetector mScaleDetector;
	protected GestureDetector mGestureDetector;
	protected int mTouchSlop;
	protected float mCurrentScaleFactor;
	protected float mScaleFactor;
	protected int mDoubleTapDirection;
	protected GestureListener mGestureListener;
	protected ScaleListener mScaleListener;

	public static interface OnImageInZoomListener {
		void inZoomMode(int screen);
	}

	public ImageViewTouch(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	protected void init() {
		super.init();
		mTouchSlop = ViewConfiguration.getTouchSlop();
		mGestureListener = new GestureListener();
		mScaleListener = new ScaleListener();

		mScaleDetector = new ScaleGestureDetector(getContext(), mScaleListener);
		mGestureDetector = new GestureDetector(getContext(), mGestureListener,
				null, true);
		mCurrentScaleFactor = 1f;
		mDoubleTapDirection = 1;

	}

	@Override
	public void setImageRotateBitmapReset(RotateBitmap bitmap, boolean reset) {
		super.setImageRotateBitmapReset(bitmap, reset);
		mScaleFactor = getMaxZoom() / 3;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mScaleDetector.onTouchEvent(event);
		if (!mScaleDetector.isInProgress())
			mGestureDetector.onTouchEvent(event);

		int action = event.getAction();
		int pintsCount = event.getPointerCount();
		switch (action) {
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_DOWN:
			isPosibleScrollToRight = isPosibleScrollToLeft = 0;
			// if (isPosibleScrollToLeft == -1 || isPosibleScrollToRight == -1
			// || getScale() == 1f)
			callParentView(event);
			break;

		default:
			// if (getScale() == 1f) {
			if (pintsCount == 1) {
				if (isPosibleScrollToLeft == -1 || isPosibleScrollToRight == -1
						|| getScale() == 1f)
					callParentView(event);
			}
			break;
		}

		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_UP:
			if (getScale() < 1f) {
				zoomTo(1f, 50);
			}
			break;
		}

		return true;
	}

	private void callParentView(MotionEvent event) {
		if (isParentFliperExist)
			if (getParentView() != null) {
				getParentView().onTouchEvent(event);
			}
	}

	boolean isParentFliperExist = true;
	View parent;

	private View getParentView() {

		if (parent == null) {
			parent = getParentFliper((View) getParent());
		}
		return parent;
	}

	public View getParentFliper(View view) {

		if (view.getParent() == view.getRootView()) {
			isParentFliperExist = false;
			return null;
		}
		if (view.getParent() instanceof BaseRealViewSwitcher) {
			return (View) view.getParent();
		}
		if (view instanceof BaseRealViewSwitcher) {
			return (View) view;
		}

		return getParentFliper((View) view.getParent());
	}

	@Override
	protected void onZoom(float scale) {
		super.onZoom(scale);
		if (!mScaleDetector.isInProgress())
			mCurrentScaleFactor = scale;
	}

	protected float onDoubleTapPost(float scale, float maxZoom) {
		if (mDoubleTapDirection == 1) {
			if ((scale + (mScaleFactor * 2)) <= maxZoom) {
				return scale + mScaleFactor;
			} else {
				mDoubleTapDirection = -1;
				return maxZoom;
			}
		} else {
			mDoubleTapDirection = 1;
			return 1f;
		}
	}

	private int isPosibleScrollToLeft;
	private int isPosibleScrollToRight;

	class GestureListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			float scale = getScale();
			float targetScale = scale;
			targetScale = onDoubleTapPost(scale, getMaxZoom());
			targetScale = Math.min(getMaxZoom(),
					Math.max(targetScale, MIN_ZOOM));
			mCurrentScaleFactor = targetScale;
			zoomTo(targetScale, e.getX(), e.getY(), 200);
			invalidate();
			return super.onDoubleTap(e);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {

			if (e1 == null || e2 == null)
				return false;
			if (e1.getPointerCount() > 1 || e2.getPointerCount() > 1)
				return false;
			if (mScaleDetector.isInProgress())
				return false;
			if (getScale() == 1f)
				return false;
			if (!isBitmapSet())
				return false;
			boolean verticalScroll = Math.abs(distanceY) > Math.abs(distanceX);

			if (isPosibleScrollToLeft == 0 && distanceX < 0 && !verticalScroll)
				isPosibleScrollToLeft = isPosibleScrollImageToLeft(-distanceX,
						-distanceY) ? 1 : -1;
			boolean isPosibleLeftNow = isPosibleScrollImageToLeft(-distanceX,
					-distanceY);
			if (isPosibleScrollToLeft == 1 && !isPosibleLeftNow)
				isPosibleScrollToLeft = -1;

			if (isPosibleScrollToRight == 0 && distanceX > 0 && !verticalScroll)
				isPosibleScrollToRight = isPosibleScrollImageToRight(
						-distanceX, -distanceY) ? 1 : -1;
			boolean isPosibleRightNow = isPosibleScrollImageToRight(-distanceX,
					-distanceY);
			if (isPosibleScrollToRight == 1 && !isPosibleRightNow)
				isPosibleScrollToRight = -1;
			if (isPosibleScrollToLeft == -1)
				return false;
			if (isPosibleScrollToRight == -1)
				return false;

			scrollBy(-distanceX, -distanceY);

			invalidate();

			return super.onScroll(e1, e2, distanceX, distanceY);
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			// MenuController.getIstance(getContext()).showHideMenu();
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e1.getPointerCount() > 1 || e2.getPointerCount() > 1)
				return false;
			if (mScaleDetector.isInProgress())
				return false;
			invalidate();
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}

	class ScaleListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {

		@SuppressWarnings("unused")
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			float span = detector.getCurrentSpan() - detector.getPreviousSpan();
			float targetScale = mCurrentScaleFactor * detector.getScaleFactor();
			if (true) {
				targetScale = Math.min(getMaxZoom(),
						Math.max(targetScale, MIN_ZOOM));
				zoomTo(targetScale, detector.getFocusX(), detector.getFocusY());
				mCurrentScaleFactor = Math.min(getMaxZoom(),
						Math.max(targetScale, MIN_ZOOM));
				mDoubleTapDirection = 1;
				invalidate();
				return true;
			}
			return false;
		}
	}
}
