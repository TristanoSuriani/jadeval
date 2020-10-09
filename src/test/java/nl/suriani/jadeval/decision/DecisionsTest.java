package nl.suriani.jadeval.decision;

import nl.suriani.jadeval.JadevalExecutor;
import nl.suriani.jadeval.JadevalLoader;
import nl.suriani.jadeval.JadevalModel;
import nl.suriani.jadeval.common.annotation.Fact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

class DecisionsTest {

	private Decisions model;

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
	void runFromFile() {
		String fileName = "src/test/resources/decisions.jdl";
		File file = new File(fileName);
		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put("connected", false);
		factsMap.put("credit", 2000);

		/*
			Matching rule:
			"This rule has a description"
			when connected is false
				and credit > 1234.56                // This is a comment
				then CONGRATULATE
				and SEND_10_EUROS_COUPON
		* */
		model = DecisionsBuilder.fromFile(file).build();
		DecisionResults resultsTable = model.apply(factsMap);

		List<String> responses = resultsTable.getResponses();
		assertEquals("This rule has a description", resultsTable.getResults().get(0).getDescription());
		assertEquals("This rule has a description too", resultsTable.getResults().get(2).getDescription());
		assertEquals(2, responses.size());
		assertLinesMatch(responses, Arrays.asList("CONGRATULATE", "SEND_10_EUROS_COUPON"));
	}

	@Test
	void runFromFileNewLoader() {
		String fileName = "src/test/resources/decisions.jdl";
		File file = new File(fileName);
		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put("connected", false);
		factsMap.put("credit", 2000);

		/*
			Matching rule:
			"This rule has a description"
			when connected is false
				and credit > 1234.56                // This is a comment
				then CONGRATULATE
				and SEND_10_EUROS_COUPON
		* */
		JadevalModel model = new JadevalLoader().load(file);
		System.out.println();
		DecisionResults resultsTable = new JadevalExecutor(model).applyDecisions(factsMap);

		List<String> responses = resultsTable.getResponses();
		assertEquals("This rule has a description", resultsTable.getResults().get(0).getDescription());
		assertEquals("This rule has a description too", resultsTable.getResults().get(2).getDescription());
		assertEquals(2, responses.size());
		assertLinesMatch(responses, Arrays.asList("CONGRATULATE", "SEND_10_EUROS_COUPON"));
	}

	@Test
	void runFromFileNameAndAnnotedFacts() {
		String fileName = "src/test/resources/decisions.jdl";

		/*
			Matching rule:
			when amount is 0.1
				and disconnected is not false
				then LOG_NOT_DISCONNECTED
				and LOG_CONNECTED
		 */
		model = DecisionsBuilder.fromFile(new File(fileName)).build();
		DecisionResults resultsTable = model.apply(this);

		List<String> responses = resultsTable.getResponses();
		assertEquals("This rule has a description", resultsTable.getResults().get(0).getDescription());
		assertEquals("This rule has a description too", resultsTable.getResults().get(2).getDescription());
		assertEquals(4, responses.size());
		assertLinesMatch(responses, Arrays.asList("LOG_NOT_DISCONNECTED", "LOG_CONNECTED", "HOUSTON_GOT_PROBLEM", "GET_Z"));
	}

	@Test
	void runFromInputStream() throws FileNotFoundException {
		String fileName = "src/test/resources/decisions.jdl";
		File file = new File(fileName);
		FileInputStream inputStream = new FileInputStream(file);
		/*
			Matching rule:
			when amount is 0.1
				and disconnected not false
				then LOG_NOT_DISCONNECTED
				and LOG_CONNECTED
		 */
		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put("debt", "big");
		factsMap.put("life_expectance", "short");
		factsMap.put("numberOfPartners", 1);
		model = DecisionsBuilder.fromInputStream(inputStream).build();
		DecisionResults resultsTable = model.apply(factsMap);
		List<String> responses = resultsTable.getResponses();
		assertEquals("This rule has a description", resultsTable.getResults().get(0).getDescription());
		assertEquals("This rule has a description too", resultsTable.getResults().get(2).getDescription());
		assertEquals(3, responses.size());
		assertLinesMatch(responses, Arrays.asList("SEND_COUPON", "SUGGEST_THERAPY", "CALL_THE_POLICE"));
	}

	@Test
	void runFromString() {
		String string = "\"description.\"\n\n\n\n\nwhen connected is true and credit > 1234.56 then CONGRATULATE /*multilinecommentBut123132RQRQInline!!!~\\*/and SEND_10_EUROS_COUPON//Comment!@#@%!";
		model = DecisionsBuilder.newFromString(string).build();
		DecisionResults resultsTable = model.apply(this, new ByteArrayInputStream(string.getBytes()));

		List<String> responses = resultsTable.getResponses();
		assertEquals(1, resultsTable.getResults().size());
		assertEquals("description.", resultsTable.getResults().get(0).getDescription());
		assertEquals(2, responses.size());
		assertLinesMatch(responses, Arrays.asList("CONGRATULATE", "SEND_10_EUROS_COUPON"));
	}
}
