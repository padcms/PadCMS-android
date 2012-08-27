package padcms.magazine.controls;

import padcms.magazine.controls.switcher.BaseRealViewSwitcher;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

public class HorizontalScrollViewController extends HorizontalScrollView {

	public HorizontalScrollViewController(Context context) {
		super(context);
		Display display = ((Activity) context).getWindowManager()
				.getDefaultDisplay();
		maxDeltaScrollHorisontal = display.getWidth() / 10;
	}

	private float lastx = 0;
	private float lasty = 0;
	private float firstx = 0;
	private float firsty = 0;

	private boolean donewithclick = true;
	private ViewGroup parent = null;
	private int childWidth = 0;
	private static final int NONE = -1;
	private static final int DOSCROLLVIEW = 0;
	private static final int DOGALLERY = 1;

	private int gestureRecipient = NONE;
	private int maxDeltaScrollHorisontal = 0;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		boolean retthis = true;
		// Log.e("############## HorizontalScrollViewController ",
		// "== ev"+ev.getAction());
		if (donewithclick) {
			firstx = ev.getRawX();
			firsty = ev.getRawY();
			lastx = firstx;
			lasty = firsty;
			gestureRecipient = DOSCROLLVIEW;
			childWidth = getChildAt(0).getWidth();
			donewithclick = false;
			// firstclick = true;
			// countPoint = 0;

		}
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {

			firstx = ev.getRawX();
			firsty = ev.getY();
			lastx = firstx;
			lasty = firsty;

			childWidth = getChildAt(0).getWidth();

			getParentView().onTouchEvent(ev);
			return super.onTouchEvent(ev);
		}

		// if (gestureRecipient == NONE && lastx != ev.getX()) {
		// if (lastx == firstx) {
		// getParentView().onTouchEvent(ev);
		// retthis = super.onTouchEvent(ev);
		// } else {
		// float xdiff = firstx - ev.getX();
		//
		// float ydiff = firsty - ev.getY();
		//
		// if (xdiff < 0)
		// xdiff = xdiff * -1;
		//
		// if (ydiff < 0)
		// ydiff = ydiff * -1;
		//
		// Log.i("events", "xdiff " + xdiff + " ydiff " + ydiff
		// + " width:" + getLayoutParams().width);
		// // if (xdiff > ydiff) {
		// gestureRecipient = DOSCROLLVIEW;
		// // } else if (ydiff > maxDeltaScrollHorisontal) {
		// // gestureRecipient = DOGALLERY;
		// //
		// // } else {
		// // gestureRecipient = NONE;
		// // // getParentView().onTouchEvent(ev);
		// // retthis = super.onTouchEvent(ev);
		// // }
		// }
		// }
		else if (ev.getAction() == MotionEvent.ACTION_UP) {

			getParentView().onTouchEvent(ev);
			super.onTouchEvent(ev);
			gestureRecipient = NONE;
			donewithclick = true;

		} else {
			if (gestureRecipient == DOGALLERY) {
				Log.e("gestureRecipient ", "== DOGALLERY");
				retthis = getParentView().onTouchEvent(ev);

				// firstclick = false;
			} else if (gestureRecipient == DOSCROLLVIEW) {
				float xdiff = ev.getRawX() - lastx;

				if (this.getScrollX() == 0 && xdiff > 0) {
					Log.e("gestureRecipient ", "== DOGALLERY");
					gestureRecipient = DOGALLERY;
					retthis = getParentView().onTouchEvent(ev);
					super.onTouchEvent(ev);
				} else if (childWidth - this.getScrollX() == getMeasuredWidth()
						&& xdiff < 0) {
					Log.e("gestureRecipient ", "== DOGALLERY");
					gestureRecipient = DOGALLERY;
					retthis = getParentView().onTouchEvent(ev);
					super.onTouchEvent(ev);
				} else {
					Log.e("gestureRecipient ", "== DOSCROLLVIEW");
					retthis = super.onTouchEvent(ev);

				}
			}
		}

		lastx = ev.getRawX();

		return retthis;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN)
			getParentView().onTouchEvent(ev);
		return super.onInterceptTouchEvent(ev);
	}

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

}
