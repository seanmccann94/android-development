package org.thiagosouza.whereami;

public class ProximityAlert {

	Double lat = 0.0;
	Double lng = 0.0;
	float radius = 0;
	String msg = "";
	
	ProximityAlert(Double lat, Double lng, float radius, String msg){
		
		this.lat = lat;
		this.lng = lng;
		this.radius = radius;
		this.msg = msg;
		
	}
	
	ProximityAlert(Double lat, Double lng, float radius) {
		this(lat, lng, radius, "");
	}
	
}
