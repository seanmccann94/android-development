package org.thiagosouza.whereami;

import java.io.IOException;

import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
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

	LocationManager locationManager = null;
	MapController mapController;



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

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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

		// chama a funcao para configurar os alertas de proximidade
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

		String lastLocation = "";

		if (location != null) {

			// Atualiza o mapa com a localizacao atual
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

		myLocationText.setText("Your Current Position is:\n" + latLongString);

		if (addressString.equalsIgnoreCase(lastLocation)) {
			alert(this, "alerta", "repetido");
		} else {
			Toast.makeText(WhereAmI.this, addressString, Toast.LENGTH_LONG)
					.show();
			// alert(this, addressString, "repetido");
		}

		lastLocation = addressString;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	public static void alert(Context context, String msg, String title) {

		Dialog dialog = new AlertDialog.Builder(context).setIcon(0).setTitle(
				title).setPositiveButton("OK", null).setMessage(msg).create();

		dialog.show();
	}


	/** Configure a proximity alert */
	private void setProximityAlert() {

		ProximityAlert proximityAlertPonto1 = new ProximityAlert(-25.443195,
				-49.280977, 100, "Alerta personalizado");
		
		ProximityAlert proximityAlertPonto10 = new ProximityAlert(-25.442595,
				-49.279444, 100);

		
		Setup.addProximityAlert(proximityAlertPonto1);
		Setup.addProximityAlert(proximityAlertPonto10);
		Setup setup = new Setup(getApplicationContext(), locationManager);

	}

}
