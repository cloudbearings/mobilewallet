package com.mobilewallet;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilewallet.utils.Utils;

public class ChangeMobileNumber extends ActionBarActivity {
	private EditText new_mobile_no;
	private Button change_mobile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_mobile_number);
		try {
			TextView heading = (TextView) findViewById(R.id.textView1);
			heading.setTypeface(Utils.getFont(this, getString(R.string.GothamRnd)), Typeface.BOLD);

			new_mobile_no = (EditText) findViewById(R.id.new_mobile_no);

			new_mobile_no.setOnKeyListener(new OnKeyListener() {
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if ((event.getAction() == KeyEvent.ACTION_DOWN)
							&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
						changeMobileNumber();
						return true;
					}
					return false;
				}
			});

			change_mobile = (Button) findViewById(R.id.change_mobile);
			change_mobile.setTypeface(Utils.getFont(this, getString(R.string.GothamRnd)),
					Typeface.BOLD);

			change_mobile.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					changeMobileNumber();
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void changeMobileNumber() {

		if (isMobilevalid(new_mobile_no)) {
			startActivity(new Intent(ChangeMobileNumber.this, CMN_verification.class)
					.putExtra("mobile", new_mobile_no.getText().toString()));

		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent tabsIntent = new Intent(ChangeMobileNumber.this, TabsActivity.class);
			tabsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(tabsIntent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private boolean isMobilevalid(EditText mobile_number) {
		if ("".equals(mobile_number.getText().toString().trim())) {
			showToad("Enter mobile number.");
			return false;
		}

		if (mobile_number.getText().toString().trim().length() != 10) {
			showToad("Invalid mobile number.");
			return false;
		}

		char pos_0 = mobile_number.getText().toString().trim().charAt(0);
		if (!(pos_0 == '9' || pos_0 == '8' || pos_0 == '7')) {
			showToad("Invalid mobile number.");
			return false;
		}
		return true;
	}

	private void showToad(String toad) {
		Toast.makeText(ChangeMobileNumber.this, toad, Toast.LENGTH_LONG).show();
	}
}
