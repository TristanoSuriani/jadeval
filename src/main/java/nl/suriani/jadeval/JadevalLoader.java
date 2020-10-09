package nl.suriani.jadeval;

import nl.suriani.jadeval.parser.JadevalInterpreter;
import nl.suriani.jadeval.parser.ConditionFactory;
import nl.suriani.jadeval.common.JadevalLexer;
import nl.suriani.jadeval.common.JadevalParser;
import nl.suriani.jadeval.symbols.EqualitySymbolFactory;
import nl.suriani.jadeval.symbols.value.ValueFactory;
import nl.suriani.jadeval.models.JadevalModel;
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

public class JadevalLoader {
	private final static Logger logger = Logger.getLogger(JadevalLoader.class.getName());
	private final static CharStreamRetriever charStreamRetriever = new CharStreamRetriever();

	public JadevalModel load(File file) {
		return load(charStreamRetriever.get(file));
	}

	public JadevalModel load(InputStream inputStream) {
		return load(charStreamRetriever.get(inputStream));
	}

	public JadevalModel load(List<String> decisionStatements) {
		return load(charStreamRetriever.get(decisionStatements));
	}

	public JadevalModel load(String... decisionStatements) {
		return load(charStreamRetriever.get(decisionStatements));
	}

	private JadevalModel load(CharStream input) {
		try {
			JadevalLexer javaLexer = new JadevalLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(javaLexer);
			JadevalParser JadevalParser = new JadevalParser(tokens);
			ParseTree tree = JadevalParser.jadevalProgram();
			EqualitySymbolFactory equalitySymbolFactory = new EqualitySymbolFactory();
			ValueFactory valueFactory = new ValueFactory();
			ConditionFactory conditionFactory = new ConditionFactory(equalitySymbolFactory, valueFactory);
			JadevalInterpreter jadevalInterpreter = new JadevalInterpreter(conditionFactory, valueFactory);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(jadevalInterpreter, tree);
			return jadevalInterpreter.getModel();
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
