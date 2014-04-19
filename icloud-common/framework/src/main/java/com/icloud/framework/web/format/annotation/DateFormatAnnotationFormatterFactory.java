package com.icloud.framework.web.format.annotation;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.datetime.DateFormatter;

public class DateFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<DateFormat> {
	@Override
	public Set<Class<?>> getFieldTypes() {
		Set<Class<?>> fieldTypes = new HashSet<Class<?>>(1, 1);
		fieldTypes.add(Date.class);
		return fieldTypes;
	}

	@Override
	public Printer<?> getPrinter(DateFormat annotation, Class<?> fieldType) {
		return new DateFormatter(annotation.value());
	}

	@Override
	public Parser<?> getParser(DateFormat annotation, Class<?> fieldType) {
		return new DateFormatter(annotation.value());
	}
}
