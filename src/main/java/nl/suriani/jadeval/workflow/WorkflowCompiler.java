package nl.suriani.jadeval.workflow;

import nl.suriani.jadeval.common.condition.BooleanEqualityCondition;
import nl.suriani.jadeval.common.condition.Condition;
import nl.suriani.jadeval.common.condition.NumericEqualityCondition;
import nl.suriani.jadeval.common.condition.TextEqualityCondition;
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
	private String currentAlternativeToState;
	private List<Condition> currentConditions;

	private WorkflowConditionFactory workflowConditionFactory;

	public WorkflowCompiler(WorkflowConditionFactory workflowConditionFactory) {
		constantsScope = new SymbolTable();
		rootStates = new HashSet<>();
		intermediateStates = new HashSet<>();
		finalStates = new HashSet<>();
		allStates = new HashSet<>();
		transitions = new ArrayList<>();

		this.workflowConditionFactory = workflowConditionFactory;
	}

	@Override
	public void enterConstantDefinition(WorkflowParser.ConstantDefinitionContext ctx) {
		String constantName = ctx.getChild(0).getText();
		ParseTree valueContext = ctx.getChild(2);
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
		String fromState = ctx.getChild(0).getText();
		String toState = ctx.getChild(2).getText();

		Transition transition = new DirectTransition(fromState, toState);
		transitions.add(transition);
	}

	@Override
	public void enterConditionalTransition(WorkflowParser.ConditionalTransitionContext ctx) {
		currentFromState = ctx.getChild(0).getText();
		currentToState = ctx.getChild(2).getText();

		if (ctx.OTHERWISE() != null) {
			currentAlternativeToState = ctx.getChild(7).getText();
		}
	}

	@Override
	public void enterConditionExpression(WorkflowParser.ConditionExpressionContext ctx) {
		if (ctx.getParent() instanceof WorkflowParser.ConditionalTransitionContext) {
			currentConditions = new ArrayList<>();
		}
	}

	@Override
	public void enterNumericEqualityCondition(WorkflowParser.NumericEqualityConditionContext ctx) {
		NumericEqualityCondition condition = workflowConditionFactory.make(ctx);
		currentConditions.add(condition);
	}

	@Override
	public void enterBooleanEqualityCondition(WorkflowParser.BooleanEqualityConditionContext ctx) {
		BooleanEqualityCondition condition = workflowConditionFactory.make(ctx);
		currentConditions.add(condition);
	}

	@Override
	public void enterTextEqualityCondition(WorkflowParser.TextEqualityConditionContext ctx) {
		TextEqualityCondition condition = workflowConditionFactory.make(ctx);
		currentConditions.add(condition);
	}

	@Override
	public void enterConstantEqualityCondition(WorkflowParser.ConstantEqualityConditionContext ctx) {
		Condition condition = workflowConditionFactory.make(constantsScope, ctx);
		currentConditions.add(condition);
	}

	@Override
	public void exitConditionalTransition(WorkflowParser.ConditionalTransitionContext ctx) {
		transitions.add(new ConditionalTransition(currentFromState, currentToState, currentConditions, currentAlternativeToState));

		currentFromState = null;
		currentToState = null;
		currentAlternativeToState = null;
		currentConditions = null;
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

	public List<Transition> getTransitions() {
		return transitions;
	}

	public Set<String> getAllStates() {
		return allStates;
	}
}
