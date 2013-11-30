package org.ggp.base.player.strategy.algorithm.scorecalculator;

import java.util.List;

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
		
		for (int i = 0; i < moves.size(); i++) {
			int branchesLeftToSearch = moves.size() - i;
			if (hasTimedout(branchesLeftToSearch)) {
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
		List<Move> opponentsMoves = getOpponenstMove(machineState);
		List<Move> movesToSimulate = getMovesToSimulate(playerMove, opponentsMoves);
		MachineState newMachineState = getStateMachine().getNextState(machineState, movesToSimulate);
		int result = calculateMaxScore(newMachineState, level + 1);
		if (result == 0) {
			return getMinGameScore(level);
		}
		if (result < score) {
			score = result;
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
		int goal = getGoal(state, playerRole);
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
}