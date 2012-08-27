package padcms.magazine.controls;

import android.content.Context;
import android.view.View;

public abstract class BaseSmoothFliperAdapter {
	private SmoothFliperView smoothFliperView;
	private Context mContext;

	public BaseSmoothFliperAdapter(Context context) {
		mContext = context;
	}

	public SmoothFliperView getSmoothFliperView() {
		return smoothFliperView;
	}

	public void setSmoothFliperView(SmoothFliperView smoothFliperView) {
		this.smoothFliperView = smoothFliperView;
		initFirstView();
	}

	public Context getContext() {
		return mContext;
	}

	public abstract void initFirstView();

	public abstract boolean allowToFlip(
			SmoothFliperView.MoveSideAction sideAction);

	public abstract View getSideView(SmoothFliperView.MoveSideAction sideAction);

	public abstract void activationView(
			SmoothFliperView.MoveSideAction sideAction, boolean flipToFinish);

	public abstract void onViewClicked();

	public abstract void cleanAdapter();

	public abstract void startFlipAnimation();
}
