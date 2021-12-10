package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.interpreter.values.IntegerValue
import io.github.kylemclean.scriptengine.interpreter.values.Value

class IntegerLiteralExpression(intValue: Int) : Expression() {
    private val cachedValue = IntegerValue(intValue)

    override fun evaluate(): Value {
        return cachedValue
    }
}