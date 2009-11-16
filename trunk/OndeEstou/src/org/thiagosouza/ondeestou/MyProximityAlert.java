package org.thiagosouza.ondeestou;

/**
 * Clase dos alertas de proximidade
 * */
public class MyProximityAlert {

	Double lat = 0.0;
	Double lng = 0.0;
	float radius = 0;
	String msg = "";

	MyProximityAlert(Double lat, Double lng, float radius, String msg) {

		this.lat = lat;
		this.lng = lng;
		this.radius = radius;
		this.msg = msg;
	}

	MyProximityAlert(Double lat, Double lng, float radius) {
		this(lat, lng, radius, "");
	}

}
