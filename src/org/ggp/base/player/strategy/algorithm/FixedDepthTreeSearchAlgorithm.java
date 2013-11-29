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

	public FixedDepthTreeSearchAlgorithm(AbstractFixedDepthGamer gamer) {
		this.gamer = gamer;
	}

	@Override
	public Move getSelectedMove(List<Move> moves, long finishByMillis) throws MoveDefinitionException,
			TransitionDefinitionException, GoalDefinitionException, SymbolFormatException {
		int selectedMoveIndex = 0;
		Move selectedMove = moves.get(selectedMoveIndex);
		int score = 0;

		if (moves.size() > 1) {
			FixedDepthScoreCalculator scoreCalculator = new
					FixedDepthScoreCalculator(gamer, finishByMillis);
			for (int i = 0; i < moves.size(); i++) {
				Move move = moves.get(i);
				if (System.currentTimeMillis() > finishByMillis) {
					break;
				}
				int result = scoreCalculator.calculateMinScore(getMachineState(), move, 0);
				System.out.println("\nmove [" + moves.get(i) + "](" + i +
						"/" + moves.size() + ").{" + result
						+ "}");
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

		System.out.println("\nSelected move [" + selectedMove + "](" + selectedMoveIndex +
				"/" + moves.size() + ") = " + score);
		System.out.println("Overtime " + (System.currentTimeMillis() - finishByMillis) + " mills");

		return selectedMove;
	}

	private MachineState getMachineState() {
		return gamer.getCurrentState();
	}
}
