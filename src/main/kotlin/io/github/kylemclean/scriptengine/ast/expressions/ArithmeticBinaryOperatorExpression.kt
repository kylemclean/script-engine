package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.interpreter.values.FloatValue
import io.github.kylemclean.scriptengine.interpreter.values.IntegerValue
import io.github.kylemclean.scriptengine.interpreter.values.NumericValue
import io.github.kylemclean.scriptengine.interpreter.values.Value
import kotlin.math.floor
import kotlin.math.pow

abstract class ArithmeticBinaryOperatorExpression(lhsExpression: Expression, rhsExpression: Expression) :
    BinaryOperatorExpression(lhsExpression, rhsExpression, ) {
    override fun evaluate(lhs: Value, rhs: Value): NumericValue {
        require(lhs is NumericValue) { "lhs is not a NumericValue" }
        require(rhs is NumericValue) { "rhs is not a NumericValue" }
        return if (lhs is FloatValue || rhs is FloatValue) {
            floatValue(lhs, rhs)
        } else {
            intValue(lhs, rhs)
        }
    }

    protected abstract fun floatValue(lhs: NumericValue, rhs: NumericValue): NumericValue

    protected abstract fun intValue(lhs: NumericValue, rhs: NumericValue): NumericValue

    class AdditionExpression(lhsExpression: Expression, rhsExpression: Expression) :
        ArithmeticBinaryOperatorExpression(lhsExpression, rhsExpression) {
        override fun floatValue(lhs: NumericValue, rhs: NumericValue): FloatValue =
            FloatValue(lhs.asFloat() + rhs.asFloat())

        override fun intValue(lhs: NumericValue, rhs: NumericValue): IntegerValue =
            IntegerValue(lhs.asInt() + rhs.asInt())
    }

    class SubtractionExpression(lhsExpression: Expression, rhsExpression: Expression) :
        ArithmeticBinaryOperatorExpression(lhsExpression, rhsExpression) {
        override fun floatValue(lhs: NumericValue, rhs: NumericValue): FloatValue =
            FloatValue(lhs.asFloat() - rhs.asFloat())

        override fun intValue(lhs: NumericValue, rhs: NumericValue): IntegerValue =
            IntegerValue(lhs.asInt() - rhs.asInt())
    }

    class MultiplicationExpression(lhsExpression: Expression, rhsExpression: Expression) :
        ArithmeticBinaryOperatorExpression(lhsExpression, rhsExpression) {
        override fun floatValue(lhs: NumericValue, rhs: NumericValue): FloatValue =
            FloatValue(lhs.asFloat() * rhs.asFloat())

        override fun intValue(lhs: NumericValue, rhs: NumericValue): IntegerValue =
            IntegerValue(lhs.asInt() * rhs.asInt())
    }

    class FloatDivisionExpression(lhsExpression: Expression, rhsExpression: Expression) :
        ArithmeticBinaryOperatorExpression(lhsExpression, rhsExpression) {
        override fun floatValue(lhs: NumericValue, rhs: NumericValue): FloatValue =
            FloatValue(lhs.asFloat() / rhs.asFloat())

        override fun intValue(lhs: NumericValue, rhs: NumericValue): FloatValue =
            FloatValue(lhs.asFloat() / rhs.asFloat())
    }

    class IntegerDivisionExpression(lhsExpression: Expression, rhsExpression: Expression) :
        ArithmeticBinaryOperatorExpression(lhsExpression, rhsExpression) {
        override fun floatValue(lhs: NumericValue, rhs: NumericValue): FloatValue =
            FloatValue(floor(lhs.asFloat() / rhs.asFloat()))

        override fun intValue(lhs: NumericValue, rhs: NumericValue): IntegerValue =
            IntegerValue(lhs.asInt() / rhs.asInt())
    }

    class RemainderExpression(lhsExpression: Expression, rhsExpression: Expression) :
        ArithmeticBinaryOperatorExpression(lhsExpression, rhsExpression) {
        override fun floatValue(lhs: NumericValue, rhs: NumericValue): FloatValue =
            FloatValue(lhs.asFloat() % rhs.asFloat())

        override fun intValue(lhs: NumericValue, rhs: NumericValue): IntegerValue =
            IntegerValue(lhs.asInt() % rhs.asInt())
    }

    class ExponentiationExpression(lhsExpression: Expression, rhsExpression: Expression) :
        ArithmeticBinaryOperatorExpression(lhsExpression, rhsExpression) {
        override fun floatValue(lhs: NumericValue, rhs: NumericValue): FloatValue =
            FloatValue(lhs.asFloat().pow(rhs.asFloat()))

        override fun intValue(lhs: NumericValue, rhs: NumericValue): IntegerValue =
            IntegerValue(lhs.asFloat().pow(rhs.asFloat()).toInt())
    }
}