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

	public PostAdapter(Context context, int resourceId,
			ArrayList<PostRowItem> items) {
		super(context, resourceId, items);
		this.context = context;
		imgLoader = new ImageLoader(context.getApplicationContext());
		this.items = items;
	}

	/* private view holder class */
	private class ViewHolder {

		TextView title, des, commentCount;
		ImageView post_image;
	}

	public View getView(final int position, View myAppsView, ViewGroup parent) {
		ViewHolder holder = null;
		PostRowItem rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (myAppsView == null) {

			myAppsView = mInflater.inflate(R.layout.post_list_item, parent,
					false);

			holder = new ViewHolder();

			holder.title = (TextView) myAppsView.findViewById(R.id.post_title);
			holder.des = (TextView) myAppsView.findViewById(R.id.post_des);
			holder.post_image = (ImageView) myAppsView
					.findViewById(R.id.post_icon);
			holder.commentCount = (TextView) myAppsView
					.findViewById(R.id.comment_count);

			myAppsView.setTag(holder);
		} else
			holder = (ViewHolder) myAppsView.getTag();

		holder.title.setText(Html.fromHtml(rowItem.getTitle()));
		holder.title.setTypeface(Utils.getFont(context,
				context.getString(R.string.Helvetica)));
		holder.des.setText(rowItem.getDate());
		holder.des.setTypeface(Utils.getFont(context,
				context.getString(R.string.DroidSerif)));

		holder.commentCount.setText(Html.fromHtml(rowItem.getComment_count()
				+ ""));
		holder.commentCount.setTypeface(Utils.getFont(context,
				context.getString(R.string.Helvetica)));

		int loader = R.drawable.app_default_icon;
		imgLoader.DisplayImage(rowItem.getPost_icon_url(), loader,
				holder.post_image);

		((ImageView) myAppsView.findViewById(R.id.post_image))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						context.startActivity(new Intent(context,
								PostViewSwipeActivity.class)
								.putExtra("postDetails", items)
								.putExtra("position", position)
								.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					}
				});

		return myAppsView;
	}

	public int add(List<PostRowItem> rowItems) {
		return rowItems.size();
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}
}
