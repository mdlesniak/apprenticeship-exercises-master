package flight.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import flight.Flight;
import flight.Flyable;
import flight.Route;
import flight.passenger.Passenger;

public class FlightManagerTest {
	FlightDataManagerAgentUtilProcessorClass flightManager;
	
	 //TODO Cannot add first leg if its departure time is X hours less than current time...
  //TODO Mininum connection times vary by airport
  //TODO Mininum connection times vary by whether terminal change is required to get to new gate at connecting airport
  //TODO Route list is returned sorted by weighted score that balances number of legs with soonest arrival at destination
  //TODO If itinerary object containing flight specifies car rental at dest airport, route list contains flights to airports nearby destination airport, 
         //sorted by drive-time proximity to dest airport. 
  //TODO In last case, fake service layer permits changing of car rental from original dest airport to new dest airport, along with extra charges
  //TODO Space on any flight is governed by coachClassIsFull and firstClassIsFull. if coachClassIsFull and spaces exist in first class, low-priority route
       //include flights that would bump passenger to first class for an additional charge. 
	
	@Before
	public void initStandardFlightTemplates() {
		flightManager = new FlightDataManagerAgentUtilProcessorClass();

		flightManager.put("4182", new Flight("4182", "DTW", "RIC", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 11, 20), newDateTime(2010, Calendar.JANUARY, 2, 12, 20)));
		
