package nl.suriani.jadeval.models;

public class JadevalModel {
	private JadevalModelType modelType;
	private RuleSet ruleSet;
	private TransitionSet transitionSet;
	private StateSet stateSet;

	public JadevalModel(JadevalModelType modelType, RuleSet ruleSet) {
		this.modelType = modelType;
		this.ruleSet = ruleSet;
		this.stateSet = new StateSet();
		this.transitionSet = new TransitionSet();
	}

	public JadevalModel(JadevalModelType modelType, StateSet stateSet, TransitionSet transitionSet) {
		this.modelType = modelType;
		this.stateSet = stateSet;
		this.transitionSet = transitionSet;
		this.ruleSet = new RuleSet();
	}

	public JadevalModelType getModelType() {
		return modelType;
	}

	public RuleSet getRuleSet() {
		return ruleSet;
	}

	public TransitionSet getTransitionSet() {
		return transitionSet;
	}

	public StateSet getStateSet() {
		return stateSet;
	}
}
