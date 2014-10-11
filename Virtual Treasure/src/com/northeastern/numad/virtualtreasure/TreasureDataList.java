package com.northeastern.numad.virtualtreasure;

import java.io.Serializable;
import java.util.ArrayList;

public class TreasureDataList extends ArrayList<TreasureData> implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<OnTreasureAddListener> mTreasureAddListenerList;
	private ArrayList<OnTreasureRemoveListener> mTreasureRemoveListenerList;
	
	public TreasureDataList()
	{
		mTreasureAddListenerList = new ArrayList<OnTreasureAddListener>();
		mTreasureRemoveListenerList = new ArrayList<OnTreasureRemoveListener>();
	}
	
	public TreasureData get(String dataId)
	{
		TreasureData treasureData = null;
		
		if (dataId != null)
		{
			for(TreasureData data: this)
			{
				if (0 == data.getId().compareTo(dataId))
				{
					treasureData = data;
					break;
				}
			}
		}
		
		return treasureData;
	}
	
	@Override
	public boolean add(TreasureData treasureData)
	{
		Boolean result = super.add(treasureData);
		
		// Notify listeners if added successfully
		if (result)
		{
			for(OnTreasureAddListener listener: mTreasureAddListenerList)
			{
				listener.OnTreasureAdd(treasureData);
			}
		}
		
		return result; 
	}
	
	@Override
	public boolean contains(Object treasureData)
	{
		return contains(((TreasureData) treasureData).getId());
	}
	
	public boolean contains(String dataId)
	{
		for(TreasureData data: this)
		{
			if (0 == data.getId().compareTo(dataId))
			{
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean remove(Object treasureData)
	{
		Boolean result = super.remove(treasureData);
		
		// Notify listeners if removed successfully
		if (result)
		{
			for(OnTreasureRemoveListener listener: mTreasureRemoveListenerList)
			{
				listener.OnTreasureRemove((TreasureData) treasureData);
			}
		}
		
		return result;
	}
	
	public void addOnTreasureAddListener(OnTreasureAddListener listener)
	{
		if (!this.mTreasureAddListenerList.contains(listener))
		{
			this.mTreasureAddListenerList.add(listener);
		}
	}
	
	public void removeOnTreasureAddListener(OnTreasureAddListener listener)
	{
		this.mTreasureAddListenerList.remove(listener);
	}
	
	public void addOnTreasureRemoveListener(OnTreasureRemoveListener listener)
	{ 
		if (!this.mTreasureRemoveListenerList.contains(listener))
		{
			this.mTreasureRemoveListenerList.add(listener);
		}
	}
	
	public void removeOnTreasureRemoveListener(OnTreasureRemoveListener listener)
	{ 
		this.mTreasureRemoveListenerList.remove(listener);
	}
	
	public interface OnTreasureAddListener
	{
		public void OnTreasureAdd(TreasureData treasureData);
	}
	
	public interface OnTreasureRemoveListener
	{
		public void OnTreasureRemove(TreasureData treasureData);
	}
}