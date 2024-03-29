package com.margaret.parking.activity;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.margaret.parking.R;
import com.margaret.parking.adapters.ImageAdapter;
import com.margaret.parking.db.DBOpenHelper;
import com.margaret.parking.pojo.ClampRecord;
import com.margaret.parking.util.FileUtil;
import com.margaret.parking.util.Utils;

import retrofit.mime.TypedFile;

public class Clamp extends Activity implements View.OnClickListener {
	private static final int CAPTURE_PHOTO_CODE = 10;
	private static final int CAPTURE_AFTER_CLAMP = 11;
	GridView mPhotoGridView;
	GridView mAfterClampPhotoGrid;
	ImageView mCapturePhoto, mAfterClampPhoto;
	EditText referenceEditText, vehicleNumberEditText;
	List<Bitmap> beforeClampBitmapsList;
	List<Bitmap> afterClampBitmapsList;
	ArrayAdapter mBeforeClampAdapter;
	ArrayAdapter mAfterClampAdater;
	String vehicleNumber, referenceNumber;
	Button mSubmitButton, mCancelButton;
	DBOpenHelper dbOpenHelper;
	private TypedFile typedFile1, typedFile2, typedFile3, typedFile4;
	private static final String MIME_TYPE = "image/jpeg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_clamp);
			mPhotoGridView = (GridView) findViewById(R.id.photoGrid);
			// Capture vehicle and reference numbers
			referenceNumber = getIntent().getExtras().getString("REFERENCE_ID");
			vehicleNumber = getIntent().getExtras().getString("VEHICLE_NUMBER");
			dbOpenHelper = DBOpenHelper.getInstance(this);

			referenceEditText = (EditText) findViewById(R.id.referenceID);
			vehicleNumberEditText = (EditText) findViewById(R.id.wronglyParkedVNumber);
			mSubmitButton = (Button) findViewById(R.id.submitJobButton);
			mCancelButton = (Button) findViewById(R.id.closeJobButton);
			mSubmitButton.setOnClickListener(this);
			mCancelButton.setOnClickListener(this);
			referenceEditText.setText(referenceNumber);
			vehicleNumberEditText.setText(vehicleNumber);

			mAfterClampPhotoGrid = (GridView) findViewById(R.id.afterClampPhotoGrid);
			mCapturePhoto = (ImageView) findViewById(R.id.capturePhoto);
			mAfterClampPhoto = (ImageView) findViewById(R.id.clampPhoto);
			mCapturePhoto.setOnClickListener(this);
			mAfterClampPhoto.setOnClickListener(this);
			beforeClampBitmapsList = new ArrayList<>();
			afterClampBitmapsList = new ArrayList<>();

