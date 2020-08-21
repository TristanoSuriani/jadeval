package nl.suriani.jadeval.rule;

import java.util.List;

/**
 * Object that wraps the rules and allow to apply them
 * @param <T> The type of the facts to be validated
 */
public class Rules<T> {
	/**
	 * Rules to be applied, not null
	 */
	private List<Rule<T>> decisions;


	public Rules(List<Rule<T>> decisions) {
		this.decisions = decisions;
	}

	/**
	 * Applies the rules to the facts
	 * @param facts facts to be checked
	 */
	public void apply(T facts) {
		decisions.stream()
				.filter(decision -> decision.test(facts))
				.forEach(rule -> rule.getAction().accept(facts));
	}
}
