package com.icloud.framework.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FieldUtil {
	private final static Logger logger = LoggerFactory.getLogger(FieldUtil.class);

	public static <T> String asFieldString(T[] ts, String name) {
		String s = asFieldSet(ts, name).toString();
		return s.substring(1, s.length() - 1);
	}

	@SuppressWarnings("unchecked")
	public static <T, P> P[] asFieldArray(T[] ts, String name) {
		return (P[]) asFieldList(ts, name).toArray();
	}

	@SuppressWarnings("unchecked")
	public static <T, P> List<P> asFieldList(T[] ts, String name) {
		List<P> l = new ArrayList<P>();
		for (T t : ts)
			try {
				Field field = t.getClass().getDeclaredField(name);
				field.setAccessible(true);
				Object e = field.get(t);
				if (e != null && StringUtils.isNotBlank(e.toString()))
					l.add((P) e);
			} catch (IllegalArgumentException e) {
				logger.error(e.getMessage(), e);
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			} catch (NoSuchFieldException e) {
				try {
					Object e1 = t.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1)).invoke(t);
					if (e1 != null && StringUtils.isNotBlank(e1.toString()))
						l.add((P) e1);
				} catch (InvocationTargetException e1) {
					logger.error(e1.getMessage(), e1);
				} catch (IllegalArgumentException e1) {
					logger.error(e1.getMessage(), e1);
				} catch (IllegalAccessException e1) {
					logger.error(e1.getMessage(), e1);
				} catch (NoSuchMethodException e1) {
					logger.error(e1.getMessage(), e1);
				} catch (SecurityException e1) {
					logger.error(e1.getMessage(), e1);
				}
			} catch (SecurityException e) {
				logger.error(e.getMessage(), e);
			}
		return l;
	}

	public static <T, P> Set<P> asFieldSet(T[] ts, String name) {
		Set<P> s = new LinkedHashSet<P>();
		List<P> l = asFieldList(ts, name);
		s.addAll(l);
		return s;
	}

	public static <T> String asFieldString(Collection<T> coll, String name) {
		return asFieldString(coll.toArray(), name);
	}

	public static <T, P> P[] asFieldArray(Collection<T> coll, String name) {
		return asFieldArray(coll.toArray(), name);
	}

	public static <T, P> List<P> asFieldList(Collection<T> coll, String name) {
		return asFieldList(coll.toArray(), name);
	}

	public static <T, P> Set<P> asFieldSet(Collection<T> coll, String name) {
		return asFieldSet(coll.toArray(), name);
	}

	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> coll2Map(Collection<V> coll, String name) {
		Map<K, V> map = new HashMap<K, V>();
		for (V v : coll)
			try {
				Field field = v.getClass().getDeclaredField(name);
				field.setAccessible(true);
				Object key = field.get(v);
				if (key != null && StringUtils.isNotBlank(key.toString()) && !map.containsKey(key))
					map.put((K) key, v);
			} catch (IllegalArgumentException e) {
				logger.error(e.getMessage(), e);
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			} catch (NoSuchFieldException e) {
				try {
					Object key = v.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1)).invoke(v);
					if (key != null && StringUtils.isNotBlank(key.toString()) && !map.containsKey(key))
						map.put((K) key, v);
				} catch (InvocationTargetException e1) {
					logger.error(e1.getMessage(), e1);
				} catch (IllegalArgumentException e1) {
					logger.error(e1.getMessage(), e1);
				} catch (IllegalAccessException e1) {
					logger.error(e1.getMessage(), e1);
				} catch (NoSuchMethodException e1) {
					logger.error(e1.getMessage(), e1);
				} catch (SecurityException e1) {
					logger.error(e1.getMessage(), e1);
				}
			} catch (SecurityException e) {
				logger.error(e.getMessage(), e);
			}
		return map;
	}

	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, List<V>> coll2Map4List(Collection<V> coll, String name) {
		Map<K, List<V>> map = new HashMap<K, List<V>>();
		for (V v : coll)
			try {
				Field field = v.getClass().getDeclaredField(name);
				field.setAccessible(true);
				Object key = field.get(v);
				if (key != null && StringUtils.isNotBlank(key.toString())) {
					if (!map.containsKey(key))
						map.put((K) key, new ArrayList<V>());
					map.get(key).add(v);
				}
			} catch (IllegalArgumentException e) {
				logger.error(e.getMessage(), e);
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			} catch (NoSuchFieldException e) {
				logger.error(e.getMessage(), e);
			} catch (SecurityException e) {
				logger.error(e.getMessage(), e);
			}
		return map;
	}
}
