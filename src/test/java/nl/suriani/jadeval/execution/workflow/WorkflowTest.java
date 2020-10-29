package nl.suriani.jadeval.execution.workflow;

import nl.suriani.jadeval.execution.JadevalExecutor;
import nl.suriani.jadeval.JadevalLoader;
import nl.suriani.jadeval.execution.shared.TransitionAttemptedEventHandler;
import nl.suriani.jadeval.models.JadevalModel;
import nl.suriani.jadeval.annotation.Fact;
import nl.suriani.jadeval.annotation.State;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;

class WorkflowTest {

	private JadevalModel model;
	private WorkflowOptions workflowOptions;
	private WorkflowDelegate<ToDoBoardFacts> workflowDelegate;

	@BeforeEach
	void setUp() {
		File file = new File("src/test/resources/todo_workflow.jwl");
		model = new JadevalLoader().load(file);
		workflowOptions = new WorkflowOptionsBuilder<ToDoBoardFacts>().build();
		workflowDelegate = new JadevalExecutor(model).workflow(workflowOptions);
	}
	/*
	root states
		TO_DO

	intermediate state
		IN_PROGRESS, ON_HOLD

	final states
		DONE, CANCELLED

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
		workflowDelegate.apply(toDoBoardFacts);
		Assertions.assertEquals(ToDoState.IN_PROGRESS, toDoBoardFacts.state);

		toDoBoardFacts = new ToDoBoardFacts("cancel", ToDoState.IN_PROGRESS);
		workflowDelegate.apply(toDoBoardFacts);
		Assertions.assertEquals(ToDoState.CANCELLED, toDoBoardFacts.state);
	}

	@Test
	void testUntilPauseWithConditionalTransactionsOnly() {
		TransitionAttemptedEventHandler<ToDoBoardFacts> transitionAttemptedEventHandler = Mockito.mock(TransitionAttemptedEventHandler.class);
		workflowDelegate = new JadevalExecutor(model).workflow(new WorkflowOptionsBuilder<ToDoBoardFacts>()
				.withExecutionType(WorkflowExecutionType.UNTIL_PAUSE)
				.withTransitionAttemptedEventHandler(transitionAttemptedEventHandler)
				.build());

		ToDoBoardFacts toDoBoardFacts = new ToDoBoardFacts("start", ToDoState.TO_DO);
		workflowDelegate.apply(toDoBoardFacts);
		Mockito.verify(transitionAttemptedEventHandler, Mockito.times(1)).handle(toDoBoardFacts);
	}

	@Test
	void testUntilPauseWithDirectTransaction() {
		TransitionAttemptedEventHandler<ToDoBoardFacts> transitionAttemptedEventHandler = Mockito.mock(TransitionAttemptedEventHandler.class);
		workflowDelegate = new JadevalExecutor(model).workflow(new WorkflowOptionsBuilder<ToDoBoardFacts>()
				.withExecutionType(WorkflowExecutionType.UNTIL_PAUSE)
				.withTransitionAttemptedEventHandler(transitionAttemptedEventHandler)
				.build());

		ToDoBoardFacts toDoBoardFacts = new ToDoBoardFacts("delete", ToDoState.TO_DO);
		workflowDelegate.apply(toDoBoardFacts);
		Mockito.verify(transitionAttemptedEventHandler, Mockito.times(2)).handle(toDoBoardFacts);
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
		TO_DO, IN_PROGRESS, ON_HOLD, READY_TO_DELETE, DONE, CANCELLED, DELETED
	}
}
