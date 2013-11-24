package org.ggp.base.player.gamer.statemachine.strategic

import static org.junit.Assert.assertNotNull

import org.ggp.base.game.TTTGameDescription
import org.ggp.base.util.gdl.grammar.GdlConstant
import org.ggp.base.util.gdl.grammar.GdlPool
import org.ggp.base.util.statemachine.Move

class AlphaBetaSearchGamerSpec extends GamerSpecification  {

	def setup() {
		ruleSheet = (new TTTGameDescription()).ruleSheet
		gamer = new AlphaBetaSearchGamer()
	}

	def "Best move for TicTacToe is any move when Minimax unbounded"() {
		given:
		GdlConstant WHITE_PLAYER = GdlPool.getConstant("white")
		long timeout = System.currentTimeMillis() + 10000

		StrategicGamer theGamer = getGamer(WHITE_PLAYER)
		theGamer.metaGame(timeout)

		when:
		Move move = theGamer.stateMachineSelectMove(timeout)

		then:
		move.contents.toString().indexOf("( mark ") > -1
	}
}
