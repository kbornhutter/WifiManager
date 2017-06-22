package com.example.wifimanagerprototype;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Displays a list of all found APs. 
 *
 */
public class CameraAZActivity extends Activity {
	private final ArrayList<String> listAll = new ArrayList<String>();
	private final ArrayList<String> listPublic = new ArrayList<String>();
	private final ArrayList<String> listPrivate = new ArrayList<String>();
	private String chosenAP;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_az);

		ListView listview = (ListView) findViewById(R.id.listview_camera_az);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("TestObject");
		// Find AP Database and add to AP List
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					Toast.makeText(getApplicationContext(),
							objects.size() + " Access Points Found",
							Toast.LENGTH_LONG).show();

					for (ParseObject entry : objects) {
						if (!listAll.contains(entry.getString("SSID"))) {
							listAll.add(entry.getString("SSID"));
							// Test if Public Network
							if ((entry.getString("Enc") == "[ESS]")
									|| (entry.getString("Enc") == "")) {
								listPublic.add((entry.getString("SSID")));
							} else {
								// Private Network
								listPrivate.add((entry.getString("SSID")));
							}
						}
						View b = findViewById(R.id.progressBar1);
						b.setVisibility(View.GONE);
						filterAll(findViewById(R.id.select_camera_all));
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"Data retrievel Unsuccessful", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		listview.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					// Scroll through List of AP
					return false;
				}
				return false;
			}

		});
		listview.setAdapter(new NetworkListArrayAdapter(this,
				android.R.layout.simple_list_item_1, this.listAll));
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				chosenAP = listAll.get(position);
				loadHeatmap(null);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_no_relocate, menu);
		return true;
	}

	@Override
	/**
	 * Handles selection of settings.
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_home) {
			startActivity(new Intent(this, MainActivity.class));
			return true;
		}
		if (item.getItemId() == R.id.action_az) {
			return true;
		}
		if (item.getItemId() == R.id.action_favorite) {
			startActivity(new Intent(this, CameraFavouritesActivity.class));
			return true;
		}
		if (item.getItemId() == R.id.action_settings) {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		} else
			return super.onOptionsItemSelected(item);
	}

	public void loadHeatmap(View view) {
		startActivity(new Intent(this, CameraActivity.class));
	}
	/**
	 * Shows All networks as a list
	 * @param view
	 */
	public void filterAll(View view) {
		setListAdapter(this.listAll);
		findViewById(R.id.select_camera_all).setBackgroundResource(
				R.drawable.search_type_active);
		findViewById(R.id.select_camera_public).setBackgroundResource(
				R.drawable.search_type);
		findViewById(R.id.select_camera_private).setBackgroundResource(
				R.drawable.search_type);
	}
	/**
	 * Shows only public networks as a list
	 * @param view
	 */
	public void filterPublic(View view) {
		setListAdapter(this.listPublic);
		findViewById(R.id.select_camera_all).setBackgroundResource(
				R.drawable.search_type);
		findViewById(R.id.select_camera_public).setBackgroundResource(
				R.drawable.search_type_active);
		findViewById(R.id.select_camera_private).setBackgroundResource(
				R.drawable.search_type);
	}
	/**
	 * Shows only Private networks as a list
	 * @param view
	 */
	public void filterPrivate(View view) {
		setListAdapter(this.listPrivate);
		findViewById(R.id.select_camera_all).setBackgroundResource(
				R.drawable.search_type);
		findViewById(R.id.select_camera_public).setBackgroundResource(
				R.drawable.search_type);
		findViewById(R.id.select_camera_private).setBackgroundResource(
				R.drawable.search_type_active);
	}

	/**
	 * Takes an Array of Strings and sets the ListView to pull it's data from
	 * that array.
	 **/
	private void setListAdapter(ArrayList<String> list) {
		ListView listview = (ListView) findViewById(R.id.listview_camera_az);
		listview.setAdapter(new NetworkListArrayAdapter(this,
				android.R.layout.simple_list_item_1, list));
	}

}
