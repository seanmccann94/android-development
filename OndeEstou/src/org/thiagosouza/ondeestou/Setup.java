package org.thiagosouza.ondeestou;

import java.util.ArrayList;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.widget.Toast;

/**
 * Clase de configuracao dos alertas de proximidade
 * */
public class Setup {

	private static String TREASURE_PROXIMITY_ALERT = "org.thiagosouza.alertacommensagem";

	static ArrayList<ProximityAlert> proximityAlerts = new ArrayList<ProximityAlert>();

	// ’cones mostrados no mapa
	// Drawable androidMarker = this.getResources().getDrawable(
	// R.drawable.androidmarker);

	public static void addProximityAlert(ProximityAlert pa) {
		proximityAlerts.add(pa);
	}

	public void setProximityAlert(Context context,
			LocationManager locationManager) {

		long expiration = -1; // n‹o espira

		Intent intent = new Intent(TREASURE_PROXIMITY_ALERT);

		PendingIntent proximityIntent = PendingIntent.getBroadcast(context, -1,
				intent, 0);

		for (int i = 0; i < proximityAlerts.size(); i++) {
			// para cada alerta de proximidade
			ProximityAlert paTemp = proximityAlerts.get(i);


			
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
				Toast.makeText(context, "Treasure: " + entering,
						Toast.LENGTH_LONG).show();
		}
	}
}
