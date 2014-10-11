
package com.northeastern.numad.virtualtreasure;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import edu.neu.virtualtreasure.R;


// ----------------------------------------------------------------------

public class ActivityProjectDescription extends Activity {
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_description);
    }
	
	public void onPDNext(View view)
    {
		// Start maps activity
    	Intent i = new Intent(ActivityProjectDescription.this, ActivityMainMenu.class);
		startActivity(i);
    }
}