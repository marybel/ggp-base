package org.ggp.base.player.gamer.statemachine.strategic

import org.ggp.base.game.AlquerqueGameDescription
import org.ggp.base.player.gamer.statemachine.strategic.fixedDepth.AbstractFixedDepthGamer
import org.ggp.base.player.gamer.statemachine.strategic.fixedDepth.GoalProximityHeuristicFixedDepthGamer
import org.ggp.base.util.gdl.grammar.GdlConstant
import org.ggp.base.util.gdl.grammar.GdlPool
import org.ggp.base.util.statemachine.Move

class FixedDepthWithGoalProximityGamerSpec extends GamerSpecification {

	def setup() {
		ruleSheet = ( new AlquerqueGameDescription()).ruleSheet
		gamer = new GoalProximityHeuristicFixedDepthGamer(2)
		roleNotInControl = GdlPool.getConstant("black")
	}

	def "Selected move for Alquerque is one of five when level limit is 5"() {
		given:
		gamer = new GoalProximityHeuristicFixedDepthGamer(7)
		GdlConstant RED_PLAYER = GdlPool.getConstant("red")
		long timeout = System.currentTimeMillis() + 10000

		AbstractFixedDepthGamer theGamer = getGamer(RED_PLAYER)
		theGamer.metaGame(timeout)

		when:
		Move bestMove = theGamer.stateMachineSelectMove(timeout)

		then:
		def selectedMoveStr = bestMove.contents.toString()
		selectedMoveStr.indexOf(" ( move 4 2 3 ") >= -1
	}

	def "Selected move for Alquerque is one of five when level limit is 4"() {
		given:
		gamer = new GoalProximityHeuristicFixedDepthGamer(5)
		GdlConstant RED_PLAYER = GdlPool.getConstant("red")
		long timeout = System.currentTimeMillis() + 10000

		AbstractFixedDepthGamer theGamer = getGamer(RED_PLAYER)
		theGamer.metaGame(timeout)

		when:
		Move bestMove = theGamer.stateMachineSelectMove(timeout)

		then:
		def selectedMoveStr = bestMove.contents.toString()
		selectedMoveStr.indexOf("( move 4") >= -1
	}

	def "Selected move for Alquerque is one of five when level limit is 3"() {
		given:
		gamer = new GoalProximityHeuristicFixedDepthGamer(3)
		GdlConstant RED_PLAYER = GdlPool.getConstant("red")
		long timeout = System.currentTimeMillis() + 10000

		AbstractFixedDepthGamer theGamer = getGamer(RED_PLAYER)
		theGamer.metaGame(timeout)

		when:
		Move bestMove = theGamer.stateMachineSelectMove(timeout)

		then:
		def selectedMoveStr = bestMove.contents.toString()
		selectedMoveStr.indexOf("( mark 4") >= -1
	}
}
