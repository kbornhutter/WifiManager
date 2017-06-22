package com.example.wifimanagerprototype;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


import android.app.Activity;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.os.Handler;

import com.example.wifimanagerprototype.location.ILocationProvider;
import com.example.wifimanagerprototype.location.LocationProvider;
import com.example.wifimanagerprototype.utils.Constants;
import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.ArchitectView.ArchitectConfig;
import com.wikitude.architect.SensorAccuracyChangeListener;
import com.example.wifimanagerprototype.R;

public class BasicArchitectActivity extends Activity {
	
	
	public static final String EXTRAS_KEY_ACTIVITY_TITLE_STRING = "activityTitle";
	public static final String EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL = "activityArchitectWorldUrl";

	/**
	 * holds the AR-view
	 */
	protected ArchitectView					architectView;
	
	/**
	 * sensor accuracy listener in case you want to display calibration hints
	 */
	protected SensorAccuracyChangeListener	sensorAccuracyListener;
	
	/**
	 * last known location of the user, used internally for content-loading after user location was fetched
	 */
	protected Location lastKnownLocaton;

	/**
	 * sample location strategy
	 */
	protected ILocationProvider				locationProvider;

	/**
	 * location listener receives location updates and must forward them to the architectView
	 */
	protected LocationListener locationListener;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate( final Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		/* pressing volume up/down should cause music volume changes */
		this.setVolumeControlStream( AudioManager.STREAM_MUSIC );

		/* set samples content view */
		this.setContentView( R.layout.sample_cam );
		
		this.setTitle(this.getActivityTitle());

		/* set AR-view for life-cycle notifications etc. */
		this.architectView = (ArchitectView)this.findViewById( R.id.architectView );
		this.setTitle("AR Camera");
		this.findViewById(R.id.architectView).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2000));
		final Handler mHandler = new Handler();
		 
        // Create runnable for posting
        final Runnable mUpdateResults = new Runnable() {
            @Override
			public void run() {
            	setSizeofAR(LayoutParams.MATCH_PARENT, 800);
 
            }
        };
 
        int delay = 15000; // delay for 15 sec.
 
        int period = 60000; // repeat every 60 sec.
 
        Timer timer = new Timer();
 
        timer.scheduleAtFixedRate(new TimerTask() {
 
        @Override
		public void run() {
 
             mHandler.post(mUpdateResults);
 
        }
 
        }, delay, period);

		// Wikitude SDK Key for AR API
		final ArchitectConfig config = new ArchitectConfig( Constants.WIKITUDE_SDK_KEY );
		
		// Set SDK Key to AR VIEW
		this.architectView.onCreate( config );

		this.sensorAccuracyListener = new SensorAccuracyChangeListener() {
			@Override
			public void onCompassAccuracyChanged( int accuracy ) {
				/* UNRELIABLE = 0, LOW = 1, MEDIUM = 2, Height = 3 */
				if ( accuracy < SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM && BasicArchitectActivity.this != null && !BasicArchitectActivity.this.isFinishing() ) {
					Toast.makeText( BasicArchitectActivity.this, "Low", Toast.LENGTH_LONG ).show();
				}
			}
		};

		this.locationListener = new LocationListener() {

			@Override
			public void onStatusChanged( String provider, int status, Bundle extras ) {
			}

			@Override
			public void onProviderEnabled( String provider ) {
			}

			@Override
			public void onProviderDisabled( String provider ) {
			}

			@Override
			public void onLocationChanged( final Location location ) {
				if (location!=null) {
					BasicArchitectActivity.this.lastKnownLocaton = location;
				if ( BasicArchitectActivity.this.architectView != null ) {
					if ( location.hasAltitude() ) {
						BasicArchitectActivity.this.architectView.setLocation( location.getLatitude(), location.getLongitude(), location.getAltitude(), location.hasAccuracy() ? location.getAccuracy() : 1000 );
					} else {
						BasicArchitectActivity.this.architectView.setLocation( location.getLatitude(), location.getLongitude(), location.hasAccuracy() ? location.getAccuracy() : 1000 );
					}
				}
				} 
			}
		};
		
		this.architectView.registerSensorAccuracyChangeListener( this.sensorAccuracyListener );
		this.locationProvider = new LocationProvider( this, this.locationListener );
	}

	@Override
	protected void onResume() {
		super.onResume();
		if ( this.architectView != null ) {
			this.architectView.onResume();
		}

		if ( this.locationProvider != null ) {
			this.locationProvider.onResume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if ( this.architectView != null ) {
			this.architectView.onPause();
		}
		if ( this.locationProvider != null ) {
			this.locationProvider.onPause();
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if ( this.architectView != null ) {
			if ( this.sensorAccuracyListener != null ) {
				this.architectView.unregisterSensorAccuracyChangeListener( this.sensorAccuracyListener );
			}
			this.architectView.onDestroy();
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		if ( this.architectView != null ) {
			this.architectView.onLowMemory();
		}
	}


	@Override
	protected void onPostCreate( final Bundle savedInstanceState ) {
		super.onPostCreate( savedInstanceState );
		if ( this.architectView != null ) {
			this.architectView.onPostCreate();
		}

		try {
			this.architectView.load( getARchitectWorldPath() );
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	

	/**
	 * path to the architect-file (AR-Experience HTML) to launch
	 * @return
	 */
	public String getARchitectWorldPath() {
		return "samples/5_Browsing$Pois_2_Poi$Radar/index.html";

	} public void setSizeofAR(int width, int height) {
		this.findViewById(R.id.architectView).setLayoutParams(new LinearLayout.LayoutParams(width,height));

	}
	
	public String getActivityTitle() {
		return (getIntent().getExtras()!=null && getIntent().getExtras().get(EXTRAS_KEY_ACTIVITY_TITLE_STRING)!=null) ? getIntent().getExtras().getString(EXTRAS_KEY_ACTIVITY_TITLE_STRING) : "Test-World";
	}

	

}