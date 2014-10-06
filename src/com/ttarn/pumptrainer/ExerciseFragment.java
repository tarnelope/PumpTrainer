package com.ttarn.pumptrainer;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ttarn.pumptrainer.customviews.CircleProgressView;

public class ExerciseFragment extends Fragment {
	
	private Typeface mTimeFont;
	
	private TextView mActionText;
	private TextView mTimeText;
	private TextView mRepText;
	private CircleProgressView mRestWheel;
	private CircleProgressView mTimeWheel;
	
	private boolean isPaused;
	private boolean isResuming;
	
	private static int mStartCountDownTime = 3;
	private int mHangTime;
	private int mRestTime;
	private int mRepNum;
	private int mRecoveryTime;
	
	private long mSecondInterval = 1000;
	
	private static int START_INDEX = 0;
	private static int HANG_INDEX = 1;
	private static int REST_INDEX = 2;
	private int mCurrentIndex;
	
	private String mRepRemaining;
	
	private final Handler handler = new Handler();
	private Animation mRotateAnim;
	
	private ExerciseCompleteListener mListener;
	
	public interface ExerciseCompleteListener {
		public void launchExerciseComplete();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mRotateAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
		mRotateAnim.setDuration(mHangTime*mSecondInterval);
		mRotateAnim.setInterpolator(new LinearInterpolator());
		
		isPaused = false;
		isResuming = false;
		
		mTimeFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Chunkfive.otf");
	};
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
    	super.onCreateView(inflater, container, bundle);
    	View v = inflater.inflate(R.layout.fragment_exercise, container, false);
    	
    	mRestWheel = (CircleProgressView) v.findViewById(R.id.rest_wheel);
    	mRestWheel.setVisibility(View.GONE);
    	mRestWheel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pauseCountDown();
			}
		});
    	
    	mTimeWheel = (CircleProgressView) v.findViewById(R.id.time_wheel);
    	mTimeWheel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pauseCountDown();
			}
		});
    	
    	mActionText = (TextView) v.findViewById(R.id.action_text);
    	mActionText.setTypeface(mTimeFont);
    	
    	mTimeText = (TextView) v.findViewById(R.id.time_text);
    	mTimeText.setTypeface(mTimeFont);
    	
    	mRepRemaining = " of " + String.valueOf(mRepNum);
    	
    	mRepText = (TextView) v.findViewById(R.id.rep_text);
    	mRepText.setText(mRepNum + mRepRemaining);
    	
    	return v;
	}
	
	private void pauseCountDown() {
		Log.d("ExerciseFrag", "PAUSED");
		isPaused = !isPaused;
		if (!isPaused) {
			mActionText.setBackgroundColor(getActivity().getResources().getColor(R.color.black_75));
			resumeTimer();
		} else {
			mActionText.setText("PAUSED");
			mActionText.setBackgroundColor(getActivity().getResources().getColor(R.color.tomato_red_80));
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mListener = (ExerciseCompleteListener) activity;
	};
	
	@Override
	public void onResume() {
		super.onResume();
		mRepText.setText(Integer.toString(mRepNum));
		startCountDown(mStartCountDownTime);
		//runCountDown(mHangTime, HANG_INDEX);
	}
	
	public void resumeTimer() {
		isResuming = true;
		int remainingTime = Integer.parseInt(mTimeText.getText().toString());
		switch (mCurrentIndex) {
			case 0:
				startCountDown(remainingTime);
				break;
			case 1:
				hangCountDown(remainingTime);
				break;
			case 2:
				restCountDown(remainingTime);
				break;
		}
	}
	
	public void setWorkoutTimes(int hang, int rest, int rep, int recovery) {
		mHangTime = hang;
		mRestTime = rest;
		mRepNum = rep;
		mRecoveryTime = recovery;
	}
	
	private void startCountDown(final int time) {
		
		mCurrentIndex = START_INDEX;
		
		mActionText.setText("Workout begins in...");
		mActionText.setTextSize(30);
		
		Runnable r = new Runnable() {
	        private int cdTime = time*Math.round(mSecondInterval);
	        public void run() {
	            while (cdTime >= 0 && !isPaused) {  
	                handler.post(new Runnable(){
	                    public void run() {
	                       updateCountDown(cdTime, START_INDEX);
	                       cdTime -= 1000;
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
		new Thread(r).start();    
	}
	
	private void updateCountDown(int time, int actionIndex) {
		if (time > 0) {
    		mTimeText.setText("" + ((time) / 1000));
    	} else if (actionIndex == START_INDEX ){
    		hangCountDown(mHangTime);
    	} else if (actionIndex == HANG_INDEX ) {
    		restCountDown(mRestTime);
    	} else if (actionIndex == REST_INDEX && mRepNum > 0) {
    		mRepNum--;
    		mRepText.setText(String.valueOf(mRepNum) + mRepRemaining);
    		mRestWheel.setVisibility(View.GONE);
    		hangCountDown(mHangTime);
    	} else {
    		mListener.launchExerciseComplete();
    	}
	}
	
	private void hangCountDown(final int hangTime) {
		
		mCurrentIndex = HANG_INDEX;
		
		mActionText.setText("GET IT!");
		mActionText.setTextSize(70);
		
		mTimeWheel.setType(CircleProgressView.ARC);
		
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
	
	private void restCountDown(final int restTime) {
	
		mCurrentIndex = REST_INDEX;
		mRestWheel.setVisibility(View.VISIBLE);
		mActionText.setText("REST");
		
	    Runnable runnable = new Runnable() {
			private int timeLeft = Math.round(restTime * mSecondInterval);
	        public void run() {
	            while (timeLeft >= 0 && !isPaused) {  
	            	handler.post(new Runnable(){
	                    public void run() {
	                    	if (isResuming) {
	            				//currentSlice = mTimeWheel.getCurrentSubCurProgress();
	            				isResuming = false;
	            			}
	                       updateCountDown(timeLeft, REST_INDEX);
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
	
}
