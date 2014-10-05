package com.ttarn.pumptrainer;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ttarn.pumptrainer.customviews.SetTimeRowView;

public class HomeFragment extends Fragment {
	
	
	private SetTimeRowView mHangRow; 
	private SetTimeRowView mRestRow;
	private SetTimeRowView mRepRow; 
	private SetTimeRowView mRecoveryRow;
	
	private Button mStartButton;
	private TimeSetListener mTimeSetListener;
	
	public HomeFragment() {
		
	}
	
	public interface TimeSetListener {
		public void launchExercise(int hang, int rest, int rep, int recovery);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mTimeSetListener = (TimeSetListener) activity;
	};
	
	@Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
       
    }
    
    @Override
    public ViewGroup onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
    	RelativeLayout v = (RelativeLayout) inflater.inflate(R.layout.fragment_home, container, false);
		
    	mHangRow = (SetTimeRowView) v.findViewById(R.id.hang_time_row);
    	mRestRow = (SetTimeRowView) v.findViewById(R.id.rest_time_row);
    	mRepRow = (SetTimeRowView) v.findViewById(R.id.rep_time_row);
    	mRecoveryRow = (SetTimeRowView) v.findViewById(R.id.recovery_time_row);
		
    	mStartButton = (Button) v.findViewById(R.id.start_btn);
    	mStartButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		
				int hangTime = mHangRow.getMinutes() + mHangRow.getSeconds();
				int restTime = mRestRow.getMinutes() + mRestRow.getSeconds();
				int repNum = mRepRow.getSeconds();
				int recoveryTime = mRecoveryRow.getMinutes() + mRecoveryRow.getSeconds();
				
				mTimeSetListener.launchExercise(hangTime, restTime, repNum, recoveryTime);
				
			}
		});
    	
    	return v;
    }
}
