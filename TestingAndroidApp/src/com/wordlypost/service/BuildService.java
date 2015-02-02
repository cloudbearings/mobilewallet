package com.wordlypost.service;

import retrofit.RestAdapter;

public interface BuildService {

	WordlyPostService build = new RestAdapter.Builder().setEndpoint("http://www.wordlypost.in/api")
			.setConverter(new StringConverter()).build().create(WordlyPostService.class);

	WordlyPostService build_GCM = new RestAdapter.Builder().setEndpoint("http://www.wordlypost.in")
			.setConverter(new StringConverter()).build().create(WordlyPostService.class);

}
