package com.northeastern.numad.virtualtreasure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import edu.neu.virtualtreasure.R;

public class ActivityFacebookFriends extends ListActivity {

	public class Data {
		public String id;
		public String name;
	};

	public class DataComparator implements Comparator<Data> {

		@Override
		public int compare(Data arg0, Data arg1) {
			return arg0.name.compareTo(arg1.name);
		}
	}

	// ListView listView;
	private ArrayList<Data> listFriends;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook_friends);

		final ListView listView = (ListView) findViewById(android.R.id.list);
		final LinearLayout llFacebookFriends = (LinearLayout) findViewById(R.id.llFacebookFriends);

		final Session session = Session.getActiveSession();

		// Check if logged in via FaceBook
		if ((session != null) && session.isOpened()) {
			Request request = Request.newMyFriendsRequest(session,
					new GraphUserListCallback() {

						@Override
						public void onCompleted(List<GraphUser> users,
								Response response) {

							if (users != null) {

								String[] values = new String[users.size()];

								listFriends = new ArrayList<ActivityFacebookFriends.Data>();

								for (int counter = 0; counter < users.size(); counter++) {
									Data data = new Data();

									data.name = users.get(counter).getName();
									data.id = users.get(counter).getId();

									listFriends.add(data);
								}

								Collections.sort(listFriends,
										new DataComparator());

								for (int counter = 0; counter < listFriends
										.size(); counter++) {
									values[counter] = listFriends.get(counter).name;
								}

								// First paramenter - Context
								// Second parameter - Layout for the row
								// Third parameter - ID of the TextView to which
								// the data is written
								// Forth - the Array of data
								ArrayAdapter<String> adapter = new ArrayAdapter<String>(
										ActivityFacebookFriends.this,
										android.R.layout.simple_list_item_1,
										android.R.id.text1, values);

								// Assign adapter to ListView
								listView.setAdapter(adapter);

								listView.setVisibility(View.VISIBLE);
								llFacebookFriends.setVisibility(View.GONE);
							} else
								Toast.makeText(ActivityFacebookFriends.this,
										"Check internet connection",
										Toast.LENGTH_SHORT).show();
						}
					});

			Request.executeBatchAsync(request);
		}

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1,
					int position, long arg3) {

				final Session session = Session.getActiveSession();

				final Data data = listFriends.get(position);

				String url = "https://graph.facebook.com/" + data.id;
				HttpRequest request = new HttpRequest();
				request.execute(url);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private class HttpRequest extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... uri) {

			try {
				URL url = new URL(uri[0]);

				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setDoInput(true);
				connection.connect();

				BufferedReader bf = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;

				while ((line = bf.readLine()) != null) {
					sb.append(line + '\n');
				}

				return sb.toString();
			} catch (IOException e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(String response) {
			String test = response;
			try {

				Intent intent = new Intent();

				JSONObject json = new JSONObject(response);

				intent.putExtra("UserName", json.getString("username"));
				setResult(Activity.RESULT_OK, intent);

				finish();
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}
}