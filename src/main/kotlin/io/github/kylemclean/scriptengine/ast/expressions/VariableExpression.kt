package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.interpreter.Interpreter.Companion.interpreter
import io.github.kylemclean.scriptengine.interpreter.values.Value

class VariableExpression(private val identifier: String) : Expression(), AssignableExpression {
    override fun evaluate(): Value {
        val symbol = interpreter.resolveSymbol(identifier)
        require(symbol != null) { "symbol \"$identifier\" is not declared" }
        return symbol.value
    }

    override fun assign(newValue: Value) {
        val symbol = interpreter.resolveSymbol(identifier)
        require(symbol != null) { "symbol \"$identifier\" is not declared" }
        symbol.value = newValue
    }

}