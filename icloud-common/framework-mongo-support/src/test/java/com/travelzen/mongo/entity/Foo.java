package com.travelzen.mongo.entity;

import java.util.List;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;

@Entity("foo")
public class Foo implements MorphiaEntity<ObjectId> {

	@Id
	private ObjectId id;
	private String name;
	private String gender;
	private List<String> values;
	private DateTime updateTime;

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	@Override
	public ObjectId getId() {
		return id;
	}

	@Override
	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return String.format("name:%s\tgender:%s", this.name, this.gender);
	}

	@Override
	public void setId(String id) {
		this.id = new ObjectId(id);
	}

	public DateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(DateTime updateTime) {
		this.updateTime = updateTime;
	}

}
