package nl.suriani.jadeval.workflow;

import nl.suriani.jadeval.common.Facts;
import nl.suriani.jadeval.common.condition.Condition;
import nl.suriani.jadeval.workflow.annotation.State;
import nl.suriani.jadeval.workflow.internal.transition.ConditionalTransition;
import nl.suriani.jadeval.workflow.internal.transition.DirectTransition;
import nl.suriani.jadeval.workflow.internal.transition.Transition;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WorkflowExecutor<CONTEXT> {
	private List<Transition> transitions;
	private Set<String> availableStates;
	private List<StateUpdateEventHandler<CONTEXT>> eventHandlers;

	WorkflowExecutor(List<Transition> transitions, Set<String> availableStates, List<StateUpdateEventHandler<CONTEXT>> eventHandlers) {
		this.transitions = transitions;
		this.availableStates = availableStates;
		this.eventHandlers = eventHandlers;
	}

	public void updateState(CONTEXT context) {
		try {
			String stateName;
			Field stateField = Arrays.asList(context.getClass().getDeclaredFields())
					.stream().filter(field -> field.isAnnotationPresent(State.class))
					.peek(field -> field.setAccessible(true))
					.findFirst()
					.orElse(null);

			if (stateField == null) {
				throw new IllegalArgumentException("No @State annotation present in given object, cannot determine current state");
			}

			if (stateField.get(context) instanceof String) {
				stateName = (String) stateField.get(context);
			} else if (stateField.get(context) instanceof Enum) {
				stateName = ((Enum) stateField.get(context)).name();
			} else {
				throw new IllegalArgumentException("The property annotated with @State must be a non-null String or an enum");
			}

			if (!availableStates.contains(stateName)) {
				throw new IllegalArgumentException("Invalid state " + stateName + ". It must be one of the following states:\n" + availableStates);
			}

			String nextState = getNextState(stateName, new Facts(context));
			if (!stateName.equals(nextState)) {
				synchroniseState(stateField, context, nextState);

				eventHandlers.stream().
						filter(eventHandler -> eventHandler.getStateName().equals(stateName))
						.forEach(eventHandler -> eventHandler.exitState(context));

				eventHandlers.stream()
						.filter(eventHandler -> eventHandler.getStateName().equals(nextState))
						.forEach(eventHandler -> eventHandler.enterState(context));
			}
		} catch(IllegalAccessException exception){
			throw new RuntimeException(exception);
		}
	}

	void synchroniseState(Field stateField, CONTEXT context, String nextState) throws IllegalAccessException {
		if (stateField.get(context) instanceof String) {
			stateField.set(context, nextState);
		} else if (stateField.get(context) instanceof Enum) {
			stateField.set(context, Enum.valueOf((Class<Enum>) stateField.getType(), nextState));
		}
	}

	public String getNextState(String current, Facts facts) {
		return transitions.stream()
				.filter(transition -> !(transition instanceof ConditionalTransition))
				.map(transition -> (DirectTransition) transition)
				.filter(transition -> transition.getFromState().equals(current))
				.findFirst()
				.map(DirectTransition::getToState)
				.orElseGet(() -> getNextStateFromConditionalTransitions(current, facts));
	}

	private String getNextStateFromConditionalTransitions(String current, Facts facts) {
		List<ConditionalTransition> currentStateTransitions = transitions.stream()
				.filter(transition -> transition instanceof ConditionalTransition)
				.map(transition -> (ConditionalTransition) transition)
				.filter(transition -> transition.getFromState().equals(current))
				.collect(Collectors.toList());

		for (ConditionalTransition transition: currentStateTransitions) {
			if (transition.getConditions().stream()
				.map(condition -> isConditionSatisfied(condition, facts))
				.allMatch(result -> result == true)) {
				return transition.getToState();
			}
		}
		return current;
	}

	private boolean isConditionSatisfied(Condition condition, Facts facts) {
		return facts.getFact(condition.getFactName())
				.map(value -> condition.solve(value))
				.orElse(false);
	}
}