			if (savedInstanceState != null) {
				beforeClampBitmapsList = (List<Bitmap>) savedInstanceState
						.get("CLAMP_BEFORE");
				afterClampBitmapsList = (List<Bitmap>) savedInstanceState
						.get("CLAMP_AFTER");
			}
			mBeforeClampAdapter = new ImageAdapter(this,
					R.layout.photogrid_item, R.id.thumbnail,
					beforeClampBitmapsList);
			mAfterClampAdater = new ImageAdapter(this, R.layout.photogrid_item,
					R.id.thumbnail, afterClampBitmapsList);
			mPhotoGridView.setAdapter(mBeforeClampAdapter);
			mAfterClampPhotoGrid.setAdapter(mAfterClampAdater);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.menu_clamp, menu); return true; }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { // Handle
	 * action bar item clicks here. The action bar will // automatically handle
	 * clicks on the Home/Up button, so long // as you specify a parent activity
	 * in AndroidManifest.xml. int id = item.getItemId();
	 * 
	 * //noinspection SimplifiableIfStatement if (id == R.id.action_settings) {
	 * return true; }
	 * 
	 * return super.onOptionsItemSelected(item); }
	 */

	@Override
	public void onClick(View v) {
		if (v == mCapturePhoto) {
			if (getApplicationContext().getPackageManager().hasSystemFeature(
					PackageManager.FEATURE_CAMERA)) {
				Intent takePictureIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(takePictureIntent, CAPTURE_PHOTO_CODE);
			} else {
				Utils.displayToad(Clamp.this, "Camera not suported");
			}
		} else if (v == mAfterClampPhoto) {
			if (getApplicationContext().getPackageManager().hasSystemFeature(
					PackageManager.FEATURE_CAMERA)) {
				Intent takePictureIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(takePictureIntent, CAPTURE_AFTER_CLAMP);
			} else {
				Utils.displayToad(Clamp.this, "Camera not suported");
			}
		} else if (v == mSubmitButton) {
			final String date = Utils.getCurrentDate();
			// Save clamp details to DB and sync to server.
			new Thread(new Runnable() {
				@Override
				public void run() {
					long value = dbOpenHelper
							.updateComplaintClampDateAndStatus("true", date,
									referenceNumber, vehicleNumber);
				}
			}).start();

			for (Bitmap bitmap : beforeClampBitmapsList) {
				final ClampRecord record = new ClampRecord();
				record.setBeforeClampPhoto(bitmap);
				record.setClampDate(date);
				record.setReferenceId(referenceNumber);
				record.setPlateDetails(vehicleNumber);
				new Thread(new Runnable() {
					@Override
					public void run() {
						dbOpenHelper.insertClampData(record);
					}
				}).start();

			}

			for (Bitmap bitmap : afterClampBitmapsList) {
				final ClampRecord record = new ClampRecord();
				record.setAfterClampPhoto(bitmap);
				record.setClampDate(date);
				record.setReferenceId(referenceNumber);
				record.setPlateDetails(vehicleNumber);
				new Thread(new Runnable() {
					@Override
					public void run() {
						dbOpenHelper.insertClampData(record);
					}
				}).start();
			}

			setResult(RESULT_OK);
			finish();

		} else if (v == mCancelButton) {
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAPTURE_PHOTO_CODE) {
			if (data != null && data.getExtras() != null
					&& data.getExtras().get("data") != null) {

				Bundle extras = data.getExtras();
				Bitmap mImageBitmap = (Bitmap) extras.get("data");

				drawDate(mImageBitmap);
				beforeClampBitmapsList.add(mImageBitmap);
				mBeforeClampAdapter.notifyDataSetChanged();
			}
		} else if (requestCode == CAPTURE_AFTER_CLAMP) {
			if (data != null && data.getExtras() != null
					&& data.getExtras().get("data") != null) {
				Bundle extras = data.getExtras();
				Bitmap mImageBitmap = (Bitmap) extras.get("data");
				drawDate(mImageBitmap);
				afterClampBitmapsList.add(mImageBitmap);
				mAfterClampAdater.notifyDataSetChanged();
			}
		}

	}

	private TypedFile getTypedFile(Intent data) {
		TypedFile typedFile = null;
		try {
			typedFile = new TypedFile(MIME_TYPE, new File(FileUtil.getPath(
					Clamp.this, data.getData())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return typedFile;
	}

	private void drawDate(Bitmap mImageBitmap) {
		Canvas canvas = new Canvas(mImageBitmap); // bmp is the bitmap
		// to dwaw into
		Paint paint = new Paint();
		paint.setColor(Color.YELLOW);
		paint.setTextSize(10);
		paint.setTextAlign(Paint.Align.LEFT);
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
		String currentDateandTime = sdf.format(new Date());
		canvas.drawText(currentDateandTime, 10, 25, paint);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// Save captured Bitmap list.
		outState.putSerializable("CLAMP_BEFORE",
				(Serializable) beforeClampBitmapsList);
		outState.putSerializable("CLAMP_AFTER",
				(Serializable) afterClampBitmapsList);
		super.onSaveInstanceState(outState);
	}

}
