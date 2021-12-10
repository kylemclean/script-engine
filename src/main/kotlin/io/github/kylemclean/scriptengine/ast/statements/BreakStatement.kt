package io.github.kylemclean.scriptengine.ast.statements

import io.github.kylemclean.scriptengine.interpreter.Interpreter.Companion.interpreter

object BreakStatement : Statement() {
    override fun execute() {
        interpreter.shouldBreak = true
    }
}