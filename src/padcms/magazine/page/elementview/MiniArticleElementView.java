package padcms.magazine.page.elementview;

import padcms.application11.R;
import padcms.bll.ApplicationSkinFactory;
import padcms.dao.issue.bean.Element;
import padcms.magazine.controls.imagecontroller.ImageViewResources;
import padcms.magazine.factory.ElementViewFactory;
import padcms.magazine.factory.ResourceFactory;
import padcms.magazine.factory.ResourceResolutionHelper;
import padcms.magazine.page.BasePageView;
import padcms.magazine.page.State;
import padcms.magazine.resource.ImageViewController;
import padcms.magazine.resource.PartedImageViewController;
import padcms.magazine.resource.ResourceController;
import padcms.net.NetHepler;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * mini_article
 * 
 * There can be many mini-articles, so it�s multiple field. Each mini article is
 * composed of a PDF / HTML, a Thumbnail illustration used in the sliding scroll
 * band and an optional video file. If a video file is associated with the mini
 * article, it must be launched from PDF using a local://video link.
 * 
 * resource (string) relative path to the resource thumbnail (string) relative
 * path to the thumbnail thumbnail_selected (string) relative path to the
 * thumbnail_selected video (string) relative path to the video
 */
public class MiniArticleElementView extends BaseElementView implements
		OnClickListener {
	private String pathToThumbnail;
	private String pathToThumbnailSelected;
	private String pathToVideo;

	private ImageView thumbnailView;

	private RelativeLayout bodyViewRelativeLayout;
	private ImageViewController thumbnailResourceController;
	private ImageViewController thumbnailSelectedResourceController;
	// private PartedImageViewController bodyResourceController;
	private BackgroundElementView backgroundElementView;

	private boolean isActive;

	private BasePageView parentPageView;

	public MiniArticleElementView(BasePageView parentPageView, Element element) {
		super(parentPageView, element);

		pathToThumbnail = ElementViewFactory
				.getElementDataStringValue(ElementViewFactory
						.getElementDataCurrentType(elementDataCollection,
								"thumbnail"));

		pathToThumbnailSelected = ElementViewFactory
				.getElementDataStringValue(ElementViewFactory
						.getElementDataCurrentType(elementDataCollection,
								"thumbnail_selected"));

		thumbnailResourceController = new ImageViewController(
				parentPageView.getContext(), pathToThumbnail, "none");
		thumbnailResourceController.setBaseElemetView(this);
		if (pathToThumbnailSelected != null
				&& pathToThumbnailSelected.length() > 0) {
			thumbnailSelectedResourceController = new ImageViewController(
					parentPageView.getContext(), pathToThumbnailSelected,
					"none");
			thumbnailSelectedResourceController.setBaseElemetView(this);
		}
		// thumbnailResourceController
		// .setOnDrowBitmapListner(new onDrowBitmapListner() {
		// @Override
		// public void onInvalidateView() {
		// thumbnailView.invalidate();
		// }
		//
		// @Override
		// public void onDrow(final Bitmap drowBitmap) {
		// if (thumbnailView != null) {
		// runInUI(new Runnable() {
		// @Override
		// public void run() {
		// if (thumbnailView != null) {
		// if (drowBitmap != null
		// && !drowBitmap.isRecycled()) {
		// thumbnailView
		// .setImageBitmap(drowBitmap);
		//
		// ResourceResolutionHelper bitmapResolution =
		// ResourceResolutionHelper.getResolutionThumbl(
		// thumbnailView.getContext(),
		// drowBitmap.getWidth(),
		// drowBitmap.getHeight());
		//
		// thumbnailView.getLayoutParams().width = bitmapResolution.width;
		// thumbnailView.getLayoutParams().height = bitmapResolution.height;
		//
		// } else
		// thumbnailView.setImageBitmap(null);
		// }
		//
		// }
		// });
		//
		// }
		//
		// }
		// });

		// if (pathToThumbnailSelected != null
		// && pathToThumbnailSelected.length() > 0) {
		// thumbnailSelectedResourceController = new ImageViewController(
		// parentPageView.getContext(), pathToThumbnailSelected,
		// "none");
		// thumbnailSelectedResourceController.setBaseElemetView(this);
		// // new ImageViewController(
		// // parentPageView.getContext(), pathToThumbnailSelected,
		// // "none");
		// // thumbnailSelectedResourceController
		// // .setOnDrowBitmapListner(new onDrowBitmapListner() {
		// // @Override
		// // public void onInvalidateView() {
		// // thumbnailView.invalidate();
		// // }
		// //
		// // @Override
		// // public void onDrow(final Bitmap drowBitmap) {
		// //
		// // if (thumbnailView != null) {
		// // runInUI(new Runnable() {
		// // @Override
		// // public void run() {
		// // if (thumbnailView != null) {
		// // if (drowBitmap != null
		// // && !drowBitmap.isRecycled()) {
		// // thumbnailView
		// // .setImageBitmap(drowBitmap);
		// // ResourceResolutionHelper bitmapResolution =
		// // ResourceResolutionHelper.getResolutionThumbl(
		// // thumbnailView
		// // .getContext(),
		// // drowBitmap.getWidth(),
		// // drowBitmap.getHeight());
		// //
		// // thumbnailView.getLayoutParams().width = bitmapResolution.width;
		// // thumbnailView.getLayoutParams().height = bitmapResolution.height;
		// //
		// // } else
		// // thumbnailView
		// // .setImageBitmap(null);
		// // }
		// //
		// // }
		// // });
		// //
		// // }
		// //
		// // }
		// //
		// // });
		// }

	}

	public BackgroundElementView getBackgroundElementView() {
		return backgroundElementView;
	}

	public void setBackgroundElementView(
			BackgroundElementView backgroundElementView) {
		this.backgroundElementView = backgroundElementView;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public BasePageView getParentPageView() {
		return parentPageView;
	}

	//
	public void setBodyView(RelativeLayout bodyMinArticleView) {
		bodyViewRelativeLayout = bodyMinArticleView;
	}

	public void setParentPageView(BasePageView parentPageView) {
		this.parentPageView = parentPageView;
	}

	public String getPathToThumbnail() {
		return pathToThumbnail;
	}

	public void setPathToThumbnail(String pathToThumbnail) {
		this.pathToThumbnail = pathToThumbnail;
	}

	public String getPathToThumbnailSelected() {
		return pathToThumbnailSelected;
	}

	public void setPathToThumbnailSelected(String pathToThumbnailSelected) {
		this.pathToThumbnailSelected = pathToThumbnailSelected;
	}

	public String getPathToVideo() {
		return pathToVideo;
	}

	public void setPathToVideo(String pathToVideo) {
		this.pathToVideo = pathToVideo;
	}

	@Override
	public View getView(Context context) {
		if (thumbnailView == null) {

			thumbnailView = new ImageViewResources(context);
			LinearLayout.LayoutParams backLayoutParams = new LinearLayout.LayoutParams(
					ResourceResolutionHelper.getDefaultDisplay(context)
							.getWidth() / 5, -2);

			thumbnailView.setLayoutParams(backLayoutParams);
			thumbnailView.setScaleType(ScaleType.FIT_XY);
			thumbnailView.setOnClickListener(this);
			thumbnailView.setPadding(3, 0, 3, 0);
			thumbnailView.setOnTouchListener(ApplicationSkinFactory
					.getOnTouchListnerForDrckPress(thumbnailView,
							getParentPageView()));
			// // ((ImageViewController) thumbnailResourceController)
			// .setTargetImagetView(thumbnailView);
			// if (thumbnailSelectedResourceController != null)
			// ((ImageViewController) thumbnailSelectedResourceController)
			// .setTargetImagetView(thumbnailView);

		}
		return thumbnailView;
	}

	@Override
	public void activateElement() {
		super.activateElement();
		if (!isActive) {
			backgroundElementView.setState(getState());

			if (thumbnailSelectedResourceController != null) {
				thumbnailSelectedResourceController.setState(getState());
				ResourceFactory
						.processResourceController(thumbnailSelectedResourceController);
			}
			isActive = true;
		}
	};

	@Override
	public void disactivateElement() {
		super.disactivateElement();
		if (isActive) {
			backgroundElementView.setState(State.RELEASE);

			if (thumbnailSelectedResourceController != null)
				ResourceFactory
						.processResourceController(thumbnailResourceController);

			isActive = false;
		}
	}

	@Override
	public void setState(State state) {

		super.setState(state);

		if (state == State.DISACTIVE || state == State.ACTIVE) {

			if (thumbnailResourceController != null)
				thumbnailResourceController.setState(state);

			if (thumbnailSelectedResourceController != null)
				thumbnailSelectedResourceController.setState(state);
		}

		if (state != State.RELEASE) {
			if (state != State.DOWNLOAD) {
				if (isActive) {

					if (backgroundElementView != null)
						backgroundElementView.setState(State.ACTIVE);

					if (thumbnailSelectedResourceController != null)
						ResourceFactory
								.processResourceController(thumbnailSelectedResourceController);
					else
						ResourceFactory
								.processResourceController(thumbnailResourceController);

				} else {
					backgroundElementView.setState(State.DOWNLOAD);
					if (thumbnailSelectedResourceController != null) {
						thumbnailSelectedResourceController
								.setState(State.DOWNLOAD);
						ResourceFactory
								.processResourceController(thumbnailSelectedResourceController);
					}
					ResourceFactory
							.processResourceController(thumbnailResourceController);

				}
			} else {
				if (backgroundElementView != null)
					backgroundElementView.setState(getState());

				if (thumbnailResourceController != null)
					thumbnailResourceController.setState(state);

				if (thumbnailSelectedResourceController != null)
					thumbnailSelectedResourceController.setState(state);

				if (thumbnailResourceController!= null)
				ResourceFactory
						.processResourceController(thumbnailResourceController);
				if (thumbnailSelectedResourceController != null)
					ResourceFactory
							.processResourceController(thumbnailSelectedResourceController);

			}
		} else {
			if (backgroundElementView != null)
				backgroundElementView.setState(getState());

			if (thumbnailResourceController != null)
				thumbnailResourceController.setState(state);

			if (thumbnailSelectedResourceController != null)
				thumbnailSelectedResourceController.setState(state);

		}
	}

	@Override
	public void onClick(View v) {
		parentPageView.activateElementView(this);
	}

	public void destroyElementView() {

		thumbnailView = null;

	}

	@Override
	public ResourceResolutionHelper getResolutionForController(Bitmap bitmap) {

		return ResourceResolutionHelper.getResolutionThumbl(
				thumbnailView.getContext(), bitmap.getWidth(),
				bitmap.getHeight());

	}

	@Override
	public View getShowingView() {
		return thumbnailView;
	}
}
