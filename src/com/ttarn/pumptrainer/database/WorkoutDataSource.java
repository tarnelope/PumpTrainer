package com.ttarn.pumptrainer.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ttarn.pumptrainer.PumpTrainerApplication;


public class WorkoutDataSource {
	
	private WorkoutDbHelper mHelper;
	private SQLiteDatabase mDb;
	private String[] mAllColumns = {
			WorkoutDbHelper.COLUMN_NAME_WORKOUT_ID,
			WorkoutDbHelper.COLUMN_NAME_DATE,
			WorkoutDbHelper.COLUMN_NAME_HANG,
			WorkoutDbHelper.COLUMN_NAME_REST,
			WorkoutDbHelper.COLUMN_NAME_REP,
			WorkoutDbHelper.COLUMN_NAME_REC,
			WorkoutDbHelper.COLUMN_NAME_SETS,
			WorkoutDbHelper.COLUMN_NAME_NOTES
	};
	private boolean isDbOpen = false;
	
	public WorkoutDataSource(Context context) {
		mHelper = PumpTrainerApplication.get(context).getWorkoutDbHelper();
	}
	
	public void open() throws SQLException {
		mDb = mHelper.getWritableDatabase();
		isDbOpen = true;
	}
	
	public void close() {
		mHelper.close();
	}
	
	public Workout createWorkout(String date, String hang, String rest, String rep, String rec, String sets, String notes) {
		
		if (!isDbOpen) {
			open();
		}
		
		ContentValues values = new ContentValues();
		values.put(WorkoutDbHelper.COLUMN_NAME_DATE, date);
		values.put(WorkoutDbHelper.COLUMN_NAME_HANG, hang);
		values.put(WorkoutDbHelper.COLUMN_NAME_REST, rest);
		values.put(WorkoutDbHelper.COLUMN_NAME_REP, rep);
		values.put(WorkoutDbHelper.COLUMN_NAME_REC, rec);
		values.put(WorkoutDbHelper.COLUMN_NAME_SETS, sets);
		values.put(WorkoutDbHelper.COLUMN_NAME_NOTES, notes);
		
		long insertId = mDb.insert(WorkoutDbHelper.TABLE_NAME, null, values);
		
		Cursor cursor = mDb.query(WorkoutDbHelper.TABLE_NAME, mAllColumns, WorkoutDbHelper.COLUMN_NAME_WORKOUT_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		
		Workout newWo = cursorToWorkout(cursor);
		cursor.close();
		
		return newWo;
	}
	
	public void deleteWorkout(Workout w) {
		long id = w.getId();
		Log.d("Deleting..", "deleted is is " + String.valueOf(id));
		mDb.delete(WorkoutDbHelper.TABLE_NAME, WorkoutDbHelper.COLUMN_NAME_WORKOUT_ID + " = " + id,  null);
	}
	
	public ArrayList<Workout> getAllWorkouts() {
		ArrayList<Workout> workouts = new ArrayList<Workout>();
		
		Cursor c = mDb.query(WorkoutDbHelper.TABLE_NAME, mAllColumns, null, null, null, null, null);
		c.moveToFirst();
		
		while( !c.isAfterLast()) {
			Workout w = cursorToWorkout(c);
			workouts.add(w);
			c.moveToNext();
		}
		
		return workouts;
	}
	
	private Workout cursorToWorkout(Cursor c) {
		Workout wo = new Workout();
		wo.setId(c.getLong(0));
		wo.setDate(c.getString(1));
		wo.setHang(c.getInt(2));
		wo.setRest(c.getInt(3));
		wo.setRep(c.getInt(4));
		wo.setRecovery(c.getInt(5));
		wo.setCompletedSet(c.getInt(6));
		wo.setNotes(c.getString(7));
		return wo;
	}

}
