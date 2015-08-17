package com.margaret.parking.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.margaret.parking.R;
import com.margaret.parking.db.DBOpenHelper;
import com.margaret.parking.pojo.ComplaintRecord;
import com.margaret.parking.pojo.CustomerDetails;
import com.margaret.parking.service.BuildService;
import com.margaret.parking.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ComplaintRegistration extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    LinearLayout mComplainerLayout, mReferenceLayout;
    TextView mComplaintDetailsHeading;
    Button mCreateButton, mCancelButton, mSearch;
    //Form fields
    EditText mBuildingName, mCluster, mLevel, mZone, mBayNumber;
    EditText mMake, mColor, mModel, mPlateDetails;
    EditText mComplainerName, mComplainerEmail, mComplainerContactNumber;
    private RefreshUI callback;
    private Spinner mCustomerNames;
    private HashMap<String, CustomerDetails> customerNames = new HashMap<String, CustomerDetails>();
    private String mobile;

    public interface RefreshUI {
        void onRefresh();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //NOTE: complaint verification and complaint registration uses the same layout file
        setContentView(R.layout.activity_complaint_verification);
        loadUIElements();
    }

    private void loadUIElements() {
        mSearch = (Button) findViewById(R.id.search);
        mCustomerNames = (Spinner) findViewById(R.id.customerNames);
        mCustomerNames.setOnItemSelectedListener(ComplaintRegistration.this);
        mComplainerLayout = (LinearLayout) findViewById(R.id.customerInfoLayout);
        mReferenceLayout = (LinearLayout) findViewById(R.id.referenceLayout);
        mComplaintDetailsHeading = (TextView) findViewById(R.id.complaintRaisedByHeading);
        mReferenceLayout.setVisibility(View.GONE);
        mComplaintDetailsHeading.setVisibility(View.VISIBLE);
        mComplainerLayout.setVisibility(View.VISIBLE);

        mCreateButton = (Button) findViewById(R.id.submitJobButton);
        mCancelButton = (Button) findViewById(R.id.closeJobButton);
        //FORM DATA
        mBuildingName = (EditText) findViewById(R.id.buildingName);
        mCluster = (EditText) findViewById(R.id.cluster);
        mLevel = (EditText) findViewById(R.id.level);
        mZone = (EditText) findViewById(R.id.zone);
        mBayNumber = (EditText) findViewById(R.id.bay);

        mMake = (EditText) findViewById(R.id.make);
        mColor = (EditText) findViewById(R.id.color);
        mModel = (EditText) findViewById(R.id.model);
        mPlateDetails = (EditText) findViewById(R.id.plateDetails);

        mComplainerName = (EditText) findViewById(R.id.complainerName);
        mComplainerEmail = (EditText) findViewById(R.id.complainerEmail);
        mComplainerContactNumber = (EditText) findViewById(R.id.complainerContact);

        mCreateButton.setText(R.string.create_job);
        mCancelButton.setText(R.string.cancel);

        //Register click listeners
        mCreateButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            // On selecting a spinner item
            String item = parent.getItemAtPosition(position).toString();
            CustomerDetails de = customerNames.get(item);

            mComplainerName.setText(de.getFirstName());
            mComplainerEmail.setText(de.getEmail());
            mComplainerContactNumber.setText(de.getMobile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    @Override
    public void onClick(View v) {
        if (v == mCancelButton) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (v == mSearch) {
            try {
                BuildService.build.getCustomersByFNameLName("", new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        try {
                            JSONArray names = new JSONArray(s);

                            if (names.length() > 0) {
                                CustomerDetails details;
                                ArrayList<String> ne = new ArrayList<>();
                                for (int i = 0; i < names.length(); i++) {
                                    details = new CustomerDetails();
                                    JSONObject name = names.getJSONObject(i);
                                    details.setFirstName(name.getString("firstName"));
                                    details.setLastName(name.getString("lastName"));
                                    details.setMobile(name.getString("mobileNo"));
                                    details.setEmail(name.getString("email"));
                                    details.setRoleId(name.getInt("roleId"));
                                    ne.add(name.getString("firstName"));
                                    customerNames.put(name.getString("firstName"), details);
                                }

                                mCustomerNames.setAdapter(new ArrayAdapter<String>(ComplaintRegistration.this,
                                        android.R.layout.simple_spinner_item, ne));


                            } else {
                                Utils.displayToad(ComplaintRegistration.this, getString(R.string.customer_name_error));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Log.d("CompliantRegistration :", "Exception raised in getCustomersByFNameLName service");
                        retrofitError.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (v == mCreateButton) {
            //validate all the fields here
            boolean isFiledFilled = validateAllFields();
            if (isFiledFilled) {


                //TODO: FETCH ALL DATA FROM FORM AND SAVE IT LOCALLY AND SEND TO SERVER
                String buildingName = getEditTextString(mBuildingName);
                String cluster = getEditTextString(mCluster);
                String level = getEditTextString(mLevel);
                String zone = getEditTextString(mZone);
                String bayNumber = getEditTextString(mBayNumber);
                String make = getEditTextString(mMake);
                String color = getEditTextString(mColor);
                String model = getEditTextString(mModel);
                String vehicleNumber = getEditTextString(mPlateDetails);
                String complainerName = getEditTextString(mComplainerName);
                String complainerEmail = getEditTextString(mComplainerEmail);
                String complainerContactNumber = getEditTextString(mComplainerContactNumber);
                //import com.google.gson.JsonObject;
                JsonObject complaint = new JsonObject();
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                Date date = new Date();
                complaint.addProperty("date", dateFormat.format(date));
                complaint.addProperty("v_status", "false");
                complaint.addProperty("c_status", "false");
                complaint.addProperty("t_status", "false");
                complaint.addProperty("p_status", "false");
                complaint.addProperty("v_date", "");
                complaint.addProperty("temp_Location", "");
                complaint.addProperty("comments", "");


                JsonObject location = new JsonObject();
                location.addProperty("name", buildingName);
                location.addProperty("cluster", cluster);
                location.addProperty("bay", bayNumber);
                location.addProperty("zone", zone);
                location.addProperty("level", level);

                JsonObject reportedBy = new JsonObject();
                reportedBy.addProperty("name", complainerName);
                reportedBy.addProperty("mail", complainerEmail);
                reportedBy.addProperty("contact", complainerContactNumber);

                JsonObject vehicle = new JsonObject();
                vehicle.addProperty("make", make);
                vehicle.addProperty("color", color);
                vehicle.addProperty("model", model);
                vehicle.addProperty("number", vehicleNumber);


                ComplaintRecord record = new ComplaintRecord("", String.valueOf(new Date().getTime()), complaint, location, reportedBy, vehicle);

                DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(this);
                long rowID = dbOpenHelper.insertNewComplaint(getApplicationContext(), record);
                //Toast.makeText(this, "row" + rowID, Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            } else {

            }

        }
    }

    private boolean validateAllFields() {
        boolean returnValue = true;
        if (mBuildingName.getText().toString().isEmpty()) {
            mBuildingName.setError("Building Name Can't be blank");
            returnValue = false;
        }
        if (mCluster.getText().toString().isEmpty()) {
            mCluster.setError("Cluster can't be blank");
            returnValue = false;
        }
        if (mLevel.getText().toString().isEmpty()) {
            mLevel.setError("Level can't be blank");
            returnValue = false;
        }

        if (mZone.getText().toString().isEmpty()) {
            mZone.setError("Zone can't be blank");
            returnValue = false;
        }

        if (mBayNumber.getText().toString().isEmpty()) {
            mBayNumber.setError("Bay Number can't be blank");
            returnValue = false;
        }

        if (mMake.getText().toString().isEmpty()) {
            mMake.setError("Make can't be blank");
        }

        if (mColor.getText().toString().isEmpty()) {
            mColor.setError("Color can't be blank");
        }

        if (mModel.getText().toString().isEmpty()) {
            mModel.setError("Model can't be blank");
        }

        if (mPlateDetails.getText().toString().isEmpty()) {
            mPlateDetails.setError("Vehicle number can't be blank");
        }

        if (mComplainerName.getText().toString().isEmpty()) {
            mComplainerName.setError("Name can't be blank");
        }

        if (mComplainerEmail.getText().toString().isEmpty()) {
            mComplainerEmail.setError("Email can't be blank");
        }

        if (mComplainerContactNumber.getText().toString().isEmpty()) {
            mComplainerContactNumber.setError("Contact number can't be blank");
        }

        return returnValue;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        //finish();
    }

    private String getEditTextString(EditText editText) {
        return editText.getText().toString();
    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_complaint_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    } */
}
