package org.ggp.base.player.strategy.algorithm.scorecalculator;

import java.util.List;

import org.ggp.base.player.gamer.statemachine.StateMachineGamer;
import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.Role;
import org.ggp.base.util.statemachine.exceptions.GoalDefinitionException;
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
			TransitionDefinitionException, GoalDefinitionException, SymbolFormatException {
		Role playerRole = getGamer().getRole();

		if (getStateMachine().isTerminal(state)) {

			return getStateMachine().getGoal(state, playerRole);
		}

		return calculateNoTerminalMaxScore(state, playerRole, alpha, beta);
	}

	private int calculateNoTerminalMaxScore(MachineState state, Role playerRole, int alpha, int beta)
			throws MoveDefinitionException,
			TransitionDefinitionException, GoalDefinitionException, SymbolFormatException {
		List<Move> moves = getStateMachine().getLegalMoves(state, playerRole);

		for (Move move : moves) {
			if (System.currentTimeMillis() > getFinishByMillis()) {
				LOGGER.debug("Cutting alpha-beta search short");
				break;
			}

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
			throws MoveDefinitionException, TransitionDefinitionException, GoalDefinitionException,
			SymbolFormatException {

		Role opponentRole = getOpponent(getGamer().getRole());
		List<Move> opponentMoves = getStateMachine().getLegalMoves(machineState, opponentRole);

		for (Move opponentMove : opponentMoves) {
			if (System.currentTimeMillis() > getFinishByMillis()) {
				LOGGER.debug("Cutting alpha-beta search short");
				break;
			}

			List<Move> movesToSimulate = getMovesToSimulate(playerMove, opponentMove);
			MachineState newMachineState = getStateMachine().getNextState(machineState, movesToSimulate);
			int result = calculateMaxScore(newMachineState, alpha, beta);
			if (beta > result) {
				beta = result;
			}

			if (beta <= alpha) {
				return alpha;
			}
		}

		return beta;
	}
}