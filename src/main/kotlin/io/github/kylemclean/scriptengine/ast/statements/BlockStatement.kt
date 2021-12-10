package io.github.kylemclean.scriptengine.ast.statements

import io.github.kylemclean.scriptengine.interpreter.Interpreter.Companion.interpreter

class BlockStatement(private val statements: List<Statement>) : Statement() {
    override fun execute() {
        interpreter.enterScope()
        for (statement in statements) {
            statement.execute()
            if (interpreter.currentStackFrame?.returnValue != null
                || interpreter.shouldBreak
                || interpreter.shouldContinue) {
                break
            }
        }
        interpreter.exitScope()
    }
}