package com.funnyzone.gcm;

public interface Config {
	String Url = "https://public-api.wordpress.com/rest/v1.1/sites/funnyzune.wordpress.com";
	String MESSAGE_KEY = "message";
	String GOOGLE_PROJECT_ID = "652502414746";
	String APP_VERSION = "appVersion";
	String GCM_ID = "gcmId";
	String ADD_LOADED_DATE = "add_loaded_date";
	String POST_LOADED_DATE = "post_loaded_date";
	String HM_POST_LOADED_DATE = "home_post_loaded_date";
	String CATEGORIES = Url + "/categories";
	String POSTS = Url + "/posts";
}
