package nl.suriani.jadeval.examples;

import nl.suriani.jadeval.rule.RulesBuilder;

class LocationRulesBuilder extends RulesBuilder<Location> {
	@Override
	protected void compile() {
		rule("Tutte le strade portano a Roma")
				.when(location -> location.getName().equals("Roma") || location.getName().equals("Rome"))
				.then(this::printAllTheStreetsBringToLocationName)
				.andThen(location -> System.out.println(location.getName() + ", the eternal city"))
				.end();

		rule("A city is a big city if it has more than 1M inhabitants and it's not called Frank")
				.when(location -> location.getInhabitants() > 1_000_000)
				.and(location -> !location.getName().equalsIgnoreCase("Frank"))
				.then(location -> System.out.println(location.getName() + " is hella big!"))
				.end();

		rule("No place can hava 123456 as a zipcode")
				.when(location -> location.getZipCode().equals("123456"))
				.then(location -> System.out.println(location.getName()
						+ " has the invalid zipcode" + location.getZipCode() + ". Shame!"))
				.end();

	}

	private void printAllTheStreetsBringToLocationName(Location location) {
		System.out.println("Tutte le strade portano a " + location.getName());
	}
}
