package nl.suriani.jadeval.examples;

import nl.suriani.jadeval.rule.Rules;


class LocationRulesBuilderExample {
	public static void main(String[] args) {
		LocationRulesBuilder rulesBuilder = new LocationRulesBuilder();
		Rules<Location> locationRules = rulesBuilder.build();
		Location roma = new Location("Roma", "00100", "Roma", 3_700_000);
		locationRules.apply(roma);
		/*
			prints:
			Tutte le strade portano a Roma
			Roma, the eternal city
			Roma is hella big!
		 */
	}
}
