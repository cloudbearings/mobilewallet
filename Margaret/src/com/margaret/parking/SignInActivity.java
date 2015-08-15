package com.margaret.parking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.margaret.parking.db.DBContract;
import com.margaret.parking.db.DBOpenHelper;
import com.margaret.parking.gcm.Config;
import com.margaret.parking.gcm.GcmRegistration;
import com.margaret.parking.pojo.ComplaintRecord;
import com.margaret.parking.service.BuildService;
import com.margaret.parking.util.PreferenceStore;
import com.margaret.parking.util.Utils;

import java.util.List;

/**
 * Created by varmu02 on 6/17/2015.
 */
public class SignInActivity extends Activity {
    private static final String TAG = "SignInActivity";
    EditText mUserName, mPassword;
    ProgressBar mProgressBar;
    boolean isSignInAllowed = true;
    private DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_sigin);

        mProgressBar = new ProgressBar(this);
        mProgressBar.setIndeterminate(false);

        mUserName = (EditText) findViewById(R.id.userName);
        mPassword = (EditText) findViewById(R.id.password);

        initDatabase();

        dbOpenHelper = DBOpenHelper
                .getInstance(SignInActivity.this);


        if (PreferenceStore.getLoggedInStatus(this)) {
            // Logged in already
            new GcmRegistration(SignInActivity.this).register();
            if (PreferenceStore.getOperatorLogged(this)) {
                final Intent intent = new Intent(this,
                        OperatorMainActivity.class);
                startActivity(intent);
                finish();
            } else {
                final Intent intent = new Intent(this,
                        CustomerMainActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }

    private void parseNewComplaints(List<ComplaintRecord> newComplaints) {


        Log.i("dsjngdgn ", "fdsgdkfg");
        if (newComplaints.size() > 0) {
            JSONArray complaints = new JSONArray();
            for (ComplaintRecord record : newComplaints) {
                Gson gson = new Gson();
                complaints.put(gson.toJson(record));
            }
            Log.i("new complaints ", complaints + "");
        }
    }

    public void signIn(View view) {
        mProgressBar.setVisibility(View.VISIBLE);
        if (mUserName.getText().toString().trim().isEmpty()) {
            mUserName.setError("Enter Username");
            isSignInAllowed = false;
        }

        if (mPassword.getText().toString().trim().isEmpty()) {
            mPassword.setError("Enter Password");
            isSignInAllowed = false;
        }

        BuildService.build.authenticateUser(mUserName.getText().toString(),
                mPassword.getText().toString(), new Callback<String>() {

                    @Override
                    public void success(String output, Response arg1) {
                        try {
                            Log.i("Logged In status :", output);
                            JSONObject obj = new JSONObject(output);
                            if (!obj.getBoolean("statusCode")) {
                                isSignInAllowed = false;
                            }
                        } catch (JSONException je) {
                            Log.d(TAG,
                                    "Exception raised in authenticateUser service"
                                            + je.getMessage());
                            je.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        retrofitError.printStackTrace();
                    }
                });

        if (isSignInAllowed) {
            final DownloadComplaintTask downloadTask = new DownloadComplaintTask();
            if (isOperator()) {
                // Download
                if (!PreferenceStore.getComplaintsDownloadStatus(this)) {
                    // downloadComplaints("complaints.json", true);
                    downloadTask.execute(Config.SERVER_URL + "values/getallcomplaints", String.valueOf(true));
                    // DownloadComplaints(String.valueOf(true));
                } else {
                    PreferenceStore.saveLoggedIn(SignInActivity.this, true);
                    PreferenceStore.saveOperatorLogged(SignInActivity.this,
                            true);
                    final Intent intent = new Intent(this,
                            OperatorMainActivity.class);
                    startActivity(intent);
                    mProgressBar.setVisibility(View.GONE);
                    finish();
                }

            } else {
                if (!PreferenceStore.getCustComplaintsDownloadStatus(this)) {
                    // downloadComplaints("my_complaints.json", false);
                    downloadTask.execute(Config.SERVER_URL + "values/getbyuserid?id=parking@concordiadubai.com",
                            String.valueOf(false));
                    //DownloadComplaints(String.valueOf(false));
                } else {
                    PreferenceStore.saveLoggedIn(SignInActivity.this, true);
                    PreferenceStore.saveOperatorLogged(SignInActivity.this,
                            false);
                    final Intent intent = new Intent(this,
                            CustomerMainActivity.class);
                    startActivity(intent);
                    mProgressBar.setVisibility(View.GONE);
                    finish();
                }
            }
        }

    }

    public void forgotPassword(View view) {
        final Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
        finish();
    }

    private void initDatabase() {
        DBOpenHelper.getInstance(this);
    }

    private void DownloadComplaints(final String param) {
        BuildService.build.getAllComplaints(new Callback<String>() {

            @Override
            public void success(String complaintsAsString, Response arg1) {
                try {
                    Log.i("getallcomplaints", complaintsAsString);
                    boolean isOperator = Boolean.valueOf(param);

                    JsonParser jsonParser = new JsonParser();
                    JsonArray complainsArray = (JsonArray) jsonParser
                            .parse(complaintsAsString);
                    for (int index = 0; index < complainsArray.size(); index++) {
                        Gson gson = new GsonBuilder().create();
                        final ComplaintRecord record = gson.fromJson(
                                complainsArray.get(index), ComplaintRecord.class);

                        if (isOperator) {
                            dbOpenHelper
                                    .insertNewComplaint(SignInActivity.this, record);
                        } else {
                            dbOpenHelper.insertNewCustComplaint(SignInActivity.this,
                                    record);
                        }
                    }

                    if (isOperator) {
                        PreferenceStore.setComplaintsDownloadStatus(
                                SignInActivity.this, true);
                        PreferenceStore.saveLoggedIn(SignInActivity.this, true);
                        PreferenceStore.saveOperatorLogged(SignInActivity.this, true);
                        final Intent intent = new Intent(SignInActivity.this,
                                OperatorMainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        PreferenceStore.setCustComplaintsDownloadStatus(
                                SignInActivity.this, true);
                        PreferenceStore.saveLoggedIn(SignInActivity.this, true);
                        PreferenceStore.saveOperatorLogged(SignInActivity.this, false);
                        final Intent intent = new Intent(SignInActivity.this,
                                CustomerMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception je) {
                    Log.d(TAG,
                            "Exception raised in getallcomplaints service"
                                    + je.getMessage());
                    je.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                retrofitError.printStackTrace();
            }
        });
    }

    private class DownloadComplaintTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String jsonName = params[0];
            boolean isOperator = Boolean.valueOf(params[1]);

            final DBOpenHelper dbOpenHelper = DBOpenHelper
                    .getInstance(SignInActivity.this);
            String complaintsAsString = Utils.loadJSONAsStringFromServer(
                    SignInActivity.this, jsonName);
            Log.i("complaintsAsString", complaintsAsString + "");

            JsonParser jsonParser = new JsonParser();
            JsonArray complainsArray = (JsonArray) jsonParser
                    .parse(complaintsAsString);
            for (int index = 0; index < complainsArray.size(); index++) {
                Gson gson = new GsonBuilder().create();
                final ComplaintRecord record = gson.fromJson(
                        complainsArray.get(index), ComplaintRecord.class);
                if (isOperator) {
                    dbOpenHelper
                            .insertNewComplaint(SignInActivity.this, record);
                } else {
                    dbOpenHelper.insertNewCustComplaint(SignInActivity.this,
                            record);
                }

            }

            return String.valueOf(isOperator);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            boolean isOperator = Boolean.valueOf(result);
            if (isOperator) {
                PreferenceStore.setComplaintsDownloadStatus(
                        SignInActivity.this, true);
                PreferenceStore.saveLoggedIn(SignInActivity.this, true);
                PreferenceStore.saveOperatorLogged(SignInActivity.this, true);
                final Intent intent = new Intent(SignInActivity.this,
                        OperatorMainActivity.class);
                startActivity(intent);
                finish();
            } else {
                PreferenceStore.setCustComplaintsDownloadStatus(
                        SignInActivity.this, true);
                PreferenceStore.saveLoggedIn(SignInActivity.this, true);
                PreferenceStore.saveOperatorLogged(SignInActivity.this, false);
                final Intent intent = new Intent(SignInActivity.this,
                        CustomerMainActivity.class);
                startActivity(intent);
                finish();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);

        }

    }

	/*
     * private void downloadComplaints(final String jsonName,final boolean
	 * isOperator) { final DBOpenHelper dbOpenHelper =
	 * DBOpenHelper.getInstance(this); final CustDBOpenHelper custdbOpenHeler =
	 * CustDBOpenHelper.getInstance(this); String complaintsAsString =
	 * Utils.loadJSONAsStringFromAssets(this, jsonName);
	 * 
	 * JsonParser jsonParser = new JsonParser(); JsonArray complainsArray =
	 * (JsonArray) jsonParser.parse(complaintsAsString); for (int index = 0;
	 * index < complainsArray.size(); index++) { Gson gson = new
	 * GsonBuilder().create(); final ComplaintRecord record =
	 * gson.fromJson(complainsArray.get(index), ComplaintRecord.class); new
	 * Thread(new Runnable() {
	 * 
	 * @Override public void run() { if(isOperator){
	 * dbOpenHelper.insertNewComplaint(SingnInActivity.this, record); }else{
	 * custdbOpenHeler.insertNewCustComplaint(SingnInActivity.this, record); }
	 * 
	 * } }).start();
	 * 
	 * }
	 * 
	 * if(isOperator){ PreferenceStore.setComplaintsDownloadStatus(this, true);
	 * PreferenceStore.saveLoggedIn(SingnInActivity.this, true);
	 * PreferenceStore.saveOperatorLogged(SingnInActivity.this, true); final
	 * Intent intent = new Intent(this,OperatorMainActivity.class);
	 * startActivity(intent); finish(); }else{
	 * PreferenceStore.setCustComplaintsDownloadStatus(this, true);
	 * PreferenceStore.saveLoggedIn(SingnInActivity.this, true);
	 * PreferenceStore.saveOperatorLogged(SingnInActivity.this, false); final
	 * Intent intent = new Intent(this,CustomerMainActivity.class);
	 * startActivity(intent); finish(); }
	 * 
	 * }
	 */

    private boolean isOperator() {
        final String username = mUserName.getText().toString();
        final String password = mPassword.getText().toString();
        if (username.equalsIgnoreCase("margaret")) {
            // //Operator
            return true;
        }
        return false;
    }
}
