package com.mobilewallet.users;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mobilewallet.R;
import com.mobilewallet.TabsActivity;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics.TrackerName;
import com.mobilewallet.service.BuildService;
import com.mobilewallet.utils.Utils;

public class MyProfile extends ActionBarActivity {

	private JSONObject obj;

	@Override
	protected void onResume() {
		super.onResume();
		try {
			Tracker tracker = ((MobileWalletGoogleAnalytics) getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			tracker.setScreenName(getString(R.string.my_profile_screen_name));
			tracker.send(new HitBuilders.AppViewBuilder().build());
		} catch (Exception e) {
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_profile);

		try {
			BuildService.build.userProfile(Utils.getUserId(MyProfile.this),
					new Callback<String>() {

						@Override
						public void failure(RetrofitError arg0) {
							arg0.printStackTrace();
						}

						@Override
						public void success(String result, Response arg1) {

							try {

								Log.i("user profile", result);
								obj = new JSONObject(result);

								try {
									String name = obj.getString("name");
									if (name == null || name.equals("null")) {
										name = "";
									}

									TextView userName = (TextView) findViewById(R.id.user_name);
									if (!name.trim().equals("")) {
										userName.setText(Html.fromHtml("<font color=\"#5a5a5a\">"
												+ name + "</font>"));
									} else {
										userName.setText(getString(R.string.not_mentioned));
									}
								} catch (Exception e) {
								}

								try {
									String email = obj.getString("email");
									if (email == null || email.equals("null")) {
										email = "";
									}

									TextView userName = (TextView) findViewById(R.id.email_address);
									if (!email.trim().equals("")) {
										userName.setText(Html.fromHtml("<font color=\"#5a5a5a\">"
												+ email + "</font>"));
									} else {
										userName.setText(getString(R.string.not_mentioned));
									}
								} catch (Exception e) {
								}

								try {
									String mobile = obj.getString("mobile");
									TextView mobileNumber = (TextView) findViewById(R.id.mobile_number);
									if (!mobile.trim().equals("0")) {
										mobileNumber.setText(Html
												.fromHtml("<font color=\"#5a5a5a\">" + mobile
														+ "</font>"));
									} else {
										mobileNumber.setText(getString(R.string.not_mentioned));
									}
								} catch (Exception e) {
								}

								try {
									String dob = obj.getString("dob");
									TextView dateOfBirth = (TextView) findViewById(R.id.date_of_birth);
									if (!"null".equals(dob.trim())) {
										dateOfBirth.setText(Html
												.fromHtml("<font color=\"#5a5a5a\">" + dob
														+ "</font>"));
									} else {
										dateOfBirth.setText(getString(R.string.not_mentioned));
									}
								} catch (Exception e) {
								}

								try {
									String gender = obj.getString("gender");
									TextView user_gender = (TextView) findViewById(R.id.gender);
									if (!"null".equals(gender.trim())) {
										user_gender.setText(Html
												.fromHtml("<font color=\"#5a5a5a\">"
														+ getGender(gender) + "</font>"));
									} else {
										user_gender.setText(getString(R.string.not_mentioned));
									}
								} catch (Exception e) {
								}

								try {
									String ocu = obj.getString("occupation");
									TextView ocupation = (TextView) findViewById(R.id.ocupation);
									if (!ocu.equals("0")) {
										String[] occupationArray = getResources().getStringArray(
												R.array.occupation_array_items);
										ocupation.setText(Html.fromHtml("<font color=\"#5a5a5a\">"
												+ occupationArray[obj.getInt("occupation")]
												+ "</font>"));
									} else {
										ocupation.setText(getString(R.string.not_mentioned));
									}
								} catch (Exception e) {
								}

								try {
									String user_income = obj.getString("income");
									TextView income = (TextView) findViewById(R.id.income);
									if (!user_income.equals("0")) {
										String[] incomeArray = getResources().getStringArray(
												R.array.income_array_items);
										income.setText(Html.fromHtml("<font color=\"#5a5a5a\">"
												+ incomeArray[obj.getInt("income")] + "</font>"));
									} else {
										income.setText(getString(R.string.not_mentioned));
									}
								} catch (Exception e) {
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});

			Button edit_profile = (Button) findViewById(R.id.edit_profile);
			edit_profile.setTypeface(
					Utils.getFont(MyProfile.this, getString(R.string.GothamRnd)),
					Typeface.BOLD);
			edit_profile.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					try {
						if (obj != null) {
							startActivity(new Intent(MyProfile.this, EditProfile.class)
									.putExtra("userProfile", obj.toString()).addFlags(
											Intent.FLAG_ACTIVITY_CLEAR_TOP));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent tabsIntent = new Intent(MyProfile.this, TabsActivity.class);
			tabsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(tabsIntent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private String getGender(String gender) {
		if (gender.equals("M")) {
			return "Male";
		} else {
			return "Female";
		}
	}
}
