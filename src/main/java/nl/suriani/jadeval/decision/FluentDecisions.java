package nl.suriani.jadeval.decision;

import nl.suriani.jadeval.common.Facts;
import nl.suriani.jadeval.common.internal.value.EmptyValue;

import java.util.List;

public class FluentDecisions {
	private List<Decision> decisions;

	public FluentDecisions(List<Decision> decisions) {
		this.decisions = decisions;
	}

	public DecisionResults apply(Object... objects) {
		Facts facts = new Facts(objects);
		DecisionResults results = new DecisionResults();
		decisions.stream()
				.filter(decision -> getResponses(decision, facts))
				.map(decision -> new DecisionResult(decision.getDescription(), decision.getResponses()))
				.forEach(results::add);

		return results;
	}

	private boolean getResponses(Decision decision, Facts facts) {
		return decision.getConditions().stream()
				.allMatch(condition -> condition.solve(facts.getFact(condition.getFactName())
						.orElse(new EmptyValue())));
	}
}
