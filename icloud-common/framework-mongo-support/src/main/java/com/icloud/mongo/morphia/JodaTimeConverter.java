package com.icloud.mongo.morphia;

import java.util.Date;

import org.joda.time.DateTime;

import com.github.jmkgreen.morphia.converters.DateConverter;
import com.github.jmkgreen.morphia.converters.SimpleValueConverter;
import com.github.jmkgreen.morphia.mapping.MappedField;
import com.github.jmkgreen.morphia.mapping.MappingException;

public class JodaTimeConverter extends DateConverter implements
SimpleValueConverter {

	public JodaTimeConverter() {
		super(DateTime.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object decode(Class targetClass, Object val,
			MappedField optionalExtraInfo) throws MappingException {
		Date d = (Date) super.decode(targetClass, val, optionalExtraInfo);
		return new DateTime(d);
	}

	@Override
	public Object encode(Object val, MappedField optionalExtraInfo) {
		if (val == null)
			return null;
		return ((DateTime) val).toDate();
	}

}
