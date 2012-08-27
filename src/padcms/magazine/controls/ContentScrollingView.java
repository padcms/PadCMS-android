package padcms.magazine.controls;

import java.util.Timer;
import java.util.TimerTask;

import padcms.bll.ApplicationSkinFactory;
import padcms.magazine.controls.switcher.BaseRealViewSwitcher;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class ContentScrollingView extends ScrollView implements
		OnScrollListener {
	private View rLSpinnerUp;
	private View rLSpinnerDown;
	private Timer ScrollerCheckTimer;
	private int scrollCollor = -1;
	private int oldScrollY = 0;
	private static final int NONE = -1;
	private static final int DOSCROLLVIEW = 0;
	private static final int DOGALLERY = 1;

	private float lastTouchX = 0;
	private float lastTouchY = 0;
	private float firstx = 0;
	private float firsty = 0;
	private int gestureRecipient = NONE;
	private boolean donewithclick = true;
	private ViewGroup parent = null;

	private int childHeight = 0;
	boolean firstclick;

	private int maxDeltaScrollHorisontal = 0;

	public ContentScrollingView(Context context, int color) {
		super(context);

		maxDeltaScrollHorisontal = ViewConfiguration.getTouchSlop();
		scrollCollor = color;
		setHorizontalFadingEdgeEnabled(false);
		setVerticalFadingEdgeEnabled(false);
		

	}

	public void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		setSpinnerVisible(getScrollY());

	}

	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
		getChildAt(0).setVisibility(visibility);
	}

	Handler handlerScrollCheck = new Handler() {

		public void handleMessage(Message msg) {

			Log.i("handleMessage", "oldScrollY:" + msg.arg1 + "  y"
					+ getScrollY());
			if (msg.arg1 == getScrollY()) {
				oldScrollY = -1;
				if (ScrollerCheckTimer != null) {
					ScrollerCheckTimer.cancel();
					ScrollerCheckTimer = null;
				}
				setSpinnerVisible(msg.arg1);
			}

		}
	};

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		oldScrollY = t;

		if (handlerScrollCheck != null) {

			if (donewithclick && ScrollerCheckTimer == null) {
				ScrollerCheckTimer = new Timer();
				ScrollerCheckTimer.schedule(new TimerTask() {

					@Override
					public synchronized void run() {
						while (oldScrollY != -1) {
							try {
								wait(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							Message msg = new Message();
							msg.arg1 = oldScrollY;

							handlerScrollCheck.sendMessage(msg);

						}
					}
				}, 0);
			}
		}

		super.onScrollChanged(l, t, oldl, oldt);
	}

	public void setSpinnerVisible(int scrollY) {
		if (rLSpinnerUp == null) {
			if (rLSpinnerUp == null) {
				RelativeLayout.LayoutParams currentParams = (RelativeLayout.LayoutParams) getLayoutParams();
				rLSpinnerUp = ApplicationSkinFactory.getScrollUpDirectButton(
						getContext(), scrollCollor, currentParams.topMargin,
						currentParams.leftMargin);

				rLSpinnerUp.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						ScrollContentTo(true);
					}
				});

				((ViewGroup) getParent()).addView(rLSpinnerUp);
				rLSpinnerUp.bringToFront();
			}
			if (rLSpinnerDown == null) {
				RelativeLayout.LayoutParams currentParams = (RelativeLayout.LayoutParams) getLayoutParams();

				rLSpinnerDown = ApplicationSkinFactory
						.getScrollDownDirectButton(getContext(), scrollCollor,
								currentParams.topMargin,
								currentParams.leftMargin);

				rLSpinnerDown.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						ScrollContentTo(false);
					}
				});
				((ViewGroup) getParent()).addView(rLSpinnerDown);
				rLSpinnerDown.bringToFront();
			}

		}

		if (rLSpinnerUp != null && rLSpinnerDown != null) {
			childHeight = getChildAt(0).getHeight();
			if (childHeight <= getMeasuredHeight()) {
				Log.i("setSpinnerVisible", "1");

				rLSpinnerUp.setVisibility(GONE);
				rLSpinnerDown.setVisibility(GONE);
			} else if (scrollY == 0) {
				Log.i("setSpinnerVisible", "2");
				rLSpinnerUp.setVisibility(GONE);
				rLSpinnerDown.setVisibility(VISIBLE);
			} else if (childHeight - scrollY + getPaddingTop() == getMeasuredHeight()) {
				Log.i("setSpinnerVisible", "3");
				rLSpinnerUp.setVisibility(VISIBLE);
				rLSpinnerDown.setVisibility(GONE);
			} else {
				Log.i("setSpinnerVisible", "4");
				rLSpinnerUp.setVisibility(VISIBLE);
				rLSpinnerDown.setVisibility(VISIBLE);
			}

		}

	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
