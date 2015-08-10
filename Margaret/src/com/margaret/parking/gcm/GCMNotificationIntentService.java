package com.margaret.parking.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMNotificationIntentService extends IntentService {
	public GCMNotificationIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {

			String gcm_id = extras.getString("registration_id");

			if (gcm_id != null && !"".equals(gcm_id.trim())) {
				new UpdateGcmId(gcm_id, getApplicationContext()).update();
				Log.i("gcm id", gcm_id);

			} else {

				if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
						.equals(messageType)) {
					sendNotification(extras.get(Config.MESSAGE_KEY).toString());
				}
			}

		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String jsonMsg) {

		/*JSONObject obj = null;
		String notificationType = null;
		try {
			Log.i("notification json", jsonMsg);
			obj = new JSONObject(jsonMsg);
			notificationType = obj.getString("type");
			if ("offer".equals(notificationType)) {

				String userId = Utils.getUserId(getApplicationContext());
				if (userId != null && !"".equals(userId.trim()))
					new OfferNotification(obj, this, userId).notifyOffer();

			} else if ("gift".equals(notificationType)) {
				((NotificationManager) this
						.getSystemService(Context.NOTIFICATION_SERVICE))
						.notify(obj.getInt("sid"),
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
														new Intent(
																this,
																GiftCardNotificationActivity.class)
																.addFlags(
																		Intent.FLAG_ACTIVITY_CLEAR_TOP)
																.putExtra(
																		"sid",
																		obj.getString("sid")),
														PendingIntent.FLAG_CANCEL_CURRENT))
										.setDefaults(Notification.DEFAULT_ALL)
										.setAutoCancel(true).build());
			} else if ("credit".equals(notificationType)
					|| "debit".equals(notificationType)) {
				((NotificationManager) this
						.getSystemService(Context.NOTIFICATION_SERVICE))
						.notify(obj.getInt("sid"),
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
														new Intent(
																this,
																BalanceNotificationActivity.class)
																.addFlags(
																		Intent.FLAG_ACTIVITY_CLEAR_TOP)
																.putExtra(
																		"json",
																		jsonMsg),
														PendingIntent.FLAG_CANCEL_CURRENT))
										.setDefaults(Notification.DEFAULT_ALL)
										.setAutoCancel(true).build());

			} else if ("url".equals(notificationType)) {
				((NotificationManager) this
						.getSystemService(Context.NOTIFICATION_SERVICE))
						.notify(obj.getInt("sid"),
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
														new Intent(
																this,
																UrlNotificationActivity.class)
																.addFlags(
																		Intent.FLAG_ACTIVITY_CLEAR_TOP)
																.putExtra(
																		"url",
																		obj.getString("url")),
														PendingIntent.FLAG_CANCEL_CURRENT))
										.setDefaults(Notification.DEFAULT_ALL)
										.setAutoCancel(true).build());

			} else if ("update".equals(notificationType)) {
				((NotificationManager) this
						.getSystemService(Context.NOTIFICATION_SERVICE))
						.notify(obj.getInt("sid"),
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
														new Intent(
																this,
																AppUpdateNotificationActivity.class)
																.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
														PendingIntent.FLAG_CANCEL_CURRENT))
										.setDefaults(Notification.DEFAULT_ALL)
										.setAutoCancel(true).build());
			} else if ("check".equals(notificationType)) {

				((NotificationManager) this
						.getSystemService(Context.NOTIFICATION_SERVICE))
						.notify(obj.getInt("sid"),
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
														new Intent(
																this,
																CheckNotificationActivity.class)
																.addFlags(
																		Intent.FLAG_ACTIVITY_CLEAR_TOP)
																.putExtra(
																		"sid",
																		obj.getString("sid"))
																.putExtra(
																		"package",
																		obj.getString("package"))
																.putExtra(
																		"esid",
																		obj.getString("esid")),
														PendingIntent.FLAG_CANCEL_CURRENT))
										.setDefaults(Notification.DEFAULT_ALL)
										.setAutoCancel(true).build());

			} else if ("daily".equals(notificationType)) {
				((NotificationManager) this
						.getSystemService(Context.NOTIFICATION_SERVICE))
						.notify(obj.getInt("sid"),
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
														new Intent(
																this,
																DailyLoginCreditActivity.class)
																.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
														PendingIntent.FLAG_CANCEL_CURRENT))
										.setDefaults(Notification.DEFAULT_ALL)
										.setAutoCancel(true).build());
			} else if ("click".equals(notificationType)) {
				String userId = Utils.getUserId(getApplicationContext());
				if (userId != null && !"".equals(userId.trim()))
					new ClickNotification(obj, this, userId).notifyClick();
			} else if ("reg".equals(notificationType)) {
				((NotificationManager) this
						.getSystemService(Context.NOTIFICATION_SERVICE))
						.notify(obj.getInt("sid"),
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
														new Intent(
																this,
																FinishRegistration.class)
																.putExtra(
																		"id",
																		obj.getString("id")),
														PendingIntent.FLAG_CANCEL_CURRENT))
										.setDefaults(Notification.DEFAULT_ALL)
										.setAutoCancel(true).build());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}*/

	}

}
