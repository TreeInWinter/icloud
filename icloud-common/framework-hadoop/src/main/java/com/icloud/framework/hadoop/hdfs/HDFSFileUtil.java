package com.icloud.framework.hadoop.hdfs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HDFSFileUtil {
	private static Logger logger = LoggerFactory
			.getLogger(HDFSFileUtil.class);


	/**
	 *  copy a inputStream to a hdfs directory
	 * @param inputStream
	 * @param dstFS
	 * @param dst
	 * @param overwrite
	 * @param conf
	 * @return
	 * @throws IOException
	 */
	public static boolean copyStream2File(InputStream inputStream,
			FileSystem dstFS, Path dstPath, Path renamePath , boolean overwrite, Configuration conf)
			throws IOException {

		OutputStream out = null;

		try {
			out = dstFS.create(dstPath, overwrite);
			IOUtils.copyBytes(inputStream, out, conf, true);

			IOUtils.closeStream(inputStream);
			IOUtils.closeStream(out);


			logger.info("finish writeStream2HDFS:{} -> {}", dstPath );

			FileSystem fs = dstPath.getFileSystem(conf);



			fs.rename(dstPath, renamePath);

			logger.info("finish rename:{} -> {}", renamePath );


		} catch (IOException e) {
			IOUtils.closeStream(out);
			IOUtils.closeStream(inputStream);
			throw e;
		}

		return true;
	}

}
