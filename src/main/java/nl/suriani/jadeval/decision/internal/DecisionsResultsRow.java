package nl.suriani.jadeval.decision.internal;

import java.util.ArrayList;
import java.util.List;

public final class DecisionsResultsRow {
	private final String description;
	private final List<String> events;

	public DecisionsResultsRow(String description, List<String> events) {
		this.description = description;
		this.events = events;
	}

	public String getDescription() {
		return description;
	}

	public boolean getResult() {
		return events.size() > 0;
	}

	public List<String> getEvents() {
		return new ArrayList<>(events);
	}
}
