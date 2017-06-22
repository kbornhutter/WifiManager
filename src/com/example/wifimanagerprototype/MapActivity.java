package com.example.wifimanagerprototype;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseObject;

/**
 *  Displays AP data on a Map. Coagulates the networks so that only the strongest points
 *  are shown.
 **/

public class MapActivity extends Activity {
	// Location of UQ central -> Map zooms in upon creation
	static final LatLng STLUCIACENTRE = new LatLng(-27.497356, 153.013317);
	static final ArrayList<ParseObject> APCoagulation = new ArrayList<ParseObject>();
	// Map to display AP data
	private GoogleMap map;
	private Activity activity;

	@SuppressLint("NewApi")
	@Override
	/**
	 * Creates Map for displaying AP data. Collects DB info.
	 * 
	 */
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		this.activity = this;

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		map.setMyLocationEnabled(true);

		ParseQuery<ParseObject> query = ParseQuery.getQuery("TestObject");

		// Query AP Database for Networks - add to map
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					for (ParseObject entry : objects) {
						if (!APCoagulation.isEmpty()) {
							for (ParseObject AP : APCoagulation) {
								// Only select AP with highest signal strength
								if (entry.getString("SSID").equals(
										AP.getString("SSID"))) {
									if (Double.valueOf(entry
											.getString("Signal")) > Double
											.valueOf(AP.getString("Signal"))) {
										APCoagulation.remove(AP);
										APCoagulation.add(entry);
										break;

									}
								} else if (APCoagulation.indexOf(AP) == APCoagulation
										.size() - 1) {
									APCoagulation.add(entry);
								}
							}
						} else {
							APCoagulation.add(entry);
						}
					}
					// Add Access Points to Map
					for (ParseObject APentry : APCoagulation) {
						map.addMarker(new MarkerOptions()
								.position(
										new LatLng(APentry.getDouble("Lat"),
												APentry.getDouble("Long")))
								.title(APentry.getString("SSID"))
								.snippet(
										"Signal: "
												+ APentry.getString("Signal")
												+ "dB"));
					}
					Toast.makeText(getApplicationContext(),
							APCoagulation.size() + " Access Points Found",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(),
							"Data retrievel Unsuccessful", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(STLUCIACENTRE, 16));

		// Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);

		map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker marker) {
				loadHeatmap(null);
			}
		});
	}

	@Override
	/**
	 * Creates option menu
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public Activity mapGetActivity() {
		return this.activity;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_home) {
			startActivity(new Intent(this, MainActivity.class));
			return true;
		} else if (item.getItemId() == R.id.action_az) {
			startActivity(new Intent(this, MapAZActivity.class));
			return true;
		} else if (item.getItemId() == R.id.action_favorite) {
			startActivity(new Intent(this, MapFavoriteActivity.class));
			return true;
		} else if (item.getItemId() == R.id.action_relocate) {
			startActivity(new Intent(this, MapRelocateActivity.class));
			return true;
		} else if (item.getItemId() == R.id.action_settings) {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		} else
			return super.onOptionsItemSelected(item);

	}

	public void loadHeatmap(View view) {
		startActivity(new Intent(this, MapGreenActivity.class));
	}

	public void loadInfo(View view) {

		DialogFragment newFragment = new InfoPaneFragment();
		newFragment.show(getFragmentManager(), "missiles");
	}

	@SuppressLint("ValidFragment")
	class InfoPaneFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(getString(R.string.info_map)).setTitle("Info");
			builder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							// User clicked OK button
						}
					});
			return builder.create();
		}
	}
}
