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

	protected long getFinishByMillis() {
		return finishByMillis;
	}

	protected List<Move> getMovesToSimulate(Move currentlyExploredMove, Move opponentMove) {
		List<Move> movesToSimulate = new ArrayList<>();
		movesToSimulate.add(currentlyExploredMove);
		movesToSimulate.add(opponentMove);

		return movesToSimulate;
	}

	protected Role getOpponent(Role role) {
		for (Role gameRole : getStateMachine().getRoles()) {
			if (role.getName() != gameRole.getName()) {
				return gameRole;
			}
		}
		throw new RuntimeException("No opponent found for role " + role.getName() + " while doing score calculation");
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

	protected List<Move> getOpponentMoves(MachineState machineState) {
		try {
			Role opponentRole = getOpponent(getGamer().getRole());

			return getStateMachine().getLegalMoves(machineState, opponentRole);
		} catch (Exception e) {
			List<Move> opponentMoves = new ArrayList<Move>();
			opponentMoves.add(new Move(GdlPool.getConstant("NOOP")));

			return opponentMoves;
		}
	}

	protected MachineState getMachineState() {
		return gamer.getCurrentState();
	}
}