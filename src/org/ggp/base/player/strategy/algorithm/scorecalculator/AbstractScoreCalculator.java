package org.ggp.base.player.strategy.algorithm.scorecalculator;

import java.util.ArrayList;
import java.util.List;

import org.ggp.base.player.gamer.statemachine.StateMachineGamer;
import org.ggp.base.util.gdl.grammar.GdlPool;
import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.Role;
import org.ggp.base.util.statemachine.StateMachine;

public class AbstractScoreCalculator {

	private StateMachineGamer gamer;
	private long finishByMillis;

	public StateMachineGamer getGamer() {
		return gamer;
	}

	private long getFinishByMillis() {
		return finishByMillis;
	}

	protected List<Move> getMovesToSimulate(Move currentlyExploredMove, List<Move> opponentsMove) {
		List<Move> movesToSimulate = new ArrayList<>();
		movesToSimulate.add(currentlyExploredMove);
		movesToSimulate.addAll(opponentsMove);

		return movesToSimulate;
	}

	private List<Role> getOpponents(Role role) {
		List<Role> opponentRoles = new ArrayList<>();
		for (Role gameRole : getStateMachine().getRoles()) {
			if (role.getName() != gameRole.getName()) {
				opponentRoles.add(gameRole);

			}
		}
		if (opponentRoles.isEmpty()) {
			throw new RuntimeException("No opponent found for role " + role.getName()
					+ " while doing score calculation");
		}
		return opponentRoles;
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

	protected List<Move> getOpponenstMove(MachineState machineState) {
		List<Move> opponentMoves = new ArrayList<>();
		try {
			for (Role opponentRole : getOpponents(getGamer().getRole())) {
				opponentMoves.add(getStateMachine().getLegalMoves(machineState, opponentRole).get(0));
			}
		} catch (Exception e) {
			opponentMoves.add(new Move(GdlPool.getConstant("NOOP")));
		}

		return opponentMoves;
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
}