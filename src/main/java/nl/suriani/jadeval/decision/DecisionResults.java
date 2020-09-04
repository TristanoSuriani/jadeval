package nl.suriani.jadeval.decision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
* The object that is returned when a set of Decisions is performed.
* */
public final class DecisionResults {
	private final List<DecisionResult> results;

	public DecisionResults() {
		results = new ArrayList<>();
	}

	private DecisionResults(List<DecisionResult> results) {
		this.results = results;
	}

	/**
	 * add row to the results table
	 * @param result Result row to be added to the table
	 * @return new instance of the object
	 */
	public void add(DecisionResult result) {
		results.add(result);
	}

	public List<String> getResponses() {
		return results.stream()
				.map(DecisionResult::getResponses)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	public List<DecisionResult> getResults() {
		return new ArrayList<>(results);
	}
}
