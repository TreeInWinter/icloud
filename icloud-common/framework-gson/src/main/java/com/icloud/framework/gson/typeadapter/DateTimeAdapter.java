package com.icloud.framework.gson.typeadapter;

import java.io.IOException;

import org.joda.time.DateTime;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DateTimeAdapter extends TypeAdapter<DateTime> {
	public DateTime read(JsonReader reader) throws IOException {
		if (reader.peek() == JsonToken.NULL) {
			reader.nextNull();
			return null;
		}
		long millis = reader.nextLong();
		return new DateTime(millis);
	}

	public void write(JsonWriter writer, DateTime value) throws IOException {
		if (value == null) {
			writer.nullValue();
			return;
		}
		long millis = value.getMillis();
		writer.value(millis);
	}
}