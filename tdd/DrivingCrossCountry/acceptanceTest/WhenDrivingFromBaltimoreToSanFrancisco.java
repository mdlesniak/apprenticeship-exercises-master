import static org.junit.Assert.*;
import journey.ComplexJourney;

import org.junit.Before;
import org.junit.Test;

import com.patrickwilsonwelsh.driving.places.Place;
import com.patrickwilsonwelsh.driving.places.PlaceName;

public class WhenDrivingFromBaltimoreToSanFrancisco {
	private ComplexJourney crossCountryTrip;

	@Before
	public void init() {
		crossCountryTrip = new ComplexJourney(new Place(PlaceName.Baltimore), new Place(PlaceName.SanFrancisco));
	}
	
	@Test
	public void canReportTotalMilesDriven() {
		assertTrue(crossCountryTrip.totalMilesDriven() > 3000);
	}
	
}
