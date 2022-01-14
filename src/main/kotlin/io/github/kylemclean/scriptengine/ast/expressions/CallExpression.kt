package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.interpreter.Arguments
import io.github.kylemclean.scriptengine.interpreter.Interpreter.Companion.interpreter
import io.github.kylemclean.scriptengine.interpreter.values.Value

class CallExpression(private val functionExpression: Expression, private val arguments: List<Expression>) : Expression() {
    override fun evaluate(): Value {
        val value = functionExpression.evaluate()
        val callee = value.callFunction
        require(callee != null) { "lhs is not callable" }
        val argValues: MutableList<Value> = mutableListOf()
        for (argExpr in arguments) {
            argValues.add(argExpr.evaluate())
        }
        return interpreter.call(callee, Arguments(argValues))
    }
}
