package com.hrsoftware.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonMapper {

	public Gson createGson() {

		GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd' 'HH:mm:ss");
		return gsonBuilder.create();

	}

}
