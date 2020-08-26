# Jadeval
### Just A DEcision and VAlidation Library

Jadeval is a free, simple library for executing business rules, decisions and validations. You can do so by using the Java fluent 
interface and Jadeval's own decision and validation DSLs.

## Rules
Rules are composed by conditions and actions and are performed against a subject 
Think about the sentence: 

~~~~
If there are less than 100 euros on a bank account, send a notification
~~~~

In this example the subject is *'a bank account'*, the condition is *'there are less than 100 euros \[on the bank account\]'* and the action is *'send a notification 
[using some information found in the bank account]'*.
When the condition is satisfied the action is performed.

## Decisions
Decisions are similar to the rules, but instead of running an action when the condition is satisfied,
a business event is generated. Think about the sentence:
~~~~
If there are less than 100 euros on a bank account, generate event: SEND_NOTIFICATION
~~~~
In this case the output will be the event itself which holds a certain meaning and can be coupled to specific actions to be performed later in the process.

## Validations
Validations are also similar to rules, but are meant to check the validity of a certain subject.
If the check fails, a validation error will be raised. If the check succeeds, there will be no consequence.
Think about the sentence:
~~~~
A city must have at least 10000 inhabitants
~~~~

## Fluent interface for rules example
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

## Jadeval's own DSL for decisions
You can write decisions using Jadeval's own DSL instead of writing Java code.
There are multiple advantages related to this:
- The decision statements read in plain English, making it immediately accessible to non technical people like domain experts
- It allows to store the decisions externally to the application, like in a filesystem, database or other application.
- If a decision creates issues in production, is possible to correct it or remove it without downtime and without a new deployment

The followings are all valid decisions:
~~~~
/*
    This is a multiline comment.
    Useful, isn't it?
*/
"send notification when there aren't items in stock anymore" // that was a rule description, this is an inline comment
when itemsInStock == 0 then SEND_NO_ITEMS_IN_STOCK_NOTIFICATION

// the next rule doesn't have a description
when productType is clothing and status is ACTIVE then DO_THIS and DO_THAT

# not enough money in your bank account
when amount <= 1234.56 then SEND_UNSATISFYING_AMOUNT_EMAIL and SEND_UNSATISFYING_AMOUNT_NOTIFICATION

when connected not true then REMOVE_LOCK and /* I can add a multiline comment here, no problem! */ LOG_USER_DISCONNECTED

when flagged is true and amountInfractions > 3 and amountDebt > 2000 then SEND_CONVOCATION_LETTER and ADD_TO_PRIORITY_LIST
~~~~

**Rule description:**
A rule description consist in a text wrapped in double quotes (example: "this is a description"). It is optional.

**When:**
The keyword *when* indicates the beginning of a new decision rule, that starts with a condition. A condition can be composed
by multiple conditions separated by the keyword *and*.

**Then:**
The keyword *then* indicates which event will be returned in case the condition is satisfied. In case multiple
events are returned the names of the events are separated by the keyword *and*.

**Comments:**
Inline comments start with // followed by text and they end at the end of the line.
Multiline comments start with /* and terminate with */.

## Facts
A fact is a portion of reality that is compared with the decision rules in order to decrete
which business events will be created. Check this example:
~~~~
when flagged is true and amountInfractions > 3 and amountDebt >= 2000 then SEND_CONVOCATION_LETTER and ADD_TO_PRIORITY_LIST
~~~~
In this decision statement **flagged**, **amountInfractions** and **amountDebt** are fact names; **true**, **3** and **2000** are the expected fact values;
 **is**, **>** and **>=** are equality operators.
The actual fact values are provided to the library through the **Facts** object.

**Example**
Check [CloseAccountExample.java](src/examples/nl/suriani/jadeval/examplesCloseAccountExample.java) and [close_account.decisions](src/examples/nl/suriani/jadeval/examplesclose_account.decisions) for the complete code.

````java
class CloseAccountExample {
	public static void main(String[] args) {
		Decisions decisions = new Decisions();
		Person person = new Person();
		person.setAge(19);
		person.setFirstname("Piet");
		person.setLastname("de Haan");

		Account account = new Account();
		account.setOwner(person);
		account.setAmount(BigDecimal.valueOf(1234.56));
		account.setCanBeClosed(true);

		DecisionsResultsTable decisionsResultsTable = decisions.apply(new Facts(account, account.getOwner()), new File("src/examples/close_account.decisions"));
		decisionsResultsTable.getEvents().stream()
				.filter(event -> event.equalsIgnoreCase("CLOSE_ACCOUNT"))
				.findFirst()
				.ifPresent(System.out::println);

		// It prints "CLOSE_ACCOUNT".
	}    
    
    // .....
}
````

The facts are retrieved in this example with the statement *Facts.fromObjects(account, account.getOwner())*.
In order to mark a class member as a fact you can use the annotation @Fact:
````java
public static class Account {
    private Person owner;

    @Fact
    private BigDecimal amount;

    @Fact
    private boolean canBeClosed;
    // ...
}
````


````java
public static class Person {
    private String firstname;

    private String lastname;

    @Fact
    private int age;
    // ...
}
````

By providing the objects account (of type Account) and account.getOwner (of type Person) the following
facts are retrieved:
- amount (with value 1234.56)
- canBeClosed (with value true)
- age (with value 19).

These facts are checked against the rule:
~~~~
when age >= 18
    and canBeClosed is true
    and amount > 0
    then CLOSE_ACCOUNT
    and SEND_CONFIRMATION_LETTER
~~~~

which applies, so the business events *CLOSE_ACCOUNT* and *SEND_CONFIRMATION_LETTER* are returned.

For more examples, check [here](src/examples/nl/suriani/jadeval/examples)

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
