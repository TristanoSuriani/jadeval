# Jadeval
### It was Just A DEcision and VAlidation Library (but now it's much more than that)

## Introduction
Jadeval is lightweight open source library that allows to define fact based complex computational models as business decisions, validations and workflows (more is coming), 
through its own domain specific language for maximum flexibility.

## Main concepts
### Decisions
Business decisions defines or constraints some aspect of business and resolves with a set of responses.
Think about the sentence:
~~~~
When the customer's age is at least 18 years old and his salary at least 40000 and no previous breaches are found then approve loan
~~~~

In this example the customer's age, his salary and the presence of previous breaches are the facts that are used to take
 the decision and 'approve loan' is the response.
 
[Jadeval Decision Language](docs/Decision.md) allows to define this decision in almost plain English: 
~~~~
when customerAge >= 18 and hisSalary >= 40000 and foundPreviousBreaches is false then APPROVE_LOAN
~~~~

### Validations
Validations constrant aspects of business and resolves either without a response (sucess) or a validation exception (failure).
Think about the sentence:
~~~~
When the customer's account is blocked stop the process and notify the error
~~~~

[Jadeval Validation Language](docs/Validation.md) allows to define this decision in almost plain English: 
~~~~
valid when accountStatus is not blocked
~~~~

### Workflows
A workflow consists of an orchestrated and repeatable pattern of activity. Elements of a workflow include states, 
transitions between states and the rules the regulate them, as well as task performed as consequence of a state transition.
Think about this set of sentences:
~~~~
Start playing this card game.
Get cards (they will be 37 in total, divided by the two players. The only queen in the deck will be the queen of spades).
Choose a card from opponent's deck.
Offer a card to your opponent.
If you have matching cards (for example two cards with the value 3) you may discard them.
Repeat until both players discarded all their cards and one of them remained with
If you remained with the queen of spades you lost. 
~~~~

Now reorganise this use case as a set of events. You can find for example:
~~~~
GameStarted
GotCards
ChosenCardFromOpponentDeck
CardsDiscarded
CardOfferedToOpponent
YouWon
YouLost
~~~~
You may realise that _GameStarted_ is the entrypoint (root state), _YouWon_ and _YouLost_ are final states
and all the other states are intermediate states. Hereby a brief explanation:
- *Root states* may flow into intermediate and final states and no state can flow into them
- *Intermediate states* may flow to intermediate and final states and can be reached by root and intermediate states
- *Final states* can be reached by non-final states and cannot flow to any other state

Now you can defined how these states flow into the others:
~~~~
from GameStarted to GotCards

from GotCards to ChosenCardFromOpponentDeck

from ChosenCardFromOpponentDeck to YouLost if you have only the queen of spades and your opponent finished the cards
from ChosenCardFromOpponentDeck to CardsDiscarded if there are matching cards
from ChosenCardFromOpponentDeck to CardOfferedToOpponent if there aren't matching cards

from CardsDiscarded to CardOfferedToOpponent if there are more than one card in your deck
from CardsDiscarded to YouWon if you finished your cards and your opponent has only the queen of spades
from CardsDiscarded to ChosenCardFromOpponentDeck if the sum of the cards in both players' decks is greather then 1
~~~~

[Jadeval Workflow Definition language](docs/Workflow.md) allows to define workflows in a formal but easy to read fashion.
Defining this example would like something like this:
~~~~
root states
    GameStarted

intermediateStates
    GotCards
    ChosenCardFromOpponentDeck
    CardsDiscarded
    CardOfferedToOpponent

final states
    YouWon
    YouLost

transitions
    GameStarted -> GotCards
    GotCards -> ChosenCardFromOpponentDeck

    ChosenCardFromOpponentDeck -> YouLost when numberOfCardsInOpponentDeck is 0 and numberOfCardsInYourDeck is 1 and lastCard is queenOfSpades
    ChosenCardFromOpponentDeck -> CardsDiscarded when thereAreMatchingCards is true
    ChosenCardFromOpponentDeck -> CardOfferedToOpponent when thereAreMatchingCards is false
    
    CardsDiscarded -> CardOfferedToOpponent when numberOfCardsInYourDeck > 1
    CardsDiscarded -> YouWon when numberOfCardsInYourDeck is 0 and opponentHasQueenOfSpades is true
    CardsDiscarded -> ChosenCardFromOpponentDeck when sumOfCardsInBothDecks > 1
~~~~

### State machines
A state machine is similar to a workflow, with a couple of differences:
- there are no distinction between root, intermediate and final states
- the state machine does not need to end in a specific state. It doesn't need to end at all.
- there is no 'play until pause' mechanism. It is possible to move only one step at a time, regardless of the transition being
conditional or direct.

[Jadeval State machine Definition Language](docs/StateMachine.md) allows to formally define state machines.
Hereby an example of a state machine:
~~~~
state machine
    constants
        $daysToNewSprint = 3

    states
        sprintPlanningPrepared, storyPointsAssigned, sprintGoalDefined, oldSprintClosed, newSprintStarted, sprintInProgress

    transitions
        sprintPlanningPrepared -> storyPointsAssigned when activity is assignStoryPoints
        storyPointsAssigned -> sprintGoalDefined when activity is defineSprintGoal
        sprintGoalDefined -> oldSprintClosed when activity is closeOldSprint
        oldSprintClosed -> newSprintStarted when activity is startNewSprint and sprintApprovedByPO is true
        newSprintStarted -> sprintInProgress
        sprintInProgress -> sprintPlanningPrepared when activity is prepareSprintPlanning and daysToNewSprint == $daysToNewSprint
~~~~

## Import Jadeval in your project with Maven
To import Jadeval simply add this Maven dependency:
````xml
<dependency>
    <groupId>nl.suriani</groupId>
    <artifactId>jadeval</artifactId>
    <version>xxx</version>
</dependency>
````

## State of the library
Jadeval is currently in alpha version. This means that it can change considerably between versions,
the stability and absence of major bugs in not guaranteed, and it should therefore not be used for production code.

## Roadmap
The following features are planned for the next releases of Jadeval:
- Automatic scan of resource definition files
- Jadeval XML DSL
- Support to compare between facts
- Support for Empty Value in facts
- Facts expressions
- Translation of semantic models to diagrams in DOT language for integration with Graphviz.
- Several improvements to stability and APIs
- Jadeval Extensions API?

