package com.margaret.parking.service;

import org.json.JSONArray;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface MargaretService {
	@GET("/values/UpdateGCMId")
	void gcm(@Query("userid") String userid, @Query("GCMId") String GCMId,
			Callback<String> callback);

	@GET("/values/getallcomplaints")
	void getAllComplaints(@Query("pageindex") int pageindex,
			@Query("pagesize") int pagesize, Callback<String> callback);

	@POST("/values/AuthenticateUser")
	void authenticateUser(@Query("username") String uname,
			@Query("password") String pwd, Callback<String> callback);

	@GET("values/sendNewComplaints")
	void sendNewcomplaints(@Query("complaints") JSONArray uname,
			Callback<String> callback);

	@GET("values/GetAllComplaintsByTowings")
	void getAllComplaintsByTowings(Callback<String> callback);

	@GET("values/GetAllComplaintsByclamps")
	void getAllComplaintsByclamps(Callback<String> callback);

	@GET("values/getbyuserid")
	void getbyuserid(@Query("id") String id, Callback<String> callback);

	@GET("values/GetCustomersByFNameLName")
	void getCustomersByFNameLName(@Query("searchname") String searchname, Callback<String> callback);

}
