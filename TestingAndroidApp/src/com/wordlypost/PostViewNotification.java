package com.wordlypost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wordlypost.WordlyPostGoogleAnalytics.TrackerName;
import com.wordlypost.beans.PostRowItem;
import com.wordlypost.utils.ImageLoader;
import com.wordlypost.utils.PostCommentPopUp;
import com.wordlypost.utils.Utils;

public class PostViewNotification extends ActionBarActivity {

	private PostRowItem postDetails;
	String condition = "0";
	String ms;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_view);
		try {
			condition = getIntent().getStringExtra("alert");
			if (condition != null) {
				ms = getIntent().getStringExtra("msg");
				Utils.displayToad(PostViewNotification.this, ms);
			}

			Bundle args = getIntent().getExtras();
			if (args.getSerializable("postView") != null) {
				postDetails = (PostRowItem) args.getSerializable("postView");

				try {
					Tracker t = ((WordlyPostGoogleAnalytics) getApplication())
							.getTracker(TrackerName.APP_TRACKER);
					t.setScreenName(postDetails.getTitle());
					t.send(new HitBuilders.AppViewBuilder().build());

				} catch (Exception e) {
					Log.d("TAG", getString(R.string.google_analytics_error));
				}

				TextView postTitle = (TextView) findViewById(R.id.post_title);
				postTitle.setText(Html.fromHtml(postDetails.getTitle()));

				TextView author = (TextView) findViewById(R.id.author);
				author.setText(Html.fromHtml("By " + postDetails.getAuthor() + "<br/>"
						+ postDetails.getDate()));

				TextView commentCount = (TextView) findViewById(R.id.comment_count);
				if (postDetails.getComment_count() > 0) {
					commentCount.setVisibility(View.VISIBLE);
					commentCount.setText(Html.fromHtml(postDetails.getComment_count() + ""));
				} else {
					commentCount.setVisibility(View.INVISIBLE);
				}

				ImageView postImage = (ImageView) findViewById(R.id.post_image);
				ImageLoader imageLoder = new ImageLoader(PostViewNotification.this);
				imageLoder
						.DisplayImage(postDetails.getPost_banner(), R.drawable.loading, postImage);

				final WebView webView = (WebView) findViewById(R.id.content);
				webView.getSettings().setJavaScriptEnabled(true);
				webView.getSettings().setDefaultFontSize(
						getResources().getInteger(R.integer.post_des_text_size));
				webView.getSettings().setDefaultTextEncodingName("utf-8");
				String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
				webView.loadData(header + postDetails.getContent(), "text/html; charset=utf-8",
						null);
				webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

				Button readComments = (Button) findViewById(R.id.read_comments);
				if (postDetails.getComment_count() > 0) {
					readComments.setVisibility(View.VISIBLE);
					Resources res = getResources();
					String text = String.format(res.getString(R.string.read_comments),
							postDetails.getComment_count());
					readComments.setText(text
							+ readCommentButtonText(postDetails.getComment_count()));
				}

				readComments.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						readComments(postDetails.getCommentsArray(), postDetails.getPost_id());
					}
				});

				Button postComment = (Button) findViewById(R.id.post_comment);
				postComment.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						PostCommentPopUp.showPostCommentDailog(PostViewNotification.this,
								postDetails.getPost_id());
					}
				});

				try {
					JSONArray tagsArray = new JSONArray(postDetails.getTagsArray());
					LinearLayout Ll = (LinearLayout) findViewById(R.id.tags);
					if (tagsArray.length() > 0) {
						for (int i = 0; i < tagsArray.length(); i++) {
							final JSONObject tagObj = tagsArray.getJSONObject(i);

							TextView textDynamic = new TextView(PostViewNotification.this);
							textDynamic.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT));
							if (i == 0) {
								textDynamic.setText(getString(R.string.tags)
										+ tagObj.getString("title"));
							} else if (i == tagsArray.length() - 1) {
								textDynamic.setText(tagObj.getString("title"));
							} else {
								textDynamic.setText(", " + tagObj.getString("title") + ", ");
							}
							textDynamic.setTextColor(getResources().getColor(
									R.color.post_view_tags_color));
							textDynamic.setTextSize(getResources().getDimension(
									R.dimen.post_view_tags_textsize));
							textDynamic.isClickable();
							Ll.addView(textDynamic);

							textDynamic.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									try {
										startActivity(new Intent(PostViewNotification.this,
												TagPosts.class).putExtra(
												getString(R.string.tagSlug),
												tagObj.getString("id") + "$"
														+ tagObj.getString("slug")).addFlags(
												Intent.FLAG_ACTIVITY_CLEAR_TOP));
									} catch (JSONException je) {
										je.printStackTrace();
									}

								}
							});
						}
					} else {
						Ll.setVisibility(View.GONE);
					}
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readComments(String commentsArray, int postId) {
		startActivity(new Intent(PostViewNotification.this, CommentView.class)
				.putExtra(getString(R.string.pst_comments), commentsArray)
				.putExtra(getString(R.string.pst_id), postId)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent tabsIntent = new Intent(PostViewNotification.this, TabsActivity.class);
			tabsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(tabsIntent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private String readCommentButtonText(int commentCount) {
		if (commentCount > 1) {
			return getString(R.string.comments);
		} else {
			return getString(R.string.pv_comment);
		}
	}
}
