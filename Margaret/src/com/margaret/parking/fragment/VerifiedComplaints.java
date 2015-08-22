package com.margaret.parking.fragment;

import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.margaret.parking.R;
import com.margaret.parking.adapters.ComplaintsAdapter;
import com.margaret.parking.adapters.GenericComplaintsAdapter;
import com.margaret.parking.db.DBOpenHelper;
import com.margaret.parking.pojo.ComplaintRecord;

public class VerifiedComplaints extends Fragment {
    List<ComplaintRecord> mClosedComplaintsList;
    ListView mComplaintsListView;
    ComplaintsAdapter mComplaintsAdapter;
    TextView mNoComplaintsTextView;
    final int COMPLAINT_CLAMP_REQUEST = 3;

    public VerifiedComplaints() {
        // TODO Auto-generated constructor stub
    }

    public static VerifiedComplaints getInstance() {
        VerifiedComplaints closedComplaintsFragment = new VerifiedComplaints();
        return closedComplaintsFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fetch records from DB.
        mClosedComplaintsList = DBOpenHelper.getInstance(getActivity()).fetchVerifiedComplaints();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.layout_complaints, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            mComplaintsListView = (ListView) getActivity().findViewById(R.id.complaintsListView);
            mNoComplaintsTextView = (TextView) getActivity().findViewById(R.id.noComplaintsView);
            showNoComplainteMsg();
            mComplaintsAdapter = new ComplaintsAdapter(getActivity(), R.layout.item_complaints_list, 0, mClosedComplaintsList);
            mComplaintsListView.setAdapter(mComplaintsAdapter);

            mComplaintsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object viewHolder = view.getTag();
                    mComplaintsAdapter.showPopupMenu(view, viewHolder, position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == COMPLAINT_CLAMP_REQUEST
                && resultCode == getActivity().RESULT_OK) {
            refreshUI();
        }
    }

    public void refreshUI() {
        try {
            mClosedComplaintsList.clear();
            mClosedComplaintsList = DBOpenHelper.getInstance(getActivity()).fetchVerifiedComplaints();
            showNoComplainteMsg();
            mComplaintsAdapter = new ComplaintsAdapter(getActivity(), R.layout.item_complaints_list, 0, mClosedComplaintsList);
            mComplaintsListView.setAdapter(mComplaintsAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNoComplainteMsg(){
        try{
            if (mClosedComplaintsList.isEmpty()) {
                mNoComplaintsTextView.setText(getString(R.string.no_verified_complaints));
                mNoComplaintsTextView.setVisibility(View.VISIBLE);
            } else {
                mNoComplaintsTextView.setVisibility(View.GONE);
            }
        }catch (Exception e){

        }
    }
}
