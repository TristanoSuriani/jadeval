package nl.suriani.jadeval.execution.decision;

import nl.suriani.jadeval.execution.JadevalExecutor;
import nl.suriani.jadeval.JadevalLoader;
import nl.suriani.jadeval.models.JadevalModel;
import nl.suriani.jadeval.annotation.Fact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

class DecisionsTest {

	@Fact
	private double amount;

	@Fact
	private boolean disconnected;

	@Fact
	private boolean connected;

	@Fact
	private double credit;

	@Fact
	private String thing;

	@Fact(qualifier = "list")
	private List<String> someList;

	private static final String FILE_NAME = "src/test/resources/decisions.jdl";

	@BeforeEach
	void setUp() {
		amount = 0.1;
		disconnected = true;
		connected = true;
		credit = 2000.01;
		thing = "Ayeye";
		someList = Arrays.asList("1", "element", "stuff");
	}

	@Test
	void runFromFileNameAndAnnotedFacts() {
		/*
			Matching rule:
			when amount is 0.1
				and disconnected is not false
				then LOG_NOT_DISCONNECTED
				and LOG_CONNECTED
		 */
		JadevalModel model = new JadevalLoader().load(new File(FILE_NAME));
		DecisionResults resultsTable = new JadevalExecutor(model).decision().apply(this);

		List<String> responses = resultsTable.getResponses();
		assertEquals("This rule has a description", resultsTable.getResults().get(0).getDescription());
		assertEquals("This rule has a description too", resultsTable.getResults().get(2).getDescription());
		assertEquals(4, responses.size());
		assertLinesMatch(responses, Arrays.asList("LOG_NOT_DISCONNECTED", "LOG_CONNECTED", "HOUSTON_GOT_PROBLEM", "GET_Z"));
	}

	@Test
	void runFromString() {
		String string = "\"description.\"\n\n\n\n\nwhen connected is true and credit > 1234.56 then CONGRATULATE /*multilinecommentBut123132RQRQInline!!!~\\*/and SEND_10_EUROS_COUPON//Comment!@#@%!";
		JadevalModel model = new JadevalLoader().load(string);
		DecisionResults resultsTable = new JadevalExecutor(model).decision().apply(this);

		List<String> responses = resultsTable.getResponses();
		assertEquals(1, resultsTable.getResults().size());
		assertEquals("description.", resultsTable.getResults().get(0).getDescription());
		assertEquals(2, responses.size());
		assertLinesMatch(responses, Arrays.asList("CONGRATULATE", "SEND_10_EUROS_COUPON"));
	}
}
