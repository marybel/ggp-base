package org.ggp.base.player.strategy.algorithm.scorecalculator.heuristic;

import org.ggp.base.player.gamer.statemachine.StateMachineGamer;
import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Role;
import org.ggp.base.util.statemachine.exceptions.GoalDefinitionException;
import org.ggp.base.util.statemachine.exceptions.MoveDefinitionException;

public interface HeuristicFunction {

	int getScore(MachineState state, Role playerRole) throws MoveDefinitionException, GoalDefinitionException;

	void setGamer(StateMachineGamer stateMachineGamer);

}
