package org.ggp.base.player.strategy.algorithm;

import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.exceptions.GoalDefinitionException;
import org.ggp.base.util.statemachine.exceptions.MoveDefinitionException;
import org.ggp.base.util.statemachine.exceptions.TransitionDefinitionException;
import org.ggp.base.util.symbol.factory.exceptions.SymbolFormatException;

public interface MinimaxScoreCalculator {

	int calculateMaxScore(MachineState state) throws MoveDefinitionException,
			TransitionDefinitionException, GoalDefinitionException, SymbolFormatException;

	int calculateMinScore(MachineState machineState, Move move) throws MoveDefinitionException,
			TransitionDefinitionException, GoalDefinitionException, SymbolFormatException;

}
