package com.mobilewallet.gcm;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Context;

import com.mobilewallet.service.BuildService;
import com.mobilewallet.utils.Utils;

public class StoreGcmId {
	private String gcmId;
	private Context context;

	public StoreGcmId(String gcmId, Context context) {
		this.gcmId = gcmId;
		this.context = context;
	}

	public void update() {

		String userId = Utils.getUserId(context);
		if (userId != null && !"".equals(userId.trim())) {

			BuildService.build.storeGcmId(userId, gcmId, new Callback<String>() {

				@Override
				public void success(String result, Response response) {
					// TODO Auto-generated method stub
					try {
						if ("Y".equals(new JSONObject(result).getString("updated"))) {
							Utils.storeGcmId(gcmId, context);
						} else {
							Utils.storeGcmId("", context);
						}

					} catch (Exception e) {
						e.printStackTrace();
						Utils.storeGcmId("", context);
					}

				}

				@Override
				public void failure(RetrofitError retrofitError) {
					// TODO Auto-generated method stub
					Utils.storeGcmId("", context);

				}
			});

		} else {
			Utils.storeGcmId("", context);
		}
	}
}
