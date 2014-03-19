package com.travelzen.framework.hadoop.hbase.util;

import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.travelzen.framework.core.config.SimplePropertiesUtil;

public class HBaseUtil {
	private static Logger logger = LoggerFactory.getLogger(HBaseUtil.class);

	private static Configuration cfg = null;

	static {
		Configuration HBASE_CONFIG = new Configuration();
		//be change from PropertiesUtil to SimplePropertiesUtil, may has bug
		Map<String,String> properties = SimplePropertiesUtil.mapProperties("zookeeper.properties");
		for (String key : properties.keySet())
			HBASE_CONFIG.set(key, (String) properties.get(key));
		cfg = HBaseConfiguration.create(HBASE_CONFIG);
	}

	public static void creatTable(String tableName) throws Exception {
		creatTable(tableName, new String[] { "cf" });
	}

	public static void creatTable(String tableName, String[] familyNames)
			throws Exception {
		HBaseAdmin admin = new HBaseAdmin(cfg);
		if (admin.tableExists(tableName)) {
			logger.warn("table Exists!");
		} else {
			HTableDescriptor tableDesc = new HTableDescriptor(tableName);
			for (String familyName : familyNames)
				tableDesc.addFamily(new HColumnDescriptor(familyName));
			admin.createTable(tableDesc);
			logger.info("create table ok.");
		}
	}

	public static void addData(String tableName, String row, String value)
			throws Exception {
		addData(tableName, "cf", row, "qualifier", value);
	}

	public static void addData(String tableName, String familyName, String row,
			String qualifier, String value) throws Exception {
		HTable table = new HTable(cfg, tableName);
		Put put = new Put(row.getBytes(), System.currentTimeMillis());
		put.add(familyName.getBytes(), qualifier.getBytes(), value.getBytes());
		table.put(put);
		table.close();
	}

	public static void addData(String tableName, String row,
			String[] qualifiers, String[] values) throws Exception {
		addData(tableName, "cf", row, qualifiers, values);
	}

	public static void addData(String tableName, String familyName, String row,
			String[] qualifiers, String[] values) throws Exception {
		HTable table = new HTable(cfg, tableName);
		Put put = new Put(row.getBytes(), System.currentTimeMillis());
		if (qualifiers.length == values.length)
			for (int i = 0; i < values.length; i++)
				put.add(familyName.getBytes(), qualifiers[i].getBytes(),
						values[i].getBytes());
		table.put(put);
		table.close();
	}

	public static String getData(String tableName, String row) throws Exception {
		return getData(tableName, "cf", row, "qualifier");
	}

	public static String getData(String tableName, String familyName,
			String row, String qualifier) throws Exception {
		return new String(new HTable(cfg, tableName).get(
				new Get(row.getBytes())).getValue(familyName.getBytes(),
				qualifier.getBytes()));
	}

	public static void getAllData(String tablename) throws Exception {
		ResultScanner scanner = new HTable(cfg, tablename)
				.getScanner(new Scan());
		for (Result result : scanner)
			for (KeyValue keyValue : result.raw())
				System.out.println(new String(keyValue.getRow()) + ":"
						+ new String(keyValue.getValue()));
	}
}
