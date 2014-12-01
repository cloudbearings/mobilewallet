package com.mobilewallet.beans;

public class RechargeHistoryBean {

	private String tid;
	private String des;
	private float coins;
	private String date;
	private String status;

	public RechargeHistoryBean(String tid, String des, float coins, String date, String status) {
		this.tid = tid;
		this.des = des;
		this.coins = coins;
		this.date = date;
		this.status = status;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public float getCoins() {
		return coins;
	}

	public void setCoins(float coins) {
		this.coins = coins;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
