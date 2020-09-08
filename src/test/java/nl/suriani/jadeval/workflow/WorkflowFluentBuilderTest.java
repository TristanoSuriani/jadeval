package nl.suriani.jadeval.workflow;

import junit.framework.Assert;
import nl.suriani.jadeval.common.annotation.Fact;
import nl.suriani.jadeval.workflow.annotation.State;
import org.junit.jupiter.api.Test;

class WorkflowFluentBuilderTest {

	public static final String TODO_STATE = "todo";
	public static final String IN_PROGRESS_STATE = "inProgress";
	public static final String ON_HOLD_STATE = "onHold";
	public static final String COMPLETED_STATE = "completed";
	public static final String CANCELLED_STATE = "cancelled";

	public static final String ACTION_FACT = "action";

	public static final String START_ACTION = "start";
	public static final String COMPLETE_ACTION = "complete";
	public static final String CANCEL_ACTION = "cancel";
	public static final String IMPEDIMENT_ACTION = "impediment";
	public static final String REMOVE_IMPEDIMENT_ACTION = "removeImpediment";

	@Test
	public void test() {
		Workflow workflow = new TODOWorkflowTestBuilder().build();
		TODOWorkflowContext workflowContext = new TODOWorkflowContext(TODO_STATE, START_ACTION);

		workflow.updateState(workflowContext);
		Assert.assertEquals(IN_PROGRESS_STATE, workflowContext.state);

		workflowContext = new TODOWorkflowContext(IN_PROGRESS_STATE, COMPLETE_ACTION);
		workflow.updateState(workflowContext);
		Assert.assertEquals(COMPLETED_STATE, workflowContext.state);

		workflowContext = new TODOWorkflowContext(COMPLETED_STATE, START_ACTION);
		workflow.updateState(workflowContext);
		Assert.assertEquals(COMPLETED_STATE, workflowContext.state);
	}
	private static class TODOWorkflowContext {
		@State
		private String state;

		@Fact
		private String action;

		public TODOWorkflowContext(String state, String action) {
			this.state = state;
			this.action = action;
		}

		public String getState() {
			return state;
		}

		public String getAction() {
			return action;
		}
	}

	private static class TODOWorkflowTestBuilder extends WorkflowFluentBuilder {

		@Override
		public void compile() {
			defineWorkflow()
				.addRootStates(TODO_STATE)
				.addIntermediateStates(IN_PROGRESS_STATE, ON_HOLD_STATE)
				.addFinalStates(COMPLETED_STATE, CANCELLED_STATE)

				.transitions()

				.defineTransition(TODO_STATE, IN_PROGRESS_STATE)
					.when(ACTION_FACT)
					.is(START_ACTION)

				.defineTransition(TODO_STATE, COMPLETED_STATE)
					.when(ACTION_FACT)
					.is(COMPLETE_ACTION)

				.defineTransition(TODO_STATE, ON_HOLD_STATE)
					.when(ACTION_FACT)
					.is(IMPEDIMENT_ACTION)

				.defineTransition(TODO_STATE, CANCELLED_STATE)
					.when(ACTION_FACT)
					.is(CANCEL_ACTION)

				.defineTransition(IN_PROGRESS_STATE, COMPLETED_STATE)
					.when(ACTION_FACT)
					.is(COMPLETE_ACTION)

				.defineTransition(IN_PROGRESS_STATE, CANCELLED_STATE)
					.when(ACTION_FACT)
					.is(CANCEL_ACTION)

				.defineTransition(IN_PROGRESS_STATE, ON_HOLD_STATE)
					.when(ACTION_FACT)
					.is(IMPEDIMENT_ACTION)

				.defineTransition(ON_HOLD_STATE, COMPLETED_STATE)
					.when(ACTION_FACT)
					.is(COMPLETE_ACTION)

				.defineTransition(ON_HOLD_STATE, CANCELLED_STATE)
					.when(ACTION_FACT)
					.is(CANCEL_ACTION)

				.defineTransition(ON_HOLD_STATE, IN_PROGRESS_STATE)
					.when(ACTION_FACT)
					.is(REMOVE_IMPEDIMENT_ACTION)

				.end();
		}
	}

}
