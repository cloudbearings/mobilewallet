package com.wordlypost.beans;

import java.io.Serializable;

public class NavDrawerItem implements Serializable {

	private static final long serialVersionUID = -6590514305977522847L;
	private int id, post_count;
	private String slug, title;

	public NavDrawerItem() {
	}

	public NavDrawerItem(int id, int post_count, String slug, String title) {
		this.id = id;
		this.post_count = post_count;
		this.slug = slug;
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPost_count() {
		return post_count;
	}

	public void setPost_count(int post_count) {
		this.post_count = post_count;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
