
package com.northeastern.numad.virtualtreasure;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import edu.neu.virtualtreasure.R;


// ----------------------------------------------------------------------

public class ActivityTreasureDetails extends Activity {
    
	public static String KEY_DATA_ID = "details_data_id";
	
	private static String mTreasureDataId;
	private static int MAX_AVAIL_DISTANCE = 100;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure_details);
        
        final TreasureData treasureData;
        
        if (savedInstanceState != null)
        {
        	mTreasureDataId = savedInstanceState.getString(KEY_DATA_ID);
        }
        else
        {
        	Intent intent = getIntent();
        	
        	if (intent != null)
        	{
        		mTreasureDataId = intent.getStringExtra(KEY_DATA_ID);
        	}
        }
        
        treasureData = ApplicationData.mTreasureDataList.get(mTreasureDataId);
        
        if (treasureData != null)
        {
        	android.util.Log.d("VT", "Distance : " + treasureData.getDistance());
        	
        	if (treasureData.getDistance() <= MAX_AVAIL_DISTANCE)
        	{
        	    TextView tvLatitude = (TextView) findViewById(R.id.tvDetailsLatitude);
		        TextView tvLongitude = (TextView) findViewById(R.id.tvDetailsLongitude);
		        TextView tvMessageType = (TextView) findViewById(R.id.tvDetailsMessageType);
		        TextView tvMessage = (TextView) findViewById(R.id.tvDetailsMessage);
		        TextView tvAuthor = (TextView) findViewById(R.id.tvDetailsAuthor);
		        TextView tvIsPrivate = (TextView) findViewById(R.id.tvDetailsIsPrivate);
		        TextView tvRecipients = (TextView) findViewById(R.id.tvDetailsRecipients);
		        
		        tvLatitude.setText(String.format("%.6f", treasureData.getLatitude()));
		        tvLongitude.setText(String.format("%.6f", treasureData.getLongitude()));
		        tvMessageType.setText(treasureData.getType().toString());
		        tvMessage.setText(treasureData.getMessage());
		        tvAuthor.setText(treasureData.getAuthor());
		        tvIsPrivate.setText((treasureData.isPrivate())?"Yes":"No");
		        tvRecipients.setText(treasureData.getRecipientsAsString());
		        
		        OnClickListener listener = null;
		        
		        if (treasureData.getType() == TreasureData.DataType.Facebook)
		        {
		        	listener = new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							String url = "https://www.facebook.com/" + treasureData.getMessage();
							Intent i = new Intent(Intent.ACTION_VIEW);
							i.setData(Uri.parse(url));
							startActivity(i);
						}
					};
		        }
		        else if (treasureData.getType() == TreasureData.DataType.Twitter)
		        {
		        	listener = new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							String url = "https://twitter.com/" + treasureData.getMessage().replace("@", "");
							Intent i = new Intent(Intent.ACTION_VIEW);
							i.setData(Uri.parse(url));
							startActivity(i);
						}
					};
		        }
		        else if (treasureData.getType() == TreasureData.DataType.URL)
		        {
		        	listener = new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							String url = treasureData.getMessage();
							if (url.contains("http"))
							{
								Intent i = new Intent(Intent.ACTION_VIEW);
								i.setData(Uri.parse(url));
								startActivity(i);
							} 
							else
							{
								Toast.makeText(ActivityTreasureDetails.this, "Invalid Url.", Toast.LENGTH_SHORT).show();
							}
						} 
					};
		        }
		        
		        if (listener != null)
		        {
			        tvMessage.setTextColor(getResources().getColor(R.color.link_highlight));
			        tvMessage.setOnClickListener(listener);
		        }
        	}
        	else
        	{
        		Toast.makeText(ActivityTreasureDetails.this, "Please reach the location first !!!", Toast.LENGTH_SHORT).show();
        		ActivityTreasureDetails.this.finish();
        	}
        }
    }
}