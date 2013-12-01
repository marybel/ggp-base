package org.ggp.base.player.strategy.algorithm.scorecalculator.heuristic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ggp.base.player.strategy.algorithm.scorecalculator.AbstractScoreCalculator;
import org.ggp.base.util.gdl.grammar.GdlPool;
import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.Role;
import org.ggp.base.util.statemachine.exceptions.GoalDefinitionException;
import org.ggp.base.util.statemachine.exceptions.MoveDefinitionException;

public class MonteCarloTreeSearchHeuristic extends AbstractScoreCalculator implements HeuristicFunction {

	private static final int PARENT_SEQ = 0;
	private Map<String, SearchNode> alreadyVisitedNodes = new HashMap<>();

	@Override
	public int getScore(MachineState state, Role playerRole) throws MoveDefinitionException, GoalDefinitionException {
		SearchNode rootNode = getSearchNode(state, null, PARENT_SEQ);
		Node selectedNode = getSelectedNode(rootNode);
		if (!getStateMachine().isTerminal(selectedNode.getMachineState())) {
			Node newChildNode = expand(selectedNode);
			int score = simulate(newChildNode);
			backpropagate(newChildNode, score);
		}

		int selectScore = selectedNode.getUtility() / selectedNode.getVisits();
		System.out.println("MCTS =>" + selectScore);
		return selectScore;
	}

	private SearchNode getSearchNode(MachineState state, Node parent, int childSequence) {
		String nodeId = state.hashCode() + "-" + childSequence;
		SearchNode alreadyVisitedNode = alreadyVisitedNodes.get(nodeId);
		if (alreadyVisitedNode != null) {
			System.out.println("Existing node returned " + nodeId);

			return alreadyVisitedNode;
		}

		return createSearchNode(state, parent, childSequence);
	}

	private SearchNode createSearchNode(MachineState state, Node parent, int childSequence) {
		SearchNode rootNode = new SearchNode(state, parent, 0, 0, childSequence);
		alreadyVisitedNodes.put(rootNode.getId(), rootNode);

		return rootNode;
	}

	private boolean backpropagate(Node node, int score) {
		node.increaseVisits(1);
		node.increaseUtility(score);
		if (node.hasParent()) {
			backpropagate(node.getParent(), score);
		}
		return true;
	}

	private int simulate(Node newChildNode) {
		MachineState nodeState = newChildNode.getMachineState();
		if (getStateMachine().isTerminal(nodeState)) {

			return getGoal(newChildNode.getMachineState(), getGamerRole());
		}

		List<Move> legalMoves = getLegalMoves(newChildNode.getMachineState(), getGamerRole());
		int i = 0;
		for (Move playerMove : legalMoves) {
			MachineState newMachineState = simulateRandomJointMove(newChildNode.getMachineState(), playerMove);

			return simulate(getSearchNode(newMachineState, newChildNode, ++i));
		}
		return 0;
	}

	private Node getSelectedNode(Node node) {
		if (!hasBeenVisited(node)) {
			return node;
		}
		List<Node> nodeChildren = node.getChildren();
		for (int i = 0; i < nodeChildren.size(); i++) {
			Node childNode = nodeChildren.get(i);
			if (!hasBeenVisited(childNode)) {
				return childNode;
			}
		}
		Node treeNodeSelected = getTreeNodeSelected(node);

		return getSelectedNode(treeNodeSelected);
	}

	public Node expand(Node node) {
		List<Move> legalMoves = getLegalMoves(node.getMachineState(), getGamerRole());
		Node lastNewChild = null;
		for (int i = 0; i < legalMoves.size() && i < 2; i++) {
			MachineState newState = simulateRandomJointMove(node.getMachineState(), legalMoves.get(i));

			Node newNode = getSearchNode(newState, node, i + 1);
			List<Node> nodeChildren = node.getChildren();
			nodeChildren.add(newNode);
			lastNewChild = newNode;
		}

		return lastNewChild;
	}

	private Node getTreeNodeSelected(Node node) {
		int score = 0;
		Node selectedNode = node;

		List<Node> nodeChildren = node.getChildren();
		for (int i = 0; i < nodeChildren.size(); i++) {
			Node childNode = nodeChildren.get(i);
			int childSelectScore = getSelectScore(childNode);
			if (childSelectScore > score) {
				score = childSelectScore;
				selectedNode = childNode;
			}
		}

		return selectedNode;
	}

	private boolean hasBeenVisited(Node node) {
		return node.getVisits() > 0;
	}

	private int getSelectScore(Node node) {
		Node parent = node.getParent();
		int parentVisits = 1;
		if (parent != null) {
			parentVisits = parent.getVisits();
		}
		return (int) (node.getUtility() + Math.sqrt(2 * Math.log(parentVisits) / node.getVisits()));
	}

	private List<Move> getLegalMoves(MachineState nodeState, Role gamerRole) {
		try {

			return getStateMachine().getLegalMoves(nodeState, gamerRole);
		} catch (MoveDefinitionException e) {
			e.printStackTrace();
			List<Move> legalMoves = new ArrayList<>();
			legalMoves.add(new Move(GdlPool.getConstant("NOOP")));

			return legalMoves;
		}
	}
}
