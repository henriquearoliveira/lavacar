package com.hrsoftware.services;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.hrsoftware.relatorios.PathReports;

public class GsonMapper {

	public Gson createGson() {

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setDateFormat("yyyy-MM-dd' 'HH:mm:ss");
		gsonBuilder.disableHtmlEscaping();
		gsonBuilder.registerTypeAdapter(PathReports.class, new EnumSerializer());
		return gsonBuilder.create();

	}

}

class EnumSerializer implements JsonSerializer<PathReports> {

	@Override
	public JsonElement serialize(PathReports src, Type typeOfSrc, JsonSerializationContext context) {
		return context.serialize(src.getDescription());
	}

}
