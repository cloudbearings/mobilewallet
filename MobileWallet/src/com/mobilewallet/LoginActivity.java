package com.mobilewallet;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mobilewallet.gcm.Config;
import com.mobilewallet.service.BuildService;
import com.mobilewallet.utils.Utils;

public class LoginActivity extends ActionBarActivity {

	private EditText email, pwd;
	private static final String TAG = "LoginActivity";
	private Button login;
	private boolean clicked;
	private String gcmId;

	private static final List<String> permissions = new ArrayList<String>();
	static {
		permissions.add("email");
		permissions.add("user_birthday");
		permissions.add("user_about_me");
	}

	private Session.StatusCallback callBack = new Session.StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			Log.i(TAG, getString(R.string.session_status));

		}
	};

	private UiLifecycleHelper uiHelper;

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
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

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		uiHelper = new UiLifecycleHelper(LoginActivity.this, callBack);
		uiHelper.onCreate(savedInstanceState);
		runTaskForGcmId();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		try {

			// Specifying actionbar display options
			getSupportActionBar().setDisplayOptions(
					ActionBar.DISPLAY_SHOW_CUSTOM);
			// Adding custom actionbar.
			getSupportActionBar().setCustomView(R.layout.custom_actionbar);

			TextView activity_title = (TextView) findViewById(R.id.actionbar_title);
			activity_title.setTypeface(Utils.getFont(LoginActivity.this,
					getString(R.string.Helvetica)), Typeface.BOLD);
			activity_title.setText(getString(R.string.title_activity_login));

			// Facebok authentication code
			LoginButton fbSignupButton = (LoginButton) findViewById(R.id.fb_login);
			fbSignupButton.setReadPermissions(permissions);
			fbSignupButton
					.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
						@Override
						public void onUserInfoFetched(GraphUser user) {

							Session session = Session.getActiveSession();

							if (session != null && session.isOpened()) {
								if (isvalideGraphUser(user)) {

									String gender = Utils.getGender(user
											.getProperty("gender").toString()
											.trim());

									String id = null;
									try {
										id = Utils.getRegistrationId(
												user.getFirstName(),
												user.getLastName(),
												user.getProperty("email")
														.toString()
														.trim()
														.toLowerCase(
																Locale.ENGLISH),
												null, gcmId, gender,
												getDate(user.getBirthday()),
												user.getId(),
												LoginActivity.this);
									} catch (Exception e) {
										e.printStackTrace();
									}

									if (id != null) {
										registerUser(id);
									} else {

										Utils.displayToad(LoginActivity.this,
												"Login using facebook is failed.");
									}

								} else {
									displayToad(getString(R.string.failed_to_get_details));
								}

								facebookLogout();
							}
						}
					});

			email = (EditText) findViewById(R.id.email);
			pwd = (EditText) findViewById(R.id.pwd);

			login = (Button) findViewById(R.id.login);
			// Adding Helvetica custom font to button text
			login.setTypeface(Utils.getFont(LoginActivity.this,
					getString(R.string.Helvetica)));
			login.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (validate()) {
						if (!clicked) {
							clicked = true;
							login.setText(R.string.title_processing);
							BuildService.build.login(email.getText().toString()
									.trim().toLowerCase(Locale.ENGLISH), pwd
									.getText().toString(),
									new Callback<String>() {

										@Override
										public void success(String result,
												Response arg1) {
											// TODO Auto-generated method stub

											try {
												Log.i("login json", result);

												JSONObject obj = new JSONObject(
														result);

												boolean inavalid = false;

												try {
													inavalid = "Y".equals(obj
															.getString("invalid"));

												} catch (Exception e) {

												}

												if (inavalid) {
													displayToad("Inavalid email or password.");
													login.setText(getString(R.string.login_button_name));
													clicked = false;
												} else {

													String userId = obj
															.getString("userId");
													if (userId != null
															&& !"".equals(userId
																	.trim())
															&& !"null"
																	.equalsIgnoreCase(userId)) {

														openTabsActivity(
																obj.getString("mycode"),
																obj.getDouble("amount"),
																userId);

													} else {
														displayToad("Login failed.");
														login.setText(getString(R.string.login_button_name));
														clicked = false;
													}

												}

											} catch (Exception e) {
												e.printStackTrace();
												displayToad("Login failed.");
												login.setText(getString(R.string.login_button_name));
												clicked = false;

											}
										}

										@Override
										public void failure(RetrofitError arg0) {
											// TODO Auto-generated method stub
											displayToad("Login failed.");
											login.setText(getString(R.string.login_button_name));
											clicked = false;

										}
									});

						} else {
							displayToad("Just wait a moment.");
						}
					}
				}
			});

			Button dontHaveAnAccount = (Button) findViewById(R.id.dont_have_an_account);
			// Adding Helvetica custom font to button text
			dontHaveAnAccount.setTypeface(Utils.getFont(LoginActivity.this,
					getString(R.string.Helvetica)));
			dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// Opening Login activity
					/*startActivity(new Intent(LoginActivity.this,
							RegisterActivity.class)
							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));*/
					
					startActivity(new Intent(LoginActivity.this, TabsActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				}
			});

			TextView forgotPwd = (TextView) findViewById(R.id.forgot_pwd);
			forgotPwd.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// Opening Login activity
					startActivity(new Intent(LoginActivity.this,
							ForgotPassword.class)
							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				}
			});

			// Add code to print out the key hash
			try {
				PackageInfo info = getPackageManager().getPackageInfo(
						"com.testingfbauth", PackageManager.GET_SIGNATURES);
				for (Signature signature : info.signatures) {
					MessageDigest md = MessageDigest.getInstance("SHA");
					md.update(signature.toByteArray());
					Log.i("keyhash: ",
							Base64.encodeToString(md.digest(), Base64.DEFAULT));

				}
			} catch (NameNotFoundException e) {
			} catch (NoSuchAlgorithmException e) {
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void facebookLogout() {
		Session session = Session.getActiveSession();
		if (session != null)
			session.closeAndClearTokenInformation();
		else {
			session = Session
					.openActiveSession(LoginActivity.this, false, null);
			if (session != null)
				session.closeAndClearTokenInformation();
		}
		Session.setActiveSession(null);
	}

	private String getDate(String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy",
					Locale.ENGLISH);
			Date d = sdf.parse(date.trim());
			sdf.applyPattern("dd-MMM-yyyy");

			Log.i("", sdf.format(d));
			return sdf.format(d);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private boolean isvalideGraphUser(GraphUser user) {
		try {
			if (user == null)
				return false;

			if (user.getProperty("email") == null
					|| "".equals(user.getProperty("email").toString().trim()))
				return false;

			if (user.getProperty("verified") == null
					|| !"true".equalsIgnoreCase(user.getProperty("verified")
							.toString().trim()))
				return false;

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean validate() {
		if (!Utils.isNetworkAvailable(LoginActivity.this)) {
			displayToad(getString(R.string.no_internet));
			return false;
		}
		if (!(Patterns.EMAIL_ADDRESS)
				.matcher(email.getText().toString().trim()).matches()) {
			displayToad(getString(R.string.invalid_email));
			return false;
		}

		if (!pwd.getText().toString().matches("((?=\\S+$).{8,20})")) {
			displayToad(getString(R.string.invalid_pwd));
			return false;
		}
		return true;
	}

	private void registerUser(String id) {
		BuildService.build.reg(id, new Callback<String>() {

			@Override
			public void success(String result, Response arg1) {
				try {
					Log.i("reg json", result);

					JSONObject obj = new JSONObject(result);
					String error = null;
					try {
						error = obj.getString("error");
					} catch (Exception e) {

					}

					if (error == null) {

						String userId = obj.getString("id");
						if (userId != null && !"".equals(userId.trim())
								&& !"null".equalsIgnoreCase(userId)) {

							openTabsActivity(obj.getString("myRefCode"),
									obj.getDouble("BL"), userId);

						} else {
							Utils.displayToad(LoginActivity.this,
									"Login using facebook is failed.");
						}

					} else {
						Utils.displayToad(LoginActivity.this, error);
					}

				} catch (Exception e) {

					Utils.displayToad(LoginActivity.this,
							"Login using facebook is failed.");
					e.printStackTrace();
				}
			}

			@Override
			public void failure(RetrofitError arg0) {
				Utils.displayToad(LoginActivity.this,
						"Login using facebook is failed.");
				arg0.printStackTrace();
			}
		});
	}

	private void openTabsActivity(String refCode, double balance, String userId) {
		Utils.storeRefCode(refCode, LoginActivity.this);
		Utils.storeBal((float) balance, LoginActivity.this);
		Utils.storeUserId(userId, LoginActivity.this);

		startActivity(new Intent(LoginActivity.this, TabsActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

		LoginActivity.this.finish();
	}

	private void runTaskForGcmId() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					gcmId = GoogleCloudMessaging
							.getInstance(LoginActivity.this).register(
									Config.GOOGLE_PROJECT_ID);
				} catch (Exception e) {
				}
				return null;
			}
		}.execute();
	}

	private void displayToad(String toad) {
		Toast.makeText(LoginActivity.this, toad, Toast.LENGTH_LONG).show();
	}
}
