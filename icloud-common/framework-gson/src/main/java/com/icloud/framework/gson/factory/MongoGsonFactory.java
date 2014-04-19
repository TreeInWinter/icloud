package com.icloud.framework.gson.factory;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.icloud.framework.gson.typeadapter.DateTimeAdapter;
import com.icloud.framework.gson.typeadapter.ObjectIdAdapter;
 

public class MongoGsonFactory {
	
	/**
	 * 适配更多类型
	 * @return
	 */
	public static Gson create() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(DateTime.class,
				new DateTimeAdapter().nullSafe());
		builder.registerTypeAdapter(ObjectId.class,
				new ObjectIdAdapter().nullSafe());
 
		return builder.create();
	}
	/**
	 * 只适配了基类的两种类型(DateTime,ObjectId)
	 * @return
	 */
	public static Gson createSimple() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(DateTime.class,
				new DateTimeAdapter().nullSafe());
		builder.registerTypeAdapter(ObjectId.class,
				new ObjectIdAdapter().nullSafe());
		return builder.create();
	}
}
