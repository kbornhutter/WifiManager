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
 * Incomplete Favorites Management Class
 *
 */
public class MapFavoriteActivity extends Activity {
	private final ArrayList<String> listAll = new ArrayList<String>();
	private final ArrayList<String> listPublic = new ArrayList<String>();
	private final ArrayList<String> listPrivate = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_favorite);
		
		ListView listview = (ListView) findViewById(R.id.listview_favorite);
		this.listAll.add(getString(R.string.network_eduroam));
		this.listAll.add(getString(R.string.network_wifi));
		this.listPublic.add(getString(R.string.network_wifi));
		this.listPrivate.add(getString(R.string.network_eduroam));
		
		listview.setAdapter(new NetworkListArrayAdapter(this, android.R.layout.simple_list_item_1, this.listAll));
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//final String item = (String) parent.getItemAtPosition(position);
				loadHeatmap(null);
			}
		});
		listview.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                }
                return false;
            }

        });
		
		/*ListView listview = (ListView) findViewById(R.id.list_favorites);
		final ArrayList<String> list = new ArrayList<String>();
		list.add("Eduroam");
		list.add("WiFi");
		
		listview.setAdapter(new StableArrayAdapter(this, android.R.layout.simple_list_item_1, list));
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//final String item = (String) parent.getItemAtPosition(position);
			}
		});*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
                return true;
        } else if (item.getItemId() == R.id.action_relocate) {
        		startActivity(new Intent(this, MapRelocateActivity.class));
                return true;
        } else if (item.getItemId() == R.id.action_settings) {
            	startActivity(new Intent(this, SettingsActivity.class));
                return true;
        } else return super.onOptionsItemSelected(item);
        
    }
	
	public void loadHeatmap(View view) {
    	startActivity(new Intent(this, MapGreenActivity.class));
    }

}
