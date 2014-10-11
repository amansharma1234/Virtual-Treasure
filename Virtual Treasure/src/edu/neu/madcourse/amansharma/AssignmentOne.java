package edu.neu.madcourse.amansharma;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.neu.virtualtreasure.R;


public class AssignmentOne extends Activity {
    /** Called when the activity is first created. */
    
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final Intent intent = new Intent (this, Sudoku.class);
        final Intent intent_Boggle = new Intent (this, SplashActivity.class);
        final Intent intent_trBoggle = new Intent (this, src.com.northeastern.numad.virtual.treasure.MainActivity.class);
        final Intent intent_virtual_treasure = new Intent (this, com.northeastern.numad.virtualtreasure.ActivityProjectDescription.class);
        String s = "Aman Sharma\nsharma.aman@husky.neu.edu\n\nRohan Joshi\n" +
        		"joshi.roh@husky.neu.edu\n\nApp V 1.0\nIMEI 000000000000000";
        
        TextView tv = (TextView)findViewById(R.id.tv);
        tv.setText(R.string.my_name); 
        
        Button bTeam = (Button)findViewById(R.id.button_t);
        bTeam.setText(R.string.team_text);
        
        Button bSudoku = (Button)findViewById(R.id.button_s);
        bSudoku.setText(R.string.sudoku_text);
        
        Button bError = (Button)findViewById(R.id.button_e);
        bError.setText(R.string.error_text);
        
        Button bExit = (Button)findViewById(R.id.button_ex);
        bExit.setText(R.string.exit_text);
        
        Button bBoggle = (Button)findViewById(R.id.button_boggle);
        bBoggle.setText(R.string.boggle_text);
        
        Button pBoggle = (Button)findViewById(R.id.button_pers);
        pBoggle.setText("Persistent Boggle");
        
        Button trBoggle = (Button)findViewById(R.id.button_tp);
        trBoggle.setText("Trickiest Part");
        
        Button vtButton = (Button)findViewById(R.id.button_vt);
        vtButton.setText("Final Project");
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(s).setCancelable(false)
             
               .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                   }
               });
        final AlertDialog alert = builder.create();
          
        OnClickListener teamClick = new OnClickListener() {
         public void onClick(View v) {
        	 	 
        	 	alert.show();
            }
        };
        
        OnClickListener sClick = new OnClickListener() {
            public void onClick(View v) {
            	startActivity(intent);
            	
               }
           };
       
       OnClickListener eClick = new OnClickListener() {
               public void onClick(View v) {
              	 
            	   int n =  1 / 0;
            	 }
              };
        
       OnClickListener boggleClick = new OnClickListener() {
                  public void onClick(View v) {
                   	
                	  startActivity(intent_Boggle);
                     }
                   };
        
       OnClickListener exClick = new OnClickListener() {
               public void onClick(View v) {
                	
                	finish();
                  }
                };
      OnClickListener trickClick = new OnClickListener() {
               public void onClick(View v) {
            	   startActivity(intent_trBoggle);
                 }
               };  
      OnClickListener vtClick = new OnClickListener() {
               public void onClick(View v) {
               	   startActivity(intent_virtual_treasure);
               	 }
               }; 
       
        bTeam.setOnClickListener(teamClick); 
        bSudoku.setOnClickListener(sClick);
        bError.setOnClickListener(eClick);
        bBoggle.setOnClickListener(boggleClick);
        bExit.setOnClickListener(exClick);
        trBoggle.setOnClickListener(trickClick);
        vtButton.setOnClickListener(vtClick);
        
        
	}
}

  

