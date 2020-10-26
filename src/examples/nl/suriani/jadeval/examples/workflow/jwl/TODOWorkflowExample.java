package nl.suriani.jadeval.examples.workflow.jwl;

import nl.suriani.jadeval.JadevalLoader;
import nl.suriani.jadeval.annotation.Fact;
import nl.suriani.jadeval.annotation.State;
import nl.suriani.jadeval.execution.JadevalExecutor;
import nl.suriani.jadeval.execution.workflow.WorkflowDelegate;
import nl.suriani.jadeval.execution.workflow.WorkflowOptions;
import nl.suriani.jadeval.execution.workflow.WorkflowOptionsBuilder;
import nl.suriani.jadeval.models.JadevalModel;

import java.io.File;

public class TODOWorkflowExample {
	public static void main(String[] args) {
		File file = new File("src/examples/nl/suriani/jadeval/examples/workflow/jwl/todo_workflow_example.jwl");

		JadevalModel model = new JadevalLoader().load(file);
		JadevalExecutor executor = new JadevalExecutor(model);
		TODOWorkflowContext context = new TODOWorkflowContext("todo", "start");
		WorkflowOptions<TODOWorkflowContext> options = new WorkflowOptionsBuilder<TODOWorkflowContext>().build();

		WorkflowDelegate<TODOWorkflowContext> stateMachineDelegate = executor.workflow(options);
		stateMachineDelegate.apply(context);

		System.out.println(context.getState());

		context = new TODOWorkflowContext("inProgress", "complete");
		stateMachineDelegate.apply(context);
		System.out.println(context.getState());

		context = new TODOWorkflowContext("completed", "start");
		stateMachineDelegate.apply(context);
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
