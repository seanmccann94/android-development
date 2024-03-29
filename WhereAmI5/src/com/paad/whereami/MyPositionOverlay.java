package com.paad.whereami;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.location.Location;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class MyPositionOverlay extends Overlay {

	/** Get the position location */
	public Location getLocation() {
		return location;
	}

	/** Set the position location */
	public void setLocation(Location location) {
		this.location = location;
	}

	Location location;

	private final int mRadius = 5;

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		Projection projection = mapView.getProjection();

		if (shadow == false) {
			// Get the current location
			Double latitude = location.getLatitude() * 1E6;
			Double longitude = location.getLongitude() * 1E6;
			GeoPoint geoPoint = new GeoPoint(latitude.intValue(), longitude
					.intValue());

			// Convert the location to screen pixels
			Point point = new Point();
			projection.toPixels(geoPoint, point);

			RectF oval = new RectF(point.x - mRadius, point.y - mRadius,
					point.x + mRadius, point.y + mRadius);

			// Setup the paint
			Paint paint = new Paint();
			paint.setARGB(255, 255, 255, 255);
			paint.setAntiAlias(true);
			paint.setFakeBoldText(true);

			Paint backPaint = new Paint();
			backPaint.setARGB(180, 50, 50, 50);
			backPaint.setAntiAlias(true);

			RectF backRect = new RectF(point.x + 2 + mRadius, point.y - 3
					* mRadius, point.x + 65, point.y + mRadius);

			// Draw the marker on the screen
			canvas.drawOval(oval, paint);
			canvas.drawRoundRect(backRect, 5, 5, backPaint);
			canvas.drawText("Here I Am", point.x + 2 * mRadius, point.y, paint);
		}
		super.draw(canvas, mapView, shadow);
	}

	@Override
	public boolean onTap(GeoPoint point, MapView mapView) {
		return false;
	}
}