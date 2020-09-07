package nl.suriani.jadeval;

import org.antlr.v4.gui.TestRig;

/**
 * Test tool used to launch the TestRig tool (aka grun) to test the antlr4 grammar
 */
class GrunLauncher {
	/**
	 * Launches the TestRig tool (aka grun). The test must be stopped manually.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new TestRig(WORKFLOW_ARGUMENTS).process();

		// Suspends the thread in order to allow the TestRig GUI to keep running.
		Thread.currentThread().suspend();
	}

	// *************** Decisions ***************
	private static final String DECISIONS_TEST_FILE = "decisions.jdl";
	private static final String DECISIONS_GRAMMAR_CLASS_NAME = "nl.suriani.jadeval.decision.Decisions";
	private static final String DECISIONS_ROOT_RULE = "decisionsDefinition";

	// *************** Workflow ***************
	private static final String WORKFLOW_TEST_FILE = "workflow.jwl";
	private static final String WORKFLOW_GRAMMAR_CLASS_NAME = "nl.suriani.jadeval.workflow.Workflow";
	private static final String WORKFLOW_ROOT_RULE = "workflowDefinition";

	// *************** Validations ***************
	private static final String VALIDATIONS_TEST_FILE = "validations.jvl";
	private static final String VALIDATIONS_GRAMMAR_CLASS_NAME = "nl.suriani.jadeval.validation.Validations";
	private static final String VALIDATIONS_ROOT_RULE = "validationsDefinition";

	// *************** Common ***************
	private static final String FOLDER_TEST_FILE = "/Users/tristanosuriani/workspace/IdeaProjects/jadeval/src/test/resources/";
	private static final String OPTIONS = "-gui";

	// *************** ARGUMENTS ***************
	private static final String[] DECISIONS_ARGUMENTS = new String[]{DECISIONS_GRAMMAR_CLASS_NAME, DECISIONS_ROOT_RULE,
			FOLDER_TEST_FILE.concat(DECISIONS_TEST_FILE),
			OPTIONS
	};

	private static final String[] WORKFLOW_ARGUMENTS = new String[]{WORKFLOW_GRAMMAR_CLASS_NAME, WORKFLOW_ROOT_RULE,
			FOLDER_TEST_FILE.concat(WORKFLOW_TEST_FILE),
			OPTIONS
	};

	private static final String[] VALIDATIONS_ARGUMENTS = new String[]{VALIDATIONS_GRAMMAR_CLASS_NAME, VALIDATIONS_ROOT_RULE,
			FOLDER_TEST_FILE.concat(VALIDATIONS_TEST_FILE),
			OPTIONS
	};

	// *************** ***************
}
