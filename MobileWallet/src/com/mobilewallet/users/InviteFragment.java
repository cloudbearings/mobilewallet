package com.mobilewallet.users;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mobilewallet.R;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics.TrackerName;
import com.mobilewallet.service.BuildService;
import com.mobilewallet.utils.Utils;

public class InviteFragment extends Fragment {

	private boolean doubleBackToExitPressedOnce = false;

	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callBack = new Session.StatusCallback() {

		@Override
		public void call(Session session, SessionState state, Exception exception) {
			// TODO Auto-generated method stub

		}
	};

	private static String APP_ID;
	private Facebook facebook;
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
		dismissDialog();

		try {

			Tracker t = ((MobileWalletGoogleAnalytics) getActivity().getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			t.setScreenName(getString(R.string.invite_screen_name));
			t.send(new HitBuilders.AppViewBuilder().build());

			View view = getView();
			view.setFocusableInTouchMode(true);
			view.requestFocus();
			view.setOnKeyListener(new View.OnKeyListener() {
				@Override
				public boolean onKey(View view, int keyCode, KeyEvent event) {
					if (event.getAction() == KeyEvent.ACTION_UP) {
						if (keyCode == KeyEvent.KEYCODE_BACK) {
							if (doubleBackToExitPressedOnce) {
								doubleBackToExitPressedOnce = false;
								getActivity().finish();
							} else {
								doubleBackToExitPressedOnce = true;
								displayToad(getString(R.string.click_back_agiain_to_exit));
								return true;
							}
						}
					}
					return false;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.invite_fragment, container, false);
		APP_ID = getActivity().getString(R.string.facebook_app_id);
		initializeDialog();
		uiHelper = new UiLifecycleHelper(getActivity(), callBack);
		uiHelper.onCreate(savedInstanceState);

		facebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(facebook);

		/*
		 * try {
		 * 
		 * utils.bannerAd(getActivity(), (RelativeLayout)
		 * rootView.findViewById(R.id.inviteLayout),
		 * getString(R.string.invite_fragment_banner_unit_id)); } catch
		 * (Exception e) { e.printStackTrace(); }
		 */

		try {
			final String shareText = "Help me get free recharge on my mobile. Install this app "
					+ BuildService.URL + "/r/" + Utils.getRefCode(getActivity())
					+ " and enter my invitation code " + Utils.getRefCode(getActivity())
					+ " if it's not automatically entered during registration.";

			String val[] = Utils.getReferralAmount(getActivity());
			// val[0] = referralAmt, val[1] = referralEarningAmt

			TextView inviteDes = (TextView) rootView.findViewById(R.id.inviteDes);

			inviteDes.setText(Html.fromHtml("<font color=\"#6ebb3b\">Get Rs " + val[0]
					+ "</font> when you refer any new friend to Mobile Wallet."));
			inviteDes.setTypeface(Utils.getFont(getActivity(), getString(R.string.GothamRnd)));

			ImageView inviteViaWhatsApp = (ImageView) rootView
					.findViewById(R.id.invite_via_whatsapp);
			inviteViaWhatsApp.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					if (Utils.isPackageInstalled("com.whatsapp", getActivity())) {
						showDialog();

						startActivity(Intent.createChooser(
								new Intent(Intent.ACTION_SEND).setType("text/plain")
										.setPackage("com.whatsapp")
										.putExtra(Intent.EXTRA_TEXT, shareText), "Share with"));
					} else {
						displayToad("Whatsapp app is not installed.");
					}
				}
			});

			ImageView inviteViaFacebook = (ImageView) rootView
					.findViewById(R.id.invite_via_facebook);
			inviteViaFacebook.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					showDialog();
					try {
						PackageInfo info = getActivity().getPackageManager().getPackageInfo(
								"com.mobilewallet", PackageManager.GET_SIGNATURES);
						for (Signature signature : info.signatures) {
							MessageDigest md = MessageDigest.getInstance("SHA");
							md.update(signature.toByteArray());
							Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
						}
					} catch (NameNotFoundException e) {

					} catch (NoSuchAlgorithmException e) {

					}

					if (FacebookDialog.canPresentShareDialog(getActivity().getApplicationContext(),
							FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
						FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(
								getActivity())
								.setLink(
										"http://" + Utils.getRefCode(getActivity())
												+ ".mobilewalletapp.com")
								.setName("Free Mobile Recharge")
								.setCaption("Invite your friends")
								.setDescription(
										"Help me to get free recharge on my mobile. Install this app and enter my invitation code "
												+ Utils.getRefCode(getActivity())
												+ " if it's not automatically entered during registration.   Click on "
												+ "http://" + Utils.getRefCode(getActivity())
												+ ".mobilewalletapp.com")
								.setPicture(
										"http://mobilewalletapp.com/images/mobilewallet_fbshare.png")
								.build();
						uiHelper.trackPendingDialogCall(shareDialog.present());
					} else {
						loginToFacebook();
					}

				}
			});

