package org.ggp.base.player.gamer.statemachine.strategic;

import org.ggp.base.player.strategy.algorithm.FixedDepthTreeSearchAlgorithm;

/**
 * Created with IntelliJ IDEA. User: marybel.archer Date: 11/23/13 Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class FixedDepthGamer extends StrategicGamer {

	private Integer levelLimit;

	public FixedDepthGamer(Integer levelLimit) {
		super.setSearchAlgorithm(new FixedDepthTreeSearchAlgorithm(this));
		this.levelLimit = levelLimit;
	}

	public FixedDepthGamer() {
		super.setSearchAlgorithm(new FixedDepthTreeSearchAlgorithm(this));
		this.levelLimit = 10;
	}

	public Integer getLevelLimit() {
		return levelLimit;
	}
}
