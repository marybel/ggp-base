package org.ggp.base.player.strategy.algorithm;

import java.util.ArrayList;
import java.util.List;

import org.ggp.base.player.gamer.statemachine.StateMachineGamer;
import org.ggp.base.player.gamer.statemachine.strategic.MinimaxGamer;
import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.Role;
import org.ggp.base.util.statemachine.StateMachine;
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
		Move bestMoveFound = moves.get(0);

		if (moves.size() > 1) {
			int score = 0;
			for (Move move : moves) {
				if (System.currentTimeMillis() > finishByMillis) {
					break;
				}
				int result = calculateMinScore(getMachineState(), gamer.getRole(), move, finishByMillis);
				if (result == 100) {
					return move;
				}
				if (result > score) {
					score = result;
					bestMoveFound = move;
				}
			}

		}
		LOGGER.debug("Overtime was {} millis", System.currentTimeMillis() - finishByMillis);
		return bestMoveFound;
	}

	private MachineState getMachineState() {
		return gamer.getCurrentState();
	}

	private int calculateMinScore(MachineState currentState, Role role, Move playerMove, long finishByMillis)
			throws MoveDefinitionException, TransitionDefinitionException, GoalDefinitionException,
			SymbolFormatException {
		Role opponent = getOpponent(role);
		List<Move> opponentMoves = getStateMachine().getLegalMoves(currentState, opponent);
		int score = 100;

		for (Move opponentMove : opponentMoves) {
			if (System.currentTimeMillis() > finishByMillis) {
				break;
			}
			List<Move> movesToSimulate = getMovesToSimulate(playerMove, opponentMove);
			MachineState newMachineStatetate = getStateMachine().getNextState(currentState, movesToSimulate);
			int result = calculateMaxScore(newMachineStatetate, role, finishByMillis);
			if (result < score) {
				score = result;
			}
		}

		return score;

	}

	private List<Move> getMovesToSimulate(Move currentlyExploredMove, Move opponentMove) {
		List<Move> movesToSimulate = new ArrayList<>();
		movesToSimulate.add(currentlyExploredMove);
		movesToSimulate.add(opponentMove);

		return movesToSimulate;
	}

	private StateMachine getStateMachine() {
		return gamer.getStateMachine();
	}

	private int calculateMaxScore(MachineState state, Role role, long finishByMillis) throws MoveDefinitionException,
			TransitionDefinitionException, GoalDefinitionException, SymbolFormatException {
		if (getStateMachine().isTerminal(state)) {
			return getStateMachine().getGoal(state, role);
		}

		List<Move> moves = getStateMachine().getLegalMoves(state, role);
		int score = 0;

		for (Move move : moves) {
			if (System.currentTimeMillis() > finishByMillis) {
				break;
			}
			int result = calculateMinScore(state, role, move, finishByMillis);
			if (result > score) {
				score = result;
			}

		}

		return score;
	}

	private Role getOpponent(Role role) {
		for (Role gameRole : getStateMachine().getRoles()) {
			if (role.getName() != gameRole.getName()) {
				return gameRole;
			}
		}
		throw new RuntimeException("No opponent found while doing Minimax Tree Search for role " + role.getName());
	}
}
