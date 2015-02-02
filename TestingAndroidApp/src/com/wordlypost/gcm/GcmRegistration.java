package com.wordlypost.gcm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.wordlypost.utils.Utils;

public class GcmRegistration {
	private Context context;

	public GcmRegistration(Context context) {
		this.context = context;

	}

	public void register() {

		String gcmid = Utils.getGcmId(context);
		if (gcmid == null || "".equals(gcmid.trim())) {
			new AsyncTask<String, String, String>() {

				@Override
				protected String doInBackground(String... params) {
					// TODO Auto-generated method stub
					String gcmid = null;
					try {
						gcmid = GoogleCloudMessaging.getInstance(context).register(
								Config.GOOGLE_PROJECT_ID);
					} catch (Exception e) {
					}
					return gcmid;
				}

				protected void onPostExecute(String gcmid) {
					try {
						if (gcmid != null && !"".equals(gcmid.trim())) {
							Log.i("gcm id", gcmid);
							new StoreGcmId(gcmid, context).update();
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
