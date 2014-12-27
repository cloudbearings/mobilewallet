package com.mobilewallet.beans;

import java.io.Serializable;

public class QuestionViewModel implements Serializable {
	private static final long serialVersionUID = 339753860028828649L;
	private String question, answerA, answerB, answerC, answerD, answer,
			explanation, qt_type;
	private int qt_no;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswerA() {
		return answerA;
	}

	public void setAnswerA(String answerA) {
		this.answerA = answerA;
	}

	public String getAnswerB() {
		return answerB;
	}

	public void setAnswerB(String answerB) {
		this.answerB = answerB;
	}

	public String getAnswerC() {
		return answerC;
	}

	public void setAnswerC(String answerC) {
		this.answerC = answerC;
	}

	public String getAnswerD() {
		return answerD;
	}

	public void setAnswerD(String answerD) {
		this.answerD = answerD;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getQt_type() {
		return qt_type;
	}

	public void setQt_type(String qt_type) {
		this.qt_type = qt_type;
	}

	public int getQt_no() {
		return qt_no;
	}

	public void setQt_no(int qt_no) {
		this.qt_no = qt_no;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
