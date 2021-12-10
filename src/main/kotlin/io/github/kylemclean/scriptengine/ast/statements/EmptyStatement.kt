package io.github.kylemclean.scriptengine.ast.statements

object EmptyStatement : Statement() {
    override fun execute() {}
}