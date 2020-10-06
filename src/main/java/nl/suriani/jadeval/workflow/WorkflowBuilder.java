package nl.suriani.jadeval.workflow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WorkflowBuilder {

	public static <T> FromInputStream<T> fromFile(File file) {
		try {
			return new FromInputStream(new FileInputStream(file));
		} catch (FileNotFoundException fileNotFoundException) {
			throw new IllegalArgumentException(fileNotFoundException);
		}
	}

	public static <T> FromInputStream<T> fromInputStream(InputStream inputStream) {
		return new FromInputStream<>(inputStream);
	}

	public static class FromInputStream<T> {
		private InputStream inputStream;

		private FromInputStream(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		public AddStateUpdateEventHandler<T> addStateUpdateEventHandler(StateUpdateEventHandler<T> eventHandler) {
			TransitionAttemptedEventHandler<T> defaultTransitionAttemptedEventHandler = (context) -> {
				// do nothing
			};
			return new AddStateUpdateEventHandler<T>(inputStream, defaultTransitionAttemptedEventHandler, eventHandler);
		}

		public Workflow<T> build() {
			TransitionAttemptedEventHandler<T> defaultTransitionAttemptedEventHandler = (context) -> {
				// do nothing
			};
			return new WorkflowDefinition<T>(defaultTransitionAttemptedEventHandler, new ArrayList<>()).build(inputStream);
		}
	}

	public static class AddAttemptedTransitionEventHandler<T> {
		private InputStream inputStream;
		private TransitionAttemptedEventHandler<T> eventHandler;

		public AddAttemptedTransitionEventHandler(InputStream inputStream, TransitionAttemptedEventHandler<T> eventHandler) {
			this.inputStream = inputStream;
			this.eventHandler = eventHandler;
		}

		public AddStateUpdateEventHandler<T> addStateUpdateEventHandler(StateUpdateEventHandler<T> stateUpdateEventHandler) {
			return new AddStateUpdateEventHandler<T>(inputStream, eventHandler, stateUpdateEventHandler);
		}

		public Workflow<T> build() {
			return new WorkflowDefinition<T>(eventHandler, new ArrayList<>()).build(inputStream);
		}
	}

	public static class AddStateUpdateEventHandler<T> {
		private InputStream inputStream;
		private TransitionAttemptedEventHandler<T> transitionAttemptedEventHandler;
		private List<StateUpdateEventHandler<T>> eventHandlers;

		private AddStateUpdateEventHandler(InputStream inputStream, TransitionAttemptedEventHandler<T> transitionAttemptedEventHandler, StateUpdateEventHandler<T> eventHandler) {
			this.inputStream = inputStream;
			this.transitionAttemptedEventHandler = transitionAttemptedEventHandler;
			this.eventHandlers = new ArrayList<>();
			this.eventHandlers.add(eventHandler);
		}

		private AddStateUpdateEventHandler(InputStream inputStream, TransitionAttemptedEventHandler<T> transitionAttemptedEventHandler, List<StateUpdateEventHandler<T>> eventHandlers) {
			this.inputStream = inputStream;
			this.transitionAttemptedEventHandler = transitionAttemptedEventHandler;
			this.eventHandlers = eventHandlers;
		}

		public AddStateUpdateEventHandler<T> addStateUpdateEventHandler(StateUpdateEventHandler<T> eventHandler) {
			eventHandlers.add(eventHandler);
			return new AddStateUpdateEventHandler<T>(inputStream, transitionAttemptedEventHandler, eventHandlers);
		}

		public Workflow<T> build() {
			return new WorkflowDefinition<T>(transitionAttemptedEventHandler, eventHandlers).build(inputStream);
		}
	}
}
