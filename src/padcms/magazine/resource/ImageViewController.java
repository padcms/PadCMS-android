package padcms.magazine.resource;

import padcms.bll.DialogHelper;
import padcms.magazine.factory.IssueViewFactory;
import padcms.magazine.factory.ResourceFactory;
import padcms.magazine.factory.ResourceResolutionHelper;
import padcms.magazine.page.State;
import padcms.net.NetHepler;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

public class ImageViewController extends ResourceController {

	private Bitmap bitmapOriginal;
	private Bitmap bitmapScaled;
	private boolean isBitmapOriginalExist;
	private boolean isBitmapScaledExist;
	// private ArrayList<Bitmap> listBitmap;
	private int sizeScale = -1;
	private View controllerView;

	// public ImageView targetImagetView;

	public ImageViewController(Context mContext, String imageResoureRelativePath) {
		super(mContext);

		if (imageResoureRelativePath != null
				&& imageResoureRelativePath.length() > 0) {
			setUrlToResource(NetHepler
					.getUrlToResourceByDemetion(imageResoureRelativePath));

		}

	}

	public ImageViewController(Context mContext,
			String imageResoureRelativePath, String dimentionString) {
		super(mContext);

		if (imageResoureRelativePath != null
				&& imageResoureRelativePath.length() > 0) {
			setUrlToResource(NetHepler.getUrlToResourceByDemetion(
					imageResoureRelativePath, dimentionString));

		}

	}

	// public ImageViewController(Context mContext, ImageView targetImagetView,
	// String imageResourseRelativePath, String resolution) {
	// super(mContext);
	// this.targetImagetView = targetImagetView;
	// if (imageResourseRelativePath != null
	// && imageResourseRelativePath.length() > 0) {
	// setUrlToResource(NetHepler.getUrlToResourceByDemetion(
	// imageResourseRelativePath, resolution));
	//
	// }
	// }

	// public boolean isPartedBitmap(String pathToImage) {
	// Options optionsBitmap = new Options();
	// optionsBitmap.inJustDecodeBounds = true;
	// BitmapFactory.decodeFile(pathToResourse, optionsBitmap);
	//
	// Display display = ResourceResolutionHelper.getDefaultDisplay(mContext);
	//
	// int realHeight = display.getHeight();
	// if (display.getWidth() > display.getHeight()) {
	// realHeight = display.getWidth();
	// }
	//
	// if (realHeight < optionsBitmap.outHeight) {
	// return false;
	// }
	//
	// return false;
	// }

	// public abstract void drowElement(Bitmap bitmap);

	// public void setTargetImagetView(ImageView targetImagetView) {
	// this.targetImagetView = targetImagetView;
	// }

	public void setControllerView(View controllerView) {
		this.controllerView = controllerView;
	}

	public View getControllerView() {
		return controllerView;
	}

	// public void initResourceController(String imageResourseRelativePath) {
	// if (imageResourseRelativePath != null
	// && imageResourseRelativePath.length() > 0) {
	//
	// this.urlToImageResource = NetHepler
	// .getUrlToResourceByDemetion(imageResourseRelativePath);
	//
	// this.pathToImageResourse = new File(IssueViewFactory
	// .getIssueViewFactoryIstance(mContext)
	// .getPathToResourceFolder(),
	// String.valueOf(urlToImageResource.hashCode()))
	// .getAbsolutePath();
	// // ApplicationFileHelper
	// // .getPathToIssueFolder(IssueViewFactory.issueFolderName)
	// // .getAbsolutePath()
	// // + "/" + String.valueOf(urlToImageResource.hashCode());
	//
	// } else {
	//
	// this.urlToImageResource = this.pathToImageResourse = "";
	//
	// }
	// isBitmapOriginalExist = false;
	// isBitmapScaledExist = false;
	// // releaseInctiveResources();
	//
	// }

	//
	// public ImageResourceController(String pathToImageResourse,
	// String urlToImageResource) {
	// this.pathToImageResourse = pathToImageResourse;
	// this.urlToImageResource = urlToImageResource;
	// }

	public int getSizeScale() {
		return sizeScale;
	}

	public void setSizeScale(int sizeScale) {
		this.sizeScale = sizeScale;
	}

	@Override
	public void setState(State state) {

		// if (this.state != state) {
		super.setState(state);
		switch (state) {
		case ACTIVE:
			break;
		case DISACTIVE:
			if (isBitmapScaledExist) {
				isBitmapOriginalExist = false;
				// drowElement();

			} else if (isBitmapOriginalExist) {
				bitmapScaled = ResourceFactory
						.decodeScaledBitmap(bitmapOriginal);
				isBitmapScaledExist = true;
				isBitmapOriginalExist = false;
				onDrow(bitmapScaled);
			}
			// else {
			// drowElement();
			// }
			break;

		case RELEASE:
			isBitmapOriginalExist = false;
			isBitmapScaledExist = false;
			releaseInctiveResources();
			onDrow(null);
			break;
		default:
			break;
		}
		// }

	}

