package org.ggp.base.player.strategy.algorithm.scorecalculator.heuristic;

import org.ggp.base.player.gamer.statemachine.StateMachineGamer;
import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Role;

public class MinimumScoreHeuristic implements HeuristicFunction {

	@Override
	public int getScore(MachineState state, Role playerRole) {
		return 0;
	}

	@Override
	public void setGamer(StateMachineGamer stateMachineGamer) {
		// TODO Auto-generated method stub

	}

}
