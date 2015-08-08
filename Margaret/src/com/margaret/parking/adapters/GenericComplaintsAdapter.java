package com.margaret.parking.adapters;


import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.margaret.parking.R;
import com.margaret.parking.activity.Clamp;
import com.margaret.parking.activity.ComplaintVerification;
import com.margaret.parking.activity.Payment;
import com.margaret.parking.activity.Tow;
import com.margaret.parking.pojo.ComplaintRecord;

/**
 * Created by varmu02 on 6/17/2015.
 */
public class GenericComplaintsAdapter extends ArrayAdapter {
    List<ComplaintRecord> listOfComplaints;
    Context mContext;
    public static PopupMenu popupMenu;


    public GenericComplaintsAdapter(Context context, int resource, int textViewResourceId, List objects) {
        super(context, resource, textViewResourceId, objects);
        this.mContext = context;
        listOfComplaints = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_complaints_list, null);
            viewHolder = new ViewHolder();
            viewHolder.referenecNumber = (TextView) convertView.findViewById(R.id.refNumber);
            viewHolder.buildingName = (TextView) convertView.findViewById(R.id.buildingName);
            viewHolder.clusterId = (TextView) convertView.findViewById(R.id.clusterId);
            viewHolder.bayNumber = (TextView) convertView.findViewById(R.id.bayNumber);
            viewHolder.level = (TextView) convertView.findViewById(R.id.level);
            viewHolder.poupupImage = (ImageView) convertView.findViewById(R.id.popupImage);
            viewHolder.dateNumber = (TextView) convertView.findViewById(R.id.dateNumber);
            viewHolder.dateMonth = (TextView) convertView.findViewById(R.id.dateMonth);
            viewHolder.verifiedIcon = (ImageView)convertView.findViewById(R.id.verifiedIcon);
            viewHolder.clampIcon = (ImageView) convertView.findViewById(R.id.clampIcon);
            viewHolder.towIcon = (ImageView) convertView.findViewById(R.id.truckIcon);
            viewHolder.paymentIcon = (ImageView) convertView.findViewById(R.id.paymentIcon);
            viewHolder.complaintClosedDate = (TextView) convertView.findViewById(R.id.closedDate);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ComplaintRecord compRec = listOfComplaints.get(position);
        JsonObject locationArray = compRec.getLocation();

        String cluster = locationArray.getAsJsonObject().get("cluster").getAsString();
        String buildingName = locationArray.getAsJsonObject().get("name").getAsString();
        String bay_number = locationArray.getAsJsonObject().get("bay").getAsString();
        String levelNumber = locationArray.getAsJsonObject().get("level").getAsString();

        String date = compRec.getComplaint().get("date").getAsString();
        if(date.contains(" ")){
            String[] dateAfterSplit = date.split(" ");
            String dateMonthYear[] = dateAfterSplit[0].split("/");
            viewHolder.dateNumber.setText(dateMonthYear[0]);
            String[] monthNames = mContext.getResources().getStringArray(R.array.month_names);
            String month = monthNames[Integer.parseInt(dateMonthYear[1]) - 1];
            String year = dateMonthYear[2];
            viewHolder.dateMonth.setText(month + " "+ year);

        }

        //Set values
        viewHolder.referenecNumber.setText(compRec.getReferenceId());
        viewHolder.clusterId.setText(cluster);
        viewHolder.buildingName.setText(buildingName);
        viewHolder.bayNumber.setText(bay_number);
        viewHolder.level.setText(levelNumber);

        final ComplaintRecord record = listOfComplaints.get(position);
        String verifiedStatus = record.getComplaint().get("v_status").getAsString();
        String clampStatus = record.getComplaint().get("c_status").getAsString();
        String towingStatus = record.getComplaint().get("t_status").getAsString();
        final String paymentStatus = record.getComplaint().get("p_status").getAsString();
        final String livingStatus = record.getComplaint().get("l_status").getAsString();
        String dateString = "";
        if(record.getComplaint().get("v_date") != null){
            dateString = record.getComplaint().get("v_date").getAsString();
        }
                
        
        if(Boolean.valueOf(verifiedStatus) && Boolean.valueOf(livingStatus)){
            viewHolder.verifiedIcon.setVisibility(View.VISIBLE);
        }
        
        if(!TextUtils.isEmpty(dateString) && dateString.contains(" ") && !Boolean.valueOf(livingStatus)){
            viewHolder.complaintClosedDate.setVisibility(View.VISIBLE);
            viewHolder.complaintClosedDate.setText(mContext.getString(R.string.complaint_closed_on).toString().concat(dateString.split(" ")[0]));
        }

        
        
        if(Boolean.valueOf(clampStatus)){
            viewHolder.clampIcon.setVisibility(View.VISIBLE);
        }

