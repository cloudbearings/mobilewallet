package com.mobilewallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mobilewallet.utils.Utils;

public class LoginActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		try {
			EditText email = (EditText) findViewById(R.id.email);
			EditText pwd = (EditText) findViewById(R.id.pwd);

			Button login = (Button) findViewById(R.id.login);
			// Adding Helvetica custom font to button text
			login.setTypeface(Utils.getFont(LoginActivity.this,
					getString(R.string.Helvetica)));
			login.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// Opening Registration activity
					startActivity(new Intent(LoginActivity.this,
							TabsActivity.class)
							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
					startActivity(new Intent(LoginActivity.this,
							LoginActivity.class)
							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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

		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}
