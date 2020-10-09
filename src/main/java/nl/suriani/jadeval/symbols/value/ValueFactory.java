package nl.suriani.jadeval.symbols.value;

import nl.suriani.jadeval.common.JadevalParser;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.stream.Collectors;

public class ValueFactory {
	public NumericValue make(JadevalParser.NumericValueContext ctx) {
		return new NumericValue(ctx.getText());
	}

	public BooleanValue make(JadevalParser.BooleanValueContext ctx) {
		return new BooleanValue(ctx.getText());
	}

	public TextValue make(JadevalParser.TextValueContext ctx) {
		return new TextValue(ctx.getText().replaceAll("\"", ""));
	}

	public ListValue make(JadevalParser.ListValueContext ctx) {
		return new ListValue(ctx.children.stream()
				.filter(c -> !c.getText().equals("["))
				.filter(c -> !c.getText().equals("]"))
				.map(c -> make(c.getChild(0)))
				.collect(Collectors.toList()));
	}

	public EmptyValue make() {
		return new EmptyValue();
	}

	public FactValue make(ParseTree ctx) {
		if (ctx instanceof JadevalParser.NumericValueContext) {
			return make((JadevalParser.NumericValueContext) ctx);
		} else if (ctx instanceof JadevalParser.BooleanValueContext) {
			return make((JadevalParser.BooleanValueContext) ctx);
		} else if (ctx instanceof JadevalParser.TextValueContext) {
			return make((JadevalParser.TextValueContext) ctx);
		} else if (ctx instanceof JadevalParser.ListValueContext) {
			return make((JadevalParser.ListValueContext) ctx);
		}
		return make();
	}
}
