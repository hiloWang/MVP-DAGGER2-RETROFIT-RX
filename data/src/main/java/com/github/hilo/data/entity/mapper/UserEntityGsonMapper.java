package com.github.hilo.data.entity.mapper;

import android.content.Context;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserEntityGsonMapper {

	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	private final Gson gson;

	public static final long ONE_KB = 1024L;
	public static final long ONE_MB = ONE_KB * 1024L;
	public static final long CACHE_DATA_MAX_SIZE = ONE_MB * 3L;

	@Inject public UserEntityGsonMapper(Context context) {
		this.gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();
		try {
			Reservoir.init(context,CACHE_DATA_MAX_SIZE,gson);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Gson getGson() {
		return gson;
	}
}
