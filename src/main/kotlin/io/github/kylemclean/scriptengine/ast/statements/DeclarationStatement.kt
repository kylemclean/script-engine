package io.github.kylemclean.scriptengine.ast.statements

import io.github.kylemclean.scriptengine.ast.expressions.Expression
import io.github.kylemclean.scriptengine.interpreter.Interpreter.Companion.interpreter

class DeclarationStatement(private val identifier: String, private val expression: Expression) : Statement() {
    override fun execute() {
        interpreter.declareSymbol(identifier, expression.evaluate())
    }
}