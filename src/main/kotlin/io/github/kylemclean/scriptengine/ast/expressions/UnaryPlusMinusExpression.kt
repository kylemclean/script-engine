package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.interpreter.values.NumericValue
import io.github.kylemclean.scriptengine.interpreter.values.Value

class UnaryPlusMinusExpression(private val expression: Expression, private val isMinus: Boolean) : Expression() {
    override fun evaluate(): Value {
        val value = expression.evaluate()
        require(value is NumericValue) { "value is not a NumericValue" }
        return if (isMinus) value.negation() else value
    }
}