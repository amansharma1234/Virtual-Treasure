package com.northeastern.numad.virtualtreasure;

import com.northeastern.numad.virtualtreasure.TreasureData.DataType;

public class Treasure {

	private String mAuthor;
	private double mLat;
	private double mLong;
	private boolean isPrivate;
	private DataType type;
	private String message;
	private String id;
	private int drawableId;
	
	public String getmAuthor() {
		return mAuthor;
	}
	public void setmAuthor(String mAuthor) {
		this.mAuthor = mAuthor;
	}
	
	
	public double getmLat() {
		return mLat;
	}
	public void setmLat(double mLat) {
		this.mLat = mLat;
	}
	public double getmLong() {
		return mLong;
	}
	public void setmLong(double mLong) {
		this.mLong = mLong;
	}
	public boolean isPrivate() {
		return isPrivate;
	}
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	public DataType getType() {
		return type;
	}
	public void setType(DataType dataType) {
		this.type = dataType;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getDrawableId() {
		return drawableId;
	}
	public void setDrawableId(int drawableId) {
		this.drawableId = drawableId;
	}
	
	
	

}
