package com.travelzen.framework.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionUtils {

	private static final Logger LGR = LoggerFactory
			.getLogger(ReflectionUtils.class);

	private static Map<Class<?>, Map<String, Field>> mutableFieldsCache = new HashMap<Class<?>, Map<String, Field>>();

	public static Map<String, Field> getMutableFields(Class<?> target) {
		Map<String, Field> result = mutableFieldsCache.get(target);
		if (result != null) {
			return result;
		}
		result = new HashMap<String, Field>();
		getMutableFields(result, target);
		mutableFieldsCache.put(target, result);
		return result;
	}

	public static void getMutableFields(Map<String, Field> toFill,
			Class<?> target) {
		for (Field f : target.getDeclaredFields()) {
			if (Modifier.isFinal(f.getModifiers())) {
				continue;
			}
			f.setAccessible(true);
			toFill.put(f.getName(), f);
		}
		if (target.getSuperclass() != null) {
			getMutableFields(toFill, target.getSuperclass());
		}
	}

	public static <M> void mergeModel(M from, M to, boolean replaceSame) {
		if (from == null || to == null) {
			return;
		}
		try {
			Map<String, Field> fields = getMutableFields(from.getClass());
			for (Field fld : fields.values()) {
				Object fromValue = fld.get(from);
				if (fromValue == null) {
					continue;
				}
				if (!replaceSame && fld.get(to) != null) {
					continue;
				}
				fld.set(to, fromValue);
			}
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static <M> void cloneModel(M from, M to) {
		if (from == null || to == null) {
			return;
		}
		try {
			Map<String, Field> fromFields = getMutableFields(from.getClass());
			Map<String, Field> toFields = getMutableFields(to.getClass());
			List<Field> fields = new ArrayList<Field>(fromFields.values());
			fields.retainAll(toFields.values());
			for (Field f : fields) {
				Object fromValue = f.get(from);
				if (fromValue == null) {
					continue;
				}
				f.set(to, fromValue);
			}
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 返回Field持有的类型参数，如果没有类型参数返回空数组
	 */
	public static Type[] getParameterTypes(Field fld) {
		Type gt = fld.getGenericType();
		if (!(gt instanceof ParameterizedType)) {
			return new Type[] {};
		}
		ParameterizedType pt = (ParameterizedType) gt;
		return pt.getActualTypeArguments();
	}

	private static String stripFilenameExtension(final String filename) {
		if (filename.indexOf('.') != -1) {
			return filename.substring(0, filename.lastIndexOf('.'));
		} else {
			return filename;
		}
	}

	public static Set<Class<?>> getFromDirectory(final File directory,
			final String packageName) throws ClassNotFoundException {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		if (directory.exists()) {
			for (String file : directory.list()) {
				if (file.endsWith(".class")) {
					String name = packageName + '.'
							+ stripFilenameExtension(file);
					Class<?> clazz = Class.forName(name);
					classes.add(clazz);
				}
			}
		}
		return classes;
	}

	/**
	 * 返回直属指定包路径下的所有类，不包括子目录
	 */
	public static Set<Class<?>> getClassesInPackage(final String packageName) {
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			Set<Class<?>> classes = new HashSet<Class<?>>();
			String path = packageName.replace('.', '/');
			Enumeration<URL> resources = loader.getResources(path);
			if (resources != null) {
				while (resources.hasMoreElements()) {
					String filePath = resources.nextElement().getFile();
					// WINDOWS HACK
					if (filePath.indexOf("%20") > 0)
						filePath = filePath.replaceAll("%20", " ");
					// # in the jar name
					if (filePath.indexOf("%23") > 0)
						filePath = filePath.replaceAll("%23", "#");

					if (filePath != null) {
						if ((filePath.indexOf("!") > 0)
								& (filePath.indexOf(".jar") > 0)) {
							String jarPath = filePath.substring(0,
									filePath.indexOf("!")).substring(
									filePath.indexOf(":") + 1);
							// WINDOWS HACK
							if (jarPath.indexOf(":") >= 0) {
								jarPath = jarPath.substring(1);
							}
							classes.addAll(getFromJARFile(jarPath, path));
						} else {
							classes.addAll(getFromDirectory(new File(filePath),
									packageName));
						}
					}
				}
			}
			return classes;
		} catch (Exception e) {
			LGR.error("get classes fron package[" + packageName + "] failed.",
					e);
			return null;
		}
	}

	public static Set<Class<?>> getFromJARFile(final String jar,
			final String packageName) throws IOException,
			FileNotFoundException, ClassNotFoundException {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		JarInputStream jarFile = new JarInputStream(new FileInputStream(jar));
		JarEntry jarEntry;
		do {
			jarEntry = jarFile.getNextJarEntry();
			if (jarEntry != null) {
				String className = jarEntry.getName();
				if (className.endsWith(".class")) {
					className = stripFilenameExtension(className);
					if (className.startsWith(packageName)) {
						classes.add(Class.forName(className.replace('/', '.')));
					}
				}
			}
		} while (jarEntry != null);
		jarFile.close();
		return classes;
	}

	public static String getSimpleMethodStr(Method mth) {
		if (mth == null) {
			return null;
		}
		StringBuilder sbd = new StringBuilder();
		sbd.append(mth.getName());
		sbd.append('(');
		Class<?>[] params = mth.getParameterTypes(); // avoid clone
		for (int j = 0; j < params.length; j++) {
			sbd.append(getParamTypeName(params[j]));
			if (j < (params.length - 1))
				sbd.append(',');
		}
		sbd.append(')');
		return sbd.toString();
	}

	public static String getParamTypeName(Class<?> type) {
		if (type.isArray()) {
			try {
				Class<?> cl = type;
				int dimensions = 0;
				while (cl.isArray()) {
					dimensions++;
					cl = cl.getComponentType();
				}
				StringBuffer sb = new StringBuffer();
				sb.append(cl.getName());
				for (int i = 0; i < dimensions; i++) {
					sb.append("[]");
				}
				return sb.toString();
			} catch (Throwable e) {
				LGR.error("", e);
			}
		}
		return type.getName();
	}

}
