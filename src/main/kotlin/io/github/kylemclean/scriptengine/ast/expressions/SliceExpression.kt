package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.interpreter.values.NullValue
import io.github.kylemclean.scriptengine.interpreter.values.SliceValue

class SliceExpression(
    private val start: Expression?,
    private val end: Expression?,
    private val step: Expression?
) : Expression() {
    override fun evaluate(): SliceValue {
        val startValue = start?.evaluate() ?: NullValue
        val endValue = end?.evaluate() ?: NullValue
        val stepValue = step?.evaluate() ?: NullValue
        return SliceValue(startValue, endValue, stepValue)
    }
}