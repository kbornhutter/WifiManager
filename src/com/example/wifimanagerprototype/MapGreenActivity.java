package com.example.wifimanagerprototype;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

public class MapGreenActivity extends Activity {
	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_green);
		
		this.activity = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onHeatmapCheck(View view) {
		CheckBox checkbox = (CheckBox) findViewById(R.id.checkbox_heatmap);
		ImageView imagePrototype = (ImageView) findViewById(R.id.prototype_image);
		if(checkbox.isChecked()) {
			Drawable d = getResources().getDrawable(R.drawable.prototype_map_display);
			imagePrototype.setImageDrawable(d);
		} else {
			Drawable d = getResources().getDrawable(R.drawable.prototype_map_display_dots);
			imagePrototype.setImageDrawable(d);
		}
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
    		AlertDialog.Builder builder = new AlertDialog.Builder(mapGetActivity());
    		builder.setMessage(getString(R.string.info_map))
    			   .setTitle("Info");
    		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    	           @Override
				public void onClick(DialogInterface dialog, int id) {
    	               // User clicked OK button
    	           }
    	       });
			return builder.create();
    	}
    }

}
