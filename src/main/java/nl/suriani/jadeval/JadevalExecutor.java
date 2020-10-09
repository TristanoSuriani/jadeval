package nl.suriani.jadeval;

import nl.suriani.jadeval.common.Facts;
import nl.suriani.jadeval.common.internal.value.EmptyValue;
import nl.suriani.jadeval.decision.DecisionResult;
import nl.suriani.jadeval.decision.DecisionResults;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JadevalExecutor {
	private JadevalModel jadevalModel;

	public JadevalExecutor(JadevalModel jadevalModel) {
		this.jadevalModel = jadevalModel;
	}

	public DecisionResults applyDecisions(Object... objects) {
		return applyDecisions(new Facts(objects));
	}

	public DecisionResults applyDecisions(Map<String, Object> factsMap) {
		return applyDecisions(new Facts(factsMap));
	}

	private DecisionResults applyDecisions(Facts facts) {
		DecisionResults results = new DecisionResults();
		jadevalModel.getRuleSet().getRules().stream()
				.map(decision -> new DecisionResult(decision.getDescription(), getResponses(decision, facts)))
				.forEach(results::add);

		return results;
	}

	List<String> getResponses(Rule rule, Facts facts) {
		return allConditionsAreSatisfied(rule, facts) ? rule.getResponses() : new ArrayList<>();
	}

	private boolean allConditionsAreSatisfied(Rule rule, Facts facts) {
		return rule.getConditions().stream()
				.allMatch(condition -> condition.solve(facts.getFact(condition.getFactName())
						.orElse(new EmptyValue())));
	}
}
