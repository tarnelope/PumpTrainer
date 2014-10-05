package com.ttarn.pumptrainer;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ttarn.pumptrainer.ExerciseFragment.ExerciseCompleteListener;
import com.ttarn.pumptrainer.HomeFragment.TimeSetListener;

public class MainActivity extends Activity implements TimeSetListener, ExerciseCompleteListener {
	
	private static final int FRAGMENT_COUNT = 2;
	private static final int HOME_INDEX = 0;
	private static final int EXERCISE_INDEX = 1;
	private int mCurrentFrag;
	
	private int mHangTime;
	private int mRestTime;
	private int mRepNum;
	private int mRecoveryTime;
	private ArrayList<Integer> mCurrentWorkoutArray;
	
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.OnSharedPreferenceChangeListener mPrefListener;
	
	private Fragment[] mFrags = new Fragment[FRAGMENT_COUNT];
	
	private ExerciseFragment mExerciseFrag;
	private HomeFragment mHomeFrag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mSharedPreferences = getPreferences(MODE_PRIVATE);
		mPrefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
					String key) {
				Log.d("main activity", "pref changed");
			}
		};
		
		if (findViewById(R.id.container) != null) {
			if (savedInstanceState != null) {
				return;
			}
		}
		
		mHomeFrag = new HomeFragment();
		
		showFragment(mHomeFrag, true);
		
	}
	
	private void setupFragArray() {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
	
		mFrags[HOME_INDEX] = mHomeFrag;
		mFrags[EXERCISE_INDEX] = mExerciseFrag;
		for (int i = 0; i < FRAGMENT_COUNT; i++) {
			ft.hide(mFrags[i]);
		}
		ft.commit();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//showFragment(mCurrentFrag);
	};
	
	@Override
	protected void onStart() {
		super.onStart();
		mSharedPreferences.registerOnSharedPreferenceChangeListener(mPrefListener);
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt("currentFragment", mCurrentFrag);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void launchExercise(int hang, int rest, int rep, int recovery) {
		mHangTime = hang;
		mRestTime = rest;
		mRepNum = rep;
		mRecoveryTime = recovery;
		
		mCurrentWorkoutArray = new ArrayList<Integer>();
		mCurrentWorkoutArray.add(mHangTime);
		mCurrentWorkoutArray.add(mRestTime); 
		mCurrentWorkoutArray.add(mRepNum);
		mCurrentWorkoutArray.add(mRecoveryTime);
		
		Log.d("launchExercise", "Hang time in seconds is " + mHangTime);
		
		mExerciseFrag = new ExerciseFragment();
		mExerciseFrag.setWorkoutTimes(mHangTime, mRestTime, mRepNum, mRecoveryTime);
		mExerciseFrag.setTimesArray(mCurrentWorkoutArray);
		mFrags[EXERCISE_INDEX] = mExerciseFrag;
		
		//getFragmentManager().beginTransaction().hide(mHomeFrag).show(mExerciseFrag).commit();
		showFragment(mExerciseFrag, false);
	}
	
	private void showFragment(Fragment frag, boolean firstTime) {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		
		if (firstTime) {
			ft.add(R.id.container, frag);
		} else {
			ft.addToBackStack(null).replace(R.id.container, frag);
		}
		
		ft.commit();	
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mSharedPreferences.unregisterOnSharedPreferenceChangeListener(mPrefListener);
	}

	/**
	 * Purpose: To launch exercise complete fragment. There, users will have the option 
	 * to log their completed workout and comment on it.
	 */
	@Override
	public void launchExerciseComplete() {
		// TODO Auto-generated method stub
		
	}
}
