package padcms.kiosk.view.gallery;

import padcms.bll.ImageLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class CoverView extends RelativeLayout {

	private ImageView contentCover;
	private boolean inZoomMode;

	public CoverView(Context context) {
		super(context);
	}

	public CoverView(Context context, String coverImageUrl, int width) {
		super(context);

		ProgressBar progressBar = new ProgressBar(context);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				width / 2, width / 2);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);

		addView(progressBar, lp);

		contentCover = new ImageView(context);

		RelativeLayout.LayoutParams lpContentCover = new RelativeLayout.LayoutParams(
				-1, -1);
		lpContentCover.topMargin = 20;
		addView(contentCover, lpContentCover);
		contentCover.bringToFront();
		ImageLoader.getInstance(context).drawImage(coverImageUrl,
				callByHandler, -1);
	}

	public CoverView(Context context, String coverImageUrl, int width,
			int height) {
		super(context);

		ProgressBar progressBar = new ProgressBar(context);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				width / 2, width / 2);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);

		addView(progressBar, lp);

		contentCover = new ImageView(context);

		RelativeLayout.LayoutParams lpContentCover = new RelativeLayout.LayoutParams(
				width, height);
		addView(contentCover, lpContentCover);
		contentCover.bringToFront();
		ImageLoader.getInstance(context).drawImage(coverImageUrl,
				callByHandlerNoReflectedBitmap, -1);
	}

	Handler callByHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.obj != null) {
				Bitmap bitmap = (Bitmap) msg.obj;
				if (!bitmap.isRecycled()) {

					contentCover.setImageDrawable(new BitmapDrawable(
							getReflectedBitmap(bitmap)));
				}
			}
		}
	};

	Handler callByHandlerNoReflectedBitmap = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.obj != null) {
				Bitmap bitmap = (Bitmap) msg.obj;
				if (!bitmap.isRecycled()) {

					contentCover.setImageDrawable(new BitmapDrawable(bitmap));
				}
			}
		}
	};

	public void makeZoomCover() {
		ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1, 1.5f, 1.5f);
		scaleAnimation.setFillEnabled(true);
		scaleAnimation.setFillAfter(true);
		scaleAnimation.setDuration(1000);
		contentCover.startAnimation(scaleAnimation);
		// RelativeLayout.LayoutParams lpContentCover =
		// (RelativeLayout.LayoutParams) contentCover
		// .getLayoutParams();
		// lpContentCover.height = (int) (lpContentCover.height * 1.5f);
		// lpContentCover.width = (int) (lpContentCover.width * 1.5f);

	}

	public Bitmap getReflectedBitmap(Bitmap originalImage) {
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		// This will not scale but will flip on the Y axis
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		// Create a Bitmap with the flip matix applied to it.
		// We only want the bottom half of the image
		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
				height / 2, width, height / 2, matrix, false);

		// Create a new bitmap with same width but taller to fit reflection
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		// Create a new Canvas with the bitmap that's big enough for
		// the image plus gap plus reflection
		Canvas canvas = new Canvas(bitmapWithReflection);
		// Draw in the original image
		canvas.drawBitmap(originalImage, 0, 0, null);
		// Draw in the gap
		Paint deafaultPaint = new Paint();
		canvas.drawRect(0, height, width, height, deafaultPaint);
		// Draw in the reflection
		canvas.drawBitmap(reflectionImage, 0, height, null);

		// Create a shader that is a linear gradient that covers the
		// reflection
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0,
				originalImage.getHeight(), 0, bitmapWithReflection.getHeight(),
				0x70ffffff, 0x00ffffff, TileMode.CLAMP);
		// Set the paint to use this shader (linear gradient)
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight(),
				paint);
		originalImage.recycle();
		return bitmapWithReflection;
	}

	public boolean isInZoomMode() {
		return inZoomMode;
	}

	public void setInZoomMode(boolean inZoomMode) {
		this.inZoomMode = inZoomMode;
	}
	
}
