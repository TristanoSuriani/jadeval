package nl.suriani.jadeval.workflow;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.HashSet;
import java.util.Set;

public class WorkflowCompiler extends WorkflowBaseListener {
	private Set<String> rootStates;
	private Set<String> intermediateStates;
	private Set<String> finalStates;
	private Set<String> allStates;
	private Set<String> userEvents;
	private Set<String> systemEvents;

	public WorkflowCompiler() {
		rootStates = new HashSet<>();
		intermediateStates = new HashSet<>();
		finalStates = new HashSet<>();
		allStates = new HashSet<>();
		userEvents = new HashSet<>();
		systemEvents = new HashSet<>();
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
	}

	@Override
	public void enterUserEventsDefinition(WorkflowParser.UserEventsDefinitionContext ctx) {
		ctx.children.subList(1, ctx.children.size()).stream()
				.map(ParseTree::getText)
				.forEach(userEvents::add); }

	@Override
	public void enterSystemEventsDefinition(WorkflowParser.SystemEventsDefinitionContext ctx) {
		ctx.children.subList(1, ctx.children.size()).stream()
				.map(ParseTree::getText)
				.forEach(systemEvents::add);
	}

	@Override
	public void enterTransitionDefinition(WorkflowParser.TransitionDefinitionContext ctx) {
		/*
		*   TODO figure out how to distinguish between transition of types:
		*   1) state1 -> state 2 when certain conditions [otherwise -> state3], no user/system event
		* 	2) like 1 but user event
		* 	3) like 2 but system event
		*/
		System.out.println();
	}
}
