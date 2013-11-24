package org.ggp.base.player.gamer.statemachine.strategic;

import org.ggp.base.player.strategy.algorithm.FixedDepthTreeSearchAlgorithm;
import org.ggp.base.player.strategy.algorithm.scorecalculator.HeuristicFunction;
import org.ggp.base.player.strategy.algorithm.scorecalculator.MobilityHeuristicFunction;

/**
 * Created with IntelliJ IDEA. User: marybel.archer Date: 11/23/13 Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class FixedDepthGamer extends StrategicGamer {

	private Integer levelLimit = 100;
	private HeuristicFunction heuristicFunction = new MobilityHeuristicFunction();

	public FixedDepthGamer(HeuristicFunction heuristicFunction, Integer levelLimit) {
		super.setSearchAlgorithm(new FixedDepthTreeSearchAlgorithm(this));
		this.heuristicFunction = heuristicFunction;
		this.heuristicFunction.setGamer(this);
		this.levelLimit = levelLimit;
	}

	public FixedDepthGamer(Integer levelLimit) {
		super.setSearchAlgorithm(new FixedDepthTreeSearchAlgorithm(this));
		this.levelLimit = levelLimit;
	}

	public FixedDepthGamer() {
		super.setSearchAlgorithm(new FixedDepthTreeSearchAlgorithm(this));
	}

	public HeuristicFunction getHeuristicFunction() {
		return heuristicFunction;
	}

	public Integer getLevelLimit() {
		return levelLimit;
	}
}
