package org.ggp.base.player.strategy.algorithm;

import java.util.List;

import org.ggp.base.player.gamer.statemachine.StateMachineGamer;
import org.ggp.base.player.gamer.statemachine.strategic.MinimaxGamer;
import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.exceptions.GoalDefinitionException;
import org.ggp.base.util.statemachine.exceptions.MoveDefinitionException;
import org.ggp.base.util.statemachine.exceptions.TransitionDefinitionException;
import org.ggp.base.util.symbol.factory.exceptions.SymbolFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinimaxTreeSearchAlgorithm implements SearchAlgorithm {
	private static Logger LOGGER = LoggerFactory.getLogger(MinimaxTreeSearchAlgorithm.class);
	private StateMachineGamer gamer;

	public MinimaxTreeSearchAlgorithm(MinimaxGamer minimaxGamer) {
		this.gamer = minimaxGamer;
	}

	@Override
	public Move getBestMove(List<Move> moves, long finishByMillis) throws MoveDefinitionException,
			TransitionDefinitionException, GoalDefinitionException, SymbolFormatException {
		// given
		Move bestMoveFound = moves.get(0);
		// when
		if (moves.size() > 1) {
			MinimaxScoreCalculator scoreCalculator = new UnboundedMinmaxScoreCalculator(gamer, finishByMillis);

			int score = 0;
			for (Move move : moves) {
				if (System.currentTimeMillis() > finishByMillis) {
					break;
				}
				int result = scoreCalculator.calculateMinScore(getMachineState(), move);
				if (result == 100) {
					return move;
				}
				if (result > score) {
					score = result;
					bestMoveFound = move;
				}
			}

		}
		// then
		LOGGER.debug("Overtime was {} millis", System.currentTimeMillis() - finishByMillis);
		return bestMoveFound;
	}

	private MachineState getMachineState() {
		return gamer.getCurrentState();
	}
}
