# Jadeval

## Jadeval Validation Language

It is possible to write validations using Jadeval's Validation Language instead of writing Java code.
There are multiple advantages related to this:
- The validation statements read in plain English, making it immediately accessible to non technical people like domain experts
- It allows to store the validations externally to the application, like in a filesystem, database or other application.
- If a validation creates issues in production, is possible to correct it or remove it without downtime and without a new deployment

The followings are all valid validations:
~~~~
/*
    This is a multiline comment.
    Useful, isn't it?
*/
"There must be at least one item in stock" // that was a rule description, this is an inline comment
valid when itemsInStock >= 1

// the next rule doesn't have a description
valid when productType is clothing and status is ACTIVE

"you need a decent amount of money in your bank account"
valid when amount <= 1234.56

valid when connected is not true
valid when flagged is true and amountInfractions > 3 and amountDebt > 2000
~~~~

**Rule description:**
A rule description consist in a text wrapped in double quotes (example: "this is a description"). It is optional.

**Valid when:**
The keywords *valid when* indicates the beginning of a new validation rule, that starts with a condition. A condition can be composed
by multiple conditions separated by the keyword *and*.

**Comments:**
Inline comments start with // followed by text and they end at the end of the line.
Multiline comments start with /* and terminate with */.

## Facts
A fact is a portion of reality that is compared with the validation rules in order to decrete
which business events will be created. Check this example:
~~~~
valid when flagged is true and and amountInfractions > 3 amountDebt >= 2000
~~~~
In this validation statement **flagged**, **amountInfractions** and **amountDebt** are fact names; **true**, **3** and **2000** are the expected fact values;
 **is**, **>** and **>=** are equality operators.
The actual fact values are provided to the library as Java objects.

**Example**
Check [here](src/examples/nl/suriani/jadeval/examples/validation/jvl/) for the complete code.

````java
class ValidateOrderExample {
    public static void main(String[] args) {
        File file = new File("src/examples/nl/suriani/jadeval/examples/validation/jvl/validate_order.jvl");
        JadevalModel model = new JadevalLoader().load(file);
        JadevalExecutor jadevalExecutor = new JadevalExecutor(model);

        Product product1 = new Product(10);
        Account account1 = new Account(false);
        Order order1 = new Order(2, product1, account1);

        jadevalExecutor.applyValidations(new OrderValidationContext(order1));
        // This validation succeeds.

        Product product2 = new Product(10);
        Account account2 = new Account(false);
        Order order2 = new Order(11, product2, account2);

        try {
            jadevalExecutor.applyValidations(new OrderValidationContext(order2));
        } catch (ValidationException validationException) {
            System.out.println(validationException.getMessage());
        }
        /* This validation fails and prints:
            Validation 'The amount of product must be in stock':
            false IS true (itemsInStockGreaterThanAmount)
         */

        Product product3 = new Product(10);
        Account account3 = new Account(false);
        Order order3 = new Order(0, product3, account3);

        try {
            jadevalExecutor.applyValidations(new OrderValidationContext(order3));
        } catch (ValidationException validationException) {
            System.out.println(validationException.getMessage());
        }
        /* This validation fails and prints:
            Validation 'The amount of product must be in stock':
            0 GREATER_THAN_EQUALS 1.0 (amount)
         */

        Product product4 = new Product(10);
        Account account4 = new Account(true);
        Order order4 = new Order(1, product3, account4);
        try {
            jadevalExecutor.applyValidations(new OrderValidationContext(order4));
        } catch (ValidationException validationException) {
            System.out.println(validationException.getMessage());
        }
        /* This validation fails and prints:
            Validation 'The order cannot be processed for customers with a blocked account':
            true IS false (blockedAccount)
         */
    }
    // ...
}
````
The objects passed as an argument for _validations.apply()_ are processed in order to extract the facts.
In order to mark a class member as a fact the annotation @Fact is used:
````java
static class OrderValidationContext {
    @Fact
    private int amount;

    @Fact
    private boolean blockedAccount;

    @Fact
    private boolean itemsInStockGreaterThanAmount;

    public OrderValidationContext(Order order) {
        this.amount = order.getAmount();
        this.blockedAccount = order.getAccount().isBlocked();
        this.itemsInStockGreaterThanAmount = order.getProduct().getItemsInStock() >= order.getAmount();
    }
}
````
The file _validate_order.jvl_ looks like this:
````
validations
    "The amount of product must be in stock"
    valid when amount >= 1
        and itemsInStockGreaterThanAmount is true

    "The order cannot be processed for customers with a blocked account"
    valid when blockedAccount is false
````
