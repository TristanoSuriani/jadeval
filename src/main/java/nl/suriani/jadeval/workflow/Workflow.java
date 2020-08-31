package nl.suriani.jadeval.workflow;

import nl.suriani.jadeval.common.condition.EqualitySymbolFactory;
import nl.suriani.jadeval.workflow.internal.WorkflowConditionFactory;
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

public class Workflow {
	private final static CharStreamRetriever charStreamRetriever = new CharStreamRetriever();

	public void build(File file) {
		build(charStreamRetriever.get(file));
	}

	public void build(InputStream inputStream) {
		build(charStreamRetriever.get(inputStream));
	}

	private void build(CharStream input) {
		try {
			WorkflowLexer workflowLexer = new WorkflowLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(workflowLexer);
			WorkflowParser workflowParser = new WorkflowParser(tokens);
			ParseTree tree = workflowParser.workflowDefinition();
			EqualitySymbolFactory equalitySymbolFactory = new EqualitySymbolFactory();
			WorkflowConditionFactory workflowConditionFactory = new WorkflowConditionFactory(equalitySymbolFactory);
			WorkflowCompiler workflowCompiler = new WorkflowCompiler(workflowConditionFactory);
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
