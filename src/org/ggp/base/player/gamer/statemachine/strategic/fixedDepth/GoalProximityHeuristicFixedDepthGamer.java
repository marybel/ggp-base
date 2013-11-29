package org.ggp.base.player.gamer.statemachine.strategic.fixedDepth;

import org.ggp.base.player.strategy.algorithm.scorecalculator.GoalProximityHeuristicFunction;

/**
 * Created with IntelliJ IDEA. User: marybel.archer Date: 11/23/13 Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class GoalProximityHeuristicFixedDepthGamer extends AbstractFixedDepthGamer {

	public GoalProximityHeuristicFixedDepthGamer(Integer levelLimit) {
		super(new GoalProximityHeuristicFunction(), levelLimit);
		super.getHeuristicFunction().setGamer(this);
	}

	public GoalProximityHeuristicFixedDepthGamer() {
		this(3);
	}
}
