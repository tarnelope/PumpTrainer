package com.ttarn.pumptrainer;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ttarn.pumptrainer.LogDialogFragment.LogDialogListener;
import com.ttarn.pumptrainer.customviews.CircleProgressView;

public class ExerciseActivity extends Activity implements LogDialogListener {
	
	private SoundPool mSoundPool;
	private int mLowBeepId;
	private int mHighBeepId;
	private int mPolishBeepId;
	private int mCurrentBeepId;
	private boolean isSoundLoaded;
	private AudioManager mAudioManager;
	private float mSetVolume;
	private float mMaxVolume;
	private float mVolume;
	
	private Typeface mTimeFont;
	
	private TextView mActionText;
	private TextView mTimeText;
	private TextView mRepText;
	private CircleProgressView mRestWheel;
	private CircleProgressView mTimeWheel;
	private ImageButton mPauseBtn;
	
	private boolean isPaused;
	private boolean isResuming;
	private boolean isSetFromLog;
	
	private static int START_COUNTDOWN_TIME = 3;
	private static int mHangTime;
	private static int mRestTime;
	private static int mRepNum;
	private int mCurrentRepNum;
	private static int mRecoveryTime;
	private int mCurrentActionTime;
	private int mSetsCompleted;
	
	private long mSecondInterval = 1000;
	
	private static int START_INDEX = 0;
	private static int HANG_INDEX = 1;
	private static int REST_INDEX = 2;
	private static int RECOVERY_INDEX = 3;
	private int mCurrentIndex;
	
	private String mRepRemaining;
	
