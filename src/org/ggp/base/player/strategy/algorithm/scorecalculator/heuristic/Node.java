package org.ggp.base.player.strategy.algorithm.scorecalculator.heuristic;

import java.util.List;

import org.ggp.base.util.statemachine.MachineState;

public interface Node {

	int getVisits();

	List<Node> getChildren();

	Node getParent();

	int getUtility();

	MachineState getMachineState();

	void increaseVisits(int i);

	void increaseUtility(int score);

	boolean hasParent();

}
