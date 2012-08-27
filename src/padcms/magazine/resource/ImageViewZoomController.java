package padcms.magazine.resource;

import padcms.magazine.controls.imagecontroller.ImageViewTouch;
import padcms.magazine.factory.ResourceResolutionHelper;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;

public class ImageViewZoomController extends ImageViewController {

	public ImageViewZoomController(Context mContext,
			String imageResoureRelativePath) {
		super(mContext, imageResoureRelativePath);

	}

	public ImageViewZoomController(Context mContext,
			String imageResoureRelativePath, String dimentionString) {
		super(mContext, imageResoureRelativePath, dimentionString);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDrow(final Bitmap bitmap) {
		if (getBaseElemetView().getShowingView() != null) {

			((Activity) mContext).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ImageView targetImagetView = (ImageView) getBaseElemetView()
							.getShowingView();
					if (bitmap != null && !bitmap.isRecycled()) {
						// backgroundView.setImageBitmap(bitmap);
						ResourceResolutionHelper bitmapResolution = getBaseElemetView()
								.getResolutionForController(bitmap);

						targetImagetView.getLayoutParams().width = bitmapResolution.width;
						targetImagetView.getLayoutParams().height = bitmapResolution.height;
						if (((ImageViewTouch) targetImagetView).isBitmapSet())
							((ImageViewTouch) targetImagetView)
									.setImageBitmapReset(bitmap, false);
						else
							((ImageViewTouch) targetImagetView)
									.setImageBitmapReset(bitmap, true);

						releaseInctiveResources();
						targetImagetView.setBackgroundColor(Color.WHITE);
					} else {
						targetImagetView.setImageBitmap(null);
						targetImagetView.setBackgroundColor(Color.TRANSPARENT);
						releaseInctiveResources();
					}

				}

			});

		}

	}

}
