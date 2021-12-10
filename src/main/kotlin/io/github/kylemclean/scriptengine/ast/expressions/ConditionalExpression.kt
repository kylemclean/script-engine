package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.interpreter.values.BooleanValue
import io.github.kylemclean.scriptengine.interpreter.values.Value

class ConditionalExpression(
    private val condition: Expression,
    private val consequent: Expression,
    private val alternate: Expression
) : Expression() {
    override fun evaluate(): Value {
        val result = condition.evaluate()
        check(result is BooleanValue) { "result is not a BooleanValue" }
        return (if (result.value) consequent else alternate).evaluate()
    }
}