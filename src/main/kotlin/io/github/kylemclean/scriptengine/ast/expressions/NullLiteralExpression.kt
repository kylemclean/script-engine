package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.interpreter.values.NullValue
import io.github.kylemclean.scriptengine.interpreter.values.Value

object NullLiteralExpression : Expression() {
    override fun evaluate(): Value = NullValue
}