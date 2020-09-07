package nl.suriani.jadeval.examples.decision.jdl;

import nl.suriani.jadeval.decision.Decisions;
import nl.suriani.jadeval.decision.DecisionsBuilder;
import nl.suriani.jadeval.decision.DecisionResults;

import java.util.HashMap;
import java.util.Map;

class BanUserExample {
	public static void main(String[] args) {
		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put("status", Status.REPORTED);
		factsMap.put("numberOfWarnings", 3);
		factsMap.put("timesReported", 5);

		String input = "\"Ban user if warned at least 3 times and reported more than 2 times\"" +
		"when status is REPORTED and numberOfWarnings >= 3 and timesReported >= 2 " +
				"then BAN_USER";

		Decisions decisions = DecisionsBuilder.newFromString(input).build();
		DecisionResults results = decisions.apply(factsMap);

		System.out.println(results.getResponses().get(0));
		// it prints BAN_USER
	}

	public enum Status {
		DISABLED,
		ENABLED,
		REPORTED,
		WARNED,
		BANNED
	}
}
