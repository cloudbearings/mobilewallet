package com.margaret.parking.service;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface MargaretService {
	@GET("/updateGcm")
	void gcm(@Query("id") String userId, @Query("gcmid") String gcmid,
			Callback<String> callback);

	@GET("/values/GetAllComplaints")
	void getAllComplaints(Callback<String> callback);

	@POST("/values/AuthenticateUser")
	void authenticateUser(@Query("username") String uname,
			@Query("password") String pwd, Callback<String> callback);

}
