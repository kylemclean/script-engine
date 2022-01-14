package io.github.kylemclean.scriptengine.ast.statements

import io.github.kylemclean.scriptengine.ast.expressions.Expression
import io.github.kylemclean.scriptengine.interpreter.Interpreter.Companion.interpreter
import io.github.kylemclean.scriptengine.interpreter.values.BooleanValue

class WhileStatement(private val condition: Expression, private val body: BlockStatement) : Statement() {
    private fun isConditionTrue(): Boolean {
        val conditionResult = condition.evaluate()
        check(conditionResult is BooleanValue) { "condition is not a BooleanValue" }
        return conditionResult.value
    }

    override fun execute() {
        while (isConditionTrue()) {
            body.execute()
            if (interpreter.shouldContinue) {
                interpreter.shouldContinue = false
            } else if (interpreter.shouldBreak) {
                interpreter.shouldBreak = false
                break
            }
        }
    }
}
