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

**Example:** Check [here](src/examples/nl/suriani/jadeval/examples/validation/jvl/) for example code.
