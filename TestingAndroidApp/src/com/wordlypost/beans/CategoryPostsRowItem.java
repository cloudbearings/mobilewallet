package com.wordlypost.beans;

import java.io.Serializable;

public class CategoryPostsRowItem implements Serializable {

	private static final long serialVersionUID = -3149280330134029311L;
	private String title, date, post_icon_url, author, content, post_banner, comment_count,
			post_url;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
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

	public String getComment_count() {
		return comment_count;
	}

	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}

	public String getPost_url() {
		return post_url;
	}

	public void setPost_url(String post_url) {
		this.post_url = post_url;
	}
}
