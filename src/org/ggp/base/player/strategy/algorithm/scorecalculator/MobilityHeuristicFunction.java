package org.ggp.base.player.strategy.algorithm.scorecalculator;

import java.util.List;
import java.util.Map;

import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.Role;
import org.ggp.base.util.statemachine.exceptions.MoveDefinitionException;

public class MobilityHeuristicFunction extends AbstractScoreCalculator implements HeuristicFunction {

	@Override
	public int getScore(MachineState state, Role playerRole) throws MoveDefinitionException {
		List<Move> legalMoves = getGamer().getStateMachine().getLegalMoves(state, playerRole);
		Map<Role, List<Move>> opponentsMoves = getOpponentMoves(state);
		int rawScore = 100;
		for (java.util.Map.Entry<Role, List<Move>> opponentMovesEntry : opponentsMoves.entrySet()) {
			List<Move> opponentMoves = opponentMovesEntry.getValue();
			rawScore = 100 - (opponentMoves.size() * rawScore / legalMoves.size());
		}

		return rawScore > 0 ? rawScore : 0;
	}
}