	@Override
	public void showResource() {
		switch (state) {
		case EXTRA_ACTIVE:
			if (!isBitmapOriginalExist) {
				// try {
				// drowOriginalBitmap();
				// } catch (Exception e) {
				//
				// IssueViewFactory.getIssueViewFactoryIstance(mContext)
				// .getVerticalPageSwitcherController()
				// .releaseInactive();
				// bitmapOriginal = ResourceFactory
				// .decodeBitmapWithoutCatch(pathToResourse);
				// DialogHelper.makeAsynckMessageShow(mContext,
				// "ERROR: " + e.getMessage());
				//
				// }
				// isBitmapScaledExist = false;
				// isBitmapOriginalExist = true;
				// }
			}
			// drowElement();

			break;
		case ACTIVE:
			isBitmapScaledExist = false;
			// if (isBitmapOriginalExist) {
			// // drowElement(bitmapOriginal, false);
			// drowElement();
			// } else {
			// try {
			drowOriginalBitmap();
			// bitmapOriginal = ResourceFactory
			// .decodeBitmap(pathToResourse);
			// } catch (OutOfMemoryError out) {
			// IssueViewFactory.getIssueViewFactoryIstance(mContext)
			// .getVerticalPageSwitcherController()
			// .releaseInactive();
			// bitmapOriginal = ResourceFactory
			// .decodeBitmapWithoutCatch(pathToResourse);
			// DialogHelper.makeAsynckMessageShow(mContext, "ERROR: "
			// + out.getMessage());
			// } catch (Exception e) {
			//
			// DialogHelper.makeAsynckMessageShow(mContext,
			// "ERROR: " + e.getMessage());
			// }
			// if (bitmapOriginal != null) {
			// isBitmapOriginalExist = true;
			// }
			// drowElement();
			// }
			break;
		case DISACTIVE:
			if (isBitmapScaledExist) {
				isBitmapOriginalExist = false;
				// drowElement();

			} else if (isBitmapOriginalExist) {
				bitmapScaled = ResourceFactory
						.decodeScaledBitmap(bitmapOriginal);

				isBitmapScaledExist = true;
				isBitmapOriginalExist = false;
				onDrow(bitmapScaled);
			} else {
				if (bitmapScaled == null || bitmapScaled.isRecycled()) {
					bitmapScaled = ResourceFactory
							.decodeScaledBitmap(pathToResourse);
					isBitmapScaledExist = true;
					isBitmapOriginalExist = false;
					onDrow(bitmapScaled);
				}

			}
			break;

		case RELEASE:
			isBitmapOriginalExist = false;
			isBitmapScaledExist = false;
			releaseInctiveResources();
			onDrow(null);
			break;

		default:
			break;
		}
	}

