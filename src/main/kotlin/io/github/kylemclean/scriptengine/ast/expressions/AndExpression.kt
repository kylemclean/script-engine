package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.interpreter.values.BooleanValue
import io.github.kylemclean.scriptengine.interpreter.values.Value

class AndExpression(lhsExpression: Expression, rhsExpression: Expression) :
    BinaryOperatorExpression(lhsExpression, rhsExpression) {
    override fun evaluate(lhs: Value, rhs: Value): Value {
        require(lhs is BooleanValue) { "lhs is not a BooleanValue" }
        require(rhs is BooleanValue) { "rhs is not a BooleanValue" }
        return BooleanValue.of(lhs.value && rhs.value)
    }
}