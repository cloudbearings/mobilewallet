package com.mobilewallet.earnrewards;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
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
import com.mobilewallet.R;
import com.mobilewallet.earnrewards.adapters.UserQuestionsAdapter;
import com.mobilewallet.earnrewards.beans.UserQuestionsRowItem;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics.TrackerName;
import com.mobilewallet.service.BuildService;
import com.mobilewallet.utils.Utils;

public class UserQuestions extends ActionBarActivity {

	private ListView userQuestions;
	private List<UserQuestionsRowItem> rowItems;
	private boolean loadingMore = false;
	private int end;
	private UserQuestionsAdapter adapter;
	private ProgressBar progressBar;

	@Override
	public void onResume() {
		super.onResume();
		try {
			Tracker tracker = ((MobileWalletGoogleAnalytics) getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			tracker.setScreenName(getString(R.string.user_questions_screen_name));
			tracker.send(new HitBuilders.AppViewBuilder().build());

		} catch (Exception e) {
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_questions);

		rowItems = new ArrayList<UserQuestionsRowItem>();
		userQuestions = (ListView) findViewById(R.id.userQuestionsList);
		progressBar = (ProgressBar) findViewById(R.id.userQueProgressBar);
		adapter = new UserQuestionsAdapter(getApplicationContext(),
				R.layout.user_questions_list_item, rowItems);
		userQuestions.setTextFilterEnabled(true);
		userQuestions.setAdapter(adapter);

		userQuestions.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				Log.i("Scrolled", "Scrolled");
				int lastInScreen = firstVisibleItem + visibleItemCount;
				if ((lastInScreen == totalItemCount) && !(loadingMore)) {
					loadingMore = true;
					progressBar.setVisibility(View.VISIBLE);

					BuildService.build.credit(
							Utils.getUserId(UserQuestions.this), (end + 1),
							new Callback<String>() {

								@Override
								public void success(String userQuestions,
										Response arg1) {
									Log.i("userQuestions", userQuestions);
									try {
										if (Utils
												.isNetworkAvailable(UserQuestions.this)) {
											displayToad(getString(R.string.no_internet));
										} else {
											JSONObject obj = new JSONObject(
													userQuestions);
											end = obj
													.getInt(getString(R.string.end));

											if (!"null".equals(obj
													.getString("userQues"))) {
												JSONArray userQuestionsArray = obj
														.getJSONArray("userQues");

												for (int i = 0; i < userQuestionsArray
														.length(); i++) {
													JSONObject userQuesObj = userQuestionsArray
															.getJSONObject(i);

													UserQuestionsRowItem item = new UserQuestionsRowItem(
															userQuesObj
																	.getString("cid"),
															userQuesObj
																	.getString("desc"),
															userQuesObj
																	.getString("amount"),
															userQuesObj
																	.getString("cTime"),
															userQuesObj
																	.getString("cTime"),
															userQuesObj
																	.getString("cTime"),
															userQuesObj
																	.getString("cTime"));
													rowItems.add(item);
												}
												adapter.notifyDataSetChanged();
												loadingMore = false;

												if (obj.getInt(getString(R.string.end)) == obj
														.getInt(getString(R.string.count))) {
													loadingMore = true;
												}
											} else {
												TextView no_questions = (TextView) findViewById(R.id.no_user_questions);
												no_questions
														.setText(getString(R.string.no_user_questions));
											}
											progressBar
													.setVisibility(View.GONE);
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
									displayToad(getString(R.string.user_ques_server_error));
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

	private void displayToad(String msg) {
		try {
			Toast.makeText(UserQuestions.this, msg, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
