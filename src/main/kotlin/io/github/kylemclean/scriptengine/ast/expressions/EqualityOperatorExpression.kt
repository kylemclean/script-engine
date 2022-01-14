package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.interpreter.values.BooleanValue
import io.github.kylemclean.scriptengine.interpreter.values.Value

class EqualityOperatorExpression(
    lhsExpression: Expression,
    rhsExpression: Expression,
    private val not: Boolean
) :
    BinaryOperatorExpression(lhsExpression, rhsExpression) {
    override fun evaluate(lhs: Value, rhs: Value): Value {
        val isEqual: Boolean = lhs == rhs
        return BooleanValue.of(if (not) !isEqual else isEqual)
    }
}
