package com.mobilewallet.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mobilewallet.beans.QuestionViewModel;
import com.mobilewallet.persistance.DBAdapter;

public class QuestionsDAO {

	private SQLiteDatabase database;
	private DBAdapter dbHelper;
	private Context context;

	public QuestionsDAO(Context context) {
		this.context = context;
	}

	public QuestionsDAO opnToWrite() {
		dbHelper = new DBAdapter(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void Close() {
		database.close();
	}
	public long insertQuestion(int qt_no, String question, String answerA,
			String answerB, String answerC, String answerD, String answer,
			String explanation, String qt_type) {
		ContentValues cv = new ContentValues(12);
		cv.put(DBAdapter.QT_NO, qt_no);
		cv.put(DBAdapter.QUESTION, question);
		cv.put(DBAdapter.ANSWERA, answerA);
		cv.put(DBAdapter.ANSWERB, answerB);
		cv.put(DBAdapter.ANSWERC, answerC);
		cv.put(DBAdapter.ANSWERD, answerD);
		cv.put(DBAdapter.ANSWER, answer);
		cv.put(DBAdapter.EXPLANATION, explanation);
		cv.put(DBAdapter.QT_TYPE, qt_type);
		opnToWrite();
		long val = database.insert(DBAdapter.QT_TABLE_NAME, null, cv);
		Close();
		return val;
	}

	public long updateQuestion(int qt_no, String question, String answerA,
			String answerB, String answerC, String answerD, String answer,
			String explanation, String qt_type) {
		ContentValues cv = new ContentValues(12);
		cv.put(DBAdapter.QT_NO, qt_no);
		cv.put(DBAdapter.QUESTION, question);
		cv.put(DBAdapter.ANSWERA, answerA);
		cv.put(DBAdapter.ANSWERB, answerB);
		cv.put(DBAdapter.ANSWERC, answerC);
		cv.put(DBAdapter.ANSWERD, answerD);
		cv.put(DBAdapter.ANSWER, answer);
		cv.put(DBAdapter.EXPLANATION, explanation);
		cv.put(DBAdapter.QT_TYPE, qt_type);
		opnToWrite();
		long val = 0;
		if (isQuestionExists(qt_no) > 0) {
			val = database.update(DBAdapter.QT_TABLE_NAME, cv, DBAdapter.QT_NO
					+ "='" + qt_no + "'", null);
			Close();
		} else {
			val = database.insert(DBAdapter.QT_TABLE_NAME, null, cv);
			Close();
		}

		return val;
	}
	public long isQuestionExists(int qt_no) {
		SQLiteDatabase database = null;
		long count = 0;
		try {
			String sql = "SELECT COUNT(*) FROM " + DBAdapter.QT_TABLE_NAME
					+ " where " + DBAdapter.QT_NO + "='" + qt_no + "'";

			dbHelper = new DBAdapter(context);
			database = dbHelper.getReadableDatabase();
			SQLiteStatement statement = database.compileStatement(sql);
			count = statement.simpleQueryForLong();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				database.close();
			} catch (Exception e) {
			}
		}
		return count;
	}
	public long getQuestionsCount() {
		SQLiteDatabase database = null;
		Cursor c = null;
		long count = 0;
		try {
			String[] cols = { DBAdapter.QT_NO };

			dbHelper = new DBAdapter(context);
			database = dbHelper.getReadableDatabase();
			c = database.query(DBAdapter.QT_TABLE_NAME, cols, null, null, null, null, null);
			count = c.getCount();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (!c.isClosed()) {
					c.close();
				}
			} catch (Exception e) {
			}
			try {
				database.close();
			} catch (Exception e) {
			}
		}
		return count;
	}

