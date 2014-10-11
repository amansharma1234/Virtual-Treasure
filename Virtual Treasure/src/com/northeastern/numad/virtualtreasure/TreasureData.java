package com.northeastern.numad.virtualtreasure;

import java.util.ArrayList;

import android.location.Location;
import android.util.Log;
import android.widget.ImageView;
import edu.neu.virtualtreasure.R;

public class TreasureData{
	
	private static String KEY_AUTHOR = "author";
	private static String KEY_LAT = "lat";
	private static String KEY_LON = "lon";
	private static String KEY_PRIVATE = "private";
	private static String KEY_TARGET = "target";
	private static String KEY_FACEBOOK = "facebook";
	private static String KEY_TWITTER = "twitter";
	private static String KEY_HTTP = "http";
	private static String KEY_MESSAGE = "message";
	
	public enum DataType
	{
		Unknown,
		Facebook,
		Twitter,
		URL,
		Message
	}
	
	private static double CAMERA_ANGLE_BY_2;
	private static int MAX_ID = 0;
	
	// Data Id
	private String mId = "";
	public Boolean isShowing;
	public Boolean isShowing() {
		return isShowing;
	}
	public void setShowing(Boolean isShowing) {
		this.isShowing = isShowing;
	}
	public String getId() {
		return mId;
	}
	public void setId(String mId) {
		this.mId = mId;
	}

	// Latitude
	private double mLatitude;
	public double getLatitude() {
		return mLatitude;
	}
	public void setLatitude(double mLatitude) {
		this.mLatitude = mLatitude;
	}

	// Longitude
	private double mLongitude;
	public double getLongitude() {
		return mLongitude;
	}
	public void setLongitude(double mLongitude) {
		this.mLongitude = mLongitude;
	}

	// Data Type
	private DataType mType;
	public DataType getType() {
		return mType;
	}
	public void setType(DataType mType) {
		this.mType = mType;
	}
	
	// Message
	private String mMessage;
	public String getMessage() {
		return mMessage;
	}
	public void setMessage(String mMessage) {
		this.mMessage = mMessage;
	}
	
	// IsPrivate
	private boolean mIsPrivate;
	public boolean isPrivate() {
		return mIsPrivate;
	}
	public void setPrivate(boolean isPrivate) {
		this.mIsPrivate = isPrivate;
	}
	
	String mAuthor;
	
	public String getAuthor() {
		return mAuthor;
	}
	public void setAuthor(String mAuthor) {
		this.mAuthor = mAuthor;
	}

	// Distance from current location
	private double mDistance;

	public double getDistance() {
		return mDistance;
	}
	
	private ArrayList<String> mTargetedUsersList;
	
	private double mRequiredAngle;
	private double mOldDisplacementAngle = 0;
	
	private ArrayList<OnTreasureShowListener> mListenerList;
	
	private ImageView ivData;
	
	public ImageView getImageView() {
		return ivData;
	}
	public void setImageView(ImageView ivData) {
		this.ivData = ivData;
	}

	public static void UpdateCameraAngle(double angle)
	{
		CAMERA_ANGLE_BY_2 = angle / 2.0f;
	}
	
	public TreasureData()
	{
		this.mListenerList = new ArrayList<OnTreasureShowListener>();
		this.mTargetedUsersList = new ArrayList<String>();
		isShowing = Boolean.valueOf(false);
		generateNewId();
	}
	
	public TreasureData(String id, String data)
	{
		this(id, data, null);
	}
	
	public void addTargetedUser(String users)
	{
		String []targetUsers = users.split("\n");
		
		for (String user: targetUsers)
		{
			this.mTargetedUsersList.add(user.toLowerCase().trim());
		}
	}
	
	public void removeTargetedUser(String user)
	{
		this.mTargetedUsersList.remove(user.toLowerCase().trim());
	}
	
	public boolean containsTargetedUser(String user)
	{
		boolean isContain = (0 == this.mAuthor.toLowerCase().compareTo(user.toLowerCase()));
		
		if (this.mIsPrivate)
		{
			isContain |= this.mTargetedUsersList.contains(user);
		}
		
		return isContain;
	}
	
	public TreasureData(String id, String data, OnTreasureShowListener listener)
	{
		// Call private constructor to assign id
		this();
		
		// Check if id is not null
		if (id != null)
		{
			// Assign id if not null
			this.mId = id;
			
			// Check current number in id
			int current = Integer.parseInt(id.replace("vt_", ""));
			
			if (MAX_ID <= current)
			{
				MAX_ID = current;
			}
		}
		
		// Assign data
		String[] splitData = data.split("&");
		
		for (int counter = 0; counter < splitData.length; counter++)
		{			
			if (splitData[counter].startsWith(KEY_AUTHOR))
			{
				this.mAuthor = splitData[counter].split("=")[1];
			}
			else if (splitData[counter].startsWith(KEY_LAT))
			{
				this.mLatitude = Double.parseDouble(splitData[counter].split("=")[1]);
			}
			else if (splitData[counter].startsWith(KEY_LON))
			{
				this.mLongitude = Double.parseDouble(splitData[counter].split("=")[1]);
			}
			else if (splitData[counter].startsWith(KEY_PRIVATE))
			{
				this.mIsPrivate = (Integer.parseInt(splitData[counter].split("=")[1]) == 1);
			}
			else if (splitData[counter].startsWith(KEY_TARGET))
			{
				String [] targets = splitData[counter].split("=")[1].split("\n");
				
				for (String target: targets)
				{
					this.mTargetedUsersList.add(target);
				}
			}
			else
			{
				String[] dataMessage = splitData[counter].split("//");
				
				if (dataMessage.length >= 2)
				{
					if (dataMessage[0].startsWith(KEY_FACEBOOK))
					{
						this.mType = DataType.Facebook;
					}
					else if (dataMessage[0].startsWith(KEY_TWITTER))
					{
						this.mType = DataType.Twitter;
					} 
					else if (dataMessage[0].startsWith(KEY_HTTP))
					{
						this.mType = DataType.URL;
						this.mMessage = splitData[counter];
					}
					else if (dataMessage[0].startsWith(KEY_MESSAGE))
					{
						this.mType = DataType.Message;
					}
					
					if (this.mType != DataType.URL)
					{
						this.mMessage = dataMessage[1];
					}
				}
			}
		}
		
		mRequiredAngle = 0.0f;
		
		// Update listener
		if (listener != null)
		{
			mListenerList.add(listener);
		}
	}
	
