package org.ggp.base.player.strategy.algorithm.scorecalculator;

import java.util.List;

import org.ggp.base.player.gamer.statemachine.strategic.FixedDepthGamer;
import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.Role;
import org.ggp.base.util.statemachine.exceptions.GoalDefinitionException;
import org.ggp.base.util.statemachine.exceptions.MoveDefinitionException;
import org.ggp.base.util.statemachine.exceptions.TransitionDefinitionException;
import org.ggp.base.util.symbol.factory.exceptions.SymbolFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixedDepthScoreCalculator extends AbstractScoreCalculator {
	private static Logger LOGGER = LoggerFactory.getLogger(FixedDepthScoreCalculator.class);
	private FixedDepthGamer fixedDepthGamer;

	public FixedDepthScoreCalculator(FixedDepthGamer gamer, long finishByMillis) {
		setGamer(gamer);
		setFinishByMillis(finishByMillis);
		fixedDepthGamer = (FixedDepthGamer) getGamer();
	}

	public int calculateMaxScore(MachineState state, Integer level) throws MoveDefinitionException,
			TransitionDefinitionException, GoalDefinitionException, SymbolFormatException {
		Role playerRole = getGamer().getRole();
		if (getStateMachine().isTerminal(state)) {

			return getStateMachine().getGoal(state, playerRole);
		}

		return calculateNoTerminalMaxScore(state, playerRole, level);
	}

	private int calculateNoTerminalMaxScore(MachineState state, Role playerRole, Integer level)
			throws MoveDefinitionException, TransitionDefinitionException, GoalDefinitionException,
			SymbolFormatException {
		if (hasMaxLevelBeenReach(level)) {
			int heuristicScore = fixedDepthGamer.getHeuristicFunction().getScore();
			LOGGER.debug("Returning {} because exiding level limit {}", heuristicScore,
					((FixedDepthGamer) getGamer()).getLevelLimit());

			return heuristicScore;
		}

		int score = 0;
		List<Move> moves = getStateMachine().getLegalMoves(state, playerRole);
		// when
		for (Move move : moves) {
			if (System.currentTimeMillis() > getFinishByMillis()) {
				break;
			}

			int result = calculateMinScore(state, move, level);
			if (result == 100) {
				LOGGER.debug("Returning because 100 result found in level {}. Max level {}", level,
						((FixedDepthGamer) getGamer()).getLevelLimit());

				return 100;
			}
			if (result > score) {
				score = result;
			}

		}
		// then
		return score;
	}

	public int calculateMinScore(MachineState machineState, Move playerMove, Integer level)
			throws MoveDefinitionException, TransitionDefinitionException, GoalDefinitionException,
			SymbolFormatException {
		// given
		int score = 100;
		List<Move> opponentMoves = getOpponentMoves(machineState);
		// when
		for (Move opponentMove : opponentMoves) {
			if (System.currentTimeMillis() > getFinishByMillis()) {
				break;
			}

			List<Move> movesToSimulate = getMovesToSimulate(playerMove, opponentMove);
			MachineState newMachineState = getStateMachine().getNextState(machineState, movesToSimulate);
			int result = calculateMaxScore(newMachineState, level + 1);
			if (result == 0) {
				return 0;
			}
			if (result < score) {
				score = result;
			}

		}
		// then
		return score;
	}

	private boolean hasMaxLevelBeenReach(Integer level) {

		return level >= fixedDepthGamer.getLevelLimit();
	}
}