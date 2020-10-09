package nl.suriani.jadeval.models;

import java.util.ArrayList;
import java.util.List;

public class RuleSet {
	private List<Rule> rules = new ArrayList<>();
	private RulesSetType type;

	public RuleSet(List<Rule> rules, RulesSetType type) {
		this.rules = rules;
		this.type = type;
	}

	public RuleSet() {
	}

	public List<Rule> getRules() {
		return rules;
	}

	public RulesSetType getType() {
		return type;
	}
}
