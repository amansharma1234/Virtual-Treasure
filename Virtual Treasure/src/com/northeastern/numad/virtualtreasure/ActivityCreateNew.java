
package com.northeastern.numad.virtualtreasure;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.northeastern.numad.virtualtreasure.TreasureData.DataType;

import edu.neu.virtualtreasure.R;


// ----------------------------------------------------------------------

public class ActivityCreateNew extends Activity {
    
	private TreasureData mTreasureData;
	private ProgressDialog mProgressDialog; 
	
	private static int RESULT_FB_FRIENDS = 0x0;
	
	EditText etRecipients;
	EditText etMessage;
	Button btAddRecipient;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new);
        
        this.mTreasureData = new TreasureData();
        if (savedInstanceState != null)
        {
        	this.mTreasureData.setLatitude(savedInstanceState.getDouble("Latitude"));
            this.mTreasureData.setLongitude(savedInstanceState.getDouble("Longitude"));
        }
        else
        {
        	Intent intent = getIntent();
        	if (intent != null)
        	{
	        	this.mTreasureData.setLatitude(intent.getDoubleExtra("Latitude", 0.0f));
	            this.mTreasureData.setLongitude(intent.getDoubleExtra("Longitude", 0.0f));
        	}
        }
        
        EditText etLatitude = (EditText) findViewById(R.id.etNewLatitude);
        EditText etLongitude = (EditText) findViewById(R.id.etNewLongitude);
        
        final Button btAddRecipient = (Button) findViewById(R.id.addRecipient);
        
        etLatitude.setText(String.format("%.6f", this.mTreasureData.getLatitude()));
        etLongitude.setText(String.format("%.6f", this.mTreasureData.getLongitude()));
        
        CheckBox cbPrivate = (CheckBox) findViewById(R.id.cbNewIsPrivate);
        etRecipients = (EditText) findViewById(R.id.etNewRecipients);
    	etMessage = (EditText) findViewById(R.id.etNewMessage);
        
    	etMessage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CreateMessageDialog().show();
			}
		});
        // Add onFocusChanged listener to Message EditText
        btAddRecipient.setOnClickListener(new OnClickListener() {

        	@Override
			public void onClick(View arg0) {
				
				android.util.Log.d("VirtualTreasure", "");
					if (ApplicationData.FBUser != null)
					{
						// Start maps activity
				    	Intent i = new Intent(ActivityCreateNew.this, ActivityFacebookFriends.class);
				    	startActivityForResult(i, RESULT_FB_FRIENDS);
					}
					else
					{
						CreateRecipientDialog().show();
					}
			}
		});
        
        cbPrivate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) 
			{
				if (checked)
				{
					btAddRecipient.setVisibility(View.VISIBLE);
				}
				else
				{
					btAddRecipient.setVisibility(View.GONE);
				}
			}
		});
        
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(DataTypeSpinnerAdapter.MAP_KEY_NAME, "Facebook");
        map.put(DataTypeSpinnerAdapter.MAP_KEY_ICON, R.drawable.img_facebook);
        map.put(DataTypeSpinnerAdapter.MAP_KEY_TYPE, TreasureData.DataType.Facebook);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put(DataTypeSpinnerAdapter.MAP_KEY_NAME, "Twitter");
        map.put(DataTypeSpinnerAdapter.MAP_KEY_ICON, R.drawable.img_twitter);
        map.put(DataTypeSpinnerAdapter.MAP_KEY_TYPE, TreasureData.DataType.Twitter);
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put(DataTypeSpinnerAdapter.MAP_KEY_NAME, "http://");
        map.put(DataTypeSpinnerAdapter.MAP_KEY_ICON, R.drawable.img_http);
        map.put(DataTypeSpinnerAdapter.MAP_KEY_TYPE, TreasureData.DataType.URL);
        list.add(map);
        
        map = new HashMap<String, Object>();
        map.put(DataTypeSpinnerAdapter.MAP_KEY_NAME, "Message");
        map.put(DataTypeSpinnerAdapter.MAP_KEY_ICON, R.drawable.img_message);
        map.put(DataTypeSpinnerAdapter.MAP_KEY_TYPE, TreasureData.DataType.Message);
        list.add(map);

        Spinner spin = (Spinner) findViewById(R.id.spNewMessageType);
        DataTypeSpinnerAdapter adapter = new DataTypeSpinnerAdapter(getApplicationContext(), list,
                R.layout.custom_spinner, new String[] { "Name", "Icon" },
                new int[] { R.id.tvSpinnerType, R.id.ivSpinnerType }, this);

        spin.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    
    public Dialog CreateMessageDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View v = inflater.inflate(R.layout.dialog_message, null);
		final EditText rnameEditText = (EditText) v.findViewById(R.id.etmessage);
		rnameEditText.setText(etMessage.getText().toString());
		//final String result = rnameEditText.getText().toString();
		builder.setView(v)
				.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								etMessage.setText(rnameEditText.getText().toString());
							}
						});

		return builder.create();
	}
    
    public Dialog CreateRecipientDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View v = inflater.inflate(R.layout.dialog_addrecipient, null);
		final EditText rnameEditText = (EditText) v.findViewById(R.id.recipientname);
		
		//final String result = rnameEditText.getText().toString();
		builder.setView(v)
				.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								String text = etRecipients.getText().toString();
								if (rnameEditText.getText().toString().equals(""))
								{
									Toast.makeText(ActivityCreateNew.this, "Please add recipient!!", Toast.LENGTH_SHORT).show();
								}
								else
								{
									if (text.length() > 0)
									{
										text += "\n" + rnameEditText.getText().toString();
									}
									else
									{
										text += rnameEditText.getText().toString();
						 			}	
						 			etRecipients.setText(text);
								}
							}
						});

		return builder.create();
	}

    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	 super.onActivityResult(requestCode, resultCode, data);
    	 
    	 if (requestCode == RESULT_FB_FRIENDS)
    	 {
	 		 if(resultCode == Activity.RESULT_OK)
	 		 {
	 			 String text = etRecipients.getText().toString();
	 			 
	 			 if (text.length() > 0)
	 			 {
	 				 text += "\n" + data.getExtras().getString("UserName");
	 			 }
	 			 else
	 			 {
	 				text += data.getExtras().getString("UserName");
	 			 }
	 			 
	 			 etRecipients.setText(text);
	 		 }
    	}
    }
    
    public void onNewCreateButtonClicked(final View view)
    {
    	CheckBox cbPrivate = (CheckBox) findViewById(R.id.cbNewIsPrivate);
    	if (etRecipients.getText().toString().equals("") && cbPrivate.isChecked())
    	{
    		Toast.makeText(ActivityCreateNew.this, "Please add recipient!!", Toast.LENGTH_SHORT).show();
    	}
    	else
    	{
    		Spinner spType = (Spinner) findViewById(R.id.spNewMessageType);
	        EditText etRecipients = (EditText) findViewById(R.id.etNewRecipients);
	    	
	        
			@SuppressWarnings("unchecked")
			HashMap<String, Object> data = (HashMap<String, Object>) spType.getSelectedItem();
	    	
	    	// Disable view
	    	view.setEnabled(false);
	    	
	    	mTreasureData.setType((DataType) data.get(DataTypeSpinnerAdapter.MAP_KEY_TYPE));
	    	mTreasureData.setMessage(etMessage.getText().toString());
	    	mTreasureData.setAuthor(ApplicationData.UserName);
	    	mTreasureData.setPrivate(cbPrivate.isChecked());
	    	if (mTreasureData.isPrivate())
	    	{
	    		mTreasureData.addTargetedUser(etRecipients.getText().toString());
	    	}
	    	
	    	Treasure treasure = new Treasure();
	    	treasure.setType((DataType) data.get(DataTypeSpinnerAdapter.MAP_KEY_TYPE));
	    	treasure.setMessage(etMessage.getText().toString());
	    	treasure.setmAuthor(ApplicationData.UserName);
	    	treasure.setPrivate(cbPrivate.isChecked());
	    	treasure.setmLat(mTreasureData.getLatitude());
	    	treasure.setmLong(mTreasureData.getLongitude());
	    	treasure.setId(mTreasureData.getId());
	    	treasure.setDrawableId(mTreasureData.GetDrawableId());
	    	
	    	TreasureList list = new TreasureList();
	    	TreasureList.listGlobal.add(treasure); 
	    	list.getList().addAll(TreasureList.listGlobal); 
	    	
	    	String t = new Gson().toJson(list);
	    	writeToFile(t);

	    	ShowDialog("Gift Box Created", "Hurray! You successfully created a treasure.", 0);

    	}
    }
    
    public void ShowDialog(String title, String message, final Integer result)
    {
	    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				ActivityCreateNew.this);
	
		// Title
		alertDialogBuilder.setTitle(title);
		 
		// Message
		alertDialogBuilder.setMessage(message)
			.setCancelable(false)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
