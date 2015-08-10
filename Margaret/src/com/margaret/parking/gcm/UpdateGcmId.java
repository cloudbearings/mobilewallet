package com.margaret.parking.gcm;

import org.json.JSONObject;

import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Context;
import com.margaret.parking.service.BuildService;
import com.margaret.parking.util.Utils;

public class UpdateGcmId {
	private String gcmId;
	private Context context;

	public UpdateGcmId(String gcmId, Context context) {
		this.gcmId = gcmId;
		this.context = context;
	}

	public void update() {

		String userId = Utils.getDataFromPref(context, Config.USER_ID);
		if (userId != null && !"".equals(userId.trim())) {

			BuildService.build.gcm(userId, gcmId,
					new retrofit.Callback<String>() {

						@Override
						public void success(String result, Response response) {
							try {
								if ("Y".equals(new JSONObject(result)
										.getString("updated"))) {
									storeGcmId(gcmId);
								} else {
									storeGcmId("");
								}
							} catch (Exception e) {
								e.printStackTrace();
								storeGcmId("");
							}
						}

						@Override
						public void failure(RetrofitError retrofitError) {
							storeGcmId("");
							retrofitError.printStackTrace();
						}
					});
		}
	}

	private void storeGcmId(String gcm_id) {
		Utils.storeDataInPref(context, Config.GCM_ID, gcm_id);
	}
}
