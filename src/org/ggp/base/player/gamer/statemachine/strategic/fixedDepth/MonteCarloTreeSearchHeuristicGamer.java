package org.ggp.base.player.gamer.statemachine.strategic.fixedDepth;

import org.ggp.base.player.strategy.algorithm.scorecalculator.heuristic.MonteCarloTreeSearchHeuristic;

public class MonteCarloTreeSearchHeuristicGamer extends AbstractFixedDepthGamer {

	public MonteCarloTreeSearchHeuristicGamer(Integer levelLimit) {
		super(new MonteCarloTreeSearchHeuristic(), levelLimit);
		super.getHeuristicFunction().setGamer(this);
	}

	public MonteCarloTreeSearchHeuristicGamer() {
		this(3);
	}

}
