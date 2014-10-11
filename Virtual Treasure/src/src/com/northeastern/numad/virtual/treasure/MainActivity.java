package src.com.northeastern.numad.virtual.treasure;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import edu.neu.virtualtreasure.R;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);
	}

	

	public void loginButtonClicked(View v) {
		Intent i = new Intent(MainActivity.this, ThingsToDoActivity.class);
		startActivity(i);
	}
}
