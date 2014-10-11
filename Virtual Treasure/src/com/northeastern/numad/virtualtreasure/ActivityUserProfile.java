
package com.northeastern.numad.virtualtreasure;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import edu.neu.virtualtreasure.R;

// ----------------------------------------------------------------------

public class ActivityUserProfile extends Activity {
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        
        ImageView ivProfile = (ImageView) findViewById(R.id.ivUPProfilePic);
        final TextView tvName = (TextView) findViewById(R.id.tvUPUserName);
        
        final Session session = Session.getActiveSession();
    	
        // Check if logged in via facebook
        if ((session != null) && session.isOpened())
        {
	        if (ApplicationData.FBUser == null)
	        {
	        	Request request = Request.newMeRequest(session, new GraphUserCallback() {
					
					@Override
					public void onCompleted(GraphUser user, Response response) {
						
						ApplicationData.FBUser = user;
						ApplicationData.UserName = user.getName();
						tvName.setText(ApplicationData.FBUser.getName());
						
						String url = "https://graph.facebook.com/" + ApplicationData.FBUser.getId() + "/picture?type=large";
						HttpRequest request = new HttpRequest();
				    	request.execute(url);
					}
				});
		    	
		    	Request.executeBatchAsync(request);
	        }
	        else
	        {
	        	tvName.setText(ApplicationData.FBUser.getName());
	        	
	        	if (ApplicationData.FBPicture != null)
	        	{
	        		ivProfile.setImageBitmap(ApplicationData.FBPicture);
	        		
	        		ProgressBar pbUPPhoto = (ProgressBar) findViewById(R.id.pbUPPhoto);
	    			pbUPPhoto.setVisibility(ProgressBar.GONE);
	        	}
	        	else
	        	{
	        		String url = "https://graph.facebook.com/" + ApplicationData.FBUser.getId() + "/picture?type=large";
	        		HttpRequest request = new HttpRequest();
	            	request.execute(url);
	        	}
	        }
        }
        else
        {
        	tvName.setText(ApplicationData.UserName);
        	
        	ProgressBar pbUPPhoto = (ProgressBar) findViewById(R.id.pbUPPhoto);
			pbUPPhoto.setVisibility(ProgressBar.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    
    public void onUPFindTreasureClicked(View view)
    {
    	// Start maps activity
    	Intent i = new Intent(ActivityUserProfile.this, ActivityMaps.class);
		startActivity(i);
    }
    
    public void onUPMapsHelpClicked(View view)
    {
    	Intent i = new Intent(ActivityUserProfile.this, ActivityMapsHelp.class);
		startActivity(i);
    }
    
    public void onUPCameraHelpClicked(View view)
    {
    	Intent i = new Intent(ActivityUserProfile.this, ActivityCameraHelp.class);
		startActivity(i);
    }
    
    public void onUPLogoutClicked(View view)
    {
    	Session session = Session.getActiveSession();
    	
    	Button buttonLogout = (Button) view;
    	buttonLogout.setEnabled(false);
        
     	// Close facebook session if logged in
    	if (session.isOpened()) {
            session.closeAndClearTokenInformation();
            ApplicationData.FBPicture = null;
            ApplicationData.FBUser = null;
        }
    	
		// Delete normal login preferences if logged in
        SharedPreferences settings = getSharedPreferences(getResources().getString(R.string.app_name), 0);
        
        SharedPreferences.Editor editor = settings.edit();
    	editor.remove(getResources().getString(R.string.username));
    	editor.remove(getResources().getString(R.string.password));
    	editor.commit();
    	
    	ApplicationData.UserName = null;
    	
    	Intent i = new Intent(ActivityUserProfile.this, ActivityMainMenu.class);
		startActivity(i);
    	ActivityUserProfile.this.finish();
    }
    
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            
            return myBitmap;
        } 
        catch (IOException e) {
            return null;
        }
    }
    
    private class HttpRequest extends AsyncTask<String, Bitmap, Bitmap>
    {
		@Override
		protected Bitmap doInBackground(String... url) {

			return getBitmapFromURL(url[0]);
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap)
		{
			if (bitmap != null)
			{
				ImageView ivProfile = (ImageView) findViewById(R.id.ivUPProfilePic);
				
				ApplicationData.FBPicture = bitmap;
				ivProfile.setImageBitmap(ApplicationData.FBPicture);
			}
			
			ProgressBar pbUPPhoto = (ProgressBar) findViewById(R.id.pbUPPhoto);
			pbUPPhoto.setVisibility(ProgressBar.GONE);
		}
    }
}