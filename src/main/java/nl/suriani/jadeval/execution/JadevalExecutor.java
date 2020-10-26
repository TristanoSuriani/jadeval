package nl.suriani.jadeval.execution;

import nl.suriani.jadeval.execution.decision.DecisionsDelegate;
import nl.suriani.jadeval.execution.statemachine.StateMachineDelegate;
import nl.suriani.jadeval.execution.statemachine.StateMachineOptions;
import nl.suriani.jadeval.execution.validation.ValidationsDelegate;
import nl.suriani.jadeval.execution.workflow.WorkflowDelegate;
import nl.suriani.jadeval.execution.workflow.WorkflowOptions;
import nl.suriani.jadeval.models.JadevalModel;

public class JadevalExecutor {
	private JadevalModel model;

	public JadevalExecutor(JadevalModel model) {
		this.model = model;
	}

	public DecisionsDelegate decision() {
		return new DecisionsDelegate(model);
	}

	public ValidationsDelegate validation() {
		return new ValidationsDelegate(model);
	}

	public <T> WorkflowDelegate<T> workflow(WorkflowOptions<T> workflowOptions) {
		return new WorkflowDelegate<T>(model, workflowOptions);
	}

	public <T> StateMachineDelegate<T> stateMachine(StateMachineOptions<T> stateMachineOptions) {
		return new StateMachineDelegate<T>(model, stateMachineOptions);
	}
}
