package com.ttarn.pumptrainer.database;


public class Workout {
	
	private long mId;
	private String mDate;
	private int mHangTime;
	private int mRestTime;
	private int mRepNum;
	private int mRecoveryTime;
	private int mSetsCompleted;
	private String mNotes;

	public void setId(long id) {
		mId = id;
	}
	
	public void setDate(String d) {
		mDate = d;
	}
	
	public void setHang(int hang) {
		mHangTime = hang;
	}
	
	public void setRest(int rest) {
		mRestTime = rest;
	}
	
	public void setRep(int rep) {
		mRepNum = rep;
	}
	
	public void setRecovery(int rec) {
		mRecoveryTime = rec;
	}
	
	public void setCompletedSet(int sets) {
		mSetsCompleted = sets;
	}
	
	public void setNotes(String notes) {
		mNotes = notes;
	}
	
	public long getId() {
		return mId;
	}
	
	public String getDate() {
		return mDate;
	}
	
	public int getHang() {
		return mHangTime;
	}
	
	public int getRest() {
		return mRestTime;
	}
	
	public int getRep() {
		return mRepNum;
	}
	
	public int getRecovery() {
		return mRecoveryTime;
	}
	
	public int getCompletedSets() {
		return mSetsCompleted;
	}
	
	public String getNotes() {
		return mNotes;
	}
}
