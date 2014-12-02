package com.mobilewallet.recharge;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mobilewallet.R;
import com.mobilewallet.RechargeConfirmation;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics.TrackerName;
import com.mobilewallet.service.BuildService;
import com.mobilewallet.utils.Utils;

public class RechargeFragment extends android.support.v4.app.Fragment {

	private boolean doubleBackToExitPressedOnce = false;
	private EditText mobileNumberEditText;
	private EditText operatorCircleEditText;
	private EditText amountEditText;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ArrayList<String> operatorCircleArrayList = new ArrayList<String>();

	private ActionBarDrawerToggle mDrawerToggle;
	// ////
	private String operator;
	private String circle;
	private View finalView;
	private Button rechargeButton;
	private boolean clicked = false;

	private RadioGroup recharge_type;
	private RadioButton recharge_type_button;

	@Override
	public void onResume() {
		super.onResume();
		clicked = false;
		try {
			rechargeButton.setText("RECHARGE");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			Tracker t = ((MobileWalletGoogleAnalytics) getActivity().getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			t.setScreenName(getString(R.string.recharge_screen_name));
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
								showMessage(getString(R.string.click_back_agiain_to_exit));
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

		finalView = inflater.inflate(R.layout.recharge_fragment, container, false);
		mobileNumberEditText = (EditText) finalView.findViewById(R.id.mobile_number);
		mobileNumberEditText.setTypeface(Utils.getFont(getActivity(), getString(R.string.arial)));

		operatorCircleEditText = (EditText) finalView.findViewById(R.id.operator_circle);
		operatorCircleEditText.setTypeface(Utils.getFont(getActivity(), getString(R.string.arial)));

		amountEditText = (EditText) finalView.findViewById(R.id.recharge_amount);
		amountEditText.setTypeface(Utils.getFont(getActivity(), getString(R.string.arial)));

		rechargeButton = (Button) finalView.findViewById(R.id.recharge);
		rechargeButton.setTypeface(Utils.getFont(getActivity(), getString(R.string.GothamRnd)),
				Typeface.BOLD);

		recharge_type = (RadioGroup) finalView.findViewById(R.id.recharge_type);
		onRadioButtonClicked(recharge_type.getCheckedRadioButtonId());

		initializeDrawerLayout();
		addMobileNumberTextChangedListener();
		addContactsClickListener();
		addRechargeButtonClickListener();

		return finalView;
	}

	public void onRadioButtonClicked(int selectedId) {
		// Is the button now checked?
		recharge_type_button = (RadioButton) finalView.findViewById(selectedId);

		if (recharge_type_button.getText().equals("self")) {
			((TextView) finalView.findViewById(R.id.contacts)).setVisibility(View.GONE);
		} else {
			((TextView) finalView.findViewById(R.id.contacts)).setVisibility(View.VISIBLE);
		}
	}

	private void initializeDrawerLayout() {

		mDrawerLayout = (DrawerLayout) finalView.findViewById(R.id.rcg_drawer_layout);
		mDrawerLayout.setFocusableInTouchMode(false);
		mDrawerList = (ListView) finalView.findViewById(R.id.operator_list_view);
		mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				// TODO Auto-generated method stub
				String selectedFromList = (mDrawerList.getItemAtPosition(position).toString());
				if (operatorCircleArrayList.size() == 0) {

					mDrawerList.setAdapter(new ArrayAdapter<String>(getActivity(),
							R.layout.operator_circle_listitem, R.id.text1, getResources()
									.getStringArray(R.array.circles_array)));
					operatorCircleArrayList.add(0, selectedFromList);

				} else if (operatorCircleArrayList.size() == 1) {

					mDrawerLayout.closeDrawer(Gravity.RIGHT);
					operatorCircleArrayList.add(1, selectedFromList);
					operatorCircleEditText.setText(operatorCircleArrayList.get(0) + " ("
							+ operatorCircleArrayList.get(1) + ")");
					operator = operatorCircleArrayList.get(0);
					circle = operatorCircleArrayList.get(1);
					operatorCircleArrayList.clear();

				}

			}
		});

		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout,
				android.R.color.transparent, R.string.app_name,

