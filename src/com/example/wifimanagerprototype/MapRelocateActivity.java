package com.example.wifimanagerprototype;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
/** 
 * Incomplete Relocation class - May be removed in future development
 *
 *
 */
public class MapRelocateActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_relocate);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**@Override
	public void onAttachedToWindow() {
	    openOptionsMenu(); 
	}**/
	
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
                return true;
        } else if (item.getItemId() == R.id.action_settings) {
            	startActivity(new Intent(this, SettingsActivity.class));
                return true;
        } else return super.onOptionsItemSelected(item);
        
    }

}
