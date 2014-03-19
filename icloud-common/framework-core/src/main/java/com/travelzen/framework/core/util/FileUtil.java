/* FileUtils
 *
 * $Id: FileUtils.java 5863 2008-07-10 21:38:48Z gojomo $
 *
 * Created on Feb 2, 2004
 *
 * Copyright (C) 2004 Internet Archive.
 *
 * This file is part of the Heritrix web crawler (crawler.archive.org).
 *
 * Heritrix is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * any later version.
 *
 * Heritrix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with Heritrix; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.travelzen.framework.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Utility methods for manipulating files and directories.
 * 
 * @author John Erik Halse
 */
public class FileUtil {
	private static final Logger LOGGER = Logger.getLogger(FileUtil.class
			.getName());

	public static final File TMPDIR = new File(System.getProperty(
			"java.io.tmpdir", "/tmp"));

	private static final boolean DEFAULT_OVERWRITE = true;

	/**
	 * Constructor made private because all methods of this class are static.
	 */
	private FileUtil() {
		super();
	}

	public static int copyFiles(final File srcDir, Set<String> srcFile, final File dest)
			throws IOException {
		int count = 0;
		for (Iterator<String> i = srcFile.iterator(); i.hasNext();) {
			String name = i.next();
			File src = new File(srcDir, name);
			File tgt = new File(dest, name);
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.fine("Before " + src.getAbsolutePath() + " "
						+ src.exists() + ", " + tgt.getAbsolutePath() + " "
						+ tgt.exists());
			}
			copyFiles(src, tgt);
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.fine("After " + src.getAbsolutePath() + " "
						+ src.exists() + ", " + tgt.getAbsolutePath() + " "
						+ tgt.exists());
			}
			count++;
		}
		return count;
	}

	/**
	 * Recursively copy all files from one directory to another.
	 * 
	 * @param src
	 *            file or directory to copy from.
	 * @param dest
	 *            file or directory to copy to.
	 * @throws IOException
	 */
	public static void copyFiles(File src, File dest) throws IOException {
		copyFiles(src, null, dest, false, true, null);
	}

	/**
	 * @param src
	 *            Directory of files to fetch.
	 * @param filter
	 *            Filter to apply to filenames.
	 * @return Files in directory sorted.
	 */
	public static String[] getSortedDirContent(final File src,
			final FilenameFilter filter) {
		if (!src.exists()) {
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.fine(src.getAbsolutePath() + " does not exist");
			}
			return null;
		}

		if (!src.isDirectory()) {
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.fine(src.getAbsolutePath() + " is not directory.");
			}
			return null;
		}
		// Go through the contents of the directory
		String[] list = (filter == null) ? src.list() : src.list(filter);
		if (list != null) {
			Arrays.sort(list);
		}
		return list;
	}

	/**
	 * Recursively copy all files from one directory to another.
	 * 
	 * @param src
	 *            File or directory to copy from.
	 * @param filter
	 *            Filename filter to apply to src. May be null if no filtering
	 *            wanted.
	 * @param dest
	 *            File or directory to copy to.
	 * @param inSortedOrder
	 *            Copy in order of natural sort.
	 * @param overwrite
	 *            If target file already exits, and this parameter is true,
	 *            overwrite target file (We do this by first deleting the target
	 *            file before we begin the copy).
	 * @throws IOException
	 */
	public static void copyFiles(final File src, final FilenameFilter filter,
			final File dest, final boolean inSortedOrder,
			final boolean overwrite) throws IOException {
		copyFiles(src, filter, dest, inSortedOrder, overwrite, null);
	}

	/**
	 * Recursively copy all files from one directory to another.
	 * 
	 * @param src
	 *            File or directory to copy from.
	 * @param filter
	 *            Filename filter to apply to src. May be null if no filtering
	 *            wanted.
	 * @param dest
	 *            File or directory to copy to.
	 * @param inSortedOrder
	 *            Copy in order of natural sort.
	 * @param overwrite
	 *            If target file already exits, and this parameter is true,
	 *            overwrite target file (We do this by first deleting the target
	 *            file before we begin the copy).
	 * @param exceptions
	 *            if non-null, add any individual-file IOExceptions to this List
	 *            rather than throwing, and proceed with the deep copy
	 * @return TODO
	 * @throws IOException
	 */
	public static void copyFiles(final File src, final FilenameFilter filter,
			final File dest, final boolean inSortedOrder,
			final boolean overwrite, List<IOException> exceptions)
			throws IOException {
		// TODO: handle failures at any step
		if (!src.exists()) {
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.fine(src.getAbsolutePath() + " does not exist");
			}
			return;
		}

		if (src.isDirectory()) {
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.fine(src.getAbsolutePath() + " is a directory.");
			}
			// Create destination directory
			if (!dest.exists()) {
				dest.mkdirs();
			}
			// Go through the contents of the directory
			String list[] = (filter == null) ? src.list() : src.list(filter);
			if (inSortedOrder) {
				Arrays.sort(list);
			}
			for (int i = 0; i < list.length; i++) {
				copyFiles(new File(src, list[i]), filter, new File(dest,
						list[i]), inSortedOrder, overwrite, exceptions);
			}
		} else {
			try {
				copyFile(src, dest, overwrite);
			} catch (IOException ioe) {
				if (exceptions != null) {
					exceptions.add(ioe);
				} else {
					// rethrow
					throw ioe;
				}
			}
		}
	}

	/**
	 * Copy the src file to the destination.
	 * 
	 * @param src
	 * @param dest
	 * @return True if the extent was greater than actual bytes copied.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean copyFile(final File src, final File dest)
			throws FileNotFoundException, IOException {
		return copyFile(src, dest, -1, DEFAULT_OVERWRITE);
	}

	/**
	 * Copy the src file to the destination.
	 * 
	 * @param src
	 * @param dest
	 * @param overwrite
	 *            If target file already exits, and this parameter is true,
	 *            overwrite target file (We do this by first deleting the target
	 *            file before we begin the copy).
	 * @return True if the extent was greater than actual bytes copied.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean copyFile(final File src, final File dest,
			final boolean overwrite) throws FileNotFoundException, IOException {
		return copyFile(src, dest, -1, overwrite);
	}

	/**
	 * Copy up to extent bytes of the source file to the destination
	 * 
	 * @param src
	 * @param dest
	 * @param extent
	 *            Maximum number of bytes to copy
	 * @return True if the extent was greater than actual bytes copied.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean copyFile(final File src, final File dest, long extent)
			throws FileNotFoundException, IOException {
		return copyFile(src, dest, extent, DEFAULT_OVERWRITE);
	}

	/**
	 * Copy up to extent bytes of the source file to the destination
	 * 
	 * @param src
	 * @param dest
	 * @param extent
	 *            Maximum number of bytes to copy
	 * @param overwrite
	 *            If target file already exits, and this parameter is true,
	 *            overwrite target file (We do this by first deleting the target
	 *            file before we begin the copy).
	 * @return True if the extent was greater than actual bytes copied.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean copyFile(final File src, final File dest,
			long extent, final boolean overwrite) throws FileNotFoundException,
			IOException {
		boolean result = false;
		if (LOGGER.isLoggable(Level.FINE)) {
			LOGGER.fine("Copying file " + src + " to " + dest + " extent "
					+ extent + " exists " + dest.exists());
		}
		if (dest.exists()) {
			if (overwrite) {
				dest.delete();
				LOGGER.finer(dest.getAbsolutePath() + " removed before copy.");
			} else {
				// Already in place and we're not to overwrite. Return.
				return result;
			}
		}
		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel fcin = null;
		FileChannel fcout = null;
		try {
			// Get channels
			fis = new FileInputStream(src);
			fos = new FileOutputStream(dest);
			fcin = fis.getChannel();
			fcout = fos.getChannel();
			if (extent < 0) {
				extent = fcin.size();
			}

			// Do the file copy
			long trans = fcin.transferTo(0, extent, fcout);
			if (trans < extent) {
				result = false;
			}
			result = true;
		} catch (IOException e) {
			// Add more info to the exception. Preserve old stacktrace.
			// We get 'Invalid argument' on some file copies. See
			// http://intellij.net/forums/thread.jsp?forum=13&thread=63027&message=853123
			// for related issue.
			String message = "Copying " + src.getAbsolutePath() + " to "
					+ dest.getAbsolutePath() + " with extent " + extent
					+ " got IOE: " + e.getMessage();
			if (e.getMessage().equals("Invalid argument")) {
				LOGGER.severe("Failed copy, trying workaround: " + message);
				workaroundCopyFile(src, dest);
			} else {
				LOGGER.log(Level.SEVERE, message, e);
				// rethrow
				throw e;
			}
		} finally {
			// finish up
			if (fcin != null) {
				fcin.close();
			}
			if (fcout != null) {
				fcout.close();
			}
			if (fis != null) {
				fis.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
		return result;
	}

	protected static void workaroundCopyFile(final File src, final File dest)
			throws IOException {
		FileInputStream from = null;
		FileOutputStream to = null;
		try {
			from = new FileInputStream(src);
			to = new FileOutputStream(dest);
			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = from.read(buffer)) != -1) {
				to.write(buffer, 0, bytesRead);
			}
		} finally {
			if (from != null) {
				try {
					from.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (to != null) {
				try {
					to.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Deletes all files and subdirectories under dir.
	 * 
	 * @param dir
	 * @return true if all deletions were successful. If a deletion fails, the
	 *         method stops attempting to delete and returns false.
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// The directory is now empty so delete it
		return dir.delete();
	}

	/**
	 * Utility method to read an entire file as a String.
	 * 
	 * @param file
	 * @return File as String.
	 * @throws IOException
	 */
	public static String readFileAsString(File file) throws IOException {
		StringBuffer sb = new StringBuffer((int) file.length());
		String line;
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));
		try {
			line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
		} finally {
			br.close();
		}
		return sb.toString();
	}

	/**
	 * Get a list of all files in directory that have passed prefix.
	 * 
	 * @param dir
	 *            Dir to look in.
	 * @param prefix
	 *            Basename of files to look for. Compare is case insensitive.
	 * 
	 * @return List of files in dir that start w/ passed basename.
	 */
	public static File[] getFilesWithPrefix(File dir, final String prefix) {
		FileFilter prefixFilter = new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().toLowerCase()
						.startsWith(prefix.toLowerCase());
			}
		};
		return dir.listFiles(prefixFilter);
	}

	/**
	 * Get a @link java.io.FileFilter that filters files based on a regular
	 * expression.
	 * 
	 * @param regexp
	 *            the regular expression the files must match.
	 * @return the newly created filter.
	 */
	public static FileFilter getRegexpFileFilter(String regexp) {
		// Inner class defining the RegexpFileFilter
		class RegexpFileFilter implements FileFilter {
			Pattern pattern;

			protected RegexpFileFilter(String re) {
				pattern = Pattern.compile(re);
			}

			public boolean accept(File pathname) {
				return pattern.matcher(pathname.getName()).matches();
			}
		}

		return new RegexpFileFilter(regexp);
	}

	/**
	 * Use for case where files are being added to src. Will break off copy when
	 * tgt is same as src.
	 * 
	 * @param src
	 *            Source directory to copy from.
	 * @param tgt
	 *            Target to copy to.
	 * @param filter
	 *            Filter to apply to files to copy.
	 * @throws IOException
	 */
	public static void syncDirectories(final File src,
			final FilenameFilter filter, final File tgt) throws IOException {
		Set<String> srcFilenames = null;
		do {
			srcFilenames = new HashSet<String>(Arrays.asList(src.list(filter)));
			List<String> tgtFilenames = Arrays.asList(tgt.list(filter));
			srcFilenames.removeAll(tgtFilenames);
			if (srcFilenames.size() > 0) {
				int count = FileUtil.copyFiles(src, srcFilenames, tgt);
				if (LOGGER.isLoggable(Level.FINE)) {
					LOGGER.fine("Copied " + count);
				}
			}
		} while (srcFilenames != null && srcFilenames.size() > 0);
	}

	/**
	 * Test file exists and is readable.
	 * 
	 * @param f
	 *            File to test.
	 * @exception IOException
	 *                If file does not exist or is not unreadable.
	 */
	public static File isReadable(final File f) throws IOException {
		if (!f.exists()) {
			throw new FileNotFoundException(f.getAbsolutePath()
					+ " does not exist.");
		}

		if (!f.canRead()) {
			throw new FileNotFoundException(f.getAbsolutePath()
					+ " is not readable.");
		}

		return f;
	}

	/**
	 * @param f
	 *            File to test.
	 * @return True if file is readable, has uncompressed extension, and magic
	 *         string at file start.
	 * @exception IOException
	 *                If file does not exist or is not readable.
	 */
	public static boolean isReadableWithExtensionAndMagic(final File f,
			final String uncompressedExtension, final String magic)
			throws IOException {
		boolean result = false;
		FileUtil.isReadable(f);
		if (f.getName().toLowerCase().endsWith(uncompressedExtension)) {
			FileInputStream fis = new FileInputStream(f);
			try {
				byte[] b = new byte[magic.length()];
				int read = fis.read(b, 0, magic.length());
				fis.close();
				if (read == magic.length()) {
					StringBuffer beginStr = new StringBuffer(magic.length());
					for (int i = 0; i < magic.length(); i++) {
						beginStr.append((char) b[i]);
					}

					if (beginStr.toString().equalsIgnoreCase(magic)) {
						result = true;
					}
				}
			} finally {
				fis.close();
			}
		}

		return result;
	}

	/**
	 * Turn path into a File, relative to context (which may be ignored if path
	 * is absolute).
	 * 
	 * @param context
	 *            File context if path is relative
	 * @param path
	 *            String path to make into a File
	 * @return File created
	 */
	public static File maybeRelative(File context, String path) {
		File f = new File(path);
		if (f.isAbsolute()) {
			return f;
		}
		return new File(context, path);
	}

	/**
	 * Delete the file now -- but in the event of failure, keep trying in the
	 * future.
	 * 
	 * VERY IMPORTANT: Do not use with any file whose name/path may be reused,
	 * because the lagged delete could then wind up deleting the newer file.
	 * Essentially, only to be used with uniquely-named temp files.
	 * 
	 * Necessary because some platforms (looking at you, JVM-on-Windows) will
	 * have deletes fail because of things like file-mapped buffers remaining,
	 * and there's no explicit way to unmap a buffer. (See 6-year-old
	 * Sun-stumping Java bug
	 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4724038 ) We just have
	 * to wait and retry.
	 * 
	 * (Why not just File.deleteOnExit? There could be an arbitrary, unbounded
	 * number of files in such a situation, that are only deletable a few
	 * seconds or minutes after our first attempt. Waiting for JVM exist could
	 * mean disk exhaustion. It's also unclear if the native FS class
	 * implementations of deleteOnExit use RAM per pending file.)
	 * 
	 * @param fileToDelete
	 */
	public static synchronized void deleteSoonerOrLater(File fileToDelete) {
		pendingDeletes.add(fileToDelete);
		// if things are getting out of hand, force gc/finalization
		if (pendingDeletes.size() > 50) {
			LOGGER.warning(">50 pending Files to delete; forcing gc/finalization");
			System.gc();
			System.runFinalization();
		}
		// try all pendingDeletes
		Iterator<File> iter = pendingDeletes.listIterator();
		while (iter.hasNext()) {
			File pending = iter.next();
			if (pending.delete()) {
				iter.remove();
			}
		}
		// if things are still out of hand, complain loudly
		if (pendingDeletes.size() > 50) {
			LOGGER.severe(">50 pending Files to delete even after gc/finalization");
		}
	}

	static LinkedList<File> pendingDeletes = new LinkedList<File>();

	/*----------------------------------------------------------------------*\
	Constructor
	\*----------------------------------------------------------------------*/

	/*----------------------------------------------------------------------*\
	 Public Methods
	 \*----------------------------------------------------------------------*/

	/**
	 * Determine whether a string represents an absolute path. On Unix, an
	 * absolute path must start with a "/". On Windows, it must begin with one
	 * of the machine's valid drive letters.
	 * 
	 * @param path
	 *            the path to check
	 * 
	 * @return <tt>true</tt> if it's absolute, <tt>false</tt> if not
	 * 
	 * @throws IOException
	 *             on error
	 */
	public static boolean isAbsolutePath(String path) throws IOException {
		// It's important not to use java.util.File.listRoots(), for two
		// reasons:
		//
		// 1. On Windows, the floppy can be one of the roots. If there isn't
		// a disk in the floppy drive, some versions of Windows will issue
		// an "Abort/Continue/Retry" pop-up.
		// 2. If a security manager is installed, listRoots() can return
		// a null or empty array.
		//
		// So, this version analyzes the pathname textually.

		boolean isAbsolute = false;
		String fileSep = System.getProperty("file.separator");

		if (fileSep.equals("/")) {
			// Unix.

			isAbsolute = path.startsWith("/");
		}

		else if (fileSep.equals("\\")) {
			// Windows. Must start with something that looks like a drive
			// letter.

			isAbsolute = (Character.isLetter(path.charAt(0)))
					&& (path.charAt(1) == ':') && (path.charAt(2) == '\\');
		}

		else {
			throw new IOException("Can't determine operating system from "
					+ "file separator \"" + fileSep + "\"");
		}

		return isAbsolute;
	}

	/**
	 * Copy an <tt>InputStream</tt> to an <tt>OutputStream</tt>. If either
	 * stream is not already buffered, then it's wrapped in the corresponding
	 * buffered stream (i.e., <tt>BufferedInputStream</tt> or
	 * <tt>BufferedOutputStream</tt>) before copying. Calling this method is
	 * equivalent to:
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 * copyStream(src, dst, 8192);
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param is
	 *            the source <tt>InputStream</tt>
	 * @param os
	 *            the destination <tt>OutputStream</tt>
	 * 
	 * @return total number of bytes copied
	 * 
	 * @throws IOException
	 *             on error
	 * 
	 * @see #copyStream(InputStream,OutputStream,int)
	 * @see #copyReader(Reader,Writer)
	 * @see #copyFile(File,File)
	 */
	public static int copyStream(InputStream is, OutputStream os)
			throws IOException {
		return copyStream(is, os, -1);
	}

	/**
	 * Copy an <tt>InputStream</tt> to an <tt>OutputStream</tt>. If either
	 * stream is not already buffered, then it's wrapped in the corresponding
	 * buffered stream (i.e., <tt>BufferedInputStream</tt> or
	 * <tt>BufferedOutputStream</tt>) before copying.
	 * 
	 * @param src
	 *            the source <tt>InputStream</tt>
	 * @param dst
	 *            the destination <tt>OutputStream</tt>
	 * @param bufferSize
	 *            the buffer size to use, or -1 for a default
	 * 
	 * @return total number of bytes copied
	 * 
	 * @throws IOException
	 *             on error
	 * 
	 * @see #copyReader(Reader,Writer,int)
	 * @see #copyStream(InputStream,OutputStream)
	 * @see #copyFile(File,File)
	 */
	public static int copyStream(InputStream src, OutputStream dst,
			int bufferSize) throws IOException {
		int totalCopied = 0;

		if (!(src instanceof BufferedInputStream)) {
			if (bufferSize > 0)
				src = new BufferedInputStream(src, bufferSize);
			else
				src = new BufferedInputStream(src);
		}

		if (!(dst instanceof BufferedOutputStream)) {
			if (bufferSize > 0)
				dst = new BufferedOutputStream(dst, bufferSize);
			else
				dst = new BufferedOutputStream(dst);
		}

		int b;

		while ((b = src.read()) != -1) {
			dst.write(b);
			totalCopied++;
		}

		dst.flush();

		return totalCopied;
	}

	/**
	 * Copy characters from a reader to a writer. If the reader is not already
	 * buffered, then it's wrapped in a <tt>BufferedReader</tt>, using the
	 * specified buffer size. Similarly, buffered stream (i.e.,
	 * <tt>BufferedInputStream</tt> or If the writer is not already buffered,
	 * then it's wrapped in a <tt>BufferedWriter</tt>, using the specified
	 * buffer size.
	 * 
	 * @param reader
	 *            where to read from
	 * @param writer
	 *            where to write to
	 * @param bufferSize
	 *            buffer size to use, if reader and writer are not already
	 *            buffered, or -1 to use a default size.
	 * 
	 * @return total number of characters copied
	 * 
	 * @throws IOException
	 *             on error
	 * 
	 * @see #copyReader(Reader,Writer)
	 * @see #copyStream(InputStream,OutputStream,int)
	 * @see #copyStream(InputStream,OutputStream)
	 * @see #copyFile(File,File)
	 */
	public static int copyReader(Reader reader, Writer writer, int bufferSize)
			throws IOException {
		if (!(reader instanceof BufferedReader)) {
			if (bufferSize > 0)
				reader = new BufferedReader(reader, bufferSize);
			else
				reader = new BufferedReader(reader);
		}

		if (!(writer instanceof BufferedWriter)) {
			if (bufferSize > 0)
				writer = new BufferedWriter(writer, bufferSize);
			else
				writer = new BufferedWriter(writer);
		}

		int ch;
		int total = 0;

		while ((ch = reader.read()) != -1) {
			writer.write(ch);
			total++;
		}

		writer.flush();

		return total;
	}

	/**
	 * Copy characters from a reader to a writer, using a default buffer size.
	 * 
	 * @param reader
	 *            where to read from
	 * @param writer
	 *            where to write to
	 * 
	 * @return total number of characters copied
	 * 
	 * @throws IOException
	 *             on error
	 * 
	 * @see #copyReader(Reader,Writer)
	 * @see #copyStream(InputStream,OutputStream,int)
	 * @see #copyStream(InputStream,OutputStream)
	 * @see #copyFile(File,File)
	 */
	public static int copyReader(Reader reader, Writer writer)
			throws IOException {
		return copyReader(reader, writer, -1);
	}

	/**
	 * Copy one file to another, character by character, possibly doing
	 * character set conversions
	 * 
	 * @param src
	 *            the file to copy
	 * @param srcEncoding
	 *            the character set encoding for the source file, or null to
	 *            assume the default
	 * @param dst
	 *            Where to copy it. Can be a directory or a file.
	 * @param dstEncoding
	 *            the character set encoding for the destination file, or null
	 *            to assume the default
	 * 
	 * @return total number of characters copied
	 * 
	 * @throws IOException
	 *             on error
	 * 
	 * @see #copyFile(File,File)
	 * @see #copyReader(Reader,Writer,int)
	 * @see #copyReader(Reader,Writer)
	 * @see #copyStream(InputStream,OutputStream,int)
	 * @see #copyStream(InputStream,OutputStream)
	 */
	public static int copyTextFile(File src, String srcEncoding, File dst,
			String dstEncoding) throws IOException {
		if (dst.isDirectory())
			dst = new File(dst, src.getName());

		Reader reader;
		Writer writer;

		if (srcEncoding != null) {
			reader = new InputStreamReader(new FileInputStream(src),
					srcEncoding);
		}

		else {
			reader = new FileReader(src);
		}

		if (dstEncoding != null) {
			writer = new OutputStreamWriter(new FileOutputStream(dst),
					dstEncoding);
		}

		else {
			writer = new FileWriter(dst);
		}

		int total = copyReader(reader, writer);

		reader.close();
		writer.close();

		return total;
	}

	/**
	 * Get the virtual machine's default encoding.
	 * 
	 * @return the default encoding
	 */
	public static String getDefaultEncoding() {
		return java.nio.charset.Charset.defaultCharset().name();
	}

	/**
	 * Get the extension for a path or file name. Does not include the ".".
	 * 
	 * @param file
	 *            the file
	 * 
	 * @return the extension, or null if there isn't one
	 */
	public static String getFileNameExtension(File file) {
		return getFileNameExtension(file.getName());
	}

	/**
	 * Get the extension for a path or file name. Does not include the ".".
	 * 
	 * @param path
	 *            the file or path name
	 * 
	 * @return the extension, or null if there isn't one
	 */
	public static String getFileNameExtension(String path) {
		String ext = null;
		int i = path.lastIndexOf('.');

		if ((i != -1) && (i != (path.length() - 1)))
			ext = path.substring(i + 1);

		return ext;
	}

	/**
	 * Get the name of a file without its extension. Does not remove any parent
	 * directory components. Uses <tt>File.getAbsolutePath()</tt>, not
	 * <tt>File.getCanonicalPath()</tt> to get the path.
	 * 
	 * @param file
	 *            the file
	 * 
	 * @return the path without the extension
	 */
	public static String getFileNameNoExtension(File file) {
		return getFileNameNoExtension(file.getAbsolutePath());
	}

	/**
	 * Get the name of a file without its extension. Does not remove any parent
	 * directory components.
	 * 
	 * @param path
	 *            the path
	 * 
	 * @return the path without the extension
	 */
	public static String getFileNameNoExtension(String path) {
		int i = path.lastIndexOf('.');

		if (i != -1)
			path = path.substring(0, i);

		return path;
	}

	/**
	 * Get the name of a file's parent directory. This is the directory part of
	 * the filename. For instance, "/home/foo.zip" would return "/home". This
	 * method uses the file's absolute path.
	 * 
	 * @param fileName
	 *            the file name
	 * 
	 * @return directory name part of the file's absolute pathname
	 * 
	 * @see #dirname(File)
	 * @see #basename(String)
	 */
	public static String dirname(String fileName) {
		return dirname(new File(fileName));
	}

	/**
	 * Get the name of a file's parent directory. This is the directory part of
	 * the filename. For instance, "/home/foo.zip" would return "/home". This
	 * method uses the file's absolute path.
	 * 
	 * @param file
	 *            the file whose parent directory is to be returned
	 * 
	 * @return directory name part of the file's absolute pathname
	 * 
	 * @see #dirname(String)
	 * @see #basename(File)
	 */
	public static String dirname(File file) {
		String absName = file.getAbsolutePath();
		String fileSep = System.getProperty("file.separator");
		int lastSep = absName.lastIndexOf(fileSep);

		return absName.substring(0, lastSep);
	}

	/**
	 * Get the base (i.e., simple file) name of a file. This is the file name
	 * stripped of any directory information. For instance, "/home/foo.zip"
	 * would return "foo.zip".
	 * 
	 * @param fileName
	 *            name of the file to get the basename for
	 * 
	 * @return file name part of the file
	 * 
	 * @see #dirname(String)
	 */
	public static String basename(String fileName) {
		String fileSep = System.getProperty("file.separator");
		int lastSep = fileName.lastIndexOf(fileSep);

		return (lastSep == -1) ? fileName : fileName.substring(lastSep + 1);
	}

	/**
	 * Get the base (i.e., simple file) name of a file. This is the file name
	 * stripped of any directory information. For instance, "/home/foo.zip"
	 * would return "foo.zip".
	 * 
	 * @param file
	 *            the file to get the basename for
	 * 
	 * @return file name part of the file
	 * 
	 * @see #basename(String)
	 * @see #dirname(File)
	 */
	public static String basename(File file) {
		return basename(file.getName());
	}
}