
package com.northeastern.numad.virtualtreasure;

import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionState;
import com.facebook.Settings;

import edu.neu.virtualtreasure.R;


// ----------------------------------------------------------------------

public class ActivityMainMenu extends Activity {
    
	static final String URL_PREFIX_FRIENDS = "https://graph.facebook.com/me/friends?access_token=";
    Session.StatusCallback statusCallback = new SessionStatusCallback();
    
    Button bFBLoginLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vt);
        
        bFBLoginLogout = (Button) findViewById(R.id.buttonFBLogin);
        Settings.addLoggingBehavior(com.facebook.LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new OpenRequest(this).setCallback(statusCallback));
            }
        }
        
        ApplicationData.mTreasureDataList = new TreasureDataList();
        updateView();
        
        checkLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        // Update FB button
        updateView();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    
    public Dialog CreateLoginDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View v = inflater.inflate(R.layout.dialog_login, null);
		final EditText usernameEditText = (EditText) v.findViewById(R.id.username);
		final EditText passwordEditText = (EditText) v.findViewById(R.id.password);
		
//		builder.setView(v)
//				.setPositiveButton("Enter",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog, int id) {
//
//								if (usernameEditText.getText().toString().trim().length()
//										== 0) {
//									Toast.makeText(
//											ActivityMainMenu.this,
//											"UserName cannot be blank",
//											Toast.LENGTH_LONG).show();
//								} else if (passwordEditText.getText().toString().trim().length()
//										== 0) {
//									Toast.makeText(
//											ActivityMainMenu.this,
//											"Passowrd cannot be blank",
//											Toast.LENGTH_LONG).show();
//								} else
//								{
//									int loginStatus = ServerConnection.Login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
											
									// Check if login is valid 
//									if (loginStatus == ServerConnection.SERVER_UNAVAILABLE)
//									{
//										Toast.makeText(ActivityMainMenu.this, "Sigh! Our servers are unavailable this time. Please try again later.", Toast.LENGTH_SHORT).show();
//									}
//									else if (loginStatus == ServerConnection.LOGIN_FAILED)
//									{
//										Toast.makeText(ActivityMainMenu.this, "Login Failed !", Toast.LENGTH_SHORT).show();
//									}
//									else
//									{
//										if (loginStatus == ServerConnection.UNKNOWN_USERNAME)
//										{
//											ServerConnection.CreateAccount(usernameEditText.getText().toString(), passwordEditText.getText().toString());
//											Toast.makeText(ActivityMainMenu.this, "Created New Account !", Toast.LENGTH_SHORT).show();
//										}
										
//										// Save username and password
//										SharedPreferences settings = getSharedPreferences(getResources().getString(R.string.app_name), 0);
//										SharedPreferences.Editor editor = settings.edit();
//										
//						            	editor.putString(getResources().getString(R.string.username), usernameEditText.getText().toString());
//						            	editor.putString(getResources().getString(R.string.password), passwordEditText.getText().toString());
//						            	
//						            	editor.commit();
//						            	
						            	// Save login info as application data
//						            	ApplicationData.UserName = usernameEditText.getText().toString();
//						            	
//										checkLogin();
//									}
//								}
//							}
//						})
//				.setNegativeButton("Cancel",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog, int id) {
//								dialog.cancel();
//							}
//						});

		return builder.create();
	}

    public void checkLogin()
    {
    	// Check if a user is logged in to facebook
    	Session session = Session.getActiveSession();
    	
        if ((session != null) && (session.isOpened())) {
        	
        	// Go to User's profile page
        	startUserProfileActivity();
        }
        else
        {
        	// Restore preferences and check if a user is logged in 
        	// via normal login method
            SharedPreferences settings = getSharedPreferences(getResources().getString(R.string.app_name), 0);
            
            String username = settings.getString(getResources().getString(R.string.username), "");
            String password = settings.getString(getResources().getString(R.string.password), "");
            
            if ((username.length() > 0) && (password.length() > 0))
            {
    	        ApplicationData.UserName = username;
    	        
    	        // Go to User's profile page
    	        startUserProfileActivity();
            }
            else
            {
            	SharedPreferences.Editor editor = settings.edit();
            	editor.remove(getResources().getString(R.string.username));
            	editor.remove(getResources().getString(R.string.password));
            	editor.commit();
            }
        }
    }
    
	public void startUserProfileActivity() {
		
		Intent i = new Intent(ActivityMainMenu.this, ActivityUserProfile.class);
		startActivity(i);
		this.finish();
	}
	
	public void loginButtonClicked(View v)
    {
		// Show login dialog
		CreateLoginDialog().show();
    }
    
    public void aboutButtonClicked(View v)
    {
    	Log.e("VirtualTreasure", "About");
    	
    	Intent i = new Intent(ActivityMainMenu.this, ActivityAbout.class);
		startActivity(i);
    }
    
    public void ackButtonClicked(View v)
    {
    	Log.e("VirtualTreasure", "Acknowledgment");
    	
    	Intent i = new Intent(ActivityMainMenu.this, ActivityAcknowledgement.class);
		startActivity(i);
    }
	
	public void exitActivity(View v) {
		
		// Exit application
		finish();
	}
    
	 @Override
    public void onStart() {
        super.onStart();
        Session.getActiveSession().addCallback(statusCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        Session.getActiveSession().removeCallback(statusCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }

    private void updateView() {
        Session session = Session.getActiveSession();
        if (session.isOpened()) {
        	
        	// Setup facebook logout button
        	bFBLoginLogout.setText(R.string.fbLogout);
            bFBLoginLogout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    onClickLogout();
                }
            });
        } else {
        	
        	// Setup facebook login button
            bFBLoginLogout.setText(R.string.fbLogin);
            bFBLoginLogout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) { 
                	onClickLogin(); 
                }
            });
        }
    }

    private void onClickLogin() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
        } else {
//            Session.openActiveSession(this, true, statusCallback);
            Session.OpenRequest request = new Session.OpenRequest(this);
            request.setPermissions(Arrays.asList("user_friends"));
            request.setCallback(statusCallback);

            // get active session
            Session mFacebookSession = Session.getActiveSession();
            if (mFacebookSession == null || mFacebookSession.isClosed()) 
            {
                mFacebookSession = new Session(this);
            }
            mFacebookSession.openForRead(request);
            Session.setActiveSession(mFacebookSession);
        }
    }

    private void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
    }
	
	private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
        	updateView();
            checkLogin();
        }
    }

}