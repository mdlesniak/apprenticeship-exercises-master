package closest;

public class ClosestToZeroFinder {
	private final int[] input;
	
	public ClosestToZeroFinder(int[] candidates) {
		this.input = candidates;
	}

	public int getClosest() {
		int closest;
		int currentCandidate;
		if (input == null || input.length == 0) throw new IllegalArgumentException("Invalid input");
		closest = input[0];
		
		for (int candidate : input) {
			currentCandidate = candidate;
			
			closest = Math.abs(currentCandidate) < Math.abs(closest) ? currentCandidate : closest;
			
			if ((Math.abs(currentCandidate) == Math.abs(closest))) {
				closest = currentCandidate > 0 ? currentCandidate : closest;
			}
		}
		
		return closest;
	}
	
}
