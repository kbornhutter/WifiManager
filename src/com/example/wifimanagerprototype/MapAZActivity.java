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
 * Displays AR data in a list. Sorts depending on encryption (Public/Private)
 *
 */
public class MapAZActivity extends Activity {
	// Lists to hold AP data 
	private final ArrayList<String> listAll = new ArrayList<String>();
	private final ArrayList<String> listPublic = new ArrayList<String>();
	private final ArrayList<String> listPrivate = new ArrayList<String>();

	@Override
	/**
	 * Creates List of APs. Handles selection.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_az);
		
		ListView listview = (ListView) findViewById(R.id.listview_az);
		listview.setAdapter(new NetworkListArrayAdapter(this, android.R.layout.simple_list_item_1, this.listAll));
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	/**
	 * Handles selection of options. Creates new instance of activity class if needed.
	 */
    public boolean onOptionsItemSelected(MenuItem item) {
       if (item.getItemId() == R.id.action_home) {
        		startActivity(new Intent(this, MainActivity.class));
        		return true;
       } else if (item.getItemId() == R.id.action_az) {
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
       } else return super.onOptionsItemSelected(item);
        
    }
	
	public void loadHeatmap(View view) {
    	startActivity(new Intent(this, MapGreenActivity.class));
    }
	/**
	 * Filters AP - Creates list that includes Public and Private networks
	 * @param view
	 */
	public void filterAll(View view) {
		setListAdapter(this.listAll);
		findViewById(R.id.select_all).setBackgroundResource(R.drawable.search_type_active);
		findViewById(R.id.select_public).setBackgroundResource(R.drawable.search_type);
		findViewById(R.id.select_private).setBackgroundResource(R.drawable.search_type);
	}
	/**
	 * Filters AP - Creates list containing only Public networks
	 * @param view
	 */
	public void filterPublic(View view) {
		setListAdapter(this.listPublic);
		findViewById(R.id.select_all).setBackgroundResource(R.drawable.search_type);
		findViewById(R.id.select_public).setBackgroundResource(R.drawable.search_type_active);
		findViewById(R.id.select_private).setBackgroundResource(R.drawable.search_type);
	}
	/**
	 * Filters AP - Creates list containing only Private networks
	 * @param view
	 */
	public void filterPrivate(View view) {
		setListAdapter(this.listPrivate);
		findViewById(R.id.select_all).setBackgroundResource(R.drawable.search_type);
		findViewById(R.id.select_public).setBackgroundResource(R.drawable.search_type);
		findViewById(R.id.select_private).setBackgroundResource(R.drawable.search_type_active);
	}
	
	/** Takes an Array of Strings and sets the ListView to pull it's data from
	 ** that array.
	 **/
	private void setListAdapter(ArrayList<String> list) {
		// Add some rows

		ListView listview = (ListView) findViewById(R.id.listview_az);
		listview.setAdapter(new NetworkListArrayAdapter(this, android.R.layout.simple_list_item_1, list));
	}
	// Not used but can be used to keep a static reference to cloud DB
	private class Network<T,D> {
		private T text;
		private D distance;
		
		public Network(T text, D distance) {
			this.text = text;
			this.distance = distance;
		}
		
		public T getText() {return this.text;}
		public D getDistance() {return this.distance;}
		public void setText(T text) {this.text = text;}
		public void setDistance(D distance) {this.distance = distance;}
	}

}
