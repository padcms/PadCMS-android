package padcms.kiosk.view.adapter;

import java.util.List;

import padcms.dao.application.bean.Revision;
import padcms.kiosk.view.gallery.CoverView;
import padcms.net.NetHepler;
import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.Gallery;

public class GalleryAdapter extends AbstractAdapter {
	private List<Revision> listRevision;

	public static class Image {
		private int width;
		private int height;

		public int getHeight() {
			return height;
		}
	}

	public static Image getImage(Display display) {
		Image image = new Image();
		image.width = display.getWidth() / 3;
		if (display.getWidth() > display.getHeight()) {
			image.width = display.getHeight() / 4;
		}
		image.height = (int) (image.width * (260.0 / 180.0));

		return image;
	}

	public GalleryAdapter(Context context, List<Revision> list) {
		Display display = ((Activity) context).getWindowManager()
				.getDefaultDisplay();
		views = new View[list.size()];
		listRevision = list;
		Image image = getImage(display);

		for (int i = 0; i < list.size(); i++) {
			/* The gap we want between the reflection and the original image */
			Revision revision = list.get(i);

			String imageUrl = NetHepler
					.getAbsoulutePathToDownloadResourceImage(revision
							.getCover_image_list());
			CoverView viewCover = new CoverView(context, imageUrl, image.width);
			viewCover.setTag(revision);
			views[i] = viewCover;
			views[i].setLayoutParams(new Gallery.LayoutParams(image.width,
					image.height + image.height / 2));

		}
	}

	public void initDisplayForItems(Display display) {
		int width = display.getWidth() / 3;
		if (display.getWidth() > display.getHeight()) {
			width = display.getHeight() / 4;
		}
		int height = (int) (width * (260.0 / 180.0));

		for (int i = 0; i < views.length; i++) {

			views[i].setLayoutParams(new Gallery.LayoutParams(width, height
					+ height / 2));
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return views[position];
	}

	public void setZoomForCover(Revision revision) {
		for (int i = 0; i < listRevision.size(); i++) {
			if (listRevision.get(i).equals(revision)) {

				ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.5f,
						1f, 1.5f, views[i].getLayoutParams().width / 2,
						views[i].getLayoutParams().height / 2);

				scaleAnimation.setFillEnabled(true);
				scaleAnimation.setFillAfter(true);
				scaleAnimation.setDuration(100);
				((CoverView) views[i]).startAnimation(scaleAnimation);
				((CoverView) views[i]).setInZoomMode(true);
				break;
			}
		}

	}

	public void dismisZoomForCover() {
		for (int i = 0; i < listRevision.size(); i++) {

			boolean isScaled = ((CoverView) views[i]).isInZoomMode();

			if (isScaled) {
				ScaleAnimation scaleAnimation = new ScaleAnimation(1.5f, 1f,
						1.5f, 1f, views[i].getLayoutParams().width / 2,
						views[i].getLayoutParams().height / 2);

				scaleAnimation.setFillEnabled(true);
				scaleAnimation.setFillAfter(true);
				scaleAnimation.setDuration(100);
				((CoverView) views[i]).startAnimation(scaleAnimation);
				((CoverView) views[i]).setInZoomMode(false);
			}

		}

	}
}