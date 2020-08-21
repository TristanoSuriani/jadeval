package nl.suriani.jadeval;

import nl.suriani.jadeval.rule.RulesBuilder;

public class RPSLRulesBuilder extends RulesBuilder<RPSLSGame> {
	
	private RPSLSRuleEngineDelegate rpslsRuleEngineDelegate;

	public RPSLRulesBuilder(RPSLSRuleEngineDelegate rpslsRuleEngineDelegate) {
		this.rpslsRuleEngineDelegate = rpslsRuleEngineDelegate;
	}

	@Override
	protected void compile() {
		rule("Draw")
				.when(game -> game.getStatus() == RPSLSGame.Status.READY_FOR_EVALUATION)
				.and(rpslsRuleEngineDelegate::maximumScoreHasNotBeenReached)
				.and(game -> game.getMovePlayer1() == game.getMovePlayer2())
				.then(rpslsRuleEngineDelegate::printMoves)
				.andThen(rpslsRuleEngineDelegate::printDraw)
				.andThen(rpslsRuleEngineDelegate::printScore)
				.andThen(game -> game.setStatus(RPSLSGame.Status.NEW_ROUND))
				.end();

		rule("Player1 scores")
				.when(game -> game.getStatus() == RPSLSGame.Status.READY_FOR_EVALUATION)
				.and(rpslsRuleEngineDelegate::maximumScoreHasNotBeenReached)
				.and(rpslsRuleEngineDelegate::player1Scores)
				.then(rpslsRuleEngineDelegate::printMoves)
				.andThen(rpslsRuleEngineDelegate::printPlayer1Scores)
				.andThen(rpslsRuleEngineDelegate::incrementScorePlayer1)
				.andThen(rpslsRuleEngineDelegate::printScore)
				.andThen(game -> game.setStatus(RPSLSGame.Status.NEW_ROUND))
				.end();

		rule("Player2 scores")
				.when(game -> game.getStatus() == RPSLSGame.Status.READY_FOR_EVALUATION)
				.and(rpslsRuleEngineDelegate::maximumScoreHasNotBeenReached)
				.and(rpslsRuleEngineDelegate::player2Scores)
				.then(rpslsRuleEngineDelegate::printMoves)
				.andThen(rpslsRuleEngineDelegate::printPlayer2Scores)
				.andThen(rpslsRuleEngineDelegate::incrementScorePlayer2)
				.andThen(rpslsRuleEngineDelegate::printScore)
				.andThen(game -> game.setStatus(RPSLSGame.Status.NEW_ROUND))
				.end();

		rule("Player1 won the match")
				.when(game -> game.getPlayer1().getScore() == RPSLSRuleEngineDelegate.MAXIMUM_SCORE)
				.then(rpslsRuleEngineDelegate::printPlayer1Won)
				.andThen(game -> game.setStatus(RPSLSGame.Status.PLAYER1_WON))
				.end();

		rule("Player2 won the match")
				.when(game -> game.getPlayer2().getScore() == RPSLSRuleEngineDelegate.MAXIMUM_SCORE)
				.then(rpslsRuleEngineDelegate::printPlayer2Won)
				.andThen(game -> game.setStatus(RPSLSGame.Status.PLAYER2_WON))
				.end();
	}
}
