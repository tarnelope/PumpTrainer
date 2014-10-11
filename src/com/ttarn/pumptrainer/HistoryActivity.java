package com.ttarn.pumptrainer;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
	
	private Typeface chunkfive; 
	private Typeface kozoko;
	
	public HistoryActivity() {
		
	}
	
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.activity_history);
		
		mWDS = new WorkoutDataSource(this);
		mWDS.open();
		
		chunkfive = Fonts.getChunkfive(this);
		kozoko = Fonts.getKozopro(this);
		
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
	
	private static class ViewHolder {
		TextView dateVal;
		TextView hangVal;
		TextView restVal;
		TextView repVal;
		TextView recVal;
		TextView setsVal;
		TextView notesVal;
		
		ImageView btn;
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
			
			View v = cView;
			ViewHolder vh = null;
			
			if (v == null) {
				
				v = LayoutInflater.from(getContext()).inflate(R.layout.item_workout, parent, false);
				vh = new ViewHolder();
				
				vh.dateVal = (TextView) v.findViewById(R.id.wo_date);
				vh.hangVal = (TextView) v.findViewById(R.id.wo_hang_val);
				vh.restVal = (TextView) v.findViewById(R.id.wo_rest_val);
				vh.repVal = (TextView) v.findViewById(R.id.wo_rep_val);
				vh.recVal = (TextView) v.findViewById(R.id.wo_rec_val);
				vh.setsVal = (TextView) v.findViewById(R.id.wo_set_val);
				vh.notesVal = (TextView) v.findViewById(R.id.wo_notes);
				vh.btn = (ImageView) v.findViewById(R.id.delete_wo_btn);
				
				v.setTag(vh);
			} else {
				vh = (ViewHolder) v.getTag();
			}
			
			vh.dateVal.setTypeface(chunkfive);
			vh.dateVal.setText(wo.getDate());
			
			vh.hangVal.setTypeface(kozoko);
			vh.hangVal.setText(String.valueOf(wo.getHang()));
			
			vh.restVal.setTypeface(kozoko);
			vh.restVal.setText(String.valueOf(wo.getRest()));
			
			vh.repVal.setTypeface(kozoko);
			vh.repVal.setText(String.valueOf(wo.getRep()));
			
			vh.recVal.setTypeface(kozoko);
			vh.recVal.setText(String.valueOf(wo.getRecovery()));
			
			vh.setsVal.setTypeface(kozoko);
			vh.setsVal.setText(String.valueOf(wo.getCompletedSets()));
			
			vh.notesVal.setTypeface(kozoko);
			vh.notesVal.setText(wo.getNotes());
			
			vh.btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (getCount() > 0) {
						mWDS.deleteWorkout(wo);
						remove(wo);
					}
				}
			});
			
			return v;
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
