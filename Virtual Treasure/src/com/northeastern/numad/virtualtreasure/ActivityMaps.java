

package com.northeastern.numad.virtualtreasure;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.OverlayItem;
import com.google.gson.Gson;
import com.northeastern.numad.virtualtreasure.ApplicationData.FilterType;
import com.northeastern.numad.virtualtreasure.TreasureDataList.OnTreasureAddListener;

import edu.neu.virtualtreasure.R;

public class ActivityMaps extends MapActivity implements LocationListener, OnTreasureAddListener{
	
	private static int OverlaySize = 40;
	
//	private MapView mMapView;
	private GoogleMap map;
	
	private MapsItemizedOverlay mCurrentLocationOverlay;
	private MapsItemizedOverlay mItemizedDataOverlay;
	private Location mCurrentLocation;
	
	private LocationManager mLocationManager;
	
	private ProgressDialog mProgressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_maps_vt);
	    
//	    mMapView = (MapView) findViewById(R.id.mapview);
	    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
	            .getMap();
	    
//	    map.set
//	    mMapView.setBuiltInZoomControls(true);
//	    List<Overlay> mapOverlays = mMapView.getOverlays();
	    Drawable drawable = this.getResources().getDrawable(R.drawable.img_happy);
	    drawable.setBounds(-1 * (OverlaySize / 2), -1 * (OverlaySize), OverlaySize / 2, 0);
	    mItemizedDataOverlay = new MapsItemizedOverlay(drawable, this);
	    
	    // Create data list
//        if (ApplicationData.mTreasureDataList == null)
//        {
//        	ApplicationData.mTreasureDataList = new TreasureDataList();
//        
//        	// Fetch data from Server
//    	    mProgressDialog = new ProgressDialog( ActivityMaps.this );
//        	mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        	mProgressDialog.setMessage("Fetching data from server...");
//        	mProgressDialog.setCancelable(false);
//        	mProgressDialog.setIndeterminate(true);
//        	mProgressDialog.show();
//		}
//	    
//    	HttpRequest request = new HttpRequest();
//    	request.execute((Object) null);
	    
