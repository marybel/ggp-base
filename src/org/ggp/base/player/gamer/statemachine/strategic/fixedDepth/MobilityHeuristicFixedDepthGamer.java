package org.ggp.base.player.gamer.statemachine.strategic.fixedDepth;

import org.ggp.base.player.strategy.algorithm.scorecalculator.MobilityHeuristicFunction;

/**
 * Created with IntelliJ IDEA. User: marybel.archer Date: 11/23/13 Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class MobilityHeuristicFixedDepthGamer extends AbstractFixedDepthGamer {

	public MobilityHeuristicFixedDepthGamer(Integer levelLimit) {
		super(new MobilityHeuristicFunction(), levelLimit);
		super.getHeuristicFunction().setGamer(this);
	}

	public MobilityHeuristicFixedDepthGamer() {
		this(3);
	}
}
