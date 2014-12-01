package com.mobilewallet;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mobilewallet.adapters.WalletHistoryAdapter;
import com.mobilewallet.beans.WalletHistoryBean;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics.TrackerName;
import com.mobilewallet.service.BuildService;
import com.mobilewallet.utils.Utils;

public class WalletHistory extends ActionBarActivity {

	private ListView amulyamCreditList;
	private List<WalletHistoryBean> rowItems;

	private boolean loadingMore = false;
	private int start;
	private long page;
	private WalletHistoryAdapter adapter;

	private ProgressBar progressBar;

	@Override
	public void onResume() {

		super.onResume();
		try {
			Tracker tracker = ((MobileWalletGoogleAnalytics) getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			tracker.setScreenName(getString(R.string.wallet_history_screen_name));
			tracker.send(new HitBuilders.AppViewBuilder().build());

		} catch (Exception e) {
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.credit_history);

		TextView heading = (TextView) findViewById(R.id.textView1);
		heading.setTypeface(Utils.getFont(this, getString(R.string.GothamRnd)), Typeface.BOLD);
		TextView date = (TextView) findViewById(R.id.debitDateText);
		date.setTypeface(Utils.getFont(this, getString(R.string.GothamRnd)), Typeface.BOLD);
		TextView amount = (TextView) findViewById(R.id.debitCoinsText);
		amount.setTypeface(Utils.getFont(this, getString(R.string.GothamRnd)), Typeface.BOLD);

		rowItems = new ArrayList<WalletHistoryBean>();
		amulyamCreditList = (ListView) findViewById(R.id.creditList);
		progressBar = (ProgressBar) findViewById(R.id.crdtProgressBar);
		adapter = new WalletHistoryAdapter(getApplicationContext(),
				R.layout.wallet_history_list_item, rowItems);
		amulyamCreditList.setTextFilterEnabled(true);
		amulyamCreditList.setAdapter(adapter);

		amulyamCreditList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
					int totalItemCount) {
				Log.i("Scrolled", "Scrolled");
				int lastInScreen = firstVisibleItem + visibleItemCount;
				if ((lastInScreen == totalItemCount) && !(loadingMore)) {
					loadingMore = true;
					progressBar.setVisibility(View.VISIBLE);

					BuildService.build.credit(Utils.getUserId(WalletHistory.this), (page * 15) + 1,
							new Callback<String>() {

								@Override
								public void success(String creditHistory, Response arg1) {

									try {

										if (creditHistory != null && !("").equals(creditHistory)) {
											Log.i("", creditHistory);
											if (creditHistory.equals("No network")) {
												displayToad(getString(R.string.no_internet));
											} else {
												JSONObject obj = new JSONObject(creditHistory);
												start = obj.getInt("begin");
												page = Math.round((start / 15.0) + 0.5);

												if (!"Y".equals(obj.getString("non"))) {
													JSONArray creditArray = obj
															.getJSONArray("ctrList");

													for (int i = 0; i < creditArray.length(); i++) {
														JSONArray cdtArray = creditArray
																.getJSONArray(i);

														WalletHistoryBean item = new WalletHistoryBean(
																"ID:" + cdtArray.getString(0),
																cdtArray.getString(1), Float
																		.parseFloat(cdtArray
																				.getString(2)),
																cdtArray.getString(3), "");
														rowItems.add(item);
													}

													adapter.notifyDataSetChanged();
													loadingMore = false;

													if (obj.getInt("end") == obj.getInt("count")) {

														loadingMore = true;
													}
												} else {
													displayToad("No credit history");
												}
												progressBar.setVisibility(View.GONE);
											}
										}

									} catch (Exception e) {
										progressBar.setVisibility(View.GONE);
										e.printStackTrace();
									}
								}

								@Override
								public void failure(RetrofitError arg0) {
									loadingMore = false;
									progressBar.setVisibility(View.GONE);
									displayToad("Unable to get debit history");
								}
							});

				}
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	private void displayToad(String msg) {
		try {
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}