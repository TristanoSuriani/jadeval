package nl.suriani.jadeval;

import nl.suriani.jadeval.common.Facts;
import nl.suriani.jadeval.common.internal.value.EmptyValue;
import nl.suriani.jadeval.decision.DecisionResult;
import nl.suriani.jadeval.decision.DecisionResults;

import java.util.ArrayList;
import java.util.List;

public class DecisionsDelegate {
	private JadevalModel model;

	public DecisionsDelegate(JadevalModel model) {
		this.model = model;
	}

	DecisionResults apply(Facts facts) {
		DecisionResults results = new DecisionResults();
		model.getRuleSet().getRules().stream()
				.map(decision -> new DecisionResult(decision.getDescription(), getResponses(decision, facts)))
				.forEach(results::add);

		return results;
	}

	private List<String> getResponses(Rule rule, Facts facts) {
		return allConditionsAreSatisfied(rule, facts) ? rule.getResponses() : new ArrayList<>();
	}

	private boolean allConditionsAreSatisfied(Rule rule, Facts facts) {
		return rule.getConditions().stream()
				.allMatch(condition -> condition.solve(facts.getFact(condition.getFactName())
						.orElse(new EmptyValue())));
	}
}
