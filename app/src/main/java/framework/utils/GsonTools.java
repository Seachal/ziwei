package framework.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.laka.live.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Gson解析工具类， 类单例模式
 *
 */
public class GsonTools {

	private static Gson gson = new Gson();

	/**
	 * 将json字符串转换成bean
	 *
	 * @param json
	 *            json字符串，注意字符串不能带外层参数
	 * @param clazz
	 *            bean对应的类类型
	 * @return
	 */
	public static <T> T fromJson(String json, Class<T> clazz) {
		return gson.fromJson(json, clazz);
	}

	/**
	 * 将json字符串转换成bean，用于泛型
	 *
	 * @param json
	 *            json字符串，注意字符串不能带外层参数
	 * @param typeOfT
	 *            对应的type类型，可使用new TypeToken<T>(){}.getType()的方式获取
	 * @return
	 */
	public static <T> T fromJson(String json, Type typeOfT) {
		return gson.fromJson(json, typeOfT);
	}

	/**
	 * 将对象转换为json字符串
	 */
	public static String toJson(Object src) {
		return gson.toJson(src);
	}

	/**
	 * 将对象转换为json字符串，只有有@Expose注释的字段才会被转换
	 */
	public static String toJsonWithExposeAnnotation(Object src) {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.create();
		return gson.toJson(src);
	}


	/**
	 *
	 * @param data 从标准的{data里获取}
	 * @param clazz
	 * @param <T>
     * @return
     */
	public static <T> List<T> getData(String data , final Class<T> clazz) {

		Type type = null;
		String str;
		JSONObject jo = null;

		try {
			jo = new JSONObject(data);
			str = JSONUtil.getString(jo, "data");
			type = new ParameterizedType() {
				public Type getRawType() {
					return ArrayList.class;
				}

				public Type getOwnerType() {
					return null;
				}

				public Type[] getActualTypeArguments() {
					Type[] type = new Type[1];
					type[0] = clazz;
					return type;
				}
			};

			return  GsonTools.fromJson(str, type);

		} catch (JSONException e) {
			Log.log("GsonTools:"+e.toString());
			return null;
		}

	}

	/**
	 *
	 * @param data 从data的list里获取
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> getDataInList(String data , final Class<T> clazz) {

		Type type = null;
		String str;
		JSONObject jo = null;

		try {

			jo = new JSONObject(data);
			str = JSONUtil.getString(jo, "data");
			return  GsonTools.getJsonList(new JSONObject(str)
					.optString("list"), clazz);

		} catch (JSONException e) {
			Log.log("GsonTools:"+e.toString());
			return null;
		}

	}
	/**
	 * 直接将json转成list，json必须是一个Json数组
	 * @param json
	 * @param clazz
	 * @param <T>
     * @return
     */
	public static <T> List<T> getJsonList(String json , final Class<T> clazz) {

		Type type = null;

		try {

			type = new ParameterizedType() {
				public Type getRawType() {
					return ArrayList.class;
				}

				public Type getOwnerType() {
					return null;
				}

				public Type[] getActualTypeArguments() {
					Type[] type = new Type[1];
					type[0] = clazz;
					return type;
				}
			};

			return  GsonTools.fromJson(json, type);

		} catch (Exception e) {
			Log.log("GsonTools:"+e.toString());
			return null;
		}


	}

}
