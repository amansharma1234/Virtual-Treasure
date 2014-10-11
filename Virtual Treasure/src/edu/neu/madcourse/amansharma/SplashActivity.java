package edu.neu.madcourse.amansharma;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import edu.neu.virtualtreasure.R;

public class SplashActivity extends Activity
    {
	protected boolean _active = true;
	protected int _splashTime = 5000; // time to display the splash screen in ms
	Search srch = new Search();
    	/** Called when the activity is first created. */
    	@Override
    	public void onCreate(Bundle savedInstanceState) {
    	    super.onCreate(savedInstanceState);
    	    setContentView(R.layout.splash);
    	 
    	    // thread for displaying the SplashScreen
    	    Thread splashTread = new Thread() {
    	        @Override
    	        public void run() {
    	            try {
    	               // int waited = 0;
//    	                while(_active && (waited < _splashTime)) {
//    	                    sleep(100);
//    	                    if(_active) {
//    	                        waited += 100;
//    	                    }
//    	                }
    	                srch.dictionary(SplashActivity.this);
    	            } finally {
    	                finish();
    	                startActivity(new Intent(SplashActivity.this, Boggle.class));  /// error here
    	                
    	            }
    	        }
    	    };
    	    splashTread.start();
    	}
    }

