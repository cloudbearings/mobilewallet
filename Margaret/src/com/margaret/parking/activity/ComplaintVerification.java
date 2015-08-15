package com.margaret.parking.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.margaret.parking.R;
import com.margaret.parking.db.DBOpenHelper;
import com.margaret.parking.pojo.ComplaintRecord;

public class ComplaintVerification extends Activity implements View.OnClickListener{
    Button mSubmitButton;
    Button mCloseJobButton;
    EditText mReferenceId, mBuildingName, mCluster, mBayNumber, mVehicleMake, mVehicleColor, mLevel , mZone;
    EditText mVehicleModel, mVehicleNumber;
    LinearLayout mCustomerInfoLayout;
    ComplaintRecord complaintRecord;
    String mTempLocation;
    String mComments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_verification);
        String record = getIntent().getStringExtra("COMPLAINT_RECORD");


        Gson gson = new Gson();
        complaintRecord = gson.fromJson(record, ComplaintRecord.class);

        loadUIElements();
        setValues();
    }

    private void loadUIElements(){
        mSubmitButton = (Button)findViewById(R.id.submitJobButton);
        mCloseJobButton = (Button)findViewById(R.id.closeJobButton);
        mCustomerInfoLayout = (LinearLayout) findViewById(R.id.customerInfoLayout);
        mCustomerInfoLayout.setVisibility(View.GONE);
        mReferenceId = (EditText) findViewById(R.id.referenceID);
        mBuildingName = (EditText)findViewById(R.id.buildingName);
        mCluster = (EditText) findViewById(R.id.cluster);
        mBayNumber = (EditText) findViewById(R.id.bay);
        mVehicleMake = (EditText) findViewById(R.id.make);
        mVehicleColor = (EditText) findViewById(R.id.color);
        mVehicleModel = (EditText) findViewById(R.id.model);
        mVehicleNumber = (EditText) findViewById(R.id.plateDetails);
        mLevel = (EditText) findViewById(R.id.level);
        mZone = (EditText) findViewById(R.id.zone);
        //Registering listeners.
        mSubmitButton.setOnClickListener(this);
        mCloseJobButton.setOnClickListener(this);
    }

    private void setValues(){
        if(complaintRecord != null){
            mReferenceId.setText(complaintRecord.getReferenceId());
            mBuildingName.setText(complaintRecord.getLocation().get("name").getAsString());
            mCluster.setText(complaintRecord.getLocation().get("cluster").getAsString());
            mBayNumber.setText(complaintRecord.getLocation().get("bay").getAsString());
            mLevel.setText(complaintRecord.getLocation().get("level").getAsString());
            mZone.setText(complaintRecord.getLocation().get("zone").getAsString());

            mVehicleMake.setText(complaintRecord.getWrongparkeddetails().get("make").getAsString());
            mVehicleColor.setText(complaintRecord.getWrongparkeddetails().get("color").getAsString());
            mVehicleModel.setText(complaintRecord.getWrongparkeddetails().get("model").getAsString());
            mVehicleNumber.setText(complaintRecord.getWrongparkeddetails().get("number").getAsString());

        }

    }

    private void createAndDisplayDialog(String title,final boolean isTempLocNeeded){
        //Display dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(ComplaintVerification.this);
        builder.setTitle(title);
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_validation, null);
        final EditText tempLocation = (EditText) view.findViewById(R.id.tempLocation);
        final EditText comments = (EditText) view.findViewById(R.id.comments);
        if(!isTempLocNeeded) {
            tempLocation.setVisibility(View.GONE);
        }
        builder.setView(view).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean isRequiredFieldsFilled = true;
                if (tempLocation.getVisibility() == View.VISIBLE) {
                    mTempLocation = tempLocation.getText().toString();
                    if (mTempLocation.isEmpty()) {
                        tempLocation.setError("Temporary location can't be blank");
                        //isRequiredFieldsFilled = false; // TODO: 7/7/2015  
                    }

                }
                mComments = comments.getText().toString();
                if (mComments.isEmpty()) {
                    comments.setError("Comments can't be blank");
                    //isRequiredFieldsFilled = false; // TODO: 7/7/2015  
                }

                if (isRequiredFieldsFilled) {
                    JsonObject location = new JsonObject();
                    location.addProperty("name", mBuildingName.getText().toString());
                    location.addProperty("cluster", mCluster.getText().toString());
                    location.addProperty("bay", mBayNumber.getText().toString());
                    location.addProperty("zone", mZone.getText().toString());
                    location.addProperty("level", mLevel.getText().toString());

                    JsonObject vehicle = new JsonObject();
                    vehicle.addProperty("make", mVehicleMake.getText().toString());
                    vehicle.addProperty("color", mVehicleColor.getText().toString());
                    vehicle.addProperty("model", mVehicleModel.getText().toString());
                    vehicle.addProperty("number", mVehicleNumber.getText().toString());


                    JsonObject complaint = new JsonObject();
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date();
                    complaint.addProperty("date", complaintRecord.getComplaints().get("date").getAsString());

                    complaint.addProperty("v_status", "true");
                    complaint.addProperty("c_status", "false");
                    complaint.addProperty("t_status", "false");
                    complaint.addProperty("p_status", "false");
                    if (!isTempLocNeeded) {
                        //closing the complaint
                        complaint.addProperty("l_status", "false");
                        //rejecting the complaint
                        complaint.addProperty("rejected", "true");
                    } else {
                        complaint.addProperty("rejected", "false");
                        complaint.addProperty("l_status", "true");
                    }

                    complaint.addProperty("v_date", dateFormat.format(date));
                    complaint.addProperty("temp_location", mTempLocation = (TextUtils.isEmpty(mTempLocation) ? "" : mTempLocation));
                    complaint.addProperty("comments", mComments);

                    complaintRecord.setComplaints(complaint);
                    complaintRecord.setLocation(location);
                    complaintRecord.setWrongparkeddetails(vehicle);

                    long status = DBOpenHelper.getInstance(ComplaintVerification.this).updateJobVerificationStatus(ComplaintVerification.this, complaintRecord);
                    //Toast.makeText(ComplaintVerification.this, "status" + status, Toast.LENGTH_LONG).show();

                    setResult(RESULT_OK);
                    finish();
                }

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        if(v == mSubmitButton){
            createAndDisplayDialog("ACCEPT", true);

        }else if(v == mCloseJobButton){
            createAndDisplayDialog("CLOSE JOB", false);
        }
    }
}
