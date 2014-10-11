package com.ttarn.pumptrainer;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ttarn.pumptrainer.database.WorkoutDataSource;

public class LogDialogFragment extends DialogFragment {
	
	private static String mHangTime;
	private static String mRestTime;
	private static String mRepNum;
	private static String mRecoveryTime;
	private static String mSetsCompleted;
	
	private WorkoutDataSource mWorkoutDS;
	
	private EditText mNotesText;
	
	static LogDialogFragment newInstance(int hang, int rest, int rep, int recovery, int setsCompleted) {
		mHangTime = String.valueOf(hang);
		mRestTime = String.valueOf(rest);
		mRepNum = String.valueOf(rep);
		mRecoveryTime = String.valueOf(recovery);
		mSetsCompleted = String.valueOf(setsCompleted);
		
		return new LogDialogFragment();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWorkoutDS = new WorkoutDataSource(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialogfrag_log, container, false);
		
		Typeface valueFont = Fonts.getKozopro(getActivity());
		
		TextView hangText = (TextView) v.findViewById(R.id.dialog_hang_time);
		hangText.setTypeface(valueFont);
		hangText.setText(mHangTime);
		
		TextView restText = (TextView) v.findViewById(R.id.dialog_rest_time);
		restText.setTypeface(valueFont);
		restText.setText(mRestTime);
		
		TextView repText = (TextView) v.findViewById(R.id.dialog_rep_time);
		repText.setTypeface(valueFont);
		repText.setText(mRepNum);
		
		TextView recText = (TextView) v.findViewById(R.id.dialog_rec_time);
		recText.setTypeface(valueFont);
		recText.setText(mRecoveryTime);
		
		TextView setText = (TextView) v.findViewById(R.id.dialog_sets);
		setText.setTypeface(valueFont);
		setText.setText(mSetsCompleted);
		
		mNotesText = (EditText) v.findViewById(R.id.dialog_notes);
		mNotesText.setTypeface(valueFont);
		
		Button cancelBtn = (Button) v.findViewById(R.id.dismiss_btn);
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		Button saveBtn = (Button) v.findViewById(R.id.save_btn);
		saveBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
				String date = df.format(cal.getTime());
				
				String notesEntry = mNotesText.getText().toString();
				mWorkoutDS.createWorkout(date, mHangTime, mRestTime, mRepNum, mRecoveryTime, mSetsCompleted, notesEntry);
				
				dismiss();
				
				Toast.makeText(getActivity(), "Workout saved! Pump it up!",
						   Toast.LENGTH_LONG).show();
				
				Intent i = new Intent(getActivity(), HistoryActivity.class);
				getActivity().startActivity(i);
			}
		});
		
		getDialog().setTitle("Log Workout");
		
		return v;
	}
}
