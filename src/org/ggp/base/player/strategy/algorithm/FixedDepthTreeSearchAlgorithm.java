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

public class FixedDepthTreeSearchAlgorithm implements SearchAlgorithm {
	private AbstractFixedDepthGamer gamer;
	private long finishByMillis;

	public FixedDepthTreeSearchAlgorithm(AbstractFixedDepthGamer gamer) {
		this.gamer = gamer;
	}

	@Override
	public Move getSelectedMove(List<Move> moves, long finishByMillis) throws MoveDefinitionException,
			TransitionDefinitionException, GoalDefinitionException, SymbolFormatException {
		this.finishByMillis = finishByMillis;
		int selectedMoveIndex = 0;
		Move selectedMove = moves.get(selectedMoveIndex);
		int score = 0;

		if (moves.size() > 1) {
			for (int i = 0; i < moves.size(); i++) {
				gamer.setLevelLimit((DEFAULT_LEVEL_LIMIT_FACTOR * Math
						.round(getMillisToTimeout() / 10000)));
				FixedDepthScoreCalculator scoreCalculator = new FixedDepthScoreCalculator(gamer,
						getFinishByMillisForLevel0Node(moves.size() - i));
				Move move = moves.get(i);
				if (hasTimedOut()) {
					break;
				}
				int result = scoreCalculator.calculateMinScore(getMachineState(), move, 0);
				if (result == 100) {
					return move;
				}
				if (result > score) {
					score = result;
					selectedMoveIndex = i;
					selectedMove = move;
				}
			}
		}

		System.out.println("\nSelected " + selectedMove + "[" + selectedMoveIndex +
				"/" + moves.size() + "] = " + score);
		System.out.println("Overtime " + (System.currentTimeMillis() - getFinishByMillis()) + " mills");

		return selectedMove;
	}

	private MachineState getMachineState() {
		return gamer.getCurrentState();
	}

	private long getFinishByMillisForLevel0Node(int branchesLeftToSearch) {
		long currentTimeMillis = System.currentTimeMillis();
		long millisToTimeout = getMillisToTimeout();
		long maxAllowedTimeForBranch = millisToTimeout / branchesLeftToSearch;
		long finishByMillisForBranch = currentTimeMillis + maxAllowedTimeForBranch;
		System.out.print("\nfinishByMillisForLevel0Node = " + finishByMillisForBranch);

		return finishByMillisForBranch;
	}

	private long getFinishByMillis() {
		return finishByMillis;
	}

	private boolean hasTimedOut() {
		return System.currentTimeMillis() > getFinishByMillis();
	}

	private long getMillisToTimeout() {
		return getFinishByMillis() - System.currentTimeMillis();
	}
}
