package com.patrickwilsonwelsh.driving.location;

import static org.junit.Assert.*;

import org.junit.Test;

import com.patrickwilsonwelsh.driving.location.Place;
import com.patrickwilsonwelsh.driving.location.PlaceName;


public class WhenCreatingPlaces {
	
	@Test
	public void canSetName() throws Exception {
		Place baltimore = new Place(PlaceName.Baltimore);
		
		assertEquals(PlaceName.Baltimore, baltimore.getName());
	}
	
	@Test
	public void canSetPreviousPlace() throws Exception {
		
	}
	
	
	@Test
	public void canSetNextPlace() throws Exception {
		
	}

}
