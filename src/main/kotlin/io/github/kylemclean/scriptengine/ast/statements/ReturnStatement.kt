package io.github.kylemclean.scriptengine.ast.statements

import io.github.kylemclean.scriptengine.ast.expressions.Expression
import io.github.kylemclean.scriptengine.interpreter.Interpreter.Companion.interpreter

class ReturnStatement(private val expression: Expression) : Statement() {
    override fun execute() {
        val returnValue = expression.evaluate()
        check(interpreter.currentStackFrame != null) { "cannot return when not in a function" }
        interpreter.currentStackFrame!!.returnValue = returnValue
    }
}