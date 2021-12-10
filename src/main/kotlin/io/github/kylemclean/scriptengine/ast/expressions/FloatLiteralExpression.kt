package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.interpreter.values.FloatValue
import io.github.kylemclean.scriptengine.interpreter.values.Value

class FloatLiteralExpression(floatValue: Float) : Expression() {
    private val cachedValue = FloatValue(floatValue)

    override fun evaluate(): Value {
        return cachedValue
    }
}