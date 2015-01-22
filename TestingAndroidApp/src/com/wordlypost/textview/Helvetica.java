package com.wordlypost.textview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wordlypost.R;
import com.wordlypost.utils.FontCache;

public class Helvetica extends TextView {
	public Helvetica(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (!isInEditMode())
			init();
	}

	public Helvetica(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!isInEditMode())
			init();
	}

	public Helvetica(Context context) {
		super(context);
		if (!isInEditMode())
			init();
	}

	private void init() {
		try {
			Typeface tf = FontCache.get(getContext().getString(R.string.Helvetica), getContext());

			if (tf != null) {
				setTypeface(tf);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
