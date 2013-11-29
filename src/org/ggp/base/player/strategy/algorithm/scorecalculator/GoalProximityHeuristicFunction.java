package org.ggp.base.player.strategy.algorithm.scorecalculator;

import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Role;
import org.ggp.base.util.statemachine.exceptions.GoalDefinitionException;
import org.ggp.base.util.statemachine.exceptions.MoveDefinitionException;

public class GoalProximityHeuristicFunction extends AbstractScoreCalculator implements HeuristicFunction {

	@Override
	public int getScore(MachineState state, Role playerRole) throws MoveDefinitionException, GoalDefinitionException {

		return getGamer().getStateMachine().getGoal(state, playerRole);
	}
}
