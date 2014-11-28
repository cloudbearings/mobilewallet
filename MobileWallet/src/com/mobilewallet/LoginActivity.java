package com.mobilewallet;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Typeface;
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
import com.mobilewallet.utils.Utils;
import com.mobilewallet.widget.LoginButton;

public class LoginActivity extends ActionBarActivity {

	private EditText email, pwd;
	private static final String TAG = "LoginActivity";

	private static final List<String> permissions = new ArrayList<String>();
	static {
		permissions.add("email");
		permissions.add("user_birthday");
		permissions.add("user_about_me");
	}

	private Session.StatusCallback callBack = new Session.StatusCallback() {

		@Override
		public void call(Session session, SessionState state, Exception exception) {
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

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		try {

			// Specifying actionbar display options
			getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			// Adding custom actionbar.
			getSupportActionBar().setCustomView(R.layout.custom_actionbar);

			TextView activity_title = (TextView) findViewById(R.id.actionbar_title);
			activity_title
					.setTypeface(Utils.getFont(LoginActivity.this, getString(R.string.Helvetica)),
							Typeface.BOLD);
			activity_title.setText(getString(R.string.title_activity_login));

			// Facebok authentication code
			LoginButton fbSignupButton = (LoginButton) findViewById(R.id.fb_login);
			fbSignupButton.setReadPermissions(permissions);
			fbSignupButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
				@Override
				public void onUserInfoFetched(GraphUser user) {

					Session session = Session.getActiveSession();

					if (session != null && session.isOpened()) {
						if (isvalideGraphUser(user)) {

							try {
								Log.i("UserFBDetails", "Hello " + user.getName() + "\nFbid : "
										+ user.getId() + "\nBirthday : " + user.getBirthday()
										+ "\nGender : " + user.getProperty("gender") + "\nEmail : "
										+ user.getProperty("email") + "\nBirthday Date : "
										+ getDate(user.getBirthday()));

								startActivity(new Intent(LoginActivity.this, TabsActivity.class)
										.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							} catch (Exception e) {
								e.printStackTrace();
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

			Button login = (Button) findViewById(R.id.login);
			// Adding Helvetica custom font to button text
			login.setTypeface(Utils.getFont(LoginActivity.this, getString(R.string.Helvetica)));
			login.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (validate()) {
						if (email.getText().toString().equals("cutegopichand@gmail.com	")
								&& pwd.getText().toString().equals("password")) {
							// Opening TabsActivity
							startActivity(new Intent(LoginActivity.this, TabsActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

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
					startActivity(new Intent(LoginActivity.this, RegisterActivity.class)
							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				}
			});

			TextView forgotPwd = (TextView) findViewById(R.id.forgot_pwd);
			forgotPwd.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// Opening Login activity
					startActivity(new Intent(LoginActivity.this, ForgotPassword.class)
							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				}
			});

			// Add code to print out the key hash
			try {
				PackageInfo info = getPackageManager().getPackageInfo("com.testingfbauth",
						PackageManager.GET_SIGNATURES);
				for (Signature signature : info.signatures) {
					MessageDigest md = MessageDigest.getInstance("SHA");
					md.update(signature.toByteArray());
					Log.i("keyhash: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));

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
			session = Session.openActiveSession(LoginActivity.this, false, null);
			if (session != null)
				session.closeAndClearTokenInformation();
		}
		Session.setActiveSession(null);
	}

	private String getDate(String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
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
					|| !"true".equalsIgnoreCase(user.getProperty("verified").toString().trim()))
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
		if (!(Patterns.EMAIL_ADDRESS).matcher(email.getText().toString().trim()).matches()) {
			displayToad(getString(R.string.invalid_email));
			return false;
		}

		if (!pwd.getText().toString().matches("((?=\\S+$).{8,20})")) {
			displayToad(getString(R.string.invalid_pwd));
			return false;
		}
		return true;
	}

	private void displayToad(String toad) {
		Toast.makeText(LoginActivity.this, toad, Toast.LENGTH_LONG).show();
	}
}
