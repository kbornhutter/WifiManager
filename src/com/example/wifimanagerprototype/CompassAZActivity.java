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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class CompassAZActivity extends Activity {
	private final ArrayList<String> listAll = new ArrayList<String>();
	private final ArrayList<String> listPublic = new ArrayList<String>();
	private final ArrayList<String> listPrivate = new ArrayList<String>();
	static String chosenAP;

	@Override
	/**
	 * Creates list of available APs for selection
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compass_az);

		ListView listview = (ListView) findViewById(R.id.listview_compass_az);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("TestObject");

		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					Toast.makeText(getApplicationContext(),
							objects.size() + " Access Points Found",
							Toast.LENGTH_LONG).show();
					// Queries database for all networks available minus repeats
					for (ParseObject entry : objects) {
						if (!listAll.contains(entry.getString("SSID"))) {
							listAll.add(entry.getString("SSID"));
							if ((entry.getString("Enc").length() == 5)) {
								listPublic.add((entry.getString("SSID")));

							} else {
								listPrivate.add((entry.getString("SSID")));
							}
						}
					}
					View b = findViewById(R.id.progressBar1);
					b.setVisibility(View.GONE);
					filterAll(findViewById(R.id.select_compass_all));
				} else {
					Toast.makeText(getApplicationContext(),
							"Data retrievel Unsuccessful", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		// Listener used to scroll 
		listview.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					return false;
				}
				return false;
			}

		});
		listview.setAdapter(new NetworkListArrayAdapter(this,
				android.R.layout.simple_list_item_1, this.listAll));
		// Listener used for AP selection
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
	/**
	 * Options menu inflates if selected
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_no_relocate, menu);
		return true;
	}

	@Override
	/**
	 * Handler for settings selection
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_home) {
			startActivity(new Intent(this, MainActivity.class));
			return true;
		} else if (item.getItemId() == R.id.action_az) {
			return true;
		} else if (item.getItemId() == R.id.action_favorite) {
			startActivity(new Intent(this, CompassFavouritesActivity.class));
			return true;
		} else if (item.getItemId() == R.id.action_settings) {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		} else
			return super.onOptionsItemSelected(item);

	}
	/**
	 * Loads heat map
	 * @param view
	 */
	public void loadHeatmap(View view) {
		startActivity(new Intent(this, CompassActivity.class));
	}
	/**
	 * Displays complete AP list - both public and private networks.
	 * @param view
	 */
	public void filterAll(View view) {
		setListAdapter(this.listAll);
		findViewById(R.id.select_compass_all).setBackgroundResource(
				R.drawable.search_type_active);
		findViewById(R.id.select_compass_public).setBackgroundResource(
				R.drawable.search_type);
		findViewById(R.id.select_compass_private).setBackgroundResource(
				R.drawable.search_type);
	}
	/**
	 * Displays only Public AP networks
	 * @param view
	 */
	public void filterPublic(View view) {
		setListAdapter(this.listPublic);
		findViewById(R.id.select_compass_all).setBackgroundResource(
				R.drawable.search_type);
		findViewById(R.id.select_compass_public).setBackgroundResource(
				R.drawable.search_type_active);
		findViewById(R.id.select_compass_private).setBackgroundResource(
				R.drawable.search_type);
	}
	/**
	 * Displays only Private AP networks
	 * @param view
	 */
	public void filterPrivate(View view) {
		setListAdapter(this.listPrivate);
		findViewById(R.id.select_compass_all).setBackgroundResource(
				R.drawable.search_type);
		findViewById(R.id.select_compass_public).setBackgroundResource(
				R.drawable.search_type);
		findViewById(R.id.select_compass_private).setBackgroundResource(
				R.drawable.search_type_active);
	}

	/**
	 * Takes an Array of Strings and sets the ListView to pull it's data from
	 * that array.
	 **/
	private void setListAdapter(ArrayList<String> list) {
		ListView listview = (ListView) findViewById(R.id.listview_compass_az);
		listview.setAdapter(new NetworkListArrayAdapter(this,
				android.R.layout.simple_list_item_1, list));
	}

}