	public List<QuestionViewModel> getquestions() {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		List<QuestionViewModel> questionsList = null;
		try {
			questionsList = new ArrayList<QuestionViewModel>();
			String[] cols = { DBAdapter.QT_NO, DBAdapter.QUESTION, DBAdapter.ANSWERA,
					DBAdapter.ANSWERB, DBAdapter.ANSWERC, DBAdapter.ANSWERD, DBAdapter.ANSWER,
					DBAdapter.EXPLANATION, DBAdapter.QT_TYPE};

			dbHelper = new DBAdapter(context);
			database = dbHelper.getReadableDatabase();
			cursor = database.query(DBAdapter.QT_TABLE_NAME, cols, null, null, null, null,
					DBAdapter.POSITION + " ASC");
			QuestionViewModel question;
			if (cursor.moveToFirst()) {
				do {
					question = new QuestionViewModel();
					question.setQt_no(cursor.getInt(cursor.getColumnIndex(DBAdapter.QT_NO)));
					question.setQuestion(cursor.getString(cursor.getColumnIndex(DBAdapter.QUESTION)));
					question.setAnswerA(cursor.getString(cursor.getColumnIndex(DBAdapter.ANSWERA)));
					question.setAnswerB(cursor.getString(cursor.getColumnIndex(DBAdapter.ANSWERB)));
					question.setAnswerC(cursor.getString(cursor.getColumnIndex(DBAdapter.ANSWERC)));
					question.setAnswerD(cursor.getString(cursor.getColumnIndex(DBAdapter.ANSWERD)));
					question.setAnswer(cursor.getString(cursor.getColumnIndex(DBAdapter.ANSWER)));
					question.setExplanation(cursor.getString(cursor.getColumnIndex(DBAdapter.EXPLANATION)));
					question.setQt_type(cursor.getString(cursor.getColumnIndex(DBAdapter.QT_TYPE)));					
					questionsList.add(question);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (!cursor.isClosed()) {
					cursor.close();
				}
			} catch (Exception e) {
			}
			try {
				database.close();
			} catch (Exception e) {
			}
		}
		return questionsList;
	}

	public QuestionViewModel getQuestion(String qt_no) {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		QuestionViewModel question = null;
		try {
			String[] cols = {DBAdapter.QT_NO, DBAdapter.QUESTION, DBAdapter.ANSWERA,
					DBAdapter.ANSWERB, DBAdapter.ANSWERC, DBAdapter.ANSWERD, DBAdapter.ANSWER,
					DBAdapter.EXPLANATION, DBAdapter.QT_TYPE};

			dbHelper = new DBAdapter(context);
			database = dbHelper.getReadableDatabase();
			cursor = database.query(DBAdapter.QT_TABLE_NAME, cols, DBAdapter.QT_NO + "='" + qt_no
					+ "'", null, null, null, null);
			if (cursor.moveToNext()) {
				question = new QuestionViewModel();
				question.setQt_no(cursor.getInt(cursor.getColumnIndex(DBAdapter.QT_NO)));
				question.setQuestion(cursor.getString(cursor.getColumnIndex(DBAdapter.QUESTION)));
				question.setAnswerA(cursor.getString(cursor.getColumnIndex(DBAdapter.ANSWERA)));
				question.setAnswerB(cursor.getString(cursor.getColumnIndex(DBAdapter.ANSWERB)));
				question.setAnswerC(cursor.getString(cursor.getColumnIndex(DBAdapter.ANSWERC)));
				question.setAnswerD(cursor.getString(cursor.getColumnIndex(DBAdapter.ANSWERD)));
				question.setAnswer(cursor.getString(cursor.getColumnIndex(DBAdapter.ANSWER)));
				question.setExplanation(cursor.getString(cursor.getColumnIndex(DBAdapter.EXPLANATION)));
				question.setQt_type(cursor.getString(cursor.getColumnIndex(DBAdapter.QT_TYPE)));		
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (!cursor.isClosed()) {
					cursor.close();
				}
			} catch (Exception e) {
			}
			try {
				database.close();
			} catch (Exception e) {
			}
		}
		return question;
	}
	public long deletePausedQuestions(String currentMilliseconds) {
		opnToWrite();
		long val = database.delete(DBAdapter.QT_TABLE_NAME, DBAdapter.CURRENT_MILLISECONDS + "!='"
				+ currentMilliseconds + "'", null);
		Close();
		return val;
	}
}
