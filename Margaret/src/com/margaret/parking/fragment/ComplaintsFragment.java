package com.margaret.parking.fragment;

import java.util.List;

import org.json.JSONArray;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.margaret.parking.R;
import com.margaret.parking.activity.ComplaintRegistration;
import com.margaret.parking.adapters.ComplaintsAdapter;
import com.margaret.parking.db.DBOpenHelper;
import com.margaret.parking.gcm.Config;
import com.margaret.parking.pojo.ComplaintRecord;
import com.margaret.parking.service.BuildService;

/**
 * Created by varmu02 on 6/17/2015.
 */
public class ComplaintsFragment extends Fragment {
	ListView mComplaintsListView;
	List<ComplaintRecord> complainstList;
	ComplaintsAdapter mComplaintsAdapter;
	final int COMPLAINT_NEW_REQUEST = 1;
	final int COMPLAINT_VERIFICATION_REQUEST = 2;
	TextView mNoComplaintsTextView;
	private ImageView left, right;
	private View paginationView;
	private int pageindex = 0;

	public ComplaintsFragment() {
	}

	public static ComplaintsFragment newInstance() {
		ComplaintsFragment fragment = new ComplaintsFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.layout_complaints, null);
		try {
			left = (ImageView) view.findViewById(R.id.left);
			right = (ImageView) view.findViewById(R.id.right);
			paginationView = (View) view.findViewById(R.id.paginationView);
			mComplaintsListView = (ListView) view
					.findViewById(R.id.complaintsListView);
			getComplaintsFromSqlite(false);

			left.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					decrementPageSize();
					if (pageindex == 0) {
						left.setVisibility(View.INVISIBLE);
						right.setVisibility(View.VISIBLE);
						getComplaintsFromSqlite(true);
					} else {
						right.setVisibility(View.VISIBLE);
						getDataFromServer(true);
					}
				}
			});

			right.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					if (pageindex == 0) {
						incrementPageSize();
					}
					getDataFromServer(false);
				}
			});
			mNoComplaintsTextView = (TextView) view
					.findViewById(R.id.noComplaintsView);
			if (complainstList.isEmpty()) {
				mNoComplaintsTextView.setVisibility(View.VISIBLE);
			} else {
				mNoComplaintsTextView.setVisibility(View.GONE);
			}

			mComplaintsListView
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							Object viewHolder = view.getTag();
							mComplaintsAdapter.showPopupMenu(view, viewHolder,
									position);
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

	private void getComplaintsFromSqlite(boolean type) {
		complainstList = DBOpenHelper.getInstance(getActivity())
				.fetchComplaints(getActivity());
		mComplaintsAdapter = new ComplaintsAdapter(getActivity(),
				R.layout.item_complaints_list, 0, complainstList);
		mComplaintsListView.setAdapter(mComplaintsAdapter);

		if (complainstList.size() == Config.PAGE_SIZE) {
			paginationView.setVisibility(View.VISIBLE);
			if (!type) {
				incrementPageSize();
			}
		}
	}

	private void getDataFromServer(final boolean type) {
		BuildService.build.getAllComplaints(pageindex, Config.PAGE_SIZE,
				new Callback<String>() {

					@Override
					public void success(String complaintsAsString, Response arg1) {
						try {
							Log.i("Url", arg1.getUrl());
							Log.i("complaintsAsString", complaintsAsString);
							JSONArray complaints = new JSONArray(
									complaintsAsString);
							left.setVisibility(View.VISIBLE);

							if (!type && complaints.length() < Config.PAGE_SIZE) {
								right.setVisibility(View.INVISIBLE);
							}

							complainstList.clear();
							JsonParser jsonParser = new JsonParser();
							JsonArray complainsArray = (JsonArray) jsonParser
									.parse(complaintsAsString);
							for (int index = 0; index < complainsArray.size(); index++) {
								Gson gson = new GsonBuilder().create();
								final ComplaintRecord record = gson.fromJson(
										complainsArray.get(index),
										ComplaintRecord.class);
								complainstList.add(record);
							}

							mComplaintsAdapter = new ComplaintsAdapter(
									getActivity(),
									R.layout.item_complaints_list, 0,
									complainstList);
							mComplaintsListView.setAdapter(mComplaintsAdapter);
							mComplaintsAdapter.notifyDataSetChanged();
							if (!type
									&& complaints.length() == Config.PAGE_SIZE) {
								incrementPageSize();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void failure(RetrofitError retrofitError) {
						Log.d("Complaints Fragment ",
								"Exception raised in getAllCompliants service");
						retrofitError.printStackTrace();
					}
				});
	}

	private void incrementPageSize() {
		pageindex = pageindex + 1;
		Log.i("pagesize", pageindex + "");
	}

	private void decrementPageSize() {
		pageindex = pageindex - 1;
		Log.i("pagesize", pageindex + "");
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_complaint, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_new_complaint) {
			final Intent intent = new Intent(getActivity(),
					ComplaintRegistration.class);
			startActivityForResult(intent, COMPLAINT_NEW_REQUEST);
			return true;
		} else if (item.getItemId() == R.id.verifiedOnly) {
			mComplaintsAdapter.displayVerifiedOnly();
			return true;
		} else if (item.getItemId() == R.id.newComplaintsOnly) {
			mComplaintsAdapter.displayNewComplaints();
			return true;
		} else if (item.getItemId() == R.id.paidComplaintsOnly) {
			mComplaintsAdapter.displayPaidComlaints();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == COMPLAINT_NEW_REQUEST
				&& resultCode == getActivity().RESULT_OK) {
			refreshUI();
		} else if (requestCode == COMPLAINT_VERIFICATION_REQUEST
				&& resultCode == getActivity().RESULT_OK) {
			refreshUI();
		}
	}

	public void refreshUI() {
		try {
			complainstList.clear();
			complainstList = DBOpenHelper.getInstance(getActivity())
					.fetchComplaints(getActivity());
			if (complainstList.isEmpty()) {
				mNoComplaintsTextView.setVisibility(View.VISIBLE);
			} else {
				mNoComplaintsTextView.setVisibility(View.GONE);
			}
			mComplaintsAdapter = new ComplaintsAdapter(getActivity(),
					R.layout.item_complaints_list, 0, complainstList);
			mComplaintsListView.setAdapter(mComplaintsAdapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
