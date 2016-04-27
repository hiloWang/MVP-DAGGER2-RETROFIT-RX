package com.github.hilo.data.cache.serializer;

import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class user as Serializer/Deserializer for user entities.
 * 单例模式的 序列化/反序列化工具类
 */
@Singleton
public class JsonSerializer {

	private final Gson gson = new Gson();

	@Inject public JsonSerializer() {
	}

	/**
	 * Serialize an object to Json.
	 *
	 * @param entity a object to Serialize
	 */
	public String serialize(Object entity) {
		String jsonString = gson.toJson(entity,entity.getClass());
		return jsonString;
	}

	/**
	 * Deserialize a json representation of an object.
	 *
	 * @param jsonString A json string to deserialize.
	 */
	public Object deserialize(String jsonString,Class clazz) {
		Object entity = gson.fromJson(jsonString,clazz);
		return entity;
	}
}
