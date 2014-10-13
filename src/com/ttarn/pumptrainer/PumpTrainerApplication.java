package com.ttarn.pumptrainer;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;

import com.ttarn.pumptrainer.database.WorkoutDbHelper;

public class PumpTrainerApplication extends Application {
	
	private WorkoutDbHelper mHelper;
	private boolean isFirstTime;
	private SoundPool mSoundPool;
	
	public PumpTrainerApplication() {
		
	}
	
	public static PumpTrainerApplication get(Context context) {
		return (PumpTrainerApplication) context.getApplicationContext();
	}
	
	@Override
	public void onCreate() {
		mHelper = new WorkoutDbHelper(getApplicationContext());
		isFirstTime = true;
		mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	}
	
	public SQLiteDatabase getReadableDb() {
		return mHelper.getReadableDatabase();
	}
	
	public SQLiteDatabase getWritableDb() {
		return mHelper.getWritableDatabase();
	}
	
	public WorkoutDbHelper getWorkoutDbHelper() {
		return mHelper;
	}
	
	public void setFirstTime(boolean b) {
		isFirstTime = b;
	}
	
	public boolean isFirstTime() {
		return isFirstTime;
	}
	
	public SoundPool getSoundPool() {
		return mSoundPool;
	}
	
	public void resetSoundPool() {
		mSoundPool.release();
		mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	}
}
