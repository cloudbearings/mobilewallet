package com.wordlypost;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wordlypost.beans.CategoryPostsRowItem;
import com.wordlypost.service.BuildService;
import com.wordlypost.utils.ImageLoader;
import com.wordlypost.utils.Utils;

public class PostView extends ActionBarActivity {

	private CategoryPostsRowItem postDetails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_view);
		try {
			postDetails = (CategoryPostsRowItem) getIntent().getSerializableExtra("postDetails");

			TextView postTitle = (TextView) findViewById(R.id.post_title);
			postTitle.setText(Html.fromHtml(postDetails.getTitle()));
			postTitle.setTypeface(Utils.getFont(PostView.this, getString(R.string.Helvetica)));

			TextView author = (TextView) findViewById(R.id.author);
			author.setText(Html.fromHtml("By " + postDetails.getAuthor() + "<br/>"
					+ postDetails.getDate()));
			author.setTypeface(Utils.getFont(PostView.this, getString(R.string.DroidSerif)));

			ImageView postImage = (ImageView) findViewById(R.id.post_image);
			ImageLoader imageLoder = new ImageLoader(PostView.this);
			imageLoder.DisplayImage(postDetails.getPost_banner(), R.drawable.loading, postImage);

			WebView webView = (WebView) findViewById(R.id.content);
			webView.loadData(postDetails.getContent(), "text/html", null);
			webView.getSettings().setLoadsImagesAutomatically(false);
			webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

			Button postComment = (Button) findViewById(R.id.post_comment);
			postComment.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showPostCommentDaiolg();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showPostCommentDaiolg() {
		try {
			final Dialog alertDailog = Utils
					.alertDailog(PostView.this, R.layout.post_comment_popup);
			if (alertDailog != null) {
				Button postButton = (Button) alertDailog.findViewById(R.id.post);

				postButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						EditText name = (EditText) alertDailog.findViewById(R.id.name);
						EditText email = (EditText) alertDailog.findViewById(R.id.emailAddress);
						EditText comment = (EditText) alertDailog.findViewById(R.id.comment);
						BuildService.build.postComment(postDetails.getPost_id(), name.getText()
								.toString(), email.getText().toString(), comment.getText()
								.toString(), new Callback<String>() {

							@Override
							public void success(String output, Response arg1) {
								Log.i("output", output);
								try {
									JSONObject obj = new JSONObject(output);
									if (obj.getString("status").equals(getString(R.string.error))) {
										Utils.displayToad(PostView.this, obj.getString("error"));
									} else {
										Utils.displayToad(PostView.this,
												getString(R.string.post_comment_success_msg));
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.share_icon:
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, postDetails.getPost_url()).putExtra(
					Intent.EXTRA_SUBJECT, postDetails.getTitle());
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
