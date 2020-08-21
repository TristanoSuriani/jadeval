package nl.suriani.jadeval.decision;

import nl.suriani.jadeval.decision.internal.DecisionsListenerImpl;
import nl.suriani.jadeval.decision.internal.condition.ConditionResolver;
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

/**
 * Runner for sets of decisions
 */
public class DecisionsRunner {
	private final static Logger logger = Logger.getLogger(DecisionsRunner.class.getName());
	private final static CharStreamRetriever charStreamRetriever = new CharStreamRetriever();

	/**
	 * Runs the decisions.
	 * @param facts, not null
	 * @param file, not null
	 * @return results table
	 */
	public DecisionsResultsTable run(Facts facts, File file) {
		return run(facts, charStreamRetriever.get(file));
	}

	/**
	 * Runs the decisions.
	 * @param facts, not null
	 * @param inputStream, not null
	 * @return results table
	 */
	public DecisionsResultsTable run(Facts facts, InputStream inputStream) {
		return run(facts, charStreamRetriever.get(inputStream));
	}

	/**
	 * Runs the decisions.
	 * @param facts, not null
	 * @param decisionStatements, not null
	 * @return results table
	 */
	public DecisionsResultsTable run(Facts facts, List<String> decisionStatements) {
		return run(facts, charStreamRetriever.get(decisionStatements));
	}

	/**
	 * Runs the decisions.
	 * @param facts, not null
	 * @param decisionStatements, not null
	 * @return results table
	 */
	public DecisionsResultsTable run(Facts facts, String... decisionStatements) {
		return run(facts, charStreamRetriever.get(decisionStatements));
	}

	private DecisionsResultsTable run(Facts facts, CharStream input) {
		try {
			DecisionsLexer javaLexer = new DecisionsLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(javaLexer);
			DecisionsParser decisionsParser = new DecisionsParser(tokens);
			ParseTree tree = decisionsParser.decisionTable();
			ConditionResolver conditionResolver = new ConditionResolver();
			DecisionsListenerImpl decisionsListener = new DecisionsListenerImpl(facts, conditionResolver);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(decisionsListener, tree);
			return decisionsListener.getDecisionsResultsTable();
		} catch (Exception exception) {
			return new DecisionsResultsTable(exception);
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
