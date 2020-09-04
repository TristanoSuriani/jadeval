# Jadeval
## Jadeval Rules Java DSL

First subclass RulesBuilder with a parameter type that correspond to the root object you want to run the business rules against:
````Java
public class LocationRulesBuilder extends RulesBuilder<Location> {
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
		
        rule("No place can have 123456 as a zipcode")
                .when(location -> location.getZipCode().equals("123456"))
                .then(location -> System.out.println(location.getName() + " has the invalid zipcode" + location.getZipCode() + ". Shame!"))
                .end();
	}

	private void printAllTheStreetsBringToLocationName(Location location) {
		System.out.println("Tutte le strade portano a " + location.getName());
	}
}
````
PRO tip: don't forget to add .end() to terminate the rule definition.

Now it's time to check the rules!
````Java
...
Rules<Location> locationRules = rulesBuilder.build();
Location roma = new Location("Roma", "00100", "Roma", 3_700_000);
locationRules.apply(roma);
````

As expected, the output for this example will be:
~~~~
Tutte le strade portano a Roma
Roma, the eternal city
Roma is hella big!
~~~~
For more examples, check [here](src/examples/nl/suriani/jadeval/examples)
