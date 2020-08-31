package nl.suriani.jadeval.workflow;

import nl.suriani.jadeval.common.Facts;
import nl.suriani.jadeval.decision.condition.ConditionFactory;
import nl.suriani.jadeval.decision.condition.ConditionResolver;
import nl.suriani.jadeval.decision.condition.Condition;
import nl.suriani.jadeval.workflow.internal.transition.BaseTransition;
import nl.suriani.jadeval.workflow.internal.transition.ConditionalTransition;
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

	private Facts facts;
	private ConditionFactory conditionFactory;
	private ConditionResolver conditionResolver;

	public WorkflowCompiler(Facts facts, ConditionFactory conditionFactory, ConditionResolver conditionResolver) {
		constantsScope = new SymbolTable();
		rootStates = new HashSet<>();
		intermediateStates = new HashSet<>();
		finalStates = new HashSet<>();
		allStates = new HashSet<>();
		transitions = new ArrayList<>();

		this.facts = facts;
		this.conditionFactory = conditionFactory;
		this.conditionResolver = conditionResolver;
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
	public void enterBaseTransition(WorkflowParser.BaseTransitionContext ctx) {
		String fromState = ctx.children.get(0).getText();
		String toState = ctx.children.get(2).getText();
		WorkflowParser.TransitionDefinitionContext parent = (WorkflowParser.TransitionDefinitionContext)
				ctx.parent;

		if (parent.conditionExpression() == null) {
			Transition transition = new BaseTransition(fromState, toState);
			transitions.add(transition);
		} else {
			WorkflowParser.ConditionExpressionContext conditionExpressionContext = parent.conditionExpression();
			conditionExpressionContext.children.stream()
					.filter(child -> child instanceof WorkflowParser.ConditionExpressionContext)
					.map(child -> (WorkflowParser.ConditionExpressionContext) child)
					.map(child -> child.getChild(0))
					.allMatch(this::resolveCondition);
			Condition condition = conditionFactory.make(new BooleanValue(true), new BooleanValue(true), "is");
			Transition transition = new ConditionalTransition(fromState, toState, condition);
			transitions.add(transition);
		}
		System.out.println();
	}

	@Override
	public void enterAnyTypeTransition(WorkflowParser.AnyTypeTransitionContext ctx) {
		super.enterAnyTypeTransition(ctx);
	}

	private boolean resolveCondition(ParseTree equalityConditionContext) {
		String factName = equalityConditionContext.getChild(0).getText();
		String equalitySymbol = equalityConditionContext.getChild(1).getText();
		ParseTree valueContext = equalityConditionContext.getChild(2);
		if (valueContext instanceof WorkflowParser.NumericValueContext) {
			valueContext = (WorkflowParser.NumericValueContext) valueContext;
			conditionResolver.resolveNumericEquality(factName, equalitySymbol, new NumericValue(valueContext.getChild(0).getText()));
		} else if (valueContext instanceof WorkflowParser.BooleanValueContext) {
			valueContext = (WorkflowParser.BooleanValueContext) valueContext;
			conditionResolver.resolveBooleanEquality(factName, equalitySymbol, new BooleanValue(valueContext.getChild(0).getText()));
		} else if (valueContext instanceof WorkflowParser.TextValueContext) {
			valueContext = (WorkflowParser.TextValueContext) valueContext;
			conditionResolver.resolveTextEquality(factName, equalitySymbol, new TextValue(valueContext.getChild(0).getText()));
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
