package org.ggp.base.player.gamer.statemachine.strategic

import static org.junit.Assert.assertNotNull

import org.ggp.base.game.TTTGameDescription
import org.ggp.base.util.game.Game
import org.ggp.base.util.gdl.grammar.GdlConstant
import org.ggp.base.util.gdl.grammar.GdlPool
import org.ggp.base.util.match.Match
import org.ggp.base.util.statemachine.Move

import spock.lang.Specification

class AlphaBetaSearchGamerSpec extends Specification {
	private tttGameDescription = new TTTGameDescription()
	private String theRulesheet = tttGameDescription.ruleSheet

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

	def "Role not in control does not have a move to make"() {
		given:
		GdlConstant BLACK_PLAYER = GdlPool.getConstant("black")
		long timeout = System.currentTimeMillis() + 10000

		StrategicGamer theGamer = getGamer(BLACK_PLAYER)
		theGamer.metaGame(timeout)

		when:
		Move bestMove = theGamer.stateMachineSelectMove(timeout)

		then:
		bestMove
		bestMove.contents == GdlPool.getConstant("noop")
	}

	private StrategicGamer getGamer(GdlConstant WHITE_PLAYER) {
		Game theGame = getGame()
		Match theMatch = getMatch(theGame)

		getStrategicGamer(theMatch, WHITE_PLAYER)
	}

	private Game getGame() {
		def theKey
		def theName
		def theDescription
		def theRepositoryURL
		def theStylesheet

		Game theGame = new Game(theKey, theName, theDescription, theRepositoryURL, theStylesheet, theRulesheet)

		theGame
	}

	private Match getMatch(Game theGame) {
		Match theMatch = Mock(Match)
		theMatch.game >> theGame

		theMatch
	}

	private StrategicGamer getStrategicGamer(Match theMatch, GdlConstant roleName) {
		StrategicGamer gamer = new AlphaBetaSearchGamer()
		gamer.match = theMatch
		gamer.roleName = roleName

		gamer
	}
}
