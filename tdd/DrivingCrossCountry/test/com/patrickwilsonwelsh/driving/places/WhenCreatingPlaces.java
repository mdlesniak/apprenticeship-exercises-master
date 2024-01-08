package com.patrickwilsonwelsh.driving.places;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import com.patrickwilsonwelsh.driving.places.Place;
import com.patrickwilsonwelsh.driving.places.PlaceName;
import com.patrickwilsonwelsh.driving.places.PlaceRepository;


public class WhenCreatingPlaces {
	
	private PlaceRepository travelPlaces;

	@Test
	public void canSetName() throws Exception {
		Place baltimore = new Place(PlaceName.Baltimore);
		
		assertEquals(PlaceName.Baltimore, baltimore.getName());
	}
	
	
	@Test
	public void canFindAllLikelyUsefulPlacesInRepository() throws Exception {
		travelPlaces = new PlaceRepository();
		
		assertEquals(new Place(PlaceName.Baltimore), travelPlaces.getPlace(PlaceName.Baltimore));
		assertEquals(new Place(PlaceName.Wytheville), travelPlaces.getPlace(PlaceName.Wytheville));
		assertEquals(new Place(PlaceName.Austin), travelPlaces.getPlace(PlaceName.Austin));
		assertEquals(new Place(PlaceName.SanFrancisco), travelPlaces.getPlace(PlaceName.SanFrancisco));
		
	}
	
	@Ignore
	@Test
	public void canGetDurationBetweenTwoPlacesInTrip() {

		assertEquals(3.5, travelPlaces.hoursBetween(PlaceName.Baltimore, PlaceName.Charlottesville));
	}

}
