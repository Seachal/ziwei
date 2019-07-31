package framework.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BeanUtil {

	/**
	 * 对象copy只拷贝非空属性
	 * 
	 * @param from
	 * @param to
	 */
	public static void copyBeanWithOutNull(Object from, Object to) {
		Class<?> beanClass = from.getClass();
		Field[] fields = beanClass.getFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			try {
				Object value = field.get(from);
				if (value != null) {
					field.set(to, value);
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 对象copy
	 * 
	 * @param from
	 * @param to
	 */
	public static void copyBean(Object from, Object to) {
		Class<?> beanClass = from.getClass();
		Field[] fields = beanClass.getFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			try {
				Object value = field.get(from);
				field.set(to, value);
			} catch (Exception e) {
			}
		}
	}

	public static Field getDeclaredField(Class<?> clazz, String name) {
		try {
			return clazz.getDeclaredField(name);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取属性
	 * 
	 * @param o
	 * @param field
	 * @return
	 */
	public static Object getProperty(Object o, String field) {
		try {
			Field f = o.getClass().getDeclaredField(field);
			return f.get(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取属性，支持用“.”分隔进入多层获取
	 * 例如：status.code,获取o的status属性的code属性值
	 */
	public static Object getField(Object o, String field) {
		try {
			Field f;
			if (field.contains(".")) {
				String[] temp = field.split("\\.");
				Object tempO = o;
				for (String fieldName : temp) {
					f = tempO.getClass().getField(fieldName);
					tempO = f.get(tempO);
					if (tempO == null) {
						break;
					}
				}
				return tempO;
			} else {
				f = o.getClass().getField(field);
				return f.get(o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取方法，并执行，返回其执行后的返回值
	 * 只支持无参方法
	 */
	public static Object doMethod(Object o, String method) {
		try {
			Method m = o.getClass().getMethod(method);
			return m.invoke(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 添加属性
	 */
	public static void setProperty(Object o, String field, Object value) {
		try {
			Field f = o.getClass().getDeclaredField(field);
			f.setAccessible(true);
			f.set(o, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
