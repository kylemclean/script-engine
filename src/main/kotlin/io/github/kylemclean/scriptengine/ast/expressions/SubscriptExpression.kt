package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.interpreter.values.SubscriptableValue
import io.github.kylemclean.scriptengine.interpreter.values.Value

class SubscriptExpression(private val expression: Expression, private val subscriptExpression: Expression) :
    Expression(), AssignableExpression {

    override fun evaluate(): Value {
        val value = expression.evaluate()
        require(value is SubscriptableValue) { "value is not a SubscriptableValue" }
        val subscriptValue = subscriptExpression.evaluate()
        return value.getAtSubscript(subscriptValue)
    }

    override fun assign(newValue: Value) {
        val value = expression.evaluate()
        require(value is SubscriptableValue) { "value is not a SubscriptableValue" }
        val subscriptValue = subscriptExpression.evaluate()
        value.setAtSubscript(subscriptValue, newValue)
    }
}
