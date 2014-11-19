package com.mobilewallet;

import com.mobilewallet.users.BalanceActivity;
import com.mobilewallet.users.MyProfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MoreFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.more_fragment, container, false);

		ImageView wallet = (ImageView) view.findViewById(R.id.wallet);
		wallet.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), BalanceActivity.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

			}
		});

		ImageView dialyCredits = (ImageView) view
				.findViewById(R.id.dailyCredits);
		dialyCredits.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), DailyCredits.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

			}
		});

		ImageView notification = (ImageView) view
				.findViewById(R.id.notification);
		notification.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), Notifications.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

			}
		});

		ImageView profile = (ImageView) view.findViewById(R.id.profile);
		profile.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), MyProfile.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

			}
		});

		ImageView share = (ImageView) view.findViewById(R.id.share);
		share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		ImageView rate = (ImageView) view.findViewById(R.id.rate);
		rate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		ImageView feedBack = (ImageView) view.findViewById(R.id.feedBack);
		feedBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), Feedback.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			}
		});

		ImageView about = (ImageView) view.findViewById(R.id.about);
		about.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), About.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

			}
		});

		return view;
	}
}
