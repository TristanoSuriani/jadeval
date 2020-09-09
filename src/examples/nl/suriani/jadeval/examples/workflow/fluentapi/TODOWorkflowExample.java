package nl.suriani.jadeval.examples.workflow.fluentapi;

import nl.suriani.jadeval.common.annotation.Fact;
import nl.suriani.jadeval.workflow.Workflow;
import nl.suriani.jadeval.workflow.annotation.State;

public class TODOWorkflowExample {
	public static void main(String[] args) {
		Workflow<TODOWorkflowContext> workflow = new TODOWorkflowExampleBuilder().build();

		TODOWorkflowContext context = new TODOWorkflowContext("todo", "start");
		workflow.updateState(context);
		System.out.println(context.getState());

		context = new TODOWorkflowContext("inProgress", "complete");
		workflow.updateState(context);
		System.out.println(context.getState());

		context = new TODOWorkflowContext("completed", "start");
		workflow.updateState(context);
		System.out.println(context.getState());

		/*
			It prints:

			inProgress
			completed
			completed
		 */
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
}
