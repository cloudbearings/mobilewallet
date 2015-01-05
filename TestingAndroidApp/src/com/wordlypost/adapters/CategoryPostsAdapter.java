package com.wordlypost.adapters;

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

import com.wordlypost.R;
import com.wordlypost.beans.CategoryPostsRowItem;
import com.wordlypost.utils.ImageLoader;
import com.wordlypost.utils.Utils;

public class CategoryPostsAdapter extends ArrayAdapter<CategoryPostsRowItem> {

	private Context context;
	private ImageLoader imgLoader;
	private CategoryPostsRowItem rowItem;

	public CategoryPostsAdapter(Context context, int resourceId, List<CategoryPostsRowItem> items) {
		super(context, resourceId, items);
		this.context = context;
		imgLoader = new ImageLoader(context.getApplicationContext());
	}

	/* private view holder class */
	private class ViewHolder {

		TextView title, date;
		ImageView post_image;
	}

	public View getView(final int position, View myAppsView, ViewGroup parent) {
		ViewHolder holder = null;
		rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (myAppsView == null) {

			myAppsView = mInflater.inflate(R.layout.category_posts_list_item, parent, false);

			holder = new ViewHolder();

			holder.title = (TextView) myAppsView.findViewById(R.id.title);
			holder.date = (TextView) myAppsView.findViewById(R.id.date);
			holder.post_image = (ImageView) myAppsView.findViewById(R.id.post_image);

			myAppsView.setTag(holder);
		} else
			holder = (ViewHolder) myAppsView.getTag();

		holder.title.setText(Html.fromHtml(rowItem.getTitle()));
		holder.title.setTypeface(Utils.getFont(context, context.getString(R.string.Helvetica)));
		holder.date.setText(rowItem.getDate());
		holder.date.setTypeface(Utils.getFont(context, context.getString(R.string.DroidSerif)));

		int loader = R.drawable.app_default_icon;
		imgLoader.DisplayImage(rowItem.getPost_icon_url(), loader, holder.post_image);

		((ImageView) myAppsView.findViewById(R.id.post_image))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						CategoryPostsRowItem item = getItem(position);
						/*context.startActivity(new Intent(context, AppView.class).putExtra("appId",
								item.getAPPID()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));*/
					}
				});

		return myAppsView;
	}

	public int add(List<CategoryPostsRowItem> rowItems) {
		return rowItems.size();
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}
}