		flightManager.put("4321", new Flight("4321", "DTW", "RIA", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 13, 30), newDateTime(2010, Calendar.JANUARY, 2, 14, 50)));

		flightManager.put("5687", new Flight("5687", "DTW", "ORD", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 9, 20), newDateTime(2010, Calendar.JANUARY, 2, 10, 20)));
		flightManager.put("3245", new Flight("3245", "ORD", "SFO", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 11, 25), newDateTime(2010, Calendar.JANUARY, 2, 15, 20)));
		
		flightManager.put("5555", new Flight("5555", "ORD", "MIA", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 12, 5), newDateTime(2010, Calendar.JANUARY, 2, 14, 20)));
	}
	
	@Test
	public void canReRoutePassengerForCancelledFlight() {
		Flight cFlight = new Flight("7138", "DTW", "RIC", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 12, 20), newDateTime(2010, Calendar.JANUARY, 2, 13, 20));
		cFlight.put("PW45689", new Passenger("Poor", "Schmuck", "11-12-1974", "807B9684754"));
		cFlight.setStatus(Flight.C);
		
		flightManager.put("7138", cFlight);
		
		Passenger poorSchmuck = cFlight.getPassenger("PW45689");
		List<Flyable>  routes = flightManager.reRoute(cFlight, poorSchmuck);
		Route nRoute = (Route) routes.get(0);
		assertEquals("4182", nRoute.getFlight(0).getNo());
	}
	
	@Test
	public void canReRouteDifferentPassengerForCancelledFlight() {
		Flight cFlight = new Flight("1234", "DTW", "RIA", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 14, 30), newDateTime(2010, Calendar.JANUARY, 2, 15, 50));
		cFlight.put("MH45689", new Passenger("Different", "Schmuck", "11-12-1974", "807B9684754"));
		cFlight.setStatus(Flight.C);
		
		flightManager.put("1234", cFlight);
		
		Passenger differentSchmuck = cFlight.getPassenger("MH45689");
		List<Flyable>  routes = flightManager.reRoute(cFlight, differentSchmuck);
		Route nRoute = (Route) routes.get(0);
		assertEquals("4321", nRoute.getFlight(0).getNo());
	}
	
	@Test
	public void canReRoutePassengerOnMultiLegFlight_sForCancelledFlight() {
		Flight cFlight = new Flight("4444", "DTW", "MIA", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 10, 30), newDateTime(2010, Calendar.JANUARY, 2, 12, 50));
		cFlight.put("MH45689", new Passenger("Yet Another", "Schmuck", "11-12-1974", "807B9684754"));
		cFlight.setStatus(Flight.C);
		
		flightManager.put("4444", cFlight);
		
		Passenger randomSchmuck = cFlight.getPassenger("MH45689");
		List<Flyable>  routes = flightManager.reRoute(cFlight, randomSchmuck);
		Route route = (Route) routes.get(0);
		
		assertEquals("5555", route.getFlight(1).getNo());
		assertEquals(2,route.noLegs());
	}
	
	@Test
	public void canFindMultipleRoutes() {
		flightManager.put("0089", new Flight("0089", "PTT", "KCA", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 10, 30), newDateTime(2010, Calendar.JANUARY, 2, 12, 50)));
		flightManager.put("0980", new Flight("0980", "KCA", "IAD", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 14, 0), newDateTime(2010, Calendar.JANUARY, 2, 16, 50)));
		flightManager.put("3489", new Flight("3489", "IAD", "DEN", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 18, 30), newDateTime(2010, Calendar.JANUARY, 2, 22, 50)));
		
		flightManager.put("9080", new Flight("9080", "PTT", "DEN", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 10, 30), newDateTime(2010, Calendar.JANUARY, 2, 12, 50)));
			
		Flight cFlight = new Flight("3128", "PTT", "DEN", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 9, 0), newDateTime(2010, Calendar.JANUARY, 2, 11, 50));
		cFlight.put("MH45689", new Passenger("Yet Another", "Schmuck", "11-12-1974", "807B9684754"));
		cFlight.setStatus(Flight.C);
		flightManager.put("3128", cFlight);
		
		Passenger randomSchmuck = cFlight.getPassenger("MH45689");
		List<Flyable>  routes = flightManager.reRoute(cFlight, randomSchmuck);
		
		assertEquals(2, routes.size());
		Route firstRoute = (Route) routes.get(0);
		Route secondRoute = (Route) routes.get(1);
		assertFalse(firstRoute.getLegs().size() == secondRoute.getLegs().size());
		
		assertEquals(1, secondRoute.noLegs());
		assertEquals(3, firstRoute.noLegs());
	}
	
	@Test
	public void canFindRoute() {
		assertEquals(2, flightManager.route("DTW", "MIA", null).size());
		assertEquals(0, flightManager.route("DTW", "IAD", null).size());
	}
	
	@Test
	public void canMakeRoute() {
		List<Flight> newRoute = flightManager.route("DTW", "MIA", null);
		assertEquals(2, newRoute.size());
		assertEquals("5687",newRoute.get(0).getNo());
		assertEquals("5555",newRoute.get(1).getNo());
	}
	
	@Test
	public void canMakeThreeLeggedRoute() {
		flightManager.put("7218", new Flight("7218", "DTW", "NWK", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 10, 30), newDateTime(2010, Calendar.JANUARY, 2, 12, 50)));
		flightManager.put("4555", new Flight("4555", "NWK", "LGA", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 14, 0), newDateTime(2010, Calendar.JANUARY, 2, 16, 50)));
		flightManager.put("5656", new Flight("5656", "LGA", "LON", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 18, 30), newDateTime(2010, Calendar.JANUARY, 2, 22, 50)));
		
		List<Flight> newRoute = flightManager.route("DTW", "LON", null);
		assertEquals(3, newRoute.size());
		assertEquals("7218",newRoute.get(0).getNo());
		assertEquals("4555",newRoute.get(1).getNo());		
		assertEquals("5656",newRoute.get(2).getNo());		
	}

	@Test
	public void cannotMakeRoute() {
		List<Flight> newRoute = flightManager.route("FYU", "YYZ", null);
		assertEquals(0, newRoute.size());
	}
	
	@Test
	public void cannotMakeThreeLeggedRouteWithInsufficientLegs() {
		flightManager.put("1212", new Flight("1212", "MID", "YYZ", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 10, 30), newDateTime(2010, Calendar.JANUARY, 2, 12, 50)));
		flightManager.put("7780", new Flight("7780", "ORD", "MID", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 14, 0), newDateTime(2010, Calendar.JANUARY, 2, 16, 50)));
		flightManager.put("7780", new Flight("7780", "SFO", "FYU", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 18, 30), newDateTime(2010, Calendar.JANUARY, 2, 22, 50)));
		
		List<Flight> newRoute = flightManager.route("SFO", "YYZ", null);
		assertEquals(0, newRoute.size());
	}
	
	@Test
	public void cannotMakeRouteIfCrucialLegHasNoEmptySeats() {
		flightManager.put("7218", new Flight("7218", "DTW", "NWK", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 10, 30), newDateTime(2010, Calendar.JANUARY, 2, 12, 50)));
		flightManager.put("4555", new Flight("4555", "NWK", "LGA", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 14, 0), newDateTime(2010, Calendar.JANUARY, 2, 16, 50)));
		
		Flight fullFlight = new Flight("5656", "LGA", "LON", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 18, 30), newDateTime(2010, Calendar.JANUARY, 2, 22, 50));
		fullFlight.setFull(true);
		flightManager.put("5656", fullFlight);
		
		List<Flight> newRoute = flightManager.route("DTW", "LON", null);
		assertEquals(0, newRoute.size());
	}
	
	@Test
	public void cannotMakeRouteIfInsufficientConnectionTimeExistsBetweenLegs() {
		flightManager.put("7218", new Flight("7218", "DTW", "NWK", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 10, 30), newDateTime(2010, Calendar.JANUARY, 2, 12, 50)));
		flightManager.put("4555", new Flight("4555", "NWK", "LGA", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 14, 0), newDateTime(2010, Calendar.JANUARY, 2, 16, 50)));
		
		flightManager.put("5656", new Flight("5656", "LGA", "LON", Flight.O, 
				newDateTime(2010, Calendar.JANUARY, 2, 13, 30), newDateTime(2010, Calendar.JANUARY, 2, 15, 50)));
		
		List<Flight> newRoute = flightManager.route("DTW", "LON", null);
		assertEquals(0, newRoute.size());
	}
	

	
	private Calendar newDateTime(int year, int month, int dayOfMonth, int hour,
			int minute) {
		Calendar requestedDate = Calendar.getInstance();
		requestedDate.set(year, month, dayOfMonth, hour, minute);
		
		return requestedDate;
	}

}

