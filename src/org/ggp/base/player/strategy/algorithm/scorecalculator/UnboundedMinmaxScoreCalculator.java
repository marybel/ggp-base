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

public class UnboundedMinmaxScoreCalculator extends AbstractScoreCalculator {
	
	public UnboundedMinmaxScoreCalculator(StateMachineGamer gamer, long finishByMillis) {
		setGamer(gamer);
		setFinishByMillis(finishByMillis);
	}

	public int calculateMaxScore(MachineState state) throws MoveDefinitionException,
			TransitionDefinitionException, GoalDefinitionException, SymbolFormatException {
		Role playerRole = getGamer().getRole();
		if (getStateMachine().isTerminal(state)) {

			return getStateMachine().getGoal(state, playerRole);
		}

		return calculateNoTerminalMaxScore(state, playerRole);
	}

	private int calculateNoTerminalMaxScore(MachineState state, Role playerRole) throws MoveDefinitionException,
			TransitionDefinitionException, GoalDefinitionException, SymbolFormatException {
		int score = 0;
		List<Move> moves = getStateMachine().getLegalMoves(state, playerRole);
		// when
		for (Move move : moves) {
			if (System.currentTimeMillis() > getFinishByMillis()) {
				break;
			}

			int result = calculateMinScore(state, move);
			if (result > score) {
				score = result;
			}

		}
		// then
		return score;
	}

	public int calculateMinScore(MachineState machineState, Move playerMove)
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
			int result = calculateMaxScore(newMachineState);
			if (result < score) {
				score = result;
			}

		}
		// then
		return score;
	}
}