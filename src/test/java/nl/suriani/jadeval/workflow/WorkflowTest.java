package nl.suriani.jadeval.workflow;

import nl.suriani.jadeval.common.Facts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

class WorkflowTest {
	private Workflow workflow;

	@BeforeEach
	void setUp() {
		workflow = new Workflow();
	}

	@Test
	void test() {
		Facts facts = new Facts();
		File file = new File("src/test/resources/workflow.jwl");
		workflow.build(facts, file);
	}
}
