package com.margaret.parking.customer;

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.margaret.parking.R;
import com.margaret.parking.adapters.GenericComplaintsAdapter;
import com.margaret.parking.db.DBOpenHelper;
import com.margaret.parking.pojo.ComplaintRecord;

public class CustClosedComplaints extends Fragment{
    List<ComplaintRecord> mClosedComplaintsList;
    ListView mComplaintsListView;
    GenericComplaintsAdapter mComplaintsAdapter;
    
    public CustClosedComplaints() {
        // TODO Auto-generated constructor stub
    }
    
    public static CustClosedComplaints getInstance(){
            CustClosedComplaints closedComplaintsFragment = new CustClosedComplaints();
        return closedComplaintsFragment;
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fetch records from DB.
        mClosedComplaintsList = DBOpenHelper.getInstance(getActivity()).fetchClosedComplaints();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.layout_complaints, null);
        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mComplaintsListView = (ListView) getActivity().findViewById(R.id.complaintsListView);
        mComplaintsAdapter = new GenericComplaintsAdapter(getActivity(), R.layout.item_complaints_list, 0, mClosedComplaintsList);
        mComplaintsListView.setAdapter(mComplaintsAdapter);

        mComplaintsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object viewHolder = view.getTag();
                //mComplaintsAdapter.showPopupMenu(view, viewHolder,position);
            }
        });
    }
    
    public void refreshUI(){
        mClosedComplaintsList.clear();
        mClosedComplaintsList = DBOpenHelper.getInstance(getActivity()).fetchClosedComplaints();
        mComplaintsAdapter = new GenericComplaintsAdapter(getActivity(), R.layout.item_complaints_list, 0, mClosedComplaintsList);
        mComplaintsListView.setAdapter(mComplaintsAdapter);
    }


}