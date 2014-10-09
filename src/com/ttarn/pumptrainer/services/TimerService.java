package com.ttarn.pumptrainer.services;

import com.ttarn.pumptrainer.customviews.CircleProgressView;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class TimerService extends Service {
	
	private static int mStartCountDownTime = 3;
	private int mHangTime;
	private int mRestTime;
	private int mRepNum;
	private int mRecoveryTime;
	
	private Handler mHandler = new Handler();
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.d("TimerService", "TIMER SERVICE BOUND");
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	
	public void setWorkoutTimes(int hang, int rest, int rep, int recovery) {
		mHangTime = hang;
		mRestTime = rest;
		mRepNum = rep;
		mRecoveryTime = recovery;
	}
	
	/**
	 * 
	 * private void hangCountDown(final int hangTime) {
		
		mCurrentIndex = HANG_INDEX;
		
		mActionText.setText("GET IT!");
		mActionText.setTextSize(70);
		
		mTimeWheel.setType(CircleProgressView.ARC);
		
		if (isSoundLoaded) {
			mSoundPool.play(mHighBeepId, mVolume, mVolume, 1, 0, 1f);
		}
		
	    Runnable runnable = new Runnable() {
			private int hangTimeLeft = Math.round(hangTime * mSecondInterval);
			double slice = 100.00/mHangTime;
			double currentSlice = slice;
	        public void run() {
	            while (hangTimeLeft >= 0 && !isPaused) {  
	            	handler.post(new Runnable(){
	            		public void run() {
	            			if (isResuming) {
	            				currentSlice = mTimeWheel.getCurrentSubCurProgress();
	            				isResuming = false;
	            			}
	            			if (hangTimeLeft != 0) {
	            				mSoundPool.play(mHighBeepId, mVolume, mVolume, 1, 0, 1f);
	            			}
	            			mTimeWheel.setmSubCurProgress((float)currentSlice);	
	            			updateCountDown(hangTimeLeft, HANG_INDEX);
		                    hangTimeLeft -= 1000;
		                    currentSlice += slice;
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
	 */
	
	

}