			ImageView invite_via_twitter = (ImageView) rootView
					.findViewById(R.id.invite_via_twitter);
			invite_via_twitter.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (Utils.isPackageInstalled("com.twitter.android", getActivity())) {
						showDialog();

						startActivity(Intent.createChooser(
								new Intent(Intent.ACTION_SEND).setType("text/plain")
										.setPackage("com.twitter.android")
										.putExtra(Intent.EXTRA_TEXT, shareText), "Share with"));
					} else {
						displayToad("Twitter app is not installed.");
					}
				}
			});

			ImageView shareViaApps = (ImageView) rootView.findViewById(R.id.share_via_apps);
			shareViaApps.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					showDialog();

					startActivity(new Intent().setAction(Intent.ACTION_SEND)
							.putExtra(Intent.EXTRA_TEXT, shareText).setType("text/plain"));
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

		return rootView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
			@Override
			public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
				Log.e("Activity", String.format("Error: %s", error.toString()));

			}

			@Override
			public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
				Log.i("Activity", "Success!");

			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	private void displayToad(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
	}

	private ProgressDialog dialog;

	private void initializeDialog() {
		try {
			dialog = new ProgressDialog(getActivity());
			dialog.setMessage(this.getString(R.string.title_loading));
			dialog.setCancelable(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void showDialog() {
		try {

			if (!dialog.isShowing()) {
				dialog.show();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void dismissDialog() {
		try {
			dialog.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void publishFeedDialog() {
		Bundle params = new Bundle();
		params.putString("name", "Free Mobile Recharge");
		params.putString("caption", "Invite your friends");
		params.putString("description",
				"Help me to get free recharge on my mobile. Install this app and enter my invitation code "
						+ Utils.getRefCode(getActivity())
						+ " if it's not automatically entered during registration.   Click on "
						+ "http://" + Utils.getRefCode(getActivity()) + ".mobilewalletapp.com");
		params.putString("link", "http://" + Utils.getRefCode(getActivity()) + ".mobilewalletapp.com");
		params.putString("picture", "http://mobilewalletapp.com/images/mobilewallet_fbshare.png");

		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(getActivity(),
				facebook.getSession(), params)).setOnCompleteListener(new OnCompleteListener() {

			@Override
			public void onComplete(Bundle values, FacebookException error) {
				// TODO Auto-generated method stub
				dismissDialog();
				if (error == null) {
					// When the story is posted, echo the success
					// and the post Id.
					final String postId = values.getString("post_id");
					if (postId != null) {
						Toast.makeText(getActivity(), "Successfully Posted", Toast.LENGTH_LONG)
								.show();
					} else {
						// User clicked the Cancel button
						Toast.makeText(getActivity().getApplicationContext(), "Publish cancelled",
								Toast.LENGTH_LONG).show();
					}
				} else if (error instanceof FacebookOperationCanceledException) {
					// User clicked the "x" button
					Toast.makeText(getActivity().getApplicationContext(), "Publish cancelled",
							Toast.LENGTH_LONG).show();
				} else {
					// Generic, ex: network error
					Toast.makeText(getActivity().getApplicationContext(), "Error posting story",
							Toast.LENGTH_LONG).show();
				}

			}

		}).build();
		feedDialog.show();
	}

	public void loginToFacebook() {
		mPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);

		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}

		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (!facebook.isSessionValid()) {
			facebook.authorize(getActivity(), null, new DialogListener() {

				@Override
				public void onComplete(Bundle values) {
					// TODO Auto-generated method stub
					// Function to handle complete event
					// Edit Preferences and update facebook acess_token
					SharedPreferences.Editor editor = mPrefs.edit();
					editor.putString("access_token", facebook.getAccessToken());
					editor.putLong("access_expires", facebook.getAccessExpires());
					editor.commit();

					publishFeedDialog();

				}

				@Override
				public void onFacebookError(FacebookError e) {
					// TODO Auto-generated method stub
					dismissDialog();

				}

				@Override
				public void onError(DialogError e) {
					// TODO Auto-generated method stub
					dismissDialog();

				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
					dismissDialog();

				}
			});
		} else {
			publishFeedDialog();
		}
	}
}
