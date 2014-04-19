package com.icloud.framework.picture;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TZPhotoUtil {

	public static final int WIDTH = 300;
	public static final int HEIGHT = 450;
	public static final int FACTOR = 4;
	public static final int MINWIDTH = 93;
	public static final int MIDWIDTH = 128;
	public static final int MAXWIDTH = 205;

	public static final int SquareWidth = 140;
	public static final int ClientWidth = 600;

	public static byte[] getBytesFromFile(File file) throws IOException {

		InputStream is = new FileInputStream(file);
		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			throw new IOException("File is to large " + file.getName());
		}

		byte[] bytes = new byte[(int) length];

		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}
		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

	public static boolean storeToFile(String path, byte[] bytes) {
		boolean flag = false;
		if (bytes != null) {
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(path);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				bos.write(bytes);
				bos.close();
				fos.close();
				flag = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return flag;
	}

	public static byte[] reduceImageWithWidth(byte[] content, String type, int width) {
		if (content == null)
			return content;
		content = ImageUtil.resizePhotoStep(content, width, 0);
		return content;
	}

	public static byte[] reduceImageWithHeight(byte[] content, String type, int height) {
		if (content == null)
			return content;
		content = ImageUtil.resizePhotoStep(content, 0, height);
		return content;
	}

	public static byte[] reduceSquareImage(byte[] content, String type, int squareWidth) {
		if (content == null)
			return content;

		content = ImageUtil.createSquareThumbnail(content);

		content = ImageUtil.resizePhotoStep(content, squareWidth, 0);

		return content;
	}

	public static void main(String args[]) {
		if (args.length != 2) {
			System.err.println("<firstPath> <destPath>");
			return;
		}
		System.out.println(System.getProperty("java.library.path"));
		// args = new String[2];
		// // args[0] = "E:/haitu360软件管理/v1.3.0.0/软件截图/icon.png";
		// // args[1] = "E:/haitu360软件管理/v1.3.0.0/软件截图/icon2.png";
		// // args[0] = "/home/tangzhige/Projects/tz/tz-dev/ImageMagick/1.jpg";
		// // args[1] = "/home/tangzhige/Projects/tz/tz-dev/ImageMagick/3.jpg";
		// args[0] = "/home/jiangningcui/workspace/tz/tz-dev/ImageMagick/1.jpg";
		// args[1] = "/home/jiangningcui/workspace/tz/tz-dev/ImageMagick/3.jpg";
		byte[] content = null;
		try {
			content = getBytesFromFile(new File(args[0]));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] content1 = reduceSquareImage(content, "jpg", 100);
		// byte[] content1 = ImageUtil.resizePhotoStep(content,123, 200);
		storeToFile(args[1], content1);
		System.out.println("yes...");
	}
}