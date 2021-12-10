package io.github.kylemclean.scriptengine.ast.statements

import io.github.kylemclean.scriptengine.ast.expressions.Expression
import io.github.kylemclean.scriptengine.interpreter.Interpreter.Companion.interpreter

class ExpressionStatement(private val expression: Expression) : Statement() {
    override fun execute() {
        interpreter.lastExpressionStatementValue = expression.evaluate()
    }
}