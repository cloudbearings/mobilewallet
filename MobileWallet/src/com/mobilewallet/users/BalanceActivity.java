package com.mobilewallet.users;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mobilewallet.R;
import com.mobilewallet.RechargeHistory;
import com.mobilewallet.TabsActivity;
import com.mobilewallet.WalletHistoy;

public class BalanceActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.balance_activity);

			TextView creditAmt = (TextView) findViewById(R.id.creditAmt);
			creditAmt.setText("100");
			creditAmt.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.rupee_icon, 0, 0, 0);

			TextView balance = (TextView) findViewById(R.id.balance);
			balance.setText("20");
			balance.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.rupee_icon, 0, 0, 0);

			TextView balnce_amt = (TextView) findViewById(R.id.balnce_amt);
			balnce_amt.setText(Html.fromHtml("<b>90</b>"));
			balnce_amt.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.rupee_icon, 0, 0, 0);

			((Button) findViewById(R.id.wallet_history))
					.setOnClickListener(new View.OnClickListener() {
						public void onClick(View view) {

							startActivity(new Intent(BalanceActivity.this,
									WalletHistoy.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
						}
					});

			((Button) findViewById(R.id.recharge_history))
					.setOnClickListener(new View.OnClickListener() {
						public void onClick(View view) {

							startActivity(new Intent(BalanceActivity.this,
									RechargeHistory.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
						}
					});

			((Button) findViewById(R.id.rechargeMyMobile))
					.setOnClickListener(new View.OnClickListener() {
						public void onClick(View view) {

							startActivity(new Intent(BalanceActivity.this,
									TabsActivity.class).putExtra(
									getString(R.string.recharge_tab),
									getString(R.string.show_true)).addFlags(
									Intent.FLAG_ACTIVITY_CLEAR_TOP));
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent tabsIntent = new Intent(BalanceActivity.this,
					TabsActivity.class);
			tabsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(tabsIntent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
