package com.mobilewallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.mobilewallet.utils.Utils;

public class MainActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.main_activity);

			Button register = (Button) findViewById(R.id.register);
			// Adding Helvetica custom font to button text
			register.setTypeface(Utils.getFont(MainActivity.this,
					getString(R.string.Helvetica)));
			register.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// Opening Registration activity
					startActivity(new Intent(MainActivity.this,
							RegisterActivity.class)
							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				}
			});

			Button login = (Button) findViewById(R.id.login);
			login.setTypeface(Utils.getFont(MainActivity.this,
					getString(R.string.Helvetica)));
			login.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(new Intent(MainActivity.this,
							LoginActivity.class)
							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
