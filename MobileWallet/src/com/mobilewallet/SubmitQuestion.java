package com.mobilewallet;

import org.json.JSONObject;

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

public class SubmitQuestion extends ActionBarActivity {
	private static final String TAG = "Submit Question";
	private TextView question, option1, option2, option3, option4, answer;

	@Override
	protected void onResume() {
		super.onResume();
		Utils.googleAnalyticsTracking(SubmitQuestion.this,
				getString(R.string.submit_question_screen_name));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.submit_question);

			question = (TextView) findViewById(R.id.question);

			option1 = (TextView) findViewById(R.id.answerA);
			option2 = (TextView) findViewById(R.id.answerB);
			option3 = (TextView) findViewById(R.id.answerC);
			option4 = (TextView) findViewById(R.id.answerD);

			answer = (TextView) findViewById(R.id.answer);

			BuildService.build.submitQuestion(Utils
					.getUserId(SubmitQuestion.this), question.getText()
					.toString(), option1.getText().toString(), option2
					.getText().toString(), option3.getText().toString(),
					option4.getText().toString(), answer.getText().toString(),
					new Callback<String>() {

						@Override
						public void success(String result, Response arg1) {

							try {
								Log.i("submitQuestion json", result + " result");
								JSONObject obj = new JSONObject(result);
								if (getString(R.string.Y).equals(
										obj.getString("status"))) {

								}

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
			e.printStackTrace();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			startActivity(new Intent(SubmitQuestion.this, TabsActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
