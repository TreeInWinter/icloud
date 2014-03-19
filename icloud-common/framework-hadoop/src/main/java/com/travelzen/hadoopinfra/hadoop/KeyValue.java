package com.travelzen.hadoopinfra.hadoop;


public class KeyValue<K, V> {

	private K key;
	
	private V value;
	
	public KeyValue() {
	}
	
	public KeyValue(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}
	
	public void set(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	public boolean isSetKey() {
		return this.key != null;
	}
	
	public boolean isSetValue() {
		return this.value != null;
	}
	
	public boolean isSet() {
		return (this.key != null) || (this.value != null);
	}
	
	public boolean isSetAll() {
		return (this.key != null) && (this.value != null);
	}
	
}
