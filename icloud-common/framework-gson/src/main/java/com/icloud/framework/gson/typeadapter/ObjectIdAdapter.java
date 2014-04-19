package com.icloud.framework.gson.typeadapter;

import java.io.IOException;

import org.bson.types.ObjectId;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class ObjectIdAdapter extends TypeAdapter<ObjectId> {
	public ObjectId read(JsonReader reader) throws IOException {
		if (reader.peek() == JsonToken.NULL) {
			reader.nextNull();
			return null;
		}
		String id = reader.nextString();
		return new ObjectId(id);
	}

	public void write(JsonWriter writer, ObjectId value) throws IOException {
		if (value == null) {
			writer.nullValue();
			return;
		}
		String id = value.toString();
		writer.value(id);
	}
}