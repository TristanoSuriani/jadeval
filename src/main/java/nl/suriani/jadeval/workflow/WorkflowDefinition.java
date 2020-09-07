package nl.suriani.jadeval.workflow;

import nl.suriani.jadeval.common.condition.EqualitySymbolFactory;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class WorkflowDefinition<T> {
	private final static CharStreamRetriever charStreamRetriever = new CharStreamRetriever();
	private WorkflowCompiler workflowCompiler;
	private List<StateUpdateEventHandler<T>> eventHandlers;

	WorkflowDefinition(WorkflowCompiler workflowCompiler, List<StateUpdateEventHandler<T>> eventHandlers) {
		this.workflowCompiler = workflowCompiler;
		this.eventHandlers = eventHandlers;
	}

	public WorkflowDefinition(List<StateUpdateEventHandler<T>> eventHandlers) {
		this.workflowCompiler = new WorkflowCompiler(new WorkflowConditionFactory(new EqualitySymbolFactory()));
		this.eventHandlers = eventHandlers;
	}

	public Workflow build(File file) {
		compile(file);
		return new Workflow(workflowCompiler.getTransitions(), workflowCompiler.getAllStates(), eventHandlers);
	}

	public Workflow build(InputStream inputStream) {
		compile(inputStream);
		return new Workflow(workflowCompiler.getTransitions(), workflowCompiler.getAllStates(), eventHandlers);
	}

	private void compile(File file) {
		compile(charStreamRetriever.get(file));
	}

	private void compile(InputStream inputStream) {
		compile(charStreamRetriever.get(inputStream));
	}

	private void compile(CharStream input) {
		try {
			WorkflowLexer workflowLexer = new WorkflowLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(workflowLexer);
			WorkflowParser workflowParser = new WorkflowParser(tokens);
			ParseTree tree = workflowParser.workflowDefinition();
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(workflowCompiler, tree);
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	private static class CharStreamRetriever {
		public CharStream get(File file) {
			try {
				return CharStreams.fromStream(new FileInputStream(file));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		public CharStream get(InputStream inputStream) {
			try {
				return CharStreams.fromStream(inputStream);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		public CharStream get(String... lines) {
			return get(Arrays.asList(lines));
		}

		public CharStream get(List<String> lines) {
			String inputString = String.join("\n", lines);
			return CharStreams.fromString(inputString);
		}
	}
}
