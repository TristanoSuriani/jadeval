package nl.suriani.jadeval.workflow;

import nl.suriani.jadeval.common.Facts;
import nl.suriani.jadeval.common.annotation.Fact;
import nl.suriani.jadeval.common.condition.EqualitySymbolFactory;
import nl.suriani.jadeval.workflow.annotation.State;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WorkflowExecutorTest {
	public static final String PLAYER_1_JOINS = "PLAYER1_JOINS";
	private static final String PLAYER_2_JOINS = "PLAYER2_JOINS";
	private static final String PLAYER_1_CHOOSES = "PLAYER1_CHOOSES";
	private static final String PLAYER_2_CHOOSES = "PLAYER2_CHOOSES";
	private static final String PLAYER_1_SCORES = "PLAYER1_SCORES";
	private static final String PLAYER_2_SCORES = "PLAYER2_SCORES";
	private static final String NOBODY_SCORES = "NOBODY_SCORES";
	private Workflow workflow;
	private WorkflowCompiler workflowCompiler;
	private WorkflowConditionFactory workflowConditionFactory;
	private EqualitySymbolFactory equalitySymbolFactory;
	private StateUpdateEventHandler eventHandler;
	private WorkflowExecutor executor;

	private final static String USER_EVENT = "userEvent";
	private final static String SYSTEM_EVENT = "systemEvent";

	@BeforeEach
	void setUp() {
		equalitySymbolFactory = new EqualitySymbolFactory();
		workflowConditionFactory = new WorkflowConditionFactory(equalitySymbolFactory);
		workflowCompiler = new WorkflowCompiler(workflowConditionFactory);
		workflow = new Workflow(workflowCompiler, new ArrayList<>());
		File file = new File("src/test/resources/workflow.jwl");
		executor = workflow.build(file);
		eventHandler = Mockito.mock(StateUpdateEventHandler.class);
	}

	@Test
	void test_waitingForPlayer1ToJoin__waitingForPlayer2ToJoin_whenUserEventIsPlayer1Joins() {
		String fromState = "waitingForPlayer1ToJoin";
		String expectedToState = "waitingForPlayer2ToJoin";
		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put(USER_EVENT, PLAYER_1_JOINS);
		Facts facts = new Facts(factsMap);

		assertTransitionEquals(facts, fromState, expectedToState);
	}

	@Test
	void test_waitingForPlayer1ToJoin__waitingForPlayer2ToJoin_ConditionNotMet() {
		String fromState = "waitingForPlayer1ToJoin";
		String expectedToState = fromState;
		Map<String, Object> factsMap = new HashMap<>();
		Facts facts = new Facts(factsMap);

		assertTransitionEquals(facts, fromState, expectedToState);
	}

	@Test
	void test_waitingForPlayer2ToJoin_bothPlayersJoined_whenUserEventIsPlayer2Joins() {
		String fromState = "waitingForPlayer2ToJoin";
		String expectedToState = "bothPlayersJoined";
		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put(USER_EVENT, PLAYER_2_JOINS);
		Facts facts = new Facts(factsMap);

		assertTransitionEquals(facts, fromState, expectedToState);
	}

	@Test
	void test_bothPlayersJoined_waitingForBothPlayersToChoose() {
		String fromState = "bothPlayersJoined";
		String expectedToState = "waitingForBothPlayersToChoose";
		Facts facts = new Facts();

		assertTransitionEquals(facts, fromState, expectedToState);
	}

	@Test
	void test_waitingForBothPlayersToChoose_waitingForPlayer1ToChoose_when_userEvent_is_player1Chooses() {
		String fromState = "waitingForBothPlayersToChoose";
		String expectedToState = "waitingForPlayer2ToChoose";
		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put(USER_EVENT, PLAYER_1_CHOOSES);
		Facts facts = new Facts(factsMap);

		assertTransitionEquals(facts, fromState, expectedToState);
	}

	@Test
	void test_waitingForBothPlayersToChoose_waitingForPlayer2ToChoose_when_userEvent_is_player2Chooses() {
		String fromState = "waitingForBothPlayersToChoose";
		String expectedToState = "waitingForPlayer1ToChoose";
		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put(USER_EVENT, PLAYER_2_CHOOSES);
		Facts facts = new Facts(factsMap);

		assertTransitionEquals(facts, fromState, expectedToState);
	}

	@Test
	void test_waitingForPlayer1ToChoose_bothPlayersHaveChosen_when_userEvent_is_player1Chooses() {
		String fromState = "waitingForPlayer1ToChoose";
		String expectedToState = "bothPlayersHaveChosen";
		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put(USER_EVENT, PLAYER_1_CHOOSES);
		Facts facts = new Facts(factsMap);

		assertTransitionEquals(facts, fromState, expectedToState);
	}

	@Test
	void test_waitingForPlayer2ToChoose_bothPlayersHaveChosen_when_userEvent_is_player2Chooses() {
		String fromState = "waitingForPlayer2ToChoose";
		String expectedToState = "bothPlayersHaveChosen";
		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put(USER_EVENT, PLAYER_2_CHOOSES);
		Facts facts = new Facts(factsMap);

		assertTransitionEquals(facts, fromState, expectedToState);
	}

	@Test
	void test_bothPlayersHaveChosen_player1HasScored_when_systemEvent_is_player1Scores() {
		String fromState = "bothPlayersHaveChosen";
		String expectedToState = "player1HasScored";
		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put(SYSTEM_EVENT, PLAYER_1_SCORES);
		Facts facts = new Facts(factsMap);

		assertTransitionEquals(facts, fromState, expectedToState);
	}

	@Test
	void test_bothPlayersHaveChosen_player2HasScored_when_systemEvent_is_player2Scores() {
		String fromState = "bothPlayersHaveChosen";
		String expectedToState = "player2HasScored";
		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put(SYSTEM_EVENT, PLAYER_2_SCORES);
		Facts facts = new Facts(factsMap);

		assertTransitionEquals(facts, fromState, expectedToState);
	}

	@Test
	void test_bothPlayersHaveChosen_nobodyHasScored_when_systemEvent_is_nobodyScores() {
		String fromState = "nobodyHasScored";
		String expectedToState = "waitingForBothPlayersToChoose";
		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put(SYSTEM_EVENT, NOBODY_SCORES);
		Facts facts = new Facts(factsMap);

		assertTransitionEquals(facts, fromState, expectedToState);
	}

	@Test
	void test_player1HasScored_player1HasWon_when_player1Score_greaterThanEquals_maximumScore() {
		String fromState = "player1HasScored";
		String expectedToState = "player1HasWon";
		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put("player1Score", 3);
		Facts facts = new Facts(factsMap);

		assertTransitionEquals(facts, fromState, expectedToState);
	}

	@Test
	void test_player1HasScored_player1HasWon_otherwise_waitingForBothPlayersToChoose() {
		String fromState = "player1HasScored";
		String expectedToState = "waitingForBothPlayersToChoose";
		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put("player1Score", 2);
		Facts facts = new Facts(factsMap);

		assertTransitionEquals(facts, fromState, expectedToState);
	}

	@Test
	void test_player2HasScored_player2HasWon_when_player1Score_greaterThanEquals_maximumScore() {
		String fromState = "player2HasScored";
		String expectedToState = "player2HasWon";
		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put("player2Score", 3);
		Facts facts = new Facts(factsMap);

		assertTransitionEquals(facts, fromState, expectedToState);
	}

	@Test
	void test_player2HasScored_player2HasWon_otherwise_waitingForBothPlayersToChoose() {
		String fromState = "player2HasScored";
		String expectedToState = "waitingForBothPlayersToChoose";
		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put("player2Score", 2);
		Facts facts = new Facts(factsMap);

		assertTransitionEquals(facts, fromState, expectedToState);
	}

	@Test
	void test_nobodyHasScored_waitingForBothPlayersToChoose() {
		String fromState = "nobodyHasScored";
		String expectedToState = "waitingForBothPlayersToChoose";
		Facts facts = new Facts();

		assertTransitionEquals(facts, fromState, expectedToState);
	}

	@Test
	void test_cancel_by_player1() {
		List<String> fromStates = Arrays.asList(
				"waitingForPlayer2ToJoin",
				"waitingForBothPlayersToChoose",
				"waitingForPlayer1ToChoose",
				"waitingForPlayer2ToChoose",
				"bothPlayersHaveChosen",
				"player1HasScored",
				"player2HasScored");

		String expectedToState = "player1IsDisconnected";

		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put("userEvent", "PLAYER1_DISCONNECTS");
		Facts facts = new Facts(factsMap);

		fromStates.forEach(fromState -> assertTransitionEquals(facts, fromState, expectedToState));
	}

	@Test
	void test_cancel_by_player2() {
		List<String> fromStates = Arrays.asList(
				"waitingForBothPlayersToChoose",
				"waitingForPlayer1ToChoose",
				"waitingForPlayer2ToChoose",
				"bothPlayersHaveChosen",
				"player1HasScored",
				"player2HasScored");

		String expectedToState = "player2IsDisconnected";

		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put("userEvent", "PLAYER2_DISCONNECTS");
		Facts facts = new Facts(factsMap);

		fromStates.forEach(fromState -> assertTransitionEquals(facts, fromState, expectedToState));
	}

	@Test
	void test_eventHandler_enterState_and_exitState_are_called_1_time() {
		workflow = new Workflow(workflowCompiler, getEventHandlers());
		File file = new File("src/test/resources/todo_workflow.jwl");
		executor = workflow.build(file);

		when(eventHandler.getStateName()).thenReturn("IN_PROGRESS");

		ToDoBoardFacts facts = new ToDoBoardFacts("start", ToDoState.TO_DO);
		executor.updateState(facts);
		verify(eventHandler, times(1)).enterState(any());

		facts = new ToDoBoardFacts("cancel", ToDoState.IN_PROGRESS);
		executor.updateState(facts);
		verify(eventHandler, times(1)).exitState(any());
	}

	@Test
	void test_condition_is_correctly_processed_when_facts_are_missing() {
		workflow = new Workflow(workflowCompiler, new ArrayList<>());
		File file = new File("src/test/resources/todo_workflow.jwl");
		executor = workflow.build(file);

		when(eventHandler.getStateName()).thenReturn("IN_PROGRESS");

		ToDoBoardFacts facts = new ToDoBoardFacts(null, ToDoState.TO_DO);
		executor.updateState(facts);

		facts = new ToDoBoardFacts("cancel", ToDoState.IN_PROGRESS);
		executor.updateState(facts);
	}

	@Test
	void test_synchronise_state_after_next_state_retrieval() {
		workflow = new Workflow(workflowCompiler, new ArrayList<>());
		File file = new File("src/test/resources/todo_workflow.jwl");
		executor = workflow.build(file);

		when(eventHandler.getStateName()).thenReturn("IN_PROGRESS");

		ToDoBoardFacts facts = new ToDoBoardFacts("cancel", ToDoState.TO_DO);
		executor.updateState(facts);
		Assertions.assertEquals(ToDoState.CANCELLED, facts.state);
	}

	private void assertTransitionEquals(Facts facts, String fromState, String toState) {
		Assertions.assertEquals(toState, executor.getNextState(fromState, facts));
	}

	private List<StateUpdateEventHandler<ToDoBoardFacts>> getEventHandlers() {
		return Collections.singletonList(eventHandler);
	}

	private class ToDoBoardFacts {
		@Fact
		private String userAction;

		@State
		private ToDoState state;

		public ToDoBoardFacts(String userAction, ToDoState state) {
			this.userAction = userAction;
			this.state = state;
		}
	}

	private enum ToDoState {
		TO_DO, IN_PROGRESS, ON_HOLD, DONE, CANCELLED
	}
}
