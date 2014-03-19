package com.travelzen.mongo.morphia;

import java.net.UnknownHostException;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Test;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.travelzen.mongo.entity.Foo;

public class JodaTimeConverterTest {

	private Datastore ds;

	public JodaTimeConverterTest() throws UnknownHostException {
		Mongo mng = new MongoClient("127.0.0.1");
		ds = new Morphia().createDatastore(mng, "test");
		ds.getMapper().getConverters().addConverter(JodaTimeConverter.class);
	}

	@Test
	public void testConv() throws Exception {
		JodaTimeConverter dtc = new JodaTimeConverter();
		DateTime d1 = new DateTime();
		DateTime d2 = (DateTime) dtc.decode(DateTime.class, dtc.encode(d1));
		Assert.assertEquals(d1, d2);

		d1 = new DateTime("2012-12-18");
		d2 = (DateTime) dtc.decode(DateTime.class, dtc.encode(d1));
		Assert.assertEquals(d1, d2);
		Assert.assertEquals(2012, d2.getYear());
		Assert.assertEquals(18, d2.getDayOfMonth());
		Assert.assertEquals(0, d2.getMinuteOfHour());
	}

	@Test
	public void testConversion() throws Exception {

		Foo fi = new Foo();
		DateTime dt = new DateTime();

		fi.setGender("male");
		fi.setName("joda_time_converter_test");
		fi.setUpdateTime(dt);
		ds.save(fi);

		Foo fo = ds.find(Foo.class).get();
		junit.framework.Assert.assertNotNull(fi.getUpdateTime());
		junit.framework.Assert.assertEquals(dt, fi.getUpdateTime());
	}

}
