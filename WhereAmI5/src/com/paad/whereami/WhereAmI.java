package com.paad.whereami;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.paad.whereami.WhereAmI;
import com.paad.whereami.WhereAmI.ProximityIntentReceiver;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class WhereAmI extends MapActivity {

	private static String TREASURE_PROXIMITY_ALERT = "com.paad.treasurealert";

	MapController mapController;
	MyPositionOverlay positionOverlay;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);

		MapView myMapView = (MapView) findViewById(R.id.myMapView);
		mapController = myMapView.getController();

		// Configure the map display options
		myMapView.setSatellite(true);
		myMapView.setStreetView(true);

		// Zoom in
		// mapController.setZoom(18);
		mapController.setZoom(16);

		// Add the MyPositionOverlay
		positionOverlay = new MyPositionOverlay();
		List<Overlay> overlays = myMapView.getOverlays();
		overlays.add(positionOverlay);

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		String provider = locationManager.getBestProvider(criteria, true);

		Location location = locationManager.getLastKnownLocation(provider);

		updateWithNewLocation(location);

		locationManager.requestLocationUpdates(provider, 2000, 10,
				locationListener);

		setProximityAlert();
	}

	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
		}

		public void onProviderDisabled(String provider) {
			updateWithNewLocation(null);
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	/** Update the map with a new location */
	private void updateWithNewLocation(Location location) {
		TextView myLocationText = (TextView) findViewById(R.id.myLocationText);

		String latLongString;
		String addressString = "No address found";

		if (location != null) {
			// Update my location marker
			positionOverlay.setLocation(location);

			// Update the map location.
			Double geoLat = location.getLatitude() * 1E6;
			Double geoLng = location.getLongitude() * 1E6;
			GeoPoint point = new GeoPoint(geoLat.intValue(), geoLng.intValue());

			mapController.animateTo(point);

			double lat = location.getLatitude();
			double lng = location.getLongitude();
			latLongString = "Lat:" + lat + ", Long:" + lng;

			Geocoder gc = new Geocoder(this, Locale.getDefault());
			try {
				List<Address> addresses = gc.getFromLocation(lat, lng, 1);
				StringBuilder sb = new StringBuilder();
				if (addresses.size() > 0) {
					Address address = addresses.get(0);

					for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {

						if (i == (address.getMaxAddressLineIndex() - 1))
							sb.append(address.getAddressLine(i));
						else
							sb.append(address.getAddressLine(i)).append(" - ");
					}

					// sb.append(address.getLocality()).append(" - ");
					// sb.append(address.getPostalCode()).append("\n");
					// sb.append(address.getCountryName());
				}
				addressString = sb.toString();
			} catch (IOException e) {
			}
		} else {
			latLongString = "No location found";
		}
		// myLocationText.setText("Your Current Position is:\n" + latLongString
		// + "\n" + addressString);

		Toast.makeText(WhereAmI.this, addressString, Toast.LENGTH_LONG).show();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/** Configure a proximity alert */
	private void setProximityAlert() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// ultima coordenada
		// double lng = -49.278220;
		// double lat = -25.442130;

		// coordenada n 10
		double lng = -49.279444;
		double lat = -25.442595;

		// float radius = 20f; // meters
		float radius = 100; // meters
		long expiration = -1; // do not expire

		Intent intent = new Intent(TREASURE_PROXIMITY_ALERT);

		PendingIntent proximityIntent = PendingIntent.getBroadcast(
				getApplicationContext(), -1, intent, 0);

		locationManager.addProximityAlert(lat, lng, radius, expiration,
				proximityIntent);

		IntentFilter filter = new IntentFilter(TREASURE_PROXIMITY_ALERT);
		registerReceiver(new ProximityIntentReceiver(), filter);
	}

	/** Proximity Alert Broadcast Receiver */
	public class ProximityIntentReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String key = LocationManager.KEY_PROXIMITY_ENTERING;

			Boolean entering = intent.getBooleanExtra(key, false);

			if (entering)
				alert(context, "Lugar perigoso", "Alerta");

			else
				alert(context, "Lugar calmo", "Alerta");

			// Toast.makeText(WhereAmI.this, "Treasure: " + entering,
			// Toast.LENGTH_LONG).show();
			// [ ... perform proximity alert actions ... ]
		}
	}

	public static void alert(Context context, String msg, String title) {

		Dialog dialog = new AlertDialog.Builder(context).setIcon(0).setTitle(
				title).setPositiveButton("OK", null).setMessage(msg).create();

		dialog.show();
	}
}
