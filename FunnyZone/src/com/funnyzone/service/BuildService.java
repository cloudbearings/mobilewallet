package com.funnyzone.service;

import retrofit.RestAdapter;

public interface BuildService {

	FunnyZoneService build = new RestAdapter.Builder().setEndpoint("http://www.wordlypost.in/api")
			.setConverter(new StringConverter()).build().create(FunnyZoneService.class);

	FunnyZoneService build_GCM = new RestAdapter.Builder().setEndpoint("http://www.wordlypost.in")
			.setConverter(new StringConverter()).build().create(FunnyZoneService.class);

}
