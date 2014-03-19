package com.travelzen.framework.hadoop.hdfs;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.KeyValue.KeyComparator;
import org.apache.hadoop.hbase.io.hfile.Compression;
import org.apache.hadoop.hbase.io.hfile.HFile;
import org.apache.hadoop.hbase.io.hfile.HFileScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.travelzen.framework.core.util.ByteUtil;
import com.travelzen.framework.core.util.FPGenerator;

public class HDFSUtil {

	private static Logger logger = LoggerFactory.getLogger(HDFSUtil.class);
	private static long flushCnt = 0;
	private static long flushTotalMS = 0;

	public static void writeToHDFS(List<byte[]> mergedAdsKeyValueInfo)
			throws IOException {

		if (mergedAdsKeyValueInfo == null || mergedAdsKeyValueInfo.size() <= 0) {
			return;
		}

		Date date = new Date();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy_MM_dd");
		String todayStr = format1.format(date);

		SimpleDateFormat format2 = new SimpleDateFormat("HH_mm_ss");
		String timeStr = format2.format(date);

		Configuration conf = HBaseConfiguration.create();

		/*
		 * 判断输出文件夹是否存在
		 */
		Path outputDir = new Path(conf.get("hadoop.advs.dir") + "/output/"
				+ todayStr);
		FileSystem fs = outputDir.getFileSystem(conf);
		// 判断输出文件夹是否存在
		if (!fs.exists(outputDir)) { // 不存在则新建
			fs.mkdirs(outputDir);
		} else {
			// 如果存在此文件，但不是文件夹，则将此文件删除，然后重新新建文件夹
			FileStatus fileStatus = fs.getFileStatus(outputDir);
			if (!fileStatus.isDir()) {
				logger.info("error file dir:" + outputDir);
				fs.delete(outputDir, true);
				fs.mkdirs(outputDir);
			}
		}

		// 临时输出文件夹，和上面的逻辑一样
		Path tmpOutputDir = new Path(conf.get("hadoop.advs.dir")
				+ "/tmpOutput/" + todayStr);
		if (!fs.exists(tmpOutputDir)) {
			fs.mkdirs(tmpOutputDir);
		} else {
			FileStatus fileStatus = fs.getFileStatus(tmpOutputDir);
			if (!fileStatus.isDir()) {
				logger.info("error file dir:" + tmpOutputDir);
				fs.delete(tmpOutputDir, true);
				fs.mkdirs(tmpOutputDir);
			}
		}

		Path tmpOutputFile = new Path(tmpOutputDir.getParent() + "/"
				+ tmpOutputDir.getName() + "/advsMerged_doc-" + todayStr + "-"
				+ timeStr);

		Path outputFile = new Path(outputDir.getParent() + "/"
				+ outputDir.getName() + "/advsMerged_doc-" + todayStr + "-"
				+ timeStr);

		logger.info(tmpOutputFile.getParent() + "/" + tmpOutputFile.getName());
		logger.info(outputFile.getParent() + "/" + outputFile.getName());

		final String compression = conf.get("hfile.compression",
				Compression.Algorithm.NONE.getName());

		int blocksize = conf.getInt("hfile.min.blocksize.size", 65536);

		KeyComparator rawComparator = new KeyComparator();

		HFile.Writer writer = new HFile.Writer(fs, tmpOutputFile, blocksize,
				compression, rawComparator);

		long timestampBeginWrite = System.currentTimeMillis();

		try {
			// 追加到指定的文件
			for (byte[] das : mergedAdsKeyValueInfo) {
				Long fp = FPGenerator.std64.fp(das, 0, das.length);
				byte[] keyValueBytes = ByteUtil.longToByte(fp);
				byte[] keylegthBytes = ByteUtil.shortToByte((short) 8);
				byte[] keyBytes = new byte[10];
				System.arraycopy(keylegthBytes, 0, keyBytes, 0, 2);
				System.arraycopy(keyValueBytes, 0, keyBytes, 2, 8);
				writer.append(keyBytes, das);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			writer.close();
			logger.info("total byte:" + writer.getTotalBytes());
		}

		long timestampEndWrite = System.currentTimeMillis();
		/*
		 * 如下是计算的一些性能指标，时间等
		 */
		flushCnt++;

		long thisTimeMS = timestampEndWrite - timestampBeginWrite;

		flushTotalMS += thisTimeMS;

		double averageTime = flushTotalMS / flushCnt;

		logger.warn("one write time :" + thisTimeMS + "ms--averageTime:"
				+ averageTime);

		fs.rename(tmpOutputFile, outputFile);

	}

	public static Map<byte[], byte[]> readFromHDFS(String hdfsFiledir,
			String hdfsFileName) throws Exception {

		Map<byte[], byte[]> result = null;

		if (hdfsFiledir == null || hdfsFiledir.length() <= 0) {
			return result;
		}

		if (hdfsFileName == null || hdfsFileName.length() <= 0) {
			return result;
		}

		Path filedir = new Path(hdfsFiledir);
		Configuration conf = HBaseConfiguration.create();
		FileSystem fs = filedir.getFileSystem(conf);
		Path fileAbusolutePath = new Path(hdfsFiledir + hdfsFileName);

		if (!fs.exists(fileAbusolutePath)) {
			return result;
		}
		FileStatus fileStatus = fs.getFileStatus(fileAbusolutePath);
		if (fileStatus.isDir()) {
			return result;
		}

		HFile.Reader reader = new HFile.Reader(fs, fileAbusolutePath, null,
				false);
		reader.loadFileInfo();

		HFileScanner scanner = reader.getScanner(false, false);
		if (scanner == null) {
			return result;
		}

		result = new HashMap<byte[], byte[]>();
		scanner.seekTo();
		do {
			KeyValue keyValue = scanner.getKeyValue();
			result.put(keyValue.getKey(), keyValue.getValue());
		} while (scanner.next());

		return result;
	}

	public static void main(String[] args) {

		try {
			HDFSUtil.writeToHDFS(null);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
