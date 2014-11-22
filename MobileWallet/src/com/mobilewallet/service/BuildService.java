package com.mobilewallet.service;

import retrofit.RestAdapter;

public class BuildService {

	String URL = "http://www.mobilewalletapp.com";

	MobileWalletService build = new RestAdapter.Builder().setEndpoint(URL)
			.setConverter(new StringConverter()).build()
			.create(MobileWalletService.class);

}