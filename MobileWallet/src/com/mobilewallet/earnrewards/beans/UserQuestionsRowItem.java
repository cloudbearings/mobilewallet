package com.mobilewallet.earnrewards.beans;

public class UserQuestionsRowItem {

	private String question, ansewerA, ansewerB, ansewerC, ansewerD, ansewer,
			status;

	public UserQuestionsRowItem(String question, String ansewerA,
			String ansewerB, String ansewerC, String ansewerD, String ansewer,
			String status) {
		this.question = question;
		this.ansewerA = ansewerA;
		this.ansewerB = ansewerB;
		this.ansewerC = ansewerC;
		this.ansewerD = ansewerD;
		this.ansewer = ansewer;
		this.status = status;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnsewerA() {
		return ansewerA;
	}

	public void setAnsewerA(String ansewerA) {
		this.ansewerA = ansewerA;
	}

	public String getAnsewerB() {
		return ansewerB;
	}

	public void setAnsewerB(String ansewerB) {
		this.ansewerB = ansewerB;
	}

	public String getAnsewerC() {
		return ansewerC;
	}

	public void setAnsewerC(String ansewerC) {
		this.ansewerC = ansewerC;
	}

	public String getAnsewerD() {
		return ansewerD;
	}

	public void setAnsewerD(String ansewerD) {
		this.ansewerD = ansewerD;
	}

	public String getAnsewer() {
		return ansewer;
	}

	public void setAnsewer(String ansewer) {
		this.ansewer = ansewer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
