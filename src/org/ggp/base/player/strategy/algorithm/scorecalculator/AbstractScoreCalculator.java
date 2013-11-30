package org.ggp.base.player.strategy.algorithm.scorecalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ggp.base.player.gamer.statemachine.StateMachineGamer;
import org.ggp.base.util.gdl.grammar.GdlConstant;
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

	protected List<Move> getMovesToSimulate(Move currentlyExploredMove, Move opponentMove) {
		List<Move> movesToSimulate = new ArrayList<>();
		movesToSimulate.add(currentlyExploredMove);
		movesToSimulate.add(opponentMove);

		return movesToSimulate;
	}

	protected List<Role> getOpponents(Role role) {
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

	protected Map<Role, List<Move>> getOpponentMoves(MachineState machineState) {
		Map<Role, List<Move>> opponentMoves = new HashMap<Role, List<Move>>();
		try {
			for (Role opponentRole : getOpponents(getGamer().getRole())) {
				opponentMoves.put(opponentRole, getStateMachine().getLegalMoves(machineState, opponentRole));
			}
		} catch (Exception e) {
			opponentMoves.put(new Role(GdlPool.getConstant("noopPlayer")), getNoOppMoveList());
		}
		return opponentMoves;
	}

	private List<Move> getNoOppMoveList() {
		ArrayList<Move> list = new ArrayList<>();
		list.add(new Move(GdlPool.getConstant("NOOP")));
		return list;
	}

	protected MachineState getMachineState() {
		return gamer.getCurrentState();
	}

	protected long getFinishByMillis(int branchesLeftToSearch) {
		long currentTimeMillis = System.currentTimeMillis();
		long maxAllowedTimeForBranch = (getFinishByMillis() - currentTimeMillis) / branchesLeftToSearch;

		return maxAllowedTimeForBranch + currentTimeMillis;
	}
}