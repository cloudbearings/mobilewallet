package com.margaret.parking.service;

import com.margaret.parking.gcm.Config;

import retrofit.RestAdapter;

public interface BuildService {

	MargaretService build = new RestAdapter.Builder()
			.setEndpoint(Config.SERVER_URL).setConverter(new StringConverter())
			.build().create(MargaretService.class);

}
