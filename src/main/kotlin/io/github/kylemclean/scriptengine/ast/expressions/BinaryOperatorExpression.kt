package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.interpreter.values.Value

abstract class BinaryOperatorExpression(private val lhsExpression: Expression, private val rhsExpression: Expression) :
    Expression() {
    override fun evaluate(): Value {
        return evaluate(lhsExpression.evaluate(), rhsExpression.evaluate())
    }

    protected abstract fun evaluate(lhs: Value, rhs: Value): Value
}