        if(Boolean.valueOf(towingStatus)){
            viewHolder.towIcon.setVisibility(View.VISIBLE);
        }

        if(Boolean.valueOf(paymentStatus)){
            viewHolder.paymentIcon.setVisibility(View.VISIBLE);
        }


        return convertView;
    }

    /*public void showPopupMenu(View view, Object viewHolder, final int position) {
        popupMenu = new PopupMenu(mContext, ((ViewHolder) viewHolder).poupupImage);
        popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
        final ComplaintRecord selectedRecord = listOfComplaints.get(position);
        final JsonObject complaintData = selectedRecord.getComplaint().getAsJsonObject();
        String verificationStatus = complaintData.get("v_status").getAsString();
        String livingStatus = complaintData.get("l_status").getAsString();
        String paymentStatus = complaintData.get("p_status").getAsString();
        int amount = 0;


        String clampStatus = complaintData.get("c_status").getAsString();
        String towingStatus = complaintData.get("t_status").getAsString();
        final String wronglyParkedVehicleNumber = selectedRecord.getWronglyParkedDetails().get("number").getAsString();
        //Menu Items
        MenuItem verifyItem = popupMenu.getMenu().getItem(0);
        MenuItem clampItem = popupMenu.getMenu().getItem(1);
        MenuItem towItem = popupMenu.getMenu().getItem(2);
        MenuItem payItem = popupMenu.getMenu().getItem(3);

        if (Boolean.valueOf(verificationStatus)) {
            boolean isPaymentOptionEnabled = false;
            //Verification is done.
            verifyItem.setVisible(false);
            if(Boolean.valueOf(clampStatus)){
                //Clamping is done
                clampItem.setVisible(false);
                isPaymentOptionEnabled = true;
                amount = 600;
            }
            if(Boolean.valueOf(towingStatus)){
                clampItem.setVisible(false);
                towItem.setVisible(false);
                isPaymentOptionEnabled = true;
                amount = +600;
            }

            if(!isPaymentOptionEnabled){
                payItem.setVisible(false);
            }

        } else {
            clampItem.setVisible(false);
            towItem.setVisible(false);
            payItem.setVisible(false);
        }
        
        final int amountToBePaid = amount;
        //IF COMPLAINT IS CLOSED...NO NEED TO SHOW POPUP MENU
        if((!TextUtils.isEmpty(livingStatus) && Boolean.valueOf(livingStatus)) && !Boolean.valueOf(paymentStatus) ){
            popupMenu.show();
        }
        

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.action_verify){
                    Intent launchIntent = new Intent(mContext, ComplaintVerification.class);
                    launchIntent.putExtra("COMPLAINT_RECORD",new Gson().toJson(selectedRecord));
                    ((Activity)mContext).startActivityForResult(launchIntent, 2);
                    return true;
                }else if(item.getItemId() == R.id.action_clamp){
                    Intent launchIntent = new Intent(mContext, Clamp.class);
                    launchIntent.putExtra("REFERENCE_ID", selectedRecord.getReferenceId());
                    launchIntent.putExtra("VEHICLE_NUMBER", wronglyParkedVehicleNumber);
                    ((Activity)mContext).startActivityForResult(launchIntent, 3);
                    return true;
                }else if(item.getItemId() == R.id.action_tow){
                    Intent launchIntent = new Intent(mContext, Tow.class);
                    launchIntent.putExtra("REFERENCE_ID", selectedRecord.getReferenceId());
                    launchIntent.putExtra("VEHICLE_NUMBER", wronglyParkedVehicleNumber);
                    ((Activity)mContext).startActivityForResult(launchIntent, 4);
                    return true;
                }else if(item.getItemId() == R.id.action_payment){
                    Intent launchIntent = new Intent(mContext, Payment.class);
                    launchIntent.putExtra("REFERENCE_ID", selectedRecord.getReferenceId());
                    launchIntent.putExtra("VEHICLE_NUMBER", wronglyParkedVehicleNumber);
                    launchIntent.putExtra("AMOUNT_TO_BE_PAID", amountToBePaid);
                    ((Activity)mContext).startActivityForResult(launchIntent, 5);
                    return true;
                }
                return false;
            }
        });
    }
*/
    private class ViewHolder {
        TextView referenecNumber;
        ImageView poupupImage;

        TextView buildingName;
        TextView clusterId;
        TextView bayNumber;
        TextView level;
        //TextView zone;
        //Date related
        TextView dateNumber;
        TextView dateMonth;//May 2015

        ImageView clampIcon;
        ImageView towIcon;
        ImageView paymentIcon;
        ImageView verifiedIcon;
        
        //Verification closed date
        TextView complaintClosedDate;

    }


}
