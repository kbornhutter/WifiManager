package com.example.wifimanagerprototype;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
/**
 * Updates List for the use in all classes. Not currently used but has been included
 * for use in future development. 
 */
/** Code from http://www.vogella.com/articles/AndroidListView/article.html **/
public class NetworkListArrayAdapter extends ArrayAdapter<String> {
	HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
	List<String> data = null;

    public NetworkListArrayAdapter(Context context, int textViewResourceId,
    		List<String> objects) {
    	super(context, textViewResourceId, objects);
    	this.data = objects;
    	for (int i = 0; i < objects.size(); ++i) {
    		mIdMap.put(objects.get(i), i);
    	}
    }

    @Override
    public long getItemId(int position) {
    	String item = getItem(position);
    	return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
    	return true;
    }
    
    /** This stuff doesn't work :( **/
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder holder = null;
    	//LayoutInflater inflator = getLayoutInflater();
    	LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	if(convertView == null) {
    		convertView = inflater.inflate(R.layout.list_networks, null, false);
    		holder = new ViewHolder(convertView);
    		convertView.setTag(holder);
    	} else {
    		holder = (ViewHolder) convertView.getTag();
    	}
    	holder.getMainText().setText(data.get(position));
    	holder.getDistanceText().setText(getContext().getString(R.string.network_distance));
    	return convertView;
    }
    
	private class ViewHolder {
		private View row;
		private TextView mainText = null, distanceText = null;

		public ViewHolder(View row) {
			this.row = row;
		}

		public TextView getMainText() {
			if (this.mainText == null) {
				this.mainText = (TextView) this.row.findViewById(R.id.list_text);
			}
			return this.mainText;
		}

		public TextView getDistanceText() {
			if (this.distanceText == null) {
				this.distanceText = (TextView) this.row.findViewById(R.id.list_distance);
			}
			return this.distanceText;
		}
	}
}