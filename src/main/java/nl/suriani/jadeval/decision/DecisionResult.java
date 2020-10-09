package nl.suriani.jadeval.decision;

import java.util.ArrayList;
import java.util.List;

public final class DecisionResult {
	private final String description;
	private final List<String> responses;

	public DecisionResult(String description, List<String> responses) {
		this.description = description;
		this.responses = responses;
	}

	public String getDescription() {
		return description;
	}

	public boolean getResult() {
		return responses.size() > 0;
	}

	public List<String> getResponses() {
		return new ArrayList<>(responses);
	}
}
