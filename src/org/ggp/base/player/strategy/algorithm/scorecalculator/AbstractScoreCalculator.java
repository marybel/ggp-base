package org.ggp.base.player.strategy.algorithm.scorecalculator;

import java.util.ArrayList;
import java.util.List;

import org.ggp.base.player.gamer.statemachine.StateMachineGamer;
import org.ggp.base.util.gdl.grammar.GdlPool;
import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.Role;
import org.ggp.base.util.statemachine.StateMachine;
import org.ggp.base.util.statemachine.exceptions.GoalDefinitionException;
import org.ggp.base.util.statemachine.exceptions.MoveDefinitionException;
import org.ggp.base.util.statemachine.exceptions.TransitionDefinitionException;

public class AbstractScoreCalculator {
	public static final int MAX_GAME_SCORE = 100;
	public static final int MIN_GAME_SCORE = 0;
	private StateMachineGamer gamer;
	private long finishByMillis;

	public StateMachineGamer getGamer() {
		return gamer;
	}

	private long getFinishByMillis() {
		return finishByMillis;
	}

	protected List<Move> getMovesToSimulate(Move currentlyExploredMove, MachineState currentMachineState) {
		try {
			return getStateMachine().getRandomJointMove(currentMachineState, gamer.getRole(), currentlyExploredMove);
		} catch (MoveDefinitionException e) {
			List<Move> opponentNoops = new ArrayList<Move>(getStateMachine().getRoles().size());
			opponentNoops.add(currentlyExploredMove);
			for (int i = 1; i < opponentNoops.size(); i++) {
				opponentNoops.add(new Move(GdlPool.getConstant("NOOP")));
			}

			return opponentNoops;
		}
	}

	protected StateMachine getStateMachine() {
		return gamer.getStateMachine();
	}

	protected void setFinishByMillis(long finishByMillis) {
		this.finishByMillis = finishByMillis;

	}

	public void setGamer(StateMachineGamer gamer) {
		this.gamer = gamer;
	}

	protected MachineState getMachineState() {
		return gamer.getCurrentState();
	}

	private long getFinishByMillis(int branchesLeftToSearch) {
		long currentTimeMillis = System.currentTimeMillis();
		long maxAllowedTimeForBranch = (getFinishByMillis() - currentTimeMillis) / branchesLeftToSearch;
		long finishByMillisForBranch = currentTimeMillis + maxAllowedTimeForBranch;
		System.out.print("\nfinishByMillisForBranch = " + finishByMillisForBranch);

		return maxAllowedTimeForBranch + currentTimeMillis;
	}

	protected boolean hasTimedout(int branchesLeftToSearch) {

		return System.currentTimeMillis() > getFinishByMillis(branchesLeftToSearch);
	}

	protected int getGoal(MachineState state, Role playerRole) {
		try {
			return getStateMachine().getGoal(state, playerRole);
		} catch (GoalDefinitionException e) {
			System.out.println(e.getMessage());
			return 0;
		}
	}

	protected Role getGamerRole() {
		return getGamer().getRole();
	}

	protected MachineState simulateMove(MachineState machineState, Move playerMove)
			throws TransitionDefinitionException {
		List<Move> movesToSimulate = getMovesToSimulate(playerMove, machineState);

		return getStateMachine().getNextState(machineState, movesToSimulate);
	}
}