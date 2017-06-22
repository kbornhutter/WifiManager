package com.example.wifimanagerprototype.utils;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.location.Location;

public class GeoUtils {

	/**
	 * loads poiInformation and returns them as JSONArray. Ensure attributeNames
	 * of JSON POIs are well known in JavaScript, so you can parse them easily
	 * 
	 * @param userLocation
	 *            the location of the user
	 * @param numberOfPlaces
	 *            number of places to load (at max)
	 * @return POI information in JSONArray
	 */
	public static JSONArray getPoiInformation(final Location userLocation,
			final int numberOfPlaces) {

		if (userLocation == null) {
			return null;
		}

		final JSONArray pois = new JSONArray();
		final String ATTR_ID = "id";
		final String ATTR_NAME = "name";
		final String ATTR_DESCRIPTION = "description";
		final String ATTR_LATITUDE = "latitude";
		final String ATTR_LONGITUDE = "longitude";
		final String ATTR_ALTITUDE = "altitude";
		// Load Cloud AP Database
		final ArrayList<ParseObject> APCoagulation = new ArrayList<ParseObject>();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("TestObject");
		// Try query and add to Geo List
		try {
			ArrayList<ParseObject> objects = (ArrayList<ParseObject>) query
					.find();
			for (ParseObject entry : objects) {
				if (!APCoagulation.isEmpty()) {
					for (ParseObject AP : APCoagulation) {
						if (entry.getString("SSID")
								.equals(AP.getString("SSID"))) {
							if (Double.valueOf(entry.getString("Signal")) > Double
									.valueOf(AP.getString("Signal"))) {
								APCoagulation.remove(AP);
								APCoagulation.add(entry);
								break;

							}
						} else if (APCoagulation.indexOf(AP) == APCoagulation
								.size() - 1) {
							APCoagulation.add(entry);
						}
					}
				} else {
					APCoagulation.add(entry);
				}
			}
			int i = 0;
			for (ParseObject APentry : APCoagulation) {
				HashMap<String, String> poiInformation = new HashMap<String, String>();
				poiInformation.put(ATTR_ID, APentry.getString("SSID"));
				poiInformation.put(ATTR_NAME, APentry.getString("SSID"));
				poiInformation.put(ATTR_DESCRIPTION, APentry.getString("SSID")
						+ " " + "Signal: Strong");
				double[] poiLocationLatLon = { APentry.getDouble("Lat"),
						APentry.getDouble("Long") };
				poiInformation.put(ATTR_LATITUDE,
						String.valueOf(poiLocationLatLon[0]));
				poiInformation.put(ATTR_LONGITUDE,
						String.valueOf(poiLocationLatLon[1]));
				poiInformation.put(ATTR_ALTITUDE, String.valueOf(100f));
				pois.put(new JSONObject(poiInformation));
				i++;
			}
		} catch (NumberFormatException e) {
			// e
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		}
		return pois;
	}

	/**
	 * helper for creation of dummy places.
	 * 
	 * @param lat
	 *            center latitude
	 * @param lon
	 *            center longitude
	 * @return lat/lon values in given position's vicinity
	 */
	private static double[] getRandomLatLonNearby(final double lat,
			final double lon) {
		return new double[] { lat + Math.random() / 5 - 0.1,
				lon + Math.random() / 5 - 0.1 };
	}
}
