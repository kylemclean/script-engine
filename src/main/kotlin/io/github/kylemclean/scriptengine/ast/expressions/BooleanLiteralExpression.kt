package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.interpreter.values.BooleanValue
import io.github.kylemclean.scriptengine.interpreter.values.Value

class BooleanLiteralExpression(boolValue: Boolean) : Expression() {
    private val cachedValue = BooleanValue.of(boolValue)

    override fun evaluate(): Value {
        return cachedValue
    }
}