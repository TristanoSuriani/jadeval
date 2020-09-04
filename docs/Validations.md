# Jadeval
## Jadeval Validations JAVA DSL
## Fluent interface for validations example
First subclass ValidationsBuilder with a parameter type that correspond to the root object you want to run the business rules against:

````java
public class TrainValidationsBuilder extends ValidationsBuilder<Train> {
    @Override
    public void compile() {
        validation("The train must have at least 100 chairs and 3 wagons")
                .when(always())
                .then(train -> train.chairs >= 100)
                .and(train -> train.wagons >= 3)
                .orElseThrow(AbnormallySmallTrainException::new);

        validation("A train cannot be called Frank")
                .when(always())
                .then(train -> !train.name.equals("Frank"))
                .orElseThrow(TrainIsCalledFrankValidationException::new);

        validation("An intercity of type A must have at least 5 wagons and 200 chairs")
                .when(itIsOfType(Intercity.class))
                .and(train -> ((Intercity) train).getType() == IntercityType.A)
                .then(train -> train.wagons >= 5)
                .and(train -> train.chairs >= 200)
                .orElseThrow(DepressingIntercityTypeAException::new);
    }
}
````
Now let's validate!
````java
ValidationsBuilder<Train> validationsBuilder = new TrainValidationsBuilder();
Validations<Train> validations = validationsBuilder.build();

Assertions.assertThrows(AbnormallySmallTrainException.class, () -> validations.apply(new Train("Train", 1, 100)));
Assertions.assertThrows(TrainIsCalledFrankValidationException.class, () -> validations.apply(new Train("Frank", 4, 199)));
Assertions.assertThrows(DepressingIntercityTypeAException.class, () -> validations.apply(new Intercity("Damien", 4, 199, IntercityType.A)));
````
For more examples, check [here](src/examples/nl/suriani/jadeval/examples)
