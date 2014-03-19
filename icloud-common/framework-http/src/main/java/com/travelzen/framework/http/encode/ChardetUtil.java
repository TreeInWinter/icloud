package com.travelzen.framework.http.encode;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;

public class ChardetUtil {
	private static boolean found;
	private static String encoding;

	public static String getEncoding(File file) throws FileNotFoundException, IOException {
		return getEncoding(new FileInputStream(file));
	}

	public static String getEncoding(InputStream in) throws IOException {
		BufferedInputStream imp = new BufferedInputStream(in);
		byte[] buf = new byte[1024];
		int len;
		while ((len = imp.read(buf, 0, buf.length)) != -1)
			if (StringUtils.isNotBlank(encoding = getEncoding(buf, len)))
				break;
		return encoding;
	}

	public static String getEncoding(byte[] bytes, int length) {
		nsDetector det = new nsDetector();
		det.Init(new nsICharsetDetectionObserver() {
			public void Notify(String charset) {
				found = false;
				encoding = charset;
			}
		});
		boolean isAscii = det.isAscii(bytes, length);
		if (!isAscii)
			det.DoIt(bytes, length, false);
		det.DataEnd();
		if (isAscii) {
			encoding = "ASCII";
			found = true;
		}
		if (!found) {
			String prob[] = det.getProbableCharsets();
			if (prob.length > 0)
				encoding = prob[0];
			else
				return null;
		}
		return encoding;
	}

	public static String getEncoding(byte[] bytes) {
		return getEncoding(bytes, bytes.length);
	}

	public static boolean isUTF8(File file) throws FileNotFoundException, IOException {
		return "UTF-8".equalsIgnoreCase(getEncoding(file));
	}

	public static boolean isUTF8(InputStream in) throws IOException {
		return "UTF-8".equalsIgnoreCase(getEncoding(in));
	}
}