	private void drowOriginalBitmap() {

		try {
			bitmapOriginal = ResourceFactory.decodeBitmap(pathToResourse);
		} catch (OutOfMemoryError out) {

			((Activity) mContext).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					IssueViewFactory.getIssueViewFactoryIstance(mContext)
							.getVerticalPageSwitcherController()
							.releaseInactive();
				}
			});

			bitmapOriginal = ResourceFactory
					.decodeBitmapWithoutCatch(pathToResourse);
			DialogHelper.makeAsynckMessageShow(mContext,
					"ERROR: " + out.getMessage());
		} catch (Exception e) {

			DialogHelper.makeAsynckMessageShow(mContext,
					"ERROR: " + e.getMessage());
		}
		if (bitmapOriginal != null) {
			isBitmapOriginalExist = true;
		}

		onDrow(bitmapOriginal);

		// return ResourceFactory.decodeBitmap(pathToResourse);
	}

	// private void drowPartedBitmap() {
	// try {
	//
	// int partCount = 10;
	//
	// Options optionsBitmap = new Options();
	// optionsBitmap.inJustDecodeBounds = true;
	// BitmapFactory.decodeFile(pathToResourse, optionsBitmap);
	//
	// BitmapRegionDecoder bitmapDecoder = null;
	// try {
	// bitmapDecoder = BitmapRegionDecoder.newInstance(pathToResourse,
	// false);
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	// }
	//
	// Rect rectPartBitmap = new Rect();
	//
	// Display display = ResourceResolutionHelper
	// .getDefaultDisplay(mContext);
	// float firstPartHeight = display.getHeight() / 5.f;
	//
	// if (display.getWidth() > display.getHeight()) {
	// firstPartHeight = display.getWidth();
	//
	// }
	// rectPartBitmap.set(0, 0, optionsBitmap.outWidth,
	// (int) firstPartHeight);
	//
	// releaseInctiveResources();
	// Bitmap part = bitmapDecoder.decodeRegion(rectPartBitmap, null);
	//
	// isBitmapOriginalExist = true;
	//
	// listBitmap = new ArrayList<Bitmap>();
	// listBitmap.add(part);
	//
	// float leaftHeight = optionsBitmap.outHeight - firstPartHeight;
	// for (int i = 0; i < partCount; i++) {
	// if (isBitmapOriginalExist) {
	// rectPartBitmap = new Rect();
	//
	// float topPosiiont = firstPartHeight + i * leaftHeight
	// / (partCount * 1.0f);
	// float partHeight = topPosiiont + leaftHeight
	// / (partCount * 1.0f);
	//
	// rectPartBitmap.set(0, (int) topPosiiont,
	// optionsBitmap.outWidth, (int) partHeight);
	// try {
	// part = bitmapDecoder.decodeRegion(rectPartBitmap, null);
	// } catch (Exception e) {
	//
	// }
	// listBitmap.add(part);
	// // canvas.drawBitmap(part, 0, topPosiiont, null);
	// // ((Activity) mContext).runOnUiThread(new Runnable() {
	// // @Override
	// // public void run() {
	// // getOnDrowBitmapListner().onInvalidateView();
	// //
	// // }
	// // });
	//
	// } else
	// break;
	// }
	// // getOnDrowPartedBitmapListner().onDrow(listBitmap);
	// bitmapDecoder.recycle();
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// private void drowPartedBitmap2() {
	// try {
	//
	// int partCount = 10;
	//
	// Options optionsBitmap = new Options();
	// optionsBitmap.inJustDecodeBounds = true;
	// BitmapFactory.decodeFile(pathToResourse, optionsBitmap);
	// bitmapOriginal = Bitmap.createBitmap(optionsBitmap.outWidth,
	// optionsBitmap.outHeight, Config.ARGB_8888);
	//
	// BitmapRegionDecoder bitmapDecoder = null;
	// try {
	// bitmapDecoder = BitmapRegionDecoder.newInstance(pathToResourse,
	// false);
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	// }
	//
	// Rect rectPartBitmap = new Rect();
	//
	// Display display = ResourceResolutionHelper
	// .getDefaultDisplay(mContext);
	// float firstPartHeight = display.getHeight() / 5.f;
	//
	// if (display.getWidth() > display.getHeight()) {
	// firstPartHeight = display.getWidth();
	//
	// }
	// rectPartBitmap.set(0, 0, optionsBitmap.outWidth,
	// (int) firstPartHeight);
	//
	// Bitmap part = bitmapDecoder.decodeRegion(rectPartBitmap, null);
	//
	// Canvas canvas = new Canvas(bitmapOriginal);
	// canvas.drawBitmap(part, 0, 0, null);
	// // part.recycle();
	// isBitmapOriginalExist = true;
	// // getOnDrowBitmapListner().onDrow(bitmapOriginal);
	//
	// float leaftHeight = optionsBitmap.outHeight - firstPartHeight;
	// for (int i = 0; i < partCount; i++) {
	// if (isBitmapOriginalExist) {
	// rectPartBitmap = new Rect();
	//
	// float topPosiiont = firstPartHeight + i * leaftHeight
	// / (partCount * 1.0f);
	// float partHeight = topPosiiont + leaftHeight
	// / (partCount * 1.0f);
	//
	// rectPartBitmap.set(0, (int) topPosiiont,
	// optionsBitmap.outWidth, (int) partHeight);
	// part = bitmapDecoder.decodeRegion(rectPartBitmap, null);
	//
	// canvas.drawBitmap(part, 0, topPosiiont, null);
	// ((Activity) mContext).runOnUiThread(new Runnable() {
	// @Override
	// public void run() {
	// // getOnDrowBitmapListner().onInvalidateView();
	//
	// }
	// });
	//
	// } else
	// break;
	// }
	// bitmapDecoder.recycle();
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public void releaseResources() {
		isBitmapOriginalExist = false;
		isBitmapScaledExist = false;
		releaseInctiveResources();
	}

	@Override
	public void releaseInctiveResources() {
		if (!isBitmapOriginalExist) {
			// if (isParted) {
			// if (listBitmap != null)
			// for (Bitmap bitmap : listBitmap)
			// releaseBitmap(bitmap);
			// } else
			releaseBitmap(bitmapOriginal);
		}
		if (!isBitmapScaledExist) {
			releaseBitmap(bitmapScaled);
		}

	}

	private void releaseBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
	}

	protected void onDrow(final Bitmap bitmap) {

		if (baseElemetView.getShowingView() != null) {

			((Activity) mContext).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (mUpdateProgress != null) {
						mUpdateProgress.hideProgress();
					}

					ImageView targetImagetView = (ImageView) baseElemetView
							.getShowingView();
					if (bitmap != null && !bitmap.isRecycled()) {

						ResourceResolutionHelper bitmapResolution = baseElemetView
								.getResolutionForController(bitmap);
						targetImagetView.setImageBitmap(bitmap);

						targetImagetView.getLayoutParams().width = bitmapResolution.width;
						targetImagetView.getLayoutParams().height = bitmapResolution.height;

						releaseInctiveResources();

					} else {
						targetImagetView.setImageBitmap(null);
						// targetImagetView.setBackgroundColor(Color.TRANSPARENT);
						releaseInctiveResources();
					}
				}
			});

		}

	}

}
