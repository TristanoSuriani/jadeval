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

public class WorkflowExecutor<T> {
	private List<Transition> transitions;
	private Set<String> availableStates;
	private List<StateUpdateEventHandler<T>> eventHandlers;

	WorkflowExecutor(List<Transition> transitions, Set<String> availableStates, List<StateUpdateEventHandler<T>> eventHandlers) {
		this.transitions = transitions;
		this.availableStates = availableStates;
		this.eventHandlers = eventHandlers;
	}

	public void updateState(T object) {
		try {
			String stateName;
			Field stateField = Arrays.asList(object.getClass().getDeclaredFields())
					.stream().filter(field -> field.isAnnotationPresent(State.class))
					.peek(field -> field.setAccessible(true))
					.findFirst()
					.orElse(null);

			if (stateField == null) {
				throw new IllegalArgumentException("No @State annotation present in given object, cannot determine current state");
			}

			if (stateField.get(object) instanceof String) {
				stateName = (String) stateField.get(object);
			} else if (stateField.get(object) instanceof Enum) {
				stateName = ((Enum) stateField.get(object)).name();
			} else {
				throw new IllegalArgumentException("The property annotated with @State must be a String or an enum");
			}

			String nextState = getNextState(stateName, new Facts(object));
			if (!stateName.equals(nextState)) {


				if (!availableStates.contains(stateName)) {
					throw new IllegalArgumentException("Invalid state " + stateName + ". It must be one of the following states:\n" + availableStates);
				}

				eventHandlers.stream().
						filter(eventHandler -> eventHandler.getStateName().equals(stateName))
						.forEach(eventHandler -> eventHandler.exitState(object));

				eventHandlers.stream()
						.filter(eventHandler -> eventHandler.getStateName().equals(nextState))
						.forEach(eventHandler -> eventHandler.enterState(object));
			}
		} catch(IllegalAccessException exception){
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
