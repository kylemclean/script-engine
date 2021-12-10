package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.interpreter.values.BooleanValue
import io.github.kylemclean.scriptengine.interpreter.values.FloatValue
import io.github.kylemclean.scriptengine.interpreter.values.NumericValue
import io.github.kylemclean.scriptengine.interpreter.values.Value

abstract class InequalityOperatorExpression(lhsExpression: Expression, rhsExpression: Expression) :
    BinaryOperatorExpression(lhsExpression, rhsExpression) {
    abstract fun compare(lhs: Float, rhs: Float): Boolean

    abstract fun compare(lhs: Int, rhs: Int): Boolean

    override fun evaluate(lhs: Value, rhs: Value): Value {
        require(lhs is NumericValue) { "lhs is not a NumericValue" }
        require(rhs is NumericValue) { "rhs is not a NumericValue" }
        return BooleanValue.of(
            if (lhs is FloatValue || rhs is FloatValue) {
                compare(lhs.asFloat(), rhs.asFloat())
            } else {
                compare(lhs.asInt(), rhs.asInt())
            }
        )
    }

    class LessThanExpression(lhsExpression: Expression, rhsExpression: Expression) :
        InequalityOperatorExpression(lhsExpression, rhsExpression) {
        override fun compare(lhs: Float, rhs: Float): Boolean = lhs < rhs

        override fun compare(lhs: Int, rhs: Int): Boolean = lhs < rhs
    }

    class LessThanOrEqualExpression(lhsExpression: Expression, rhsExpression: Expression) :
        InequalityOperatorExpression(lhsExpression, rhsExpression) {
        override fun compare(lhs: Float, rhs: Float): Boolean = lhs <= rhs

        override fun compare(lhs: Int, rhs: Int): Boolean = lhs <= rhs
    }

    class GreaterThanExpression(lhsExpression: Expression, rhsExpression: Expression) :
        InequalityOperatorExpression(lhsExpression, rhsExpression) {
        override fun compare(lhs: Float, rhs: Float): Boolean = lhs > rhs

        override fun compare(lhs: Int, rhs: Int): Boolean = lhs > rhs
    }

    class GreaterThanOrEqualExpression(lhsExpression: Expression, rhsExpression: Expression) :
        InequalityOperatorExpression(lhsExpression, rhsExpression) {
        override fun compare(lhs: Float, rhs: Float): Boolean = lhs >= rhs

        override fun compare(lhs: Int, rhs: Int): Boolean = lhs >= rhs
    }
}