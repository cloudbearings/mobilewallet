package com.mobilewallet;

import com.mobilewallet.utils.Utils;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class ForgotPassword extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_password);

		TextView heading = (TextView) findViewById(R.id.textView2);
		heading.setTypeface(Utils.getFont(ForgotPassword.this, getString(R.string.Helvetica)),
				Typeface.BOLD);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		Rect dialogBounds = new Rect();
		getWindow().getDecorView().getHitRect(dialogBounds);

		if (!dialogBounds.contains((int) ev.getX(), (int) ev.getY())) {
			return false;
		}
		return super.dispatchTouchEvent(ev);
	}
}
