package nl.suriani.jadeval.workflow;

import nl.suriani.jadeval.common.ConditionFactory;
import nl.suriani.jadeval.common.JadevalLexer;
import nl.suriani.jadeval.common.JadevalParser;
import nl.suriani.jadeval.common.condition.EqualitySymbolFactory;
import nl.suriani.jadeval.common.internal.value.ValueFactory;
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
	private TransitionAttemptedEventHandler<T> transitionAttemptedEventHandler;
	private List<StateUpdateEventHandler<T>> stateUpdateEventHandlers;

	WorkflowDefinition(WorkflowCompiler workflowCompiler, TransitionAttemptedEventHandler<T> transitionAttemptedEventHandler, List<StateUpdateEventHandler<T>> stateUpdateEventHandlers) {
		this.workflowCompiler = workflowCompiler;
		this.transitionAttemptedEventHandler = transitionAttemptedEventHandler;
		this.stateUpdateEventHandlers = stateUpdateEventHandlers;
	}

	public WorkflowDefinition(TransitionAttemptedEventHandler<T> transitionAttemptedEventHandler, List<StateUpdateEventHandler<T>> stateUpdateEventHandlers) {
		ValueFactory valueFactory = new ValueFactory();
		this.workflowCompiler = new WorkflowCompiler(new ConditionFactory(new EqualitySymbolFactory(), valueFactory), valueFactory);
		this.transitionAttemptedEventHandler = transitionAttemptedEventHandler;
		this.stateUpdateEventHandlers = stateUpdateEventHandlers;
	}

	public Workflow build(File file) {
		compile(file);
		return new Workflow(workflowCompiler.getTransitions(), workflowCompiler.getAllStates(), transitionAttemptedEventHandler, stateUpdateEventHandlers);
	}

	public Workflow build(InputStream inputStream) {
		compile(inputStream);
		return new Workflow(workflowCompiler.getTransitions(), workflowCompiler.getAllStates(), transitionAttemptedEventHandler, stateUpdateEventHandlers);
	}

	private void compile(File file) {
		compile(charStreamRetriever.get(file));
	}

	private void compile(InputStream inputStream) {
		compile(charStreamRetriever.get(inputStream));
	}

	private void compile(CharStream input) {
		try {
			JadevalLexer workflowLexer = new JadevalLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(workflowLexer);
			JadevalParser JadevalParser = new JadevalParser(tokens);
			ParseTree tree = JadevalParser.workflowDefinition();
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
