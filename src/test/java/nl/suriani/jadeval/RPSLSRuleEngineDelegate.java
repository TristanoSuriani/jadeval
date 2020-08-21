package nl.suriani.jadeval;

import java.util.Arrays;
import java.util.logging.Logger;

import static nl.suriani.jadeval.RPSLSGame.Move.LIZARD;
import static nl.suriani.jadeval.RPSLSGame.Move.PAPER;
import static nl.suriani.jadeval.RPSLSGame.Move.ROCK;
import static nl.suriani.jadeval.RPSLSGame.Move.SCISSORS;
import static nl.suriani.jadeval.RPSLSGame.Move.SPOCK;

public class RPSLSRuleEngineDelegate {

	public static final int MAXIMUM_SCORE = 3;
	private final static Logger log = Logger.getLogger(RPSLSRuleEngineDelegate.class.getName());

	public boolean player1CanMove(RPSLSGame game) {
		return Arrays.asList(RPSLSGame.Status.NEW_ROUND, RPSLSGame.Status.WAITING_FOR_MOVE_PLAYER1).contains(game.getStatus());
	}

	public boolean player2CanMove(RPSLSGame game) {
		return Arrays.asList(RPSLSGame.Status.NEW_ROUND, RPSLSGame.Status.WAITING_FOR_MOVE_PLAYER2).contains(game.getStatus());
	}

	public boolean player1Scores(RPSLSGame game) {
		RPSLSGame.Move movePlayer1 = game.getMovePlayer1();
		RPSLSGame.Move movePlayer2 = game.getMovePlayer2();

		return player1Scores(movePlayer1, movePlayer2);
	}

	public boolean player2Scores(RPSLSGame game) {
		RPSLSGame.Move movePlayer1 = game.getMovePlayer1();
		RPSLSGame.Move movePlayer2 = game.getMovePlayer2();

		return player1Scores(movePlayer2, movePlayer1);
	}

	public boolean maximumScoreHasNotBeenReached(RPSLSGame game) {
		return game.getPlayer1().getScore() < MAXIMUM_SCORE && game.getPlayer2().getScore() < MAXIMUM_SCORE;
	}

	public void printMoves(RPSLSGame game) {
		log.info(game.getPlayer1().getUsername() + " chooses " + game.getMovePlayer1() +
				", " + game.getPlayer2().getUsername() + " chooses " + game.getMovePlayer2());
	}

	public void printScore(RPSLSGame game) {
		log.info(game.getPlayer1().getUsername() + ": " + game.getPlayer1().getScore() +
				" - " + game.getPlayer2().getUsername() + ": " + game.getPlayer2().getScore());
	}

	public void printPlayer1Scores(RPSLSGame game) {
		log.info(game.getPlayer1().getUsername() + " scores!");
	}

	public void printPlayer2Scores(RPSLSGame game) {
		log.info(game.getPlayer2().getUsername() + " scores!");
	}

	public void printDraw(RPSLSGame game) {
		log.info("Draw");
	}

	public void printPlayer1Won(RPSLSGame game) {
		log.info(game.getPlayer1().getUsername() + " won!");
	}

	public void printPlayer2Won(RPSLSGame game) {
		log.info(game.getPlayer2().getUsername() + " won!");
	}

	public void incrementScorePlayer1(RPSLSGame game) {
		game.getPlayer1().incrementScore();
	}

	public void incrementScorePlayer2(RPSLSGame game) {
		game.getPlayer2().incrementScore();
	}

	private boolean player1Scores(RPSLSGame.Move movePlayer1, RPSLSGame.Move movePlayer2) {
		return movePlayer1 == ROCK && Arrays.asList(SCISSORS, LIZARD).contains(movePlayer2) ||
				movePlayer1 == PAPER && Arrays.asList(ROCK, SPOCK).contains(movePlayer2) ||
				movePlayer1 == SCISSORS && Arrays.asList(PAPER, LIZARD).contains(movePlayer2) ||
				movePlayer1 == LIZARD && Arrays.asList(PAPER, SPOCK).contains(movePlayer2) ||
				movePlayer1 == SPOCK && Arrays.asList(ROCK, SCISSORS).contains(movePlayer2);
	}
}
