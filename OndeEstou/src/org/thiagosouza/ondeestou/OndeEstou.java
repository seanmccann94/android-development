package org.thiagosouza.ondeestou;

import java.io.IOException;

import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

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
	public static MapView myMapView;

	TextView myLocationText;
	TextView myApplicationFooter;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);

		// Intanciacao da classe MapView (do mapa propriamente dito)
		myMapView = (MapView) findViewById(R.id.myMapView);
		mapController = myMapView.getController();

		// Configura opcoes de visualizacao do mapa
		myMapView.setSatellite(false);
		myMapView.setTraffic(true);

		// Configura o zoom no mapa (21-1)
		mapController.setZoom(16);
		myMapView.setBuiltInZoomControls(true); // mostra o controle de zoom
		myMapView.displayZoomControls(false);

		// Obtem uma instancia do servico gerenciador de localizacao
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// Define como provedor o GPS do aparelho
		String provider = LocationManager.GPS_PROVIDER;

		// Obtem a localizacao (imediatamente)
		Location location = locationManager.getLastKnownLocation(provider);

		// Mostra o mapa na tela
		updateMap(location);

		// Inscreve a atual activity para receber notificacoes de localizacao
		// (periodicamente)
		locationManager.requestLocationUpdates(provider, 2000, 10,
				locationListener);

		// Chama a funcao para configurar os alertas de proximidade
		setProximityAlert();

		// Cria um overlay mostrando a posicao do dispositivo
		List<com.google.android.maps.Overlay> overlays = myMapView
				.getOverlays();
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this,
				myMapView);

		// Mostra o overlay com a localizacao atual do dispositivo
		overlays.add(myLocationOverlay);

		// Habilita atualizacoes do sensor
		myLocationOverlay.enableCompass();

		// Habilita o ponto azul na tela
		myLocationOverlay.enableMyLocation();

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

	/** Atualiza o mapa com a nova localização */
	private void updateMap(Location location) {

		myLocationText = (TextView) findViewById(R.id.myLocationText);

		String latLongString;
		String addressString = "Endereço não encontrado";

		if (location != null) {

			// Define a localizacao atual
			Double geoLat = location.getLatitude() * 1E6;
			Double geoLng = location.getLongitude() * 1E6;
			GeoPoint point = new GeoPoint(geoLat.intValue(), geoLng.intValue());

			// Atualiza o mapa
			mapController.setCenter(point);

			// Formata as coordenadas
			Double lat = location.getLatitude();
			Double lng = location.getLongitude();
			latLongString = "Lat:" + F.roundSixDecimals(lat) + ", Long:"
					+ F.roundSixDecimals(lng);

			// Mostra as coordenadas na parte de cima da tela
			myLocationText.setText("Minhas coordenadas:\n" + latLongString);

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
			latLongString = "Localização não encontrada";
		}

		// Mostra na tela o endereco obtido atraves do GeoCoder
		Toast.makeText(OndeEstou.this, addressString, Toast.LENGTH_LONG).show();
	}

	/** Configura os alertas de aproximacao */
	private void setProximityAlert() {

		// Alerta no pronto 1
		MyProximityAlert proximityAlertPonto1 = new MyProximityAlert(
				-25.443195, -49.280977, 100, "Alerta personalizado");

		// Alerta no pronto 10
		MyProximityAlert proximityAlertPonto10 = new MyProximityAlert(
				-25.442595, -49.279444, 100);

		Setup.addProximityAlert(proximityAlertPonto1);
		Setup.addProximityAlert(proximityAlertPonto10);

		// Habilita os alertas
		Setup setup = new Setup();
		setup.setProximityAlert(getApplicationContext(), locationManager);
	}

	/** Implementacao do metodo abstrato da classe MapActivity */
	protected boolean isRouteDisplayed() {
		return false;
	}

}
