package io.github.kylemclean.scriptengine.ast.statements

import io.github.kylemclean.scriptengine.interpreter.Interpreter.Companion.interpreter

object ContinueStmt : Statement() {
    override fun execute() {
        interpreter.shouldContinue = true
    }
}