package nl.suriani.jadeval.workflow;

import nl.suriani.jadeval.common.ConditionFactory;
import nl.suriani.jadeval.common.condition.EqualitySymbolFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WorkflowBuilder {

	public static <T> NewFromInputStream<T> newFromFile(File file) {
		try {
			return new NewFromInputStream(new FileInputStream(file));
		} catch (FileNotFoundException fileNotFoundException) {
			throw new IllegalArgumentException(fileNotFoundException);
		}
	}

	public static <T> NewFromInputStream<T> newFromInputStream(InputStream inputStream) {
		return new NewFromInputStream<>(inputStream);
	}

	public static class NewFromInputStream<T> {
		private InputStream inputStream;

		private NewFromInputStream(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		public AddStateUpdateEventHandler<T> addStateUpdateEventHandler(StateUpdateEventHandler<T> eventHandler) {
				return new AddStateUpdateEventHandler<T>(inputStream, eventHandler);
		}

		public Workflow<T> build() {
			WorkflowCompiler workflowCompiler = new WorkflowCompiler(new ConditionFactory(new EqualitySymbolFactory()));
			return new WorkflowDefinition<T>(new ArrayList<>()).build(inputStream);
		}
	}

	public static class AddStateUpdateEventHandler<T> {
		private InputStream inputStream;
		List<StateUpdateEventHandler<T>> eventHandlers;

		private AddStateUpdateEventHandler(InputStream inputStream, StateUpdateEventHandler<T> eventHandler) {
			this.inputStream = inputStream;
			this.eventHandlers = new ArrayList<>();
			this.eventHandlers.add(eventHandler);
		}

		private AddStateUpdateEventHandler(InputStream inputStream, List<StateUpdateEventHandler<T>> eventHandlers) {
			this.inputStream = inputStream;
			this.eventHandlers = eventHandlers;
		}

		public AddStateUpdateEventHandler<T> addStateUpdateEventHandler(StateUpdateEventHandler<T> eventHandler) {
			eventHandlers.add(eventHandler);
			return new AddStateUpdateEventHandler<T>(inputStream, eventHandlers);
		}

		public Workflow<T> build() {
			return new WorkflowDefinition<T>(eventHandlers).build(inputStream);
		}
	}
}
