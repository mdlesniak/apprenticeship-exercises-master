package com.patrickwilsonwelsh.driving.traversal;

import com.patrickwilsonwelsh.driving.location.Place;

public class Trip {
	Place currentPlace = null;

	public boolean isEmpty() {
		return (getNumberOfPlaces() == 0);
	}

	private int getNumberOfPlaces() {
		if (null == currentPlace) return 0;
		
		return 0;
	}

	public void appendPlace(Place newPlace) {
	}

	public int numberOfPlaces() {
		return -1;
	}

}
