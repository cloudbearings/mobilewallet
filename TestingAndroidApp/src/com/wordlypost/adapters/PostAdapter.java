package com.wordlypost.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wordlypost.PostViewSwipeActivity;
import com.wordlypost.R;
import com.wordlypost.beans.PostRowItem;
import com.wordlypost.utils.ImageLoader;
import com.wordlypost.utils.Utils;

public class PostAdapter extends ArrayAdapter<PostRowItem> {

	private Context context;
	private ImageLoader imgLoader;

	private ArrayList<PostRowItem> items;

	public PostAdapter(Context context, int resourceId, ArrayList<PostRowItem> items) {
		super(context, resourceId, items);
		this.context = context;
		imgLoader = new ImageLoader(context.getApplicationContext());
		this.items = items;
	}

	/* private view holder class */
	private class ViewHolder {

		TextView title, des, commentCount, date, author;
		ImageView post_image;
		LinearLayout commentLayout;
	}

	public View getView(final int position, View postView, ViewGroup parent) {
		ViewHolder holder = null;
		PostRowItem rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (postView == null) {

			postView = mInflater.inflate(R.layout.post_list_item, parent, false);

			holder = new ViewHolder();

			holder.title = (TextView) postView.findViewById(R.id.post_title);
			holder.des = (TextView) postView.findViewById(R.id.post_des);
			holder.post_image = (ImageView) postView.findViewById(R.id.post_icon);
			holder.date = (TextView) postView.findViewById(R.id.post_date);
			holder.author = (TextView) postView.findViewById(R.id.author);
			holder.commentCount = (TextView) postView.findViewById(R.id.comment_count);
			holder.commentLayout = (LinearLayout) postView.findViewById(R.id.comment_layout);

			postView.setTag(holder);
		} else
			holder = (ViewHolder) postView.getTag();

		holder.title.setText(Html.fromHtml(rowItem.getTitle()));
		holder.title.setTypeface(Utils.getFont(context, context.getString(R.string.Helvetica)));

		holder.des.setText(Html.fromHtml(rowItem.getPost_des()));
		holder.des.setTypeface(Utils.getFont(context, context.getString(R.string.Arial)));

		holder.date.setText(Html.fromHtml(rowItem.getDate()));
		holder.date.setTypeface(Utils.getFont(context, context.getString(R.string.DroidSerif)));

		holder.author.setText(Html.fromHtml(rowItem.getAuthor()));
		holder.author.setTypeface(Utils.getFont(context, context.getString(R.string.DroidSerif)));

		int loader = R.drawable.app_default_icon;
		imgLoader.DisplayImage(rowItem.getPost_icon_url(), loader, holder.post_image);

		holder.post_image.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				context.startActivity(new Intent(context, PostViewSwipeActivity.class)
						.putExtra("postDetails", items).putExtra("position", position)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			}
		});
		if (holder.commentLayout != null) {
			if (rowItem.getComment_count() > 0) {
				holder.commentLayout.setVisibility(View.VISIBLE);
				holder.commentCount.setText(Html.fromHtml(rowItem.getComment_count() + ""));
				holder.commentCount.setTypeface(Utils.getFont(context,
						context.getString(R.string.Helvetica)));
			} else {
				holder.commentLayout.setVisibility(View.INVISIBLE);
			}
		}

		return postView;
	}

	public int add(List<PostRowItem> rowItems) {
		return rowItems.size();
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}
}
