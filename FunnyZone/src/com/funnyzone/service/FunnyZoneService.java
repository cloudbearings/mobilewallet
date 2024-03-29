package com.funnyzone.service;

import retrofit.Callback;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Query;

public interface FunnyZoneService {
	@FormUrlEncoded
	@POST("/categories")
	void getCategories(Callback<String> callback);

	@POST("/posts")
	void getCategoriesPosts(@Query("category") String slug,
			Callback<String> callback);

	@POST("/submit_comment")
	void postComment(@Query("post_id") int post_id, @Query("name") String name,
			@Query("email") String email, @Query("content") String content,
			Callback<String> callback);

	@POST("/get_recent_posts")
	void recentPosts(@Query("page") long page, Callback<String> callback);

	@POST("/get_search_results")
	void searchPosts(@Query("search") String search, @Query("page") long page,
			Callback<String> callback);

	@POST("/get_tag_posts")
	void tagPosts(@Query("id") int tagId, @Query("slug") String slug,
			@Query("page") long page, Callback<String> callback);

	@POST("/get_category_posts")
	void getCategoryPosts(@Query("id") int categoryId,
			@Query("slug") String slug, @Query("count") int count,
			Callback<String> callback);

	@POST("/")
	void storeGcmId(@Query("regId") String regId, Callback<String> callback);
}
