package nl.suriani.jadeval.decision;

import nl.suriani.jadeval.common.ConditionFactory;
import nl.suriani.jadeval.common.JadevalLexer;
import nl.suriani.jadeval.common.JadevalParser;
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
import java.util.logging.Logger;

/**
 * Runner for sets of decisions
 */
public class DecisionsDefinition {
	private final static Logger logger = Logger.getLogger(DecisionsDefinition.class.getName());
	private final static CharStreamRetriever charStreamRetriever = new CharStreamRetriever();

	/**
	 * Runs the decisions.
	 * @param file, not null
	 * @return decisions
	 */
	public Decisions build(File file) {
		return build(charStreamRetriever.get(file));
	}

	/**
	 * Runs the decisions.
	 * @param inputStream, not null
	 * @return decisions
	 */
	public Decisions build(InputStream inputStream) {
		return build(charStreamRetriever.get(inputStream));
	}

	/**
	 * Runs the decisions.
	 * @param decisionStatements, not null
	 * @return decisions
	 */
	public Decisions build(List<String> decisionStatements) {
		return build(charStreamRetriever.get(decisionStatements));
	}

	/**
	 * Runs the decisions.
	 * @param decisionStatements, not null
	 * @return decisions
	 */
	public Decisions build(String... decisionStatements) {
		return build(charStreamRetriever.get(decisionStatements));
	}

	private Decisions build(CharStream input) {
		try {
			JadevalLexer javaLexer = new JadevalLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(javaLexer);
			JadevalParser JadevalParser = new JadevalParser(tokens);
			ParseTree tree = JadevalParser.decisionsDefinition();
			EqualitySymbolFactory equalitySymbolFactory = new EqualitySymbolFactory();
			ConditionFactory conditionFactory = new ConditionFactory(equalitySymbolFactory);
			DecisionsCompiler decisionsCompiler = new DecisionsCompiler(conditionFactory);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(decisionsCompiler, tree);
			return decisionsCompiler.compile();
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
