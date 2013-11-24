package org.ggp.base.player.gamer.statemachine.strategic

import org.ggp.base.util.game.Game
import org.ggp.base.util.gdl.grammar.GdlConstant
import org.ggp.base.util.gdl.grammar.GdlPool
import org.ggp.base.util.match.Match
import org.ggp.base.util.statemachine.Move

import spock.lang.Specification

abstract class GamerSpecification extends Specification {

	String ruleSheet
	StrategicGamer gamer
	GdlConstant roleNotInControl = GdlPool.getConstant("black")

	protected StrategicGamer getGamer(GdlConstant roleName) {
		Game theGame = getGame()
		Match theMatch = getMatch(theGame)

		getStrategicGamer(theMatch, roleName)
	}

	private Game getGame() {
		def theKey
		def theName
		def theDescription
		def theRepositoryURL
		def theStylesheet

		Game theGame = new Game(theKey, theName, theDescription, theRepositoryURL, theStylesheet, ruleSheet)

		theGame
	}

	private Match getMatch(Game theGame) {
		Match theMatch = Mock(Match)
		theMatch.game >> theGame

		theMatch
	}

	private StrategicGamer getStrategicGamer(Match theMatch, GdlConstant roleName) {
		gamer.match = theMatch
		gamer.roleName = roleName

		gamer
	}

	def "Role not in control does not have a move to make"() {
		given:

		long timeout = System.currentTimeMillis() + 10000

		StrategicGamer notInControlGamer = getGamer(roleNotInControl)
		notInControlGamer.metaGame(timeout)

		when:
		Move bestMove = notInControlGamer.stateMachineSelectMove(timeout)

		then:
		bestMove
		bestMove.contents == GdlPool.getConstant("noop")
	}
}
