package com.margaret.parking.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.margaret.parking.R;
import com.margaret.parking.db.DBOpenHelper;

public class Payment extends Activity {

	String mReferenceNumber, mVehicleNumber;
	int mAmountToBePaid;
	TextView mReferenceNumberTv;
	EditText mAmountEditText;
	Button mPaymentButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_payment);

		final Intent launchIntent = getIntent();
		mReferenceNumber = launchIntent.getStringExtra("REFERENCE_ID");
		mVehicleNumber = launchIntent.getStringExtra("VEHICLE_NUMBER");
		mAmountToBePaid = launchIntent.getIntExtra("AMOUNT_TO_BE_PAID", 600);

		mReferenceNumberTv = (TextView) findViewById(R.id.refPayment);
		mAmountEditText = (EditText) findViewById(R.id.amountToBePaid);
		mPaymentButton = (Button) findViewById(R.id.payment);

		String val = "REFERENCE NUMBER: " + mReferenceNumber;

		mReferenceNumberTv.setText(val.toString());
		mAmountEditText.setText(Integer.toString(mAmountToBePaid));

		mPaymentButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					int amount = Integer.valueOf(mAmountEditText.getText()
							.toString());
					long result = DBOpenHelper.getInstance(Payment.this)
							.updatePaymentStatus(mReferenceNumber, "true",
									amount);
					// Toast.makeText(Payment.this, "" +result,
					// Toast.LENGTH_LONG).show();

				} catch (Exception e) {

				}
				setResult(RESULT_OK);
				finish();
			}
		});
	}

	private final int PAYMENT_CODE = 10;

	public void sendAmount(View v) {
		Double amount = null;
		try {
			amount = Double.parseDouble(mAmountEditText.getText().toString());
			Intent i = new Intent("com.globalblue.mpa.PAYMENT_SEND");
			i.putExtra("amount", amount);
			i.putExtra("receiptno", mReferenceNumber);
			i.putExtra("demo", false);
			startActivityForResult(i, PAYMENT_CODE);
		} catch (NumberFormatException e) {
			Toast.makeText(this, "Entered amount not valid number",
					Toast.LENGTH_SHORT).show();
			return;
		}
	}

	public void sendVoid(View v) {
		Integer reference = null;
		try {

			/*EditText tbReference = (EditText) this
					.findViewById(R.id.tbReference);
			reference = Integer.parseInt(tbReference.getText().toString()
					.trim());
*/
			EditText tbAmount = (EditText) this
					.findViewById(R.id.amountToBePaid);
			Double amount = Double.parseDouble(tbAmount.getText().toString());
			Intent i = new Intent("com.globalblue.mpa.PAYMENT_VOID");
			// maybe add amount i.putExtra()
			i.putExtra("amount", amount);
			i.putExtra("reference", reference);
			i.putExtra("demo", false);
			startActivityForResult(i, PAYMENT_CODE);
		} catch (NumberFormatException e) {
			Toast.makeText(this, "Entered reference is not a valid number",
					Toast.LENGTH_SHORT).show();
			return;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == PAYMENT_CODE) {
			if (data.hasExtra("approved")) {
				Boolean approved = data.getExtras().getBoolean("approved",
						false);
				String authCode = data.getExtras().getString("authcode", "");
				Double amount = data.getExtras().getDouble("amount", 0.0);
				String maskedcard = data.getExtras().getString("maskedpan", "");
				String receiptno = data.getExtras().getString("receiptno", "");
				String receiptpath = data.getExtras().getString("receiptpath",
						"");
				Integer reference = data.getExtras().getInt("reference", 0);
				if (approved) {
					TextView tbResult = (TextView) this
							.findViewById(R.id.tbResult);
					tbResult.setText("Transaction complete \napproved:"
							+ approved + "\nreference: " + reference
							+ "\nauthcode: " + authCode + "\namount:" + amount
							+ "\ncard: " + maskedcard + "\nreceiptno: "
							+ receiptno);
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		setResult(RESULT_CANCELED);
	}
}