				R.string.app_name) {
			public void onDrawerClosed(View view) {

				getActivity().supportInvalidateOptionsMenu();

			}

			public void onDrawerOpened(View drawerView) {

				getActivity().supportInvalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		operatorCircleEditText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				operatorCircleArrayList.clear();

				mDrawerList.setAdapter(new ArrayAdapter<String>(getActivity(),
						R.layout.operator_circle_listitem, R.id.text1, getResources()
								.getStringArray(R.array.mobile_operators_array)));

				if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
					mDrawerLayout.closeDrawer(Gravity.RIGHT);
				} else {
					mDrawerLayout.openDrawer(Gravity.RIGHT);
				}
			}
		});

	}

	private void addMobileNumberTextChangedListener() {

		mobileNumberEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				try {
					String mNumber = s.toString().trim();
					if (mNumber.matches("\\d{10}")) {
						char pos_0 = mNumber.charAt(0);
						if (pos_0 == '9' || pos_0 == '8' || pos_0 == '7') {

							BuildService.build.findOP(Utils.getUserId(getActivity()),
									mNumber.substring(0, 4), new Callback<String>() {

										@Override
										public void failure(RetrofitError retrofitError) {
											retrofitError.printStackTrace();
											operator = null;
											circle = null;

											operatorCircleEditText.setText("");
										}

										@Override
										public void success(String output, Response arg1) {
											JSONObject obj = null;

											try {
												Log.i("findop", output);
												obj = new JSONObject(output);
											} catch (Exception e) {
											}

											if (obj != null) {

												try {

													String oc[] = obj.getString("op").split("&");
													if (oc.length == 2) {
														String ope = oc[0].trim();
														String cir = oc[1].trim();

														if (OperatorInfo.operatorMap.get(ope) != null
																&& CircleInfo.circlesMap.get(cir) != null) {

															operatorCircleEditText.setText(ope
																	+ " (" + cir + ")");
															operator = ope;
															circle = cir;

														}

													} else if (oc.length == 3) {

														String ope = oc[0].trim();
														String cir = (oc[1] + "&" + oc[2]).trim();

														if (OperatorInfo.operatorMap.get(ope) != null
																&& CircleInfo.circlesMap.get(cir) != null) {

															operatorCircleEditText.setText(ope
																	+ " (" + cir + ")");
															operator = ope;
															circle = cir;

														}

													}

												} catch (Exception e) {
													operator = null;
													circle = null;
													operatorCircleEditText.setText("");
													e.printStackTrace();
												}

											}

										}

									});
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

		});

	}

	private void addContactsClickListener() {
		((TextView) finalView.findViewById(R.id.contacts))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							startActivityForResult(new Intent(Intent.ACTION_PICK,
									ContactsContract.CommonDataKinds.Phone.CONTENT_URI), 1111);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		try {
			if (resultCode == Activity.RESULT_OK) {
				if (requestCode == 1111) {
					setMobileNumber(data);

				} else if (requestCode == 1112 && data != null) {
					setAmount(data);
				} else

				if ("RB".equals(data.getStringExtra("from"))) {
					if (validationSuccess()) {
						recharge();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void setAmount(Intent data) {
		try {
			amountEditText.setText(data.getStringExtra("amount"));
			operator = data.getStringExtra("operator");
			circle = data.getStringExtra("circle");

			operatorCircleEditText.setText(operator + " (" + circle + ")");

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	private void setMobileNumber(Intent data) {
		try {

			Cursor c = getActivity().getContentResolver().query(Phone.CONTENT_URI, null,
					Phone._ID + "=?", new String[] { data.getData().getLastPathSegment() }, null);
			int phoneIdx = c.getColumnIndex(Phone.NUMBER);
			if (c.getCount() == 1) {
				if (c.moveToFirst()) {

					String no = getMobileString(c.getString(phoneIdx));
					try {
						if (no != null && !"".equals(no)) {
							Long.parseLong(no);
							mobileNumberEditText.setText(no);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String getMobileString(String phone) {
		String tmp = "";
		try {
			if (phone != null && !"".equals(phone.trim())) {

				Pattern p = Pattern.compile("(\\d+)");
				Matcher m = p.matcher(phone);
				StringBuilder sb = new StringBuilder("");
				while (m.find()) {
					sb.append(m.group(1));
				}
				String no = sb.toString().trim();
				if (!"".equals(no)) {
					tmp = no.substring(no.length() - 10);
				}
				if (tmp.trim().length() == 10) {

					char pos_0 = tmp.charAt(0);
					if (pos_0 == '9' || pos_0 == '8' || pos_0 == '7') {

					} else {
						tmp = "";
					}

				} else {
					tmp = "";
				}

			}
		} catch (Exception e) {
		}
		return tmp.trim();

	}

	private void addRechargeButtonClickListener() {

		rechargeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (validationSuccess()) {
					recharge();
				}
			}
		});

	}

	private void recharge() {
		if (!clicked) {
			clicked = true;
			rechargeButton.setText("Processing..");

			Bundle rechargeInfoBundle = new Bundle();

			rechargeInfoBundle
					.putString("mobile", mobileNumberEditText.getText().toString().trim());
			rechargeInfoBundle.putString("amount", amountEditText.getText().toString().trim());
			rechargeInfoBundle.putString("operator", operator);
			rechargeInfoBundle.putString("circle", circle);
			rechargeInfoBundle.putString("operator_id", OperatorInfo.operatorMap.get(operator));
			rechargeInfoBundle.putString("circle_id", CircleInfo.circlesMap.get(circle));
			Log.i("Recharge Bundle", rechargeInfoBundle.toString());
			startActivity(new Intent(getActivity(), RechargeConfirmation.class).putExtra(
					"rechargeInfo", rechargeInfoBundle));

		} else {
			showMessage("Please wait..");
		}
	}

	private boolean validationSuccess() {
		if ("".equals(mobileNumberEditText.getText().toString().trim())) {
			showMessage("Enter mobile number.");
			return false;
		}

		if (mobileNumberEditText.getText().toString().trim().length() != 10) {
			showMessage("Invalid mobile number.");
			return false;
		}

		char pos_0 = mobileNumberEditText.getText().toString().trim().charAt(0);
		if (!(pos_0 == '9' || pos_0 == '8' || pos_0 == '7')) {
			showMessage("Invalid mobile number.");
			return false;
		}

		if ("".equals(operatorCircleEditText.getText().toString().trim()) || operator == null
				|| circle == null || OperatorInfo.operatorMap.get(operator) == null
				|| CircleInfo.circlesMap.get(circle) == null) {
			if ("".equals(operatorCircleEditText.getText().toString().trim())) {
				showMessage("Select Operator and circle.");
				return false;
			} else {
				operatorCircleEditText.setText("");
				showMessage("Please select Operator and circle again.");
				return false;
			}
		}

		if ("".equals(amountEditText.getText().toString().trim())) {
			showMessage("Enter recharge amount.");
			return false;
		}

		int rechargeAmount = 0;
		try {
			rechargeAmount = Integer.parseInt(amountEditText.getText().toString().trim());
		} catch (Exception e) {
		}

		if (rechargeAmount < 10) {
			showMessage("Recharge amount must be grater than or equal to Rs.10.");
			return false;
		}

		float balance = 0.0f;
		try {
			balance = Float.parseFloat(Utils.getAmount(getActivity()));
		} catch (Exception e) {
		}
		// balance = 500;
		if (rechargeAmount > balance) {
			showMessage("Low balance.");
			return false;
		}

		return true;
	}

	private void showMessage(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
	}
}
