package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.interpreter.Arguments
import io.github.kylemclean.scriptengine.interpreter.Interpreter.Companion.interpreter
import io.github.kylemclean.scriptengine.interpreter.values.Value

class ContainsExpression(private val needle: Expression, private val haystack: Expression) : Expression() {
    override fun evaluate(): Value =
        interpreter.call(
            interpreter.containsFunction,
            Arguments(listOf(needle.evaluate(), haystack.evaluate()))
        )
}
