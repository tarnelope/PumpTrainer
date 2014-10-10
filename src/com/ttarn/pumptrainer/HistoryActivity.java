package com.ttarn.pumptrainer;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ttarn.pumptrainer.database.Workout;
import com.ttarn.pumptrainer.database.WorkoutDataSource;

public class HistoryActivity extends ListActivity {
	
	private WorkoutDataSource mWDS;
	
	public HistoryActivity() {
		
	}
	
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.activity_history);
		
		mWDS = new WorkoutDataSource(this);
		mWDS.open();
		
		ArrayList<Workout> workouts = mWDS.getAllWorkouts();
		
		WorkoutAdapter adapter = new WorkoutAdapter(this, workouts);
		setListAdapter(adapter);
	}
	
	@Override
	protected void onResume() {
		mWDS.open();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		mWDS.close();
		super.onPause();
	}
	
	private class WorkoutAdapter extends ArrayAdapter<Workout> {
		
		ArrayList<Workout> woAdapterList;
		
		public WorkoutAdapter(Context context, ArrayList<Workout> list) {
			super(context, 0, list);
			woAdapterList = list;
		}
		
		@Override
		public View getView(int position, View cView, ViewGroup parent) {
			final Workout wo = getItem(position);
			
			if (cView == null) {
				cView = LayoutInflater.from(getContext()).inflate(R.layout.item_workout, parent, false);
			}
			
			TextView dateVal = (TextView) cView.findViewById(R.id.wo_date);
			dateVal.setText(wo.getDate());
			
			TextView hangVal = (TextView) cView.findViewById(R.id.wo_hang_val);
			hangVal.setText(String.valueOf(wo.getHang()));
			
			TextView restVal = (TextView) cView.findViewById(R.id.wo_rest_val);
			restVal.setText(String.valueOf(wo.getRest()));
			
			TextView repVal = (TextView) cView.findViewById(R.id.wo_rep_val);
			repVal.setText(String.valueOf(wo.getRep()));
			
			TextView recVal = (TextView) cView.findViewById(R.id.wo_rec_val);
			recVal.setText(String.valueOf(wo.getRecovery()));
			
			TextView setVal = (TextView) cView.findViewById(R.id.wo_set_val);
			setVal.setText(String.valueOf(wo.getCompletedSets()));
			
			TextView notesVal = (TextView) cView.findViewById(R.id.wo_notes);
			notesVal.setText(wo.getNotes());
			
			ImageView btn = (ImageView) cView.findViewById(R.id.delete_wo_btn);
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (getCount() > 0) {
						mWDS.deleteWorkout(wo);
						remove(wo);
					}
				}
			});
			
			return cView;
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
		if (id == R.id.new_wo) {
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
