package com.wordlypost.beans;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

public class PostRowItem implements Serializable {

	private static final long serialVersionUID = -3149280330134029311L;
	private int post_id, comment_count;

	private String title, date, post_icon_url, author, content, post_banner, post_url;

	private String post_des, commentsArray, tagsArray;

	public int getPost_id() {
		return post_id;
	}

	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return changeDateFormat(date);
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPost_icon_url() {
		return post_icon_url;
	}

	public void setPost_icon_url(String post_icon_url) {
		this.post_icon_url = post_icon_url;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		if (content.contains("<img")) {
			content = content.replace("<img", "<img1");
		}
		if (content.contains("<iframe")) {
			content = content.replace("<iframe", "<iframe1");
		}
		if (content.contains("width")) {
			content = content.replace("width", "with");
		}
		if (content.contains("height")) {
			content = content.replace("height", "high");
		}
		if (content.contains("justify")) {
			content = content.replace("justify", "");
		}
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPost_banner() {
		return post_banner;
	}

	public void setPost_banner(String post_banner) {
		this.post_banner = post_banner;
	}

	public int getComment_count() {
		return comment_count;
	}

	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}

	public String getPost_url() {
		return post_url;
	}

	public void setPost_url(String post_url) {
		this.post_url = post_url;
	}

	public String getPost_des() {
		return post_des;
	}

	public void setPost_des(String post_des) {
		this.post_des = post_des;
	}

	public String getCommentsArray() {
		return commentsArray;
	}

	public void setCommentsArray(String commentsArray) {
		this.commentsArray = commentsArray;
	}

	public String getTagsArray() {
		return tagsArray;
	}

	public void setTagsArray(String tagsArray) {
		this.tagsArray = tagsArray;
	}

	private String changeDateFormat(String oldDateString) {

		String OLD_FORMAT = "yyyy-MM-dd HH:mm:ss";
		String NEW_FORMAT = "MMM dd, yyyy hh:mm aaa";

		// August 12, 2010
		String newDateString = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, Locale.ENGLISH);
			Date d = sdf.parse(oldDateString.trim());

			sdf.applyPattern(NEW_FORMAT);
			newDateString = sdf.format(d);
		} catch (ParseException e) {
			Log.i("TAG :", "Date parse exception");
			e.printStackTrace();
		}
		return newDateString;
	}
}
