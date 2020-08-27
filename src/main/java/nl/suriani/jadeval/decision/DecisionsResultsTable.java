package nl.suriani.jadeval.decision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
* The object that is returned when a set of Decisions is performed.
* */
public final class DecisionsResultsTable {
	private final List<DecisionsResultsRow> results;

	public DecisionsResultsTable() {
		results = new ArrayList<>();
	}

	private DecisionsResultsTable(List<DecisionsResultsRow> results) {
		this.results = results;
	}

	private Exception exception;

	/**
	 * Initialize the object with an exception.
	 * @param exception, not null
	 */
	DecisionsResultsTable(Exception exception) {
		this.results = new ArrayList<>();
		this.exception = exception;
	}

	/**
	 * add row to the results table
	 * @param result Result row to be added to the table
	 * @return new instance of the object
	 */
	DecisionsResultsTable add(DecisionsResultsRow result) {
		List<DecisionsResultsRow> resultsCopy = new ArrayList<>(results);
		resultsCopy.add(result);
		return new DecisionsResultsTable(resultsCopy);
	}

	public List<String> getEvents() {
		return results.stream()
				.map(DecisionsResultsRow::getEvents)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	public List<DecisionsResultsRow> getResults() {
		return new ArrayList<>(results);
	}

	public Optional<Exception> getException() {
		return Optional.ofNullable(exception);
	}
}
