package com.icloud.framework.picture;

import java.awt.Rectangle;

import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;

public class ImageUtil {
	static {
		// 不能漏掉这个，不然jmagick.jar的路径找不到
		System.setProperty("jmagick.systemclassloader", "no");
	}

	// 获得byte
	private static MagickImage getMagickImage(byte[] b) throws MagickException {
		ImageInfo info = new ImageInfo();
		MagickImage source = new MagickImage(info, b);
		// // if (source != null) {
		// source.profileImage("*", null);
		// source.profileImage("*", null);// 移除图片的其他信息
		// source.trimImage();
		return source;
	}

	/**
	 * @param b
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static byte[] resizePhotoStep(byte[] b, int maxWidth, int maxHeight) {
		if (b == null || b.length == 0)
			return b;
		MagickImage source = null;
		byte[] content = null;
		try {
			source = getMagickImage(b);
		} catch (MagickException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			if (source != null) {
				source.destroyImages();
				source = null;
			}
		}
		if (source != null) {
			MagickImage scaled = null;
			try {
				int width = 0;
				int height = 0;
				width = (int) source.getDimension().getWidth();
				height = (int) source.getDimension().getHeight();

				float factor = 1.0f;
				float widthFactor = 1.0f;
				float heightFactor = 1.0f;

				if (maxWidth > 0 && maxWidth < width) {
					widthFactor = maxWidth / (float) width;
				}
				if (maxHeight > 0 && maxHeight < height) {
					heightFactor = maxHeight / (float) height;
				}
				
				factor = widthFactor < heightFactor ? widthFactor : heightFactor;
				height = (int) (factor * height);
				width = (int) (factor * width);
				// if (maxWidth > width) {
				// } else {
				// if (width > 0 && height > 0) {
				// height = height * maxWidth / width;
				// width = maxWidth;
				// }
				// }
				//
				// if (maxHeight > height) {
				// } else {
				// if (width > 0 && height > 0) {
				// width = width * maxHeight / height;
				// height = maxHeight;
				// }
				// }

				scaled = source.scaleImage(width, height);
				ImageInfo info = new ImageInfo();
				info.setQuality(90);
				content = scaled.imageToBlob(info);
				scaled.destroyImages();
				source.destroyImages();
			} catch (MagickException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			} finally {
				if (source != null) {
					source.destroyImages();
					source = null;
				}
				if (scaled != null) {
					scaled.destroyImages();
					scaled = null;
				}
			}
		}
		return content;
	}

	/**
	 * 切图
	 * 
	 * @param imgPath
	 *            源图路径
	 * @param toPath
	 *            修改图路径
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 * @param x
	 *            左上角的 X 坐标
	 * @param y
	 *            左上角的 Y 坐标
	 * @throws MagickException
	 */
	public static byte[] cutImg(byte[] b, int w, int h, int x, int y) throws MagickException {
		if (b == null || b.length == 0)
			return b;
		MagickImage source = null;
		byte[] content = null;
		try {
			source = getMagickImage(b);
		} catch (MagickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (source != null) {
				source.destroyImages();
			}
			source = null;
		}
		if (source != null) {
			MagickImage cropped = null;
			try {
				Rectangle rect = null;
				rect = new Rectangle(x, y, w, h);
				cropped = source.cropImage(rect);
				content = cropped.imageToBlob(new ImageInfo());
			} catch (MagickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (source != null) {
					source.destroyImages();
					source = null;
				}
				if (cropped != null) {
					cropped.destroyImages();
					cropped = null;
				}
			}
		}

		return content;

	}

	/**
	 * cutsqure Image
	 * 
	 * @param image
	 * @return
	 * @throws MagickException
	 */
	public static byte[] createSquareThumbnail(byte[] b) {
		if (b == null)
			return null;

		// 获得缩放的比例
		MagickImage source = null;
		byte[] content = null;
		try {
			source = getMagickImage(b);
		} catch (MagickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (source != null) {
				source.destroyImages();
			}
			source = null;
		}
		if (source != null) {
			MagickImage cropped = null;
			try {
				int width = 0;
				int height = 0;
				width = (int) source.getDimension().getWidth();
				height = (int) source.getDimension().getHeight();
				int x = 0, y = 0;
				int squareWidth = width;
				if (width > height) {
					x = (width - height) / 2;
					squareWidth = height;
				} else if (height > width) {
					y = (height - width) / 2;
					squareWidth = width;
				}
				Rectangle rect = null;
				rect = new Rectangle(x, y, squareWidth, squareWidth);
				cropped = source.cropImage(rect);
				content = cropped.imageToBlob(new ImageInfo());
				rect = null;
			} catch (MagickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (source != null) {
					source.destroyImages();
					source = null;
				}
				if (cropped != null) {
					cropped.destroyImages();
					cropped = null;
				}
			}
		}
		return content;
	}
}