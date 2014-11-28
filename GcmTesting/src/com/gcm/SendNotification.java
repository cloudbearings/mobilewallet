package com.gcm;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class SendNotification {

	private static final String Google_server_key = "AIzaSyACjYUSMZ6BphKPxY6zuX-xyDyAJ27aSug";
	private static final String message_key = "message";
	private static final String gcm_id = "APA91bFh9hrutl9f2Qf_i5wLFZO_x6_HvBuazsbkcVY5XLkBvMh5kwExwzJKvt9LLprCBMiKEiijW_axCszUjyaSRngnuLkRrFZ7VzOhtNTwCImhJnyGiXjlFHC8WvvaDbbSi-K5BK-raZ1iHc_bs9junwMPXbKVmA";

	private static final Sender sender = new Sender(Google_server_key);

	static String debit = "{\"type\":\"text\",\"sid\":2115,\"title\":\"Snap Deal\",\"desc\":\"register in snap deal to get RS.1\"}";

	public static void main(String[] args) throws Exception {
		Message message = new Message.Builder().addData(message_key, debit).build();
		Result result = sender.send(message, gcm_id, 2);
		System.out.println(result);
	}
}
