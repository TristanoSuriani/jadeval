package nl.suriani.jadeval.examples.workflow.jwl;

import nl.suriani.jadeval.common.annotation.Fact;
import nl.suriani.jadeval.workflow.annotation.State;

import java.io.File;

public class TODOWorkflowExample {
	/*public static void main(String[] args) {
		File file = new File("src/examples/nl/suriani/jadeval/examples/workflow/jwl/todo_workflow_example.jwl");
		Workflow<TODOWorkflowContext> workflow = WorkflowBuilder.<TODOWorkflowContext>fromFile(file).build();

		TODOWorkflowContext context = new TODOWorkflowContext("todo", "start");
		workflow.updateState(context);
		System.out.println(context.getState());

		context = new TODOWorkflowContext("inProgress", "complete");
		workflow.updateState(context);
		System.out.println(context.getState());

		context = new TODOWorkflowContext("completed", "start");
		workflow.updateState(context);
		System.out.println(context.getState());

		/_____*
			It prints:

			inProgress
			completed
			completed
		 *____/
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
	}*/
}
