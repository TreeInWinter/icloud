package com.icloud.framework.dao.util;

import org.apache.commons.lang3.StringUtils;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.query.Query;
import com.github.jmkgreen.morphia.query.UpdateOperations;
import com.icloud.framework.dao.mongo.entity.MDStoredSeqence;

public class MongoDBPKUtil {
	private final static String BASE_STR = "000000000000000000000000";
	private final static int LENGTH = BASE_STR.length();

	/**
	 * 获得指定实体类的递增主键
	 * 
	 * @param collName
	 * @param ds
	 * @return
	 */
	public static synchronized long getLongPKFromMongo(String collName, Datastore ds) {
		if (StringUtils.isBlank(collName))
			return 0l;
		Query<MDStoredSeqence> q = ds.find(MDStoredSeqence.class, "collName", collName); // 查询数据库有没有此实体的Seqence
		MDStoredSeqence mdStoredSeqence = q.get();
		// 此实体还没有Seqence，则新增一条Seqence记录
		if (mdStoredSeqence == null) {
			mdStoredSeqence = new MDStoredSeqence(collName);
			mdStoredSeqence.setValue(1l);
			ds.save(mdStoredSeqence);
		}
		UpdateOperations<MDStoredSeqence> ops = ds.createUpdateOperations(MDStoredSeqence.class);
		ops.set("value", mdStoredSeqence.getValue() + 1l);
		ds.update(q, ops);
		return mdStoredSeqence.getValue();
	}

	public static synchronized String getLongPKByStrFormat(String collName, Datastore ds) {
		return long2str(getLongPKFromMongo(collName, ds));
	}

	public static synchronized String long2str(long pk) {
		String pkStr = String.valueOf(pk);
		int length = pkStr.length();
		if (length < LENGTH)
			pkStr = BASE_STR.substring(0, LENGTH - length) + pkStr;
		return pkStr;
	}
}
