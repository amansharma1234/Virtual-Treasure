package com.northeastern.numad.virtualtreasure;

import android.graphics.Bitmap;

import com.facebook.model.GraphUser;
import com.northeastern.numad.virtualtreasure.TreasureData.DataType;

public class ApplicationData{

	public enum FilterType
	{
		All,
		Facebook,
		Twitter,
		URL,
		Message,
		Private,
		Custom
	}
	
	public static String UserName;
	public static String Password;
	
	public static GraphUser FBUser;
	public static Bitmap FBPicture;
	
	public static FilterType CurrentFilter = FilterType.All;
	public static TreasureDataList mTreasureDataList;
	
	public static Boolean IsFiltered(Treasure treasureData)
	{
		// Do not show by default
		boolean isPublic = false;
		
		// Check if data is private
		if (treasureData.isPrivate())
		{
			// Check if current UserName is also target user 
			isPublic = true;
		}
		else
		{
			isPublic = true;
		}
		
		if (CurrentFilter == FilterType.All)
		{
			return isPublic;
		}
		if (CurrentFilter == FilterType.Facebook)
		{
			return (isPublic & (treasureData.getType() == DataType.Facebook));
		}
		if (CurrentFilter == FilterType.Twitter)
		{
			return (isPublic &(treasureData.getType() == DataType.Twitter));
		}
		if (CurrentFilter == FilterType.URL)
		{
			return (isPublic &(treasureData.getType() == DataType.URL));
		}
		if (CurrentFilter == FilterType.Message)
		{
			return (isPublic &(treasureData.getType() == DataType.Message));
		}
		if (CurrentFilter == FilterType.Private)
		{
			return true;
		}
		
		// Return false by default
		return false;
	}
	
	public static Boolean IsFiltered(TreasureData treasureData)
	{
		// Do not show by default
		boolean isPublic = false;
		
		// Check if data is private
		if (treasureData.isPrivate())
		{
			// Check if current UserName is also target user 
			isPublic = treasureData.containsTargetedUser(UserName);
		}
		else
		{
			isPublic = true;
		}
		
		if (CurrentFilter == FilterType.All)
		{
			return isPublic;
		}
		if (CurrentFilter == FilterType.Facebook)
		{
			return (isPublic & (treasureData.getType() == DataType.Facebook));
		}
		if (CurrentFilter == FilterType.Twitter)
		{
			return (isPublic &(treasureData.getType() == DataType.Twitter));
		}
		if (CurrentFilter == FilterType.URL)
		{
			return (isPublic &(treasureData.getType() == DataType.URL));
		}
		if (CurrentFilter == FilterType.Message)
		{
			return (isPublic &(treasureData.getType() == DataType.Message));
		}
		if (CurrentFilter == FilterType.Private)
		{
			return treasureData.containsTargetedUser(UserName);
		}
		
		// Return false by default
		return false;
	}
}