package com.example.wifimanagerprototype;

import java.util.ArrayList;

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
/**
 * 
 * Displays Favorite APs. User is able to select one to show compass.
 *
 */
public class CompassFavouritesActivity extends Activity {
	private final ArrayList<String> listAll = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compass_favourites);
		// List of APs = Public + Private
		ListView listview = (ListView) findViewById(R.id.listview_compass_favorite);
		this.listAll.add(getString(R.string.network_eduroam));
		this.listAll.add(getString(R.string.network_wifi));
		
		listview.setAdapter(new NetworkListArrayAdapter(this, android.R.layout.simple_list_item_1, this.listAll));
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//final String item = (String) parent.getItemAtPosition(position);
				loadHeatmap();
			}
		});
		// Scroll listener
		listview.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                }
                return false;
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
	 * Handles settings as selected by user.
	 */
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_home) {
	    		startActivity(new Intent(this, MainActivity.class));
	    		return true;
        } else if (item.getItemId() == R.id.action_az) {
            	startActivity(new Intent(this, CompassAZActivity.class));
            	return true;
        } else if (item.getItemId() == R.id.action_favorite) {
            	startActivity(new Intent(this, CompassFavouritesActivity.class));
        } else if (item.getItemId() == R.id.action_settings) {
            	startActivity(new Intent(this, SettingsActivity.class));
                return true;
        } else return super.onOptionsItemSelected(item);
        return true; // check this.
	}
	
	public void loadHeatmap() {
    	startActivity(new Intent(this, CompassActivity.class));
    }

}
