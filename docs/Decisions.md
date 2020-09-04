# Jadeval
You can define decisions using Jadeval's Decisions Language or through the fluent API.

## Jadeval Decisions Language

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
public class CloseAccountExample {
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
        account.setDescription("custom");
        
        DecisionResults results = decisions.apply(new Facts(account, account.getOwner()),
                new File("src/examples/close_account.decisions"));
        
        results.getEvents().forEach(System.out::println);
        
        /* It prints:
            CLOSE_ACCOUNT
            SEND_CONFIRMATION_LETTER
            DEFAULT_DESCRIPTION
         */
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

    @Fact(qualifier = "customStuff")
    private String description;
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
set $minimum_age to 18

when age >= $minimum_age
    and canBeClosed is true
    and amount > 0
    then CLOSE_ACCOUNT
    and SEND_CONFIRMATION_LETTER

when customStuff == custom
    and amount >= 0
    then DEFAULT_DESCRIPTION
~~~~

## Jadeval Decisions Fluent API
It's possible to define the same rules with a Java fluent API, as follows:

````java
public class CloseAccountDecisionsBuilder extends DecisionsBuilder {
    private static final BigDecimal MINIMUM_AGE = BigDecimal.valueOf(18);
    
    @Override
    protected void compile() {
        decision()
            .when("age")
            .greatherThanEquals(MINIMUM_AGE)
            .and("amount")
            .greatherThan(toBigDecimal(0))
            .then("CLOSE_ACCOUNT")
            .and("SEND_CONFIRMATION_LETTER")
            .end();
    
        decision()
            .when("customStuff")
            .is("custom")
            .then("DEFAULT_DESCRIPTION")
            .end();
    }
}
````
The class CloseAccountExample will look almost exactly the same as the previous version:
````java
public class CloseAccountExample {
    public static void main(String[] args) {
        FluentDecisions decisions = new CloseAccountDecisionsBuilder().build();
        Person person = new Person();
        person.setAge(19);
        person.setFirstname("Piet");
        person.setLastname("de Haan");
        
        Account account = new Account();
        account.setOwner(person);
        account.setAmount(BigDecimal.valueOf(1234.56));
        account.setCanBeClosed(true);
        account.setDescription("custom");
        
        DecisionResults results = decisions.apply(account, account.getOwner());
        
        results.getEvents().forEach(System.out::println);
        
        /* It prints:
            CLOSE_ACCOUNT
            SEND_CONFIRMATION_LETTER
            DEFAULT_DESCRIPTION
         */
    }    
    
    // .....
}
````
