package org.ggp.base.player.strategy.algorithm.scorecalculator;

import java.util.List;

import org.ggp.base.player.gamer.statemachine.StateMachineGamer;
import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.Role;
import org.ggp.base.util.statemachine.exceptions.MoveDefinitionException;
import org.ggp.base.util.statemachine.exceptions.TransitionDefinitionException;
import org.ggp.base.util.symbol.factory.exceptions.SymbolFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlphaBetaScoreCalculator extends AbstractScoreCalculator {
	private static Logger LOGGER = LoggerFactory.getLogger(AlphaBetaScoreCalculator.class);

	public AlphaBetaScoreCalculator(StateMachineGamer gamer, long finishByMillis) {
		setGamer(gamer);
		setFinishByMillis(finishByMillis);
	}

	public int calculateMaxScore(MachineState state, int alpha, int beta) throws MoveDefinitionException,
			TransitionDefinitionException, SymbolFormatException {
		Role playerRole = getGamer().getRole();

		if (getStateMachine().isTerminal(state)) {

			return getGoal(state, playerRole);
		}

		return calculateNoTerminalMaxScore(state, playerRole, alpha, beta);
	}

	private int calculateNoTerminalMaxScore(MachineState state, Role playerRole, int alpha, int beta)
			throws MoveDefinitionException,
			TransitionDefinitionException, SymbolFormatException {
		List<Move> moves = getStateMachine().getLegalMoves(state, playerRole);

		for (int i = 0; i < moves.size(); i++) {
			int branchesLeftToSearch = moves.size() - i;
			if (hasTimedout(branchesLeftToSearch)) {
				LOGGER.debug("Cutting alpha-beta search short");
				break;
			}
			Move move = moves.get(i);
			int result = calculateMinScore(state, move, alpha, beta);

			if (alpha <= result) {
				alpha = result;
			}

			if (alpha >= beta) {
				return beta;
			}

		}

		return alpha;
	}

	public int calculateMinScore(MachineState machineState, Move playerMove, int alpha, int beta)
			throws MoveDefinitionException, TransitionDefinitionException, SymbolFormatException {
		MachineState newMachineState = simulateRandomJointMove(machineState, playerMove);
		int result = calculateMaxScore(newMachineState, alpha, beta);
		if (beta > result) {
			beta = result;
		}

		if (beta <= alpha) {
			return alpha;
		}

		return beta;
	}
}