package padcms.magazine.factory;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.view.Display;

public class ResourceResolutionHelper {
	public int width;
	public int height;
	private static int displayWidth;
	private static int displayHeight;

	public static ResourceResolutionHelper getBitmapResolution(Context context,
			int bitmapWidth, int bitmapHeight) {
		ResourceResolutionHelper bitmapResolution = new ResourceResolutionHelper();
		initBaseDisplayVertivcal(context);

		bitmapResolution.width = displayWidth;

		bitmapResolution.height = (int) (bitmapResolution.width * bitmapHeight / (bitmapWidth * 1.0f));

		return bitmapResolution;
	}

	public static ResourceResolutionHelper getBitmapResolutionHorisontal(
			Context context, int bitmapWidth, int bitmapHeight) {
		ResourceResolutionHelper bitmapResolution = new ResourceResolutionHelper();
		initBaseDisplayHorisontal(context);

		bitmapResolution.height = displayHeight;

		bitmapResolution.width = (int) (bitmapResolution.height * bitmapWidth / (bitmapHeight * 1.0f));

		return bitmapResolution;
	}

	public static ResourceResolutionHelper getBitmapResolutionHelpPageHorisontal(
			Context context, int bitmapWidth, int bitmapHeight) {
		ResourceResolutionHelper bitmapResolution = new ResourceResolutionHelper();
		initBaseDisplayHorisontal(context);

		bitmapResolution.width = displayWidth;

		bitmapResolution.height = (int) (bitmapResolution.width * bitmapHeight / (bitmapWidth * 1.0f));

		return bitmapResolution;
	}

	public static ResourceResolutionHelper getBitmapResolutionHorisontalMenuItem(
			Context context, int bitmapWidth, int bitmapHeight) {
		ResourceResolutionHelper bitmapResolution = new ResourceResolutionHelper();
		initBaseDisplayHorisontal(context);

		bitmapResolution.height = displayHeight / 4;

		bitmapResolution.width = (int) (bitmapResolution.height * bitmapWidth / (bitmapHeight * 1.0f));

		return bitmapResolution;
	}

	public static ResourceResolutionHelper getBitmapResolutionVerticalMenuItem(
			Context context, int bitmapWidth, int bitmapHeight) {
		ResourceResolutionHelper bitmapResolution = new ResourceResolutionHelper();
		initBaseDisplayHorisontal(context);

		bitmapResolution.width = displayWidth/ 5;

		bitmapResolution.height = (int) (bitmapResolution.width * bitmapHeight / (bitmapWidth * 1.0f));

		return bitmapResolution;
	}

	public static ResourceResolutionHelper getBitmapResolutionByWidth(
			int scaleWidth, int bitmapWidth, int bitmapHeight) {
		ResourceResolutionHelper bitmapResolution = new ResourceResolutionHelper();
		bitmapResolution.width = scaleWidth;

		bitmapResolution.height = (int) (bitmapResolution.width * bitmapHeight / (bitmapWidth * 1.0f));

		return bitmapResolution;
	}

	public static ResourceResolutionHelper getBitmapResolutionByHeight(
			int scaleHeight, int bitmapWidth, int bitmapHeight) {
		ResourceResolutionHelper bitmapResolution = new ResourceResolutionHelper();
		bitmapResolution.height = scaleHeight;

		bitmapResolution.width = (int) (bitmapResolution.height * bitmapWidth / (bitmapHeight * 1.0f));

		return bitmapResolution;
	}

	public static ResourceResolutionHelper getResolutionThumbl(Context context,
			int bitmapWidth, int bitmapHeight) {
		ResourceResolutionHelper bitmapResolution = new ResourceResolutionHelper();
		initBaseDisplayVertivcal(context);

		bitmapResolution.width = displayWidth / 5;

		bitmapResolution.height = (int) (bitmapResolution.width * bitmapHeight / (bitmapWidth * 1.0f));

		return bitmapResolution;
	}

	private static void initBaseDisplayVertivcal(Context context) {

		Display display = ((Activity) context).getWindowManager()
				.getDefaultDisplay();

		displayWidth = display.getWidth();
		displayHeight = display.getHeight();
		if (display.getWidth() > display.getHeight()) {
			displayWidth = display.getHeight();
			displayHeight = display.getWidth();
		}

	}

	public static boolean isPortrait(Context context) {
		Display display = ((Activity) context).getWindowManager()
				.getDefaultDisplay();
		if (display.getWidth() > display.getHeight()) {
			return false;
		}
		return true;
	}

	private static void initBaseDisplayHorisontal(Context context) {

		Display display = ((Activity) context).getWindowManager()
				.getDefaultDisplay();
		displayWidth = display.getWidth();
		displayHeight = display.getHeight();
		// if (display.getWidth() > display.getHeight()) {
		// displayWidth = display.getHeight();
		// displayHeight = display.getWidth();
		// }

	}

	public static Display getDefaultDisplay(Context context) {

		return ((Activity) context).getWindowManager().getDefaultDisplay();

	}

	public static ResourceResolutionHelper getResourceResolutionScaled(
			Context context, float width, float height) {
		ResourceResolutionHelper bitmapResolution = new ResourceResolutionHelper();
		initBaseDisplayVertivcal(context);
		if (width != -1.f) {
			bitmapResolution.width = displayWidth;
			float scaleSize = displayWidth / width;
			bitmapResolution.height = (int) (height * scaleSize);
		} else {
			bitmapResolution.width = -1;
			bitmapResolution.height = -1;
		}
		return bitmapResolution;
	}

	public static ResourceResolutionHelper getResourceResolutionHorisontalScaled(
			Context context, float width, float height) {
		ResourceResolutionHelper bitmapResolution = new ResourceResolutionHelper();
		initBaseDisplayHorisontal(context);
		if (width != -1.f) {
			bitmapResolution.width = displayWidth;
			float scaleSize = displayWidth / width;
			bitmapResolution.height = (int) (height * scaleSize);
		} else {
			bitmapResolution.width = -1;
			bitmapResolution.height = -1;
		}
		return bitmapResolution;
	}

	public static ResourceResolutionHelper getResolutionScaledPhotoGallary(
			Context context, int resourceImageID) {
		return getResourceResolution(context, resourceImageID);
	}

	public static ResourceResolutionHelper getResolutionScaledScrollButton(
			Context context, int resourceImageID) {
		return getResourceResolution(context, resourceImageID);
	}

	private static ResourceResolutionHelper getResourceResolutionForScaleByWidth(
			Context context, int resourceImageID, int scaleByWidth) {
		ResourceResolutionHelper bitmapResolution = new ResourceResolutionHelper();
		initBaseDisplayVertivcal(context);
		Options option = new BitmapFactory.Options();
		option.inJustDecodeBounds = true;

		BitmapFactory.decodeResource(context.getResources(), resourceImageID,
				option);

		bitmapResolution.width = displayWidth / scaleByWidth;
		float scaleSize = (float) option.outHeight / (float) option.outWidth;

		bitmapResolution.height = (int) (scaleSize * bitmapResolution.width);

		return bitmapResolution;
	}

	private static ResourceResolutionHelper getResourceResolution(
			Context context, int resourceImageID) {
		ResourceResolutionHelper bitmapResolution = new ResourceResolutionHelper();
		Options option = new BitmapFactory.Options();
		option.inJustDecodeBounds = true;

		BitmapFactory.decodeResource(context.getResources(), resourceImageID,
				option);

		bitmapResolution.width = option.outWidth;

		bitmapResolution.height = option.outHeight;

		return bitmapResolution;
	}
}
