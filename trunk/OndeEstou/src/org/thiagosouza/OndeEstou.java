package org.thiagosouza;

import java.io.IOException;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class OndeEstou extends MapActivity {

	LocationManager locationManager = null;
	MapController mapController;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);

		MapView myMapView = (MapView) findViewById(R.id.myMapView);
		mapController = myMapView.getController();

		// Configura opcoes de visualizacao do mapa
		myMapView.setSatellite(true);
		myMapView.setStreetView(true);

		// Configura o zoom no mapa (21-1)
		mapController.setZoom(16);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		String provider = LocationManager.GPS_PROVIDER;

		Location location = locationManager.getLastKnownLocation(provider);

		updateWithNewLocation(location);

		System.out.println(location.toString());

		locationManager.requestLocationUpdates(provider, 2000, 10,
				locationListener);

		// Chama a funcao para configurar os alertas de proximidade
		setProximityAlert();
	}

	/** Interface LocationListener */
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

	/** Atualiza o mapa com a nova localização */
	private void updateWithNewLocation(Location location) {
		TextView myLocationText = (TextView) findViewById(R.id.myLocationText);

		String latLongString;
		String addressString = "Endereço não encontrado";

		if (location != null) {

			// Define a localizacao atual
			Double geoLat = location.getLatitude() * 1E6;
			Double geoLng = location.getLongitude() * 1E6;
			GeoPoint point = new GeoPoint(geoLat.intValue(), geoLng.intValue());

			// Atualiza o mapa
			mapController.animateTo(point);

			// Formata as coordenadas
			Double lat = location.getLatitude();
			Double lng = location.getLongitude();
			latLongString = "Lat:" + roundTwoDecimals(lat) + ", Long:"
					+ roundTwoDecimals(lng);

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
				}
				addressString = sb.toString();
			} catch (IOException e) {
			}
		} else {
			latLongString = "Localização não encontrada";
		}

		myLocationText.setText("Sua posição atual é:\n" + latLongString);

		Toast.makeText(OndeEstou.this, addressString, Toast.LENGTH_LONG).show();

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

	/** Configura um alerta de aproximacao */
	private void setProximityAlert() {

		ProximityAlert proximityAlertPonto1 = new ProximityAlert(-25.443195,
				-49.280977, 100, "Alerta personalizado");

		ProximityAlert proximityAlertPonto10 = new ProximityAlert(-25.442595,
				-49.279444, 100);

		Setup.addProximityAlert(proximityAlertPonto1);
		Setup.addProximityAlert(proximityAlertPonto10);

		@SuppressWarnings("unused")
		Setup setup = new Setup(getApplicationContext(), locationManager);

	}

	double roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.######");
		return Double.valueOf(twoDForm.format(d));
	}

}
