package com.example.wifimanagerprototype;

import java.util.List;

import com.example.wifimanagerprototype.MainActivity.MyLocationListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class CompassActivity extends Activity {
	private Activity activity;
	private static SensorManager sensorService;
	private Sensor sensor;
	private Sensor sensorAccel;
	private Sensor sensorMagn;
	private Matrix mtx;

	@Override
	/**
	 * Creates Compass -  Points towards selected AP from CompassAZActivity class.
	 * 
	 */
	protected void onCreate(Bundle savedInstanceState) {
		final double lon = 0;
		final double lat = 0;
		double longitude = 0;
		double latitude = 0;
		final float azimuth = 0;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compass);
		ImageView arrow = (ImageView) findViewById(R.id.compass_arrow);
		Bitmap arrowImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.prototype_arrow);
		int w = arrowImage.getWidth();
		int h = arrowImage.getHeight();
		mtx = new Matrix();
		final String chosenAP = CompassAZActivity.chosenAP;
		// Database for APs
		ParseQuery<ParseObject> query = ParseQuery.getQuery("TestObject");

		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					for (ParseObject entry : objects) {
						if (entry.has(chosenAP)) {
							double lon = entry.getDouble("Long");
							double lat = entry.getDouble("Lat");
							break;
						}
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"Data retrievel Unsuccessful", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		final class MyLocationListener implements LocationListener {
			double mLat = 0, mLong = 0;

			@Override
			/**
			 * Changes direction of compass upon location change
			 */
			public void onLocationChanged(Location locFromGps) {
				mLat = locFromGps.getLatitude();
				mLong = locFromGps.getLongitude();
				double dLat = Math.toRadians(lat - mLat);
				double dLon = Math.toRadians(lon - mLong);
				mLat = Math.toRadians(mLat);
				double lat2 = Math.toRadians(lat);

				double y = Math.sin(dLon) * Math.cos(lat2);
				double x = Math.cos(mLat) * Math.sin(lat2) - Math.sin(mLat)
						* Math.cos(lat2) * Math.cos(dLon);

				double bearing = Math.toDegrees(Math.atan2(y, x));

				ImageView arrow = (ImageView) findViewById(R.id.compass_arrow);
				Bitmap arrowImage = BitmapFactory.decodeResource(
						getResources(), R.drawable.prototype_arrow);
				int width = arrowImage.getWidth();
				int height = arrowImage.getHeight();

				mtx.postRotate((float) bearing);
				// Rotate Compass Arrow
				Bitmap rotatedArrow = Bitmap.createBitmap(arrowImage, 0, 0,
						width, height, mtx, true);
				BitmapDrawable arrowBit = new BitmapDrawable(rotatedArrow);

				arrow.setImageDrawable(arrowBit);

			}

			@Override
			/**
			 *  Unimplemented Status Changed Listener
			 */
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				

			}

			@Override
			/**
			 * Unimplemented Provider listener
			 */
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			/**
			 * Unimplemented Provider listener
			 */
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}
		}

		SensorEventListener mySensorEventListener = new SensorEventListener() {

			@Override
			/**
			 *  Sensor Accuracy Listener - useful to use another method or provide
			 *  notification of lower location accuracy
			 */
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}

			@Override
			/**
			 * Sensor Listener - Used to update direction of arrow
			 * 
			 */
			public void onSensorChanged(SensorEvent event) {
				// TODO Auto-generated method stub
				float azimuth = event.values[0];
				ImageView arrow = (ImageView) findViewById(R.id.compass_arrow);
				Bitmap arrowImage = BitmapFactory.decodeResource(
						getResources(), R.drawable.prototype_arrow);
				int width = arrowImage.getWidth();
				int height = arrowImage.getHeight();
				// Matrix mtx = new Matrix();
				mtx.postRotate((azimuth));
				// Rotate Compass Arrow
				Bitmap rotatedArrow = Bitmap.createBitmap(arrowImage, 0, 0,
						width, height, mtx, true);
				BitmapDrawable arrowBit = new BitmapDrawable(rotatedArrow);

				arrow.setImageDrawable(arrowBit);
			}
		};
		sensorService = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorAccel = sensorService.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorMagn = sensorService.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		sensorService.registerListener(mySensorEventListener, sensorAccel,
				SensorManager.SENSOR_DELAY_NORMAL);
		sensorService.registerListener(mySensorEventListener, sensorMagn,
				SensorManager.SENSOR_DELAY_NORMAL);

		LocationListener locList = new MyLocationListener();
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1,
				locList);
		double mLat = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
				.getLatitude();
		double mLong = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
				.getLongitude();
		double dLat = Math.toRadians(lat - mLat);
		double dLon = Math.toRadians(lon - mLong);
		mLat = Math.toRadians(mLat);
		double lat2 = Math.toRadians(lat);

		double y = Math.sin(dLon) * Math.cos(lat2);
		double x = Math.cos(mLat) * Math.sin(lat2) - Math.sin(mLat)
				* Math.cos(lat2) * Math.cos(dLon);

		double bearing = Math.toDegrees(Math.atan2(y, x));
		if (bearing < 0) {
			bearing = 360 - Math.abs(bearing);
		}
		arrow = (ImageView) findViewById(R.id.compass_arrow);
		arrowImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.prototype_arrow);
		int width = arrowImage.getWidth();
		int height = arrowImage.getHeight();
		Matrix mtx = new Matrix();
		mtx.postRotate((float) bearing);
		// Rotate Compass Arrow
		Bitmap rotatedArrow = Bitmap.createBitmap(arrowImage, 0, 0, width,
				height, mtx, true);
		BitmapDrawable arrowBit = new BitmapDrawable(rotatedArrow);

		arrow.setImageDrawable(arrowBit);
		this.activity = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_no_relocate, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_home) {
			startActivity(new Intent(this, MainActivity.class));
			return true;
		} else if (item.getItemId() == R.id.action_az) {
			startActivity(new Intent(this, CompassAZActivity.class));
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

	public Activity mapGetActivity() {
		return this.activity;
	}

	public void loadInfo(View view) {
		DialogFragment newFragment = new InfoPaneFragment();
		newFragment.show(getFragmentManager(), "missiles");
	}

	@SuppressLint("ValidFragment")
	private class InfoPaneFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					mapGetActivity());
			builder.setMessage(getString(R.string.info_compass)).setTitle(
					"Info");
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
