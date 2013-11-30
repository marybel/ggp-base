package org.ggp.base.player.strategy.algorithm.scorecalculator;

import java.util.List;
import java.util.Map;

import org.ggp.base.player.gamer.statemachine.strategic.fixedDepth.AbstractFixedDepthGamer;
import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.Role;
import org.ggp.base.util.statemachine.exceptions.GoalDefinitionException;
import org.ggp.base.util.statemachine.exceptions.MoveDefinitionException;
import org.ggp.base.util.statemachine.exceptions.TransitionDefinitionException;
import org.ggp.base.util.symbol.factory.exceptions.SymbolFormatException;

public class FixedDepthScoreCalculator extends AbstractScoreCalculator {

	private static final int MAX_GAME_SCORE = 100;
	private static final int MIN_GAME_SCORE = 0;
	private AbstractFixedDepthGamer fixedDepthGamer;

	public FixedDepthScoreCalculator(AbstractFixedDepthGamer gamer, long finishByMillis) {
		setGamer(gamer);
		setFinishByMillis(finishByMillis);
		fixedDepthGamer = (AbstractFixedDepthGamer) getGamer();
	}

	public int calculateMaxScore(MachineState state, Integer level) throws MoveDefinitionException,
			TransitionDefinitionException, GoalDefinitionException, SymbolFormatException {
		printLevel(level);
		Role playerRole = getGamer().getRole();
		if (getStateMachine().isTerminal(state)) {

			return getTerminalScore(state, playerRole, level);
		}

		return calculateNoTerminalMaxScore(state, playerRole, level);
	}

	private int calculateNoTerminalMaxScore(MachineState state, Role playerRole, Integer level)
			throws MoveDefinitionException, TransitionDefinitionException, GoalDefinitionException,
			SymbolFormatException {
		if (hasMaxLevelBeenReach(level)) {
			return getMaxLevelHeuristicScore(state, playerRole);
		}

		int score = 0;
		List<Move> moves = getStateMachine().getLegalMoves(state, playerRole);
		// when
		for (int i = 0; i < moves.size(); i++) {
			if (System.currentTimeMillis() > getFinishByMillis(moves.size() - i)) {
				break;
			}

			Move move = moves.get(i);
			int result = calculateMinScore(state, move, level);
			if (result == 100) {
				return getMaxGameScore(move, level);
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
		Map<Role, List<Move>> opponentsMoves = getOpponentMoves(machineState);
		for (java.util.Map.Entry<Role, List<Move>> opponentMovesEntry : opponentsMoves.entrySet()) {
			List<Move> opponentMoves = opponentMovesEntry.getValue();
			for (int i = 0; i < opponentMoves.size(); i++) {
				if (System.currentTimeMillis() > getFinishByMillis(opponentMoves.size() - i)) {
					break;
				}
				Move opponentMove = opponentMoves.get(i);
				List<Move> movesToSimulate = getMovesToSimulate(playerMove, opponentMove);
				MachineState newMachineState = getStateMachine().getNextState(machineState, movesToSimulate);
				int result = calculateMaxScore(newMachineState, level + 1);
				if (result == 0) {
					return getMinGameScore(level);
				}
				if (result < score) {
					score = result;
				}
			}
		}

		return score;
	}

	private boolean hasMaxLevelBeenReach(Integer level) {

		return level >= fixedDepthGamer.getLevelLimit();
	}

	private int getMaxLevelHeuristicScore(MachineState state, Role playerRole) throws MoveDefinitionException,
			GoalDefinitionException {
		int heuristicScore = fixedDepthGamer.getHeuristicFunction().getScore(state, playerRole);
		System.out.print("L=" + ((AbstractFixedDepthGamer) getGamer()).getLevelLimit() + "{" + heuristicScore + "}");

		return heuristicScore;
	}

	private int getTerminalScore(MachineState state, Role playerRole, Integer level) throws GoalDefinitionException {
		int goal = getStateMachine().getGoal(state, playerRole);
		System.out.print("LT" + level + "{" + goal + "}");

		return goal;
	}

	private int getMaxGameScore(Move move, Integer level) {
		System.out.print("L>" + level + "{" + MAX_GAME_SCORE + "}. " + move);

		return MAX_GAME_SCORE;
	}

	private int getMinGameScore(Integer level) {
		System.out.print("L<" + level + "{" + MIN_GAME_SCORE + "}");
		return MIN_GAME_SCORE;
	}

	private void printLevel(Integer level) {
		System.out.print("\n");
		for (int i = 0; i < level; i++) {
			System.out.print(".");

		}

	}
}