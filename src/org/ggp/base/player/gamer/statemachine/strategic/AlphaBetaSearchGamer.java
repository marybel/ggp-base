package org.ggp.base.player.gamer.statemachine.strategic;

import org.ggp.base.player.strategy.algorithm.AlphaBetaTreeSearchAlgorithm;

public class AlphaBetaSearchGamer extends StrategicGamer {

	public AlphaBetaSearchGamer() {
		super.setSearchAlgorithm(new AlphaBetaTreeSearchAlgorithm(this));
	}
}