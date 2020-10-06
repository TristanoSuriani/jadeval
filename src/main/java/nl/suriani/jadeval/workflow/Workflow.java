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

public class Workflow<CONTEXT> {
	private List<Transition> transitions;
	private Set<String> availableStates;
	private TransitionAttemptedEventHandler<CONTEXT> transitionAttemptedEventHandler;
	private List<StateUpdateEventHandler<CONTEXT>> eventHandlers;

	Workflow(List<Transition> transitions, Set<String> availableStates, TransitionAttemptedEventHandler<CONTEXT> transitionAttemptedEventHandler, List<StateUpdateEventHandler<CONTEXT>> eventHandlers) {
		this.transitions = transitions;
		this.availableStates = availableStates;
		this.eventHandlers = eventHandlers;
		this.transitionAttemptedEventHandler = transitionAttemptedEventHandler;
	}

	public void executeUntilPause(CONTEXT context) {
		Field stateField = getStateField(context);
		String stateNameBeforeUpdate = getStateName(context, stateField);
		updateState(context);
		String stateNameAfterUpdate = getStateName(context, stateField);
		if (!stateNameBeforeUpdate.equals(stateNameAfterUpdate) && isNextTransitionDirect(context)) {
			executeUntilPause(context);
		}
	}

	public void updateState(CONTEXT context) {
		Field stateField = getStateField(context);
		stateField.setAccessible(true);
		String stateName = getStateName(context, stateField);

		if (!availableStates.contains(stateName)) {
			throw new IllegalArgumentException("Invalid state " + stateName + ". It must be one of the following states:\n" + availableStates);
		}
		transitionAttemptedEventHandler.handle(context);
		String nextState = getNextState(stateName, new Facts(context));
		if (!stateName.equals(nextState)) {
			synchroniseState(stateField, context, nextState);

			eventHandlers.stream()
					.filter(eventHandler -> eventHandler.getStateName().equals(stateName))
					.forEach(eventHandler -> eventHandler.exitState(context));

			eventHandlers.stream()
					.filter(eventHandler -> eventHandler.getStateName().equals(nextState))
					.forEach(eventHandler -> eventHandler.enterState(context));
		}
		stateField.setAccessible(false);
	}

	private boolean isNextTransitionDirect(CONTEXT context) {
		String stateName = getStateName(context, getStateField(context));
		return transitions.stream()
				.filter(transition -> transition instanceof DirectTransition)
				.map(transition -> (DirectTransition) transition)
				.anyMatch(transitions -> transitions.getFromState().equals(stateName));
	}

	private String getStateName(CONTEXT context, Field stateField) {
		try {
			if (stateField == null) {
				throw new IllegalArgumentException("No @State annotation present in given object, cannot determine current state");
			}

			if (stateField.get(context) instanceof String) {
				return (String) stateField.get(context);
			} else if (stateField.get(context) instanceof Enum) {
				return ((Enum) stateField.get(context)).name();
			} else {
				throw new IllegalArgumentException("The property annotated with @State must be a non-null String or an enum");
			}
		} catch (IllegalAccessException exception) {
			throw new RuntimeException(exception);
		}
	}

	private Field getStateField(CONTEXT context) {
		return Arrays.asList(context.getClass().getDeclaredFields()).stream()
				.filter(field -> field.isAnnotationPresent(State.class))
				.findFirst()
				.orElse(null);
	}

	void synchroniseState(Field stateField, CONTEXT context, String nextState) {
		try {
			if (stateField.get(context) instanceof String) {
				stateField.set(context, nextState);
			} else if (stateField.get(context) instanceof Enum) {
				stateField.set(context, Enum.valueOf((Class<Enum>) stateField.getType(), nextState));
			}
		} catch (IllegalAccessException exception) {
			throw new RuntimeException(exception);
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
