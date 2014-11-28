package com.mobilewallet.gcm;

import java.io.InputStream;
import java.net.URL;

import org.json.JSONObject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mobilewallet.R;
import com.mobilewallet.SplashScreen;
import com.mobilewallet.service.BuildService;
import com.mobilewallet.users.MyProfile;
import com.mobilewallet.utils.Utils;

public class GCMNotificationIntentService extends IntentService {
	public GCMNotificationIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {

			String gcm_id = extras.getString("registration_id");

			if (gcm_id != null && !"".equals(gcm_id.trim())) {
				new StoreGcmId(gcm_id, getApplicationContext()).update();
				Log.i("gcm id", gcm_id);

			} else {
				String userId = Utils.getUserId(getApplicationContext());

				if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType) && userId != null
						&& !"".equals(userId.trim())) {

					String jsonMsg = extras.get(Config.MESSAGE_KEY).toString();
					if (jsonMsg != null && !"".equals(jsonMsg.trim())) {

						sendNotification(jsonMsg);
					}
				}
			}

		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);

	}

	private void sendNotification(String jsonMsg) {

		try {
			Log.i("notification json", jsonMsg);
			final JSONObject obj = new JSONObject(jsonMsg);
			String notificationType = obj.getString("type");

			if ("J".equals(notificationType)) {

				final String imagePath = BuildService.URL + "/images/profile/"
						+ obj.getString("uf") + "/" + obj.getString("picId") + ".jpg";
				Log.i("image path", imagePath);

				new AsyncTask<Void, Void, Bitmap>() {

					@Override
					protected Bitmap doInBackground(Void... params) {
						// TODO Auto-generated method stub
						Bitmap bmp = null;
						InputStream inputStream = null;
						try {
							inputStream = (new URL(imagePath)).openConnection().getInputStream();
							bmp = BitmapFactory.decodeStream(inputStream);

						} catch (Exception e) {
							e.printStackTrace();
						}
						if (inputStream != null) {
							try {
								inputStream.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						return bmp;
					}

					@Override
					protected void onPostExecute(Bitmap result) {
						try {

							if (result == null) {
								result = BitmapFactory.decodeResource(getResources(),
										R.drawable.ic_launcher);
							}

							notifyJ(obj, result);

						} catch (Exception e) {
							e.printStackTrace();

						}

					}
				}.execute();

			} else if ("F".equals(notificationType)) {

				final String imagePath = BuildService.URL + "/images/profile/"
						+ obj.getString("uf") + "/" + obj.getString("picId") + ".jpg";

				Log.i("image path", imagePath);

				new AsyncTask<Void, Void, Bitmap>() {

					@Override
					protected Bitmap doInBackground(Void... params) {
						// TODO Auto-generated method stub
						Bitmap bmp = null;
						InputStream inputStream = null;
						try {
							inputStream = (new URL(imagePath)).openConnection().getInputStream();
							bmp = BitmapFactory.decodeStream(inputStream);

						} catch (Exception e) {
							e.printStackTrace();
						}
						if (inputStream != null) {
							try {
								inputStream.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						return bmp;
					}

					@Override
					protected void onPostExecute(Bitmap result) {
						try {

							if (result == null) {
								result = BitmapFactory.decodeResource(getResources(),
										R.drawable.ic_launcher);
							}

							notifyF(obj, result);

						} catch (Exception e) {
							e.printStackTrace();

						}

					}
				}.execute();

			} else if ("D".equals(notificationType)) {

				((NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE)).notify(
						obj.getInt("sid"),
						new NotificationCompat.Builder(this)
								.setContentTitle(obj.getString("title"))
								.setContentText(obj.getString("desc"))
								.setStyle(
										new NotificationCompat.BigTextStyle().bigText(obj
												.getString("desc")))
								.setSmallIcon(R.drawable.ic_launcher)
								.setContentIntent(
										PendingIntent.getActivity(
												this,
												obj.getInt("sid"),
												new Intent(this, MyProfile.class).putExtra("appId",
														obj.getString("appId")).addFlags(
														Intent.FLAG_ACTIVITY_CLEAR_TOP),
												PendingIntent.FLAG_CANCEL_CURRENT))
								.setDefaults(Notification.DEFAULT_ALL).setAutoCancel(true).build());

			} else if ("IN".equals(notificationType)) {

				((NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE)).notify(
						obj.getInt("sid"),
						new NotificationCompat.Builder(this)
								.setContentTitle(obj.getString("title"))
								.setContentText(obj.getString("desc"))
								.setStyle(
										new NotificationCompat.BigTextStyle().bigText(obj
												.getString("desc")))
								.setSmallIcon(R.drawable.ic_launcher)
								.setContentIntent(
										PendingIntent.getActivity(this, obj.getInt("sid"),
												new Intent(this, SplashScreen.class)
														.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
												PendingIntent.FLAG_CANCEL_CURRENT))
								.setDefaults(Notification.DEFAULT_ALL).setAutoCancel(true).build());

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void notifyF(JSONObject obj, Bitmap result) {
		try {
			((NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE)).notify(
					obj.getInt("sid"),
					new NotificationCompat.Builder(this)
							.setContentTitle(obj.getString("title"))
							.setContentText(obj.getString("desc"))
							.setStyle(
									new NotificationCompat.BigTextStyle().bigText(obj
											.getString("desc")))
							.setSmallIcon(R.drawable.ic_launcher)
							.setLargeIcon(result)
							.setContentIntent(
									PendingIntent.getActivity(
											this,
											obj.getInt("sid"),
											new Intent(this, MyProfile.class).putExtra("appId",
													obj.getString("appId")).addFlags(
													Intent.FLAG_ACTIVITY_CLEAR_TOP),
											PendingIntent.FLAG_CANCEL_CURRENT))
							.setDefaults(Notification.DEFAULT_ALL).setAutoCancel(true).build());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void notifyJ(JSONObject obj, Bitmap result) {
		try {
			((NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE)).notify(
					obj.getInt("sid"),
					new NotificationCompat.Builder(this)
							.setContentTitle(obj.getString("title"))
							.setContentText(obj.getString("desc"))
							.setStyle(
									new NotificationCompat.BigTextStyle().bigText(obj
											.getString("desc")))
							.setSmallIcon(R.drawable.ic_launcher)
							.setLargeIcon(result)
							.setContentIntent(
									PendingIntent.getActivity(
											this,
											obj.getInt("sid"),
											new Intent(this, MyProfile.class).putExtra("fid",
													obj.getString("fid")).addFlags(
													Intent.FLAG_ACTIVITY_CLEAR_TOP),
											PendingIntent.FLAG_CANCEL_CURRENT))
							.setDefaults(Notification.DEFAULT_ALL).setAutoCancel(true).build());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
