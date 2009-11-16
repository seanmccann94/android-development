package org.thiagosouza.ondeestou;

import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * Clase de configuracao dos alertas de proximidade
 * */
public class Setup {

	private static String TREASURE_PROXIMITY_ALERT = "org.thiagosouza.alertacommensagem";

	static ArrayList<MyProximityAlert> proximityAlerts = new ArrayList<MyProximityAlert>();

	// ’cones mostrados no mapa
	// Drawable androidMarker = this.getResources().getDrawable(
	// R.drawable.androidmarker);

	public static void addProximityAlert(MyProximityAlert pa) {
		proximityAlerts.add(pa);
	}

	public void setProximityAlert(Context context,
			LocationManager locationManager) {

		List<Overlay> overlays = OndeEstou.myMapView.getOverlays();

		long expiration = -1; // n‹o espira

		Intent intent = new Intent(TREASURE_PROXIMITY_ALERT);

		PendingIntent proximityIntent = PendingIntent.getBroadcast(context, -1,
				intent, 0);

		for (int i = 0; i < proximityAlerts.size(); i++) {
			// para cada alerta de proximidade
			MyProximityAlert paTemp = proximityAlerts.get(i);

			Drawable drawable = context.getResources().getDrawable(
					R.drawable.androidmarker);
			
			MyOverlays tempOverlay = new MyOverlays(drawable);

			// Define um GeoPoint com a localizacao atual
			Double geoLat = paTemp.lat * 1E6;
			Double geoLng = paTemp.lng * 1E6;
			GeoPoint tempPoint = new GeoPoint(geoLat.intValue(), geoLng
					.intValue());

			OverlayItem overlayItem = new OverlayItem(tempPoint, "teste", "teste");
			tempOverlay.addOverlay(overlayItem);
			overlays.add(tempOverlay);

			// chama o mŽtodo da classe Location para adicionar os pontos
			locationManager.addProximityAlert(paTemp.lat, paTemp.lng,
					paTemp.radius, expiration, proximityIntent);
		}

		// registra o context (parametro) para receber alertas de proximidade
		IntentFilter filter = new IntentFilter(TREASURE_PROXIMITY_ALERT);
		context.registerReceiver(new ProximityIntentReceiver(), filter);

	}

	/** Classe para receber os intents de proximidade */
	public class ProximityIntentReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String key = LocationManager.KEY_PROXIMITY_ENTERING;

			Boolean entering = intent.getBooleanExtra(key, false);

			if (entering)
				Toast.makeText(context, "Alerta",
						Toast.LENGTH_LONG).show();
		}
	}
}
