package com.mobilewallet;

import java.util.Locale;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics.TrackerName;
import com.mobilewallet.service.BuildService;
import com.mobilewallet.utils.Utils;

public class ForgotPassword extends Activity {
	private EditText emailEditText;
	private Button submitButton;
	private boolean clicked = false;

	@Override
	public void onResume() {
		super.onResume();
		try {
			Tracker tracker = ((MobileWalletGoogleAnalytics) getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			tracker.setScreenName(getString(R.string.forgot_pwd_screen_name));
			tracker.send(new HitBuilders.AppViewBuilder().build());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_password);
		try {
			TextView heading = (TextView) findViewById(R.id.textView1);
			heading.setTypeface(Utils.getFont(this, getString(R.string.GothamRnd)), Typeface.BOLD);

			emailEditText = (EditText) findViewById(R.id.email_address);
			submitButton = (Button) findViewById(R.id.ok);
			submitButton.setTypeface(Utils.getFont(this, getString(R.string.GothamRnd)),
					Typeface.BOLD);

			Button cancel = (Button) findViewById(R.id.cancel);
			submitButton.setTypeface(Utils.getFont(this, getString(R.string.GothamRnd)),
					Typeface.BOLD);

			cancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					finish();
				}
			});

			addSubmitButtonClickListener();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addSubmitButtonClickListener() {
		submitButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				forgotPassword();
			}
		});

		emailEditText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					forgotPassword();
					return true;
				}
				return false;
			}
		});
	}

	private void forgotPassword() {

		if (validate()) {
			if (!clicked) {
				clicked = true;
				submitButton.setText(getString(R.string.title_processing));
				BuildService.build.fp(
						emailEditText.getText().toString().trim().toLowerCase(Locale.ENGLISH),
						new Callback<String>() {

							@Override
							public void success(String result, Response arg1) {
								try {
									Log.i("result", result);
									JSONObject obj = new JSONObject(result);

									boolean invalid = false;

									try {
										invalid = obj.getBoolean("invalid.email");
									} catch (Exception e) {

									}

									if (invalid) {
										showToad(getString(R.string.invalid_email));
										clicked = false;
										submitButton.setText(getString(R.string.ok));

									} else {

										if (obj.getBoolean("sent")) {
											submitButton.setText(getString(R.string.ok));
											emailEditText.setText("");
											clicked = false;
										} else {
											showToad("Failed");
											clicked = false;
											submitButton.setText(getString(R.string.ok));
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
									showToad(getString(R.string.forgot_pwd_fail));
									clicked = false;
									submitButton.setText(getString(R.string.ok));
								}
							}

							@Override
							public void failure(RetrofitError retrofitError) {
								showToad(getString(R.string.forgot_pwd_fail));
								clicked = false;
								submitButton.setText(getString(R.string.ok));
							}
						});
			} else {
				showToad(getString(R.string.please_wait));
			}
		}
	}

	private boolean validate() {
		if (!(Patterns.EMAIL_ADDRESS).matcher(emailEditText.getText().toString().trim()).matches()) {
			showToad(getString(R.string.email_error));
			return false;
		}
		return true;
	}

	private void showToad(String toad) {
		Toast.makeText(ForgotPassword.this, toad, Toast.LENGTH_LONG).show();
	}
}
