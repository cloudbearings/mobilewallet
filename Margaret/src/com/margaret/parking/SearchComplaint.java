package com.margaret.parking;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.margaret.parking.adapters.ComplaintsAdapter;
import com.margaret.parking.db.DBOpenHelper;
import com.margaret.parking.gcm.Config;
import com.margaret.parking.pojo.ComplaintRecord;
import com.margaret.parking.service.BuildService;
import com.margaret.parking.util.Utils;

/**
 * Created by gopi on 8/22/2015.
 */
public class SearchComplaint extends Activity {
	ListView mComplaintsListView;
	List<ComplaintRecord> complainstList;
	ComplaintsAdapter mComplaintsAdapter;
	final int COMPLAINT_NEW_REQUEST = 1;
	final int COMPLAINT_VERIFICATION_REQUEST = 2;
	final int COMPLAINT_CLAMP_REQUEST = 3;
	TextView mNoComplaintsTextView;
	private ProgressBar progressBar;
	private boolean loadingMore = false;
	private int pageindex = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.search_complaint);

			Log.i("Searching Key ", getIntent().getStringExtra("search"));
			progressBar = (ProgressBar) findViewById(R.id.cProgressBar);
			mComplaintsListView = (ListView) findViewById(R.id.complaintsListView);
			mNoComplaintsTextView = (TextView) findViewById(R.id.noComplaintsView);
			complainstList = DBOpenHelper.getInstance(SearchComplaint.this)
					.fetchComplaints(SearchComplaint.this);

			if (complainstList.isEmpty()) {
				mNoComplaintsTextView.setVisibility(View.VISIBLE);
			} else {
				mNoComplaintsTextView.setVisibility(View.GONE);
			}
			mComplaintsAdapter = new ComplaintsAdapter(SearchComplaint.this,
					R.layout.item_complaints_list, 0, complainstList);
			mComplaintsListView.setAdapter(mComplaintsAdapter);

			mComplaintsListView
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							Object viewHolder = view.getTag();
							mComplaintsAdapter.showPopupMenu(view, viewHolder,
									position);
						}
					});
			if (Utils.isNetworkAvailable(SearchComplaint.this)) {
				mComplaintsListView
						.setOnScrollListener(new AbsListView.OnScrollListener() {

							@Override
							public void onScrollStateChanged(AbsListView view,
									int scrollState) {
							}

							@Override
							public void onScroll(AbsListView view,
									int firstVisibleItem, int visibleItemCount,
									int totalItemCount) {
								int lastInScreen = firstVisibleItem
										+ visibleItemCount;
								if ((lastInScreen == totalItemCount)
										&& !(loadingMore)) {
									loadingMore = true;
									progressBar.setVisibility(View.VISIBLE);

									BuildService.build.getAllComplaints(Utils
											.getDataFromPref(
													SearchComplaint.this,
													Config.USER_ID), Utils
											.getDataFromPref(
													SearchComplaint.this,
													Config.ROLE), pageindex,
											Config.PAGE_SIZE,
											new Callback<String>() {

												@Override
												public void success(
														String complaintsAsString,
														Response arg1) {

													try {
														JsonParser jsonParser = new JsonParser();
														JsonArray complainsArray = (JsonArray) jsonParser
																.parse(complaintsAsString);
														for (int index = 0; index < complainsArray
																.size(); index++) {
															Gson gson = new GsonBuilder()
																	.create();
															final ComplaintRecord record = gson.fromJson(
																	complainsArray
																			.get(index),
																	ComplaintRecord.class);
															complainstList
																	.add(record);
															mComplaintsAdapter
																	.add(record);
														}

														mComplaintsAdapter
																.notifyDataSetChanged();

														if (complainsArray
																.size() == Config.PAGE_SIZE) {
															pageindex = pageindex + 1;
															loadingMore = false;
														} else {
															loadingMore = true;
														}

														progressBar
																.setVisibility(View.GONE);
													} catch (Exception e) {
														progressBar
																.setVisibility(View.GONE);
														e.printStackTrace();
													}
												}

												@Override
												public void failure(
														RetrofitError arg0) {
													loadingMore = false;
													progressBar
															.setVisibility(View.GONE);
												}
											});

								}
							}
						});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK
				&& (requestCode == COMPLAINT_NEW_REQUEST
						|| requestCode == COMPLAINT_VERIFICATION_REQUEST
						|| requestCode == COMPLAINT_CLAMP_REQUEST
						|| requestCode == 4 || requestCode == 5)) {

			refreshUI();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void refreshUI() {
		try {
			complainstList.clear();
			complainstList = DBOpenHelper.getInstance(SearchComplaint.this)
					.fetchComplaints(SearchComplaint.this);
			if (complainstList.isEmpty()) {
				mNoComplaintsTextView.setVisibility(View.VISIBLE);
			} else {
				mNoComplaintsTextView.setVisibility(View.GONE);
			}
			mComplaintsAdapter = new ComplaintsAdapter(SearchComplaint.this,
					R.layout.item_complaints_list, 0, complainstList);
			mComplaintsListView.setAdapter(mComplaintsAdapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(SearchComplaint.this,
				OperatorMainActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}
}