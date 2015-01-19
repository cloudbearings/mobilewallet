package com.wordlypost;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wordlypost.WordlyPostGoogleAnalytics.TrackerName;
import com.wordlypost.beans.PostRowItem;
import com.wordlypost.utils.ImageLoader;
import com.wordlypost.utils.PostCommentPopUp;
import com.wordlypost.utils.Utils;

public class PostViewFragment extends Fragment {

	private PostRowItem postDetails;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view;

		view = inflater.inflate(R.layout.post_view, container, false);
		try {
			Bundle args = getArguments();
			if (args.getSerializable("postView") != null) {
				postDetails = (PostRowItem) args.getSerializable("postView");

				try {
					Tracker t = ((WordlyPostGoogleAnalytics) getActivity().getApplication())
							.getTracker(TrackerName.APP_TRACKER);
					t.setScreenName(postDetails.getTitle());
					t.send(new HitBuilders.AppViewBuilder().build());

				} catch (Exception e) {
					Log.d("TAG", getString(R.string.google_analytics_error));
				}

				TextView postTitle = (TextView) view.findViewById(R.id.post_title);
				postTitle.setText(Html.fromHtml(postDetails.getTitle()));
				postTitle.setTypeface(Utils.getFont(getActivity(), getString(R.string.Helvetica)),
						Typeface.BOLD);

				TextView author = (TextView) view.findViewById(R.id.author);
				author.setText(Html.fromHtml("By " + postDetails.getAuthor() + "<br/>"
						+ postDetails.getDate()));
				author.setTypeface(Utils.getFont(getActivity(), getString(R.string.DroidSerif)));

				ImageView postImage = (ImageView) view.findViewById(R.id.post_image);
				ImageLoader imageLoder = new ImageLoader(getActivity());
				imageLoder
						.DisplayImage(postDetails.getPost_banner(), R.drawable.loading, postImage);

				final WebView webView = (WebView) view.findViewById(R.id.content);
				webView.getSettings().setJavaScriptEnabled(true);
				webView.getSettings().setDefaultTextEncodingName("utf-8");
				String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
				webView.loadData(header + postDetails.getContent(), "text/html; charset=utf-8",
						null);
				webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

				Button readComments = (Button) view.findViewById(R.id.read_comments);
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

				Button postComment = (Button) view.findViewById(R.id.post_comment);
				postComment.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						PostCommentPopUp.showPostCommentDailog(getActivity(),
								postDetails.getPost_id());
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

	private void readComments(String commentsArray, int postId) {
		startActivity(new Intent(getActivity(), CommentView.class)
				.putExtra(getString(R.string.pst_comments), commentsArray)
				.putExtra(getString(R.string.pst_id), postId)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
			sendIntent.putExtra(Intent.EXTRA_TEXT, postDetails.getPost_url()).putExtra(
					Intent.EXTRA_SUBJECT, postDetails.getTitle());
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
			return true;
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
