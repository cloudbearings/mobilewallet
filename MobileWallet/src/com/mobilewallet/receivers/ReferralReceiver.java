package com.mobilewallet.receivers;

import java.net.URLDecoder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.analytics.CampaignTrackingReceiver;
import com.mobilewallet.utils.Utils;

public class ReferralReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		try {
			Bundle bundle = intent.getExtras();
			String referrer = bundle.getString("referrer");
			// Inviter referrer code
			String refId = "";

			if (referrer != null && referrer.contains("refcode")) {
				try {
					String query = URLDecoder.decode(referrer, "UTF-8");
					if (query != null) {
						String queryParams[] = query.split("\\&");
						if (queryParams.length > 1) {
							for (String s : queryParams) {
								if (s.contains("refcode")) {
									refId = s.split("=")[1];
									break;
								}
							}
						} else {
							if (queryParams[0].contains("refcode")) {
								refId = queryParams[0].split("=")[1];
							}
						}
					}
					if (refId != null && !("").equals(refId.trim())) {
						Utils.storeRefCode(context, refId);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// When you're done, pass the intent to the Google Analytics receiver.
		new CampaignTrackingReceiver().onReceive(context, intent);
	}

}
