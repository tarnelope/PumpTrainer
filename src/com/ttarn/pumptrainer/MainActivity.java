package com.ttarn.pumptrainer;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ttarn.pumptrainer.HomeFragment.TimeSetListener;

public class MainActivity extends Activity implements TimeSetListener {
	
	private AudioManager mAudioManager;
	
	private int[] mCurrentWorkoutArray;
	
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.OnSharedPreferenceChangeListener mPrefListener;
	
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
	
	public AudioManager getAudioManager() {
		if (mAudioManager == null) {
			mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		} 
		return mAudioManager;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	};
	
	@Override
	protected void onStart() {
		super.onStart();
		mSharedPreferences.registerOnSharedPreferenceChangeListener(mPrefListener);
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
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
		if (id == R.id.see_log) {
			Intent i = new Intent(this, HistoryActivity.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void launchExercise(int hang, int rest, int rep, int recovery) {
		
		mCurrentWorkoutArray = new int[4];
		mCurrentWorkoutArray[0] = hang;
		mCurrentWorkoutArray[1] = rest;
		mCurrentWorkoutArray[2] = rep;
		mCurrentWorkoutArray[3] = recovery;
	
		Intent exerciseIntent = new Intent(this, ExerciseActivity.class);
		exerciseIntent.putExtra("timeArray", mCurrentWorkoutArray);
		startActivity(exerciseIntent);
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
}