//					
//					if (result == ServerConnection.REQUEST_SUCCESSFUL)
//					{
//						ActivityCreateNew.this.finish();
//					}
				}
			});
			 
		// Create dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
	 
		// Display the dialog
		alertDialog.show();
    }
    
    private void writeToFile(String str) {
    	String filename = "treasureData";
    	FileOutputStream outputStream;

    	try {
    	  outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
    	  outputStream.write(str.getBytes());
    	  outputStream.close();
    	} catch (Exception e) {
    	  e.printStackTrace();
    	}
    }
    
    
//    private class HttpRequest extends AsyncTask<TreasureData, Integer, Integer>
//    {
//		@Override
//		protected Integer doInBackground(TreasureData... params) {
//			int result = ServerConnection.PutTreasureData(params[0]);
//			
//			ApplicationData.mTreasureDataList.add(params[0]);
//			String json = new Gson().toJson(params[0]);
//			return result;
//		}
//    	
//		@Override
//		protected void onPostExecute(Integer result) {
//			
//			if ((mProgressDialog != null) && (mProgressDialog.isShowing()))
//			{
//				mProgressDialog.cancel();
//			}
//	        
//			
//			ShowDialog("Gift Box Created", "Hurray! You successfully created a treasure.", result);
//	        
//		}
//
//    }
}