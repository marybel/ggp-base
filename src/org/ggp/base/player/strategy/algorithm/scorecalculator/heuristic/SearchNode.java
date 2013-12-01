package org.ggp.base.player.strategy.algorithm.scorecalculator.heuristic;

import java.util.ArrayList;
import java.util.List;

import org.ggp.base.util.statemachine.MachineState;

public class SearchNode implements Node {
	private MachineState machineState;
	private Node parent;
	private int visits;
	private int utility;
	private List<Node> children;

	public SearchNode(MachineState nodeState, Node nodeParent, int visits, int utility, int seq) {
		this.machineState = nodeState;
		this.parent = nodeParent;
		this.visits = visits;
		this.utility = utility;
		this.children = new ArrayList<>();
	}

	public String getId() {
		return "node" + machineState.getContents();
	}

	@Override
	public int getVisits() {
		return this.visits;
	}

	@Override
	public List<Node> getChildren() {
		return children;
	}

	@Override
	public Node getParent() {
		return this.parent;
	}

	@Override
	public int getUtility() {
		return this.utility;
	}

	@Override
	public MachineState getMachineState() {
		return machineState;
	}

	@Override
	public void increaseVisits(int i) {
		this.visits += i;
	}

	@Override
	public void increaseUtility(int score) {
		this.utility += score;
	}

	@Override
	public boolean hasParent() {
		return parent != null;
	}

	@Override
	public String toString() {
		return "SearchNode " + machineState.getContents().hashCode()
				+ "[visits=" + visits + ", utility=" + utility + "]";
	}
}
