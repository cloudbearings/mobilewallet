package com.mobilewallet.recharge;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.mobilewallet.R;
import com.mobilewallet.R.id;
import com.mobilewallet.R.layout;
import com.mobilewallet.R.string;
import com.mobilewallet.adapters.RechargeHistoryAdapter;
import com.mobilewallet.beans.RechargeHistoryBean;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics.TrackerName;
import com.mobilewallet.service.BuildService;
import com.mobilewallet.utils.Utils;

public class RechargeHistory extends Activity {

	private ListView amulyamDebitHistory;
	private List<RechargeHistoryBean> rowItems;
	private boolean loadingMore = false;
	private int start;
	private long page;
	private RechargeHistoryAdapter adapter;
	private ProgressBar progressBar;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			Tracker tracker = ((MobileWalletGoogleAnalytics) getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			tracker.setScreenName(getString(R.string.recharge_history_screen_name));
			tracker.send(new HitBuilders.AppViewBuilder().build());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.debit_history);

		TextView heading = (TextView) findViewById(R.id.textView1);
		heading.setTypeface(Utils.getFont(this, getString(R.string.GothamRnd)), Typeface.BOLD);
		TextView date = (TextView) findViewById(R.id.debitDateText);
		date.setTypeface(Utils.getFont(this, getString(R.string.GothamRnd)), Typeface.BOLD);
		TextView amount = (TextView) findViewById(R.id.debitCoinsText);
		amount.setTypeface(Utils.getFont(this, getString(R.string.GothamRnd)), Typeface.BOLD);

		rowItems = new ArrayList<RechargeHistoryBean>();
		amulyamDebitHistory = (ListView) findViewById(R.id.debitList);
		adapter = new RechargeHistoryAdapter(RechargeHistory.this,
				R.layout.recharge_history_list_item, rowItems);
		progressBar = (ProgressBar) findViewById(R.id.debtProgressBar);
		amulyamDebitHistory.setTextFilterEnabled(true);
		amulyamDebitHistory.setAdapter(adapter);

		amulyamDebitHistory.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
					int totalItemCount) {

				int lastInScreen = firstVisibleItem + visibleItemCount;
				if ((lastInScreen == totalItemCount) && !(loadingMore)) {
					loadingMore = true;
					progressBar.setVisibility(View.VISIBLE);

					BuildService.build.debit(Utils.getUserId(RechargeHistory.this),
							(page * 15) + 1, new Callback<String>() {

								@Override
								public void failure(RetrofitError arg0) {
									loadingMore = false;
									progressBar.setVisibility(View.GONE);
									displayToad("Unable to get debit history");
								}

								@Override
								public void success(String debitHistory, Response arg1) {

									try {

										if (debitHistory != null && !("").equals(debitHistory)) {
											Log.i("", debitHistory);
											if (debitHistory.equals("No network")) {
												displayToad(getString(R.string.no_internet));
											} else {
												JSONObject obj = new JSONObject(debitHistory);
												start = obj.getInt("begin");
												page = Math.round((start / 15.0) + 0.5);

												if (!"Y".equals(obj.getString("non"))) {
													JSONArray debitArray = obj
															.getJSONArray("dtrList");

													for (int i = 0; i < debitArray.length(); i++) {
														JSONArray dbtArray = debitArray
																.getJSONArray(i);

														String status = "";
														if ("S".equals(dbtArray.getString(6))) {
															status = "Status : <font color=\"#49a208\">Success</font>";
														} else if ("P".equals(dbtArray.getString(6))) {
															status = "Status : <font color=\"#eba40d\">Pending</font>";
														} else if ("R".equals(dbtArray.getString(6))) {
															status = "Status : <font color=\"#e02f2f\">Refunded</font>";
														}

														RechargeHistoryBean item = new RechargeHistoryBean(
																"ID:" + dbtArray.getString(5),
																dbtArray.getString(2)
																		+ "<br/><font color=\"#8c8c8c\">"
																		+ dbtArray.getString(3)
																		+ " - "
																		+ dbtArray.getString(4)
																		+ "</font>", Float
																		.parseFloat(dbtArray
																				.getString(1)),
																dbtArray.getString(0), status);
														rowItems.add(item);
													}

													adapter.notifyDataSetChanged();
													loadingMore = false;

													if (obj.getInt("end") == obj.getInt("count")) {

														loadingMore = true;
													}
												} else {
													displayToad("No debit history");
												}
												progressBar.setVisibility(View.GONE);
											}
										}
									} catch (Exception e) {
										progressBar.setVisibility(View.GONE);
										e.printStackTrace();
									}

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
			Toast.makeText(RechargeHistory.this, msg, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}