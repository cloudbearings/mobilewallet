package com.funnyzone.google.adcontroller;

import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;

import com.funnyzone.utils.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class AdController {

	private static AdView adView;

	public static void bannerAd(Context context, RelativeLayout layout, String AD_UNIT_ID) {
		try {

			// Create an ad.
			adView = new AdView(context);
			adView.setAdSize(AdSize.BANNER);
			adView.setAdUnitId(AD_UNIT_ID);

			// Add the AdView to the view hierarchy. The view will have no size
			// until the ad is loaded.
			RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);

			lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

			layout.addView(adView, lay);
			// Create an ad request. Check logcat output for the hashed device
			// ID to
			// get test ads on a physical device.
			AdRequest adRequest = new AdRequest.Builder().addTestDevice(
					AdRequest.DEVICE_ID_EMULATOR).build();

			// Start loading the ad in the background.
			adView.loadAd(adRequest);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void resumeAdView() {
		if (adView != null) {
			adView.resume();
		}
	}

	public static void pauseAdView() {
		if (adView != null) {
			adView.pause();
		}
	}

	public static void destroyAdView() {
		// Destroy the AdView.
		if (adView != null) {
			adView.destroy();
		}
	}

	/** The interstitial ad. */
	private InterstitialAd interstitialAd;
	private boolean isLoaded = true;

	public void interstitialAd(final Context context, String AD_UNIT_ID) {
		/** The log tag. */
		final String LOG_TAG = "InterstitialAdd";

		try {

			// Create an ad.
			interstitialAd = new InterstitialAd(context);
			interstitialAd.setAdUnitId(AD_UNIT_ID);

			// Set the AdListener.
			interstitialAd.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {
					if (isLoaded) {
						isLoaded = false;
						Utils.storeAddClosedDate(context);
						interstitialAd.show();
					}
				}

				@Override
				public void onAdFailedToLoad(int errorCode) {
					String message = String.format("onAdFailedToLoad (%s)",
							getErrorReason(errorCode));
					Log.d(LOG_TAG, message);
				}
			});

			// Check the logcat output for your hashed device ID to get test ads
			// on
			// a physical device.
			AdRequest adRequest = new AdRequest.Builder().addTestDevice(
					AdRequest.DEVICE_ID_EMULATOR).build();

			// Load the interstitial ad.
			interstitialAd.loadAd(adRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Gets a string error reason from an error code. */
	private static String getErrorReason(int errorCode) {
		String errorReason = "";
		switch (errorCode) {
		case AdRequest.ERROR_CODE_INTERNAL_ERROR:
			errorReason = "Internal error";
			break;
		case AdRequest.ERROR_CODE_INVALID_REQUEST:
			errorReason = "Invalid request";
			break;
		case AdRequest.ERROR_CODE_NETWORK_ERROR:
			errorReason = "Network Error";
			break;
		case AdRequest.ERROR_CODE_NO_FILL:
			errorReason = "No fill";
			break;
		}
		return errorReason;
	}
}
