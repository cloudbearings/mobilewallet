package com.mobilewallet.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobilewallet.R;
import com.mobilewallet.beans.RechargeHistoryBean;

public class RechargeHistoryAdapter extends ArrayAdapter<RechargeHistoryBean> {

	Context context;
	List<RechargeHistoryBean> items;

	public RechargeHistoryAdapter(Context context, int resourceId, List<RechargeHistoryBean> items) {
		super(context, resourceId, items);
		this.items = items;
		this.context = context;
	}

	/* private view holder class */
	private class DebitViewHolder {

		TextView transactionId, amout, description, date, status;
	}

	public View getView(int position, View dbtView, ViewGroup parent) {
		DebitViewHolder holder = null;
		RechargeHistoryBean rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (dbtView == null) {

			dbtView = mInflater.inflate(R.layout.recharge_history_list_item, parent, false);

			holder = new DebitViewHolder();

			holder.transactionId = (TextView) dbtView.findViewById(R.id.tId);
			holder.amout = (TextView) dbtView.findViewById(R.id.tCoins);
			holder.description = (TextView) dbtView.findViewById(R.id.tDes);
			holder.date = (TextView) dbtView.findViewById(R.id.tDate);
			holder.status = (TextView) dbtView.findViewById(R.id.tStatus);

			dbtView.setTag(holder);
		} else
			holder = (DebitViewHolder) dbtView.getTag();

		holder.transactionId.setText(rowItem.getTid());
		holder.amout.setText(rowItem.getCoins() + "");
		holder.description.setText(Html.fromHtml(rowItem.getDes()));
		holder.date.setText(rowItem.getDate());
		holder.status.setText(Html.fromHtml(rowItem.getStatus()));

		return dbtView;
	}

	public int add(List<RechargeHistoryBean> rowItems) {
		return rowItems.size();
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}
}
