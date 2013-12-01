package org.ggp.base.player.gamer.statemachine.strategic.fixedDepth;

import org.ggp.base.player.gamer.statemachine.strategic.StrategicGamer;
import org.ggp.base.player.strategy.algorithm.FixedDepthTreeSearchAlgorithm;
import org.ggp.base.player.strategy.algorithm.scorecalculator.heuristic.HeuristicFunction;

/**
 * Created with IntelliJ IDEA. User: marybel.archer Date: 11/23/13 Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractFixedDepthGamer extends StrategicGamer {

	private Integer levelLimit = 0;
	private HeuristicFunction heuristicFunction;

	public AbstractFixedDepthGamer(HeuristicFunction heuristicFunction, Integer levelLimit) {
		super.setSearchAlgorithm(new FixedDepthTreeSearchAlgorithm(this));
		this.heuristicFunction = heuristicFunction;
		this.heuristicFunction.setGamer(this);
		this.levelLimit = levelLimit;
	}

	public HeuristicFunction getHeuristicFunction() {
		return heuristicFunction;
	}

	public Integer getLevelLimit() {
		return levelLimit;
	}

	public void setLevelLimit(Integer levelLimit) {
		this.levelLimit = levelLimit;
		System.out.print("\nL = " + this.levelLimit);
	}
}
