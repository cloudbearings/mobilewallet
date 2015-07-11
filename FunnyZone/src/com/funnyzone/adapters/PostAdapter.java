package com.funnyzone.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnyzone.R;
import com.funnyzone.beans.PostRowItem;
import com.funnyzone.utils.ImageLoader;

public class PostAdapter extends ArrayAdapter<PostRowItem> {

	private Context context;
	private ImageLoader imgLoader;

	public PostAdapter(Context context, int resourceId,
			ArrayList<PostRowItem> items) {
		super(context, resourceId, items);
		this.context = context;
		imgLoader = new ImageLoader(context.getApplicationContext());
	}

	/* private view holder class */
	private class ViewHolder {
		TextView title;
		ImageView post_image;
	}

	public View getView(final int position, View postView, ViewGroup parent) {
		ViewHolder holder = null;
		PostRowItem rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (postView == null) {

			postView = mInflater
					.inflate(R.layout.post_list_item, parent, false);

			holder = new ViewHolder();

			holder.title = (TextView) postView.findViewById(R.id.post_title);
			holder.post_image = (ImageView) postView
					.findViewById(R.id.post_image);

			postView.setTag(holder);
		} else
			holder = (ViewHolder) postView.getTag();

		holder.title.setText(Html.fromHtml(rowItem.getTitle()));

		int loader = R.drawable.app_default_icon;
		imgLoader.DisplayImage(rowItem.getPost_icon_url(), loader,
				holder.post_image);

		return postView;
	}

	public int add(List<PostRowItem> rowItems) {
		return rowItems.size();
	}
}
