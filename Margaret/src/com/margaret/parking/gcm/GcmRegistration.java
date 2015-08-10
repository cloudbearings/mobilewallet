package com.margaret.parking.gcm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.margaret.parking.util.Utils;

public class GcmRegistration {
	private Context context;

	public GcmRegistration(Context context) {
		this.context = context;

	}

	public void register() {

		String gcmid = Utils.getDataFromPref(context, Config.GCM_ID);
		if (gcmid == null || "".equals(gcmid.trim())) {
			new AsyncTask<String, String, String>() {

				@Override
				protected String doInBackground(String... params) {
					String gcmid = null;
					try {
						gcmid = GoogleCloudMessaging.getInstance(context)
								.register(Config.GOOGLE_PROJECT_ID);
					} catch (Exception e) {
					}
					return gcmid;
				}

				protected void onPostExecute(String gcmid) {
					try {
						if (gcmid != null && !"".equals(gcmid.trim())) {
							new UpdateGcmId(gcmid, context);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}.execute();
		} else {
			Log.i("gcm id", gcmid);
		}
	}
}
