package org.ggp.base.player.gamer.statemachine.strategic

import static org.junit.Assert.assertNotNull

import org.ggp.base.game.TTTGameDescription
import org.ggp.base.util.gdl.grammar.GdlConstant
import org.ggp.base.util.gdl.grammar.GdlPool
import org.ggp.base.util.statemachine.Move

class MinimaxGamerSpec extends GamerSpecification {

	def setup() {
		ruleSheet = ( new TTTGameDescription()).ruleSheet
		gamer = new MinimaxGamer()
	}

	def "Selected move for TicTacToe is any move when Minimax unbounded"() {
		given:
		GdlConstant WHITE_PLAYER = GdlPool.getConstant("white")
		long timeout = System.currentTimeMillis() + 10000

		MinimaxGamer theGamer = getGamer(WHITE_PLAYER)
		theGamer.metaGame(timeout)

		when:
		Move bestMove = theGamer.stateMachineSelectMove(timeout)

		then:
		bestMove.contents.toString().indexOf("( mark ") > -1
	}
}
