package com.wordlypost.utils;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.wordlypost.R;
import com.wordlypost.service.BuildService;

public class PostCommentPopUp {

	private static Button postButton;

	public static void showPostCommentDailog(final Context context, final int postId) {
		try {
			final Dialog alertDailog = Utils.alertDailog(context, R.layout.post_comment_popup);
			if (alertDailog != null) {
				postButton = (Button) alertDailog.findViewById(R.id.post);

				postButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						EditText name = (EditText) alertDailog.findViewById(R.id.name);
						EditText email = (EditText) alertDailog.findViewById(R.id.emailAddress);
						EditText comment = (EditText) alertDailog.findViewById(R.id.comment);
						if (validate(context, name, email, comment)) {
							postButton.setText(context.getString(R.string.processing));
							BuildService.build.postComment(postId, name.getText().toString(), email
									.getText().toString(), comment.getText().toString(),
									new Callback<String>() {

										@Override
										public void success(String output, Response arg1) {
											Log.i("output", output);
											try {
												postButton.setText(context.getString(R.string.post));
												JSONObject obj = new JSONObject(output);
												if (obj.getString("status").equals(
														context.getString(R.string.error))) {
													Utils.displayToad(context,
															obj.getString("error"));
												} else {
													Utils.displayToad(
															context,
															context.getString(R.string.post_comment_success_msg));
												}
												alertDailog.dismiss();
											} catch (Exception e) {
												postButton.setText(context.getString(R.string.post));
											}
										}

										@Override
										public void failure(RetrofitError retrofitError) {
											retrofitError.printStackTrace();
											postButton.setText(context.getString(R.string.post));
										}
									});
						}
					}
				});
			}
		} catch (Exception e) {
		}
	}

	private static boolean validate(Context context, EditText name, EditText email, EditText comment) {
		if (!Utils.isNetworkAvailable(context)) {
			Utils.displayToad(context, context.getString(R.string.no_internet));
			return false;
		}
		if ("".equals(name.getText().toString().trim())) {
			Utils.displayToad(context, context.getString(R.string.name_empty_error_msg));
			return false;
		}
		if ("".equals(email.getText().toString().trim())) {
			Utils.displayToad(context, context.getString(R.string.email_empty_error_msg));
			return false;
		}
		if (!(Patterns.EMAIL_ADDRESS).matcher(email.getText().toString().trim()).matches()) {
			Utils.displayToad(context, context.getString(R.string.invalid_email_error_msg));
			return false;
		}

		if ("".equals(comment.getText().toString().trim())) {
			Utils.displayToad(context, context.getString(R.string.comment_empty_error_msg));
			return false;
		}
		return true;
	}
}
