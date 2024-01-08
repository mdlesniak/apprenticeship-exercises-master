package com.patrickwilsonwelsh.driving.traversal;

import com.patrickwilsonwelsh.driving.places.Place;
import com.patrickwilsonwelsh.driving.places.PlaceName;

public class Trip {
	Place currentPlace = null;
	private int numberOfPlaces;

	public boolean isEmpty() {
		return (getNumberOfPlaces() == 0);
	}

	private int getNumberOfPlaces() {
		if (null == currentPlace) return 0;
		
		return numberOfPlaces;
	}

	public void appendPlace(Place newPlace) {
		numberOfPlaces++;
	}

	public int numberOfPlaces() {
		return numberOfPlaces;
	}

	public int hoursBetween(PlaceName baltimore, PlaceName charlottesville) {
		return -1;
	}

}
