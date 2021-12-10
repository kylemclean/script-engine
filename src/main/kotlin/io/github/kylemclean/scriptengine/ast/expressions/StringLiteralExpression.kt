package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.interpreter.values.StringValue
import io.github.kylemclean.scriptengine.interpreter.values.Value

class StringLiteralExpression(string: String) : Expression() {
    private val cachedValue = StringValue(string)

    override fun evaluate(): Value = cachedValue
}