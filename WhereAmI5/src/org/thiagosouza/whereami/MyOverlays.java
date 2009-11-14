package org.thiagosouza.whereami;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MyOverlays extends ItemizedOverlay {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

	List<Overlay> mapOverlays;

	Overlay itemizedOverlay;

	public MyOverlays(Drawable defaultMarker, Double lat, Double lng,
			String title, String text) {

		// super(defaultMarker);
		super(boundCenterBottom(defaultMarker));

		GeoPoint point = new GeoPoint(lat.intValue(), lng.intValue());
		OverlayItem overlayItem = new OverlayItem(point, title, text);

		((MyOverlays) itemizedOverlay).addOverlay(overlayItem);
		mapOverlays.add(itemizedOverlay);
	}

	// @Override
	protected OverlayItem createItem(int i) {
		// return null;
		return mOverlays.get(i);

	}

	@Override
	public int size() {
		// return 0;
		return mOverlays.size();
	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

}