	private final Handler handler = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise);
		
		enableSound();
		
		Intent receivedIntent = getIntent();
		int[] timesArray = receivedIntent.getIntArrayExtra("timeArray");
		for (int i = 0; i < timesArray.length; i++) {
			switch (i) {
			case 0:
				mHangTime = timesArray[i];
				break;
			case 1:
				mRestTime = timesArray[i];
				break;
			case 2:
				mRepNum = timesArray[i];
				mCurrentRepNum = mRepNum;
				break;
			case 3:
				mRecoveryTime = timesArray[i];
				break;
			}
		}
		
		isSetFromLog = receivedIntent.getBooleanExtra("new", false);
		isPaused = false;
		isResuming = false;
		
		mTimeFont = Fonts.getChunkfive(this);
		
		mRestWheel = (CircleProgressView) findViewById(R.id.rest_wheel);
    	mRestWheel.setVisibility(View.GONE);
    	
    	mTimeWheel = (CircleProgressView) findViewById(R.id.time_wheel);
    	
    	mActionText = (TextView) findViewById(R.id.action_text);
    	mActionText.setTypeface(mTimeFont);
    	
    	mTimeText = (TextView) findViewById(R.id.time_text);
    	mTimeText.setTypeface(mTimeFont);
    	
    	mRepRemaining = " of " + String.valueOf(mCurrentRepNum);
    	
    	mRepText = (TextView) findViewById(R.id.rep_text);
    	mRepText.setText(mCurrentRepNum + mRepRemaining);
    	
    	final ImageButton soundBtn = (ImageButton) findViewById(R.id.sound_btn);
    	soundBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isSoundLoaded) { //Pausing it
					soundBtn.setImageResource(R.drawable.sound_off);
					PumpTrainerApplication.get(getBaseContext()).resetSoundPool();
					isSoundLoaded = false;
				} else { //Unpausing it
					soundBtn.setImageResource(R.drawable.sound_on);
					enableSound();
				}
				
			}
		});
    	
    	mPauseBtn = (ImageButton) findViewById(R.id.pause_btn);
    	mPauseBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pauseCountDown();
			}
		});
    	
    	final ImageButton logBtn = (ImageButton) findViewById(R.id.save_btn);
    	logBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment log = LogDialogFragment.newInstance(mHangTime, mRestTime, mRepNum, mRecoveryTime, mSetsCompleted);
				if (!isPaused) pauseCountDown();
				log.show(getFragmentManager().beginTransaction(), "Save Pump");
			}
		});
	};
	
	@Override
	public void onResume() {
		super.onResume();
		if (!isSoundLoaded) {
			enableSound();
		}
		if (PumpTrainerApplication.get(this).isFirstTime() || isSetFromLog) {
			mSetsCompleted = 0;
			Thread.currentThread().interrupt();
			countdown(START_COUNTDOWN_TIME, START_INDEX);
			mCurrentBeepId = mPolishBeepId;
			playBeep();
		}
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
		switch (id) {
			case R.id.see_log:
				Intent startHistoryAct = new Intent(this, HistoryActivity.class);
				startActivity(startHistoryAct);
				break;
			case R.id.new_wo:
				Intent startMainAct = new Intent(this, MainActivity.class);
				startActivity(startMainAct);
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void enableSound() {
		mSoundPool = PumpTrainerApplication.get(this).getSoundPool();
		
		mPolishBeepId = mSoundPool.load(this, R.raw.polish_beep, 1);
		mHighBeepId = mSoundPool.load(this, R.raw.high_beep, 1);
		mLowBeepId = mSoundPool.load(this, R.raw.low_beep, 1);
	
		mAudioManager = (AudioManager) this.getSystemService(MainActivity.AUDIO_SERVICE);
	
		mSetVolume = (float) mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		mMaxVolume = (float) mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mVolume = mSetVolume/mMaxVolume;
		
		isSoundLoaded = true;
	}
	
	private void pauseCountDown() {
		isPaused = !isPaused;
		if (!isPaused) {
			mPauseBtn.setImageResource(R.drawable.pause);
			mActionText.setBackgroundColor(getResources().getColor(R.color.black_75));
			resumeTimer();
		} else {
			mPauseBtn.setImageResource(R.drawable.pause_off);
			mActionText.setTextSize(70);
			mActionText.setText("PAUSED");
			mActionText.setBackgroundColor(getResources().getColor(R.color.dark_tomato_97));
			
		}
	}
	
	public void resumeTimer() {
		isResuming = true;
		int remainingTime = Integer.parseInt(mTimeText.getText().toString());
		switch (mCurrentIndex) {
			case 0:
				countdown(remainingTime, START_INDEX);
				break;
			case 1:
				countdown(remainingTime, HANG_INDEX);
				break;
			case 2:
				countdown(remainingTime, REST_INDEX);
				break;
			case 3:
				countdown(remainingTime, RECOVERY_INDEX);
				break;
			
		}
	}
	
	private void updateCountDown(int time, int actionIndex) {
		if (time > 0) {
    		mTimeText.setText("" + ((time) / 1000));
    	} else if (actionIndex == START_INDEX ){
    		countdown(mHangTime, HANG_INDEX);
    	} else if (actionIndex == HANG_INDEX ) {
    		countdown(mRestTime, REST_INDEX);
    	} else if (actionIndex == REST_INDEX && mCurrentRepNum > 1) {
    		mCurrentRepNum--;
    		mRepText.setText(String.valueOf(mCurrentRepNum) + mRepRemaining);
  //  		mRestWheel.setVisibility(View.GONE);
    		countdown(mHangTime, HANG_INDEX);
    	} else if (actionIndex == REST_INDEX && mCurrentRepNum == 1){
    		mSetsCompleted++;
    		countdown(mRecoveryTime, RECOVERY_INDEX);
    	} else {
    		mCurrentRepNum = mRepNum;
    		mRepText.setText(String.valueOf(mCurrentRepNum) + mRepRemaining);
    		countdown(mHangTime, HANG_INDEX);
    	}
	}
	
	private void countdown(final int time, final int index) {
		
		
		
		mCurrentIndex = index;
		
		switch (index) {
			case 0: //Start
				
				PumpTrainerApplication.get(this).setFirstTime(false);
				isSetFromLog = false;
				
				mCurrentBeepId = mPolishBeepId;
				
				mActionText.setText("Workout begins in...");
				mActionText.setTextSize(30);
				
				playBeep();
				
				break;
			case 1: //Hang
			    mRestWheel.setVisibility(View.GONE);
				mActionText.setText("GET IT!");
				mActionText.setTextSize(70);
				mCurrentBeepId = mHighBeepId;
				playBeep();
				mCurrentActionTime = mHangTime;
				break;
			case 2: //Rest
				mCurrentBeepId = mLowBeepId;
				mRestWheel.setVisibility(View.VISIBLE);
				mActionText.setText("REST");
				mCurrentActionTime = mRestTime;
				break;
			case 3: //Recovery
				mActionText.setText("RECOVER");
				mActionText.setTextSize(50);
				mCurrentActionTime = mRecoveryTime;
				break;
		}
		
		mTimeWheel.setType(CircleProgressView.ARC);
		
	    Runnable runnable = new Runnable() {
			private int timeLeft = Math.round(time * mSecondInterval);
			double slice = 100.00/mCurrentActionTime;
			double currentSlice = slice;
	        public void run() {
	            while (timeLeft >= 0 && !isPaused) {  
	            	handler.post(new Runnable(){
	            		public void run() {
	            			if (isResuming) {
	            				currentSlice = mTimeWheel.getCurrentSubCurProgress();
	            				isResuming = false;
	            			}
	            			if (timeLeft != 0 && index != RECOVERY_INDEX) {
	            				playBeep();
	            			}
	            			if (index == HANG_INDEX || index == RECOVERY_INDEX) {
		            			mTimeWheel.setmSubCurProgress((float)currentSlice);	
		            			currentSlice += slice;
	            			}
	            			updateCountDown(timeLeft, mCurrentIndex);
		                    timeLeft -= 1000;
		                }
		            });
	            	try {
	                    Thread.sleep(1000);
	                }    
	                catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	               
	            }
	        }
	    };

	    new Thread(runnable).start();
	}
	
	private void playBeep() {
		if (isSoundLoaded) {
			mSoundPool.play(mCurrentBeepId, mVolume, mVolume, 1, 0, 1f);
		}
	}
	
	private void pauseSound() {
		PumpTrainerApplication.get(this).resetSoundPool();
		isSoundLoaded = false;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		pauseSound();
		isResuming = true;
	}

	@Override
	public void dialogDismissed() {
		pauseCountDown();
	}
	
}
