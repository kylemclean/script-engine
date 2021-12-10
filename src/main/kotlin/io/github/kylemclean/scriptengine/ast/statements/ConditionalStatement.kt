package io.github.kylemclean.scriptengine.ast.statements

import io.github.kylemclean.scriptengine.ast.expressions.Expression
import io.github.kylemclean.scriptengine.interpreter.values.BooleanValue

class ConditionalStatement(
    private val condition: Expression,
    private val consequent: Statement,
    private val alternate: Statement?
) : Statement() {
    override fun execute() {
        val conditionResult = condition.evaluate()
        check(conditionResult is BooleanValue) { "condition is not a BooleanValue" }
        if (conditionResult.value) {
            consequent.execute()
        } else alternate?.execute()
    }
}