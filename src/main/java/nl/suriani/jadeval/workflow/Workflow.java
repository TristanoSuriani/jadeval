package nl.suriani.jadeval.workflow;

import nl.suriani.jadeval.common.Facts;
import nl.suriani.jadeval.common.internal.condition.ConditionResolver;
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
import java.util.logging.Logger;

public class Workflow {
	private final static CharStreamRetriever charStreamRetriever = new CharStreamRetriever();

	/**
	 * Runs the decisions.
	 * @param facts, not null
	 * @param file, not null
	 * @return results table
	 */
	public void apply(Facts facts, File file) {
		apply(facts, charStreamRetriever.get(file));
	}

	/**
	 * Runs the decisions.
	 * @param facts, not null
	 * @param inputStream, not null
	 * @return results table
	 */
	public void apply(Facts facts, InputStream inputStream) {
		apply(facts, charStreamRetriever.get(inputStream));
	}

	/**
	 * Runs the decisions.
	 * @param facts, not null
	 * @param decisionStatements, not null
	 * @return results table
	 */
	public void apply(Facts facts, List<String> decisionStatements) {
		apply(facts, charStreamRetriever.get(decisionStatements));
	}

	/**
	 * Runs the decisions.
	 * @param facts, not null
	 * @param decisionStatements, not null
	 * @return results table
	 */
	public void apply(Facts facts, String... decisionStatements) {
		apply(facts, charStreamRetriever.get(decisionStatements));
	}

	private void apply(Facts facts, CharStream input) {
		try {
			WorkflowLexer workflowLexer = new WorkflowLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(workflowLexer);
			WorkflowParser workflowParser = new WorkflowParser(tokens);
			ParseTree tree = workflowParser.workflowDefinition();
			ConditionResolver conditionResolver = new ConditionResolver();
			WorkflowCompiler workflowCompiler = new WorkflowCompiler();
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
