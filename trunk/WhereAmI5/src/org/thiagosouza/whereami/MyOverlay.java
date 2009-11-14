package org.thiagosouza.whereami;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MyOverlay extends ItemizedOverlay {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

	public MyOverlay(Drawable defaultMarker) {
		// super(defaultMarker);
		super(boundCenterBottom(defaultMarker));
	}

	//@Override
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
