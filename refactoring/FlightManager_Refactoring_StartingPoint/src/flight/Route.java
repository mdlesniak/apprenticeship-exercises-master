package flight;

import java.util.Collection;
import java.util.List;


public class Route implements Flyable {
	private String orig;
	private String finalDestination;
	private List<Flight> legSequence;
	
	public Route(List<Flight> legSequence) {
		this.orig = legSequence.get(0).getStart();
		this.finalDestination = legSequence.get(legSequence.size() - 1).getDest();
		this.legSequence = legSequence;
	}

	public String getDest() {
		return finalDestination;
	}

	public String getStart() {
		return orig;
	}

	public int noLegs() {
		return legSequence.size();
	}
	
	public Flight getFlight(int index) {
		return legSequence.get(index);
	}

	public Collection<? extends Flight> getLegs() {
		return legSequence;
	}

}
