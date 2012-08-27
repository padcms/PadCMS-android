package padcms.magazine.controls;

import android.content.Context;
import android.widget.RelativeLayout;

public class RelativeContainerSmoothFliper extends RelativeLayout {

	public int scrollToPositionX;
	public int scrollToPositionY;
	public boolean flipToFinish;
	private SmoothFliperView parentFliper;

	public RelativeContainerSmoothFliper(Context context) {
		super(context);
	}

	@Override
	protected void onAnimationStart() {
		super.onAnimationStart();
		getParentFliper().flipAnimationStart(flipToFinish);
	}

	@Override
	protected void onAnimationEnd() {
		super.onAnimationEnd();
		getParentFliper().flipAnimationEnd(flipToFinish);
		makeScroll();

	};

	private SmoothFliperView getParentFliper() {
		if (parentFliper == null) {
			parentFliper = (SmoothFliperView) getParent();
		}
		return parentFliper;
	}

	public void makeScroll() {
		scrollTo(scrollToPositionX, scrollToPositionY);
	}

}
