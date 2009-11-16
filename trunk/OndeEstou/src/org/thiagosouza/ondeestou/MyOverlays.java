package org.thiagosouza.ondeestou;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MyOverlays extends ItemizedOverlay {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

	public MyOverlays(Drawable defaultMarker) {
		// super(defaultMarker);
		super(boundCenterBottom(defaultMarker));
	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
		// return null;
	}

	@Override
	public int size() {
		return mOverlays.size();
		// return 0;
	}

}
