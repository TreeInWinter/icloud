/**
 * 
 */
package com.travelzen.framework.logger;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.status.ErrorStatus;

import com.github.jmkgreen.morphia.Morphia;
import com.github.jmkgreen.morphia.dao.BasicDAO;
import com.github.jmkgreen.morphia.dao.DAO;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.travelzen.framework.gson.factory.MongoGsonFactory;
import com.travelzen.framework.logger.entity.LogMessage;

/**
 * 继承自logback 的日志信息处理类 1、持久化log到mongodb 2、日志信息到业务通知实体对象Message的转化
 * 使用示例见：LibraLogToMongodbTest.java
 * 
 */
public class MongodbAppender extends AppenderBase<LoggingEvent> {
	private Mongo _mongo;
	private String _dbHost = "localhost";
	private int _dbPort = 27017;
	private String _dbName = "logging";
	protected DB _db;
	protected Morphia morphia = new Morphia();
	private DAO<LogMessage, ObjectId> messageDao;
	@Override
	public void start() {
		try {
			_mongo = new MongoClient(_dbHost, _dbPort);
			_db = _mongo.getDB(_dbName);
			morphia.map(LogMessage.class);
			messageDao = new BasicDAO<LogMessage,ObjectId>(LogMessage.class, _mongo, morphia, _db.getName());
		} catch (Exception e) {
			addStatus(new ErrorStatus("Failed to initialize MondoDB", this, e));
			return;
		}
		super.start();
	}

	public void setDbHost(String dbHost) {
		_dbHost = dbHost;
	}

	public void setDbName(String dbName) {
		_dbName = dbName;
	}

	public void setDbPort(int dbPort) {
		_dbPort = dbPort;
	}

	@Override
	public void stop() {
		_mongo.close();
		super.stop();
	}

//	private static CachingDateFormatter SDF = new CachingDateFormatter(
//			"yyyy-MM-dd HH:mm:ss");

	@Override
	protected void append(LoggingEvent e) {
		try {
			LogMessage msg = (LogMessage) MongoGsonFactory.createSimple().fromJson(
					e.getMessage(), LogMessage.class);
			msg.setCreated(new DateTime(e.getTimeStamp()));
			msg.setLevel(e.getLevel().levelInt);
			messageDao.save(msg);
		} catch (Exception ed) {
			ed.printStackTrace();
		}
	}



}
