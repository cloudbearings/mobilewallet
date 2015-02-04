package com.wordlypost;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wordlypost.WordlyPostGoogleAnalytics.TrackerName;
import com.wordlypost.beans.NavDrawerItem;
import com.wordlypost.beans.PostRowItem;
import com.wordlypost.dao.HomeDAO;
import com.wordlypost.google.adcontroller.AdController;
import com.wordlypost.utils.ImageLoader;

public class HomeFragment extends Fragment {
	private AdController adController;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			if (adController != null) {
				adController.resumeAdView();
			} else {
				new AdController().resumeAdView();
			}

			Tracker t = ((WordlyPostGoogleAnalytics) getActivity().getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			t.setScreenName(getString(R.string.home_fragment_screen_name));
			t.send(new HitBuilders.AppViewBuilder().build());

		} catch (Exception e) {
			Log.d("TAG", getString(R.string.google_analytics_error));
		}
	}

	private HomeDAO homeDAO;

	private static final int[] hidden_layout = new int[] { R.id.hiddenLayout1, R.id.hiddenLayout2,
			R.id.hiddenLayout3, R.id.hiddenLayout4, R.id.hiddenLayout5 };

	private static final int[] hidden = new int[] { R.layout.hidden1, R.layout.hidden2,
			R.layout.hidden3, R.layout.hidden4, R.layout.hidden5 };

	private static final int[] linearLayout = new int[] { R.id.linearLayout1, R.id.linearLayout2,
			R.id.linearLayout3, R.id.linearLayout4, R.id.linearLayout5 };

	private static final int[] categoty_title = new int[] { R.id.category_title1,
			R.id.category_title2, R.id.category_title3, R.id.category_title4, R.id.category_title5 };

	private static final int[] post_screen = new int[] { R.id.post_screen1, R.id.post_screen2,
			R.id.post_screen3, R.id.post_screen4, R.id.post_screen5 };

	private static final int[] title1 = new int[] { R.id.title1, R.id.title2, R.id.title3,
			R.id.title4, R.id.title5 };

	private static final int[] date1 = new int[] { R.id.date1, R.id.date2, R.id.date3, R.id.date4,
			R.id.date5 };

	private static final int[] des = new int[] { R.id.des1, R.id.des2, R.id.des3, R.id.des4,
			R.id.des5 };

	private static final int[] post_icon11 = new int[] { R.id.post_image11, R.id.post_image21,
			R.id.post_image31, R.id.post_image41, R.id.post_image51 };

	private static final int[] title11 = new int[] { R.id.title11, R.id.title21, R.id.title31,
			R.id.title41, R.id.title51 };

	private static final int[] date11 = new int[] { R.id.date11, R.id.date21, R.id.date31,
			R.id.date41, R.id.date51 };

	private static final int[] post_icon12 = new int[] { R.id.post_image12, R.id.post_image22,
			R.id.post_image32, R.id.post_image42, R.id.post_image52 };

	private static final int[] title12 = new int[] { R.id.title12, R.id.title22, R.id.title32,
			R.id.title42, R.id.title52 };

	private static final int[] date12 = new int[] { R.id.date12, R.id.date22, R.id.date32,
			R.id.date42, R.id.date52 };

	private static final int[] post_icon13 = new int[] { R.id.post_image13, R.id.post_image23,
			R.id.post_image33, R.id.post_image43, R.id.post_image53 };

	private static final int[] title13 = new int[] { R.id.title13, R.id.title23, R.id.title33,
			R.id.title43, R.id.title53 };

	private static final int[] date13 = new int[] { R.id.date13, R.id.date23, R.id.date33,
			R.id.date43, R.id.date53 };

	private static final int[] post_icon14 = new int[] { R.id.post_image14, R.id.post_image24,
			R.id.post_image34, R.id.post_image44, R.id.post_image54 };

	private static final int[] title14 = new int[] { R.id.title14, R.id.title24, R.id.title34,
			R.id.title44, R.id.title54 };

	private static final int[] date14 = new int[] { R.id.date14, R.id.date24, R.id.date34,
			R.id.date44, R.id.date54 };

	private static final int[] relativeLayout1 = new int[] { R.id.relative_layout1,
			R.id.relative_layout2, R.id.relative_layout3, R.id.relative_layout4,
			R.id.relative_layout5 };

	private static final int[] relativeLayout11 = new int[] { R.id.relative_layout11,
			R.id.relative_layout21, R.id.relative_layout31, R.id.relative_layout41,
			R.id.relative_layout51 };

	private static final int[] relativeLayout12 = new int[] { R.id.relative_layout12,
			R.id.relative_layout22, R.id.relative_layout32, R.id.relative_layout42,
			R.id.relative_layout52 };

	private static final int[] relativeLayout13 = new int[] { R.id.relative_layout13,
			R.id.relative_layout23, R.id.relative_layout33, R.id.relative_layout43,
			R.id.relative_layout53 };

	private static final int[] relativeLayout14 = new int[] { R.id.relative_layout14,
			R.id.relative_layout24, R.id.relative_layout34, R.id.relative_layout44,
			R.id.relative_layout54 };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_fragment, container, false);
		try {
			try {
				RelativeLayout bannerLayout = (RelativeLayout) view.findViewById(R.id.homeBannerAd);
				new AdController().bannerAd(getActivity(), bannerLayout,
						getString(R.string.home_banner_unit_id));
			} catch (Exception e) {
				e.printStackTrace();
			}

			homeDAO = new HomeDAO(getActivity());
			List<NavDrawerItem> categories = homeDAO.getHomeRandomCategories();
			for (int i = 0; i < categories.size(); i++) {
				NavDrawerItem category = categories.get(i);
				if (i == 0 && homeDAO.isCategoryExists(120) > 0) {
					getCategoryPosts(120, "top-news", 5, view, i, "Top News");
				} else {
					getCategoryPosts(category.getId(), category.getSlug(), 5, view, i,
							category.getTitle());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

	private void getCategoryPosts(final int categoryId, final String slug, int count,
			final View view, final int i, final String categoryTitle) {
		List<PostRowItem> posts = homeDAO.getfivePosts(categoryId, slug);
		RelativeLayout hiddenLayout = (RelativeLayout) view.findViewById(hidden_layout[i]);

		if (hiddenLayout == null) {
			// Inflate the Hidden Layout Information View
			LinearLayout myLayout = (LinearLayout) view.findViewById(linearLayout[i]);
			LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			View hiddenInfo = inflater.inflate(hidden[i], myLayout, false);
			myLayout.addView(hiddenInfo);
		}
		try {
			((TextView) view.findViewById(categoty_title[i])).setText(Html.fromHtml(categoryTitle));
			PostRowItem post = posts.get(0);
			ImageView postScreen = (ImageView) view.findViewById(post_screen[i]);
			ImageLoader imageLoader = new ImageLoader(getActivity());
			imageLoader.DisplayImage(post.getPost_banner(), R.drawable.loading, postScreen);

			((TextView) view.findViewById(title1[i])).setText(Html.fromHtml(post.getTitle()));

			((TextView) view.findViewById(date1[i])).setText(Html.fromHtml(post.getDate()));

			((TextView) view.findViewById(des[i])).setText(Html.fromHtml(post.getPost_des()));

			((RelativeLayout) view.findViewById(relativeLayout1[i]))
					.setOnClickListener(new View.OnClickListener() {
						public void onClick(View view) {
							showPostView(categoryId, slug, 0);
						}
					});

			PostRowItem post11 = posts.get(1);
			ImageView postIcon11 = (ImageView) view.findViewById(post_icon11[i]);
			ImageLoader imageLoader11 = new ImageLoader(getActivity());
			imageLoader11.DisplayImage(post11.getPost_icon_url(), R.drawable.app_default_icon,
					postIcon11);

			((TextView) view.findViewById(title11[i])).setText(Html.fromHtml(post11.getTitle()));

			((TextView) view.findViewById(date11[i])).setText(Html.fromHtml(post11.getDate()));

			((RelativeLayout) view.findViewById(relativeLayout11[i]))
					.setOnClickListener(new View.OnClickListener() {
						public void onClick(View view) {
							showPostView(categoryId, slug, 1);
						}
					});

			PostRowItem post12 = posts.get(2);
			ImageView postIcon12 = (ImageView) view.findViewById(post_icon12[i]);
			ImageLoader imageLoader12 = new ImageLoader(getActivity());
			imageLoader12.DisplayImage(post12.getPost_icon_url(), R.drawable.app_default_icon,
					postIcon12);

			((TextView) view.findViewById(title12[i])).setText(Html.fromHtml(post12.getTitle()));

			((TextView) view.findViewById(date12[i])).setText(Html.fromHtml(post12.getDate()));

			((RelativeLayout) view.findViewById(relativeLayout12[i]))
					.setOnClickListener(new View.OnClickListener() {
						public void onClick(View view) {
							showPostView(categoryId, slug, 2);
						}
					});

			PostRowItem post13 = posts.get(3);
			ImageView postIcon13 = (ImageView) view.findViewById(post_icon13[i]);
			ImageLoader imageLoader13 = new ImageLoader(getActivity());
			imageLoader13.DisplayImage(post13.getPost_icon_url(), R.drawable.app_default_icon,
					postIcon13);

			((TextView) view.findViewById(title13[i])).setText(Html.fromHtml(post13.getTitle()));

			((TextView) view.findViewById(date13[i])).setText(Html.fromHtml(post13.getDate()));

			((RelativeLayout) view.findViewById(relativeLayout13[i]))
					.setOnClickListener(new View.OnClickListener() {
						public void onClick(View view) {
							showPostView(categoryId, slug, 3);
						}
					});

			PostRowItem post14 = posts.get(4);
			ImageView postIcon14 = (ImageView) view.findViewById(post_icon14[i]);
			ImageLoader imageLoader14 = new ImageLoader(getActivity());
			imageLoader14.DisplayImage(post14.getPost_icon_url(), R.drawable.app_default_icon,
					postIcon14);

			((TextView) view.findViewById(title14[i])).setText(Html.fromHtml(post14.getTitle()));

			((TextView) view.findViewById(date14[i])).setText(Html.fromHtml(post14.getDate()));

			((RelativeLayout) view.findViewById(relativeLayout14[i]))
					.setOnClickListener(new View.OnClickListener() {
						public void onClick(View view) {
							showPostView(categoryId, slug, 4);
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showPostView(int categoryId, String slug, int position) {
		if (homeDAO != null) {
			ArrayList<PostRowItem> items = homeDAO.getPosts(categoryId, slug);
			startActivity(new Intent(getActivity(), PostViewSwipeActivity.class)
					.putExtra("postDetails", items).putExtra("position", position)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (adController != null) {
			adController.pauseAdView();
		} else {
			new AdController().pauseAdView();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (adController != null) {
			adController.destroyAdView();
		} else {
			new AdController().destroyAdView();
		}
	}
}
