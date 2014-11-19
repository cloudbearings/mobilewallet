package com.mobilewallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class SplashScreen extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.splash_screen);

			// Opening MainActivity
			startActivity(new Intent(SplashScreen.this, MainActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
