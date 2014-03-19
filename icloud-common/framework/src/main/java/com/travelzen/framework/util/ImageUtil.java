/**
 * 
 */
package com.travelzen.framework.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 
 * @author peilv
 *
 */
public class ImageUtil {
	/***
	 * 将图片进行scale。操作超大图片的时候会出现内存溢出的错误
	 * @param inFilePath
	 * @param outFilePath
	 * @param width
	 * @param height
	 */
	public static void imageOp(String inFilePath, String outFilePath, int width,
			int height) {

		File tempFile = new File(inFilePath);

		Image image = null;

		try {

			image = ImageIO.read(tempFile);

		} catch (IOException e) {

			System.out.println("file path error...");

		}

		int originalImageWidth = image.getWidth(null);

		int originalImageHeight = image.getHeight(null);

		BufferedImage originalImage = new BufferedImage(

		originalImageWidth,

		originalImageHeight,

		BufferedImage.TYPE_3BYTE_BGR);

		Graphics2D g2d = originalImage.createGraphics();

		g2d.drawImage(image, 0, 0, null);

		BufferedImage changedImage =

		new BufferedImage(

		width,

		height,

		BufferedImage.TYPE_3BYTE_BGR);

		double widthBo = (double) width / originalImageWidth;

		double heightBo = (double) width / originalImageHeight;

		AffineTransform transform = new AffineTransform();

		transform.setToScale(widthBo, heightBo);

		AffineTransformOp ato = new AffineTransformOp(transform, null);

		ato.filter(originalImage, changedImage);

		File fo = new File(outFilePath); // 将要转换出的小图文件

		try {

			ImageIO.write(changedImage, "jpeg", fo);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public static void main(String[] args) {
			imageOp("/yr/c.jpg", "/yr/c_small.jpg",400,300);
	}
}
