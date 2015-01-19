package com.wordlypost.utils;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.wordlypost.R;
import com.wordlypost.service.BuildService;

public class PostCommentPopUp {
	public static void showPostCommentDailog(final Context context, final int postId) {
		try {
			final Dialog alertDailog = Utils.alertDailog(context, R.layout.post_comment_popup);
			if (alertDailog != null) {
				Button postButton = (Button) alertDailog.findViewById(R.id.post);

				postButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						EditText name = (EditText) alertDailog.findViewById(R.id.name);
						EditText email = (EditText) alertDailog.findViewById(R.id.emailAddress);
						EditText comment = (EditText) alertDailog.findViewById(R.id.comment);
						BuildService.build.postComment(postId, name.getText().toString(), email
								.getText().toString(), comment.getText().toString(),
								new Callback<String>() {

									@Override
									public void success(String output, Response arg1) {
										Log.i("output", output);
										try {
											JSONObject obj = new JSONObject(output);
											if (obj.getString("status").equals(
													context.getString(R.string.error))) {
												Utils.displayToad(context, obj.getString("error"));
											} else {
												Utils.displayToad(
														context,
														context.getString(R.string.post_comment_success_msg));
											}
											alertDailog.dismiss();
										} catch (Exception e) {
										}
									}

									@Override
									public void failure(RetrofitError retrofitError) {
										retrofitError.printStackTrace();
									}
								});
					}
				});
			}
		} catch (Exception e) {
		}
	}
}