//	    mapOverlays.add(mItemizedDataOverlay);
	    
	    // Get current location
	    mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	    
	    // Populate Spinner
	    ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	    
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(DataTypeSpinnerAdapter.MAP_KEY_NAME, "All");
        map.put(DataTypeSpinnerAdapter.MAP_KEY_ICON, R.drawable.img_message);
        map.put(DataTypeSpinnerAdapter.MAP_KEY_TYPE, FilterType.All);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put(DataTypeSpinnerAdapter.MAP_KEY_NAME, "Facebook");
        map.put(DataTypeSpinnerAdapter.MAP_KEY_ICON, R.drawable.img_facebook);
        map.put(DataTypeSpinnerAdapter.MAP_KEY_TYPE, FilterType.Facebook);
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put(DataTypeSpinnerAdapter.MAP_KEY_NAME, "Twitter");
        map.put(DataTypeSpinnerAdapter.MAP_KEY_ICON, R.drawable.img_twitter);
        map.put(DataTypeSpinnerAdapter.MAP_KEY_TYPE, FilterType.Twitter);
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put(DataTypeSpinnerAdapter.MAP_KEY_NAME, "http://");
        map.put(DataTypeSpinnerAdapter.MAP_KEY_ICON, R.drawable.img_http);
        map.put(DataTypeSpinnerAdapter.MAP_KEY_TYPE, FilterType.URL);
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put(DataTypeSpinnerAdapter.MAP_KEY_NAME, "Message");
        map.put(DataTypeSpinnerAdapter.MAP_KEY_ICON, R.drawable.img_message);
        map.put(DataTypeSpinnerAdapter.MAP_KEY_TYPE, FilterType.Message);
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put(DataTypeSpinnerAdapter.MAP_KEY_NAME, "Private");
        map.put(DataTypeSpinnerAdapter.MAP_KEY_ICON, R.drawable.img_message);
        map.put(DataTypeSpinnerAdapter.MAP_KEY_TYPE, FilterType.Private);
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put(DataTypeSpinnerAdapter.MAP_KEY_NAME, "Custom");
        map.put(DataTypeSpinnerAdapter.MAP_KEY_ICON, R.drawable.img_message);
        map.put(DataTypeSpinnerAdapter.MAP_KEY_TYPE, FilterType.Custom);
        list.add(map);
        
        // Set spinner
        Spinner spinner = (Spinner) findViewById(R.id.spMapsFilter);
        DataTypeSpinnerAdapter adapter = new DataTypeSpinnerAdapter(getApplicationContext(), list,
                R.layout.custom_spinner, new String[] { DataTypeSpinnerAdapter.MAP_KEY_NAME, DataTypeSpinnerAdapter.MAP_KEY_ICON },
                new int[] { R.id.tvSpinnerType, R.id.ivSpinnerType }, this);

        spinner.setAdapter(adapter);
        spinner.setSelection( ApplicationData.CurrentFilter.ordinal());
        
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View selectedView, int position,
					long id) {
				Spinner spFilter = (Spinner) parent;
				
				@SuppressWarnings("unchecked")
				HashMap<String, Object> data = (HashMap<String, Object>) spFilter.getItemAtPosition(position);
		    	
		    	ApplicationData.CurrentFilter = (FilterType) data.get(DataTypeSpinnerAdapter.MAP_KEY_TYPE);
		    	
		    	updateView();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// Do Nothing
			}
		});
    }
	
	@Override
    protected void onResume() {
        super.onResume();
        addSampleLocations();
        // Register the listener with the Location Manager to receive location updates
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        
        // Get LKG location
        LocationManager locationManager = (LocationManager) this
				.getSystemService(LOCATION_SERVICE);
        Location lkgLocation = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (lkgLocation != null) {
			Log.d("GPS Location", lkgLocation.toString());
			this.onLocationChanged(lkgLocation);
		} 
		else {
			// Check for the network provider
			lkgLocation = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (lkgLocation != null) {
				Log.d("Network Location", lkgLocation.toString());
				this.onLocationChanged(lkgLocation);
			} 
			else {
				// Do nothing
			}
		}
		
		// Listen to changes in list data
//		ApplicationData.mTreasureDataList.addOnTreasureAddListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister GPS sensor
        mLocationManager.removeUpdates(this);
    }
    
    private void AddOverlayItem(Treasure treasureData)
    {
    	GeoPoint point = new GeoPoint((int) (treasureData.getmLat() * 1000000), (int) (treasureData.getmLong() * 1000000));
    	OverlayItem overlayitem = new OverlayItem(point, treasureData.getMessage(), treasureData.getId() + "");
    	
    	Drawable drawable = getResources().getDrawable(treasureData.getDrawableId());
    	drawable.setBounds(-1 * (OverlaySize / 2), -1 * OverlaySize, OverlaySize / 2, 0);
    	overlayitem.setMarker(drawable);
    	
    	mItemizedDataOverlay.AddOverlay(overlayitem);
    }
    
    private void ZoomToLocation(Location location)
    {
    	if (location != null)
    	{
	    	// Remove previous location overlay
//			this.mMapView.getOverlays().remove(mCurrentLocationOverlay);
			
			// Create current location overlay
		    Drawable drawableCurrentLocation = getResources().getDrawable(R.drawable.ic_maps_indicator_current_position);
			mCurrentLocationOverlay = new MapsItemizedOverlay(drawableCurrentLocation, this);
		    
			GeoPoint currentLocation = new GeoPoint((int )(location.getLatitude() * 1000000), (int )(location.getLongitude() * 1000000));
			OverlayItem currentLocationOverlay = new OverlayItem(currentLocation, "", "");
			mCurrentLocationOverlay.AddOverlay(currentLocationOverlay);
			
			map.addMarker(new MarkerOptions()
			.position(new LatLng(location.getLatitude(), location.getLongitude()))
			.title("Current location")
			.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.ic_maps_indicator_current_position)));
			map.animateCamera(
					CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
//	    	this.mMapView.getController().animateTo(currentLocation);
//	        this.mMapView.getController().setZoom(14);
//	        this.mMapView.getOverlays().add(mCurrentLocationOverlay);
    	}
    	else
    	{
    		Toast.makeText(this, R.string.message_current_location_unknown, Toast.LENGTH_SHORT).show();
    	}
    	
    }
    
    private void addSampleLocations() {
    	map.addMarker(new MarkerOptions()
		.title("Facebook contact")
		.snippet("")
		.position(new LatLng(42.345192,-71.08597))
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.img_facebook)));
    	
    	map.addMarker(new MarkerOptions()
		.title("Image")
		.snippet("")
		.position(new LatLng(47.620657,-122.18946))
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.img_message)));
    	
    	map.addMarker(new MarkerOptions()
		.title("Twitter contact")
		.snippet("")
		.position(new LatLng(50.620657,-56.18946))
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.img_twitter)));
    
    	map.addMarker(new MarkerOptions()
		.title("Soundcloud link")
		.snippet("")
		.position(new LatLng(42.345192,-74.08597))
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.img_http)));
    }
    
    private void updateView()
    {
    	String str = readfile();
    	
    	if(str !=null && !str.equals("")) {
    	TreasureList treasureList = new Gson().fromJson(readfile(),TreasureList.class);
    	TreasureList.listGlobal = treasureList.getList();
    	
    	synchronized (treasureList) {
	    	// Remove previous location overlay
//			this.mMapView.getOverlays().remove(mItemizedDataOverlay);
			map.clear();
			// Create current location overlay
		    Drawable drawableCurrentLocation = getResources().getDrawable(R.drawable.img_happy);
		    mItemizedDataOverlay = new MapsItemizedOverlay(drawableCurrentLocation, this);
			
			for (Treasure treasureData: treasureList.getList())
			{
				if (ApplicationData.IsFiltered(treasureData))
				{
//					this.AddOverlayItem(treasureData);
					map.addMarker(new MarkerOptions()
					.position(new LatLng(treasureData.getmLat(), treasureData.getmLong()))
					.icon(BitmapDescriptorFactory.fromResource(treasureData.getDrawableId())));
				}
			}
//			map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.img_happy)));
//	    	this.mMapView.getOverlays().add(mItemizedDataOverlay);
//	    	
//	    	this.mMapView.postInvalidate();
    	}
    	}
    }
	
    private String readfile() {
    	int ch;
    	StringBuffer fileContent = new StringBuffer();
    	FileInputStream fis;
    	try {
    	    fis = this.openFileInput("treasureData");
    	    try {
    	        while( (ch = fis.read()) != -1)
    	            fileContent.append((char)ch);
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
    	} catch (FileNotFoundException e) {
    	    e.printStackTrace();
    	}

    	return new String(fileContent);
    }
    
    @Override
	public void onLocationChanged(Location location) {
    	
    	if (location != null)
    	{
    		if (mCurrentLocation == null)
    		{
    			ZoomToLocation(location);	
    		}
    		
    		mCurrentLocation = location;
    		
    		for (TreasureData treasureData : ApplicationData.mTreasureDataList) {
    			treasureData.UpdateCurrentLocation(location);
    		}
    	}
	}
    
    @Override
	public void onProviderDisabled(String provider) {
		// Do nothing
	}

	@Override
	public void onProviderEnabled(String provider) {
		// Do nothing
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// Do nothing
	}
    
	@Override
	protected boolean isRouteDisplayed() {
		// Do nothing
		return false;
	}
	
	public void onMapsCreateNewButtonClicked(View v)
	{
		
		Intent i = new Intent(ActivityMaps.this, ActivityCreateNew.class);
		i.putExtra("Latitude", 42.35);
		i.putExtra("Longitude", -71.06);
		ActivityMaps.this.startActivity(i);
		
		if (mCurrentLocation != null)
		{
			Intent myIntent = new Intent(ActivityMaps.this, ActivityCreateNew.class);
			myIntent.putExtra("Latitude", this.mCurrentLocation.getLatitude());
			myIntent.putExtra("Longitude", this.mCurrentLocation.getLongitude());
			ActivityMaps.this.startActivity(myIntent);
		}
		else
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					ActivityMaps.this);
	 
			// Title
			alertDialogBuilder.setTitle("Unknown Location");
			 
			// Message
			alertDialogBuilder.setMessage("Unable to create new item as current Location is not known. Please wait while we fetch your current location.")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).setIcon(R.drawable.ic_menu_mylocation);
				 
			// Create dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
		 
			// Display the dialog
			alertDialog.show();
		}
	}
	
	public void onMapsCameraButtonClicked(View v)
	{
		Intent myIntent = new Intent(ActivityMaps.this, ActivityCamera.class);
		ActivityMaps.this.startActivity(myIntent);	
	}
	
	public void onMapsMyLocationButtonClicked(View v)
	{
		ZoomToLocation(mCurrentLocation);
	}

	@Override
	public void OnTreasureAdd(TreasureData treasureData) {
		
		if ((mProgressDialog == null) || (!mProgressDialog.isShowing()))
		{
			Log.d("VirtualTreasure", "Updating View");
			updateView();
		}
		else
		{
			Log.d("VirtualTreasure", "Not updating View");
		}
	}
	
	private class HttpRequest extends AsyncTask<Object, Object, Object>
    {
		@Override
		protected Object doInBackground(Object... arg0) {

//			ServerConnection.FetchTreasureData();
			
			// Uncomment this section to clear everything and add following fake Boston data to server 
		    
		    
		    
		    // Boston Locations
//		    ApplicationData.mTreasureDataList.add(new TreasureData(null, "author=virtualtreasure&lat=42.345192&lon=-71.08597&facebook//aman124@gmail.com"));
//		    ApplicationData.mTreasureDataList.add(new TreasureData(null, "author=virtualtreasure&lat=42.341195&lon=-71.084425&twitter//@aman124"));
//		    ApplicationData.mTreasureDataList.add(new TreasureData(null, "author=virtualtreasure&lat=42.343225&lon=-71.09489&twitter//@random"));
//		    ApplicationData.mTreasureDataList.add(new TreasureData(null, "author=virtualtreasure&lat=42.338467&lon=-71.102099&http://www.apple.com"));
//		    ApplicationData.mTreasureDataList.add(new TreasureData(null, "author=virtualtreasure&lat=42.327332&lon=-71.090298&http://www.google.com"));
//		    ApplicationData.mTreasureDataList.add(new TreasureData(null, "author=virtualtreasure&lat=42.330948&lon=-71.104374&facebook//aman124@gmail.com"));
//		    ApplicationData.mTreasureDataList.add(new TreasureData(null, "author=virtualtreasure&lat=42.338721&lon=-71.084161&twitter//@aman124"));
//		    ApplicationData.mTreasureDataList.add(new TreasureData(null, "author=virtualtreasure&lat=42.335453&lon=-71.094546&http://www.android.com"));
//		    ApplicationData.mTreasureDataList.add(new TreasureData(null, "author=virtualtreasure&lat=42.328601&lon=-71.099396&facebook//random@gmail.com"));
//		    ApplicationData.mTreasureDataList.add(new TreasureData(null, "author=virtualtreasure&lat=42.333423&lon=-71.097686&https://www.facebook.com"));
	        
		    /*
	        // Seattle Locations
		    // ApplicationData.mTreasureDataList.add(new TreasureData(null, "lat=47.620657&lon=-122.18946&http://www.microsoft.com"));
		    // ApplicationData.mTreasureDataList.add(new TreasureData(null, "lat=47.620758&lon=-122.184589&http://www.microsoft.com"));
		    // ApplicationData.mTreasureDataList.add(new TreasureData(null, "lat=47.618922&lon=-122.182562&http://www.microsoft.com"));
		    // ApplicationData.mTreasureDataList.add(new TreasureData(null, "lat=47.62274&lon=-122.193054&http://www.microsoft.com"));
		    
		    for (TreasureData data: ApplicationData.mTreasureDataList)
		    {
		    	ServerConnection.PutTreasureData(data);
		    }
		    */
		    
			return null;
		}
		
		@Override
		protected void onPostExecute(Object object)
		{
			if ((mProgressDialog != null) && (mProgressDialog.isShowing()))
			{
				updateView();
				mProgressDialog.cancel();
			}
		}
    }
}