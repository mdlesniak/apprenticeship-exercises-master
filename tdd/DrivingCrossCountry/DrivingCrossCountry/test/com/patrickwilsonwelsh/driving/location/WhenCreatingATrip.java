package com.patrickwilsonwelsh.driving.location;

import static org.junit.Assert.*;

import org.junit.Test;

import com.patrickwilsonwelsh.driving.location.Place;
import com.patrickwilsonwelsh.driving.location.PlaceName;
import com.patrickwilsonwelsh.driving.traversal.Trip;

public class WhenCreatingATrip {
	
	
	@Test
	public void canCreateEmptyTrip() throws Exception {
		assertTrue(new Trip().isEmpty());
	}
	
	@Test
	public void canAddPlace() throws Exception {
		Trip ring = new Trip();
		ring.appendPlace(new Place(PlaceName.Baltimore));
		assertEquals(1, ring.numberOfPlaces());
	}
	
	public void canAddTwoPlace() throws Exception {
		Trip ring = new Trip();
		ring.appendPlace(new Place(PlaceName.Baltimore));
		ring.appendPlace(new Place(PlaceName.Knoxville));
		
		assertEquals(2, ring.numberOfPlaces());
	}

}
