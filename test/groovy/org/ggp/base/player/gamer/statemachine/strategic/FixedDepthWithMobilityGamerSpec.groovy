package org.ggp.base.player.gamer.statemachine.strategic

import org.ggp.base.game.AlquerqueGameDescription
import org.ggp.base.player.strategy.algorithm.scorecalculator.MobilityHeuristicFunction
import org.ggp.base.util.gdl.grammar.GdlConstant
import org.ggp.base.util.gdl.grammar.GdlPool
import org.ggp.base.util.statemachine.Move

class FixedDepthWithMobilityGamerSpec extends GamerSpecification {

	def setup() {
		ruleSheet = ( new AlquerqueGameDescription()).ruleSheet
		gamer = new FixedDepthGamer(2)
		roleNotInControl = GdlPool.getConstant("black")
	}

	def "Selected move for Alquerque is one of five when level limit is 100"() {
		given:
		gamer = new FixedDepthGamer(new MobilityHeuristicFunction(),100)
		GdlConstant RED_PLAYER = GdlPool.getConstant("red")
		long timeout = System.currentTimeMillis() + 5000

		FixedDepthGamer theGamer = getGamer(RED_PLAYER)
		theGamer.metaGame(timeout)

		when:
		Move bestMove = theGamer.stateMachineSelectMove(timeout)

		then:
		def selectedMoveStr = bestMove.contents.toString()
		selectedMoveStr.indexOf(" ( move 4 2 3 ") >= -1
	}

	def "Selected move for Alquerque is one of five when level limit is 10"() {
		given:
		gamer = new FixedDepthGamer(new MobilityHeuristicFunction(),9)
		GdlConstant RED_PLAYER = GdlPool.getConstant("red")
		long timeout = System.currentTimeMillis() + 5000

		FixedDepthGamer theGamer = getGamer(RED_PLAYER)
		theGamer.metaGame(timeout)

		when:
		Move bestMove = theGamer.stateMachineSelectMove(timeout)

		then:
		def selectedMoveStr = bestMove.contents.toString()
		selectedMoveStr.indexOf("( move 4") >= -1
	}

	def "Selected move for Alquerque is one of five when level limit is 0"() {
		given:
		gamer = new FixedDepthGamer(new MobilityHeuristicFunction(),0)
		GdlConstant RED_PLAYER = GdlPool.getConstant("red")
		long timeout = System.currentTimeMillis() + 5000

		FixedDepthGamer theGamer = getGamer(RED_PLAYER)
		theGamer.metaGame(timeout)

		when:
		Move bestMove = theGamer.stateMachineSelectMove(timeout)

		then:
		def selectedMoveStr = bestMove.contents.toString()
		selectedMoveStr.indexOf("( mark 4") >= -1
	}
}
