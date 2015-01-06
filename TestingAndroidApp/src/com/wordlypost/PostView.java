package com.wordlypost;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appfeed.FunAchievements;
import com.appfeed.R;
import com.appfeed.TabsActivity;
import com.wordlypost.beans.CategoryPostsRowItem;
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

			((TextView) findViewById(R.id.content))
					.setText(Html.fromHtml(postDetails.getContent()));

			ImageView postComment = (ImageView) findViewById(R.id.post_comment);
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
		final Dialog alertDailog = Utils.alertDailog(PostView.this,
				R.layout.fun_achievements_popup);
		if (alertDailog != null) {
			Button declineButton = (Button) alertDailog.findViewById(R.id.fun_achievements);

			declineButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					alertDailog.dismiss();
					startActivity(new Intent(TabsActivity.this, FunAchievements.class)
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				}
			});
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
