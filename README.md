# Jadeval
### Just A DEcision and VAlidation Library (and actually other things too)

##Introduction
Jadeval is an open source library that allows to define business decisions, validations and workflows, 
through Java fluent APIs and own domain specific languages for maximum flexibility.

##Main concepts
###Decisions
Business decisions defines or constrains some aspect of business and resolves with a set of responses.
Think about the sentence:
~~~~
When the customer's age is at least 18 years old and his salary at least 40000 and no previous breaches are found then approve loan
~~~~

In this example the customer's age, his salary and the presence of previous breaches are the facts that are used to take
 the decision and 'approve loan' is the response.
 
[Jadeval Decisions Language](docs/Decisions.md) allows to define this decision in almost plain English: 
~~~~
when customerAge >= 18 and hisSalary >= 40000 and foundPreviousBreaches is false then APPROVE_LOAN
~~~~

###Validations
Validations constrant aspects of business and resolves either without a response (sucess) or a validation exception (failure).
Think about the sentence:
~~~~
When the customer's last name is unknown stop the process and notify the error
~~~~
[Jadeval Validations Fluent API](docs/Validations.md) allows to define validations like this one with a declarative approach. Follow the link for examples.


###Workflows
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

[Jadeval Workflow Definition language](docs/Workflow.md) allow to define workflows in a formal but easy to read fashion.
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

##Import Jadeval in your project with Maven
To import Jadeval simply add this Maven dependency:
````xml
<dependency>
    <groupId>nl.suriani</groupId>
    <artifactId>jadeval</artifactId>
    <version>xxx</version>
</dependency>
````

##Status of the library
Jadeval is currently in alpha version. This means that it can change considerably between versions,
the stability and absence of major bugs in not guaranteed and it should therefore not be used for production code.

##Roadmap
The following features are planned for the next releases of Jadeval:
- Java Fluent API for Decisions
- Jadeval Validations Language
- Java Fluent API for Workflows
- *Jadeval Workbench*, graphical tool to generate and test decisions, validations and workflows.
- Expand Jadeval's Decisions model
