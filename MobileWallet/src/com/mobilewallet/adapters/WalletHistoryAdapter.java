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
import com.mobilewallet.beans.WalletHistoryBean;

public class WalletHistoryAdapter extends ArrayAdapter<WalletHistoryBean> {

	Context context;
	List<WalletHistoryBean> items;

	public WalletHistoryAdapter(Context context, int resourceId, List<WalletHistoryBean> items) {
		super(context, resourceId, items);
		this.items = items;
		this.context = context;
	}

	/* private view holder class */
	private class CreditViewHolder {

		TextView transactionId, amout, description, date;
	}

	public View getView(int position, View crdtView, ViewGroup parent) {
		CreditViewHolder holder = null;
		WalletHistoryBean rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (crdtView == null) {

			crdtView = mInflater.inflate(R.layout.wallet_history_list_item, parent, false);

			holder = new CreditViewHolder();

			holder.transactionId = (TextView) crdtView.findViewById(R.id.tId);
			holder.amout = (TextView) crdtView.findViewById(R.id.tCoins);
			holder.description = (TextView) crdtView.findViewById(R.id.tDes);
			holder.date = (TextView) crdtView.findViewById(R.id.tDate);

			crdtView.setTag(holder);
		} else
			holder = (CreditViewHolder) crdtView.getTag();

		holder.transactionId.setText(rowItem.getTid());
		holder.amout.setText(rowItem.getCoins() + "");
		holder.description.setText(Html.fromHtml(rowItem.getDes()));
		holder.date.setText(rowItem.getDate());

		return crdtView;
	}

	public int add(List<WalletHistoryBean> rowItems) {
		return rowItems.size();
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}
}
