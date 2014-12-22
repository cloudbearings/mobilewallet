package com.mobilewallet.earnrewards.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobilewallet.R;
import com.mobilewallet.earnrewards.beans.UserQuestionsRowItem;

public class UserQuestionsAdapter extends ArrayAdapter<UserQuestionsRowItem> {

	Context context;
	List<UserQuestionsRowItem> items;

	public UserQuestionsAdapter(Context context, int resourceId,
			List<UserQuestionsRowItem> items) {
		super(context, resourceId, items);
		this.items = items;
		this.context = context;
	}

	/* private UserQuestionsHolder class */
	private class UserQuestionsHolder {

		TextView question, ansewer, status;
	}

	public View getView(int position, View usrQesView, ViewGroup parent) {
		UserQuestionsHolder holder = null;
		UserQuestionsRowItem rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (usrQesView == null) {

			usrQesView = mInflater.inflate(R.layout.user_questions_list_item,
					parent, false);

			holder = new UserQuestionsHolder();

			holder.question = (TextView) usrQesView.findViewById(R.id.question);
			holder.ansewer = (TextView) usrQesView.findViewById(R.id.answer);
			holder.status = (TextView) usrQesView.findViewById(R.id.status);

			usrQesView.setTag(holder);
		} else
			holder = (UserQuestionsHolder) usrQesView.getTag();

		holder.question.setText(rowItem.getQuestion());
		holder.ansewer.setText(rowItem.getAnsewer());
		holder.status.setText(rowItem.getStatus());

		return usrQesView;
	}

	public int add(List<UserQuestionsRowItem> rowItems) {
		return rowItems.size();
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}
}
