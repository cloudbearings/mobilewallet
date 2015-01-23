package com.wordlypost;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Context;
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

import com.wordlypost.beans.NavDrawerItem;
import com.wordlypost.dao.CategoriesDAO;
import com.wordlypost.service.BuildService;
import com.wordlypost.utils.ImageLoader;

public class HomeFragment extends Fragment {

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_fragment, container, false);
		try {
			CategoriesDAO categoriesDAO = new CategoriesDAO(getActivity());
			List<NavDrawerItem> categories = categoriesDAO.getRandomCategories();
			for (int i = 0; i < categories.size(); i++) {
				NavDrawerItem category = categories.get(i);
				getCategoryPosts(category.getId(), category.getSlug(), 5, view, i,
						category.getTitle());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

	private void getCategoryPosts(int categoryId, String slug, int count, final View view,
			final int i, final String categoryTitle) {
		BuildService.build.getCategoryPosts(categoryId, slug, count, new Callback<String>() {

			@Override
			public void success(String output, Response arg1) {
				Log.i("CategoryPosts :", output);
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
					((TextView) view.findViewById(categoty_title[i])).setText(Html
							.fromHtml(categoryTitle));
					JSONObject obj = new JSONObject(output);
					JSONArray postsArray = obj.getJSONArray("posts");
					JSONObject post = postsArray.getJSONObject(0);

					ImageView postScreen = (ImageView) view.findViewById(post_screen[i]);
					ImageLoader imageLoader = new ImageLoader(getActivity());
					imageLoader.DisplayImage(
							post.getJSONObject("thumbnail_images").getJSONObject("full")
									.getString("url"), R.drawable.loading, postScreen);

					((TextView) view.findViewById(title1[i])).setText(Html.fromHtml(post
							.getString("title")));

					((TextView) view.findViewById(date1[i])).setText(Html.fromHtml(post
							.getString("date")));

					((TextView) view.findViewById(des[i])).setText(Html.fromHtml(post
							.getString("excerpt")));

					JSONObject post11 = postsArray.getJSONObject(1);
					ImageView postIcon11 = (ImageView) view.findViewById(post_icon11[i]);
					ImageLoader imageLoader11 = new ImageLoader(getActivity());
					imageLoader11.DisplayImage(post11.getString("thumbnail"),
							R.drawable.app_default_icon, postIcon11);

					((TextView) view.findViewById(title11[i])).setText(Html.fromHtml(post11
							.getString("title")));

					((TextView) view.findViewById(date11[i])).setText(Html.fromHtml(post11
							.getString("date")));

					JSONObject post12 = postsArray.getJSONObject(2);
					ImageView postIcon12 = (ImageView) view.findViewById(post_icon12[i]);
					ImageLoader imageLoader12 = new ImageLoader(getActivity());
					imageLoader12.DisplayImage(post12.getString("thumbnail"),
							R.drawable.app_default_icon, postIcon12);

					((TextView) view.findViewById(title12[i])).setText(Html.fromHtml(post11
							.getString("title")));

					((TextView) view.findViewById(date12[i])).setText(Html.fromHtml(post11
							.getString("date")));

					JSONObject post13 = postsArray.getJSONObject(3);
					ImageView postIcon13 = (ImageView) view.findViewById(post_icon13[i]);
					ImageLoader imageLoader13 = new ImageLoader(getActivity());
					imageLoader13.DisplayImage(post13.getString("thumbnail"),
							R.drawable.app_default_icon, postIcon13);

					((TextView) view.findViewById(title13[i])).setText(Html.fromHtml(post11
							.getString("title")));

					((TextView) view.findViewById(date13[i])).setText(Html.fromHtml(post11
							.getString("date")));

					JSONObject post14 = postsArray.getJSONObject(4);
					ImageView postIcon14 = (ImageView) view.findViewById(post_icon14[i]);
					ImageLoader imageLoader14 = new ImageLoader(getActivity());
					imageLoader14.DisplayImage(post14.getString("thumbnail"),
							R.drawable.app_default_icon, postIcon14);

					((TextView) view.findViewById(title14[i])).setText(Html.fromHtml(post11
							.getString("title")));

					((TextView) view.findViewById(date14[i])).setText(Html.fromHtml(post11
							.getString("date")));
				} catch (Exception e) {
				}
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				retrofitError.printStackTrace();
			}
		});
	}
}
