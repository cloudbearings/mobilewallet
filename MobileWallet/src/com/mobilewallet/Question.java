package com.mobilewallet;

import org.json.JSONArray;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.mobilewallet.service.BuildService;
import com.mobilewallet.utils.Utils;

public class Question extends ActionBarActivity {
	private static final String TAG = "Question";

	@Override
	protected void onResume() {
		super.onResume();
		Utils.googleAnalyticsTracking(Question.this,
				getString(R.string.question_screen_name));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.question);

			BuildService.build.getQuestion(Utils.getUserId(Question.this),
					new Callback<String>() {

						@Override
						public void success(String result, Response arg1) {

							try {
								Log.i("Questuin json", result + " result");
								JSONArray array = new JSONArray(result);

								((TextView) findViewById(R.id.question))
										.setText("");

							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						@Override
						public void failure(RetrofitError retrofitError) {
							Log.d(TAG,
									"Exception raised in retrofilt failure()");
							retrofitError.printStackTrace();
						}
					});
		} catch (Exception e) {
			Log.d(TAG, "Exception raised in onCreate()");
			e.printStackTrace();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			startActivity(new Intent(Question.this, TabsActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
