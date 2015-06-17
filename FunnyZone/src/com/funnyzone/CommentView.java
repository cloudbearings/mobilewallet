package com.funnyzone;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.funnyzone.adapters.CommentsAdapter;
import com.funnyzone.beans.CommentRowItem;
import com.funnyzone.googleanalytics.FunnyZoneGoogleAnalytics;
import com.funnyzone.googleanalytics.FunnyZoneGoogleAnalytics.TrackerName;
import com.funnyzone.utils.PostCommentPopUp;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class CommentView extends ActionBarActivity {

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			Tracker t = ((FunnyZoneGoogleAnalytics) getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			t.setScreenName(getString(R.string.comment_view_screen_name));
			t.send(new HitBuilders.AppViewBuilder().build());

		} catch (Exception e) {
			Log.d("TAG", getString(R.string.google_analytics_error));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_view);
		try {
			if (getIntent().getStringExtra(getString(R.string.pst_comments)) != null) {
				Log.i("comments :", getIntent().getStringExtra(getString(R.string.pst_comments)));
				Button postComment = (Button) findViewById(R.id.post_comment);
				postComment.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						PostCommentPopUp.showPostCommentDailog(CommentView.this, getIntent()
								.getIntExtra(getString(R.string.pst_id), 0));
					}
				});

				ArrayList<CommentRowItem> rowItems = new ArrayList<CommentRowItem>();
				ListView commentsList = (ListView) findViewById(R.id.commentsList);
				ProgressBar progressBar = (ProgressBar) findViewById(R.id.commentsProgressBar);
				progressBar.setVisibility(View.VISIBLE);
				CommentsAdapter adapter = new CommentsAdapter(CommentView.this,
						R.layout.comment_list_item, rowItems);
				commentsList.setAdapter(adapter);

				JSONArray commentsArray = new JSONArray(getIntent().getStringExtra(
						getString(R.string.pst_comments)));

				TextView commentCount = (TextView) findViewById(R.id.commentCount);
				Resources res = getResources();
				String text = String.format(res.getString(R.string.comments_count),
						commentsArray.length());
				commentCount.setText(text);

				for (int i = 0; i < commentsArray.length(); i++) {
					JSONObject commentObj = commentsArray.getJSONObject(i);
					CommentRowItem item = new CommentRowItem();
					item.setId(commentObj.getInt("id"));
					item.setName(commentObj.getString("name"));
					item.setUrl(commentObj.getString("url"));
					item.setDate(commentObj.getString("date"));
					item.setContent(commentObj.getString("content"));
					item.setParent(commentObj.getInt("parent"));
					rowItems.add(item);
				}
				adapter.notifyDataSetChanged();
				progressBar.setVisibility(View.GONE);
			}
		} catch (Exception e) {
		}
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
}
