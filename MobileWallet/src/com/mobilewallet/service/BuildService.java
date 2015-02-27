package com.mobilewallet.service;

import retrofit.RestAdapter;

public interface BuildService {

	String URL = "http://49.204.82.175:8082/MobileWallet";

	MobileWalletService build = new RestAdapter.Builder().setEndpoint(URL)
			.setConverter(new StringConverter()).build()
			.create(MobileWalletService.class);

}
