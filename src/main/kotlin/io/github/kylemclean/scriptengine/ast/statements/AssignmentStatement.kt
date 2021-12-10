package io.github.kylemclean.scriptengine.ast.statements

import io.github.kylemclean.scriptengine.ast.expressions.AssignableExpression
import io.github.kylemclean.scriptengine.ast.expressions.Expression

class AssignmentStatement(private val srcExpression: Expression, private val dstExpression: AssignableExpression) :
    Statement() {
    override fun execute() {
        dstExpression.assign(srcExpression.evaluate())
    }
}