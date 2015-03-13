package com.mobilewallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class LearnGkFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.learn_gk_fragment, container,
				false);
		try {
			ImageView learnGK = (ImageView) view.findViewById(R.id.learn_GK);
			learnGK.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {

					startActivity(new Intent(getActivity(), Question.class)
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}
}
