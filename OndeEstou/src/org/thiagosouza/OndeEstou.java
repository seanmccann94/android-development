package org.thiagosouza;

import java.io.IOException;

import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

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

	TextView myLocationText;

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

		updateMap(location);

		System.out.println(location.toString());

		locationManager.requestLocationUpdates(provider, 2000, 10,
				locationListener);

		// Chama a funcao para configurar os alertas de proximidade
		setProximityAlert();
	}

	/** Interface LocationListener */
	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			updateMap(location);
		}

		public void onProviderDisabled(String provider) {
			updateMap(null);
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	/** Atualiza o mapa com a nova localiza��o */
	private void updateMap(Location location) {

		myLocationText = (TextView) findViewById(R.id.myLocationText);

		String latLongString;
		String addressString = "Endere�o n�o encontrado";

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
			latLongString = "Lat:" + MF.roundTwoDecimals(lat) + ", Long:"
					+ MF.roundTwoDecimals(lng);

			// Mostra as coordenadas na parte de cima da tela
			myLocationText.setText("Sua posi��o atual �:\n" + latLongString);

			// Busca o endereco atraves do GeoCoder
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
			latLongString = "Localiza��o n�o encontrada";
		}

		// Mostra na tela o endereco obtido atraves do GeoCoder
		Toast.makeText(OndeEstou.this, addressString, Toast.LENGTH_LONG).show();
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

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}