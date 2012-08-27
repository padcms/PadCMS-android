package padcms.magazine.controls.imagecontroller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import padcms.magazine.controls.switcher.BaseRealViewSwitcher;
import padcms.magazine.factory.IssueViewFactory;
import padcms.magazine.page.elementview.ActiveZoneElementDataView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class ImageViewGroup extends ViewGroup {
	private GestureDetector mGestureDetector;
	private GestureListener mGestureListener;
	protected Scroller mScroller;
	protected final static int TOUCH_STATE_REST = 0;
	protected final static int TOUCH_STATE_SCROLLING_HORISONTAL = 1;
	protected final static int TOUCH_STATE_SCROLLING_VERTICAL = 2;

	protected final static int SCROLL_MODE_LEFT = -1;
	protected final static int SCROLL_MODE_RIGHT = 1;
	protected final static int SCROLL_MODE_TOP = -2;
	protected final static int SCROLL_MODE_BOTTOM = 2;
	protected final static int SCROLL_MODE_NONE = 1;
	protected int mTouchState = TOUCH_STATE_REST;
	protected int mModeSide;
	protected float mLastMotionX;
	protected float mLastMotionY;
	protected float mFirstMotionX;
	protected float mFirstMotionY;
	protected int mTouchSlop;
	protected int mMaximumVelocity;
	private VelocityTracker mVelocityTracker;
	private ViewGroup parentViewSwitcher;
	private boolean isNoParent;
	private File imageFolder;
	private ArrayList<ImageViewPart> imageViewParts;
	private int fullHeight = 0;
	private int fullWidth = 0;
	int widthPart;
	int heightPart;
	private int bgColor = Color.TRANSPARENT;
	private int state;
	public static final int STATE_ORIGINAL = 1;
	public static final int STATE_SCALED = 2;
	public static final int STATE_RELEASE = 0;
	private OnClickListener mOnClickListener;
	private static final ExecutorService executorActive = Executors
			.newScheduledThreadPool(2);
	private static final ExecutorService executor = Executors
			.newScheduledThreadPool(1);
	private Handler handler = new Handler();
	private boolean isScrollable = true;

	private boolean isChenged = false;

	public ImageViewGroup(Context context) {
		super(context);
		mGestureListener = new GestureListener();
		mScroller = new Scroller(getContext());

		mGestureDetector = new GestureDetector(getContext(), mGestureListener,
				null, true);
		final ViewConfiguration configuration = ViewConfiguration
				.get(getContext());
		mTouchSlop = ViewConfiguration.getWindowTouchSlop();

		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
	}

	ArrayList<View> activeZoneView = new ArrayList<View>();

	@Override
	public void addView(View child) {
		super.addView(child);
		if (child instanceof ActiveZoneElementDataView) {
			activeZoneView.add(child);
		}

	}

	public boolean isScrollable() {
		return isScrollable;
	}

	public void setScrollable(boolean isScrollable) {
		this.isScrollable = isScrollable;
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		this.mOnClickListener = l;
		super.setOnClickListener(l);
	}

	public void setImagePath(String imagePath) {
		if (imageFolder == null
				|| !imageFolder.getAbsolutePath().equals(imagePath)) {
			isChenged = true;
			if (imageViewParts != null) {
				for (ImageViewPart image : imageViewParts) {
					image.setState(STATE_RELEASE);
					image.release();
					if (image.getParent() != null) {
						((ViewGroup) image.getParent()).removeView(image);
					}
				}
			}

			imageFolder = new File(imagePath);

			imageViewParts = new ArrayList<ImageViewPart>();

			if (imageFolder.isDirectory()) {
				for (String imageFilePath : imageFolder.list()) {
					if (!imageFilePath.toLowerCase().startsWith("bq")) {
						ImageViewPart imageViewPart = new ImageViewPart(
								getContext(), new File(imageFolder,
										imageFilePath));
						imageViewParts.add(imageViewPart);
						addView(imageViewPart);
					}
				}

				Collections.sort(imageViewParts);
			}
			drowRect(getScrollY());

		}
	}

	@Override
	public void setBackgroundColor(int color) {
		bgColor = color;

	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		if (imageViewParts != null && imageViewParts.size() > 0
				&& (fullWidth == 0 || isChenged)) {

			ImageViewPart imageViewPartFist = imageViewParts.get(0);
			ImageViewPart imageViewPartLast = imageViewParts.get(imageViewParts
					.size() - 1);

			int columsCount = imageViewPartLast.column;
			int rowsCount = imageViewPartLast.row;
			fullWidth = 0;
			for (int i = 0; i < columsCount; i++) {
				fullWidth += imageViewParts.get(i).getOriginalBitmapOption().outWidth;
			}

			// for (int i = 0; i < rowsCount; i++) {
			// fullWidth +=
			// imageViewParts.get(i).getOriginalBitmapOption().outWidth;
			// if ((imageView.getRow()) == rowsCount && fullHeight == 0) {
			//
			// bottom;
			// } }

			float scaleFullWidth = 1;
			int leftMargin = ((RelativeLayout.LayoutParams) getLayoutParams()).leftMargin;
			int topMargin = ((RelativeLayout.LayoutParams) getLayoutParams()).topMargin;

			if (fullWidth != getMeasuredWidth() + leftMargin) {
				scaleFullWidth = fullWidth
						/ ((getMeasuredWidth() + leftMargin) * 1.f);
			}
			widthPart = (int) (imageViewPartFist.getOriginalBitmapOption().outWidth / scaleFullWidth);
			heightPart = (int) (imageViewPartFist.getOriginalBitmapOption().outHeight / scaleFullWidth);

			fullHeight = (rowsCount - 1)
					* heightPart
					+ (int) (imageViewPartLast.getOriginalBitmapOption().outHeight / scaleFullWidth);

			int startPositionY = -topMargin;
			int startPositionX = -leftMargin;
			if (fullHeight < getMeasuredHeight()) {
				startPositionY += (getMeasuredHeight() - fullHeight) / 2;
			}

			for (ImageViewPart imageView : imageViewParts) {

				Options options = imageView.getOriginalBitmapOption();

				// ResourceResolutionHelper resolutionHelper =
				// ResourceResolutionHelper
				// .getBitmapResolutionByWidth(scaledPartWidth,
				// options.outWidth, options.outHeight);
				int width = (int) (options.outWidth / scaleFullWidth);
				int height = (int) (options.outHeight / scaleFullWidth);
				// if (options.outWidth < 256) {
				// int lastPartSize=fullWidth-256*(columsCount-1);
				// resolutionHelper = ResourceResolutionHelper
				// .getBitmapResolutionByWidth(scaledPartWidth*,
				// options.outWidth, options.outHeight);
				// } else {
				//
				// }

				int left = startPositionX + (imageView.getColumn() - 1)
						* widthPart;
				int right = left + width;
				int top = startPositionY + (imageView.getRow() - 1)
						* heightPart;
				int bottom = top + height;

				imageView.layout(left, top, right, bottom);
				imageView.setActiveBgColor(bgColor);
			}
			for (View view : activeZoneView) {
				view.layout(view.getLeft(), view.getTop() + startPositionY,
						view.getRight(), view.getBottom() + startPositionY);
				view.bringToFront();
			}
			isChenged = false;
			drowRect(getScrollY());
		}

	}

	public void relese() {
		state = STATE_RELEASE;
		parentViewSwitcher = null;
		if (imageViewParts != null)
			for (ImageViewPart imageView : imageViewParts) {
				if (state == STATE_RELEASE) {

					imageView.setState(STATE_RELEASE);

					// if (executor == null || executor.isShutdown())
					// executor = Executors.newScheduledThreadPool(1);
					executor.execute(imageView.getrunnableTask());
				}

			}
	}

	public void releseInactive() {
		if (imageViewParts != null)
			for (final ImageViewPart imageView : imageViewParts) {
				if (imageView.getTop() < getScrollY() - heightPart
						|| imageView.getBottom() - heightPart > getScrollY()
								+ getHeight()) {

					imageView.setState(STATE_RELEASE);
					imageView.release();
				}

			}

	}

	public void drowOriginal() {
		if (imageViewParts != null) {
			state = STATE_ORIGINAL;
			for (final ImageViewPart imageView : imageViewParts) {
				imageView.setState(STATE_ORIGINAL);
			}
			drowRect(getScrollY());
		}

	}

	public void drowScale() {
		if (imageViewParts != null) {
			state = STATE_SCALED;
			for (final ImageViewPart imageView : imageViewParts) {
				imageView.setState(STATE_ORIGINAL);
			}
			drowRect(getScrollY());
		}
	}

	private void drowRect(int top) {
		if (imageViewParts != null) {
			for (final ImageViewPart imageView : imageViewParts) {
				float height = heightPart * 5 / 3.f;
				if (state == STATE_SCALED)
					height = heightPart;

				if (imageView.getTop() < top - height
						|| imageView.getBottom() - height > top + getHeight()) {
					imageView.release();
				} else {
					if (state == STATE_ORIGINAL)
						executorActive.execute(imageView.getrunnableTask());
					if (state == STATE_SCALED) {
						executor.execute(imageView.getrunnableTask());
					}
				}
			}

		}
	}

	class ImageViewPart extends ImageViewResources implements
			Comparable<ImageViewPart> {

		private BitmapPart bitmapPart;
		private int stateDrowTo;
		private int stateDrowAlrady;
		private int row;
		private int column;
		private int activeBgColor;

		// private DrowImageTask drowAsyncTask;

		public ImageViewPart(Context context, File imageFile) {
			super(context);

			this.bitmapPart = new BitmapPart(imageFile);
			setScaleType(ScaleType.FIT_XY);
			String fileName = imageFile.getName().replace(".jpg", "").replace(".png", "");;
			String[] arrayArgument = fileName.split("_");
			this.row = Integer.valueOf(arrayArgument[1]);
			this.column = Integer.valueOf(arrayArgument[2]);

			// drowAsyncTask = new DrowImageTask();
		}

		public void setState(int state) {
			this.stateDrowTo = state;
		}

		public Runnable getrunnableTask() {
			return runnableTask;
		}

		public int getActiveBgColor() {
			return activeBgColor;
		}

		public void setActiveBgColor(int activeBgColor) {
			this.activeBgColor = activeBgColor;
			super.setBackgroundColor(activeBgColor);
		}

		public Options getOriginalBitmapOption() {
			Options options = bitmapPart.getImageOptions();
			return options;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public int getColumn() {
			return column;
		}

		public void setColumn(int column) {
			this.column = column;
		}

		public void setBitmapPart(BitmapPart bitmapPart) {
			this.bitmapPart = bitmapPart;
		}

		private Runnable runnableTask = new Runnable() {

			@Override
			public void run() {
				if (stateDrowTo == STATE_ORIGINAL) {
					bitmapPart.setState(STATE_ORIGINAL);
					try {
						if (stateDrowTo != stateDrowAlrady) {
							drowBitmapPart();
							stateDrowAlrady = STATE_ORIGINAL;
						}
					} catch (OutOfMemoryError e) {
						Log.e("OutOfMemoryError ", "error:" + e.getMessage());
						try {

							IssueViewFactory
									.getIssueViewFactoryIstance(getContext())
									.getVerticalPageSwitcherController()
									.releaseInactive();
							// releseInactive();

							drowBitmapPart();
							stateDrowAlrady = STATE_ORIGINAL;
						} catch (OutOfMemoryError error) {
							Log.e("OutOfMemoryError ",
									"error2:" + e.getMessage());
							stateDrowAlrady = STATE_RELEASE;
							release();

						}
					}
				} else if (stateDrowTo == STATE_SCALED) {
					bitmapPart.setState(STATE_SCALED);
					try {
						if (stateDrowTo != stateDrowAlrady)
							drowBitmapPart();
						stateDrowAlrady = STATE_SCALED;
					} catch (OutOfMemoryError e) {
						stateDrowAlrady = STATE_RELEASE;
						release();

					}
				} else {
					release();
					stateDrowAlrady = STATE_RELEASE;
				}
			}
		};

		private void drowBitmapPart() throws OutOfMemoryError {

			final Bitmap bitmap = bitmapPart.getBitmap();

			handler.post(new Runnable() {

				@Override
				public void run() {
					setImageBitmap(bitmap);
					setBackgroundColor(activeBgColor);
					bitmapPart.release();
				}
			});

		}

		public void release() {

			bitmapPart.setState(STATE_RELEASE);

			handler.post(new Runnable() {
				public void run() {
					setImageBitmap(null);
					setBackgroundColor(Color.TRANSPARENT);
				};

			});

			bitmapPart.release();
			stateDrowAlrady = STATE_RELEASE;

		}

		@Override
		public int compareTo(ImageViewPart another) {
			int rezalt = 0;
			if (row > another.row) {
				rezalt = 1;
			} else if (row < another.row)
				rezalt = -1;
			else {
				if (column > another.column)
					rezalt = 1;
				else if (column < another.column)
					rezalt = -1;
			}
			return rezalt;
		}

	}

	class BitmapPart {
		private final static int SCALE = 4;
		private Bitmap bitmap;
		private Bitmap scaledBitmap;
		private File imageFile;
		private Options outOption;
		public static final int STATE_ORIGINAL = 1;
		public static final int STATE_SCALED = 2;
		public static final int STATE_RELEASE = 0;
		private int currentState;

		public BitmapPart(File imageFile) {
			this.imageFile = imageFile;

		}

		public void setState(int state) {
			currentState = state;
		}

		// public Bitmap getScaledBitmap() {
		// if (scaledBitmap == null) {
		// scaledBitmap = decodeScaledBitmap();
		// releseBitmap();
		// }
		//
		// return scaledBitmap;
		// }

		public Bitmap getBitmap() throws OutOfMemoryError {
			if (currentState == STATE_ORIGINAL) {
				if (bitmap == null) {
					bitmap = decodeBitmap();
					scaledBitmap = null;
					// releseScaledBitmap();
					return bitmap;
				}
			}
			if (currentState == STATE_SCALED) {
				if (scaledBitmap == null) {
					scaledBitmap = decodeScaledBitmap();
					bitmap = null;
					// releseBitmap();
					return scaledBitmap;
				}
			}
			return null;
		}

		public Options getImageOptions() {
			if (outOption == null) {
				outOption = new Options();
				outOption.inJustDecodeBounds = true;
				try {
					BitmapFactory.decodeFile(imageFile.getAbsolutePath(),
							outOption);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return outOption;

		}

		private Bitmap decodeBitmap() throws OutOfMemoryError {

			Options option = new Options();

			return BitmapFactory
					.decodeFile(imageFile.getAbsolutePath(), option);

		}

		private Bitmap decodeScaledBitmap() {
			Options option = new Options();
			option.inSampleSize = SCALE;
			try {
				return BitmapFactory.decodeFile(imageFile.getAbsolutePath(),
						option);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		public void release() {
			if (currentState != STATE_ORIGINAL)
				releseBitmap();
			if (currentState != STATE_SCALED)
				releseScaledBitmap();
		}

		private void releseScaledBitmap() {
			if (scaledBitmap != null && !scaledBitmap.isRecycled()) {
				scaledBitmap.recycle();
				scaledBitmap = null;
			}
		}

		private void releseBitmap() {
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
		}

	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {

		if (t != oldt) {

			if (state == STATE_ORIGINAL) {
				super.onScrollChanged(l, t, oldl, oldt);
				drowRect(t);
			} else {
				mScroller.abortAnimation();

			}
		}
	}

	// @Override
	// protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	// // final int width = MeasureSpec.getSize(widthMeasureSpec);
	// final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	// if (widthMode != MeasureSpec.EXACTLY) {
	// throw new IllegalStateException(
	// "ViewSwitcher can only be used in EXACTLY mode.");
	// }
	//
	// final int height = MeasureSpec.getSize(heightMeasureSpec);
	//
	// final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	// // if (heightMode != MeasureSpec.EXACTLY) {
	// // throw new IllegalStateException(
	// // "ViewSwitcher can only be used in EXACTLY mode.");
	// // }
	//
	// // The children are given the same width and height as the workspace
	// // final int count = getChildCount();
	// // for (int i = 0; i < count; i++) {
	// // getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
	// // }
	//
	// // if (mFirstLayout) {
	// // scrollTo(0, mCurrentScreen * height);
	// // mFirstLayout = false;
	// // }
	// }

	// @Override
	// protected void onLayout(boolean changed, int left, int top, int right,
	// int bottom) {
	// Log.i(ImageViewController.class.getName() + "  c:" + changed, "l:"
	// + left + " r:" + right + " t:" + top + " b:" + bottom);
	// int childTop = 0;
	// final int count = getChildCount();
	// for (int i = 0; i < count; i++) {
	// final View child = getChildAt(i);
	// if (child.getVisibility() != View.GONE) {
	// final int childHeight = child.getMeasuredHeight();
	// child.layout(0, childTop, child.getMeasuredWidth(), childTop
	// + childHeight);
	// childTop += childHeight;
	// }
	// }
	// }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Log.i("###### ImageViewController.onTouchEvent ######", "event:"
		// + event.getAction() + " x:" + event.getRawX());
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		mGestureDetector.onTouchEvent(event);

		final int action = event.getAction();
		final float x = event.getRawX();
		final float y = event.getRawY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!mScroller.isFinished()) {

				mScroller.abortAnimation();

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
				} else if (yMoved) {
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
				if (isScrollable) {
					if (deltaY < 0) {
						if (scrollY > 0) {
							scrollBy(0, Math.max(-scrollY, deltaY));
						} else
							mTouchState = TOUCH_STATE_REST;
					} else if (deltaY > 0) {
						int availableToScroll = fullHeight - scrollY
								- getHeight();

						if (availableToScroll > 0) {
							scrollBy(0, Math.min(availableToScroll, deltaY));
						} else
							mTouchState = TOUCH_STATE_REST;
					}
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
				// snapToDestination();
				// }

			}
			if (mTouchState == TOUCH_STATE_SCROLLING_HORISONTAL) {
				// snapToDestination();

			}
			if (mTouchState == TOUCH_STATE_REST
					|| mTouchState == TOUCH_STATE_SCROLLING_HORISONTAL)
				// if (Math.abs(mFirstMotionY - mLastMotionY) <
				// ViewConfiguration
				// .getTouchSlop())
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

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset() && isScrollable) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
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

	class GestureListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			return super.onDoubleTap(e);
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			if (mOnClickListener != null) {
				mOnClickListener.onClick(ImageViewGroup.this);
			}
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			Log.i("onFling", "e1:" + e1.getRawY() + "e2:" + e2.getRawY()
					+ " velocityY:" + velocityY);
			int availableToScroll = (int) (fullHeight - getScrollX() - getHeight());

			if (availableToScroll > 0 && isScrollable) {
				mScroller.fling(getScrollX(), getScrollY(), 0, (int) velocityY
						* -1, 0, 0, 0, availableToScroll);
				invalidate();
			}

			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}

}
