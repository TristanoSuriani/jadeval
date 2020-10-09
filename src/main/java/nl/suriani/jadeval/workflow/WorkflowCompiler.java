package nl.suriani.jadeval.workflow;

import nl.suriani.jadeval.common.ConditionFactory;
import nl.suriani.jadeval.common.JadevalBaseListener;
import nl.suriani.jadeval.common.JadevalParser;
import nl.suriani.jadeval.common.condition.BooleanEqualityCondition;
import nl.suriani.jadeval.common.condition.Condition;
import nl.suriani.jadeval.common.condition.ListEqualityCondition;
import nl.suriani.jadeval.common.condition.NumericEqualityCondition;
import nl.suriani.jadeval.common.condition.TextEqualityCondition;
import nl.suriani.jadeval.common.internal.value.SymbolTable;
import nl.suriani.jadeval.common.internal.value.ValueFactory;
import nl.suriani.jadeval.workflow.internal.transition.ConditionalTransition;
import nl.suriani.jadeval.workflow.internal.transition.DirectTransition;
import nl.suriani.jadeval.workflow.internal.transition.Transition;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorkflowCompiler extends JadevalBaseListener {
	private SymbolTable constantsScope;
	private Set<String> rootStates;
	private Set<String> intermediateStates;
	private Set<String> finalStates;
	private List<Transition> transitions;
	private String currentFromState;
	private List<String> currentFromStates;
	private String currentToState;
	private List<Condition> currentConditions;

	private ConditionFactory conditionFactory;
	private ValueFactory valueFactory;

	public WorkflowCompiler(ConditionFactory conditionFactory, ValueFactory valueFactory) {
		constantsScope = new SymbolTable();
		rootStates = new HashSet<>();
		intermediateStates = new HashSet<>();
		finalStates = new HashSet<>();
		transitions = new ArrayList<>();
		currentFromStates = new ArrayList<>();
		this.conditionFactory = conditionFactory;
		this.valueFactory = valueFactory;
	}

	@Override
	public void enterConstantDefinition(JadevalParser.ConstantDefinitionContext ctx) {
		String constantName = ctx.getChild(0).getText();
		ParseTree valueContext = ctx.getChild(2);
		constantsScope.update(constantName, valueFactory.make(valueContext));
	}

	@Override
	public void enterRootStatesDefinition(JadevalParser.RootStatesDefinitionContext ctx) {
		ctx.children.subList(1, ctx.children.size()).stream()
				.map(ParseTree::getText)
				.forEach(rootStates::add);
	}

	@Override
	public void enterIntermediateStatesDefinition(JadevalParser.IntermediateStatesDefinitionContext ctx) {
		ctx.children.subList(1, ctx.children.size()).stream()
				.map(ParseTree::getText)
				.forEach(intermediateStates::add);
	}

	@Override
	public void enterFinalStatesDefinition(JadevalParser.FinalStatesDefinitionContext ctx) {
		ctx.children.subList(1, ctx.children.size()).stream()
				.map(ParseTree::getText)
				.forEach(finalStates::add);
	}

	@Override
	public void enterMultipleConditionalTransition(JadevalParser.MultipleConditionalTransitionContext ctx) {

		List<ParseTree> children = ctx.children;
		for(ParseTree child: children) {
			if (!(child.getText().equals(ctx.OPEN_BRACKET().getText()) ||
					child.getText().equals(ctx.CLOSE_BRACKET().getText()))) {
				if (child.getText().equals(ctx.ARROW().getText())) {
					break;
				}
				else {
					currentFromStates.add(child.getText());
				}
			}
		}
		currentToState = children.get(children.size() - 3).getText();
	}

	@Override
	public void enterMultipleDirectTransition(JadevalParser.MultipleDirectTransitionContext ctx) {
		List<ParseTree> children = ctx.children;
		List<String> localFromStates = new ArrayList<>();
		for(ParseTree child: children) {
			if (!(child.getText().equals(ctx.OPEN_BRACKET().getText()) ||
					child.getText().equals(ctx.CLOSE_BRACKET().getText()))) {
				if (child.getText().equals(ctx.ARROW().getText())) {
					break;
				}
				else {
					localFromStates.add(child.getText());
				}
			}
		}
		String toState = children.get(children.size() - 1).getText();
		localFromStates.forEach(fromState -> transitions.add(new DirectTransition(fromState, toState)));
	}

	@Override
	public void enterDirectTransition(JadevalParser.DirectTransitionContext ctx) {
		String fromState = ctx.getChild(0).getText();
		String toState = ctx.getChild(2).getText();

		Transition transition = new DirectTransition(fromState, toState);
		transitions.add(transition);
	}

	@Override
	public void enterConditionalTransition(JadevalParser.ConditionalTransitionContext ctx) {
		currentFromState = ctx.getChild(0).getText();
		currentToState = ctx.getChild(2).getText();
	}

	@Override
	public void enterConditionExpression(JadevalParser.ConditionExpressionContext ctx) {
		if (ctx.getParent() instanceof JadevalParser.ConditionalTransitionContext ||
				ctx.getParent() instanceof JadevalParser.MultipleConditionalTransitionContext) {
			currentConditions = new ArrayList<>();
		}
	}

	@Override
	public void enterNumericEqualityCondition(JadevalParser.NumericEqualityConditionContext ctx) {
		NumericEqualityCondition condition = conditionFactory.make(ctx);
		currentConditions.add(condition);
	}

	@Override
	public void enterBooleanEqualityCondition(JadevalParser.BooleanEqualityConditionContext ctx) {
		BooleanEqualityCondition condition = conditionFactory.make(ctx);
		currentConditions.add(condition);
	}

	@Override
	public void enterTextEqualityCondition(JadevalParser.TextEqualityConditionContext ctx) {
		TextEqualityCondition condition = conditionFactory.make(ctx);
		currentConditions.add(condition);
	}

	@Override
	public void enterListEqualityCondition(JadevalParser.ListEqualityConditionContext ctx) {
		ListEqualityCondition condition = conditionFactory.make(ctx);
		currentConditions.add(condition);
	}

	@Override
	public void enterConstantEqualityCondition(JadevalParser.ConstantEqualityConditionContext ctx) {
		Condition condition = conditionFactory.make(constantsScope, ctx);
		currentConditions.add(condition);
	}

	@Override
	public void exitMultipleConditionalTransition(JadevalParser.MultipleConditionalTransitionContext ctx) {
		currentFromStates.forEach(fromState -> transitions.add(new ConditionalTransition(fromState, currentToState, currentConditions)));

		currentFromStates = new ArrayList<>();
		currentToState = null;
		currentConditions = new ArrayList<>();
	}

	@Override
	public void exitConditionalTransition(JadevalParser.ConditionalTransitionContext ctx) {
		transitions.add(new ConditionalTransition(currentFromState, currentToState, currentConditions));

		currentFromState = null;
		currentToState = null;
		currentConditions = new ArrayList<>();
	}

	public List<Transition> getTransitions() {
		return null;
	}

	public Set<String> getAllStates() {
		return null;
	}
}
