package nl.suriani.jadeval.workflow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowTest {
	private Workflow workflow;

	@BeforeEach
	void setUp() {
		workflow = new Workflow();
	}

	@Test
	void test() {
		File file = new File("src/test/resources/workflow.jwl");
	}
}
