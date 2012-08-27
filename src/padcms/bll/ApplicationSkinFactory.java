package padcms.bll;

import padcms.application11.R;
import padcms.magazine.factory.*;
import padcms.magazine.page.BasePageView;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class ApplicationSkinFactory {

	public static OnTouchListener getOnTouchListnerForDrckPress(
			final ImageView button, final BasePageView parentPageView) {
		return new OnTouchListener() {
			float firstX = 0;
			float firstY = 0;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					parentPageView.setAllowClick(false);
					firstX = event.getRawX();
					firstY = event.getRawY();
					button.setColorFilter(0xF0555555, PorterDuff.Mode.MULTIPLY);

				} else if (event.getAction() == MotionEvent.ACTION_UP) {

					button.setColorFilter(null);
					
				} else {

					float deltaX = Math.abs(event.getRawX() - firstX);
					float deltaY = Math.abs(event.getRawY() - firstY);
					if (deltaX >= ViewConfiguration.getTouchSlop() || deltaY >= ViewConfiguration.getTouchSlop()) {
						button.setColorFilter(null);
						parentPageView.setAllowClick(true);
					}
				}

				return false;
			}
		};
	}

	public static void animateShow(final View view) {
		Animation animation = AnimationUtils.loadAnimation(view.getContext(),
				R.anim.show);

		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				view.setVisibility(View.VISIBLE);

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				view.invalidate();
				// view.requestLayout();
			}
		});
		view.startAnimation(animation);

	}

	public static void animateHide(final View view) {
		Animation animation = AnimationUtils.loadAnimation(view.getContext(),
				R.anim.fade);

		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				view.setVisibility(View.GONE);
			}
		});
		view.startAnimation(animation);

	}

	public static void animateHideFast(final View view) {
		Animation animation = AnimationUtils.loadAnimation(view.getContext(),
				R.anim.fade_fast);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				view.setVisibility(View.GONE);
			}
		});
		view.startAnimation(animation);
	}

	// public static View getPhotoGallaryButton_Rue(final BasePageView
	// basePageView) {
	// ImageButton gallaryButton = new ImageButton(basePageView.getContext());
	//
	// RelativeLayout.LayoutParams imageButtonRelatieLayout = new
	// RelativeLayout.LayoutParams(
	// -2, -2);
	// imageButtonRelatieLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	// imageButtonRelatieLayout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	//
	// gallaryButton
	// .setBackgroundResource(R.drawable.activezone_photo_selector_rue);
	//
	// imageButtonRelatieLayout.bottomMargin = 5;
	// imageButtonRelatieLayout.rightMargin = 5;
	//
	// gallaryButton.setLayoutParams(imageButtonRelatieLayout);
	// gallaryButton.bringToFront();
	//
	// gallaryButton.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// IssueViewFactory.getIssueViewFactoryIstance(v.getContext())
	// .showGallary(basePageView.getPageID());
	// }
	// });
	// return gallaryButton;
	//
	// }

	// public static View getPhotoGallaryButton_Air_le_mag(
	// BasePageView basePageView) {
	//
	// RelativeLayout.LayoutParams imageButtonRelatieLayout =
	// getLayoutParamsForPhotoGallaryButton(
	// basePageView.getContext(), R.drawable.button_photo_gallary_bg);
	// imageButtonRelatieLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	//
	// ImageButton gallaryButton = ctrateBasePhotoGallaryButton(basePageView,
	// R.drawable.button_photo_gallary_selector);
	//
	// gallaryButton.setLayoutParams(imageButtonRelatieLayout);
	// gallaryButton.getBackground().setColorFilter(basePageView.getColor(),
	// PorterDuff.Mode.MULTIPLY);
	//
	// gallaryButton.setImageResource(R.drawable.air_le_mag_photo_fg);
	//
	// return gallaryButton;
	//
	// }

	public static View getPhotoGallaryButton(final BasePageView basePageView) {

		RelativeLayout.LayoutParams relativeLayoutParams = getLayoutParamsForPhotoGallaryButton(
				basePageView.getContext(), R.drawable.button_photo_gallary_bg);
		if (basePageView.getContext()
				.getString(R.string.button_photo_gallary_side).equals("right"))
			relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		else if (basePageView.getContext()
				.getString(R.string.button_photo_gallary_side).equals("left"))
			relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

		ImageButton gallaryButton = ctrateBaseImageButton(
				basePageView.getContext(),
				R.drawable.button_photo_gallary_selector);

		gallaryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				IssueViewFactory.getIssueViewFactoryIstance(v.getContext())
						.showGallary(basePageView.getPageID(), 0, 0);
			}
		});

		gallaryButton.setLayoutParams(relativeLayoutParams);
		gallaryButton.getBackground().setColorFilter(basePageView.getColor(),
				PorterDuff.Mode.MULTIPLY);
		gallaryButton.setImageResource(R.drawable.button_photo_gallary_fg);

		return gallaryButton;

	}

	public static View getPhotoGallaryButtonReture(BasePageView basePageView) {

		RelativeLayout.LayoutParams imageButtonRelatieLayout = getLayoutParamsForPhotoGallaryButton(
				basePageView.getContext(),
				R.drawable.button_photo_gallary_reture_bg);
		if (basePageView.getContext()
				.getString(R.string.button_photo_gallary_side).equals("right"))
			imageButtonRelatieLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		else if (basePageView.getContext()
				.getString(R.string.button_photo_gallary_side).equals("left"))
			imageButtonRelatieLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

		ImageButton gallaryButton = ctrateBaseImageButton(
				basePageView.getContext(),
				R.drawable.button_photo_gallary_reture_selector);

		gallaryButton.setLayoutParams(imageButtonRelatieLayout);
		gallaryButton.getBackground().setColorFilter(basePageView.getColor(),
				PorterDuff.Mode.MULTIPLY);
		// gallaryButton.setScaleType(ScaleType.CENTER_INSIDE);
		gallaryButton
				.setImageResource(R.drawable.button_photo_gallary_reture_fg);

		return gallaryButton;

	}

	// public static View getPhotoGallaryButton_DCCV(BasePageView basePageView)
	// {
	//
	// RelativeLayout.LayoutParams imageButtonRelatieLayout =
	// getLayoutParamsForPhotoGallaryButton(
	// basePageView.getContext(), R.drawable.gallary_enter_fg_dccv);
	// imageButtonRelatieLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	// ImageButton gallaryButton = ctrateBasePhotoGallaryButton(basePageView,
	// R.drawable.gallary_enter_fg_dccv);
	//
	// gallaryButton.setLayoutParams(imageButtonRelatieLayout);
	// gallaryButton.getBackground().setColorFilter(basePageView.getColor(),
	// PorterDuff.Mode.MULTIPLY);
	//
	// // gallaryButton.setImageResource(R.drawable.air_le_mag_photo_fg);
	//
	// return gallaryButton;
	//
	// }

	// public static View getPhotoGallaryButtonReture_air_le_mag(
	// BasePageView basePageView) {
	//
	// RelativeLayout.LayoutParams imageButtonRelatieLayout =
	// getLayoutParamsForPhotoGallaryButton(
	// basePageView.getContext(), R.drawable.phptos_ar_le_mag_bg);
	//
	// imageButtonRelatieLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	//
	// ImageButton gallaryButton = ctrateBasePhotoGallaryButton(basePageView,
	// R.drawable.activezone_photo_selector_air);
	//
	// gallaryButton.setLayoutParams(imageButtonRelatieLayout);
	// gallaryButton.getBackground().setColorFilter(basePageView.getColor(),
	// PorterDuff.Mode.MULTIPLY);
	//
	// gallaryButton.setImageResource(R.drawable.reture_fg);
	//
	// return gallaryButton;
	//
	// }

	// public static View getPhotoGallaryButtonReture_dccv(
	// BasePageView basePageView) {
	//
	// RelativeLayout.LayoutParams imageButtonRelatieLayout =
	// getLayoutParamsForPhotoGallaryButton(
	// basePageView.getContext(), R.drawable.gallary_reture_dccv);
	//
	// imageButtonRelatieLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	//
	// ImageButton gallaryButton = ctrateBasePhotoGallaryButton(basePageView,
	// R.drawable.reture_selector_dccv);
	//
	// gallaryButton.setLayoutParams(imageButtonRelatieLayout);
	//
	// return gallaryButton;
	//
	// }

	public static View getScrollUpDirectButton(Context context, int color,
			int topPadding, int leftPadding) {

		ImageButton gallaryButton = getScrollerController(context, color,
				R.drawable.scroll_up_selector, R.drawable.scroll_up_bg,
				R.drawable.scroll_up_fg, topPadding, leftPadding);

		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) gallaryButton
				.getLayoutParams();
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

		return gallaryButton;
	}

	public static View getScrollDownDirectButton(Context context, int color,
			int topPadding, int leftPadding) {

		ImageButton gallaryButton = getScrollerController(context, color,
				R.drawable.scroll_down_selector, R.drawable.scroll_down_bg,
				R.drawable.scroll_down_fg, 0, leftPadding);

		((RelativeLayout.LayoutParams) gallaryButton.getLayoutParams())
				.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		return gallaryButton;
	}

	private static ImageButton getScrollerController(Context context,
			int color, int resourceBGSelector, int resourceBG, int resourceFG,
			int topPadding, int leftPadding) {
		RelativeLayout.LayoutParams imageButtonRelatieLayout = getLayoutParamsForScrollButton(
				context, resourceBG);

		ImageButton scrollButton = ctrateBaseImageButton(context,
				resourceBGSelector);

		scrollButton.setLayoutParams(imageButtonRelatieLayout);
		if (color != Color.WHITE)
			scrollButton.getBackground().setColorFilter(color,
					PorterDuff.Mode.MULTIPLY);

		if (resourceFG != 0) {
			scrollButton.setImageResource(resourceFG);
		}
		Display display = ResourceResolutionHelper.getDefaultDisplay(context);
		if (leftPadding != 0) {

			int width = display.getWidth() - Math.abs(leftPadding);

			imageButtonRelatieLayout.rightMargin = Math
					.abs((width - imageButtonRelatieLayout.width) / 2);

			imageButtonRelatieLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		} else
			imageButtonRelatieLayout.addRule(RelativeLayout.CENTER_HORIZONTAL);

		if (topPadding != 0) {
			int height = Math.abs(topPadding);

			imageButtonRelatieLayout.topMargin = Math.abs((height));

		}
		return scrollButton;
	}

	private static RelativeLayout.LayoutParams getLayoutParamsForPhotoGallaryButton(
			Context context, int resourceBitmapID) {
		ResourceResolutionHelper resolutionDisplay = ResourceResolutionHelper
				.getResolutionScaledPhotoGallary(context, resourceBitmapID);

		RelativeLayout.LayoutParams imageButtonRelatieLayout = new RelativeLayout.LayoutParams(
				-2, -2);
		imageButtonRelatieLayout.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		imageButtonRelatieLayout.width = resolutionDisplay.width;
		imageButtonRelatieLayout.height = resolutionDisplay.height;
		imageButtonRelatieLayout.topMargin = 0;
		imageButtonRelatieLayout.rightMargin = 10;
		return imageButtonRelatieLayout;
	}

	private static RelativeLayout.LayoutParams getLayoutParamsForScrollButton(
			Context context, int resourceBitmapID) {
		ResourceResolutionHelper resolutionDisplay = ResourceResolutionHelper
				.getResolutionScaledScrollButton(context, resourceBitmapID);

		return new RelativeLayout.LayoutParams(resolutionDisplay.width,
				resolutionDisplay.height);
	}

	private static ImageButton ctrateBaseImageButton(Context context,
			int resourseID) {
		ImageButton imageButton = new ImageButton(context);

		imageButton.setScaleType(ScaleType.FIT_CENTER);
		imageButton.setPadding(0, 0, 0, 0);
		Drawable norm = context.getResources().getDrawable(resourseID);
		imageButton.setBackgroundDrawable(norm.mutate());
		return imageButton;
	}

	public static void showAnimationOnClick(View v) {
		v.startAnimation(AnimationUtils.loadAnimation(v.getContext(),
				R.anim.animat_clickbutton));
	}

	public static int darker(int color, double fraction) {
		return changeColor(color, fraction, false);
	}

	public static int lighter(int color, double fraction) {
		return changeColor(color, fraction, true);
	}

	private static int changeColor(int color, double fraction, boolean isLight) {
		int alpha = 255;
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = (color >> 0) & 0xFF;
		int red = (int) Math.round(r + (255 * fraction) * (isLight ? 1 : -1));
		int green = (int) Math.round(g + (255 * fraction) * (isLight ? 1 : -1));
		int blue = (int) Math.round(b + (255 * fraction) * (isLight ? 1 : -1));
		if (red < 0)
			red = 0;
		else if (red > 255)
			red = 255;
		if (green < 0)
			green = 0;
		else if (green > 255)
			green = 255;
		if (blue < 0)
			blue = 0;
		else if (blue > 255)
			blue = 255;
		return Color.argb(alpha, red, green, blue);
	}

}
