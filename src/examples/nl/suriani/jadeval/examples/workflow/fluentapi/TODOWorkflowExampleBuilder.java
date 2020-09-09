package nl.suriani.jadeval.examples.workflow.fluentapi;

import nl.suriani.jadeval.workflow.WorkflowFluentBuilder;

class TODOWorkflowExampleBuilder extends WorkflowFluentBuilder {
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
