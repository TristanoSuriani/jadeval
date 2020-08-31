package nl.suriani.jadeval;

import org.antlr.v4.gui.TestRig;

/**
 * Test tool used to launch the TestRig tool (aka grun) to test the antlr4 grammar
 */
class GrunLauncher {
	// *************** Configurations **********************************
	private static final String FOLDER_TEST_FILE = "/Users/tristanosuriani/workspace/IdeaProjects/jadeval/src/test/resources/";
	private static final String NAME_TEST_FILE = "workflow.jwl";
	private static final String GRAMMAR_CLASS_NAME = "nl.suriani.jadeval.workflow.Workflow";
	private static final String OPTIONS = "-gui";
	private static final String ROOT_RULE = "workflowDefinition";
	private static final String PATH_TEST_FILE = FOLDER_TEST_FILE.concat(NAME_TEST_FILE);
	// ******************************************************************

	/**
	 * Launches the TestRig tool (aka grun). The test must be stopped manually.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new TestRig(ARGUMENTS).process();

		// Suspends the thread in order to allow the TestRig GUI to keep running.
		Thread.currentThread().suspend();
	}

	private static final String[] ARGUMENTS = new String[]{
			GRAMMAR_CLASS_NAME,
			ROOT_RULE,
			PATH_TEST_FILE,
			OPTIONS
	};
}
