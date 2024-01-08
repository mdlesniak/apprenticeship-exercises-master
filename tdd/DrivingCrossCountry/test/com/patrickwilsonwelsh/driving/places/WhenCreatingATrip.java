package com.patrickwilsonwelsh.driving.places;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.patrickwilsonwelsh.driving.traversal.Trip;

public class WhenCreatingATrip {
	
	private PlaceRepository travelPlaces;
	private Trip trip;

	@Before
	public void init() {
		travelPlaces = new PlaceRepository();
		trip = new Trip();
	}
	
	@Test
	public void canCreateEmptyTrip() throws Exception {
		assertTrue(new Trip().isEmpty());
	}
	
	@Test
	public void canAddPlace() throws Exception {
		trip.appendPlace(travelPlaces.getPlace(PlaceName.Baltimore));
		assertEquals(1, trip.numberOfPlaces());
	}
	
	@Test
	public void canAddTwoPlaces() throws Exception {
		trip.appendPlace(travelPlaces.getPlace(PlaceName.Baltimore));
		trip.appendPlace(travelPlaces.getPlace(PlaceName.Charlottesville));
		
		assertEquals(2, trip.numberOfPlaces());
	}
	
}
