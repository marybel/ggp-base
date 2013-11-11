package org.ggp.base.player.strategy.algorithm;

import java.util.ArrayList;
import java.util.List;

import org.ggp.base.player.gamer.statemachine.StateMachineGamer;
import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.Role;
import org.ggp.base.util.statemachine.StateMachine;
import org.ggp.base.util.statemachine.exceptions.GoalDefinitionException;
import org.ggp.base.util.statemachine.exceptions.MoveDefinitionException;
import org.ggp.base.util.statemachine.exceptions.TransitionDefinitionException;
import org.ggp.base.util.symbol.factory.exceptions.SymbolFormatException;

public class UnboundedMinmaxScoreCalculator implements MinimaxScoreCalculator {
	private StateMachineGamer gamer;
	private long finishByMillis;

	public UnboundedMinmaxScoreCalculator(StateMachineGamer gamer, long finishByMillis) {
		this.gamer = gamer;
		this.finishByMillis = finishByMillis;
	}

	@Override
	public int calculateMaxScore(MachineState state) throws MoveDefinitionException,
			TransitionDefinitionException, GoalDefinitionException, SymbolFormatException {
		// given
		Role playerRole = gamer.getRole();
		// when
		if (getStateMachine().isTerminal(state)) {
			// then
			return getStateMachine().getGoal(state, playerRole);
		}
		// or then
		return calculateNoTerminalMaxScore(state, playerRole);
	}

	private int calculateNoTerminalMaxScore(MachineState state, Role playerRole) throws MoveDefinitionException,
			TransitionDefinitionException, GoalDefinitionException, SymbolFormatException {
		// given
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

	@Override
	public int calculateMinScore(MachineState machineState, Move playerMove)
			throws MoveDefinitionException, TransitionDefinitionException, GoalDefinitionException,
			SymbolFormatException {
		// given
		int score = 100;
		Role opponentRole = getOpponent(gamer.getRole());
		List<Move> opponentMoves = getStateMachine().getLegalMoves(machineState, opponentRole);
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

	private long getFinishByMillis() {
		return finishByMillis;
	}

	private List<Move> getMovesToSimulate(Move currentlyExploredMove, Move opponentMove) {
		List<Move> movesToSimulate = new ArrayList<>();
		movesToSimulate.add(currentlyExploredMove);
		movesToSimulate.add(opponentMove);

		return movesToSimulate;
	}

	private Role getOpponent(Role role) {
		for (Role gameRole : getStateMachine().getRoles()) {
			if (role.getName() != gameRole.getName()) {
				return gameRole;
			}
		}
		throw new RuntimeException("No opponent found while doing Minimax Tree Search for role " + role.getName());
	}

	private StateMachine getStateMachine() {
		return gamer.getStateMachine();
	}

}