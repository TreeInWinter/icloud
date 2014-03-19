package com.travelzen.framework.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.ice.tar.TarArchive;
//import com.ice.tar.TarEntry;

/**
 * 
 * @author jianyesun
 * 
 */
public class FileUtils {
	private static Logger logger = LoggerFactory.getLogger(FileUtils.class);
	private static final int FILE_IO_BLOCK_LEN = 5000;

	private File file = null;

	private static int MAX_LENGTH = 2 * 1024 * 1024;

	private BufferedReader reader = null;

	private InputStream in = null;

	private OutputStream out = null;

	private PrintWriter writer = null;

	private static Map<String, BlockingQueue<Object>> fileLock = new HashMap<String, BlockingQueue<Object>>();

	/**
	 * Constructor
	 * 
	 * @param file
	 */
	public FileUtils(File file) {
		this.file = file;
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 */
	public FileUtils(String absolutePath) {
		this(new File(absolutePath));
	}

	/**
	 * Return whether or not this file or directory exists
	 * 
	 * @return
	 */
	public boolean exists() {
		return file.exists();
	}

	/**
	 * open the reader
	 * 
	 */
	private void openRead() {
		try {
			if (reader == null) {
				in = new FileInputStream(file);
				reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void openWrite() {
		try {
			if (writer == null) {
				out = new FileOutputStream(this.file, false);
				writer = new PrintWriter(out);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	// /**
	// *
	// * open the reader by class.resource
	// *
	// * @retained
	// */
	// private void openResource() {
	// try {
	// in = getClass().getResourceAsStream(fileName);
	// reader = new BufferedReader(new InputStreamReader(in));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * close the reader
	 * 
	 */
	public void close() {
		try {
			if (reader != null) {
				reader.close();
				reader = null;
			}

			if (in != null) {
				in.close();
				in = null;
			}

			if (writer != null) {
				writer.close();
				writer = null;
			}

			if (out != null) {
				out.close();
				out = null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * read a line from the file
	 * 
	 * @return
	 */
	public String readLine() {
		openRead();
		String s = null;
		try {
			s = reader.readLine();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return s;
	}

	/**
	 * read a line from the InputStream
	 * 
	 * @return
	 */
	public static List<String> readLine(File file) {

		try {
			return readLine(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * read a line from the InputStream
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	public static List<String> readLine(InputStream in) {

		List<String> list = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));

		if (reader == null) {
			logger.error("reader is null");
			return list;
		}

		String s = "";
		try {
			while ((s = reader.readLine()) != null) {
				list.add(s);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return list;
	}

	/**
	 * read byte to byteArray from the file
	 * 
	 * @return
	 */
	public static byte[] readByte(File file) {
		try {
			return readByte(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * read byte to byteArray from the InputStream
	 * 
	 * @param in
	 * @return
	 */
	@SuppressWarnings("unused")
	public static byte[] readByte(InputStream in) {

		if (in == null)
			return null;

		int size = 1024;
		ByteBuffer buffer = ByteBuffer.allocate(50 * 1024 * 1024);

		int len = 0;
		byte[] b = new byte[1024];
		try {
			while ((len = in.read(b)) != -1) {
				buffer.put(b, 0, len);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		int length = buffer.position();
		byte[] result = new byte[length];
		buffer.position(0);
		buffer.get(result, 0, length);

		return result;
	}

	/**
	 * read a line from the gzip-file
	 * 
	 * @return
	 */
	public String gzipReadLine() {
		String s = null;
		try {
			if (reader == null) {
				in = new FileInputStream(file);
				reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(in)));
			}
			s = reader.readLine();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return s;
	}

	/**
	 * read into String from the file
	 * 
	 * @return
	 */
	public String readString() {
		openRead();
		StringBuffer buf = new StringBuffer();
		char[] b = new char[1024];
		int len = 0;
		try {
			while ((len = reader.read(b)) != -1) {
				buf.append(b, 0, len);
				if (buf.length() > MAX_LENGTH) {
					// LogUtils.log(FileUtils.class,
					// "file size is too large, more than " + MAX_LENGTH +
					// " char", LogUtils.WARN);
					logger.warn("file size is too large, more than " + MAX_LENGTH + " char");
					break;
				}
			}
		} catch (Exception e) {
			// LogUtils.log(FileUtils.class, e, LogUtils.FATAL);
			logger.error(e.getMessage(), e);
		}
		return buf.toString();
	}

	/**
	 * read the InputStream
	 * 
	 * @return
	 */
	public int readByte(byte[] b) {
		openRead();
		int len = -1;
		try {
			if (in != null) {
				len = in.read(b);
			}
		} catch (Exception e) {
			// LogUtils.log(FileUtils.class, e, LogUtils.FATAL);
			logger.error(e.getMessage(), e);
		}
		return len;
	}

	/**
	 * read into ArrayList Object
	 * 
	 * @return
	 */
	public ArrayList<String> readLines() {
		ArrayList<String> list = new ArrayList<String>();
		String s = "";
		try {
			while ((s = readLine()) != null) {
				list.add(s.trim());
			}
		} catch (Exception e) {
			// LogUtils.log(FileUtils.class, e, LogUtils.FATAL);
			logger.error(e.getMessage(), e);
		}
		return list;
	}

	/**
	 * the file is valid
	 * 
	 * @return
	 */
	public boolean isValid(String endwith) {
		return true;
	}

	/**
	 * write a byte[] to the file
	 * 
	 * @param b
	 * @param off
	 * @param len
	 * @return
	 */
	public synchronized boolean write(byte[] b, int off, int len) {
		try {
			openWrite();
			if (out != null) {
				out.write(b, off, len);
				return true;
			}
		} catch (Exception e) {
			// LogUtils.log(FileUtils.class, e, LogUtils.FATAL);
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * write a byte[] to the file
	 * 
	 * @param b
	 * @return
	 */
	public synchronized boolean write(byte[] b) {
		if (b == null)
			return false;
		return write(b, 0, b.length);
	}

	/**
	 * write a char[] to the file
	 * 
	 * @param b
	 * @param off
	 * @param len
	 * @return
	 */
	public synchronized boolean write(char[] c, int off, int len) {
		try {
			openWrite();
			if (writer != null) {
				writer.write(c, off, len);
				return true;
			}
		} catch (Exception e) {
			// LogUtils.log(FileUtils.class, e, LogUtils.FATAL);
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * write a char[] to the file
	 * 
	 * @param b
	 * @return
	 */
	public synchronized boolean write(char[] c) {
		if (c == null)
			return false;
		return write(c, 0, c.length);
	}

	/**
	 * write a String to the file
	 * 
	 * @param s
	 */
	public synchronized boolean write(String s) {
		try {
			openWrite();
			if (writer != null) {
				writer.print(s);
				return true;
			}
		} catch (Exception e) {
			// LogUtils.log(FileUtils.class, e, LogUtils.FATAL);
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * write a String to the file
	 * 
	 * @param s
	 * @param file
	 */
	public static synchronized boolean write(String s, File file, boolean append) {
		try {
			PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(file, append)));
			writer.print(s);
			writer.close();
			writer = null;
			return true;
		} catch (Exception e) {
			// LogUtils.log(FileUtils.class, e, LogUtils.FATAL);
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * write a String to the file
	 * 
	 * @param s
	 * @param file
	 */
	public static synchronized boolean write(byte[] b, File file, boolean append) {
		try {
			BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(file, append));
			writer.write(b);
			writer.flush();
			writer.close();
			writer = null;
			return true;
		} catch (Exception e) {
			// LogUtils.log(FileUtils.class, e, LogUtils.FATAL);
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * write a String to the file
	 * 
	 * @param s
	 * @param file
	 */
	public static synchronized boolean writep(String s, File file, boolean append) {
		try {
			OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(file, append), "UTF-8");
			os.write(s);
			os.close();
			return true;
		} catch (Exception e) {
			// LogUtils.log(FileUtils.class, e, LogUtils.FATAL);
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * write a String to the file
	 * 
	 * @param s
	 * @param relativePath
	 */
	public static synchronized boolean write(String s, String absolutePath, boolean append) {
		File file = new File(absolutePath);
		return write(s, file, append);
	}

	/**
	 * write a Object to the file
	 * 
	 * @param o
	 * @param file
	 */
	public static void writeObject(Object o, String file) {
		try {
			FileOutputStream out = new FileOutputStream(file);
			ObjectOutputStream output = new ObjectOutputStream(out);
			output.writeObject(o);
			output.close();
		} catch (Exception e) {
			// LogUtils.log(FileUtils.class, e, LogUtils.FATAL);
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * read a Object from the file
	 * 
	 * @param file
	 * @return
	 */
	public static Object readObject(String file) {
		try {
			FileInputStream in = new FileInputStream(file);
			ObjectInputStream input = new ObjectInputStream(in);
			Object o = input.readObject();
			input.close();
			return o;
		} catch (Exception e) {
			// LogUtils.log(FileUtils.class, e, LogUtils.FATAL);
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * delete a file or directory
	 * 
	 * @param file
	 */
	public static boolean deleteFile(File file) {
		boolean b = true;
		if (!file.exists())
			return !b;
		if (file.isFile()) {
			if (!file.delete())
				b = false;
		} else {
			File[] files = file.listFiles();
			for (File f : files) {
				if (!deleteFile(f))
					b = false;
			}
			if (!file.delete())
				b = false;
		}
		return b;
	}

	/**
	 * delete a file or directory
	 * 
	 * @param file
	 */
	public static boolean deleteFile(String filename) {
		File f = new File(filename);
		return deleteFile(f);
	}

	public File getFile() {
		return file;
	}

	/**
	 * get the files by the absolutePath
	 * 
	 * @param s
	 * @param file
	 */
	public static File[] getFiles(String absolutePath) {
		try {
			File file = new File(absolutePath);
			if (file.isDirectory()) {
				return file.listFiles();
			}

		} catch (Exception e) {
			// LogUtils.log(FileUtils.class, e, LogUtils.FATAL);
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * get all files in a directory include its sub directory
	 * 
	 * @param absolutePath
	 */
	public static List<File> getAllFiles(String absolutePath) {
		List<File> list = new ArrayList<File>();
		try {
			File file = new File(absolutePath);
			if (file.isFile()) {
				list.add(file);
				return list;
			} else {
				for (File f : file.listFiles()) {
					list.addAll(getAllFiles(f.getAbsolutePath()));
				}
			}
		} catch (Exception e) {
			// LogUtils.log(FileUtils.class, e, LogUtils.FATAL);
			logger.error(e.getMessage(), e);
		}
		return list;
	}

	/**
	 * get the file by the absolutePath
	 * 
	 * @param s
	 * @param file
	 */
	public static File getFile(String absolutePath) {
		try {
			File file = new File(absolutePath);
			return file;

		} catch (Exception e) {
			// LogUtils.log(FileUtils.class, e, LogUtils.FATAL);
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	// /**
	// * extract a tar file to a File
	// *
	// * @param file
	// * @param directory
	// */
	// public static boolean extractTar(File srcFile, File destFile) {
	// if (!exists(srcFile)) return false;
	// return extractTar(srcFile, destFile, false);
	// }
	//
	// /**
	// * extract a gz file to a File
	// *
	// * @param file
	// * @param directory
	// * @return
	// */
	// public static boolean extractTargz(File srcFile, File destFile) {
	// if (!exists(srcFile)) return false;
	// return extractTar(srcFile, destFile, true);
	// }
	//
	// /**
	// * extract a tar or tar.gz file to a File
	// *
	// * @param file
	// * @param directory
	// * @param isGz
	// * @return
	// */
	// private static boolean extractTar(File srcFile, File destFile, boolean
	// isGz) {
	// TarArchive archive;
	// long starttime = System.currentTimeMillis();
	// try {
	// InputStream in = new FileInputStream(srcFile);
	// if (isGz) {
	// in = new GZIPInputStream(in);
	// }
	// archive = new TarArchive(in);
	// archive.extractContents(destFile);
	// archive.closeArchive();
	// } catch (Exception e) {
	// LogUtils.log(FileUtils.class, e, LogUtils.FATAL);
	// return false;
	// }
	// long endtime = System.currentTimeMillis();
	// LogUtils.log(FileUtils.class, "extracet tar time - " + (endtime -
	// starttime), LogUtils.INFO);
	// return true;
	// }
	//
	// /**
	// * tar a File to a tar file
	// *
	// * @param directory
	// * @param file
	// */
	// public static boolean tar(File srcFile, File destFile) {
	// if (!exists(srcFile)) return false;
	// return tar(srcFile, destFile, false);
	// }
	//
	// /**
	// * gz a File to a tar file
	// *
	// * @param directory
	// * @param file
	// * @return
	// */
	// public static boolean targz(File srcFile, File destFile) {
	// if (!exists(srcFile)) return false;
	// return tar(srcFile, destFile, true);
	// }
	//
	// /**
	// * tar or gz a File to a tar or tar.gz file
	// *
	// * @param directory tar or tar.gz
	// * @param file
	// * @param isGz
	// * @return
	// */
	// private static boolean tar(File srcFile, File destFile, boolean isGz) {
	// TarArchive archive;
	// long starttime = System.currentTimeMillis();
	// try {
	// OutputStream out = new FileOutputStream(destFile);
	// if (isGz) {
	// out = new GZIPOutputStream(out);
	// }
	// archive = new TarArchive(out);
	// TarEntry tarEntry = new TarEntry(srcFile);
	// archive.writeEntry(tarEntry,true);
	// archive.closeArchive();
	// } catch (Exception e) {
	// LogUtils.log(FileUtils.class, e, LogUtils.FATAL);
	// return false;
	// }
	// long endtime = System.currentTimeMillis();
	// LogUtils.log(FileUtils.class, "tar time - " + (endtime - starttime),
	// LogUtils.INFO);
	// return true;
	// }
	//
	// /**
	// * extract the gzip-file from srcFile to destFile
	// *
	// * @param srcFile
	// * @param destFile
	// * @return
	// */
	// public static boolean extractGz(File srcFile, File destFile) {
	// try {
	// GZIPInputStream gzipInputStream = null;
	// gzipInputStream = new GZIPInputStream(new FileInputStream(srcFile));
	// OutputStream out = new FileOutputStream(destFile);
	// byte[] buf = new byte[1024];
	// int len;
	// while ((len = gzipInputStream.read(buf)) > 0) {
	// out.write(buf, 0, len);
	// }
	// gzipInputStream.close();
	// out.close();
	// } catch (Exception e) {
	//
	// }
	// return true;
	// }

	/**
	 * whether is not file exists
	 * 
	 * @param file
	 * @return
	 */
	public static boolean exists(File file) {
		if (!file.exists()) {
			// LogUtils.log(FileUtils.class, "directory - " +
			// file.getAbsolutePath() + " not exists  - ", LogUtils.WARN);
			logger.warn("directory - " + file.getAbsolutePath() + " not exists  - ");
			return false;
		}
		return true;
	}

	/**
	 * whether is not filename exists
	 * 
	 * @param filename
	 * @return
	 */
	public static boolean exists(String filename) {
		File file = new File(filename);
		if (!file.exists()) {
			logger.warn("directory - " + file.getAbsolutePath() + " not exists  - ");
			// LogUtils.log(FileUtils.class, "directory - " +
			// file.getAbsolutePath() + " not exists  - ", LogUtils.WARN);
			return false;
		}
		return true;
	}

	public static boolean mkdir(String dir) {
		File file = new File(dir);
		if (!file.exists()) {

			if (!file.mkdir()) {
				logger.warn("creating directory - " + file.getAbsolutePath() + " not exists  - ");
				return false;
			}
		}
		return true;
	}

	public static boolean waitLock(String key) {
		if (!fileLock.containsKey(key)) {
			setLock(key);
		} else {
			try {
				logger.info("wait the syn-lock of " + key);
				// LogUtils.log(FileUtils.class, "wait the syn-lock of " + key,
				// LogUtils.INFO);
				fileLock.get(key).take();
				logger.info("get the syn-lock of " + key);
				// LogUtils.log(FileUtils.class, "get the syn-lock of " + key,
				// LogUtils.INFO);
			} catch (Exception e) {
				// LogUtils.log(FileUtils.class, e, LogUtils.FATAL);
				logger.error(e.getMessage(), e);
			}
		}
		return true;
	}

	private static synchronized void setLock(String key) {
		if (!fileLock.containsKey(key)) {
			fileLock.put(key, new LinkedBlockingQueue<Object>());
			// LogUtils.log(FileUtils.class, "create the syn-lock of " + key,
			// LogUtils.INFO);
			logger.info("create the syn-lock of " + key);
		} else {
			waitLock(key);
		}
	}

	public static void releaseLock(String key) {
		if (fileLock.containsKey(key)) {
			try {
				fileLock.get(key).put(new Object());
				// LogUtils.log(FileUtils.class, "release the syn-lock of " +
				// key, LogUtils.INFO);
				logger.info("release the syn-lock of " + key);
			} catch (Exception e) {
				// LogUtils.log(FileUtils.class, e, LogUtils.FATAL);
				logger.error(e.getMessage(), e);
			}
		}
	}

	public static synchronized Properties setPropertiesUsingFile(String filename) {
		if (filename == null) {
			return null;
		}

		// try to load the properties file
		Properties props = new Properties();
		File file = new File(filename);
		if (file.exists()) {
			try {
				InputStream is = new FileInputStream(file);
				props.load(is);
				is.close();
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage(), e);
				// LogUtils.log(FileUtils.class, e.getMessage(),
				// LogUtils.ERROR);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				// LogUtils.log(FileUtils.class, e.getMessage(),
				// LogUtils.ERROR);
			}
		} else {
			// // Try to load it as a resource in the CLASSPATH
			// try
			// {
			// InputStream is =
			// PersistenceConfiguration.class.getClassLoader().getResourceAsStream(filename);
			// props.load(is);
			// is.close();
			// }
			// catch (Exception e)
			// {
			//
			// }
		}

		return props;
	}

	public static String getFileSuffix(String filePath) {

		String result = null;

		if (filePath == null || filePath.length() <= 0) {
			return result;
		}

		int index = filePath.lastIndexOf(".");

		if (index == -1) {
			return result;
		}
		result = filePath.substring(index + 1, filePath.length());
		return result;

	}

	public static void writeFile(File file, InputStream is) throws IOException {
		FileOutputStream fos = org.apache.commons.io.FileUtils.openOutputStream(file);
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;

		try {
			bos = new BufferedOutputStream(fos);
			bis = new BufferedInputStream(is);
			byte[] b = new byte[FILE_IO_BLOCK_LEN];
			int c = bis.read(b, 0, FILE_IO_BLOCK_LEN);
			while (c > 0) {
				bos.write(b, 0, c);
				c = bis.read(b, 0, FILE_IO_BLOCK_LEN);
			}
			bos.flush();

		} finally {
			IOUtils.closeQuietly(bis);
			IOUtils.closeQuietly(bos);
			IOUtils.closeQuietly(fos);
		}
	}

}
