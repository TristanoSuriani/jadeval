package nl.suriani.jadeval.examples.statemachine.jsl;

import nl.suriani.jadeval.JadevalLoader;
import nl.suriani.jadeval.annotation.Fact;
import nl.suriani.jadeval.annotation.State;
import nl.suriani.jadeval.execution.JadevalExecutor;
import nl.suriani.jadeval.execution.statemachine.StateMachineDelegate;
import nl.suriani.jadeval.execution.statemachine.StateMachineOptions;
import nl.suriani.jadeval.execution.statemachine.StateMachineOptionsBuilder;
import nl.suriani.jadeval.models.JadevalModel;

import java.io.File;

public class TODOStateMachineExample {
	public static void main(String[] args) {
		File file = new File("src/examples/nl/suriani/jadeval/examples/statemachine/jsl/todo_statemachine_example.jsl");

		JadevalModel model = new JadevalLoader().load(file);
		JadevalExecutor executor = new JadevalExecutor(model);
		TODOStateMachineContext context = new TODOStateMachineContext("todo", "start");
		StateMachineOptions<TODOStateMachineContext> options = new StateMachineOptionsBuilder<TODOStateMachineContext>().build();
		StateMachineDelegate<TODOStateMachineContext> stateMachineDelegate = executor.stateMachine(options);
		stateMachineDelegate.apply(context);

		System.out.println(context.getState());

		context = new TODOStateMachineContext("inProgress", "complete");
		stateMachineDelegate.apply(context);
		System.out.println(context.getState());

		context = new TODOStateMachineContext("completed", "start");
		stateMachineDelegate.apply(context);
		System.out.println(context.getState());

		/*
			It prints:

			inProgress
			completed
			completed
		 */
	}

	private static class TODOStateMachineContext {
		@State
		private String state;

		@Fact
		private String action;

		public TODOStateMachineContext(String state, String action) {
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
