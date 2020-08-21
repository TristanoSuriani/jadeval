package nl.suriani.jadeval;

import nl.suriani.jadeval.validation.ValidationException;
import nl.suriani.jadeval.validation.ValidationsBuilder;

import static nl.suriani.jadeval.RPSLSGame.Status.NEW;
import static nl.suriani.jadeval.RPSLSGame.Status.WAITING_FOR_PLAYER2;

public class RPSLSValidationsBuilder extends ValidationsBuilder<RPSLSGame> {
	@Override
	public void compile() {
		validation("The score may not be bigger than the maximum allowed")
				.when(this::theStatusOfTheGameIsNotNewNorWaitingForPlayer2)
				.then(rpslsGame -> rpslsGame.getPlayer1().getScore() <= RPSLSGame.MAXIMUM_SCORE)
				.and(rpslsGame -> rpslsGame.getPlayer2().getScore() <= RPSLSGame.MAXIMUM_SCORE)
				.orElseThrow(() -> new ValidationException("Incorrect score"));

	}

	private boolean theStatusOfTheGameIsNotNewNorWaitingForPlayer2(RPSLSGame rpslsGame) {
		return rpslsGame.getStatus() != NEW && rpslsGame.getStatus() != WAITING_FOR_PLAYER2;
	}
}
