package com.mobilewallet.service;

public class RestAdapter {

	String URL = "http://www.mobilewalletapp.com";

	MobileWalletService build = new RestAdapter.Builder().setEndpoint(URL)
			.setConverter(new StringConverter()).build()
			.create(MobileWalletService.class);

}
