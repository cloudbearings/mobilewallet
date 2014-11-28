package com.mobilewallet.service;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface MobileWalletService {

	@GET("/updateGCM")
	void storeGcmId(@Query("id") String userId, @Query("gcmid") String gcmid,
			Callback<String> callback);

	@GET("/freeplusservices/register_v2")
	void reg(@Query("id") String id, Callback<String> callback);

	@GET("/freeplusservices/verifysignup")
	void verifysignup(@Query("vid") String vid, @Query("otp") String otp, @Query("id") String id,
			Callback<String> callback);

	@GET("/freeplusservices/resend_temp")
	void resendVerCodeAtReg(@Query("vid") String vid, Callback<String> callback);

	@GET("/freeplusservices/finishRegistration")
	void finishRegistration(@Query("vid") String vid, @Query("id") String id,
			Callback<String> callback);

	@GET("/freeplusservices/fpp_addd__ref_incentive___")
	void referral(@Query("id") String id, Callback<String> callback);

	// registration

	@GET("/freeplusservices/login_fp__user_service")
	void login(@Query("email") String email, @Query("pwd") String password,
			Callback<String> callback);

	@GET("/freeplusservices/fpp_send_password___touser")
	void fp(@Query("email") String email, Callback<String> callback);

	@GET("/freeplusservices/fpp___offerss_")
	void offers(@Query("id") String userId, Callback<String> callback);

	@GET("/freeplusservices/offerr_dts_service__")
	void offerDetails(@Query("id") String userId, @Query("eid") String encryptedOfferId,
			@Query("pos") int pos, @Query("d") String direction, Callback<String> callback);

	@GET("/freeplusservices/check_oofferr_sts_serv")
	void checkOferStatus(@Query("id") String userId, @Query("eid") String encryptedOfferId,
			Callback<String> callback);

	@GET("/freeplusservices/userprofile")
	void userProfile(@Query("id") String userId, Callback<String> callback);

	@GET("/freeplusservices/updateProfileV2")
	void updateuserProfile(@Query("id") String userId, @Query("name") String name,
			@Query("dob") String dob, @Query("gender") String gender,
			@Query("occ") String occupation, @Query("income") String income,
			Callback<String> callback);

	@GET("/freeplusservices/feed_back__me_")
	void feedback(@Query("id") String userId, @Query("feedType") String feedType,
			@Query("text") String text, @Query("email") String email, Callback<String> callback);

	@GET("/freeplusservices/fp__credit___his_toryyy")
	void credit(@Query("id") String userId, @Query("b") long b, Callback<String> callback);

	@GET("/freeplusservices/fp_debit___his____story")
	void debit(@Query("id") String userId, @Query("b") long b, Callback<String> callback);

	@GET("/freeplusservices/fp__recharge_srv___")
	void recharge(@Query("id") String userId, @Query("provider") String provider,
			@Query("service") String service, @Query("circle") String circle,
			@Query("amt") String amount, @Query("imei") String imei,
			@Query("isemulator") boolean emulator, @Query("type") String type,
			@Query("special") String isSpecialRecharge, Callback<String> callback);

	@GET("/freeplusservices/fp__update___gcm")
	void gcm(@Query("id") String userId, @Query("gcmid") String gcmid, Callback<String> callback);

	@GET("/rest/browsePlans")
	void browsePlans(@Query("operator") String operator, @Query("circle") String circle,
			@Query("plan") String plan, Callback<String> callback);

	@GET("/freeplusservices/findOPV2")
	void findOP(@Query("id") String userId, @Query("startsWith") String startsWith,
			Callback<String> callback);

	@GET("/freeplusservices/fp_balance_serVice")
	void balance(@Query("id") String userId, Callback<String> callback);

	@GET("/freeplusservices/get_notifications_offf_user")
	void notification(@Query("id") String userId, Callback<String> callback);

	@GET("/freeplusservices/notification___updatee")
	void notification(@Query("id") String userId, @Query("status") String status,
			@Query("type") String type, Callback<String> callback);

	@GET("/freeplusservices/get__OffER_Service")
	void getOfferNotification(@Query("id") String userId, @Query("eid") String encryptedOfferId,
			Callback<String> callback);

	@GET("/dailyLoginCredits.jsp")
	void dailyCredits(@Query("id") String userId, @Query("imei") String imei,
			@Query("gcmid") String gcmid, Callback<String> callback);

	@GET("/checkAppCredit.jsp")
	void checkNotification(@Query("id") String id, Callback<String> callback);

	@GET("/freeplusservices/showInvitation")
	void showInvitation(@Query("id") String userId, Callback<String> callback);

	@GET("/freeplusservices/offerinstalled")
	void installedOffers(@Query("id") String encryptedId, Callback<String> callback);

	@GET("/referralAmount.jsp")
	void referral_amount(Callback<String> callback);

	@GET("/operators.jsp")
	void operators(Callback<String> callback);

	@GET("/circles.jsp")
	void circles(@Query("oid") String oId, Callback<String> callback);

	@GET("/appStatus.jsp")
	void appStatus(@Query("id") String encryptedId, Callback<String> callback);

	@GET("/opAndCircle.jsp")
	void operatorAndCircles(Callback<String> callback);

	@GET("/ocAvailable.jsp")
	void ocAvailable(@Query("oid") String oid, @Query("cid") String cid, Callback<String> callback);

	@GET("/freeplusservices/checkMobile")
	void isMobileExists(@Query("id") String userId, Callback<String> callback);

	@GET("/freeplusservices/changeMobile")
	void changeMobileNumber(@Query("id") String userId, Callback<String> callback);

	// in recharge page
	@GET("/freeplusservices/updateMobile")
	void updateMobileNumber(@Query("id") String userId, Callback<String> callback);

	@GET("/freeplusservices/verifyupdatemobile")
	void verifyupdatemobile(@Query("id") String userId, @Query("vid") String vid,
			@Query("otp") String otp, @Query("imei") String imei, Callback<String> callback);

	@GET("/freeplusservices/resend_old")
	void resend_old(@Query("id") String userId, @Query("vid") String vid, Callback<String> callback);

	// in recharge page

	@GET("/freeplusservices/resendvid_change")
	void resend_vcode_change(@Query("id") String userId, @Query("vid") String vid,
			Callback<String> callback);

	@GET("/freeplusservices/verifyChangeMobile")
	void verify_change_mobile(@Query("id") String userId, @Query("vid") String vid,
			@Query("otp") String otp, @Query("imei") String imei, Callback<String> callback);
}
