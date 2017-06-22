package com.example.wifimanagerprototype;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DrawSurfaceView extends View {
	Point me = new Point(-33.870932d, 151.204727d, "Me");
	Paint mPaint = new Paint();
	Matrix transform = new Matrix();
	private double OFFSET = 0d;
	private double screenWidth, screenHeight = 0d;
	private Bitmap[] mSpots, mBlips;
	private Bitmap mRadar;
	static String security;
	static final ArrayList<ParseObject> APCoagulation = new ArrayList<ParseObject>();
	public static ArrayList<Point> props = new ArrayList<Point>();
	static {
		props.add(new Point(90d, 110.8000, ""));
		props.add(new Point(-90d, -110.8000, ""));
		props.add(new Point(-33.870932d, 151.8000, ""));
		props.add(new Point(-33.870932d, 150.8000, ""));
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("TestObject");
		
		query.findInBackground(new FindCallback<ParseObject>() {
		     @Override
			public void done(List<ParseObject> objects, ParseException e) {
		         if (e == null) {
	        		 //Toast.makeText(getApplicationContext(),objects.size() + " Access Points Found", Toast.LENGTH_LONG).show();
		        	 for (ParseObject entry : objects) {
							if (!APCoagulation.isEmpty()) {
									for (ParseObject AP : APCoagulation) {
										if (entry.getString("SSID").equals(
												AP.getString("SSID"))) {
											if (Double.valueOf(entry
													.getString("Signal")) > Double
													.valueOf(AP.getString("Signal"))) {
												APCoagulation.remove(AP);
												APCoagulation.add(entry);
												break;
												
										} 
									} else if (APCoagulation.indexOf(AP) == APCoagulation.size() - 1) {
										APCoagulation.add(entry);
									}
								} 
							} else {
								APCoagulation.add(entry);
							}
						}
						for (ParseObject APentry : APCoagulation) {
							props.add(new Point(APentry.getDouble("Lat"), APentry.getDouble("Long"), APentry.getString("SSID") + " Security: "));
						} 
						
					} else {
					}
		     }
		});
		
		
		
		
	}

	public DrawSurfaceView(Context c, Paint paint) {
		super(c);
	}

	public DrawSurfaceView(Context context, AttributeSet set) {
		super(context, set);
		mPaint.setColor(Color.GREEN);
		mPaint.setTextSize(50);
		mPaint.setStrokeWidth(DpiUtils.getPxFromDpi(getContext(), 2));
		mPaint.setAntiAlias(true);
		
		mRadar = BitmapFactory.decodeResource(context.getResources(), R.drawable.radar);

		mSpots = new Bitmap[props.size()];
		for (int i = 0; i < mSpots.length; i++)
			mSpots[i] = BitmapFactory.decodeResource(context.getResources(), R.drawable.dot);

		mBlips = new Bitmap[props.size()];
		for (int i = 0; i < mBlips.length; i++)
			mBlips[i] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blip);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		screenWidth = w;
		screenHeight = h;

	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mRadar, 0, 0, mPaint);

		int radarCentreX = mRadar.getWidth() / 2;
		int radarCentreY = mRadar.getHeight() / 2;

		for (int i = 0; i < mBlips.length; i++) {
			Bitmap blip = mBlips[i];
			Bitmap spot = mSpots[i];
			Point u = props.get(i);
			double dist = distInMetres(me, u);

			if (blip == null || spot == null) {
				continue;
			}

			if (dist > 70) {
				dist = 70; // we have set points very far away for demonstration
			}

			double angle = bearing(me.latitude, me.longitude, u.latitude, u.longitude) - OFFSET;
			double xPos, yPos;

			if (angle < 0) {
				angle = (angle + 360) % 360;
			}

			xPos = Math.sin(Math.toRadians(angle)) * dist;
			yPos = Math.sqrt(Math.pow(dist, 2) - Math.pow(xPos, 2));

			if (angle > 90 && angle < 270) {
				yPos *= -1;
			}

			double posInPx = angle * (screenWidth / 90d);

			int blipCentreX = blip.getWidth() / 2;
			int blipCentreY = blip.getHeight() / 2;

			xPos = xPos - blipCentreX;
			yPos = yPos + blipCentreY;
			canvas.drawBitmap(blip, (radarCentreX + (int) xPos), (radarCentreY - (int) yPos), mPaint); // radar blip

			// reuse xPos
			int spotCentreX = spot.getWidth() / 2;
			int spotCentreY = spot.getHeight() / 2;
			xPos = posInPx - spotCentreX;

			if (angle <= 45) {
				u.x = (float) ((screenWidth / 2) + xPos);
			}

			else if (angle >= 315) {
				u.x = (float) ((screenWidth / 2) - ((screenWidth * 4) - xPos));
			}

			else {
				u.x = (float) (screenWidth * 9); // somewhere off the screen
			}

			u.y = (float) screenHeight / 2 + spotCentreY;
			canvas.drawBitmap(spot, u.x, u.y, mPaint); // camera spot
			canvas.drawText(u.description, u.x, u.y, mPaint); // text
		}
	}

	public void setOffset(float offset) {
		this.OFFSET = offset;
		//this.invalidate();
	}

	public void setMyLocation(double latitude, double longitude) {
		me.latitude = latitude;
		me.longitude = longitude;
		this.invalidate();
		
	}
	
	protected double distInMetres(Point me, Point u) {
		double lat1 = me.latitude;
		double lng1 = me.longitude;

		double lat2 = u.latitude;
		double lng2 = u.longitude;

		double earthRadius = 6371;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);
		double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		return dist * 1000;
	}

	protected static double bearing(double lat1, double lon1, double lat2, double lon2) {
		double longDiff = Math.toRadians(lon2 - lon1);
		double la1 = Math.toRadians(lat1);
		double la2 = Math.toRadians(lat2);
		double y = Math.sin(longDiff) * Math.cos(la2);
		double x = Math.cos(la1) * Math.sin(la2) - Math.sin(la1) * Math.cos(la2) * Math.cos(longDiff);

		double result = Math.toDegrees(Math.atan2(y, x));
		return (result + 360.0d) % 360.0d;
	}
}
