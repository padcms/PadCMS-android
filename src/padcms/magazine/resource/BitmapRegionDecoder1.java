package padcms.magazine.resource;

import java.io.BufferedInputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 *  * BitmapRegionDecoder can be used to decode a rectangle region from an
 * image.  * BitmapRegionDecoder is particularly useful when an original image
 * is large and  * you only need parts of the image.  *  *
 * <p>
 * To create a BitmapRegionDecoder, call newInstance(...).  * Given a
 * BitmapRegionDecoder, users can call decodeRegion() repeatedly  * to get a
 * decoded Bitmap of the specified region.  *  
 */
public final class BitmapRegionDecoder1 {
	private int mNativeBitmapRegionDecoder;
	private boolean mRecycled;

	public static BitmapRegionDecoder1 newInstance(byte[] data, int offset,
			int length, boolean isShareable) throws IOException {
		if ((offset | length) < 0 || data.length < offset + length) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return nativeNewInstance(data, offset, length, isShareable);
	}

	public static BitmapRegionDecoder1 newInstance(FileDescriptor fd,
			boolean isShareable) throws IOException {
		return nativeNewInstance(fd, isShareable);
	}

	public static BitmapRegionDecoder1 newInstance(InputStream is,
			boolean isShareable) throws IOException {
		if (!is.markSupported()) {
			is = new BufferedInputStream(is, 16 * 1024);

		}
		if (is instanceof AssetManager.AssetInputStream) {
			return nativeNewInstance(
					((AssetManager.AssetInputStream) is).getAssetInt(),
					isShareable);
		} else {
			byte[] tempStorage = new byte[16 * 1024];
			return nativeNewInstance(is, tempStorage, isShareable);
		}
	}

	public static BitmapRegionDecoder1 newInstance(String pathName,
			boolean isShareable) throws IOException {
		BitmapRegionDecoder1 decoder = null;
		InputStream stream = null;

		try {
			stream = new FileInputStream(pathName);
			decoder = newInstance(stream, isShareable);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {

				}
			}
		}
		return decoder;
	}

	private BitmapRegionDecoder1(int decoder) {
		mNativeBitmapRegionDecoder = decoder;
		mRecycled = false;
	}

	public Bitmap decodeRegion(Rect rect, BitmapFactory.Options options) {

		if (rect.left < 0 || rect.top < 0 || rect.right > getWidth()
				|| rect.bottom > getHeight())
			throw new IllegalArgumentException(
					"rectangle is not inside the image");
		return nativeDecodeRegion(mNativeBitmapRegionDecoder, rect.left,
				rect.top, rect.right - rect.left, rect.bottom - rect.top,
				options);
	}

	public int getWidth() {
		checkRecycled("getWidth called on recycled region decoder");
		return nativeGetWidth(mNativeBitmapRegionDecoder);
	}

	public int getHeight() {
		checkRecycled("getHeight called on recycled region decoder");
		return nativeGetHeight(mNativeBitmapRegionDecoder);
	}

	public void recycle() {
		if (!mRecycled) {
			nativeClean(mNativeBitmapRegionDecoder);
			mRecycled = true;
		}
	}

	public final boolean isRecycled() {
		return mRecycled;
	}

	private void checkRecycled(String errorMessage) {
		if (mRecycled) {
			throw new IllegalStateException(errorMessage);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			recycle();
		} finally {
			super.finalize();
		}
	}

	private static native Bitmap nativeDecodeRegion(int lbm, int start_x,
			int start_y, int width, int height, BitmapFactory.Options options);

	private static native int nativeGetWidth(int lbm);

	private static native int nativeGetHeight(int lbm);

	private static native void nativeClean(int lbm);

	private static native BitmapRegionDecoder1 nativeNewInstance(byte[] data,
			int offset, int length, boolean isShareable);

	private static native BitmapRegionDecoder1 nativeNewInstance(
			FileDescriptor fd, boolean isShareable);

	private static native BitmapRegionDecoder1 nativeNewInstance(InputStream is,
			byte[] storage, boolean isShareable);

	private static native BitmapRegionDecoder1 nativeNewInstance(int asset,
			boolean isShareable);
}