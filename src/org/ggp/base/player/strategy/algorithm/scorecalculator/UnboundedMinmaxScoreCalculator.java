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

			return getGoal(state, playerRole);
		}

		return calculateNoTerminalMaxScore(state, playerRole);
	}

	private int calculateNoTerminalMaxScore(MachineState state, Role playerRole) throws MoveDefinitionException,
			TransitionDefinitionException, GoalDefinitionException, SymbolFormatException {
		int score = MIN_GAME_SCORE;
		List<Move> moves = getStateMachine().getLegalMoves(state, playerRole);
		// when
		for (int i = 0; i < moves.size(); i++) {
			int branchesLeftToSearch = moves.size() - i;
			if (hasTimedout(branchesLeftToSearch)) {
				break;
			}

			Move move = moves.get(i);
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
		int score = MAX_GAME_SCORE;

		MachineState newMachineState = simulateRandomJointMove(machineState, playerMove);
		int result = calculateMaxScore(newMachineState);
		if (result < score) {
			score = result;
		}

		return score;
	}
}