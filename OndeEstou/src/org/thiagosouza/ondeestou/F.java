package org.thiagosouza.ondeestou;

import java.text.DecimalFormat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

public class F {

	public static double roundSixDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.######");
		return Double.valueOf(twoDForm.format(d));
	}

	public static void alert(Context context, String msg, String title) {

		Dialog dialog = new AlertDialog.Builder(context).setIcon(0).setTitle(
				title).setPositiveButton("OK", null).setMessage(msg).create();

		dialog.show();
	}
}
