package com.funnyzone.textview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.funnyzone.R;
import com.funnyzone.utils.FontCache;

public class HelveticaBold extends TextView {
	public HelveticaBold(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (!isInEditMode())
			init();
	}

	public HelveticaBold(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!isInEditMode())
			init();
	}

	public HelveticaBold(Context context) {
		super(context);
		if (!isInEditMode())
			init();
	}

	private void init() {
		try {
			Typeface tf = FontCache.get(
					getContext().getString(R.string.Helvetica), getContext());

			if (tf != null) {
				setTypeface(tf, Typeface.BOLD);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
