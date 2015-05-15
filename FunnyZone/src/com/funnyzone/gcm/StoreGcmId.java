package com.funnyzone.gcm;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Context;
import android.util.Log;

import com.funnyzone.service.BuildService;
import com.funnyzone.utils.Utils;

public class StoreGcmId {
	private String gcmId;
	private Context context;

	public StoreGcmId(String gcmId, Context context) {
		this.gcmId = gcmId;
		this.context = context;
	}

	public void update() {

		BuildService.build_GCM.storeGcmId(gcmId, new Callback<String>() {

			@Override
			public void success(String result, Response response) {
				try {
					Log.i("result", response.getUrl());
					Log.i("result", result);
					Utils.storeGcmId(gcmId, context);
				} catch (Exception e) {
					e.printStackTrace();
					Utils.storeGcmId("", context);
				}
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				Utils.storeGcmId("", context);
			}
		});
	}
}
