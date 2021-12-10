package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.interpreter.values.ArrayValue
import io.github.kylemclean.scriptengine.interpreter.values.Value

class ArrayLiteralExpression(private val elements: List<Expression>) : Expression() {
    override fun evaluate(): Value =
        ArrayValue(elements.map {
            it.evaluate()
        })
}