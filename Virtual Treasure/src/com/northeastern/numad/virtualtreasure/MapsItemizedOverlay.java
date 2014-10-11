
package com.northeastern.numad.virtualtreasure;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MapsItemizedOverlay extends ItemizedOverlay<OverlayItem>{

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	
	public MapsItemizedOverlay(Drawable defaultMarker) {
		super(boundCenter(defaultMarker));
		populate();
	}
	
	public MapsItemizedOverlay(Drawable defaultMarker, Context context) {
		
		this(defaultMarker);
		mContext = context;
	}
	
	@Override
	protected boolean onTap(int index) {
		OverlayItem item = mOverlays.get(index);
		
		/*AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		// dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		*/
		
		Intent myIntent = new Intent(mContext, ActivityTreasureDetails.class);
		myIntent.putExtra(ActivityTreasureDetails.KEY_DATA_ID, item.getSnippet());
		mContext.startActivity(myIntent);
		
		return true;
	}

	public void AddOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	@Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow)
    {
		/*
    }
        if(!shadow)
        {
            super.draw(canvas, mapView, false);
        }
        else
        {*/
        super.draw(canvas, mapView, false);
    }

	
}