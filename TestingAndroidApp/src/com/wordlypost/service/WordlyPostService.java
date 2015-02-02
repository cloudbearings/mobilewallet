package com.wordlypost.service;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface WordlyPostService {
	@GET("/get_category_index")
	void getCategories(Callback<String> callback);

	@GET("/get_category_posts")
	void getCategoriesPosts(@Query("id") int categoryId, @Query("slug") String slug,
			@Query("page") long page, Callback<String> callback);

	@GET("/submit_comment")
	void postComment(@Query("post_id") int post_id, @Query("name") String name,
			@Query("email") String email, @Query("content") String content,
			Callback<String> callback);

	@GET("/get_recent_posts")
	void recentPosts(@Query("page") long page, Callback<String> callback);

	@GET("/get_search_results")
	void searchPosts(@Query("search") String search, @Query("page") long page,
			Callback<String> callback);

	@GET("/get_tag_posts")
	void tagPosts(@Query("id") int tagId, @Query("slug") String slug, @Query("page") long page,
			Callback<String> callback);

	@GET("/get_category_posts")
	void getCategoryPosts(@Query("id") int categoryId, @Query("slug") String slug,
			@Query("count") int count, Callback<String> callback);

	@GET("/")
	void storeGcmId(@Query("regId") String regId, Callback<String> callback);
}
