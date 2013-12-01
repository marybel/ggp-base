package org.ggp.base.player.gamer.statemachine.strategic;

import java.util.List;

import org.ggp.base.player.gamer.event.GamerSelectedMoveEvent;
import org.ggp.base.player.gamer.statemachine.sample.SampleGamer;
import org.ggp.base.player.strategy.algorithm.SearchAlgorithm;
import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.Role;
import org.ggp.base.util.statemachine.exceptions.GoalDefinitionException;
import org.ggp.base.util.statemachine.exceptions.MoveDefinitionException;
import org.ggp.base.util.statemachine.exceptions.TransitionDefinitionException;
import org.ggp.base.util.symbol.factory.exceptions.SymbolFormatException;

public abstract class StrategicGamer extends SampleGamer {

	private SearchAlgorithm searchAlgorithm;

	/**
	 * Employs a search algorithm to select the best available move for the
	 * player within the alloted time.
	 * 
	 * @throws SymbolFormatException
	 */
	@Override
	public Move stateMachineSelectMove(long timeout) throws TransitionDefinitionException, MoveDefinitionException,
			GoalDefinitionException {
		long start = System.currentTimeMillis();
		long finishByMillis = timeout - 1000;
		List<Move> moves = getLegalMoves(getCurrentState(), getRole());
		
		Move selection;
		try {
			selection = searchAlgorithm.getSelectedMove(moves, finishByMillis);
		} catch (SymbolFormatException e) {
			throw new RuntimeException(e);
		}

		long stop = System.currentTimeMillis();
		notifyObservers(new GamerSelectedMoveEvent(moves, selection, stop - start));

		return selection;
	}

	private List<Move> getLegalMoves(MachineState state, Role role) throws MoveDefinitionException {
		return getStateMachine().getLegalMoves(state, role);
	}

	public void setSearchAlgorithm(SearchAlgorithm searchAlgorithm) {
		this.searchAlgorithm = searchAlgorithm;
	}

}