	public void generateNewId()
	{
		// If id is null, assign a new id
		MAX_ID++;
		
		this.mId = "vt_" + MAX_ID;
	}
	
	public String getRecipientsAsString()
	{
		String recipients = "";
		
		for (String recipient: this.mTargetedUsersList)
		{
			recipients += "\n" + recipient;
		}
		
		recipients = recipients.replaceFirst("\n", "");
		
		return recipients;
	}
	
	public void UpdateCurrentLocation(Location fromLocation)
	{
		Location toLocation = new Location(fromLocation);
		toLocation.setLatitude(mLatitude);
		toLocation.setLongitude(mLongitude);
		
		mRequiredAngle = fromLocation.bearingTo(toLocation);
		mDistance = fromLocation.distanceTo(toLocation);
		
		// If angle is less than 0,
		// add 360 degrees to it to make it positive
		// e.g. -90 would be converted to 270, which is same
		if (mRequiredAngle < 0)
		{
			mRequiredAngle += 360;
		}
	}
	
	public void UpdateCurrentViewAngle(double currentAngle)
	{
		double displacementAngle;
		
		displacementAngle = (currentAngle - mRequiredAngle) / CAMERA_ANGLE_BY_2;
		
		if ( (displacementAngle <  1.0f) && (displacementAngle > (-1.0f)) )
		{
			// Log.d("Virtual Treasure","Show Object with id # " + mId);
			
			// Check if displacement angle is calculated first time
			if (mOldDisplacementAngle == 0)
			{
				mOldDisplacementAngle = displacementAngle;
				for(OnTreasureShowListener listener: mListenerList)
				{
					listener.OnTreasureShow(this, displacementAngle);
				}
			}
			else
			{
				// Else compare new displacement angle with old one
				// Update if new angle is NOT near
				if (Math.abs(mOldDisplacementAngle - displacementAngle) > 0.02f)
				{
					// Update if big change
					mOldDisplacementAngle = displacementAngle;
					for(OnTreasureShowListener listener: mListenerList)
					{
						listener.OnTreasureShow(this, displacementAngle);
					}
				}
				else
				{
					Log.d("Virtual Treasure","No Change");		
				}
			}
		}
		else
		{
			// Log.d("Virtual Treasure","Hide Object with id # " + mId);
			for(OnTreasureShowListener listener: mListenerList)
			{
				listener.OnTreasureHide(this);
			}
			
			mOldDisplacementAngle = 0;
		}
	} 
	
	public int GetDrawableId()
	{
		if (this.mType== DataType.Facebook)
		{
			return R.drawable.img_facebook;
		}
		else if (this.mType == DataType.Twitter)
		{
			return R.drawable.img_twitter;
		} 
		else if (this.mType == DataType.URL)
		{
			return R.drawable.img_http;
		}
		else if (this.mType == DataType.Message)
		{
			return R.drawable.img_message;
		}
		
		return R.drawable.img_happy;
	}
	
	@Override
	public String toString()
	{
		String str = "";
		
		str += KEY_AUTHOR + "=" + this.mAuthor;
		str += "&" + KEY_LAT + "=" + this.mLatitude;
		str += "&" + KEY_LON + "=" + this.mLongitude;
		
		// Check if this is private message
		if (this.mIsPrivate)
		{
			str += "&" + KEY_PRIVATE + "=1";
			
			str += "&" + KEY_TARGET + "=";
			
			int flag = 0;
			
			for (String user: this.mTargetedUsersList)
			{
				if (flag > 0)
				{
					str += "\n";
				}
				else
				{
					flag++;
				}
				
				str += user;
			}
		}
		else
		{
			str += "&" + KEY_PRIVATE + "=0";
		}
		
		str += "&";
		
		if (this.mType == DataType.Facebook)
		{
			str += KEY_FACEBOOK;
		}
		else if (this.mType == DataType.Twitter)
		{
			str += KEY_TWITTER;
		}
		else if (this.mType == DataType.URL)
		{
			str += this.mMessage;
		}
		else if (this.mType == DataType.Message)
		{
			str += KEY_MESSAGE;
		}
		
		if (this.mType != DataType.URL)
		{
			str += "//" + this.mMessage;
		}
		
		return str;
	}
	
	public void addOnTreasureShowListener(OnTreasureShowListener listener)
	{
		if (!this.mListenerList.contains(listener))
		{
			this.mListenerList.add(listener);
		}
	}
	
	public void removeOnTreasureShowListener(OnTreasureShowListener listener)
	 { 
		this.mListenerList.remove(listener);
	}
	
	public interface OnTreasureShowListener {
		public void OnTreasureShow(TreasureData data, double relativePosition);
		public void OnTreasureHide(TreasureData data);
	}
}