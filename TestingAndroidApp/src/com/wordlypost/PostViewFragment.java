package com.wordlypost;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wordlypost.WordlyPostGoogleAnalytics.TrackerName;
import com.wordlypost.beans.PostRowItem;
import com.wordlypost.service.BuildService;
import com.wordlypost.utils.ImageLoader;
import com.wordlypost.utils.Utils;

public class PostViewFragment extends Fragment {

	private PostRowItem postDetails;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;

		view = inflater.inflate(R.layout.post_view, container, false);
		try {
			Bundle args = getArguments();
			if (args.getSerializable("postView") != null) {
				postDetails = (PostRowItem) args.getSerializable("postView");

				try {
					Tracker t = ((WordlyPostGoogleAnalytics) getActivity()
							.getApplication())
							.getTracker(TrackerName.APP_TRACKER);
					t.setScreenName(postDetails.getTitle());
					t.send(new HitBuilders.AppViewBuilder().build());

				} catch (Exception e) {
					Log.d("TAG", getString(R.string.google_analytics_error));
				}

				TextView postTitle = (TextView) view
						.findViewById(R.id.post_title);
				postTitle.setText(Html.fromHtml(postDetails.getTitle()));
				postTitle.setTypeface(Utils.getFont(getActivity(),
						getString(R.string.Helvetica)));

				TextView author = (TextView) view.findViewById(R.id.author);
				author.setText(Html.fromHtml("By " + postDetails.getAuthor()
						+ "<br/>" + postDetails.getDate()));
				author.setTypeface(Utils.getFont(getActivity(),
						getString(R.string.DroidSerif)));

				ImageView postImage = (ImageView) view
						.findViewById(R.id.post_image);
				ImageLoader imageLoder = new ImageLoader(getActivity());
				imageLoder.DisplayImage(postDetails.getPost_banner(),
						R.drawable.loading, postImage);

				final WebView webView = (WebView) view
						.findViewById(R.id.content);
				webView.getSettings().setJavaScriptEnabled(true);
				webView.getSettings().setDefaultTextEncodingName("utf-8");
				String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
				webView.loadData(header + postDetails.getContent(),
						"text/html; charset=utf-8", null);
				webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

				Button readComments = (Button) view
						.findViewById(R.id.read_comments);
				if (postDetails.getComment_count() > 0) {
					readComments.setVisibility(View.VISIBLE);
					Resources res = getResources();
					String text = String.format(
							res.getString(R.string.read_comments),
							postDetails.getComment_count());
					readComments.setText(text);
				}

				Button postComment = (Button) view
						.findViewById(R.id.post_comment);
				postComment.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						showPostCommentDaiolg();
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

	private void showPostCommentDaiolg() {
		try {
			final Dialog alertDailog = Utils.alertDailog(getActivity(),
					R.layout.post_comment_popup);
			if (alertDailog != null) {
				Button postButton = (Button) alertDailog
						.findViewById(R.id.post);

				postButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						EditText name = (EditText) alertDailog
								.findViewById(R.id.name);
						EditText email = (EditText) alertDailog
								.findViewById(R.id.emailAddress);
						EditText comment = (EditText) alertDailog
								.findViewById(R.id.comment);
						BuildService.build.postComment(
								postDetails.getPost_id(), name.getText()
										.toString(),
								email.getText().toString(), comment.getText()
										.toString(), new Callback<String>() {

									@Override
									public void success(String output,
											Response arg1) {
										Log.i("output", output);
										try {
											JSONObject obj = new JSONObject(
													output);
											if (obj.getString("status").equals(
													getString(R.string.error))) {
												Utils.displayToad(
														getActivity(),
														obj.getString("error"));
											} else {
												Utils.displayToad(
														getActivity(),
														getString(R.string.post_comment_success_msg));
											}
											alertDailog.dismiss();
										} catch (Exception e) {
										}
									}

									@Override
									public void failure(
											RetrofitError retrofitError) {
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			getActivity().finish();
			return true;
		case R.id.share_icon:
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, postDetails.getPost_url())
					.putExtra(Intent.EXTRA_SUBJECT, postDetails.getTitle());
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
