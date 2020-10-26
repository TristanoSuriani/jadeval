# Jadeval

## Jadeval Decision Language

It is possible to write decisions using Jadeval's Decision Language instead of writing Java code.
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

"not enough money in your bank account"
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
responses are returned the names of the responses are separated by the keyword *and*.

**Comments:**
Inline comments start with // followed by text and they end at the end of the line.
Multiline comments start with /* and terminate with */.

## Facts
A fact is a portion of reality that is compared with the decision rules in order to decrete
which responses will be returned. Check this example:
~~~~
when flagged is true and amountInfractions > 3 and amountDebt >= 2000 then SEND_CONVOCATION_LETTER and ADD_TO_PRIORITY_LIST
~~~~
In this decision statement **flagged**, **amountInfractions** and **amountDebt** are fact names; **true**, **3** and **2000** are the expected fact values;
 **is**, **>** and **>=** are equality operators.
The actual fact values are provided to the library as Java objects.

**Example:** Check [here](src/examples/nl/suriani/jadeval/examples/decisions/jdl/) for example code.