//		if (rLSpinnerDown != null)
//			rLSpinnerDown.refreshDrawableState();

	}

	public void ScrollContentTo(boolean scrollToUp) {
		int y = 0;
		// int duration = 500;
		if (scrollToUp) {
			y = getScrollY() - getMeasuredHeight();
			if (y < 0) {
				y = 0;
				// duration = 200;
			}

		} else {
			y = getScrollY() + getMeasuredHeight();
			if (y > childHeight) {
				// duration = 200;
				y = childHeight;
			}

		}

		final int scrolltoY = y;
		// Animation animation = new TranslateAnimation((float)getScrollX(),
		// (float)getScrollX(),
		// (float)getScrollY(), (float)scrolltoY);
		// animation .setFillEnabled(true);
		// animation .setFillAfter(true);
		// animation.setDuration(duraction);
		// startAnimation(animation);

		post(new Runnable() {

			public void run() {
				scrollTo(getScrollX(), scrolltoY);

			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean retthis = true;
		// Log.i("CONTENTSCROLLING ", "Action!!!!!" + event.getAction());
		// Log.i("CONTENTSCROLLING ", "move: x:" +
		// event.getX()+" y:"+event.getY());
		// Log.i("CONTENTSCROLLING ", "ROW move: x:" +
		// event.getRawX()+" y:"+event.getRawY());

		if (donewithclick) {
			childHeight = getChildAt(getChildCount() - 1).getHeight();
			donewithclick = false;
			firstclick = true;

		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			lastTouchX = firstx = event.getRawX();
			lastTouchY = firsty = event.getRawY();
			childHeight = getChildAt(0).getHeight();

			getParentView().onTouchEvent(event);

			return super.onTouchEvent(event);
		}

		if (gestureRecipient == NONE
				&& (lastTouchX != event.getRawX() || lastTouchY != event
						.getRawY())) {
			if (lastTouchX == firstx || lastTouchY == firsty) {
				// getParentView().onTouchEvent(event);
				retthis = super.onTouchEvent(event);
			} else {
				float xdiff = Math.abs(firstx - event.getRawX());
				float ydiff = Math.abs(firsty - event.getRawY());

				Log.i("events", "xdiff " + xdiff + " ydiff " + ydiff);

				if (xdiff >= maxDeltaScrollHorisontal
						|| (childHeight - this.getScrollY() == getMeasuredHeight())) {

					gestureRecipient = DOGALLERY;
				} else if (ydiff > xdiff) {
					gestureRecipient = DOSCROLLVIEW;
				} else {
					gestureRecipient = NONE;
					retthis = getParentView().onTouchEvent(event);
				}
			}
		}
		if (gestureRecipient == DOGALLERY) {
			Log.i("gestureRecipient ", "== DOGALLERY");
			retthis = getParentView().onTouchEvent(event);
		} else if (gestureRecipient == DOSCROLLVIEW) {
			Log.i("gestureRecipient ", "== SCROLL");
			float ydiff = event.getRawY() - lastTouchY;
			if (childHeight <= getMeasuredHeight()) {
				retthis = getParentView().onTouchEvent(event);
				gestureRecipient = DOGALLERY;
				super.onTouchEvent(event);
			} else {
				if (this.getScrollY() == 0 && ydiff > 0) {
					retthis = getParentView().onTouchEvent(event);
					gestureRecipient = DOGALLERY;
					super.onTouchEvent(event);
				} else if (childHeight - this.getScrollY() == getMeasuredHeight()
						&& ydiff < 0) {
					retthis = getParentView().onTouchEvent(event);
					gestureRecipient = DOGALLERY;
					super.onTouchEvent(event);
				} else {
					// Log.i("gestureRecipient ", "== DOSCROLLVIEW:" +
					// childHeight
					// + " getMeasuredHeight():" + getMeasuredHeight());
					retthis = super.onTouchEvent(event);
				}

			}
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {

			// retthis = getParentView().onTouchEvent(event);
			super.onTouchEvent(event);
			// if (Math.abs(firsty - lastTouchY) <
			// ViewConfiguration.getTouchSlop())
			if (gestureRecipient == NONE || gestureRecipient == DOGALLERY)
				getParentView().onTouchEvent(event);

			donewithclick = true;
			gestureRecipient = NONE;

		}

		lastTouchX = event.getRawX();
		lastTouchY = event.getRawY();
		return retthis;
	}

	// @Override
	// public boolean onInterceptTouchEvent(MotionEvent event) {
	// if (event.getAction() == MotionEvent.ACTION_DOWN
	// || event.getAction() == MotionEvent.ACTION_UP)
	// getParentView().onTouchEvent(event);
	// return super.onInterceptTouchEvent(event);
	// }

	private ViewGroup getParentView() {

		if (parent == null) {
			parent = getParentFliper((View) getParent());
		}
		return parent;
	}

	public ViewGroup getParentFliper(View view) {
		if (view.getParent() instanceof BaseRealViewSwitcher) {
			return (ViewGroup) view.getParent();
		} else {
			return getParentFliper((View) view.getParent());
		}

	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// Log.i("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", "onScroll");

	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		// Log.e("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", "onScrollStateChanged");
	}

}
