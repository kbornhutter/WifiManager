package com.example.wifimanagerprototype;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.location.LocationManager;
import android.location.LocationListener;
import android.location.Location;
import android.content.Context;

public class MainActivity extends Activity {

	private LocationListener locationListener;

	@Override
	/**
	 * Creates the holder that will contain the entire application. Loads AP database
	 * and initializes AR API.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		// Initialize database with ID key
		Parse.initialize(this, "YV0COcCgZqTHg3STZaOuXBTemQpOaUrfTiiazddN",
				"Melrmbzi25X5qxwz6gsn8ufzzFuTU77BIie2Cnwg");
		// Cloud Database for Storing AP data with Location
		ParseAnalytics.trackAppOpened(getIntent());
		ParseObject wifiDB = new ParseObject("TestObject");

		double mLat = 0, mLong = 0;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		String wifi = wifiManager.getScanResults().toString();
		GPSTracker mGPS = new GPSTracker(this);

		if (mGPS.canGetLocation) {
			mLat = mGPS.getLatitude();
			mLong = mGPS.getLongitude();
		} else {
			Toast.makeText(getApplicationContext(), "No GPS Signal",
					Toast.LENGTH_LONG).show();
		} //
		System.out.println(wifi);
		int count = 0;
		// Scan for new wireless networks and add to DB
		for (ScanResult AP : wifiManager.getScanResults()) {
			System.out.println(AP.SSID);
			wifiDB.put("Signal", "" + AP.level);
			wifiDB.put("SSID", AP.SSID);
			wifiDB.put("Lat", mLat);
			wifiDB.put("Long", mLong);
			wifiDB.put("Enc", AP.capabilities);
			wifiDB.saveInBackground();
			count++;
			// break;
		}
		Toast.makeText(getApplicationContext(), count + " Nearby Networks",
				Toast.LENGTH_LONG).show();
		LocationListener locList = new MyLocationListener();
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3500, 50,
				locList);

	}
	/**
	 * Location listener for change of location.Notify user of change and
	 * scan for additional APs. 
	 *
	 */
	final class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location locFromGps) {
			// called when the listener is notified with a location update from
			// the GPS
			ParseObject testObject = new ParseObject("TestObject");
			// CLOUD DATABASE ABOVE
			double mLat = 0, mLong = 0;
			setContentView(R.layout.activity_main);
			WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
			wifiManager.startScan();
			String wifi = wifiManager.getScanResults().toString();

			if (locFromGps != null) {
				mLat = locFromGps.getLatitude();
				mLong = locFromGps.getLongitude();
			} else {
				Toast.makeText(getApplicationContext(), "No GPS Signal",
						Toast.LENGTH_LONG).show();
			}
			Toast.makeText(getApplicationContext(), "Location Changed",
					Toast.LENGTH_LONG).show();
			// Add AP Scan Results to Cloud AP Daatabase
			for (ScanResult AP : wifiManager.getScanResults()) {
				System.out.println(AP.SSID);
				testObject.put("Signal", "" + AP.level);
				testObject.put("SSID", AP.SSID);
				testObject.put("Lat", mLat);
				testObject.put("Long", mLong);
				testObject.put("Enc", AP.capabilities);
				testObject.saveInBackground();
				
			}
		}

		@Override
		/**
		 * Called when the GPS provider is turned off (user turning off the
		 * GPS on the phone)
		 */
		public void onProviderDisabled(String provider) {
			// Unimplemented
		}

		@Override
		/**
		 * Called when the GPS provider is turned on (user turning on the 
		 * GPS on the phone)
		 * 
		 */
		public void onProviderEnabled(String provider) {
			// Unimplemented
		}

		@Override
		/**
		 * Called when the status of the GPS provider changes
		 */
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// Unimplemented
		}
	}
	@Override
	/**
	 * Inflate the menu and add items to action bar if present
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	/**
	 * Handles selection of Settings
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_settings) {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		} else
			return super.onOptionsItemSelected(item);

	}
	/**
	 * Initializes Map for displaying APs
	 * @param view
	 */
	public void mapClickEvent(View view) {
		startActivity(new Intent(this, MapActivity.class));
	}
	/**
	 * Initializes Compass for providing direction to chosen AP
	 * @param view
	 */
	public void compassClickEvent(View view) {
		startActivity(new Intent(this, CompassAZActivity.class));
	}
	/**
	 * Initializes Camera AR View - shows networks through lens
	 * @param view
	 */
	public void cameraClickEvent(View view) {
		startActivity(new Intent(this, CameraActivity.class));
	}
}
