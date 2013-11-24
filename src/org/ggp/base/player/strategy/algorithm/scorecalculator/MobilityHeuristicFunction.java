package org.ggp.base.player.strategy.algorithm.scorecalculator;

import java.util.List;

import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.Role;
import org.ggp.base.util.statemachine.exceptions.MoveDefinitionException;

public class MobilityHeuristicFunction extends AbstractScoreCalculator implements HeuristicFunction {

	@Override
	public int getScore(MachineState state, Role playerRole) throws MoveDefinitionException {
		List<Move> legalMoves = getGamer().getStateMachine().getLegalMoves(state, playerRole);
		List<Move> opponentMoves = getOpponentMoves(state);

		int rawScore = 100 - (opponentMoves.size() * 100 / legalMoves.size());
		return rawScore > 0 ? rawScore : 0;
	}
}
