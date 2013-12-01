package org.ggp.base.player.strategy.algorithm.scorecalculator;

import java.util.List;

import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.Role;
import org.ggp.base.util.statemachine.exceptions.MoveDefinitionException;

public class MobilityHeuristicFunction extends AbstractScoreCalculator implements HeuristicFunction {

	@Override
	public int getScore(MachineState machineState, Role playerRole) {
		int legalMoveCount = 0;
		List<Move> legalMoves;
		try {
			legalMoves = getGamer().getStateMachine().getLegalMoves(machineState, playerRole);
		} catch (MoveDefinitionException e) {
			e.printStackTrace();
			return 0;
		}

		for (Move move : legalMoves) {
			try {
				getStateMachine().getRandomJointMove(machineState, getGamer().getRole(), move);
				legalMoveCount++;
			} catch (MoveDefinitionException e) {
				e.printStackTrace();
			}
		}

		return legalMoveCount * 100 / legalMoves.size();
	}
}
