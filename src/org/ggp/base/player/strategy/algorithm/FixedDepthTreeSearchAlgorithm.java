package org.ggp.base.player.strategy.algorithm;

import java.util.List;

import org.ggp.base.player.gamer.statemachine.strategic.fixedDepth.AbstractFixedDepthGamer;
import org.ggp.base.player.strategy.algorithm.scorecalculator.FixedDepthScoreCalculator;
import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.exceptions.GoalDefinitionException;
import org.ggp.base.util.statemachine.exceptions.MoveDefinitionException;
import org.ggp.base.util.statemachine.exceptions.TransitionDefinitionException;
import org.ggp.base.util.symbol.factory.exceptions.SymbolFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixedDepthTreeSearchAlgorithm implements SearchAlgorithm {
	private static Logger LOGGER = LoggerFactory.getLogger(FixedDepthTreeSearchAlgorithm.class);
	private AbstractFixedDepthGamer gamer;

	public FixedDepthTreeSearchAlgorithm(AbstractFixedDepthGamer gamer) {
		this.gamer = gamer;
	}

	@Override
	public Move getSelectedMove(List<Move> moves, long finishByMillis) throws MoveDefinitionException,
			TransitionDefinitionException, GoalDefinitionException, SymbolFormatException {
		// given
		Move bestMoveFound = moves.get(0);
		// when
		if (moves.size() > 1) {
			FixedDepthScoreCalculator scoreCalculator = new
					FixedDepthScoreCalculator(gamer, finishByMillis);
			int score = 0;
			for (Move move : moves) {
				if (System.currentTimeMillis() > finishByMillis) {
					break;
				}
				int result = scoreCalculator.calculateMinScore(getMachineState(), move, 0);
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
