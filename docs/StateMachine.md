# Jadeval

## Jadeval State Machine Definition Language

It is possible to define state machines using Jadeval's Validation Language instead of writing Java code.
There are multiple advantages related to this:
- The state machine definitions read in plain English, making it immediately accessible to non technical people like domain experts
- It allows to store the state machine externally to the application, like in a filesystem, database or other application.

The following is an example of a valid state machine:
~~~~
/*
    This is a multiline comment.
    Useful, isn't it?
*/

// this is an inline comment, also very useful

states
    start, doSomething, wait, end

transitions
    start -> doSomething
    doSomething -> wait when action is setOnHold
    wait -> doSomething when action is removeHold and operationIsAuthorised is true
    doSomething -> end when action is complete
~~~~

Hereby the main concepts:

**states:**
A list of states

**Transitions:**
List of transitions (changes between states). They can be direct (without condition statements) or conditional.

**When:**
The keywords *valid when* indicates the beginning of a new validation rule, that starts with a condition. A condition can be composed
by multiple conditions separated by the keyword *and*.

**Comments:**
Inline comments start with // followed by text and they end at the end of the line.
Multiline comments start with /* and terminate with */.

## Facts
A fact is a portion of reality that is compared with the validation rules in order to decrete
which business events will be created. Check this example:
~~~~
when flagged is true and and amountInfractions > 3 amountDebt >= 2000
~~~~
In this condition statement **flagged**, **amountInfractions** and **amountDebt** are fact names; **true**, **3** and **2000** are the expected fact values;
 **is**, **>** and **>=** are equality operators.
The actual fact values are provided to the library as Java objects.

**Example:** Check [here](/src/examples/nl/suriani/jadeval/examples/statemachine/jsl/) for example code.
