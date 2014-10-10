package com.ttarn.pumptrainer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WorkoutDbHelper extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "workout_db";
	
	public static final String TABLE_NAME = "workout";
	
	public static final String COLUMN_NAME_WORKOUT_ID = "workoutid";
	public static final String COLUMN_NAME_DATE = "date";
	public static final String COLUMN_NAME_HANG = "hang";
	public static final String COLUMN_NAME_REST = "rest";
	public static final String COLUMN_NAME_REP = "rep";
	public static final String COLUMN_NAME_REC = "rec";
	public static final String COLUMN_NAME_SETS= "sets";
	public static final String COLUMN_NAME_NOTES = "notes";

	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	
	private static final String SQL_CREATE_WORKOUT_TABLE =
	    "CREATE TABLE " + TABLE_NAME + " (" +
	    COLUMN_NAME_WORKOUT_ID + " INTEGER PRIMARY KEY," +
	    COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
	    COLUMN_NAME_HANG + TEXT_TYPE + COMMA_SEP +
	    COLUMN_NAME_REST + TEXT_TYPE + COMMA_SEP +
	    COLUMN_NAME_REP + TEXT_TYPE + COMMA_SEP +
	    COLUMN_NAME_REC + TEXT_TYPE + COMMA_SEP +
	    COLUMN_NAME_SETS + TEXT_TYPE + COMMA_SEP +
	    COLUMN_NAME_NOTES + TEXT_TYPE +
	    " )";

	private static final String SQL_DELETE_WORKOUT_TABLE =
	    "DROP TABLE IF EXISTS " + TABLE_NAME;

	public WorkoutDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_WORKOUT_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_WORKOUT_TABLE);
		onCreate(db);
	}

}
