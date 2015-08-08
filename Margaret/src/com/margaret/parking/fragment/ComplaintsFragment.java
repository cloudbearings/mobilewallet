package com.margaret.parking.fragment;

import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.margaret.parking.R;
import com.margaret.parking.activity.ComplaintRegistration;
import com.margaret.parking.adapters.ComplaintsAdapter;
import com.margaret.parking.db.DBOpenHelper;
import com.margaret.parking.pojo.ComplaintRecord;

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



    public ComplaintsFragment() {
    }

    public static ComplaintsFragment newInstance() {
        ComplaintsFragment fragment = new ComplaintsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        complainstList = DBOpenHelper.getInstance(getActivity()).fetchComplaints(getActivity());

        //downloadComplaints();
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
        mNoComplaintsTextView = (TextView)getActivity().findViewById(R.id.noComplaintsView);
        if(complainstList.isEmpty()){
            mNoComplaintsTextView.setVisibility(View.VISIBLE);
        }else{
            mNoComplaintsTextView.setVisibility(View.GONE);
        }
        mComplaintsAdapter = new ComplaintsAdapter(getActivity(), R.layout.item_complaints_list, 0, complainstList);
        mComplaintsListView.setAdapter(mComplaintsAdapter);

        mComplaintsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object viewHolder = view.getTag();
                mComplaintsAdapter.showPopupMenu(view, viewHolder,position);
            }
        });
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_complaint, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_new_complaint){
            final Intent intent = new Intent(getActivity(), ComplaintRegistration.class);
            startActivityForResult(intent, COMPLAINT_NEW_REQUEST);
            return true;
        }else if(item.getItemId() == R.id.verifiedOnly){
            mComplaintsAdapter.displayVerifiedOnly();
            return true;
        }else if(item.getItemId() == R.id.newComplaintsOnly){
            mComplaintsAdapter.displayNewComplaints();
            return true;
        }else if(item.getItemId() == R.id.paidComplaintsOnly){
            mComplaintsAdapter.displayPaidComlaints();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == COMPLAINT_NEW_REQUEST && resultCode == getActivity().RESULT_OK){
           refreshUI();
        }else if(requestCode == COMPLAINT_VERIFICATION_REQUEST && resultCode == getActivity().RESULT_OK){
            refreshUI();
        }
    }
    
    

    public void refreshUI(){
        complainstList.clear();
        complainstList = DBOpenHelper.getInstance(getActivity()).fetchComplaints(getActivity());
        if(complainstList.isEmpty()){
            mNoComplaintsTextView.setVisibility(View.VISIBLE);
        }else{
            mNoComplaintsTextView.setVisibility(View.GONE);
        }
        mComplaintsAdapter = new ComplaintsAdapter(getActivity(), R.layout.item_complaints_list, 0, complainstList);
        mComplaintsListView.setAdapter(mComplaintsAdapter);
    }


}
