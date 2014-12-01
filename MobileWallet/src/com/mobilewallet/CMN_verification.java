package com.mobilewallet;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mobilewallet.gcm.Config;
import com.mobilewallet.service.BuildService;
import com.mobilewallet.utils.Utils;

public class CMN_verification extends ActionBarActivity {

	private Button verifyButton;
	private boolean clicked;
	private EditText verCodeEdiText;
	private ProgressBar processing;
	private TextView resendVerCode;
	private String vid;
	private long time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.verify_mobile_after_registration);
		setContentView(R.layout.cmn_verification);
		verCodeEdiText = (EditText) findViewById(R.id.verification_code);
		verifyButton = (Button) findViewById(R.id.verify);
		processing = (ProgressBar) findViewById(R.id.processing);
		resendVerCode = (TextView) findViewById(R.id.resend_verification_code);
		processing.setVisibility(View.INVISIBLE);
		resendVerCode.setVisibility(View.INVISIBLE);
		vid = getIntent().getStringExtra("vid");
		time = getIntent().getLongExtra("time", 0);

		((TextView) findViewById(R.id.cnf_des))
				.setText("Please enter verification code sent to your mobile number "
						+ getIntent().getStringExtra("mobile"));
		addVerifyButtonOnclickListener();
		if (Config.IS_READ_SMS_PERMISSION_AVAILABLE) {
			runTaskToReadVerificationCode();
		} else {
			resendVerCode.setVisibility(View.VISIBLE);
		}
		resendVerCodeListener();
		runTaskForgcmid();

	}

	private void resendVerCodeListener() {
		resendVerCode.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				processing.setVisibility(View.VISIBLE);
				resendVerCode.setVisibility(View.INVISIBLE);
				BuildService.build.resendVerCodeAtReg(vid, new Callback<String>() {

					@Override
					public void failure(RetrofitError arg0) {
						// TODO Auto-generated method stub
						showToad("Unable to resend verfication code.");
						processing.setVisibility(View.INVISIBLE);
						resendVerCode.setVisibility(View.VISIBLE);

					}

					@Override
					public void success(String result, Response arg1) {
						// TODO Auto-generated method stub

						try {

							Log.i("resend ver code", result);

							if ("Y".equals(new JSONObject(result).getString("sent"))) {
								if (Config.IS_READ_SMS_PERMISSION_AVAILABLE) {
									runTaskToReadVerificationCode();
								} else {
									processing.setVisibility(View.INVISIBLE);
									resendVerCode.setVisibility(View.VISIBLE);
								}
								showToad("verification code resent to your mobile successfully.");
							} else {
								showToad("Unable to resend verfication code.");
								processing.setVisibility(View.INVISIBLE);
								resendVerCode.setVisibility(View.VISIBLE);
							}

						} catch (Exception e) {
							e.printStackTrace();
							showToad("Unable to resend verfication code.");
							processing.setVisibility(View.INVISIBLE);
							resendVerCode.setVisibility(View.VISIBLE);
						}

					}
				});

			}
		});
	}

	private void addVerifyButtonOnclickListener() {
		verifyButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (validate()) {
					if (!clicked) {
						clicked = true;
						verifyButton.setText("Processing..");
						BuildService.build.verifysignup(vid, verCodeEdiText.getText().toString()
								.trim(), "",
								new Callback<String>() {

									@Override
									public void failure(RetrofitError arg0) {
										// TODO Auto-generated method stub
										clicked = false;
										verifyButton.setText("VERIFY");
										showToad("Moile verification failed, Try again.");
										arg0.printStackTrace();

									}

									@Override
									public void success(String result, Response arg1) {
										// TODO Auto-generated method stub
										try {

											Log.i("verify signup", result);

											JSONObject obj = new JSONObject(result);

											String error = null;

											try {
												error = obj.getString("error");
											} catch (Exception e) {

											}
											if (error == null) {
												String id = obj.getString("id");
												if (id != null && !"null".equalsIgnoreCase(id)
														&& !"".equals(id.trim())
														&& !"0".equals(id.trim())) {

													
													CMN_verification.this.finish();
												} else {

													clicked = false;
													verifyButton.setText("VERIFY");
													showToad("Moile verification failed, Try again.");
												}
											} else {
												clicked = false;
												verifyButton.setText("VERIFY");
												showToad(error);

											}

										} catch (Exception e) {
											clicked = false;
											verifyButton.setText("VERIFY");
											showToad("Moile verification failed, Try again.");
											e.printStackTrace();
										}

									}
								});

					} else {
						showToad("Please wait");
					}

				}
			}
		});
	}

	private boolean validate() {

		if (!Utils.isNetworkAvailable(CMN_verification.this)) {
			showToad("Internet is not available.");
			return false;
		}

		if ("".equals(verCodeEdiText.getText().toString().trim())) {
			showToad("Please enter verification code");
			return false;
		}

		return true;
	}

	private void showToad(String toad) {
		Toast.makeText(CMN_verification.this, toad, Toast.LENGTH_LONG).show();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home

			startActivity(new Intent(CMN_verification.this, MainActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			CMN_verification.this.finish();

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		startActivity(new Intent(CMN_verification.this, MainActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		CMN_verification.this.finish();
	}

	private void runTaskToReadVerificationCode() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub

				String verCode = null;

				for (int i = 1; i <= 10; i++) {

					try {
						Thread.sleep(2000);
					} catch (Exception e) {
						e.printStackTrace();
					}
					verCode = Utils.readVerificationCode(CMN_verification.this, time);
					if (verCode != null)
						break;

				}

				return verCode;
			}

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				processing.setVisibility(View.VISIBLE);
			}

			@Override
			protected void onPostExecute(String verCode) {
				// TODO Auto-generated method stub
				super.onPostExecute(verCode);
				processing.setVisibility(View.INVISIBLE);
				if (verCode != null) {
					verCodeEdiText.setText(verCode);
				} else {
					resendVerCode.setVisibility(View.VISIBLE);
				}

			}

		}.execute();

	}

	private void runTaskForgcmid() {
		String gcmid = Utils.getGcmId(CMN_verification.this);

		if (gcmid == null || "".equals(gcmid.trim())) {

			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					// TODO Auto-generated method stub
					try {
						String gcmid = GoogleCloudMessaging.getInstance(
								CMN_verification.this).register(
								Config.GOOGLE_PROJECT_ID);
						if (gcmid != null && !"".equals(gcmid.trim())) {
							Utils.storeGcmId(gcmid, CMN_verification.this);
						}

					} catch (Exception e) {
					}
					return null;
				}
			}.execute();
		}
	}

}
