//package com.northeastern.numad.virtualtreasure;
//
//import android.os.AsyncTask;
//import android.util.Log;
//
//public class ServerConnection {
//	
//	public static int LOGIN_SUCCESSFUL = 0;
//	public static int LOGIN_FAILED = 1;
//	public static int UNKNOWN_USERNAME = 2;
//	public static int SERVER_UNAVAILABLE = 3;
//	public static int REQUEST_SUCCESSFUL = 4;
//	public static int REQUEST_UNSUCCESSFUL = 5;
//	
//	private static String TEAM_NAME = "teamAR";
//	private static String PASSWORD = "hello123";
//	
//	private static String KEY_DATA_IDS = "data_ids";
//	
//	private static Object mCounterlock = new Object();
//	private static int mThreadCounter = 0;
//	
//	public static int FetchTreasureData()
//	{
////		if (KeyValueAPI.isServerAvailable())
////		{
//			String dataIdListString = "1"; 
//			
//			// Check if treasure data list is null and we have some data available from server
//		    if ( (ApplicationData.mTreasureDataList == null) && (dataIdListString.length() > 0) )
//		    {
//		    	// Create new if null
//		    	ApplicationData.mTreasureDataList = new TreasureDataList();
//		    }
//		    
//		    // Break data ids into list
//			String[] dataIdList = dataIdListString.split(",");
//			
//			synchronized (mCounterlock)
//			{
//				for (String dataId : dataIdList)
//				{
//					// Fetch data only if NOT present in current list
//					if (!CheckIdInList(dataId))
//					{
//						// Fire multiple asynchronous calls to fetch data
//						// Simultaneous calling would make data population faster
//						HttpRequest request = new HttpRequest();
//				    	request.execute(dataId);
//				    	
//			    		mThreadCounter++;
//			    		Log.d("VirtualTreasure", "Fired new Http request. Counter : " + mThreadCounter);
//					}
//				}
//			
//				try {
//					mCounterlock.wait();
//					Log.d("VirtualTreasure", "Wait Finished");
//					return REQUEST_SUCCESSFUL;
//					
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					return REQUEST_UNSUCCESSFUL;
//				}
//			}
//		
//	}
//	
//	
//	
//	
//	public static int PutTreasureData(TreasureData treasureData)
//	{
//		
//		return -1;
//	}  
//	
//	// Checks if id already exists in the list
//	private static Boolean CheckIdInList(String dataId)
//	{
//		for(TreasureData treasureData: ApplicationData.mTreasureDataList)
//		{
//			if (0 == treasureData.getId().compareTo(dataId))
//			{
//				return true;
//			}
//		}
//		
//		return false;
//	}
//	
//	// Checks if id already exists in the list
//	private static Boolean CheckIdOnServerList(String id, String dataIdListString)
//	{
//		// Break data ids into list
//		String[] dataIdList = dataIdListString.split(",");
//		
//		for(String dataId: dataIdList)
//		{
//			if (0 == dataId.compareTo(id))
//			{
//				return true;
//			}
//		}
//		
//		return false;
//	}
//	
//	private static class HttpRequest extends AsyncTask<String, Object, Object>
//    {
//		@Override
//		protected Object doInBackground(String... dataId) {
//
//			String data = "Sample data";
//			// Log.d("VirtualTreasure", "Completed Http request. Counter : " + mThreadCounter);
//			
//			synchronized (ApplicationData.mTreasureDataList)
//			{	
//				// Check if data length is greater than 0
//				if (data.length() > 0)
//				{
//					// Add to list
//					ApplicationData.mTreasureDataList.add(new TreasureData(dataId[0], data));
//				}
//			}
//			
//			return null;
//		}
//		
//		@Override
//		protected void onPostExecute(Object object)
//		{
//			synchronized (mCounterlock) {
//				mThreadCounter -- ;
//				Log.d("VirtualTreasure", "Finished Http request. Counter : " + mThreadCounter);
//				
//				if (mThreadCounter == 0)
//				{
//					mCounterlock.notify();
//				}
//			}
//		}
//    }
//}
