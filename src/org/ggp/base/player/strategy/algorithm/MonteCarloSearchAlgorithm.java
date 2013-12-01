package org.ggp.base.player.strategy.algorithm;

import java.util.List;

import org.ggp.base.player.gamer.statemachine.StateMachineGamer;
import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.Role;
import org.ggp.base.util.statemachine.StateMachine;

public class MonteCarloSearchAlgorithm implements SearchAlgorithm {
	private int[] depth = new int[1];
	private StateMachineGamer gamer;

	public MonteCarloSearchAlgorithm(StateMachineGamer gamer) {
		this.gamer = gamer;

	}

	@Override
	public Move getSelectedMove(List<Move> moves, long finishByMillis) {
		if (moves.size() > 1) {
			int highestScoredIndex = getHighestScoredIndex(moves, finishByMillis);

			return moves.get(highestScoredIndex);
		}

		return moves.get(0);
	}

	private int getHighestScoredIndex(List<Move> moves, long finishByMillis) {
		double[] moveExpectedPoints = getMoveExpectedPoints(moves, finishByMillis);

		return getHighestScoredIndex(moves, moveExpectedPoints);
	}

	/**
	 * Perform depth charges for each candidate move, and keep track of the
	 * total score and total attempts accumulated for each move.
	 * 
	 * @param moves
	 * @param finishByMillis
	 * 
	 * @return
	 */
	private double[] getMoveExpectedPoints(List<Move> moves,
			long finishByMillis) {

		int[] moveTotalPoints = new int[moves.size()];
		int[] moveTotalAttempts = new int[moves.size()];

		for (int i = 0; true; i = (i + 1) % moves.size()) {
			if (hasTimedOut(finishByMillis)) {
				break;
			}
			int theScore = performDepthChargeFromMove(moves.get(i));
			moveTotalPoints[i] += theScore;
			moveTotalAttempts[i] += 1;
		}

		double[] moveExpectedPoints = getMoveExpectedPoints(moves, moveTotalPoints, moveTotalAttempts);
		System.out.println("moves = {" + moves + "}");
		System.out.println("moveExpectedPoints = {" + getMoveExpectedPointsStr(moveExpectedPoints) + "}");

		return moveExpectedPoints;
	}

	private boolean hasTimedOut(long finishByMillis) {
		return System.currentTimeMillis() > finishByMillis;
	}

	private int performDepthChargeFromMove(Move move) {
		try {
			Role role = gamer.getRole();
			MachineState randomNewState = getStateMachine().getRandomNextState(getCurrentMachineState(), role, move);
			MachineState terminalFromRandomState = getStateMachine().performDepthCharge(randomNewState, depth);

			return getStateMachine().getGoal(terminalFromRandomState, gamer.getRole());
		} catch (Exception e) {
			e.printStackTrace();

			return 0;
		}
	}

	private MachineState getCurrentMachineState() {
		return gamer.getCurrentState();
	}

	private StateMachine getStateMachine() {
		return gamer.getStateMachine();
	}

	private int getHighestScoredIndex(List<Move> moves, double[] moveExpectedPoints) {
		int highestScoredIndex = 0;
		double highestMoveScore = moveExpectedPoints[0];

		for (int i = 1; i < moves.size(); i++) {
			if (moveExpectedPoints[i] > highestMoveScore) {
				highestMoveScore = moveExpectedPoints[i];
				highestScoredIndex = i;
			}
		}

		return highestScoredIndex;
	}

	private double[] getMoveExpectedPoints(List<Move> moves, int[] moveTotalPoints, int[] moveTotalAttempts) {
		double[] moveExpectedPoints = new double[moves.size()];
		for (int i = 0; i < moves.size(); i++) {
			moveExpectedPoints[i] = (double) moveTotalPoints[i] / moveTotalAttempts[i];
		}

		return moveExpectedPoints;
	}
	
	private String getMoveExpectedPointsStr(double[] moveExpectedPoints) {
		StringBuffer sBuffer = new StringBuffer("[");
		for (int i = 0; i < moveExpectedPoints.length; i++) {
			sBuffer.append(moveExpectedPoints[i]).append(",");
		}

		return sBuffer.append("]").toString();
	}
}
