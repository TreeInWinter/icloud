package com.icloud.springmvc.web;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.util.ReflectionUtils;

/**
 * 在model返回到view之前对所有字符串元素进行html编码，防止XSS脚本注入
 * 
 * @author wangmeng
 * 
 */
public class EscapeUtil {
	public static Object escapeStringFiledsToHTMLCode(Object obj) {
		if (obj == null) {
			return null;
		}
		Field[] fields = obj.getClass().getDeclaredFields();
		if (fields == null) {
			return obj;
		}
		for (Field field : fields) {
			ReflectionUtils.makeAccessible(field);
			if (String.class.equals(field.getType())) {
				String value = (String) ReflectionUtils.getField(field, obj);
				String escapeHtmlValue = StringEscapeUtils.escapeHtml(value);
				ReflectionUtils.setField(field, obj, escapeHtmlValue);
			} else if (!field.getType().isPrimitive()) {
				escapeStringFiledsToHTMLCode(ReflectionUtils.getField(field,
						obj));
			}
		}
		return obj;
	}

	public static Object escapeCollection(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Collection) {
			obj = (Collection) obj;
			Iterator it = ((Collection) obj).iterator();
			while (it.hasNext()) {
				escapeStringFiledsToHTMLCode(it.next());
			}

		}
		return obj;
	}

	public static Object escapeMap(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Map) {
			Set entries = ((Map) obj).entrySet();
			while (entries.iterator().hasNext()) {
				escapeStringFiledsToHTMLCode(((Entry) entries.iterator().next())
						.getValue());
			}

		}
		return obj;
	}

	// public static void main(String[] args) {
	// Hotel hotel = new Hotel();
	// hotel.setName(null);
	// hotel.setPropertyCode("aaa/<a");
	// Hotel hotel1 = new Hotel();
	// hotel1.setPropertyCode("aaa/>=====//////a");
	// // hotel.setHotel(null);
	//
	// Hotel escapehotel = (Hotel) escapeStringFiledsToHTMLCode(hotel);
	// System.out.println("hotel message:" + escapehotel.getName() + "---"
	// + escapehotel.getPropertyCode());
	// //
	// System.out.println("hotel1 message:"+escapehotel.getHotel().getName()+"---"+escapehotel.getHotel().getPropertyCode());
	// }

}
