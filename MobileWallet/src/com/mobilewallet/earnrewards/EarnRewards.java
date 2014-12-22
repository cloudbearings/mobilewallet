package com.mobilewallet.earnrewards;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.mobilewallet.R;
import com.mobilewallet.TabsActivity;
import com.mobilewallet.service.BuildService;
import com.mobilewallet.utils.Utils;

public class EarnRewards extends ActionBarActivity {

	private EditText question, answerA, answerB, answerC, answerD, answer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.earn_rewards);

			question = (EditText) findViewById(R.id.question);
			answerA = (EditText) findViewById(R.id.answerA);
			answerB = (EditText) findViewById(R.id.answerB);
			answerC = (EditText) findViewById(R.id.answerC);
			answerD = (EditText) findViewById(R.id.answerD);
			answer = (EditText) findViewById(R.id.answer);
			if (validate()) {
				BuildService.build.submitQuestion(Utils
						.getUserId(EarnRewards.this), question.getText()
						.toString(), answerA.getText().toString(), answerB
						.getText().toString(), answerC.getText().toString(),
						answerD.getText().toString(), answer.getText()
								.toString(), new Callback<String>() {

							@Override
							public void success(String result, Response res) {
								Log.i("submitQuestion", result);
							}

							@Override
							public void failure(RetrofitError retrofitError) {
								retrofitError.printStackTrace();
							}
						});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean validate() {
		if (!Utils.isNetworkAvailable(EarnRewards.this)) {
			displayToad(getString(R.string.no_internet));
			return false;
		}

		if (!"".equals(question)) {
			displayToad(getString(R.string.question_error));
			return false;
		}

		if (!"".equals(answerA) && !"".equals(answerB) && !"".equals(answerC)
				&& !"".equals(answerD)) {
			displayToad(getString(R.string.answers_error));
			return false;
		}

		if (!"".equals(answer)) {
			displayToad(getString(R.string.answer_error));
			return false;
		}
		return true;
	}

	private void displayToad(String toad) {
		Toast.makeText(EarnRewards.this, toad, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			startActivity(new Intent(EarnRewards.this, TabsActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
