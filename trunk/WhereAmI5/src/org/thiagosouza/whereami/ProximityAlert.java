package org.thiagosouza.whereami;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.widget.Toast;

public class ProximityAlert {

	private static String TREASURE_PROXIMITY_ALERT = "org.thiagosouza.treasurealert";

	public ProximityAlert(Double lat, Double lng, float radius,
			Context context, LocationManager locationManager) {

		long expiration = -1; // do not expire

		Intent intent = new Intent(TREASURE_PROXIMITY_ALERT);

		PendingIntent proximityIntent = PendingIntent.getBroadcast(context, -1,
				intent, 0);

		// chama o método da classe Location para adicionar os dados dos alertas
		// de aproximação
		locationManager.addProximityAlert(lat, lng, radius, expiration,
				proximityIntent);

		// registra o context para receber alertas de proximidade
		IntentFilter filter = new IntentFilter(TREASURE_PROXIMITY_ALERT);
		context.registerReceiver(new ProximityIntentReceiver(), filter);

	}

	/** Proximity Alert Broadcast Receiver */
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

	public static void alert(Context context, String msg, String title) {

		Dialog dialog = new AlertDialog.Builder(context).setIcon(0).setTitle(
				title).setPositiveButton("OK", null).setMessage(msg).create();

		dialog.show();
	}

}
