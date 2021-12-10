package io.github.kylemclean.scriptengine.ast.expressions

import io.github.kylemclean.scriptengine.interpreter.values.Value

class AccessExpression(private val lhsExpression: Expression, private val memberIdentifier: String) : Expression(),
    AssignableExpression {
    override fun evaluate(): Value {
        val lhs = lhsExpression.evaluate()
        return lhs.getMemberValue(memberIdentifier)
    }

    override fun assign(newValue: Value) {
        val lhs = lhsExpression.evaluate()
        lhs.setMemberValue(memberIdentifier, newValue)
    }
}