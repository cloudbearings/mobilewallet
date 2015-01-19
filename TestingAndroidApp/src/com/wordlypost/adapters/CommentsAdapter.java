package com.wordlypost.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wordlypost.R;
import com.wordlypost.beans.CommentRowItem;
import com.wordlypost.utils.Utils;

public class CommentsAdapter extends ArrayAdapter<CommentRowItem> {

	private Context context;

	public CommentsAdapter(Context context, int resourceId, ArrayList<CommentRowItem> items) {
		super(context, resourceId, items);
		this.context = context;
	}

	/* private view holder class */
	private class ViewHolder {

		TextView comment, name, date;

	}

	public View getView(final int position, View commentView, ViewGroup parent) {
		ViewHolder holder = null;
		CommentRowItem rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (commentView == null) {

			commentView = mInflater.inflate(R.layout.comment_list_item, parent, false);
			holder = new ViewHolder();

			holder.comment = (TextView) commentView.findViewById(R.id.comment);
			holder.name = (TextView) commentView.findViewById(R.id.name);
			holder.date = (TextView) commentView.findViewById(R.id.date);

			commentView.setTag(holder);
		} else
			holder = (ViewHolder) commentView.getTag();

		holder.comment.setText(Html.fromHtml(rowItem.getContent()));
		holder.comment.setTypeface(Utils.getFont(context, context.getString(R.string.Helvetica)),
				Typeface.BOLD);

		holder.name.setText(Html.fromHtml(rowItem.getName()));
		holder.name.setTypeface(Utils.getFont(context, context.getString(R.string.Arial)));

		holder.date.setText(Html.fromHtml(rowItem.getDate()));
		holder.date.setTypeface(Utils.getFont(context, context.getString(R.string.Arial)));

		return commentView;
	}

	public int add(List<CommentRowItem> rowItems) {
		return rowItems.size();
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}
}
