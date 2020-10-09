package nl.suriani.jadeval.execution;

import nl.suriani.jadeval.execution.workflow.WorkflowOptions;
import nl.suriani.jadeval.symbols.value.Facts;
import nl.suriani.jadeval.models.JadevalModel;
import nl.suriani.jadeval.execution.decision.DecisionResults;

import java.util.Map;

public class JadevalExecutor {
	private JadevalModel model;

	public JadevalExecutor(JadevalModel model) {
		this.model = model;
	}

	public DecisionResults applyDecisions(Object... objects) {
		return applyDecisions(new Facts(objects));
	}

	public DecisionResults applyDecisions(Map<String, Object> factsMap) {
		return applyDecisions(new Facts(factsMap));
	}

	public void applyValidations(Object... objects) {
		applyValidations(new Facts(objects));
	}

	public void applyValidations(Map<String, Object> factsMap) {
		applyValidations(new Facts(factsMap));
	}

	public WorkflowDelegate workflow(WorkflowOptions workflowOptions) {
		return new WorkflowDelegate(model, workflowOptions);
	}

	private DecisionResults applyDecisions(Facts facts) {
		return new DecisionsDelegate(model).apply(facts);
	}

	private void applyValidations(Facts facts) {
		new ValidationsDelegate(model).apply(facts);
	}
}
