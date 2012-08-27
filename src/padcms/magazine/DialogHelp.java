package padcms.magazine;

import padcms.application11.R;
import padcms.dao.factory.DataStorageFactory;
import padcms.magazine.factory.ResourceResolutionHelper;
import padcms.magazine.help.HelpElementView;
import padcms.magazine.help.HelpElementViewHorisontal;
import padcms.magazine.page.State;
import padcms.magazine.page.elementview.ProgressDownloadElementView;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class DialogHelp extends Dialog implements OnShowListener {

	private RelativeLayout relativeLayoutScroll;
	private HelpElementView scrolElement;
	private HelpElementViewHorisontal scrolElementHorisontal;
	private Context mContext;
	private ImageView mCrossImage;
	private FrameLayout mContent;

	public DialogHelp(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		setOnShowListener(this);
		mContext = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		mContent = new FrameLayout(getContext());

		createCrossImage();

		int crossWidth = mCrossImage.getDrawable().getIntrinsicWidth();
		setContentElemtsView(mContext, crossWidth / 2);

		mCrossImage.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
		((FrameLayout.LayoutParams) mCrossImage.getLayoutParams()).gravity = Gravity.RIGHT;

		mContent.addView(mCrossImage);
		mCrossImage.bringToFront();

		addContentView(mContent, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
	}

	public void setContentElemtsView(Context context, int margin) {
		RelativeLayout scrollViewContainer = new RelativeLayout(getContext());

		ScrollView scroll = new ScrollView(context);

		relativeLayoutScroll = new RelativeLayout(context);
		relativeLayoutScroll
				.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));

		ProgressDownloadElementView progressDownloadElementView = new ProgressDownloadElementView(
				context);

		scrolElement = new HelpElementView(context, DataStorageFactory
				.getInstance(context).getCurrentRevisionIssue()
				.getHelp_page_vertical());
		scrolElementHorisontal = new HelpElementViewHorisontal(context,
				DataStorageFactory.getInstance(context)
						.getCurrentRevisionIssue().getHelp_page_horizontal());

		scrolElement
				.setProgressDownloadElementView(progressDownloadElementView);
		scrolElementHorisontal
				.setProgressDownloadElementView(progressDownloadElementView);

		RelativeLayout.LayoutParams trLayoutParams = new RelativeLayout.LayoutParams(
				-2, -2);
		trLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

		scrollViewContainer
				.addView(progressDownloadElementView, trLayoutParams);

		scroll.addView(relativeLayoutScroll);

		trLayoutParams = new RelativeLayout.LayoutParams(-1, -1);
		trLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		scrollViewContainer.addView(scroll, trLayoutParams);

		scrollViewContainer
				.setBackgroundResource(R.drawable.shape_roundrec_dialog);

		FrameLayout.LayoutParams frameLP = new FrameLayout.LayoutParams(-1, -1);
		frameLP.setMargins(margin, margin, margin, margin);
		frameLP.gravity = Gravity.CENTER;

		mContent.addView(scrollViewContainer, frameLP);
	}

	private void createCrossImage() {

		mCrossImage = new ImageView(getContext());
		mCrossImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		Drawable crossDrawable = mContent.getResources().getDrawable(
				R.drawable.close);
		mCrossImage.setImageDrawable(crossDrawable);
		mCrossImage.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onStop() {
		super.onStop();

		scrolElement.setState(State.DISACTIVE);
		scrolElementHorisontal.setState(State.DISACTIVE);

	}

	@Override
	public void onShow(DialogInterface arg0) {

		if (ResourceResolutionHelper.isPortrait(mContext)) {

			relativeLayoutScroll.removeAllViews();
			relativeLayoutScroll.addView(scrolElement.getView(mContext));
			scrolElement.setState(State.ACTIVE);
			mCrossImage.bringToFront();
		} else {

			relativeLayoutScroll.removeAllViews();
			relativeLayoutScroll.addView(scrolElementHorisontal
					.getView(mContext));
			scrolElementHorisontal.setState(State.ACTIVE);
			mCrossImage.bringToFront();
		}

	}
}
