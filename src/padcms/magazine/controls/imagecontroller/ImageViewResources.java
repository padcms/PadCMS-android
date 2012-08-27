package padcms.magazine.controls.imagecontroller;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class ImageViewResources extends ImageView {

	public ImageViewResources(Context context) {
		super(context);

	}

	public ImageViewResources(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	protected void onDraw(Canvas canvas) {

		try {
			super.onDraw(canvas);
		} catch (RuntimeException e) {
			Log.e("#### ImageViewResources.RuntimeException  #### ERROR",
					e.getMessage());
		} catch (Exception e) {
			Log.e("#### ImageViewResources #### ERROR", e.getMessage());
		}

	}
}
