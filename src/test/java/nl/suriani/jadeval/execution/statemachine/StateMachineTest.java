package nl.suriani.jadeval.execution.statemachine;

import nl.suriani.jadeval.JadevalLoader;
import nl.suriani.jadeval.annotation.Fact;
import nl.suriani.jadeval.annotation.State;
import nl.suriani.jadeval.execution.JadevalExecutor;
import nl.suriani.jadeval.models.JadevalModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

class StateMachineTest {

	private JadevalModel model;
	private StateMachineOptions<ToDoBoardFacts> stateMachineOptions;
	private StateMachineDelegate<ToDoBoardFacts> stateMachineDelegate;

	@BeforeEach
	void setUp() {
		File file = new File("src/test/resources/todo_statemachine.jsl");
		model = new JadevalLoader().load(file);
		stateMachineOptions = new StateMachineOptionsBuilder<ToDoBoardFacts>().build();
		stateMachineDelegate = new JadevalExecutor(model).stateMachine(stateMachineOptions);
	}
	/*
	states
		TO_DO, IN_PROGRESS, ON_HOLD, DONE, CANCELLED

	transitions
		TO_DO -> IN_PROGRESS when userAction is start
		TO_DO -> CANCELLED when userAction is cancel

		IN_PROGRESS -> ON_HOLD when userAction is impediment
		IN_PROGRESS -> CANCELLED when userAction is cancel
		IN_PROGRESS -> DONE when userAction is complete

		ON_HOLD -> IN_PROGRESS when userAction is removeImpediment
		ON_HOLD -> CANCELLED when userAction is cancel
	 */

	@Test
	void test() {
		ToDoBoardFacts toDoBoardFacts = new ToDoBoardFacts("start", ToDoState.TO_DO);
		stateMachineDelegate.apply(toDoBoardFacts);
		Assertions.assertEquals(ToDoState.IN_PROGRESS, toDoBoardFacts.state);

		toDoBoardFacts = new ToDoBoardFacts("cancel", ToDoState.IN_PROGRESS);
		stateMachineDelegate.apply(toDoBoardFacts);
		Assertions.assertEquals(ToDoState.CANCELLED, toDoBoardFacts.state);
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
