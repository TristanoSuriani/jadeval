package nl.suriani.jadeval.workflow;

import nl.suriani.jadeval.common.condition.Condition;
import nl.suriani.jadeval.workflow.internal.WorkflowConditionFactory;
import nl.suriani.jadeval.workflow.internal.transition.ConditionalTransition;
import nl.suriani.jadeval.workflow.internal.transition.DirectTransition;
import nl.suriani.jadeval.workflow.internal.transition.Transition;
import nl.suriani.jadeval.common.internal.value.BooleanValue;
import nl.suriani.jadeval.common.internal.value.NumericValue;
import nl.suriani.jadeval.common.internal.value.SymbolTable;
import nl.suriani.jadeval.common.internal.value.TextValue;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorkflowCompiler extends WorkflowBaseListener {
	private SymbolTable constantsScope;
	private Set<String> rootStates;
	private Set<String> intermediateStates;
	private Set<String> finalStates;
	private Set<String> allStates;
	private List<Transition> transitions;
	private String currentFromState;
	private String currentToState;
	private List<Condition> currentConditions;

	public WorkflowCompiler() {
		constantsScope = new SymbolTable();
		rootStates = new HashSet<>();
		intermediateStates = new HashSet<>();
		finalStates = new HashSet<>();
		allStates = new HashSet<>();
		transitions = new ArrayList<>();
	}

	@Override
	public void enterConstantDefinition(WorkflowParser.ConstantDefinitionContext ctx) {
		String constantName = ctx.children.get(0).getText();
		ParseTree valueContext = ctx.children.get(2);
		if (valueContext instanceof WorkflowParser.NumericValueContext) {
			constantsScope.update(constantName, new NumericValue((valueContext.getText())));
		} else if (valueContext instanceof WorkflowParser.BooleanValueContext) {
			constantsScope.update(constantName, new BooleanValue(valueContext.getText()));
		} else if (valueContext instanceof WorkflowParser.TextValueContext) {
			constantsScope.update(constantName, new TextValue(valueContext.getText()));
		}
	}

	@Override
	public void enterRootStatesDefinition(WorkflowParser.RootStatesDefinitionContext ctx) {
		ctx.children.subList(1, ctx.children.size()).stream()
				.map(ParseTree::getText)
				.forEach(rootStates::add);

		allStates.addAll(rootStates);
	}

	@Override
	public void enterIntermediateStatesDefinition(WorkflowParser.IntermediateStatesDefinitionContext ctx) {
		ctx.children.subList(1, ctx.children.size()).stream()
				.map(ParseTree::getText)
				.forEach(intermediateStates::add);

		allStates.addAll(intermediateStates);
	}

	@Override
	public void enterFinalStatesDefinition(WorkflowParser.FinalStatesDefinitionContext ctx) {
		ctx.children.subList(1, ctx.children.size()).stream()
				.map(ParseTree::getText)
				.forEach(finalStates::add);


		allStates.addAll(finalStates);
		if (duplicatedStatesCheckFails()) {
			throw new IllegalStateException("A state can only be either root or intermediate or final");
		}
	}

	private boolean duplicatedStatesCheckFails() {
		return allStates.size() < (rootStates.size() + intermediateStates.size() + finalStates.size());
	}

	@Override
	public void enterDirectTransition(WorkflowParser.DirectTransitionContext ctx) {
		String fromState = ctx.children.get(0).getText();
		String toState = ctx.children.get(2).getText();

		Transition transition = new DirectTransition(fromState, toState);
		transitions.add(transition);
	}

	@Override
	public void enterAnyTypeTransition(WorkflowParser.AnyTypeTransitionContext ctx) {
		super.enterAnyTypeTransition(ctx);
	}

	@Override
	public void enterConditionalTransition(WorkflowParser.ConditionalTransitionContext ctx) {
		currentFromState = ctx.children.get(0).getText();
		currentToState = ctx.children.get(2).getText();
	}

	@Override
	public void enterConditionExpression(WorkflowParser.ConditionExpressionContext ctx) {

		currentConditions = new ArrayList<>();
		super.enterConditionExpression(ctx);
	}

	@Override
	public void enterNumericEqualityCondition(WorkflowParser.NumericEqualityConditionContext ctx) {
		super.enterNumericEqualityCondition(ctx);
	}

	@Override
	public void enterBooleanEqualityCondition(WorkflowParser.BooleanEqualityConditionContext ctx) {
		super.enterBooleanEqualityCondition(ctx);
	}

	@Override
	public void enterTextEqualityCondition(WorkflowParser.TextEqualityConditionContext ctx) {
		super.enterTextEqualityCondition(ctx);
	}

	@Override
	public void exitConditionExpression(WorkflowParser.ConditionExpressionContext ctx) {
		transitions.add(new ConditionalTransition(currentFromState, currentToState, currentConditions));
	}

	private boolean resolveCondition(ParseTree equalityConditionContext) {
		String factName = equalityConditionContext.getChild(0).getText();
		String equalitySymbol = equalityConditionContext.getChild(1).getText();
		ParseTree valueContext = equalityConditionContext.getChild(2);
		if (equalityConditionContext instanceof WorkflowParser.NumericEqualityConditionContext) {
			new WorkflowConditionFactory().make((WorkflowParser.NumericEqualityConditionContext) equalityConditionContext);
		}
		return false;
	}

	private boolean isRootState(String state) {
		return rootStates.contains(state);
	}

	private boolean isIntermediateState(String state) {
		return intermediateStates.contains(state);
	}

	private boolean isFinalState(String state) {
		return finalStates.contains(state);
	}

	private boolean isAState(String state) {
		return allStates.contains(state);
	}
}
