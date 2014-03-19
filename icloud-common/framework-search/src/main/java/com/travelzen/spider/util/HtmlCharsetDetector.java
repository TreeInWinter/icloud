package com.travelzen.spider.util;

import java.io.*;
import java.net.*;

import org.mozilla.intl.chardet.*;

public class HtmlCharsetDetector {
	boolean found = false;
	String ret = "";

	public synchronized String detectCharset(byte[] byteSrc) {

		ret = "";
		try {
			found = false;
			nsDetector det = new nsDetector(nsPSMDetector.ALL);
			det.Init(new nsICharsetDetectionObserver() {
				public void Notify(String charset) {
					found = true;
					// System.out.println("notifyCHARSET = " + charset);
					ret = charset;
				}
			});
			boolean done = false;
			boolean isAscii = true;
			// check

			// Check if the stream is only ascii.
			if (isAscii)
				isAscii = det.isAscii(byteSrc, byteSrc.length);
			// DoIt if non-ascii and not done yet.
			if (!isAscii && !done)
				done = det.DoIt(byteSrc, byteSrc.length, false);
			det.DataEnd();
			if (isAscii && !found) {
				found = true;
				// System.out.println("CHARSET = ASCII");
				ret = "ASCII";
			}

			int count = 0;
			if (!found) {
				String prob[] = det.getProbableCharsets();
				for (int i = 0; i < prob.length; i++) {
					count++;
					ret = prob[i];
					// System.out.println("Probable Charset = " + prob[i]);
				}
				if (count > 1)
					ret = "GB18030";
			}
		} catch (Exception e) {

		}
		return ret;
	}

	public static void main(String argv[]) throws Exception {
		HtmlCharsetDetector obj = new HtmlCharsetDetector();
		String content = "ad歌曲dd";
		obj.detectCharset(content.getBytes());
		System.out.println("show:" + obj.ret);
		if (argv.length != 1 && argv.length != 2) {
			System.out
					.println("Usage: HtmlCharsetDetector <url> [<languageHint>]");
			System.out.println("");
			System.out.println("Where <url> is http://...");
			System.out.println("For optional <languageHint>. Use following...");
			System.out.println("		1 => Japanese");
			System.out.println("		2 => Chinese");
			System.out.println("		3 => Simplified Chinese");
			System.out.println("		4 => Traditional Chinese");
			System.out.println("		5 => Korean");
			System.out.println("		6 => Dont know (default)");

			return;
		}

		URL url = new URL(argv[0]);
		BufferedInputStream imp = new BufferedInputStream(url.openStream());

		byte[] buf = new byte[8192];
		int len;
		boolean done = false;
		boolean isAscii = true;
		int seq = 0;
		seq = imp.read(buf, 0, buf.length);
		obj.detectCharset(buf);

		// Initalize the nsDetector() ;
		/*
		 * int lang = (argv.length == 2)? Integer.parseInt(argv[1]) :
		 * nsPSMDetector.ALL ; nsDetector det = new nsDetector(lang) ;
		 * 
		 * // Set an observer... // The Notify() will be called when a matching
		 * charset is found.
		 * 
		 * det.Init(new nsICharsetDetectionObserver() { public void
		 * Notify(String charset) { found = true ;
		 * System.out.println("CHARSET = " + charset); } });
		 * 
		 * URL url = new URL(argv[0]); BufferedInputStream imp = new
		 * BufferedInputStream(url.openStream());
		 * 
		 * byte[] buf = new byte[2024] ; int len; boolean done = false ; boolean
		 * isAscii = true ; int seq=0; while( (len=imp.read(buf,0,buf.length))
		 * != -1) {
		 * 
		 * if(found) break; // Check if the stream is only ascii. if (isAscii)
		 * isAscii = det.isAscii(buf,len);
		 * 
		 * // DoIt if non-ascii and not done yet. if (!isAscii && !done) done =
		 * det.DoIt(buf,len, false); } det.DataEnd();
		 * 
		 * if (isAscii) { System.out.println("CHARSET = ASCII"); found = true ;
		 * }
		 * 
		 * if (!found) { String prob[] = det.getProbableCharsets() ; for(int
		 * i=0; i<prob.length; i++) { System.out.println("Probable Charset = " +
		 * prob[i]); } }
		 */
	}
}