package com.example.wifimanagerprototype;

import org.json.JSONArray;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.example.wifimanagerprototype.location.LocationProvider;
import com.example.wifimanagerprototype.utils.GeoUtils;

/**
 * Creates camera for AR display of APs
 *
 */
public class CameraActivity extends BasicArchitectActivity {

	protected JSONArray poiData;

	@Override
	/**
	 * Instantiated when Camera is selected. Sets Architect View (AR Class)
	 * 
	 */
	
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.locationListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			@Override
			public void onProviderEnabled(String provider) {
			}

			@Override
			public void onProviderDisabled(String provider) {
			}

			@Override
			/**
			 * Upon location change -> Update current location for display of AR view
			 */
			public void onLocationChanged(final Location location) {
				if (location != null) {
					CameraActivity.this.lastKnownLocaton = location;
					if (CameraActivity.this.architectView != null) {
						if (location.hasAltitude()) {
							CameraActivity.this.architectView.setLocation(
									location.getLatitude(),
									location.getLongitude(),
									location.getAltitude(),
									location.getAccuracy());
						} else {
							CameraActivity.this.architectView.setLocation(
									location.getLatitude(),
									location.getLongitude(),
									location.getAccuracy());
						}
					}
				}
			}
		};

		this.architectView
				.registerSensorAccuracyChangeListener(this.sensorAccuracyListener);
		this.locationProvider = new LocationProvider(this,
				this.locationListener);
	}

	@Override
	protected void onPostCreate(final Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		this.loadData();

	}

	boolean isLoading = false;

	final Runnable loadData = new Runnable() {

		@Override
		public void run() {

			isLoading = true;

			final int WAIT_FOR_LOCATION_STEP_MS = 2000;

			while (CameraActivity.this.lastKnownLocaton == null
					&& !CameraActivity.this.isFinishing()) {

				CameraActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(CameraActivity.this,
								"Finding your location", Toast.LENGTH_SHORT)
								.show();
					}
				});

				try {
					Thread.sleep(WAIT_FOR_LOCATION_STEP_MS);
				} catch (InterruptedException e) {
					break;
				}
			}

			if (CameraActivity.this.lastKnownLocaton != null
					&& !CameraActivity.this.isFinishing()) {
				// Collect Location info from AP database
				poiData = GeoUtils.getPoiInformation(
						CameraActivity.this.lastKnownLocaton, 20);
				CameraActivity.this.callJavaScript(
						"World.loadPoisFromJsonData",
						new String[] { poiData.toString() });

			}

			isLoading = false;
		}
	};

	protected void loadData() {
		if (!isLoading) {
			final Thread t = new Thread(loadData);
			t.start();
		}
	}

	/**
	 * call JacaScript in architectView
	 * 
	 * @param methodName
	 * @param arguments
	 */
	private void callJavaScript(final String methodName,
			final String[] arguments) {
		final StringBuilder argumentsString = new StringBuilder("");
		for (int i = 0; i < arguments.length; i++) {
			argumentsString.append(arguments[i]);
			if (i < arguments.length - 1) {
				argumentsString.append(", ");
			}
		}

		if (this.architectView != null) {
			final String js = (methodName + "( " + argumentsString.toString() + " );");
			this.architectView.callJavascript(js);
		}
	}

}