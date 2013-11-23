package org.ggp.base.player.strategy.algorithm;

import java.util.ArrayList;
import java.util.List;

import org.ggp.base.player.gamer.statemachine.StateMachineGamer;
import org.ggp.base.util.gdl.grammar.GdlPool;
import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.exceptions.GoalDefinitionException;
import org.ggp.base.util.statemachine.exceptions.MoveDefinitionException;
import org.ggp.base.util.statemachine.exceptions.TransitionDefinitionException;
import org.ggp.base.util.symbol.factory.exceptions.SymbolFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlphaBetaTreeSearchAlgorithm implements SearchAlgorithm {
	private static Logger LOGGER = LoggerFactory.getLogger(AlphaBetaTreeSearchAlgorithm.class);
	private StateMachineGamer gamer;

	public AlphaBetaTreeSearchAlgorithm(StateMachineGamer gamer) {
		this.gamer = gamer;
	}

	@Override
	public Move getSelectedMove(List<Move> moves, long finishByMillis) throws MoveDefinitionException,
			TransitionDefinitionException, GoalDefinitionException, SymbolFormatException {
		// given
		Move bestMoveFound = moves.get(0);
		// when
		if (moves.size() > 1) {
			AlphaBetaScoreCalculator scoreCalculator = new AlphaBetaScoreCalculator(gamer, finishByMillis);

			int score = 0;
			for (Move move : moves) {
				if (System.currentTimeMillis() > finishByMillis) {
					break;
				}
				List<Move> movesToSimulate = getMovesToSimulate(move, new Move(GdlPool.getConstant("noop")));
				MachineState newMachineState = gamer.getStateMachine().getNextState(getMachineState(), movesToSimulate);

				int result = scoreCalculator.calculateMaxScore(newMachineState, 0, 100);
				if (result == 100) {
					return move;
				}
				if (result > score) {
					score = result;
					bestMoveFound = move;
				}
			}
		}
		// then
		LOGGER.debug("Overtime was {} millis", System.currentTimeMillis() - finishByMillis);
		return bestMoveFound;
	}

	private MachineState getMachineState() {
		return gamer.getCurrentState();
	}

	private List<Move> getMovesToSimulate(Move currentlyExploredMove, Move opponentMove) {
		List<Move> movesToSimulate = new ArrayList<>();
		movesToSimulate.add(currentlyExploredMove);
		movesToSimulate.add(opponentMove);

		return movesToSimulate;
	}
}
